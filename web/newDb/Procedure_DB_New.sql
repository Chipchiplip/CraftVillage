USE [CraftDB]
GO

--PROCEDURE [AddAccount]
CREATE PROCEDURE [dbo].[AddAccount]
    @userName NVARCHAR(100),
    @password NVARCHAR(100), -- plain text password
    @email NVARCHAR(100),
    @address NVARCHAR(200) = NULL,
    @phoneNumber NVARCHAR(20) = NULL,
    @roleID INT = 1, -- Default: User
	@fullName NVARCHAR(100),
    @result INT OUTPUT  -- kết quả trả về
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra trùng username
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE userName = @userName)
        BEGIN
            SET @result = 2; -- Trùng username
            RETURN;
        END
        -- Kiểm tra trùng email
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE email = @email)
        BEGIN
            SET @result = 3; -- Trùng email
            RETURN;
        END
		-- Kiểm tra trùng sdt
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE phoneNumber = @phoneNumber)
        BEGIN
            SET @result = 4; -- Trùng sdt
            RETURN;
        END
        -- Nếu không trùng thì thêm mới
        INSERT INTO [dbo].[Account] (
            userName, password, email, address, phoneNumber,
            roleID, fullName
        )
        VALUES (
            @userName, dbo.HashPassword(@password), @email, @address,@phoneNumber, @roleID, @fullName
        );
        SET @result = 1; -- thành công
    END TRY
    BEGIN CATCH
        SET @result = 0; -- thất bại do lỗi hệ thống
    END CATCH
END
GO

--PROCEDURE [ChangePassword]
CREATE PROCEDURE [dbo].[ChangePassword]
    @userID INT,
    @oldPassword NVARCHAR(100),
    @newPassword NVARCHAR(100)
AS
BEGIN
    IF EXISTS (SELECT 1 FROM Account WHERE userID = @userID AND password = dbo.HashPassword(@oldPassword))
    BEGIN
        UPDATE Account
        SET password = dbo.HashPassword(@newPassword)
        WHERE userID = @userID;
        SELECT 'Password changed successfully' AS Result;
        RETURN 1;
    END
    ELSE
    BEGIN
        SELECT 'Current password is incorrect' AS Result;
        RETURN 0;
    END
END
GO
--PROCCEDURE [UpdateAccount]
CREATE PROCEDURE [dbo].[UpdateAccount]
    @userID INT,
    @email NVARCHAR(100),
    @address NVARCHAR(200) = NULL,
    @phoneNumber NVARCHAR(20) = NULL,
    @avatarUrl VARCHAR(MAX) = NULL,
	@fullName NVARCHAR(100),
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra user tồn tại
        IF NOT EXISTS (SELECT 1 FROM [dbo].[Account] WHERE userID = @userID)
        BEGIN
            SET @result = 2; -- Trung user
            RETURN;
        END
        -- Kiểm tra email đã được dùng bởi user khác chưa
        IF EXISTS (
            SELECT 1 FROM [dbo].[Account]
            WHERE email = @email AND userID != @userID
        )
        BEGIN
            SET @result = 3; -- Email bị trùng
            RETURN;
        END
		IF EXISTS (
            SELECT 1 FROM [dbo].[Account]
            WHERE phoneNumber = @phoneNumber AND userID != @userID
        )
        BEGIN
            SET @result = 4; -- SDT bị trùng
            RETURN;
        END
        -- Cập nhật thông tin cho user
        UPDATE [dbo].[Account]
        SET
            email = @email, address = @address, phoneNumber = @phoneNumber, avatarUrl = @avatarUrl, updatedDate = GETDATE() , fullName = @fullName
        WHERE userID = @userID;
        SET @result = 1; -- Thành công
    END TRY
    BEGIN CATCH
        SET @result = 0; -- Lỗi hệ thống
    END CATCH
END
GO
--PROCEDURE [AddCraftVillage]
CREATE PROCEDURE [dbo].[AddCraftVillage]
    @typeID INT,
    @villageName NVARCHAR(100),
    @description NVARCHAR(MAX) = NULL,
    @address NVARCHAR(200),
    @latitude FLOAT = NULL,
    @longitude FLOAT = NULL,
    @contactPhone NVARCHAR(20) = NULL,
    @contactEmail NVARCHAR(100) = NULL,
    @sellerId INT = NULL,
    @openingHours NVARCHAR(200) = NULL,
    @closingDays NVARCHAR(100) = NULL,
    @mainImageUrl VARCHAR(MAX) = NULL,
    @mapEmbedUrl VARCHAR(MAX) = NULL,
    @virtualTourUrl VARCHAR(MAX) = NULL,
    @history NVARCHAR(MAX) = NULL,
    @specialFeatures NVARCHAR(MAX) = NULL,
    @famousProducts NVARCHAR(MAX) = NULL,
    @culturalEvents NVARCHAR(MAX) = NULL,
    @craftProcess NVARCHAR(MAX) = NULL,
    @videoDescriptionUrl NVARCHAR(500) = NULL,
    @travelTips NVARCHAR(MAX) = NULL,
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        INSERT INTO [dbo].[CraftVillage] (
            typeID, villageName, description, address, latitude, longitude, contactPhone, contactEmail, status, clickCount, lastClicked,
            mainImageUrl, createdDate, sellerId, openingHours, closingDays, mapEmbedUrl, virtualTourUrl, history, specialFeatures,
            famousProducts, culturalEvents, craftProcess, videoDescriptionUrl, travelTips
        )
        VALUES (
            @typeID, @villageName, @description, @address, @latitude, @longitude, @contactPhone, @contactEmail, 2, 0, GETDATE(),
            @mainImageUrl, GETDATE(), @sellerId, @openingHours, @closingDays, @mapEmbedUrl, @virtualTourUrl, @history, @specialFeatures,
            @famousProducts, @culturalEvents, @craftProcess, @videoDescriptionUrl, @travelTips
        );
        SET @result = 1; -- Thành công
    END TRY
    BEGIN CATCH
        SET @result = 0; -- Lỗi
    END CATCH
