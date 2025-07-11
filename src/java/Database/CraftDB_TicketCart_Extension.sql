-- ================================================================
-- TICKET CART SYSTEM EXTENSION
-- Extension for CraftDB to support Ticket Cart functionality
-- ================================================================

USE [CraftDB]
GO

-- Table [CartTicket] - Separate table for tickets in cart
CREATE TABLE [dbo].[CartTicket](
	[itemID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[cartID] [int] NOT NULL,
	[ticketID] [int] NOT NULL,
	[quantity] [int] NOT NULL CHECK ([quantity] > 0),
	[ticketDate] [date] NOT NULL,
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_CartTicket_Cart] FOREIGN KEY([cartID]) REFERENCES [dbo].[Cart] ([cartID]),
	CONSTRAINT [FK_CartTicket_VillageTicket] FOREIGN KEY([ticketID]) REFERENCES [dbo].[VillageTicket] ([ticketID]),
	CONSTRAINT [UC_CartTicket_Item] UNIQUE NONCLUSTERED ([cartID], [ticketID], [ticketDate])
)
GO

-- Table [TicketAvailability] - Daily ticket availability management
CREATE TABLE [dbo].[TicketAvailability](
	[availabilityID] [int] PRIMARY KEY IDENTITY(1,1) NOT NULL,
	[ticketID] [int] NOT NULL,
	[availableDate] [date] NOT NULL,
	[totalSlots] [int] NOT NULL DEFAULT(20) CHECK ([totalSlots] > 0),
	[bookedSlots] [int] NOT NULL DEFAULT(0) CHECK ([bookedSlots] >= 0),
	[availableSlots] AS ([totalSlots] - [bookedSlots]) PERSISTED,
	[status] [int] NOT NULL DEFAULT(1), -- 1: Available, 0: Closed
	[createdDate] [datetime] NOT NULL DEFAULT GETDATE(),
	[updatedDate] [datetime] NULL,
	CONSTRAINT [FK_TicketAvailability_VillageTicket] FOREIGN KEY([ticketID]) REFERENCES [dbo].[VillageTicket] ([ticketID]),
	CONSTRAINT [UC_TicketAvailability_Date] UNIQUE NONCLUSTERED ([ticketID], [availableDate]),
	CONSTRAINT [CK_TicketAvailability_BookedSlots] CHECK ([bookedSlots] <= [totalSlots])
)
GO

-- Index for performance optimization
CREATE NONCLUSTERED INDEX [IX_CartTicket_CartID] ON [dbo].[CartTicket]
(	[cartID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE NONCLUSTERED INDEX [IX_CartTicket_TicketDate] ON [dbo].[CartTicket]
(	[ticketDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

CREATE NONCLUSTERED INDEX [IX_TicketAvailability_Date] ON [dbo].[TicketAvailability]
(	[availableDate] ASC
)WHERE ([status]=(1))
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO

-- Stored Procedure: Check ticket availability
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

-- Stored Procedure: Add ticket to cart with availability check
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

-- Stored Procedure: Reserve tickets during checkout
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

-- Function: Get available dates for a ticket (next 30 days)
CREATE FUNCTION [dbo].[GetAvailableDatesForTicket](@ticketID INT)
RETURNS TABLE
AS
RETURN
(
    WITH DateRange AS (
        SELECT CAST(GETDATE() AS DATE) AS DateValue
        UNION ALL
        SELECT DATEADD(DAY, 1, DateValue)
        FROM DateRange
        WHERE DateValue < DATEADD(DAY, 29, CAST(GETDATE() AS DATE))
    )
    SELECT 
        d.DateValue,
        ISNULL(ta.availableSlots, 20) AS AvailableSlots,
        ISNULL(ta.totalSlots, 20) AS TotalSlots,
        ISNULL(ta.bookedSlots, 0) AS BookedSlots
    FROM DateRange d
    LEFT JOIN [TicketAvailability] ta ON ta.ticketID = @ticketID 
                                     AND ta.availableDate = d.DateValue 
                                     AND ta.status = 1
    WHERE ISNULL(ta.availableSlots, 20) > 0
)
GO



-- ================================================================
-- SAMPLE DATA FOR TESTING
-- ================================================================

-- Sample ticket availability data
INSERT INTO [TicketAvailability] (ticketID, availableDate, totalSlots, bookedSlots)
SELECT 
    vt.ticketID,
    DATEADD(DAY, n.number, CAST(GETDATE() AS DATE)) AS availableDate,
    20 AS totalSlots,
    ABS(CHECKSUM(NEWID())) % 5 AS bookedSlots  -- Random 0-4 booked slots
FROM [VillageTicket] vt
CROSS JOIN (
    SELECT 0 AS number UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4
    UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9
    UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14
) n
WHERE vt.status = 1;


SELECT vt.ticketID, vt.villageID, tt.typeName, vt.price, tt.ageRange
FROM VillageTicket vt
INNER JOIN TicketType tt ON vt.typeID = tt.typeID  
WHERE vt.villageID = 1
ORDER BY tt.typeID;
GO 

INSERT INTO [dbo].[VillageTicket] (villageID, typeID, price)
VALUES (1, 2, 30000);
GO


PRINT 'Ticket Cart System Extension completed successfully!' 