# System Patterns - CraftVillage

## Architecture Overview
CraftVillage follows a **3-layer MVC architecture** with clear separation of concerns:

### 1. Presentation Layer (Web)
- **JSP Pages**: User interface templates with embedded Java code
- **Bootstrap 5**: Responsive CSS framework for modern UI
- **JavaScript**: Client-side validation and dynamic interactions
- **Assets**: Images, CSS, fonts organized in `/web/assets/`

### 2. Business Logic Layer (Controllers & Services)
- **Servlets**: Handle HTTP requests and responses
- **Service Classes**: Business logic and workflow management
- **Filter Classes**: Cross-cutting concerns (logging, security, validation)

### 3. Data Access Layer (DAO)
- **DAO Pattern**: Data Access Objects for database operations
- **Entity Classes**: POJOs representing database tables
- **DBContext**: Database connection management

## Key Design Patterns

### 1. MVC Pattern
```
User Request → Servlet Controller → Service Layer → DAO → Database
                     ↓
JSP View ← Response Processing ← Business Logic ← Data Access
```

### 2. DAO Pattern
- **Purpose**: Encapsulate database access logic
- **Structure**: One DAO per major entity (AccountDAO, ProductDAO, CartDAO, etc.)
- **Benefits**: Database abstraction, easier testing, maintainability

### 3. Service Pattern
- **Purpose**: Encapsulate business logic
- **Structure**: Interface + Implementation for each service
- **Benefits**: Clear business rules, transaction management, reusability

### 4. Front Controller Pattern
- **Implementation**: Individual servlets handle specific functionality
- **URL Mapping**: Each servlet mapped to specific URL patterns
- **Benefits**: Centralized request handling, consistent error handling

## Database Interaction Pattern

### Connection Management
```java
// Standard pattern used across all DAOs
Connection conn = null;
PreparedStatement ps = null;
ResultSet rs = null;

try {
    conn = new DBContext().getConnection();
    // Database operations
} catch (SQLException e) {
    // Error handling
} finally {
    // Resource cleanup
}
```

### Transaction Management
- **Cart Operations**: Use transactions for multi-table operations
- **Order Processing**: Atomic operations with rollback capability
- **User Registration**: Coordinated account and verification record creation

## Security Patterns

### 1. Input Validation
- **Server-side**: Validation in servlets and services
- **Client-side**: JavaScript validation for user experience
- **SQL Injection Prevention**: PreparedStatement usage

### 2. Authentication & Authorization
- **Session Management**: HttpSession for user state
- **Role-based Access**: Role checking in controllers
- **Password Security**: SHA-512 hashing with salt

### 3. Error Handling
- **Graceful Degradation**: Fallback to mock data when database unavailable
- **Logging**: Comprehensive logging for debugging and monitoring
- **User-friendly Messages**: Avoid exposing system details to users

## Testing Patterns

### Current Testing Architecture
- **JSP-based Integration Tests**: Test complete user workflows
- **Mock Data Approach**: Tests work without database dependency
- **Session Storage**: Client-side state management for test data

### Test Organization
```
web/test/
├── index.jsp          # Test suite dashboard
├── basic-test.jsp     # JSP functionality test
├── test-view-ticket.jsp    # Village ticket listing
├── test-add-cart.jsp       # Shopping cart operations
├── test-edit-cart.jsp      # Cart modification
├── test-wishlist.jsp       # Wishlist management
├── test-booking-ticket.jsp # Tour booking flow
└── test-place-order.jsp    # Complete checkout process
```

## Component Relationships

### Core Entity Dependencies
```
Account (User) → Cart → CartItem → Product
             → Cart → CartTicket → Ticket → TicketAvailability
             → Order → OrderDetail → Product
             → TicketOrder → TicketOrderDetail → Ticket
             → Wishlist → Product
             → VillageReview → CraftVillage
```

### Cart and Ticket System (Updated 2025)

#### Modern E-commerce Cart Pattern:
1. **Cart Storage**: Simple database storage without inventory impact
2. **Availability Display**: Real-time from TicketAvailability table
3. **Checkout Process**: Atomic availability check + reservation + order creation
4. **Inventory Management**: Only updated at confirmed purchase, not browsing

#### Ticket Booking Flow:
```
Ticket Detail → Add to Cart (no reservation) → Cart View → Checkout (reservation) → Payment → Ticket Generation
```

#### Availability Management:
- **TicketAvailability**: Central table for slot management
- **bookedSlots**: Incremented only at checkout
- **availableSlots**: Computed field (totalSlots - bookedSlots)
- **Overselling Protection**: Enforced at checkout transaction level

### Service Layer Dependencies
```
Controllers → Service Interfaces → Service Implementations → DAOs
```

### Database Schema Relationships
- **Account**: Central user management with roles
- **CraftVillage**: Village information with categories and images
- **Product**: Linked to villages and categories
- **Orders**: Shopping cart and order management
- **Tickets**: Village tour booking system

## Performance Considerations

### Database Optimization
- **Connection Pooling**: Should be implemented for production
- **Query Optimization**: Use of indexes and proper WHERE clauses
- **Resource Management**: Proper cleanup of database connections

### Caching Strategy
- **Session Storage**: User cart and preferences
- **Client-side Caching**: Static assets and product images
- **Database Caching**: Frequently accessed village and product data

## Error Handling Strategy

### Database Errors
- **Connection Failures**: Graceful degradation to mock data
- **Query Errors**: Proper logging and user-friendly messages
- **Transaction Rollbacks**: Maintain data consistency

### Business Logic Errors
- **Validation Failures**: Clear error messages to users
- **Authorization Errors**: Redirect to appropriate pages
- **State Errors**: Handle edge cases in user workflows 