END
GO

--PROCEDURE [DeleteCraftVillage]
CREATE PROCEDURE [dbo].[DeleteCraftVillage]
    @villageID INT,
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        DECLARE @hasProduct INT, @hasTicket INT;
        -- Kiểm tra có liên kết Product
        SELECT @hasProduct = COUNT(*) FROM [dbo].[Product] WHERE villageID = @villageID;
        -- Kiểm tra có liên kết Ticket
        SELECT @hasTicket = COUNT(*) FROM [dbo].[VillageTicket] WHERE villageID = @villageID;
        -- Nếu không có liên kết -> XÓA
        IF (@hasProduct = 0 AND @hasTicket = 0)
        BEGIN
            DELETE FROM [dbo].[CraftVillage] WHERE villageID = @villageID;
            SET @result = 1; -- Xoá thành công
        END
        ELSE
        BEGIN
            -- Nếu có liên kết -> cập nhật status = 0
            UPDATE [dbo].[CraftVillage] SET status = 0 WHERE villageID = @villageID;
            UPDATE [dbo].[Product] SET status = 0 WHERE villageID = @villageID;
            UPDATE [dbo].[VillageTicket] SET status = 0 WHERE villageID = @villageID;
            SET @result = 1; -- Cập nhật trạng thái thành công (có liên kết nên không xoá)
        END
    END TRY
    BEGIN CATCH
        SET @result = -1; -- Lỗi hệ thống
    END CATCH
END
Go 

--PROCEDURE [RegisterAccount]
CREATE PROCEDURE RegisterAccount
    @UserName NVARCHAR(100),
    @Password NVARCHAR(100),        -- dạng text, sẽ được hash trong procedure
    @Email NVARCHAR(100),
    @fullName NVARCHAR(100),
    @Address NVARCHAR(200) = NULL,
    @PhoneNumber NVARCHAR(20) = NULL,   
    @NewUserID INT OUTPUT 
AS
BEGIN
    SET NOCOUNT ON;
    -- 1. Kiểm tra trùng username
    IF EXISTS (SELECT 1 FROM Account WHERE userName = @UserName)
    BEGIN
        RAISERROR('Username already exists.', 16, 1);
        RETURN;
    END
    -- 2. Kiểm tra trùng email
    IF EXISTS (SELECT 1 FROM Account WHERE email = @Email)
    BEGIN
        RAISERROR('Email already exists.', 16, 1);
        RETURN;
    END
    -- 3. Tạo tài khoản mới, hash password ngay trong SQL
    INSERT INTO Account (
        userName, password, email, address, phoneNumber, fullName
    )
    VALUES (
        @UserName, dbo.HashPassword(@Password), @Email, @Address, @PhoneNumber, @fullName
    );
    -- 4. Trả về userID
    SET @NewUserID = SCOPE_IDENTITY();
END;
GO

--Login

CREATE PROCEDURE [dbo].[LoginAccount]
    @userNameOrEmail NVARCHAR(100),
    @password NVARCHAR(100)
AS
BEGIN
    DECLARE @hashedPassword VARBINARY(64) = dbo.HashPassword(@password);
    
    SELECT
        userID,
        userName,
        email,
		fullName,
        address,
        phoneNumber,
        roleID,
        status,
        createdDate,
        updatedDate
    FROM Account
    WHERE (userName = @userNameOrEmail OR email = @userNameOrEmail)
    AND password = @hashedPassword
    AND status = 1;
END
GO

--PROCEDURE [LoginByEmail] for Google OAuth
CREATE PROCEDURE [dbo].[LoginByEmail]
    @email NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    
    SELECT
        userID,
        userName,
        email,
        address,
        phoneNumber,
        roleID,
        status,
        createdDate,
        updatedDate,
		fullName
    FROM Account
    WHERE TRIM(email) = @email
    AND status = 1;
END
GO

CREATE PROCEDURE UpdateAccountFull
    @userID INT,
    @Username NVARCHAR(50),
    @Password NVARCHAR(255),
    @Email NVARCHAR(100),
    @PhoneNumber NVARCHAR(20),
    @Address NVARCHAR(255),
    @RoleID INT,
    @Status INT,
    @FullName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    -- Kiểm tra Email duy nhất (trừ chính account hiện tại)
    IF EXISTS (SELECT 1 FROM Account WHERE Email = @Email AND userID <> @userID)
    BEGIN
        RAISERROR('Email already exists.', 16, 1);
        RETURN 0;
    END
    -- Kiểm tra Phone duy nhất
    IF EXISTS (SELECT 1 FROM Account WHERE PhoneNumber = @PhoneNumber AND userID <> @userID)
    BEGIN
        RAISERROR('Phone number already exists.', 16, 1);
        RETURN 0;
    END
    -- Cập nhật tất cả thông tin
    UPDATE Account
    SET
        UserName = @Username,
        [Password] = [dbo].HashPassword(@Password),
        Email = @Email,
        PhoneNumber = @PhoneNumber,
        Address = @Address,
        RoleID = @RoleID,
        Status = @Status,
        FullName = @FullName,
        UpdatedDate = GETDATE()
    WHERE userID = @userID
	RETURN 1;
