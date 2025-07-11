<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html class="no-js" lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Products - Da Nang Craft Village</title>
        
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&amp;display=swap" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&amp;display=swap" rel="stylesheet">
        <link rel="shortcut icon" type="image/x-icon" href="hinhanh/Logo/cropped-Favicon-1-32x32.png" />
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/animate.min.css">
        <link rel="stylesheet" href="assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/css/nice-select.css">
        <link rel="stylesheet" href="assets/css/slick.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/main-color03-green.css">
        
        <script>
            function addToCart(productId, quantity) {
                fetch("cart?action=add&id=" + productId + "&quantity=" + quantity, {
                    method: "POST",
                    credentials: 'same-origin'
                })
                .then(response => {
                    if (response.redirected) {
                        alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
                        window.location.href = 'Login.jsp';
                        return;
                    }
                    alert("Đã thêm sản phẩm vào giỏ hàng!");
                })
                .catch(error => {
                    console.error("Lỗi:", error);
                });
            }
        </script>
    </head>
    <body class="biolife-body">
        <!-- HEADER/MENU -->
        <jsp:include page="Menu.jsp"></jsp:include>

        <div class="page-contain" style="padding: 60px 0; background: #f9f9f9; margin-top: 100px;">
            <!-- Main content -->
            <div class="container">
                <div class="row">
                    <!-- Sidebar filters -->
                    <div class="col-12 col-md-3">
                        <div class="top-functions-area">
                            <div class="flt-item to-left group-on-mobile">
                                <span class="flt-title">Refine</span>
                                <a href="#" class="icon-for-mobile">
                                    <span></span>
                                    <span></span>
                                    <span></span>
                                </a>
                                <div class="wrap-selectors">
                                    <form id="filterForm" name="frm-refine" method="get" action="product">
                                        <input type="hidden" name="action" value="${param.action}">
                                        <span class="title-for-mobile">Refine Products By</span>
                                        <div data-title="Price:" class="selector-item">
                                            <select name="price" class="selector" onchange="submitForm()">
                                                <option value="all" ${selectedPrice == 'all' ? 'selected' : ''}>All Prices</option>
                                                <option value="0-100000" ${selectedPrice == '0-100000' ? 'selected' : ''}>Less than 100k</option>
                                                <option value="100000-500000" ${selectedPrice == '100000-500000' ? 'selected' : ''}>100k - 500k</option>
                                                <option value="500000-1000000" ${selectedPrice == '500000-1000000' ? 'selected' : ''}>500k - 1M</option>
                                                <option value="1000000+" ${selectedPrice == '1000000+' ? 'selected' : ''}>More than 1M</option>
                                            </select>
                                        </div>
                                        <div class="flt-item to-right" style="padding-left: 100px">
                                            <span class="flt-title">Sort</span>
                                            <div class="wrap-selectors">
                                                <div class="selector-item orderby-selector">
                                                    <select name="orderby" class="orderby" aria-label="Shop order" onchange="submitForm()">
                                                        <option value="menu_order" ${param.orderby == 'menu_order' ? 'selected' : ''}>Mặc định</option>
                                                        <option value="date" ${param.orderby == 'date' ? 'selected' : ''}>Mới nhất</option>
                                                        <option value="price" ${param.orderby == 'price' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                                                        <option value="price-desc" ${param.orderby == 'price-desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <!-- Newest Products Widget -->
                        <div class="widget biolife-filter" style="margin-top: 30px;">
                            <h4 class="wgt-title">Newest</h4>
                            <div class="wgt-content">
                                <ul class="products">
                                    <c:forEach var="p" items="${list5}">
                                        <li class="pr-item">
                                            <div class="contain-product style-widget">
                                                <div class="product-thumb">
                                                    <a href="detail?pid=${p.id}" class="link-to-product" tabindex="0">
                                                        <img src="${p.img}" alt="${p.name}" width="270" height="270" class="product-thumnail">
                                                    </a>
                                                </div>
                                                <div class="info">
                                                    <!-- Debug: Categories count: ${fn:length(listCC)} -->
                                                    <c:if test="${empty listCC}">
                                                        <b class="categories">No categories loaded</b>
                                                    </c:if>
                                                    <c:if test="${not empty listCC}">
                                                        <c:set var="categoryFound" value="false"/>
                                                        <c:forEach var="cat" items="${listCC}">
                                                            <c:if test="${cat.categoryID == p.cateID}">
                                                                <b class="categories">${cat.categoryName}</b>
                                                                <c:set var="categoryFound" value="true"/>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:if test="${!categoryFound}">
                                                            <b class="categories">Product catID: ${p.cateID}</b>
                                                        </c:if>
                                                    </c:if>
                                                    <h4 class="product-title"><a href="detail?pid=${p.id}" class="pr-name" tabindex="0">${p.name}</a></h4>
                                                    <div class="price">
                                                        <ins><span class="price-amount"><fmt:formatNumber value="${p.price}" type="currency"/></span></ins>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <!-- Product listing -->
                    <div class="col-12 col-md-9">
                        <div class="biolife-title-box">
                            <span class="subtitle">Tất cả sản phẩm</span>
                            <h3 class="main-title">Sản phẩm thủ công mỹ nghệ</h3>
                        </div>

                        <div class="product-grid">
                            <div class="row">
                                <c:set var="pageSize" value="9"/>
                                <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
                                <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
                                <c:set var="end" value="${start + pageSize}"/>
                                <c:set var="totalProducts" value="${fn:length(listP)}"/>
                                <c:set var="totalPages" value="${(totalProducts % pageSize == 0) ? (totalProducts / pageSize) : (totalProducts / pageSize + 1)}"/>

                                <c:forEach var="o" items="${listP}" varStatus="status">
                                    <c:if test="${status.index >= start && status.index < end}">
                                        <div class="col-12 col-md-6 col-lg-4">
                                            <div class="product-item">
                                                <div class="contain-product layout-default">
                                                    <div class="product-thumb">
                                                        <a href="detail?pid=${o.id}" class="link-to-product">
                                                            <figure style="margin:0;padding:0;width:100%;height:270px;overflow:hidden;position:relative;border-radius:8px;background-color:#f8f8f8;">
                                                                <img src="${o.img}" alt="${o.name}" class="product-thumnail" style="width:100%;height:100%;object-fit:contain;">
                                                            </figure>
                                                        </a>
                                                    </div>
                                                    <div class="info" style="padding: 15px;">
                                                        <!-- Debug: Categories count: ${fn:length(listCC)} -->
                                                        <c:if test="${empty listCC}">
                                                            <b class="categories">No categories loaded</b>
                                                        </c:if>
                                                        <c:if test="${not empty listCC}">
                                                            <c:set var="categoryFound" value="false"/>
                                                            <c:forEach var="cat" items="${listCC}">
                                                                <c:if test="${cat.categoryID == o.cateID}">
                                                                    <b class="categories">${cat.categoryName}</b>
                                                                    <c:set var="categoryFound" value="true"/>
                                                                </c:if>
                                                            </c:forEach>
                                                            <c:if test="${!categoryFound}">
                                                                <b class="categories">Product catID: ${o.cateID}</b>
                                                            </c:if>
                                                        </c:if>
                                                        <h4 class="product-title" style="margin-bottom: 10px;">
                                                            <a href="detail?pid=${o.id}" class="pr-name" style="color:#333;font-size:16px;font-weight:500;text-decoration:none;">${o.name}</a>
                                                        </h4>
                                                        <div class="price" style="margin-bottom: 15px;">
                                                            <ins><span class="price-amount" style="color:#4CAF50;font-size:18px;font-weight:600;"><fmt:formatNumber value="${o.price}" type="currency"/></span></ins>
                                                        </div>
                                                        <div class="slide-down-box">
                                                            <div class="buttons" style="display:flex;gap:10px;justify-content:center;">
                                                                <form action="wishlist" method="post" style="display:inline;">
                                                                    <input type="hidden" name="action" value="add">
                                                                    <input type="hidden" name="userID" value="<%= session.getAttribute("userID") %>">
                                                                    <input type="hidden" name="productID" value="${o.id}">
                                                                    <input type="hidden" name="returnUrl" value="product">
                                                                    <button type="submit" class="btn wishlist-btn" style="background:#fff;border:1px solid #4CAF50;color:#4CAF50;padding:8px 15px;border-radius:4px;" onclick="return confirm('Thêm sản phẩm vào wishlist?')">
                                                                        <i class="fa fa-heart" aria-hidden="true"></i>
                                                                    </button>
                                                                </form>
                                                                <a onclick="addToCart('${o.id}', 1)" class="btn add-to-cart-btn" style="background:#4CAF50;color:#fff;padding:8px 15px;border-radius:4px;text-decoration:none;">
                                                                    <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> Thêm vào giỏ
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                            </div>
                        </div>

                        <!-- Pagination -->
                        <div class="pagination" style="display:flex;justify-content:center;align-items:center;margin-top:20px;">
                            <c:if test="${currentPage > 1}">
                                <a href="product?page=${currentPage - 1}" class="prev">&laquo; Previous</a>
                            </c:if>

                            <c:forEach var="i" begin="1" end="${totalPages}">
                                <a href="product?page=${i}" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                            </c:forEach>

                            <c:if test="${currentPage < totalPages}">
                                <a href="product?page=${currentPage + 1}" class="next">Next &raquo;</a>
                            </c:if>
                        </div>
                        <style>
                            .pagination {
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                margin-top: 20px;
                                gap: 4px;
                            }
                            .pagination a {
                                padding: 8px 16px;
                                margin: 0 2px;
                                border: 1px solid #ddd;
                                text-decoration: none;
                                color: #333;
                                border-radius: 4px;
                                display: inline-block;
                                min-width: 40px;
                                text-align: center;
                            }
                            .pagination a:hover, .pagination a.active {
                                background-color: #4CAF50;
                                color: white;
                            }
                            .pagination a.prev, .pagination a.next {
                                font-weight: bold;
                            }
                            /* Ensure slide-down-box works properly */
                            .product-grid .product-item {
                                overflow: visible;
                                margin-bottom: 40px; /* Add extra space for slide-down effect */
                            }
                            .product-grid .contain-product {
                                position: relative;
                                overflow: visible;
                            }
                            .product-grid .contain-product .info {
                                position: relative;
                                z-index: 1;
                            }
                            /* Ensure slide-down-box appears properly on hover */
                            .product-grid .slide-down-box {
                                z-index: 10;
                            }
                            /* Smooth transition for product hover effect */
                            .product-grid .contain-product.layout-default {
                                transition: all 0.3s ease;
                            }
                            .product-grid .contain-product.layout-default:hover {
                                transform: translateY(-2px);
                            }
                            /* Make sure buttons are accessible when slide-down appears */
                            .product-grid .slide-down-box .buttons {
                                padding: 10px 15px;
                            }
                            /* Category styling */
                            .categories {
                                color: #4CAF50;
                                font-size: 12px;
                                text-transform: uppercase;
                                margin-bottom: 5px;
                                display: block;
                            }
                        </style>
                    </div>
                </div>
            </div>
        </div>

        <!-- FOOTER -->
        <jsp:include page="Footer.jsp"></jsp:include>

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
        function submitForm() {
            document.getElementById('filterForm').submit();
        }
        </script>
    </body>
</html> 