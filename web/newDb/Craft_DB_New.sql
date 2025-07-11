
CREATE DATABASE [CraftDB]
GO

USE [CraftDB]
GO

-- Create Password Hashing Function
CREATE FUNCTION [dbo].[HashPassword](@password NVARCHAR(100))
RETURNS VARBINARY(64)
AS
BEGIN
    RETURN HASHBYTES('SHA2_512', @password);
END
GO

----------------------------------------------------Account-------------
--Table [Role] -- Tạo thêm bảng Role để mô tả về Role
CREATE TABLE [dbo].[Role](
	[roleID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[roleName] [nvarchar](100) NOT NULL,
	[description] [nvarchar](max) NULL,
	[status] [int] NOT NULL DEFAULT(1),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE()
)
GO

--Table [Account] -- THEM UNIQUE CHO USERNAME, EMAIL
CREATE TABLE [dbo].[Account](
	[userID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userName] [nvarchar](100) UNIQUE NOT NULL,
	[password] [varbinary](64) NOT NULL,
	[email] [nvarchar](100) UNIQUE NOT NULL,
	[address] [nvarchar](200) NULL,
	[phoneNumber] [nvarchar](20) NULL,
	[status] [int] NOT NULL DEFAULT(1), --1: hoạt động, 0: bị chặn 
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	[roleID] [int] NOT NULL DEFAULT(1) CHECK (([roleID]=(3) OR [roleID]=(2) OR [roleID]=(1))), --User: 1 - Seller: 2, Admin: 3
	[isEmailVerified] [bit] NOT NULL DEFAULT(0), 
	[lastLoginDate] [datetime] NULL,
	[loginAttempts] [int] NOT NULL DEFAULT(0),
	[lockedUntil] [datetime] NULL, 
	[avatarUrl] [varchar](max) NULL,
	[preferredLanguage] [varchar](10) DEFAULT('vi'),
	CONSTRAINT FK_Account_Role FOREIGN KEY (roleID) REFERENCES [dbo].[Role](roleID)
)
GO
ALTER TABLE Account
ADD fullName NVARCHAR(100);

--Table [EmailVerification] 
CREATE TABLE [dbo].[EmailVerification](
	[verificationID] [int] IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[verificationToken] [varchar](100) NOT NULL UNIQUE,
	[expiryDate] [datetime] NOT NULL,
	[isUsed] [bit] NOT NULL DEFAULT(0),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_EmailVerification_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [PasswordReset]
CREATE TABLE [dbo].[PasswordReset](
	[resetID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[resetToken] [varchar](100) NOT NULL UNIQUE,
	[expiryDate] [datetime] NOT NULL,
	[isUsed] [bit] NOT NULL DEFAULT(0),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[usedDate] [datetime] NULL,
	CONSTRAINT [FK_PasswordReset_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO
----------------------------------------------------Craft Village-------------
--Table [CraftType] 
CREATE TABLE [dbo].[CraftType](
	[typeID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[typeName] [nvarchar](100) NOT NULL UNIQUE,
	[description] [nvarchar](max) NULL,
	[status] [int] NOT NULL DEFAULT(1), --1: hoạt động, 0: ẩn
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL
)
GO

-- Table [CraftVillage]
CREATE TABLE [dbo].[CraftVillage](
	[villageID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL ,
	[typeID] [int],
	[villageName] [nvarchar](100) NOT NULL,
	[description] [nvarchar](max) NULL,
	[address] [nvarchar](200) NOT NULL,
	[latitude] [float] NULL,
	[longitude] [float] NULL,
	[contactPhone] [nvarchar](20) NULL,
	[contactEmail] [nvarchar](100) NULL,
	[status] [int] NOT NULL, -- 0: đang ẩn, 1 đang hoạt động, 2: đang duyệt
	[clickCount] [int] NOT NULL DEFAULT(0),
	[lastClicked] [datetime] NULL DEFAULT GETDATE(),
	[mainImageUrl] [varchar](max) NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	[sellerId] [int] NULL,
	[openingHours] [nvarchar](200) NULL,
	[closingDays] [nvarchar](100) NULL,
	[averageRating] [decimal](3, 2) NULL,
	[totalReviews] [int] NOT NULL DEFAULT(0),
	[mapEmbedUrl] [varchar](max) NULL,
	[virtualTourUrl] [varchar](max) NULL,
	[history] [nvarchar](max) NULL,
	[specialFeatures] [nvarchar](max) NULL,
	[famousProducts] [nvarchar](max) NULL,
	[culturalEvents] [nvarchar](max) NULL,
	[craftProcess] [nvarchar](max) NULL,
	[videoDescriptionUrl] [nvarchar](500) NULL,
	[travelTips] [nvarchar](max) NULL,
	CONSTRAINT [FK_CraftVillage_Seller] FOREIGN KEY([sellerId]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_CraftVillage_CraftType] FOREIGN KEY([typeID]) REFERENCES [dbo].[CraftType] ([typeID])
)
GO

--Table [NavigationPoints]
CREATE TABLE [dbo].[NavigationPoints](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[villageId] [int] NULL,
	[currentImage] [int] NULL,
	[nextImage] [int] NULL,
	[x] [float] NULL,
	[y] [float] NULL,
	[description] [nvarchar](255) NULL,
	CONSTRAINT [FK_NavigationPoints_CraftVillage] FOREIGN KEY([villageId]) REFERENCES [dbo].[CraftVillage]([villageID])
)
GO

--Table [VillageImage]
CREATE TABLE [dbo].[VillageImage](
	[imageID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[villageID] [int] NOT NULL,
	[imageUrl] [varchar](max) NOT NULL,
	[isMain] [bit] NOT NULL DEFAULT(0),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_VillageImage_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID])
)
GO

--Table [FavoriteVillage]
CREATE TABLE [dbo].[FavoriteVillage](
	[favoriteID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[villageID] [int] NOT NULL,
	[addedDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_FavoriteVillage_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_FavoriteVillage_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID]),
	CONSTRAINT [UC_FavoriteVillage] UNIQUE NONCLUSTERED ([userID], [villageID]) --1 NGƯỜI THÍCH 1 LÀNG 1 LẦN
)
GO

--Table [VillageReview]
CREATE TABLE [dbo].[VillageReview](
	[reviewID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[villageID] [int] NOT NULL,
	[userID] [int] NOT NULL,
	[rating] [int] NOT NULL CHECK  (([rating]>=(1) AND [rating]<=(5))),
	[reviewText] [nvarchar](max) NULL,
	[reviewDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[response] [nvarchar](max) NULL,
	[responseDate] [datetime] NULL,
	CONSTRAINT [FK_VillageReview_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_VillageReview_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID])
)
GO

----------------------------------------------------Product-------------
--Table [ProductCategory]
CREATE TABLE [dbo].[ProductCategory](
	[categoryID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[categoryName] [nvarchar](100) NOT NULL,
	[description] [nvarchar](max) NULL,
	[status] [int] NOT NULL DEFAULT(1),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL
)
GO

--Table [Product] 
CREATE TABLE [dbo].[Product]( --22 trường
	[pid] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[price] [decimal](10, 2) NOT NULL,
	[description] [nvarchar](max) NULL,
	[stock] [int] NOT NULL,
	[status] [int] NOT NULL DEFAULT(1), --1: hoạt dông, 2: ẩn, 3: đợi admin duyêt
	[villageID] [int] NOT NULL,
	[categoryID] [int] NOT NULL,
	[mainImageUrl] [varchar](max) NULL,
	[clickCount] [int] NOT NULL DEFAULT(0),
	[lastClicked] [datetime] NULL DEFAULT GETDATE(),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	[craftTypeID] [int] NULL, 
	[sku] [varchar](50) NULL,
	[weight] [decimal](10, 2) NULL,
	[dimensions] [nvarchar](100) NULL,
	[materials] [nvarchar](500) NULL,
	[careInstructions] [nvarchar](max) NULL,
	[warranty] [nvarchar](200) NULL,
	[isFeatured] [bit] NOT NULL DEFAULT(0), -- Bỏ
	[averageRating] [decimal](3, 2) NULL,
	[totalReviews] [int] NOT NULL DEFAULT(0),
	CONSTRAINT [FK_Product_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID]),
	CONSTRAINT [FK_Product_Category] FOREIGN KEY([categoryID]) REFERENCES [dbo].[ProductCategory] ([categoryID]),
	CONSTRAINT [FK_Product_CraftType] FOREIGN KEY([craftTypeID]) REFERENCES [dbo].[CraftType] ([typeID])
)
GO

--Table [ProductImage]
CREATE TABLE [dbo].[ProductImage](
	[imageID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[productID] [int] NOT NULL,
	[imageUrl] [varchar](max) NOT NULL,
	[isMain] [bit] NOT NULL DEFAULT(0),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_ProductImage_Product] FOREIGN KEY([productID]) REFERENCES [dbo].[Product] ([pid])
)
GO

--Table [ProductReview]
CREATE TABLE [dbo].[ProductReview](
	[reviewID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[productID] [int] NOT NULL,
	[userID] [int] NOT NULL,
	[rating] [int] NOT NULL CHECK (([rating]>=(1) AND [rating]<=(5))),
	[reviewText] [nvarchar](max) NULL,
	[reviewDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[response] [nvarchar](max) NULL,
	[responseDate] [datetime] NULL,
	CONSTRAINT [FK_ProductReview_Product] FOREIGN KEY([productID]) REFERENCES [dbo].[Product] ([pid]),
	CONSTRAINT [FK_ProductReview_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [Wishlist]
CREATE TABLE [dbo].[Wishlist](
	[wishlistID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[productID] [int] NOT NULL,
	[addedDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_Wishlist_Product] FOREIGN KEY([productID]) REFERENCES [dbo].[Product] ([pid]),
	CONSTRAINT [FK_Wishlist_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [UC_FavoriteProduct] UNIQUE NONCLUSTERED ([userID], [productID]) --1 User, 1 SP, Thêm 1 lần
)
GO

----------------------------------------------------Ticket-------------
--Table [TicketType]
CREATE TABLE [dbo].[TicketType](
	[typeID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[typeName] [nvarchar](50) NOT NULL UNIQUE,
	[description] [nvarchar](200) NULL,
	[ageRange] [nvarchar](50) NULL,
	[status] [int] NOT NULL DEFAULT(1),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL
)
GO

--Table [VillageTicket]
CREATE TABLE [dbo].[VillageTicket](
	[ticketID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[villageID] [int] NOT NULL,
	[typeID] [int] NOT NULL,
	[price] [decimal](10, 2) NOT NULL,
	[status] [int] NOT NULL  DEFAULT(1),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_VillageTicket_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID]),
	CONSTRAINT [FK_VillageTicket_TicketType] FOREIGN KEY([typeID]) REFERENCES [dbo].[TicketType] ([typeID]),
	CONSTRAINT [UC_FavoriteTicket] UNIQUE NONCLUSTERED ([villageID], [typeID])
)
GO

--Table [TicketOrder] 
CREATE TABLE [dbo].[TicketOrder](
	[orderID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[villageID] [int] NOT NULL,
	[totalPrice] [decimal](10, 2) NOT NULL,
	[totalQuantity] [int] NOT NULL,
	[status]  [int] NOT NULL DEFAULT(0), -- 0: đang xử lí, 1: đã thanh toán, 2: đã huỷ, 3: hoàn trả 
	[paymentMethod] [nvarchar](50) NOT NULL,
	[paymentStatus]  [int] NOT NULL DEFAULT(0),
	[customerName] [nvarchar](100) NOT NULL,
	[customerPhone] [nvarchar](20) NOT NULL,
	[customerEmail] [nvarchar](100) NULL,
	[note] [nvarchar](max) NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_TicketOrder_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_TicketOrder_Village] FOREIGN KEY([villageID]) REFERENCES [dbo].[CraftVillage] ([villageID])
)
GO

--Table [TicketOrderDetail]
CREATE TABLE [dbo].[TicketOrderDetail](
	[detailID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[orderID] [int] NOT NULL,
	[ticketID] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [decimal](10, 2) NOT NULL,
	[subtotal] [decimal](10, 2) NOT NULL,
	CONSTRAINT [FK_TicketOrderDetail_Order] FOREIGN KEY([orderID]) REFERENCES [dbo].[TicketOrder] ([orderID]),
	CONSTRAINT [FK_TicketOrderDetail_Ticket] FOREIGN KEY([ticketID]) REFERENCES [dbo].[VillageTicket] ([ticketID]),
)
GO

--Table [TicketCode] 
CREATE TABLE [dbo].[TicketCode](
	[codeID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[orderDetailID] [int] NOT NULL,
	[ticketCode] [varchar](20) NOT NULL UNIQUE ,
	[qrCode] [varchar](max) NULL,
	[issueDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[expiryDate] [datetime] NOT NULL,
	[usageDate] [datetime] NULL,
	[status] [int] NOT NULL DEFAULT(0), 
	[usedBy] [nvarchar](100) NULL,
	[notes] [nvarchar](500) NULL,
	CONSTRAINT [FK_TicketCode_OrderDetail] FOREIGN KEY([orderDetailID]) REFERENCES [dbo].[TicketOrderDetail] ([detailID])
)
GO

----------------------------------------------------Order-------------
--Table [Orders] -- 
CREATE TABLE [dbo].[Orders](
	[id] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[total_price] [decimal](10, 2) NOT NULL,
	[status]  [int] DEFAULT(0) NOT NULL,  -- 0: đang xử lí, 1: đã hoàn thành, 2: đã huỷ, 3: hoàn trả 
	[shippingAddress] [nvarchar](200) NOT NULL,
	[shippingPhone] [nvarchar](20) NOT NULL,
	[shippingName] [nvarchar](100) NOT NULL,
	[paymentMethod] [nvarchar](50) NOT NULL,
	[paymentStatus] [int] DEFAULT(0) NOT NULL, --0: CHƯA, 1: ĐÃ THANH TOÁN
	[note] [nvarchar](max) NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	[trackingNumber] [varchar](100) NULL,
	[estimatedDeliveryDate] [datetime] NULL,
	[actualDeliveryDate] [datetime] NULL,
	[cancelReason] [nvarchar](500) NULL,
	[cancelDate] [datetime] NULL,
	[refundAmount] [decimal](10, 2) NULL,
	[refundDate] [datetime] NULL,
	[refundReason] [nvarchar](500) NULL,
	CONSTRAINT FK_Orders_Account FOREIGN KEY (userID) REFERENCES [dbo].[Account](userID)
)
GO

--Table [OrderDetail]
CREATE TABLE [dbo].[OrderDetail](
	[id] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[order_id] [int] NOT NULL,
	[product_id] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[price] [decimal](10, 2) NOT NULL,
	[subtotal] [decimal](10, 2) NOT NULL,
	CONSTRAINT FK_OrderDetail_Orders FOREIGN KEY (order_id) REFERENCES [dbo].[Orders](id),
	CONSTRAINT FK_OrderDetail_Product FOREIGN KEY (product_id) REFERENCES [dbo].[Product](pid)
)
GO

--Table [Cart]
CREATE TABLE [dbo].[Cart](
	[cartID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_Cart_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [CartItem]
CREATE TABLE [dbo].[CartItem](
	[itemID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[cartID] [int] NOT NULL,
	[productID] [int] NOT NULL,
	[quantity] [int] NOT NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_CartItem_Cart] FOREIGN KEY([cartID]) REFERENCES [dbo].[Cart] ([cartID]),
	CONSTRAINT [FK_CartItem_Product] FOREIGN KEY([productID]) REFERENCES [dbo].[Product] ([pid])
)
GO

----------------------------------------------------Support-------------
--Table [MessageThread]---Dư thì lma
CREATE TABLE [dbo].[MessageThread](
	[threadID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[sellerID] [int] NOT NULL,
	[messageName] [varchar](max) NULL,
	CONSTRAINT [FK_MessageThread_User1] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_MessageThread_User2] FOREIGN KEY([sellerID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [Message]---Dư thì lma
CREATE TABLE [dbo].[Message](
	[messageID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[threadID] [int] NOT NULL,
	[senderID] [int] NOT NULL,
	[messageContent] [nvarchar](max) NOT NULL,
	[attachmentUrl] [varchar](max) NULL,
	[sentDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_Message_Sender] FOREIGN KEY([senderID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_Message_Thread] FOREIGN KEY([threadID]) REFERENCES [dbo].[MessageThread] ([threadID])
)
GO

----------------------------------------------------Notification-------------
--Table [NotificationType]
CREATE TABLE [dbo].[NotificationType](
	[typeID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[typeName] [nvarchar](50) UNIQUE NOT NULL
)
GO

--Table [Notification]
CREATE TABLE [dbo].[Notification](
	[notificationID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[typeID] [int] NOT NULL,
	[title] [nvarchar](200) NOT NULL,
	[content] [nvarchar](max) NOT NULL,
	[targetUrl] [varchar](500) NULL,
	[isRead] [bit] NOT NULL DEFAULT(0),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[readDate] [datetime] NULL,
	CONSTRAINT [FK_Notification_Type] FOREIGN KEY([typeID]) REFERENCES [dbo].[NotificationType] ([typeID]),
	CONSTRAINT [FK_Notification_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [PageView] -- tham chieu dfen ban????
CREATE TABLE [dbo].[PageView](
	[viewID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[pageUrl] [varchar](500) NOT NULL,
	[userID] [int] NULL,
	[ipAddress] [varchar](50) NULL,
	[userAgent] [varchar](500) NULL,
	[referrer] [varchar](500) NULL,
	[sessionID] [varchar](100) NULL,
	[viewDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_PageView_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [SearchHistory] --- SearchType: nvarchar --> int  BOTCHAT
CREATE TABLE [dbo].[SearchHistory](
	[searchID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NULL,
	[searchKeyword] [nvarchar](200) NOT NULL,
	[searchType] [nvarchar](50) NULL,
	[resultCount] [int] NULL,
	[ipAddress] [varchar](50) NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_SearchHistory_User] FOREIGN KEY([userID]) REFERENCES [dbo].[Account] ([userID])
)
GO

----------------------------------------------------Payment-------------
--Table [Payment]
Drop table Payment
CREATE TABLE [dbo].[Payment](
	[paymentID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[orderID] [int] NULL,
	[tourBookingID] [int] NULL,
	[amount] [decimal](10, 2) NOT NULL,
	[paymentMethod] [nvarchar](50) NOT NULL,
	[paymentStatus] int default(0) NOT NULL, -- 0: Chưa thanh toán, 1: đã thanh toán
	[transactionID] [nvarchar](100) NULL,
	[paymentDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_Payment_Order] FOREIGN KEY([orderID]) REFERENCES [dbo].[Orders] ([id]),
	CONSTRAINT [FK_Payment_TicketOrder] FOREIGN KEY([tourBookingID]) REFERENCES [dbo].[TicketOrder] ([orderID])
)
GO

----------------------------------------------------Admin-Seller-------------
--Table [SalesReport]
CREATE TABLE [dbo].[SalesReport](
	[reportID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[sellerID] [int] NOT NULL,
	[reportMonth] [int] NOT NULL,
	[reportYear] [int] NOT NULL,
	[totalOrders] [int] NOT NULL DEFAULT(0),
	[totalRevenue] [decimal](15, 2) NOT NULL DEFAULT(0),
	[totalProducts] [int] NOT NULL  DEFAULT(0),
	[commission] [decimal](15, 2) NOT NULL  DEFAULT(0),
	[netRevenue] [decimal](15, 2) NOT NULL  DEFAULT(0),
	[generatedDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_SalesReport_Seller] FOREIGN KEY([sellerID]) REFERENCES [dbo].[Account]([userID])
)
GO

--Table [SellerVerification] --loại làng nggeef đăng kí. 
CREATE TABLE [dbo].[SellerVerification] (
    [verificationID] INT IDENTITY(1,1) PRIMARY KEY NOT NULL,   
    -- Khóa ngoại liên kết với User muốn nâng cấp-- Check thử account >18 tuổi
    [sellerID] INT NOT NULL,
    -- Thông tin cơ bản
    [businessType] NVARCHAR(100) NOT NULL,         -- Cá nhân / Hộ kinh doanh / Công ty TNHH / HTX / Làng nghề
    [businessVillageCategry] NVARCHAR(200) NOT NULL, -- Loại làng
    [businessVillageName] NVARCHAR(200) NOT NULL,         -- Tên cá nhân / tổ chức / làng nghề
    [businessVillageAddress] NVARCHAR(500) NOT NULL,      -- Địa chỉ kinh doanh
    [productProductCategory] NVARCHAR(200) NOT NULL,      -- Nhóm sản phẩm kinh doanh/ Thêm cột cho ProductCategory
    [profileVillagePictureUrl] VARCHAR(MAX) NULL,         -- Ảnh đại diện hoặc logo
    -- Thông tin liên hệ lấy từ bảng Account
    [contactPerson] NVARCHAR(200) NOT NULL,        -- Người đại diện 
    [contactPhone] NVARCHAR(20) NOT NULL,          -- Số điện thoại liên hệ - 
    [contactEmail] NVARCHAR(200) NOT NULL,         -- Email liên hệ
    -- Thông tin cá nhân (dùng khi businessType = Cá nhân)
    [idCardNumber] NVARCHAR(50) NULL,              -- Số CMND/CCCD
    [idCardFrontUrl] VARCHAR(MAX) NULL,            -- Link ảnh mặt trước CMND/CCCD
    [idCardBackUrl] VARCHAR(MAX) NULL,             -- Link ảnh mặt sau CMND/CCCD
    -- Thông tin doanh nghiệp (dùng khi businessType != Cá nhân)
    [businessLicense] NVARCHAR(100) NULL,          -- Số giấy phép kinh doanh
    [taxCode] NVARCHAR(50) NULL,                   -- Mã số thuế
    [documentUrl] VARCHAR(MAX) NULL,               -- Link ảnh/file giấy phép kinh doanh
    -- Ghi chú bổ sung
    [note] NVARCHAR(MAX) NULL,                     -- Ghi chú Seller gửi Admin
    -- Trạng thái duyệt
    [verificationStatus] INT NOT NULL DEFAULT 0,   -- 0: Đang xử lý, 1: Đã duyệt, 2: Từ chối
    [verifiedBy] INT NULL,                         -- Admin duyệt
    [verifiedDate] DATETIME NULL,                  -- Ngày duyệt
    [rejectReason] NVARCHAR(MAX) NULL,             -- Lý do từ chối
    -- Ngày tạo
    [createdDate] DATETIME NOT NULL DEFAULT GETDATE(),    
    -- Ràng buộc FK
    CONSTRAINT [FK_SellerVerification_Admin] FOREIGN KEY ([verifiedBy]) REFERENCES [dbo].[Account] ([userID]),
    CONSTRAINT [FK_SellerVerification_Seller] FOREIGN KEY ([sellerID]) REFERENCES [dbo].[Account] ([userID])
);
GO

-- 2. TẠO BẢNG CÒN THIẾU
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[CartTicket]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[CartTicket](
        [itemID] [int] IDENTITY(1,1) NOT NULL,
        [cartID] [int] NOT NULL,
        [ticketID] [int] NOT NULL,
        [quantity] [int] NOT NULL,
        [ticketDate] [date] NOT NULL,
        [createdDate] [datetime] NOT NULL,
        [updatedDate] [datetime] NULL,
        PRIMARY KEY CLUSTERED ([itemID] ASC),
        CONSTRAINT [UC_CartTicket_Item] UNIQUE NONCLUSTERED ([cartID] ASC, [ticketID] ASC, [ticketDate] ASC)
    );
    ALTER TABLE [dbo].[CartTicket] ADD CONSTRAINT [FK_CartTicket_Cart] FOREIGN KEY([cartID]) REFERENCES [dbo].[Cart] ([cartID]);
    ALTER TABLE [dbo].[CartTicket] ADD CONSTRAINT [FK_CartTicket_VillageTicket] FOREIGN KEY([ticketID]) REFERENCES [dbo].[VillageTicket] ([ticketID]);
    ALTER TABLE [dbo].[CartTicket] ADD CHECK ([quantity] > 0);
    CREATE NONCLUSTERED INDEX [IX_CartTicket_CartID] ON [dbo].[CartTicket]([cartID] ASC);
    CREATE NONCLUSTERED INDEX [IX_CartTicket_TicketDate] ON [dbo].[CartTicket]([ticketDate] ASC);
END
Go

IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TicketAvailability]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[TicketAvailability](
        [availabilityID] [int] IDENTITY(1,1) NOT NULL,
        [ticketID] [int] NOT NULL,
        [availableDate] [date] NOT NULL,
        [totalSlots] [int] NOT NULL,
        [bookedSlots] [int] NOT NULL DEFAULT(0),
        [availableSlots] [int] NOT NULL, -- Cột này giống y Database 1
        [status] [bit] NOT NULL DEFAULT(1),
        [createdDate] [datetime] NOT NULL DEFAULT(GETDATE()),
        [updatedDate] [datetime] NULL,
        CONSTRAINT [PK_TicketAvailability] PRIMARY KEY CLUSTERED ([availabilityID] ASC),
        CONSTRAINT [UC_TicketAvailability_TicketDate] UNIQUE NONCLUSTERED ([ticketID] ASC, [availableDate] ASC)
    );
    ALTER TABLE [dbo].[TicketAvailability] ADD CONSTRAINT [FK_TicketAvailability_Ticket] FOREIGN KEY([ticketID]) REFERENCES [dbo].[VillageTicket] ([ticketID]);
END
Go

IF NOT EXISTS (
    SELECT * FROM sys.indexes WHERE name = 'IX_TicketAvailability_Date' AND object_id = OBJECT_ID('dbo.TicketAvailability')
)
BEGIN
    CREATE NONCLUSTERED INDEX [IX_TicketAvailability_Date] ON [dbo].[TicketAvailability]([availableDate] ASC)
    WHERE ([status]=(1));
END
Go
-- 5. BỔ SUNG CONSTRAINT/CHECK CÒN THIẾU
-- TicketAvailability
ALTER TABLE [dbo].[TicketAvailability] WITH NOCHECK ADD CHECK ([bookedSlots] >= 0);
Go
ALTER TABLE [dbo].[TicketAvailability] WITH NOCHECK ADD CHECK ([totalSlots] > 0);
Go
ALTER TABLE [dbo].[TicketAvailability] WITH NOCHECK ADD CONSTRAINT [CK_TicketAvailability_BookedSlots] CHECK ([bookedSlots] <= [totalSlots]);
Go