END
Go

CREATE PROCEDURE UpdateAccountWithoutPassword
    @userID INT,
    @Username NVARCHAR(50),
    @Email NVARCHAR(100),
    @PhoneNumber NVARCHAR(20),
    @Address NVARCHAR(255),
    @RoleID INT,
    @Status INT,
    @FullName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra Email duy nhất
    IF EXISTS (SELECT 1 FROM Account WHERE Email = @Email AND userID <> @userID)
    BEGIN
        RAISERROR('Email already exists.', 16, 1);
        RETURN 0;
    END

    -- Kiểm tra Phone duy nhất
    IF EXISTS (SELECT 1 FROM Account WHERE PhoneNumber = @PhoneNumber AND userID <> @userID)
    BEGIN
        RAISERROR('Phone number already exists.', 16, 1);
        RETURN 0;
    END

    -- Cập nhật không thay đổi mật khẩu
    UPDATE Account
    SET
        UserName = @Username,
        Email = @Email,
        PhoneNumber = @PhoneNumber,
        Address = @Address,
        RoleID = @RoleID,
        Status = @Status,
        FullName = @FullName,
        UpdatedDate = GETDATE()
    WHERE userID = @userID
	return 1;
END
Go
--PROCEDURE [AddAccountFull]
CREATE PROCEDURE [dbo].[AddAccountFull]
    @userName NVARCHAR(100),
    @password NVARCHAR(100), -- plain text password
    @email NVARCHAR(100),
	@FullName  NVARCHAR(100), 
    @phoneNumber NVARCHAR(20) = NULL,
	@address NVARCHAR(200) = NULL,
    @roleID INT , 
	@status INT ,
    @result INT OUTPUT  -- kết quả trả về
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra trùng username
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE userName = @userName)
        BEGIN
            SET @result = 2; -- Trùng username
            RETURN;
        END
        -- Kiểm tra trùng email
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE email = @email)
        BEGIN
            SET @result = 3; -- Trùng email
            RETURN;
        END
		-- Kiểm tra trùng sdt
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE phoneNumber = @phoneNumber)
        BEGIN
            SET @result = 4; -- Trùng sdt
            RETURN;
        END
        -- Nếu không trùng thì thêm mới
        INSERT INTO [dbo].[Account] (
            userName, password, email, address, phoneNumber, roleID, status, FullName
        )
        VALUES (
            @userName, dbo.HashPassword(@password), @email, @address,@phoneNumber, @roleID, @status, @FullName
        );
        SET @result = 1; -- thành công
    END TRY
    BEGIN CATCH
        SET @result = 0; -- thất bại do lỗi hệ thống
    END CATCH
END
GO

CREATE PROCEDURE [dbo].[UpdateProductFull]
    @pid INT,
    @name NVARCHAR(100),
    @price DECIMAL(10, 2),
    @description NVARCHAR(MAX) = NULL,
    @stock INT,
    @stockAdd INT = NULL,
    @status INT,
    @villageID INT,
    @categoryID INT,
    @mainImageUrl VARCHAR(MAX) = NULL,
    @craftTypeID INT = NULL,
    @sku VARCHAR(50) = NULL,
    @weight DECIMAL(10, 2) = NULL,
    @dimensions NVARCHAR(100) = NULL,
    @materials NVARCHAR(500) = NULL,
    @careInstructions NVARCHAR(MAX) = NULL,
    @warranty NVARCHAR(200) = NULL,
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra trùng tên sản phẩm (nhưng phải khác chính sản phẩm đang sửa)
        IF EXISTS (
            SELECT 1 
            FROM [dbo].[Product] 
            WHERE [name] = @name AND pid <> @pid
        )
        BEGIN
            SET @result = 2; -- Trùng tên sản phẩm khác
            RETURN;
        END

        -- Cập nhật sản phẩm
        UPDATE [dbo].[Product]
        SET 
            [name] = @name,
            [price] = @price,
            [description] = @description,
            [stock] = @stock + @stockAdd,
            [status] = @status,
            [villageID] = @villageID,
            [categoryID] = @categoryID,
            [mainImageUrl] = @mainImageUrl,
            [craftTypeID] = @craftTypeID,
            [sku] = @sku,
            [weight] = @weight,
            [dimensions] = @dimensions,
            [materials] = @materials,
            [careInstructions] = @careInstructions,
            [warranty] = @warranty,
            [updatedDate] = GETDATE()
        WHERE pid = @pid;

        -- Kiểm tra nếu không có dòng nào bị ảnh hưởng
        IF @@ROWCOUNT = 0
        BEGIN
            SET @result = 0; -- Không tìm thấy sản phẩm
            RETURN;
        END

        SET @result = 1; -- Thành công
    END TRY
    BEGIN CATCH
        SET @result = 0; -- Lỗi hệ thống
    END CATCH
END
GO

