USE [CraftDB]
GO

--Insert [Role]
SET IDENTITY_INSERT [dbo].[Role] ON;
INSERT INTO [dbo].[Role] (roleID, roleName, description)
VALUES 
(1, N'Customer', N'Customer'),
(2, N'Seller', N'Seller'),
(3, N'Admin', N'Admin');
SET IDENTITY_INSERT [dbo].[Role] OFF;
GO

--Insert [Account]
INSERT INTO [dbo].[Account] (userName, password, email, address, phoneNumber, roleID)
VALUES 
(N'customer01', dbo.HashPassword('123123') , N'customer01@example.com', N'Hà Nội', '0900000001', 1),
(N'seller01', dbo.HashPassword('123123') , N'seller01@example.com',   N'Hội An', '0900000002', 2),
(N'admin01',  dbo.HashPassword('123123') , N'admin01@example.com',    N'Đà Nẵng', '0900000003', 3);
GO

--Insert [CraftType]
SET IDENTITY_INSERT [dbo].[CraftType] ON;
INSERT INTO [dbo].[CraftType] (typeID,typeName, description)
VALUES 
(1, N'Gom', N'Gom Thu Cong'),
(2, N'Chieu', N'Chieu Truyen Thong');
SET IDENTITY_INSERT [dbo].[CraftType] OFF;
GO

--Insert [CraftVillage]
SET IDENTITY_INSERT [dbo].[CraftVillage] ON;
INSERT INTO [dbo].[CraftVillage] (villageID ,typeID, villageName, address, status, sellerId)
VALUES 
(1,1, N'Gom Thanh Ha', N'Quang Nam', 1, 2),
(2,2, N'Lang Nuoc Mam', N'Da Nang', 1, 2);
SET IDENTITY_INSERT [dbo].[CraftVillage] OFF;
GO

--Insert [VillageImage]
INSERT INTO [dbo].[VillageImage] (villageID, imageUrl, isMain)
VALUES 
(1, 'https://example.com/battrang.jpg', 1);
GO

--Insert [FavoriteVillage]
INSERT INTO [dbo].[FavoriteVillage] (userID, villageID)
VALUES 
(1, 1);
GO

--Insert [VillageReview]
INSERT INTO [dbo].[VillageReview] (villageID, userID, rating, reviewText)
VALUES 
(1, 1, 5, N'Nice'),
(1, 1, 2, N'VCL!');
GO


--Insert [ProductCategory]
SET IDENTITY_INSERT [dbo].[ProductCategory] ON;
INSERT INTO [dbo].[ProductCategory] (categoryID,categoryName, description)
VALUES 
(1, N'Do Gom', N'From Dat'),
(2, N'Mam',N'From Ca')
SET IDENTITY_INSERT [dbo].[ProductCategory] OFF;
GO


--Insert [Product]
SET IDENTITY_INSERT [dbo].[Product] ON;
INSERT INTO [dbo].[Product] (pid,name, price, stock, villageID, categoryID, mainImageUrl)
VALUES 
(1, N'Am Tra', 250000, 50, 1, 1, 'hinhanh/village/kim-bong.jpg'),
(2, N'Mam Ca', 50000, 50, 2, 1, 'hinhanh/village/kim-bong.jpg');
SET IDENTITY_INSERT [dbo].[Product] OFF;
GO

--Insert [ProductImage]
INSERT INTO [dbo].[ProductImage] (productID, imageUrl, isMain)
VALUES 
(1, 'https://example.com/amtra.jpg', 1);
GO

--Insert [Wishlist]
INSERT INTO [dbo].[Wishlist] (userID, productID)
VALUES 
(1, 1);
GO

--Insert [ProdcutReview]
INSERT INTO [dbo].[ProductReview] (productID, userID, rating, reviewText)
VALUES 
(1, 1, 5, N'High'),
(1, 1, 1, N'Nhu cc');
GO


