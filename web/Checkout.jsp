<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>


<!DOCTYPE html>
<html class="no-js" lang="en">


    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Da Nang Craft Village</title>
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
        <link rel="shortcut icon" type="image/x-icon" href="hinh anh/Logo/cropped-Favicon-1-32x32.png" />
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/animate.min.css">
        <link rel="stylesheet" href="assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/css/nice-select.css">
        <link rel="stylesheet" href="assets/css/slick.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/main-color.css">
        <link rel="stylesheet" href="assets/css/main-color03-green.css">
        <style>
            /* Tổng quan */
            .page-contain.checkout {
                background-color: #f8f9fa;
                padding: 30px 0;
            }

            .main-content {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            /* Checkout progress box */
            .checkout-progress-wrap {
                background-color: #ffffff;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }

            .checkout-progress-wrap .steps {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .checkout-progress-wrap .steps li {
                display: flex;
                align-items: center;
                margin-bottom: 15px;
            }

            /* Order Summary */
            .order-summary {
                background-color: #ffffff;
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                padding: 20px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }

            .order-summary .title-block {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }

            .order-summary .title-block h3 {
                font-size: 20px;
                font-weight: bold;
                color: #333;
            }

            .order-summary .title-block .link-forward {
                font-size: 14px;
                color: #007bff;
                text-decoration: none;
            }

            .order-summary .title-block .link-forward:hover {
                text-decoration: underline;
            }

            /* Cart list */
            .cart-list-box {
                margin-bottom: 20px;
            }

            .cart-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 15px;
                padding-bottom: 15px;
                border-bottom: 1px solid #e0e0e0;
            }

            .cart-item:last-child {
                border-bottom: none;
            }

            .product-thumbnail {
                display: flex;
                align-items: center;
            }

            .product-thumbnail .prd-thumb {
                margin-right: 10px;
            }

            .product-thumbnail .prd-name {
                font-size: 16px;
                color: #333;
                text-decoration: none;
            }

            .product-thumbnail .prd-name:hover {
                text-decoration: underline;
            }

            .product-price,
            .product-quantity,
            .product-subtotal {
                font-size: 16px;
                color: #666;
            }

            /* Subtotal */
            .subtotal {
                list-style: none;
                padding: 0;
                margin-top: 20px;
            }

            .subtotal-line {
                display: flex;
                justify-content: space-between;
                font-size: 18px;
                font-weight: bold;
                margin-bottom: 10px;
            }

            /* Checkout button */
            .checkout-buttons {
                text-align: right;
            }

            .checkout-buttons .checkout-btn {
                background-color: #28a745;
                color: #fff;
                padding: 10px 20px;
                border: none;
                border-radius: 5px;
                text-decoration: none;
                font-size: 16px;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .checkout-buttons .checkout-btn:hover {
                background-color: #218838;
            }
            
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.7);
                display: flex;
                justify-content: center;
                align-items: center;
                z-index: 9999;
            }

            .loading-spinner {
                background-color: white;
                padding: 30px;
                border-radius: 10px;
                text-align: center;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
                color: #333; /* Đổi màu chữ thành đen để nhìn thấy rõ */
            }

            .loading-spinner p {
                margin-top: 15px;
                font-size: 16px;
                color: #333; /* Đảm bảo chữ màu đen */
                font-weight: bold;
            }

            .spinner {
                width: 50px;
                height: 50px;
                border: 5px solid #f3f3f3;
                border-top: 5px solid #4CAF50;
                border-radius: 50%;
                margin: 0 auto 15px;
                animation: spin 1s linear infinite;
                display: block; /* Đảm bảo hiển thị */
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }

            .notification-popup {
                position: fixed;
                top: 30px;
                right: 30px;
                width: 350px;
                background-color: white;
                border-radius: 8px;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
                z-index: 9999;
                animation: slideIn 0.5s forwards;
            }

            .notification-popup.hiding {
                animation: slideOut 0.5s forwards;
            }

            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }

            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }

            .notification-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px 15px;
                border-bottom: 1px solid #eee;
            }

            .notification-header h3 {
                margin: 0;
                font-size: 18px;
            }

            .notification-body {
                padding: 15px;
                color: #333;
            }

            .notification-body p {
                margin: 0;
                font-size: 16px;
            }

            .close-btn {
                background: none;
                border: none;
                font-size: 24px;
                cursor: pointer;
                color: #888;
            }

            .close-btn:hover {
                color: #333;
            }

            /* Kiểu dáng cho các loại thông báo */
            .notification-popup.success {
                border-left: 5px solid #4CAF50;
            }

            .notification-popup.success .notification-header {
                background-color: rgba(76, 175, 80, 0.1);
                color: #4CAF50;
            }

            .notification-popup.error {
                border-left: 5px solid #f44336;
            }

            .notification-popup.error .notification-header {
                background-color: rgba(244, 67, 54, 0.1);
                color: #f44336;
            }
        </style>

        <script>
            function confirmPayment() {
                // Hiển thị biểu tượng loading
                showLoadingOverlay();
                
                // Thêm log để kiểm tra
                console.log("Đang gửi request thanh toán...");
                
                fetch("<%= request.getContextPath() %>/checkout", {
                    method: "POST",
                    headers: {"Content-Type": "application/json"}
                })
                .then(response => {
                    console.log("Nhận được response:", response);
                    return response.json();
                })
                .then(data => {
                    // Ẩn loading
                    hideLoadingOverlay();
                    console.log("Dữ liệu trả về:", data);
                    
                    if(data.status === "success") {
                        const notification = showNotification('success', '✅ ĐẶT HÀNG THÀNH CÔNG', data.message || 'Đơn hàng của bạn đã được đặt thành công!');
                        
                        // Đảm bảo thông báo hiển thị ít nhất 1 giây trước khi chuyển trang
                        setTimeout(() => {
                            // Lưu thông báo thành công vào localStorage để hiển thị ở trang home
                            localStorage.setItem('orderSuccess', 'true');
                            localStorage.setItem('orderMessage', data.message || 'Đơn hàng của bạn đã được đặt thành công!');
                            
                            // Chuyển hướng về trang home
                            window.location.href = 'home';
                        }, 4000);
                    } else {
                        showNotification('error', '❌ LỖI ĐẶT HÀNG', data.message || 'Có lỗi xảy ra khi đặt hàng. Vui lòng thử lại!');
                    }
                })
                .catch(error => {
                    // Ẩn loading
                    hideLoadingOverlay();
                    console.error("Lỗi thanh toán:", error);
                    showNotification('error', '❌ LỖI HỆ THỐNG', 'Đã xảy ra lỗi trong quá trình thanh toán. Vui lòng thử lại sau.');
                });
            }

            function showLoadingOverlay() {
    const overlay = document.createElement('div');
    overlay.className = 'loading-overlay';
    overlay.innerHTML = `
        <div class="loading-spinner">
            <div class="spinner"></div>
            <p>Đang xử lý đơn hàng...</p>
        </div>
    `;
    document.body.appendChild(overlay);
}

            function hideLoadingOverlay() {
                const overlay = document.querySelector('.loading-overlay');
                if (overlay) {
                    overlay.remove();
                }
            }

            function showNotification(type, title, message) {
    // Xóa thông báo cũ nếu có
    const oldNotification = document.querySelector('.notification-popup');
    if (oldNotification) {
        oldNotification.remove();
    }
    
    // Tạo thông báo mới
    const notification = document.createElement('div');
    notification.className = `notification-popup ${type}`;
    notification.style.display = 'block'; // Đảm bảo hiển thị
    notification.style.opacity = '1';     // Đảm bảo hiển thị
    
    // Dùng trực tiếp style và tránh sử dụng biểu thức Ternary trong JavaScript template literal
    let headerBgColor = type == 'success' ? 'rgba(76, 175, 80, 0.1)' : 'rgba(244, 67, 54, 0.1)';
    let headerTextColor = type == 'success' ? '#4CAF50' : '#f44336';
    
    notification.innerHTML = 
        '<div class="notification-header" style="background-color: ' + headerBgColor + ';">' +
            '<h3 style="color: ' + headerTextColor + '; font-weight: bold;">' + title + '</h3>' +
            '<button class="close-btn" onclick="this.parentElement.parentElement.remove()">×</button>' +
        '</div>' +
        '<div class="notification-body">' +
            '<p style="color: #333; font-size: 16px;">' + message + '</p>' +
        '</div>';
    
    document.body.appendChild(notification);
    
    // Hiển thị log để debugging
    console.log("Notification created:", {type, title, message, element: notification});
    
    // Thêm log để xác nhận thông báo đã được thêm vào DOM
    console.log("Notification added to DOM:", document.body.contains(notification));
    
    // Tự động ẩn sau 5 giây
    setTimeout(() => {
        notification.classList.add('hiding');
        setTimeout(() => {
            notification.remove();
        }, 500);
    }, 5000);
    
    return notification;
}
        </script>
        
        ${notificationScript}
        
    </head>
    <body class="biolife-body">

        <!-- Preloader -->
        <div id="biof-loading">
            <div class="biof-loading-center">
                <div class="biof-loading-center-absolute">
                    <div class="dot dot-one"></div>
                    <div class="dot dot-two"></div>
                    <div class="dot dot-three"></div>
                </div>
            </div>
        </div>

        <!-- HEADER -->
        <jsp:include page="Menu.jsp"></jsp:include>

            <!--Hero Section-->
            <div class="hero-section hero-background">
                <h1 class="page-title">Checkout</h1>
            </div>

            <!--Navigation section-->
            <div class="container">
                <nav class="biolife-nav">
                    <ul>
                        <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                        <li class="nav-item"><span class="current-page">Shopping Cart</span></li>
                    </ul>
                </nav>
            </div>

            <div class="page-contain checkout">
                <div class="checkout-container">
                    <div class="order-wrapper">
                        <div class="order-summary">
                            <div class="order-header">
                                <h3>Order Summary</h3>
                                <a href="cart" class="edit-link">Edit cart</a>
                            </div>

                            <div class="cart-items">
                                <div class="items-count">${sessionScope.cart.items.size()} items</div>

                            <c:forEach items="${sessionScope.cart.items}" var="item">
                                <div class="cart-item">
                                    <div class="item-image">
                                        <img src="${item.product.img}" alt="${item.product.name}">
                                    </div>
                                    <div class="item-details">
                                        <h4>${item.product.name}</h4>
                                        <div class="item-price"><fmt:formatNumber value="${item.product.price}" type="currency"/></div>
                                        <div class="item-quantity">Số lượng: ${item.quantity}</div>
                                        <div class="item-total">Tổng: <fmt:formatNumber value="${item.product.price * item.quantity}" type="currency"/></div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="order-total">
                            <span>Tổng tiền:</span>
                            <span class="total-amount"><fmt:formatNumber value="${sessionScope.cart.totalAmount}" type="currency"/></span>
                        </div>

                        <button class="checkout-button" onclick="confirmPayment()">
                            Đặt hàng
                        </button>
                        <!-- VNPay payment integration removed -->
                    </div>
                </div>
            </div>
        </div>

        <style>
            .page-contain.checkout {
                padding: 40px 0;
                background: #f5f5f5;
            }

            .checkout-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }

            .order-wrapper {
                max-width: 800px;
                margin: 0 auto;
            }

            .order-summary {
                background: white;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                padding: 24px;
            }

            .order-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 24px;
                border-bottom: 1px solid #eee;
                padding-bottom: 16px;
            }

            .order-header h3 {
                font-size: 24px;
                color: #333;
                margin: 0;
            }

            .edit-link {
                color: #4CAF50;
                text-decoration: none;
            }

            .cart-items {
                margin-bottom: 24px;
            }

            .items-count {
                color: #666;
                margin-bottom: 16px;
            }

            .cart-item {
                display: flex;
                gap: 20px;
                padding: 16px 0;
                border-bottom: 1px solid #eee;
            }

            .item-image {
                width: 100px;
                height: 100px;
            }

            .item-image img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 4px;
            }

            .item-details {
                flex: 1;
            }

            .item-details h4 {
                margin: 0 0 8px 0;
                color: #333;
            }

            .item-price {
                font-weight: bold;
                color: #4CAF50;
            }

            .item-quantity {
                color: #666;
                margin: 4px 0;
            }

            .item-total {
                font-weight: bold;
                color: #333;
            }

            .order-total {
                display: flex;
                justify-content: space-between;
                align-items: center;
                font-size: 20px;
                font-weight: bold;
                margin: 24px 0;
                padding-top: 16px;
                border-top: 2px solid #eee;
            }

            .total-amount {
                color: #4CAF50;
            }

            .checkout-button {
                width: 100%;
                padding: 16px;
                background: #4CAF50;
                color: white;
                border: none;
                border-radius: 4px;
                font-size: 16px;
                font-weight: bold;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .checkout-button:hover {
                background: #45a049;
            }
        </style>

    </div>

    <!-- FOOTER -->
    <jsp:include page="Footer.jsp"></jsp:include>

    <!--Footer For Mobile-->
    <div class="mobile-footer">
        <div class="mobile-footer-inner">
            <div class="mobile-block block-menu-main">
                <a class="menu-bar menu-toggle btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                    <span class="fa fa-bars"></span>
                    <span class="text">Menu</span>
                </a>
            </div>
            <div class="mobile-block block-sidebar">
                <a class="menu-bar filter-toggle btn-toggle" data-object="open-mobile-filter" href="javascript:void(0)">
                    <i class="fa fa-sliders" aria-hidden="true"></i>
                    <span class="text">Sidebar</span>
                </a>
            </div>
            <div class="mobile-block block-minicart">
                <a class="link-to-cart" href="#">
                    <span class="fa fa-shopping-bag" aria-hidden="true"></span>
                    <span class="text">Cart</span>
                </a>
            </div>
            <div class="mobile-block block-global">
                <a class="menu-bar myaccount-toggle btn-toggle" data-object="global-panel-opened" href="javascript:void(0)">
                    <span class="fa fa-globe"></span>
                    <span class="text">Global</span>
                </a>
            </div>
        </div>
    </div>

    <div class="mobile-block-global">
        <div class="biolife-mobile-panels">
            <span class="biolife-current-panel-title">Global</span>
            <a class="biolife-close-btn" data-object="global-panel-opened" href="#">&times;</a>
        </div>
        <div class="block-global-contain">
            <div class="glb-item my-account">
                <b class="title">My Account</b>
                <ul class="list">
                    <li class="list-item"><a href="#">Login/register</a></li>
                    <li class="list-item"><a href="#">Wishlist <span class="index">(8)</span></a></li>
                    <li class="list-item"><a href="#">Checkout</a></li>
                </ul>
            </div>
            <div class="glb-item currency">
                <b class="title">Currency</b>
                <ul class="list">
                    <li class="list-item"><a href="#">? EUR (Euro)</a></li>
                    <li class="list-item"><a href="#">$ USD (Dollar)</a></li>
                    <li class="list-item"><a href="#">£ GBP (Pound)</a></li>
                    <li class="list-item"><a href="#">¥ JPY (Yen)</a></li>
                </ul>
            </div>
            <div class="glb-item languages">
                <b class="title">Language</b>
                <ul class="list inline">
                    <li class="list-item"><a href="#"><img src="assets/images/languages/us.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/fr.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/ger.jpg" alt="flag" width="24" height="18"></a></li>
                    <li class="list-item"><a href="#"><img src="assets/images/languages/jap.jpg" alt="flag" width="24" height="18"></a></li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Scroll Top Button -->
    <a class="btn-scroll-top"><i class="biolife-icon icon-left-arrow"></i></a>

    <script src="assets/js/jquery-3.4.1.min.js"></script>
    <script src="assets/js/bootstrap.min.js"></script>
    <script src="assets/js/jquery.countdown.min.js"></script>
    <script src="assets/js/jquery.nice-select.min.js"></script>
    <script src="assets/js/jquery.nicescroll.min.js"></script>
    <script src="assets/js/slick.min.js"></script>
    <script src="assets/js/biolife.framework.js"></script>
    <script src="assets/js/functions.js"></script>
    
</body>

</html>