CREATE PROCEDURE CreateProductFull
    @Name NVARCHAR(255),
    @Price DECIMAL(18, 2),
    @Description NVARCHAR(MAX),
    @Stock INT,
    @Status INT,
    @VillageID INT,
    @CategoryID INT,
    @MainImageUrl NVARCHAR(500),
    @CraftTypeID INT,
    @Sku NVARCHAR(100),
    @Weight DECIMAL(10, 2),
    @Dimensions NVARCHAR(100),
    @Materials NVARCHAR(500),
    @CareInstructions NVARCHAR(1000),
    @Warranty NVARCHAR(255),
    @Result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Kiểm tra trùng tên sản phẩm (không phân biệt hoa thường)
        IF EXISTS (
            SELECT 1 FROM Product WHERE LOWER(Name) = LOWER(@Name)
        )
        BEGIN
            SET @Result = -1; -- Tên đã tồn tại
            RETURN;
        END

        -- Thêm sản phẩm mới
        INSERT INTO Product (
            Name, Price,  Description, Stock,  Status,  VillageID, CategoryID,  MainImageUrl,
            CraftTypeID, SKU,  Weight, Dimensions, Materials, CareInstructions,  Warranty,  createdDate
        )
        VALUES (
            @Name, @Price,  @Description,  @Stock, @Status,  @VillageID,  @CategoryID, @MainImageUrl,
            @CraftTypeID, @Sku, @Weight, @Dimensions, @Materials, @CareInstructions, @Warranty, GETDATE()
        );

        SET @Result = 1; -- Thành công
    END TRY
    BEGIN CATCH
        SET @Result = 0; -- Lỗi SQL khác
    END CATCH
END;
GO

CREATE PROCEDURE DeleteProductByAdmin
    @pid INT,
    @Result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    IF EXISTS (
        SELECT 1
        FROM OrderDetail
        WHERE product_id = @pid
    )
    BEGIN
        -- Sản phẩm có tồn tại trong OrderDetail → chỉ update status = 0
        UPDATE Product
        SET status = 0
        WHERE pid = @pid;
        SET @Result = 0;
    END
    ELSE
    BEGIN
        -- Sản phẩm chưa từng bán → xóa luôn
        DELETE FROM Product
        WHERE pid = @pid;
        SET @Result = 1;
    END
END
GO

-- Them SellerVerification
CREATE PROCEDURE sp_InsertSellerVerification_Individual
    @sellerID INT,
    @businessType NVARCHAR(100),
    @businessVillageCategry NVARCHAR(200),
    @businessVillageName NVARCHAR(200),
    @businessVillageAddress NVARCHAR(500),
    @productProductCategory NVARCHAR(200),
    @profileVillagePictureUrl VARCHAR(MAX),
    @contactPerson NVARCHAR(200),
    @contactPhone NVARCHAR(20),
    @contactEmail NVARCHAR(200),
    @idCardNumber NVARCHAR(50),
    @idCardFrontUrl VARCHAR(MAX),
    @idCardBackUrl VARCHAR(MAX),
    @note NVARCHAR(MAX)
AS
BEGIN
    -- Check for duplicate
    IF EXISTS (
        SELECT 1
        FROM SellerVerification
        WHERE businessVillageName = @businessVillageName and verificationStatus = 1
    )
    BEGIN
        RETURN 0; -- Duplicate found
    END

    -- Insert if no duplicate
    INSERT INTO SellerVerification
    (
        sellerID,
        businessType,
        businessVillageCategry,
        businessVillageName,
        businessVillageAddress,
        productProductCategory,
        profileVillagePictureUrl,
        contactPerson,
        contactPhone,
        contactEmail,
        idCardNumber,
        idCardFrontUrl,
        idCardBackUrl,
        note
    )
    VALUES
    (
        @sellerID,
        @businessType,
        @businessVillageCategry,
        @businessVillageName,
        @businessVillageAddress,
        @productProductCategory,
        @profileVillagePictureUrl,
        @contactPerson,
        @contactPhone,
        @contactEmail,
        @idCardNumber,
        @idCardFrontUrl,
        @idCardBackUrl,
        @note
    )

    RETURN 1; -- Success
END
GO

CREATE PROCEDURE sp_InsertSellerVerification_CraftVillage
    @sellerID INT,
    @businessType NVARCHAR(100),
    @businessVillageCategry NVARCHAR(200),
    @businessVillageName NVARCHAR(200),
    @businessVillageAddress NVARCHAR(500),
    @productProductCategory NVARCHAR(200),
    @profileVillagePictureUrl VARCHAR(MAX),
    @contactPerson NVARCHAR(200),
    @contactPhone NVARCHAR(20),
    @contactEmail NVARCHAR(200),
    @businessLicense NVARCHAR(100),
    @taxCode NVARCHAR(50),
    @documentUrl VARCHAR(MAX),
    @note NVARCHAR(MAX)
AS
BEGIN
    -- Check for duplicate
    IF EXISTS (
        SELECT 1
        FROM SellerVerification
        WHERE businessVillageName = @businessVillageName and verificationStatus = 1
    )
    BEGIN
        RETURN 0; -- Duplicate found
    END

    -- Insert if no duplicate
    INSERT INTO SellerVerification
    (
        sellerID,
        businessType,
        businessVillageCategry,
        businessVillageName,
        businessVillageAddress,
        productProductCategory,
        profileVillagePictureUrl,
        contactPerson,
        contactPhone,
        contactEmail,
        businessLicense,
        taxCode,
        documentUrl,
        note
    )
    VALUES
    (
        @sellerID,
        @businessType,
        @businessVillageCategry,
        @businessVillageName,
        @businessVillageAddress,
        @productProductCategory,
        @profileVillagePictureUrl,
        @contactPerson,
        @contactPhone,
        @contactEmail,
        @businessLicense,
        @taxCode,
        @documentUrl,
        @note
    )

    RETURN 1; -- Success
END
GO

