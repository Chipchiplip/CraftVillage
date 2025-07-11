<%-- ShoppingCart.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="0">
        <title>Da Nang Craft Village</title>
        <!-- Fonts & Favicon -->
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
        <link rel="shortcut icon" type="image/x-icon" href="hinh anh/Logo/cropped-Favicon-1-32x32.png" />
        <!-- Stylesheets -->
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/animate.min.css">
        <link rel="stylesheet" href="assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/css/nice-select.css">
        <link rel="stylesheet" href="assets/css/slick.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/main-color.css">
        <link rel="stylesheet" href="assets/css/main-color03-green.css">
        <style>
            /* Remove button styling */
            .btn-remove {
                background: none;
                border: none;
                color: #666;
                cursor: pointer;
                font-size: 16px;
                padding: 5px 8px;
                border-radius: 3px;
                transition: all 0.3s ease;
            }
            .btn-remove:hover {
                color: #e74c3c;
                background-color: #f8f9fa;
            }
            .fa-times {
                font-size: 18px;
            }

            /* Table styling for both products and tickets */
            .shop_table.cart-form {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 30px;
            }

            .shop_table.cart-form th {
                background-color: #f8f9fa;
                font-weight: 600;
                color: #333;
                padding: 15px 10px;
                border-bottom: 2px solid #dee2e6;
                text-align: left;
            }

            .shop_table.cart-form td {
                padding: 15px 10px;
                vertical-align: middle;
                border-bottom: 1px solid #dee2e6;
            }

            /* Product table specific styling */
            .price-total-container {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            /* Ticket table specific styling */
            .ticket-type-cell {
                font-weight: 500;
                color: #333;
            }

            .ticket-village-cell {
                color: #666;
                font-size: 14px;
            }

            .ticket-date-cell {
                color: #999;
                font-size: 13px;
            }

            .ticket-action-cell {
                text-align: center;
                width: 60px;
            }

            /* Quantity form styling */
            .quantity-box.type1 .qty-input form,
            .ticket-quantity-cell form {
                display: flex;
                align-items: center;
                gap: 8px;
            }

            input[type="number"] {
                height: 35px;
                padding: 0 10px;
                border: 1px solid #e6e6e6;
                border-radius: 4px;
                text-align: center;
            }

            .quantity-box.type1 .qty-input input[type="number"] {
                width: 80px;
            }

            .ticket-quantity-cell input[type="number"] {
                width: 60px;
            }

            .btn.btn-info {
                padding: 5px 10px;
                font-size: 12px;
                border-radius: 4px;
                background-color: #17a2b8;
                border-color: #17a2b8;
                color: white;
            }

            .btn.btn-info:hover {
                background-color: #138496;
                border-color: #117a8b;
            }

            /* Price styling consistency */
            .price.price-contain {
                margin: 0;
            }

            .price-amount {
                font-weight: 500;
                color: #7FAF51;
            }
        </style>
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

            <!-- Hero Section -->
            <div class="hero-section hero-background">
                <h1 class="page-title">Cart</h1>
            </div>

            <!-- Navigation section -->
            <div class="container">
                <nav class="biolife-nav">
                    <ul>
                        <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                        <li class="nav-item"><span class="current-page">ShoppingCart</span></li>
                    </ul>
                </nav>
            </div>

            <!-- Main content -->
            <div class="page-contain shopping-cart">
                <div id="main-content" class="main-content">
                    <div class="container">
                        <div class="shopping-cart-container">
                            <div class="row">
                                <div class="col-lg-9 col-md-12 col-sm-12 col-xs-12">
                                    <!-- Cart Products Table -->
                                    <h3 class="box-title">Your cart items (Products)</h3>
                                    <div class="shopping-cart-container">
                                        <table class="shop_table cart-form">
                                            <thead>
                                                <tr>
                                                    <th class="product-name">Product Name</th>
                                                    <th class="product-price">Price</th>
                                                    <th class="product-quantity">Quantity</th>
                                                    <th class="product-subtotal">Total</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                            <c:choose>
                                                <c:when test="${empty cartItems}">
                                                    <tr>
                                                        <td colspan="4" class="text-center">
                                                            <div class="empty-cart-container" style="padding: 30px; text-align: center;">
                                                                <h3 style="color: #666; margin-bottom: 15px;">Giỏ hàng của bạn đang trống</h3>
                                                                <p style="color: #999; margin-bottom: 20px;">Hãy thêm sản phẩm vào giỏ hàng để tiếp tục mua sắm</p>
                                                                <a href="home" class="btn btn-primary">Tiếp tục mua sắm</a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach items="${cartItems}" var="item">
                                                        <tr class="cart_item">
                                                            <td class="product-thumbnail" data-title="Product Name">
                                                                <a class="prd-thumb" href="#">
                                                                    <figure><img width="113" height="113" src="${item.imageUrl}" alt="shipping cart"></figure>
                                                                </a>
                                                                <a class="prd-name" href="#">${item.productName}</a>
                                                            </td>
                                                            <td class="product-price" data-title="Price">
                                                                <div class="price price-contain">
                                                                    <ins><span class="price-amount"><span class="currencySymbol"></span><fmt:formatNumber value="${item.price}" type="currency"/></span></ins>
                                                                </div>
                                                            </td>
                                                            <td class="product-quantity" data-title="Quantity">
                                                                <div class="quantity-box type1">
                                                                    <div class="qty-input">
                                                                        <form action="cart" method="POST" style="display:flex;align-items:center;">
                                                                            <input type="hidden" name="action" value="update">
                                                                            <input type="hidden" name="id" value="${item.productID}">
                                                                            <input type="hidden" name="timestamp" value="<%= System.currentTimeMillis()%>">
                                                                            <input type="number" name="quantity" value="${item.quantity}" min="1"
                                                                                   style="width:80px; height:35px; padding:0 10px; border:1px solid #e6e6e6;"
                                                                                   oninput="this.value = Math.abs(this.value || 1)">
                                                                            <button type="submit" class="btn btn-info" style="padding:5px 10px; margin-left:10px;">
                                                                                <i class="fa fa-refresh"></i> Update
                                                                            </button>
                                                                        </form>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td class="product-subtotal" data-title="Total">
                                                                <div class="price-total-container" style="display: flex; justify-content: space-between; align-items: center;">
                                                                    <div class="price price-contain">
                                                                        <ins><span class="price-amount"><span class="currencySymbol"></span><fmt:formatNumber value="${item.price * item.quantity}" type="currency"/></span></ins>
                                                                    </div>
                                                                    <form action="cart" method="POST" style="display: inline;">
                                                                        <input type="hidden" name="action" value="remove">
                                                                        <input type="hidden" name="id" value="${item.productID}">
                                                                        <input type="hidden" name="timestamp" value="<%= System.currentTimeMillis()%>">
                                                                        <button type="submit" class="btn-remove" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">
                                                                            <i class="fa fa-times" aria-hidden="true"></i>
                                                                        </button>
                                                                    </form>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Cart Tickets Table -->
                                <c:if test="${not empty cartTickets}">
                                    <h3 class="box-title">Your cart items (Tickets)</h3>
                                    <div class="shopping-cart-container">
                                        <table class="shop_table cart-form">
                                            <thead>
                                                <tr>
                                                    <th>Ticket Type</th>
                                                    <th>Village</th>
                                                    <th>Date</th>
                                                    <th>Price</th>
                                                    <th>Quantity</th>
                                                    <th>Total</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach items="${cartTickets}" var="ticket">
                                                    <tr class="cart_ticket_item">
                                                        <td class="ticket-type-cell">${ticket.ticketTypeName}</td>
                                                        <td class="ticket-village-cell">${ticket.villageName}</td>
                                                        <td class="ticket-date-cell">${ticket.formattedTicketDate}</td>
                                                        <td class="ticket-price-cell">
                                                            <div class="price price-contain">
                                                                <ins><span class="price-amount"><span class="currencySymbol"></span><fmt:formatNumber value="${ticket.price}" type="currency"/></span></ins>
                                                            </div>
                                                        </td>
                                                        <td class="ticket-quantity-cell">
                                                            <form action="cart" method="POST" style="display:flex;align-items:center;">
                                                                <input type="hidden" name="action" value="updateTicket">
                                                                <input type="hidden" name="itemId" value="${ticket.cartTicketId}">
                                                                <input type="hidden" name="timestamp" value="<%= System.currentTimeMillis()%>">
                                                                <input type="number" name="quantity" value="${ticket.quantity}" min="1"
                                                                       style="width:60px; height:35px; padding:0 10px; border:1px solid #e6e6e6; text-align: center;">
                                                                <button type="submit" class="btn btn-info" style="padding:5px 10px; margin-left:10px;">
                                                                    <i class="fa fa-refresh"></i> Update
                                                                </button>
                                                            </form>
                                                        </td>
                                                        <td class="ticket-total-cell">
                                                            <div class="price price-contain">
                                                                <ins><span class="price-amount"><span class="currencySymbol"></span><fmt:formatNumber value="${ticket.price * ticket.quantity}" type="currency"/></span></ins>
                                                            </div>
                                                        </td>
                                                        <td class="ticket-action-cell">
                                                            <form action="cart" method="POST" style="display: inline;">
                                                                <input type="hidden" name="action" value="removeTicket">
                                                                <input type="hidden" name="itemId" value="${ticket.cartTicketId}">
                                                                <input type="hidden" name="timestamp" value="<%= System.currentTimeMillis()%>">
                                                                <button type="submit" class="btn-remove" onclick="return confirm('Bạn có chắc muốn xóa vé này?');">
                                                                    <i class="fa fa-times" aria-hidden="true"></i>
                                                                </button>
                                                            </form>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </c:if>

                                <!-- Tổng tiền và các nút điều khiển -->
                                <p style="font-size: 24px; color: #7FAF51; font-weight: bold;">
                                    Total: <fmt:formatNumber value="${grandTotal}" type="currency"/>
                                </p>
                                <div class="wrap-btn-control" style="margin-bottom: 30px;">
                                    <a href="home" class="btn back-to-shop">Back to Shop</a>
                                    <form action="cart" method="post" style="display: inline;" onsubmit="return confirm('Bạn có chắc muốn xóa toàn bộ giỏ hàng?');">
                                        <input type="hidden" name="action" value="clear">
                                        <input type="hidden" name="timestamp" value="<%= System.currentTimeMillis()%>">
                                        <button type="submit" class="btn btn-clear">Clear Cart</button>
                                    </form>
                                    <a href="checkoutBefor" class="btn btn-update">Checkout</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp"></jsp:include>

        <!-- Mobile Footer & Global Panel -->
        <!-- ... (giữ nguyên như cũ, không cần thay đổi) ... -->

        <!-- Scripts -->
        <script src="assets/js/jquery-3.4.1.min.js"></script>
        <script src="assets/js/bootstrap.min.js"></script>
        <script src="assets/js/jquery.countdown.min.js"></script>
        <script src="assets/js/jquery.nice-select.min.js"></script>
        <script src="assets/js/jquery.nicescroll.min.js"></script>
        <script src="assets/js/slick.min.js"></script>
        <script src="assets/js/biolife.framework.js"></script>
        <script src="assets/js/functions.js"></script>

        <script>
                        $(document).ready(function () {
                            // Prevent browser caching
                            window.history.replaceState(null, null, window.location.href);

                            // Force refresh input values to prevent cached values
                            $('input[type="number"]').each(function () {
                                var currentValue = $(this).val();
                                $(this).val('');
                                $(this).val(currentValue);
                            });

                            // Handle quantity input changes to ensure fresh values
                            $('input[name="quantity"]').on('focus', function () {
                                // Clear any cached value and set to current value
                                var currentValue = $(this).val();
                                $(this).val('');
                                $(this).val(currentValue);
                                $(this).select(); // Select all text for easy editing
                            });

                            // Prevent form resubmission on page reload
                            if (window.history.replaceState) {
                                window.history.replaceState(null, null, window.location.href);
                            }

                            // Clear any pending form data
                            if (typeof (Storage) !== "undefined") {
                                sessionStorage.removeItem('formData');
                            }
                        });

                        // Function to force form refresh
                        function refreshFormValues() {
                            $('input[type="number"]').each(function () {
                                $(this).val($(this).val());
                            });
                        }

                        // Call refresh when page becomes visible (handles browser back/forward)
                        document.addEventListener('visibilitychange', function () {
                            if (!document.hidden) {
                                refreshFormValues();
                            }
                        });

                        // Handle page focus events
                        window.addEventListener('focus', function () {
                            refreshFormValues();
                        });
        </script>
    </body>
</html>