INSERT INTO [dbo].[Orders] ( userID, total_price, status, shippingAddress, shippingPhone, shippingName, paymentMethod, paymentStatus, createdDate)
VALUES
(1, 1000000.00, 0, N'12HV, DN', '0911000111', N'A',  N'VNPay', 0, GETDATE()),
(1, 1750000.00, 1, N'45 HV, DNg', '0911222333', N'B', N'Momo', 1, GETDATE()),
(1, 850000.00, 2, N'89 HV, DN', '0911444555', N'C', N'Cash', 0, GETDATE()),
(1, 1200000.00, 3, N'101 HV, DNg', '0911666777', N'D', N'Momo', 1, GETDATE())
GO


--Insert [TicketType]
SET IDENTITY_INSERT [dbo].[TicketType] ON;
INSERT INTO [dbo].[TicketType] (typeID,typeName, ageRange)
VALUES 
(1, N'Người lớn', N'18+'),
(2, N'Child', N'<18');
SET IDENTITY_INSERT [dbo].[TicketType] OFF;
GO

--Insert [VillageTicket]
INSERT INTO [dbo].[VillageTicket] (villageID, typeID, price)
VALUES 
(1, 1, 50000);
GO

INSERT INTO [CraftDB].[dbo].[CraftVillage]
(	typeID,
    [villageName],
    [description],
    [address],
    [contactPhone],
    [contactEmail],
    [mainImageUrl],
    [sellerId],
    [openingHours],
    [closingDays],
    [mapEmbedUrl],
    [history],
    [specialFeatures],
    [famousProducts],
    [culturalEvents],
    [craftProcess],
    [videoDescriptionUrl],
    [travelTips], 
	status
)
VALUES
( 1,
    N'Phuoc Kieu Bronze Casting Village',
    N'A traditional bronze casting village with over 400 years of history, famous for products such as bells, gongs, statues, and bronze artwork, representing the essence of Quang Nam culture.',
    N'Dien Phuong Commune, Dien Ban Town, Quang Nam Province',
    N'0935123456', -- Replace with actual phone if available
    N'dongphuockieu@gmail.com',
    N'hinhanh/village/phuoc-kieu.jpg',
    1,
    N'8 AM – 5 PM',
    N'Sunday',
    N'https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3837.768129025902!2d108.25876437495053!3d15.868779484782038!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31420f72a9ed5293%3A0xa3146eef15a8950!2zTMOgbmcgxJHDumMgxJHhu5NuZyBQaMaw4bubYyBLaeG7gXU!5e0!3m2!1svi!2s!4v1752102689258!5m2!1svi!2s',
    N'The village was founded in the 17th century, initially producing weapons for the Nguyen Lords. In 1832, King Minh Mang merged two local casting communities under the name Phuoc Kieu.',
    N'Completely handmade casting process, each product is acoustically tested and refined by skilled artisans.',
    N'Bells, gongs, bronze statues, incense burners, and high-end bronze artwork.',
    NULL,
    N'Create molds with rice husk clay → melt bronze at high temperature → pour into molds → acoustic testing → hand-finishing.',
    N'https://youtu.be/luGNYKSiQY0?si=_3qEOFG3Y0cnRfZr',
    N'Best to visit in the morning to witness the full casting process. Can be combined with trips to Hoi An and My Son Sanctuary.',
	1
);

--Insert [VillageTicket]
INSERT INTO [dbo].[VillageTicket] (villageID, typeID, price)
VALUES 
(6, 1, 50000),
(6, 2, 15000);
GO

--Insert [VillageReview]
INSERT INTO [dbo].[VillageReview] (villageID, userID, rating, reviewText)
VALUES 
(6, 1, 5, N'Nice'),
(6, 1, 2, N'VCL!');
GO

--Insert [MessageThread]
INSERT INTO [dbo].[MessageThread] ( userID, sellerID ,messageName)
VALUES 
(1, 2,  N'Lang1')
GO