CREATE PROCEDURE sp_ApprovedUpgradeAccount
(
    @verificationID INT,
    @sellerID INT,
    @businessVillageCategry NVARCHAR(200),
    @businessVillageName NVARCHAR(200),
    @businessVillageAddress NVARCHAR(500),
    @productProductCategory NVARCHAR(200),
    @profileVillagePictureUrl NVARCHAR(MAX),
    @contactPerson NVARCHAR(200),
    @contactPhone NVARCHAR(20),
    @contactEmail NVARCHAR(200),
    @verificationStatus INT,
    @verifiedBy INT,
    @newVillageID INT OUTPUT
)
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        BEGIN TRANSACTION;

        DECLARE @typeID INT;
        DECLARE @villageID INT;
        DECLARE @categoryID INT;

        -- 1. Tìm hoặc thêm mới CraftType
        SELECT TOP 1 @typeID = typeID
        FROM dbo.CraftType
        WHERE typeName COLLATE Latin1_General_CI_AI = @businessVillageCategry COLLATE Latin1_General_CI_AI;

        IF @typeID IS NULL
        BEGIN
            INSERT INTO dbo.CraftType (typeName)
            VALUES (@businessVillageCategry);

            SET @typeID = SCOPE_IDENTITY();
        END

        -- 2. Kiểm tra trùng tên CraftVillage (không phân biệt dấu)
        IF EXISTS (
            SELECT 1
            FROM dbo.CraftVillage
            WHERE villageName COLLATE Latin1_General_CI_AI = @businessVillageName COLLATE Latin1_General_CI_AI
        )
        BEGIN
            ROLLBACK TRANSACTION;
            RETURN -2; -- Trùng tên làng nghề
        END

        -- 3. Thêm mới CraftVillage
        INSERT INTO dbo.CraftVillage
        (
            typeID,
            villageName,
            address,
            contactPhone,
            contactEmail,
            status,
            mainImageUrl,
            sellerId
        )
        VALUES
        (
            @typeID,
            @businessVillageName,
            @businessVillageAddress,
            @contactPhone,
            @contactEmail,
            1, -- status = active
            @profileVillagePictureUrl,
            @sellerID
        );

        SET @villageID = SCOPE_IDENTITY();
        SET @newVillageID = @villageID;

        -- 4. Tìm hoặc thêm mới ProductCategory
        SELECT TOP 1 @categoryID = categoryID
        FROM dbo.ProductCategory
        WHERE categoryName COLLATE Latin1_General_CI_AI = @productProductCategory COLLATE Latin1_General_CI_AI;

        IF @categoryID IS NULL
        BEGIN
            INSERT INTO dbo.ProductCategory (categoryName)
            VALUES (@productProductCategory);

            SET @categoryID = SCOPE_IDENTITY();
        END

        -- 5. Update SellerVerification
        UPDATE dbo.SellerVerification
        SET 
            verificationStatus = @verificationStatus,
            verifiedBy = @verifiedBy,
            verifiedDate = GETDATE()
        WHERE
            verificationID = @verificationID;

        IF @@ROWCOUNT = 0
        BEGIN
            ROLLBACK TRANSACTION;
            RETURN -3; -- Không tìm thấy verificationID
        END

        -- 6. Update Account role
        UPDATE dbo.Account
        SET 
            roleID = 2
        WHERE
            userID = @sellerID;

        IF @@ROWCOUNT = 0
        BEGIN
            ROLLBACK TRANSACTION;
            RETURN -4; -- Không tìm thấy sellerID trong Account
        END

        COMMIT TRANSACTION;

        RETURN 1; -- Thành công
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        RETURN -1; -- Lỗi tổng quát
    END CATCH
END
GO

CREATE PROCEDURE sp_RejectedUpgradeAccount
(
    @verificationID INT,
    @verificationStatus INT,
    @verifiedBy INT,
    @rejectReason NVARCHAR(MAX) = NULL
)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.SellerVerification
    SET 
        verificationStatus = @verificationStatus,
        verifiedBy = @verifiedBy,
        verifiedDate = GETDATE(),
        rejectReason = @rejectReason
    WHERE
        verificationID = @verificationID;
    -- Kiểm tra có cập nhật được bản ghi nào không
    IF @@ROWCOUNT > 0
        RETURN 1;      -- thành công
    ELSE
        RETURN 0;      -- không thành công
END
GO

--Add New Ticket
CREATE OR ALTER PROCEDURE AddTicketByAdmin
(
    @villageID INT,
    @typeID INT,
    @price DECIMAL(10,2),
    @status INT
)
AS
BEGIN
    SET NOCOUNT ON;

    -- Check if ticket already exists
    IF EXISTS (
        SELECT 1 
        FROM VillageTicket 
        WHERE villageID = @villageID 
          AND typeID = @typeID
    )
    BEGIN
        -- Return 0 → fail because ticket exists
        RETURN 0;
    END

    -- Insert new Ticket
    INSERT INTO VillageTicket (villageID, typeID, price, status, createdDate)
    VALUES (@villageID, @typeID, @price, @status, GETDATE());

    -- Return 1 → success
    RETURN 1;
END
GO

--Update Ticket
CREATE PROCEDURE UpdateTicketByAdmin
(
    @ticketID INT,
    @price DECIMAL(10, 2),
    @status INT
)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE VillageTicket
    SET
        price = @price,
        status = @status,
        updatedDate = GETDATE()
    WHERE ticketID = @ticketID;
    RETURN 1;
END
GO

--Delete Ticket
CREATE PROCEDURE DeleteTicketByAdmin
(
    @ticketID INT
)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE VillageTicket
    SET
        status = 0,
        updatedDate = GETDATE()
    WHERE ticketID = @ticketID;
    RETURN 1;
