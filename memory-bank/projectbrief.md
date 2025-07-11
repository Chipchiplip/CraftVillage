# CraftVillage Project Brief

## Project Overview
**CraftVillage** is a Java EE web application for promoting and selling products from Vietnamese craft villages. The application serves as a platform connecting tourists and customers with traditional craft villages, allowing them to explore villages, purchase products, and book experience tours.

## Core Requirements
1. **User Management**: Registration, login, profile management with role-based access (User, Seller, Admin)
2. **Craft Village Management**: Display village information, virtual tours, reviews, and ratings
3. **Product Management**: Browse products by category, search, view details, manage inventory
4. **Shopping Cart & Orders**: Add products to cart, checkout process, order tracking
5. **Village Ticket System**: Book tours/experiences for craft villages with date selection
6. **Wishlist Management**: Save favorite products and villages
7. **Admin Panel**: Manage users, products, villages, and orders

## Technical Stack
- **Backend**: Java EE (Servlets, JSP)
- **Database**: SQL Server with extensive schema for accounts, products, villages, orders
- **Frontend**: JSP pages with Bootstrap 5 for responsive design  
- **Build Tool**: Apache Ant
- **Architecture**: MVC pattern with DAO layer for database operations

## Key Features
### For Customers
- Browse craft villages with virtual tours and detailed information
- Purchase traditional handicraft products
- Book village tour experiences
- Manage shopping cart and orders
- Review and rate villages

### For Sellers
- Manage village information and products
- Track sales and inventory
- Respond to customer reviews

### For Admins
- User account management
- Product and village approval workflow
- System monitoring and reporting

## Project Structure
- `src/java/` - Backend Java code (controllers, DAOs, entities, services)
- `web/` - Frontend JSP pages, assets, and configuration
- `web/test/` - JSP-based integration tests
- `Database/` - SQL scripts for database schema

## Current State
- Core functionality implemented
- Basic JSP test suite exists but needs improvement
- Database integration established but lacks proper testing
- Missing comprehensive error handling and validation testing 