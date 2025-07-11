
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
CREATE TABLE [dbo].[Product](
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
	[isFeatured] [bit] NOT NULL DEFAULT(0),
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
	[status]  [int] NOT NULL DEFAULT(0),
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

--Tối ưu truy vấn cho bảng [TicketOrder]
CREATE NONCLUSTERED INDEX [IX_TicketOrder_Status] ON [dbo].[TicketOrder]
(	[status] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_TicketOrder_UserID] ON [dbo].[TicketOrder]
(	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_TicketOrder_VillageID] ON [dbo].[TicketOrder]
(	[villageID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
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

--Tối ưu truy vấn cho bảng [TicketCode]
CREATE NONCLUSTERED INDEX [IX_TicketCode_ExpiryDate] ON [dbo].[TicketCode]
(	[expiryDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
CREATE NONCLUSTERED INDEX [IX_TicketCode_Status] ON [dbo].[TicketCode]
(	[status] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO

----------------------------------------------------Order-------------
--Table [Orders] -- 
CREATE TABLE [dbo].[Orders](
	[id] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[userID] [int] NOT NULL,
	[total_price] [decimal](10, 2) NOT NULL,
	[status]  [int] DEFAULT(0) NOT NULL,  --0 pending, 1: đã giao,2 ...
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
	[userID1] [int] NOT NULL,
	[userID2] [int] NOT NULL,
	[lastMessageDate] [datetime] NULL,
	[status] [int] NOT NULL DEFAULT(1),
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_MessageThread_User1] FOREIGN KEY([userID1]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_MessageThread_User2] FOREIGN KEY([userID2]) REFERENCES [dbo].[Account] ([userID])
)
GO

--Table [Message]---Dư thì lma
CREATE TABLE [dbo].[Message](
	[messageID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[threadID] [int] NOT NULL,
	[senderID] [int] NOT NULL,
	[receiverID] [int] NOT NULL,
	[messageContent] [nvarchar](max) NOT NULL,
	[isRead] [bit] NOT NULL DEFAULT(0),
	[attachmentUrl] [varchar](max) NULL,
	[status] [int] NOT NULL, -- 1: Đã gửi,  2. Đã phản hồi
	[sentDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_Message_Receiver] FOREIGN KEY([receiverID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_Message_Sender] FOREIGN KEY([senderID]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_Message_Thread] FOREIGN KEY([threadID]) REFERENCES [dbo].[MessageThread] ([threadID])
)
GO

--Tối ưu truy vấn cho bảng [Message]
CREATE NONCLUSTERED INDEX [IX_Message_ReceiverID] ON [dbo].[Message]
(   [receiverID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Message_SenderID] ON [dbo].[Message]
(   [senderID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Message_ThreadID] ON [dbo].[Message]
(   [threadID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
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

--Tối ưu truy vấn bảng [Notification]
CREATE NONCLUSTERED INDEX [IX_Notification_IsRead] ON [dbo].[Notification]
(	[isRead] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_Notification_UserID] ON [dbo].[Notification]
(	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO

--Table [PageView] -- tham chieu den ban
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

--Tối ưu truy vấn bảng [PageView]
CREATE NONCLUSTERED INDEX [IX_PageView_SessionID] ON [dbo].[PageView]
(	[sessionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_PageView_UserID] ON [dbo].[PageView]
(	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
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

--Tối ưu truy vấn bảng [SearchHistory]
CREATE NONCLUSTERED INDEX [IX_SearchHistory_Keyword] ON [dbo].[SearchHistory]
(	[searchKeyword] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [IX_SearchHistory_UserID] ON [dbo].[SearchHistory]
(	[userID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO

----------------------------------------------------Payment-------------
--Table [Payment]
CREATE TABLE [dbo].[Payment](
	[paymentID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[orderID] [int] NULL,
	[tourBookingID] [int] NULL,
	[amount] [decimal](10, 2) NOT NULL,
	[paymentMethod] [nvarchar](50) NOT NULL,
	[paymentStatus] [nvarchar](50) NOT NULL,
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

--Table [SellerVerification]
CREATE TABLE [dbo].[SellerVerification](
	[verificationID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[sellerID] [int] NOT NULL,
	[businessLicense] [nvarchar](100) NULL,
	[businessName] [nvarchar](200) NULL,
	[taxCode] [nvarchar](50) NULL,
	[documentUrl] [varchar](max) NULL,
	[verificationStatus] [int] NOT NULL DEFAULT(0),--0: đang xử lí 1: đã duyệt 2: từ chối
	[verifiedBy] [int] NULL,
	[verifiedDate] [datetime] NULL,
	[rejectReason] [nvarchar](max) NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	CONSTRAINT [FK_SellerVerification_Admin] FOREIGN KEY([verifiedBy]) REFERENCES [dbo].[Account] ([userID]),
	CONSTRAINT [FK_SellerVerification_Seller] FOREIGN KEY([sellerID]) REFERENCES [dbo].[Account] ([userID])
)
GO

--------------------------------------------------------------------Insert---------------------
--Insert [Role]
SET IDENTITY_INSERT [dbo].[Role] ON;
INSERT INTO [dbo].[Role] (roleID, roleName, description)
VALUES 
(1, N'Customer', N'Tài khoản khách mua hàng'),
(2, N'Seller', N'Tài khoản nghệ nhân/nhà bán'),
(3, N'Admin', N'Tài khoản quản trị');
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
(1, N'Gom', N'Gốm thủ công'),
(2, N'Chiếu', N'Chiếu đan truyền thống');
SET IDENTITY_INSERT [dbo].[CraftType] OFF;
GO

--Insert [CraftVillage]
SET IDENTITY_INSERT [dbo].[CraftVillage] ON;
INSERT INTO [dbo].[CraftVillage] (villageID ,typeID, villageName, address, status, sellerId)
VALUES 
(1,1, N'Làng gốm Bát Tràng', N'Gia Lâm, Hà Nội', 1, 2),
(2,2, N'Làng chiếu Cái Bè', N'Tiền Giang', 1, 2);
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
(1, 1, 5, N'Tuyệt vời!');
GO

--Insert [ProductCategory]
SET IDENTITY_INSERT [dbo].[ProductCategory] ON;
INSERT INTO [dbo].[ProductCategory] (categoryID,categoryName, description)
VALUES 
(1, N'Đồ gốm', N'Sản phẩm từ đất nung'),
(2, N'Chieu',N'San pham tu co');
SET IDENTITY_INSERT [dbo].[ProductCategory] OFF;
GO

--Insert [Product]
SET IDENTITY_INSERT [dbo].[Product] ON;
INSERT INTO [dbo].[Product] (pid,name, price, stock, villageID, categoryID)
VALUES 
(1, N'Ấm trà gốm', 250000, 50, 1, 1);
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
(1, 1, 5, N'Sản phẩm chất lượng cao');
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

--Insert [TicketOrder]
INSERT INTO [dbo].[TicketOrder] (userID, villageID, totalPrice, totalQuantity, paymentMethod, customerName, customerPhone)
VALUES 
(1,1, 100000, 2, N'Tiền mặt', N'Nguyễn Văn A', '0909999999');
GO

--Insert [TicketOrderDetail]
INSERT INTO [dbo].[TicketOrderDetail] (orderID, ticketID, quantity, price, subtotal)
VALUES 
(1, 1, 2, 50000, 100000);
GO

--Insert [TicketCode]
INSERT INTO [dbo].[TicketCode] (orderDetailID, ticketCode, expiryDate)
VALUES 
(1, 'TICKET123', DATEADD(DAY, 5, GETDATE()));
GO

--------------------------------------------------------------------Other----------------------
--PROCEDURE [AddAccount]
CREATE PROCEDURE [dbo].[AddAccount]
    @userName NVARCHAR(100),
    @password NVARCHAR(100), -- plain text password
    @email NVARCHAR(100),
    @address NVARCHAR(200) = NULL,
    @phoneNumber NVARCHAR(20) = NULL,
    @roleID INT = 1, -- Default: User
    @result INT OUTPUT  -- kết quả trả về
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra trùng username
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE userName = @userName)
        BEGIN
            SET @result = 0;
            RETURN;
        END
        -- Kiểm tra trùng email
        IF EXISTS (SELECT 1 FROM [dbo].[Account] WHERE email = @email)
        BEGIN
            SET @result = 0;
            RETURN;
        END
        -- Nếu không trùng thì thêm mới
        INSERT INTO [dbo].[Account] (
            userName, password, email, address, phoneNumber,
            roleID
        )
        VALUES (
            @userName, dbo.HashPassword(@password), @email, @address,@phoneNumber, @roleID
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
    @result INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Kiểm tra user tồn tại
        IF NOT EXISTS (SELECT 1 FROM [dbo].[Account] WHERE userID = @userID)
        BEGIN
            SET @result = 0;
            RETURN;
        END
        -- Kiểm tra email đã được dùng bởi user khác chưa
        IF EXISTS (
            SELECT 1 FROM [dbo].[Account]
            WHERE email = @email AND userID != @userID
        )
        BEGIN
            SET @result = -1; -- Email bị trùng
            RETURN;
        END
        -- Cập nhật thông tin cho user
        UPDATE [dbo].[Account]
        SET
            email = @email, address = @address, phoneNumber = @phoneNumber, avatarUrl = @avatarUrl, updatedDate = GETDATE()
        WHERE userID = @userID;
        SET @result = 1; -- Thành công
    END TRY
    BEGIN CATCH
        SET @result = -99; -- Lỗi hệ thống
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

--PROCEDURE [RegisterAccount]
CREATE PROCEDURE RegisterAccount
    @UserName NVARCHAR(100),
    @Password NVARCHAR(100),        -- dạng text, sẽ được hash trong procedure
    @Email NVARCHAR(100),
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
        userName, password, email, address, phoneNumber
    )
    VALUES (
        @UserName, dbo.HashPassword(@Password), @Email, @Address, @PhoneNumber
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