END
GO
-------------------------------------------------------------------------------------------
CREATE PROCEDURE sp_ResponseProductReviewByAdmin
    @reviewID INT,
    @response NVARCHAR(MAX),
    @result INT OUTPUT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM ProductReview WHERE reviewID = @reviewID)
    BEGIN
        UPDATE ProductReview
        SET response = @response,
            responseDate = GETDATE()
        WHERE reviewID = @reviewID;

        SET @result = 1; -- Thành công
    END
    ELSE
    BEGIN
        SET @result = 0; -- Không tồn tại review
    END
END
Go

CREATE PROCEDURE sp_DeleteProductReviewByAdmin
    @reviewID INT,
    @result INT OUTPUT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM ProductReview WHERE reviewID = @reviewID)
    BEGIN
        DELETE FROM ProductReview WHERE reviewID = @reviewID;
        SET @result = 1; -- Thành công
    END
    ELSE
    BEGIN
        SET @result = 0; -- Không tìm thấy review để xóa
    END
END
go

CREATE PROCEDURE sp_ResponseVillageReviewByAdmin
    @reviewID INT,
    @response NVARCHAR(MAX),
    @result INT OUTPUT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM VillageReview WHERE reviewID = @reviewID)
    BEGIN
        UPDATE VillageReview
        SET response = @response,
            responseDate = GETDATE()
        WHERE reviewID = @reviewID;

        SET @result = 1; -- Thành công
    END
    ELSE
    BEGIN
        SET @result = 0; -- Không tồn tại review
    END
END
go

CREATE PROCEDURE sp_DeleteVillageReviewByAdmin
    @reviewID INT,
    @result INT OUTPUT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM VillageReview WHERE reviewID = @reviewID)
    BEGIN
        DELETE FROM VillageReview WHERE reviewID = @reviewID;
        SET @result = 1; -- Thành công
    END
    ELSE
    BEGIN
        SET @result = 0; -- Không tìm thấy review để xóa
    END
END
go
CREATE PROCEDURE UpdateVillageFullByAdmin
    @villageID int,
    @villageName nvarchar(255),
    @typeID int,
    @description nvarchar(max),
    @address nvarchar(max),
    @latitude float,
    @longitude float,
    @contactPhone nvarchar(100),
    @contactEmail nvarchar(200),
    @status int,
    @sellerId int,
    @openingHours nvarchar(255),
    @closingDays nvarchar(255),
    @mapEmbedUrl nvarchar(max),
    @virtualTourUrl nvarchar(max),
    @history nvarchar(max),
    @specialFeatures nvarchar(max),
    @famousProducts nvarchar(max),
    @culturalEvents nvarchar(max),
    @craftProcess nvarchar(max),
    @videoDescriptionUrl nvarchar(500),
    @travelTips nvarchar(max),
    @mainImageUrl nvarchar(500),
    @result int OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra tên không trùng (không phân biệt hoa thường, không dấu)
    IF EXISTS (
        SELECT 1
        FROM CraftVillage
        WHERE
            villageID <> @villageID
            AND villageName COLLATE Latin1_General_CI_AI
                = @villageName COLLATE Latin1_General_CI_AI
    )
    BEGIN
        -- Trùng tên với village khác → không update
        SET @result = 0;
        RETURN;
    END

    -- Thực hiện UPDATE
    UPDATE CraftVillage
    SET
        villageName = @villageName,
        typeID = @typeID,
        description = @description,
        address = @address,
        latitude = @latitude,
        longitude = @longitude,
        contactPhone = @contactPhone,
        contactEmail = @contactEmail,
        status = @status,
        sellerId = @sellerId,
        openingHours = @openingHours,
        closingDays = @closingDays,
        mapEmbedUrl = @mapEmbedUrl,
        virtualTourUrl = @virtualTourUrl,
        history = @history,
        specialFeatures = @specialFeatures,
        famousProducts = @famousProducts,
        culturalEvents = @culturalEvents,
        craftProcess = @craftProcess,
        videoDescriptionUrl = @videoDescriptionUrl,
        travelTips = @travelTips,
        mainImageUrl = @mainImageUrl,
        updatedDate = GETDATE()
    WHERE villageID = @villageID;

    SET @result = 1;
END;
go


CREATE PROCEDURE DeleteVillageByAdmin
    @villageID INT,
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @existsInProduct BIT = 0;
    DECLARE @existsInTicket BIT = 0;

    -- Kiểm tra xem villageID có tồn tại trong Product
    IF EXISTS (
        SELECT 1
        FROM Product
        WHERE villageID = @villageID
    )
    BEGIN
        SET @existsInProduct = 1;
    END

    -- Kiểm tra xem villageID có tồn tại trong VillageTicket
    IF EXISTS (
        SELECT 1
        FROM VillageTicket
        WHERE villageID = @villageID
    )
    BEGIN
        SET @existsInTicket = 1;
    END

    -- Nếu có xuất hiện ở Product hoặc VillageTicket
    IF @existsInProduct = 1 OR @existsInTicket = 1
    BEGIN
        -- Update status = 0 trong Product
        UPDATE Product
        SET status = 0
        WHERE villageID = @villageID;

        -- Update status = 0 trong VillageTicket
        UPDATE VillageTicket
        SET status = 0
        WHERE villageID = @villageID;

		UPDATE CraftVillage
        SET status = 0
        WHERE villageID = @villageID;

        -- Không xóa CraftVillage
        SET @result = 2; -- 2 = Chỉ update status, không xóa
    END
    ELSE
    BEGIN
        -- Không tồn tại ở các bảng → XÓA hẳn CraftVillage
        DELETE FROM CraftVillage
        WHERE villageID = @villageID;

        SET @result = 1; -- 1 = Xóa thành công
    END
END;
go


