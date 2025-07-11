<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="service.CartService"%>
<%@page import="service.ICartService"%>
<%@page import="entity.Account.Account"%>
<%
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    
    String productId = request.getParameter("productId");
    String quantityStr = request.getParameter("quantity");
    
    // Validate inputs
    if (productId == null || productId.trim().isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"success\":false,\"message\":\"Thiếu thông tin sản phẩm\"}");
        return;
    }
    
    // Check if user is logged in
    Account account = (Account) session.getAttribute("acc");
    if (account == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        out.print("{\"success\":false,\"message\":\"Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng\"}");
        return;
    }
    
    try {
        // Parse inputs
        int productIdInt = Integer.parseInt(productId);
        int quantity = (quantityStr != null && !quantityStr.trim().isEmpty()) ? 
                       Integer.parseInt(quantityStr) : 1;
        
        // Validate quantity
        if (quantity <= 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"success\":false,\"message\":\"Số lượng phải lớn hơn 0\"}");
            return;
        }
        
        // ✅ PROPER MVC PATTERN: Use CartService instead of direct database access
        ICartService cartService = new CartService();
        boolean success = cartService.addProductToCart(account.getUserID(), productIdInt, quantity);
        
        if (success) {
            // Update cart in session for immediate UI feedback
            entity.CartWishList.Cart updatedCart = cartService.getCartByUser(account.getUserID());
            session.setAttribute("cart", updatedCart);
            
            out.print("{\"success\":true,\"message\":\"Đã thêm sản phẩm vào giỏ hàng\"}");
        } else {
            out.print("{\"success\":false,\"message\":\"Không thể thêm sản phẩm vào giỏ hàng. Vui lòng kiểm tra tồn kho.\"}");
        }
        
    } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        out.print("{\"success\":false,\"message\":\"Thông tin sản phẩm không hợp lệ\"}");
    } catch (Exception e) {
        e.printStackTrace(); // For debugging
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.print("{\"success\":false,\"message\":\"Có lỗi xảy ra: " + e.getMessage() + "\"}");
    }
%> 