CREATE PROCEDURE AddVillageFullByAdmin
    @villageName nvarchar(255),
    @typeID int,
    @description nvarchar(max),
    @address nvarchar(max),
    @latitude float,
    @longitude float,
    @contactPhone nvarchar(100),
    @contactEmail nvarchar(200),
    @status int,
    @sellerId int,
    @openingHours nvarchar(255),
    @closingDays nvarchar(255),
    @mapEmbedUrl nvarchar(max),
    @virtualTourUrl nvarchar(max),
    @history nvarchar(max),
    @specialFeatures nvarchar(max),
    @famousProducts nvarchar(max),
    @culturalEvents nvarchar(max),
    @craftProcess nvarchar(max),
    @videoDescriptionUrl nvarchar(500),
    @travelTips nvarchar(max),
    @mainImageUrl nvarchar(500),
    @result int OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    -- Kiểm tra villageName đã tồn tại chưa (bỏ dấu, không phân biệt hoa thường)
    IF EXISTS (
        SELECT 1
        FROM CraftVillage
        WHERE
            LTRIM(RTRIM(villageName)) COLLATE Latin1_General_CI_AI
            = LTRIM(RTRIM(@villageName)) COLLATE Latin1_General_CI_AI
    )
    BEGIN
        -- Trùng tên → không thêm
        SET @result = 0;
        RETURN;
    END

    -- Nếu chưa tồn tại thì thêm mới
    INSERT INTO CraftVillage (
        villageName,
        typeID,
        description,
        address,
        latitude,
        longitude,
        contactPhone,
        contactEmail,
        status,
        sellerId,
        openingHours,
        closingDays,
        mapEmbedUrl,
        virtualTourUrl,
        history,
        specialFeatures,
        famousProducts,
        culturalEvents,
        craftProcess,
        videoDescriptionUrl,
        travelTips,
        mainImageUrl,
        createdDate
    )
    VALUES (
        @villageName,
        @typeID,
        @description,
        @address,
        @latitude,
        @longitude,
        @contactPhone,
        @contactEmail,
        @status,
        @sellerId,
        @openingHours,
        @closingDays,
        @mapEmbedUrl,
        @virtualTourUrl,
        @history,
        @specialFeatures,
        @famousProducts,
        @culturalEvents,
        @craftProcess,
        @videoDescriptionUrl,
        @travelTips,
        @mainImageUrl,
        GETDATE()
    );

    SET @result = 1;
END;
GO

-- 3. BỔ SUNG HOẶC CẬP NHẬT STORED PROCEDURE CÒN THIẾU/CHƯA ĐÚNG
IF OBJECT_ID('[dbo].[AddTicketToCart]', 'P') IS NOT NULL DROP PROCEDURE [dbo].[AddTicketToCart];
GO
CREATE PROCEDURE [dbo].[AddTicketToCart]
    @cartID INT,
    @ticketID INT,
    @quantity INT,
    @ticketDate DATE
AS
BEGIN
    SET NOCOUNT ON;
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    BEGIN TRY
        BEGIN TRANSACTION;
        DECLARE @availableSlots INT;
        DECLARE @currentCartQuantity INT = 0;
        DECLARE @maxBookingDate DATE = DATEADD(DAY, 30, GETDATE());
        -- Validate booking date
        IF @ticketDate > @maxBookingDate OR @ticketDate < CAST(GETDATE() AS DATE)
        BEGIN
            RAISERROR('Invalid booking date', 16, 1);
            RETURN;
        END
        -- Get current quantity in cart for this ticket and date
        SELECT @currentCartQuantity = ISNULL(quantity, 0)
        FROM [CartTicket]
        WHERE cartID = @cartID AND ticketID = @ticketID AND ticketDate = @ticketDate;
        -- Get available slots
        SELECT @availableSlots = availableSlots 
        FROM [TicketAvailability] 
        WHERE ticketID = @ticketID AND availableDate = @ticketDate AND status = 1;
        -- Create availability record if not exists
        IF @availableSlots IS NULL
        BEGIN
            INSERT INTO [TicketAvailability] (ticketID, availableDate, totalSlots, bookedSlots)
            VALUES (@ticketID, @ticketDate, 20, 0);
            SET @availableSlots = 20;
        END
        -- Check if we can add the requested quantity
        IF (@availableSlots + @currentCartQuantity) < @quantity
        BEGIN
            RAISERROR('Not enough tickets available', 16, 1);
            RETURN;
        END
        -- Update or insert cart item
        IF @currentCartQuantity > 0
        BEGIN
            UPDATE [CartTicket] 
            SET quantity = @quantity, updatedDate = GETDATE()
            WHERE cartID = @cartID AND ticketID = @ticketID AND ticketDate = @ticketDate;
        END
        ELSE
        BEGIN
            INSERT INTO [CartTicket] (cartID, ticketID, quantity, ticketDate)
            VALUES (@cartID, @ticketID, @quantity, @ticketDate);
        END
        COMMIT TRANSACTION;
        SELECT 1 AS Success, 'Ticket added to cart successfully' AS Message;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SELECT 0 AS Success, ERROR_MESSAGE() AS Message;
    END CATCH
END
GO

IF OBJECT_ID('[dbo].[ReserveTickets]', 'P') IS NOT NULL DROP PROCEDURE [dbo].[ReserveTickets];
GO
CREATE PROCEDURE [dbo].[ReserveTickets]
    @cartID INT
AS
BEGIN
    SET NOCOUNT ON;
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    BEGIN TRY
        BEGIN TRANSACTION;
        -- Reserve all tickets in cart
        UPDATE ta
        SET bookedSlots = ta.bookedSlots + ct.quantity,
            updatedDate = GETDATE()
        FROM [TicketAvailability] ta
        INNER JOIN [CartTicket] ct ON ta.ticketID = ct.ticketID AND ta.availableDate = ct.ticketDate
        WHERE ct.cartID = @cartID;
        -- Clear cart tickets after reservation
        DELETE FROM [CartTicket] WHERE cartID = @cartID;
        COMMIT TRANSACTION;
        SELECT 1 AS Success, 'Tickets reserved successfully' AS Message;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        SELECT 0 AS Success, ERROR_MESSAGE() AS Message;
    END CATCH
END
GO

IF OBJECT_ID('[dbo].[CheckTicketAvailability]', 'P') IS NOT NULL DROP PROCEDURE [dbo].[CheckTicketAvailability];
GO
CREATE PROCEDURE [dbo].[CheckTicketAvailability]
    @ticketID INT,
    @requestedDate DATE,
    @quantity INT
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @availableSlots INT = 0;
    DECLARE @maxBookingDate DATE = DATEADD(DAY, 30, GETDATE());
    -- Check if date is within 30 days
    IF @requestedDate > @maxBookingDate
    BEGIN
        SELECT 0 AS IsAvailable, 'Booking date exceeds 30-day limit' AS Message;
        RETURN;
    END
    -- Check if date is in the past
    IF @requestedDate < CAST(GETDATE() AS DATE)
    BEGIN
        SELECT 0 AS IsAvailable, 'Cannot book for past dates' AS Message;
        RETURN;
    END
    -- Get or create availability record
    SELECT @availableSlots = availableSlots 
    FROM [TicketAvailability] 
    WHERE ticketID = @ticketID AND availableDate = @requestedDate AND status = 1;
    -- If no record exists, create one with default 20 slots
    IF @availableSlots IS NULL
    BEGIN
        INSERT INTO [TicketAvailability] (ticketID, availableDate, totalSlots, bookedSlots)
        VALUES (@ticketID, @requestedDate, 20, 0);
        SET @availableSlots = 20;
    END
    -- Check availability
    IF @availableSlots >= @quantity
    BEGIN
        SELECT 1 AS IsAvailable, 
               @availableSlots AS AvailableSlots,
               'Tickets available' AS Message;
    END
    ELSE
    BEGIN
        SELECT 0 AS IsAvailable, 
               @availableSlots AS AvailableSlots,
               CONCAT('Only ', @availableSlots, ' tickets available') AS Message;
    END
END
GO

CREATE PROCEDURE sp_addVillageReview
    @villageID INT,
    @userID INT,
    @reviewText NVARCHAR(MAX),
    @rating INT,
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Insert review
        INSERT INTO VillageReview (VillageID, UserID, ReviewText, Rating, ReviewDate)
        VALUES (@villageID, @userID, @reviewText, @rating, GETDATE());

        DECLARE @oldTotal INT;
        DECLARE @oldAvg FLOAT;
        DECLARE @newTotal INT;
        DECLARE @newAvg FLOAT;

        -- Lấy giá trị cũ
        SELECT @oldTotal = totalReviews, @oldAvg = averageRating
        FROM CraftVillage
        WHERE villageID = @villageID;

        -- Tính toán giá trị mới
        SET @newTotal = ISNULL(@oldTotal,0) + 1;

        IF @oldTotal > 0
            SET @newAvg = (@oldAvg * @oldTotal + @rating) / @newTotal;
        ELSE
            SET @newAvg = @rating * 1.0;

        -- Update lại CraftVillage
        UPDATE CraftVillage
        SET
            totalReviews = @newTotal,
            averageRating = @newAvg
        WHERE villageID = @villageID;

        SET @result = 1;
    END TRY
    BEGIN CATCH
        SET @result = 0;
    END CATCH
END
Go
--Chat User - Seller
CREATE PROCEDURE addNewMessageThread
    @userID INT,
    @sellerID INT,
    @messageName NVARCHAR(255),
    @result INT OUTPUT
AS
BEGIN
    -- Kiểm tra tồn tại
    IF EXISTS (
        SELECT 1
        FROM MessageThread
        WHERE userID = @userID
          AND sellerID = @sellerID
          AND messageName = @messageName
    )
    BEGIN
        -- Đã tồn tại → trả 0
        SET @result = 0;
    END
    ELSE
    BEGIN
        BEGIN TRY
            INSERT INTO MessageThread (userID, sellerID, messageName)
            VALUES (@userID, @sellerID, @messageName);
            
            IF @@ROWCOUNT > 0
                SET @result = 1;
            ELSE
                SET @result = 0;
        END TRY
        BEGIN CATCH
            -- Có lỗi khi INSERT → trả 0
            SET @result = 0;
        END CATCH
    END
END
go

CREATE PROCEDURE sendMessage
    @threadID INT,
    @senderID INT,
    @messageContent NVARCHAR(MAX),
    @attachmentUrl VARCHAR(MAX) = NULL,
    @result INT OUTPUT
AS
BEGIN
    BEGIN TRY
        INSERT INTO [dbo].[Message] (
            threadID,
            senderID,
            messageContent,
            attachmentUrl,
            sentDate
        )
        VALUES (
            @threadID,
            @senderID,
            @messageContent,
            @attachmentUrl,
            GETDATE()
        );
        IF @@ROWCOUNT > 0
            SET @result = 1;
        ELSE
            SET @result = 0;
    END TRY
    BEGIN CATCH
        -- Có lỗi xảy ra
        SET @result = 0;
    END CATCH
END
GO