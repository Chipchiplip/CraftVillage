<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>

<!DOCTYPE html>
<html class="no-js" lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tìm kiếm sản phẩm - Da Nang Craft Village</title>
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
    <link rel="stylesheet" href="assets/css/main-color.css">
    <link rel="stylesheet" href="assets/css/main-color03-green.css">
    <style>
        .hero-section .page-subtitle {
            color: #ffffff;
            font-size: 18px;
            margin-top: 10px;
            opacity: 0.9;
        }
        .cat-list-item.selected .cat-link {
            font-weight: bolder;
            color: #4CAF50;
        }
        .search-results-info {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #4CAF50;
        }
        .search-results-info h5 {
            margin: 0;
            color: #333;
            font-weight: 600;
        }
        .search-results-info .result-count {
            color: #4CAF50;
            font-weight: bold;
        }
        /* Product Grid Styling */
        .product-grid .product-item {
            margin-bottom: 30px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }
        .product-grid .product-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 20px rgba(0,0,0,0.15);
        }
        .product-grid .contain-product {
            border: none;
            border-radius: 8px;
            overflow: hidden;
        }
        .product-grid .product-thumb {
            border-radius: 8px 8px 0 0;
            overflow: hidden;
        }
        .product-grid .info {
            text-align: center;
        }
        .product-grid .buttons a {
            transition: all 0.3s ease;
        }
        .product-grid .buttons a:hover {
            transform: scale(1.05);
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
    </style>
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
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // Category mapping for display
            var categoryMap = {};
            <c:forEach var="cat" items="${listCC}">
                categoryMap[${cat.categoryID}] = "${cat.categoryName}";
            </c:forEach>

            // Cập nhật tất cả các phần tử có class "categories"
            var categoryElements = document.querySelectorAll(".categories");
            categoryElements.forEach(function (element) {
                // Lấy cateId từ data attribute của phần tử
                var cateid = element.getAttribute('data-category-id');
                if (cateid) {
                    element.textContent = categoryMap[cateid] || "Danh mục sản phẩm";
                }
            });

            // Cập nhật tiêu đề danh mục trong breadcrumb nếu có
            var breadcrumbCategory = document.querySelector(".permal-link + li span");
            if (breadcrumbCategory) {
                var cateid = breadcrumbCategory.getAttribute('data-category-id');
                if (cateid) {
                    breadcrumbCategory.textContent = categoryMap[cateid] || "Danh mục sản phẩm";
                }
            }
        });
    </script>
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
        <c:choose>
            <c:when test="${not empty param.txt || not empty searchKeyword}">
                <h1 class="page-title">Kết quả tìm kiếm</h1>
                <p class="page-subtitle">Tìm kiếm cho: "<strong>${not empty param.txt ? param.txt : searchKeyword}</strong>"</p>
            </c:when>
            <c:when test="${not empty categoryName}">
                <h1 class="page-title">${categoryName}</h1>
            </c:when>
            <c:otherwise>
                <h1 class="page-title">Danh mục sản phẩm</h1>
            </c:otherwise>
        </c:choose>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                <c:choose>
                    <c:when test="${not empty param.txt || not empty searchKeyword}">
                        <li class="nav-item"><span class="current-page">Kết quả tìm kiếm: "${not empty param.txt ? param.txt : searchKeyword}"</span></li>
                    </c:when>
                    <c:when test="${not empty cid}">
                        <li class="nav-item"><a href="category?cid=${cid}" class="permal-link">${categoryName}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><span class="current-page">Danh mục sản phẩm</span></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>

    <div class="page-contain" style="padding: 60px 0; background: #f9f9f9;">
        <div class="container">
            <div class="row">
                <!-- Sidebar for search -->
                <c:if test="${not empty param.txt || not empty searchKeyword}">
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
                                    <form id="filterForm" name="frm-refine" method="get" action="search">
                                        <input type="hidden" name="txt" value="${not empty param.txt ? param.txt : searchKeyword}">
                                        <span class="title-for-mobile">Refine Products By</span>
                                        <div data-title="Price:" class="selector-item">
                                            <select name="price" class="selector" onchange="submitForm()">
                                                <option value="all" ${selectedPrice == 'all' ? 'selected' : ''}>All Prices</option>
                                                <option value="0-100000" ${selectedPrice == '0-100000' ? 'selected' : ''}>Dưới 100.000 VNĐ</option>
                                                <option value="100000-500000" ${selectedPrice == '100000-500000' ? 'selected' : ''}>100.000 - 500.000 VNĐ</option>
                                                <option value="500000-1000000" ${selectedPrice == '500000-1000000' ? 'selected' : ''}>500.000 - 1.000.000 VNĐ</option>
                                                <option value="1000000+" ${selectedPrice == '1000000+' ? 'selected' : ''}>Trên 1.000.000 VNĐ</option>
                                            </select>
                                        </div>
                                        <div class="flt-item to-right" style="padding-left: 0px; margin-top: 15px;">
                                            <span class="flt-title">Sort</span>
                                            <div class="wrap-selectors">
                                                <form id="sortForm" name="frm-sort" method="get" action="${not empty param.txt or not empty searchKeyword ? 'search' : 'category'}">
                                                    <c:choose>
                                                        <c:when test="${not empty param.txt or not empty searchKeyword}">
                                                            <input type="hidden" name="txt" value="${not empty param.txt ? param.txt : searchKeyword}">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" name="cid" value="${cid}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <input type="hidden" name="price" value="${param.price}">
                                                    <div class="selector-item orderby-selector">
                                                        <select name="orderby" class="orderby" aria-label="Shop order" onchange="submitSortForm()">
                                                            <option value="menu_order" ${param.orderby == 'menu_order' ? 'selected' : ''}>Mặc định</option>
                                                            <option value="name_asc" ${param.orderby == 'name_asc' ? 'selected' : ''}>Tên A-Z</option>
                                                            <option value="name_desc" ${param.orderby == 'name_desc' ? 'selected' : ''}>Tên Z-A</option>
                                                            <option value="price_asc" ${param.orderby == 'price_asc' ? 'selected' : ''}>Giá: Thấp đến Cao</option>
                                                            <option value="price_desc" ${param.orderby == 'price_desc' ? 'selected' : ''}>Giá: Cao đến Thấp</option>
                                                        </select>
                                                    </div>
                                                </form>
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
                                                    <a href="product?pid=${p.pid}" class="link-to-product" tabindex="0">
                                                        <img src="${p.mainImageUrl}" alt="${p.name}" width="270" height="270" class="product-thumnail">
                                                    </a>
                                                </div>
                                                <div class="info">
                                                    <b class="categories">Sản phẩm</b>
                                                    <h4 class="product-title"><a href="product?pid=${p.pid}" class="pr-name" tabindex="0">${p.name}</a></h4>
                                                    <div class="price">
                                                        <ins><span class="price-amount"><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="VNĐ"/></span></ins>
                                                    </div>
                                                </div>
                                            </div>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </div>
                </c:if>
                
                <!-- Main content -->
                <div class="col-12 ${(not empty param.txt || not empty searchKeyword) ? 'col-md-9' : 'col-md-8'}" id="main-content">

                    <!-- Search Results Info for search -->
                    <c:if test="${not empty param.txt || not empty searchKeyword}">
                        <div class="biolife-title-box">
                            <span class="subtitle">Tìm thấy ${productList.size()} sản phẩm cho từ khóa "<strong>${not empty param.txt ? param.txt : searchKeyword}</strong>"</span>
                            <h3 class="main-title">Kết quả tìm kiếm</h3>
                        </div>
                    </c:if>
                    
                    <!-- Category Title for category pages -->
                    <c:if test="${empty param.txt && empty searchKeyword && not empty categoryName}">
                        <div class="biolife-title-box">
                            <span class="subtitle">Danh mục sản phẩm</span>
                            <h3 class="main-title">${categoryName}</h3>
                        </div>
                    </c:if>

                    <!-- Product Grid Layout like product.jsp -->
                    <div class="product-grid">
                        <div class="row">
                            <c:forEach var="p" items="${productList}">
                                <div class="col-12 col-md-6 col-lg-4">
                                    <div class="product-item">
                                        <div class="contain-product layout-default">
                                            <div class="product-thumb">
                                                <a href="product?pid=${p.pid}" class="link-to-product">
                                                    <figure style="margin:0;padding:0;width:100%;height:270px;overflow:hidden;position:relative;border-radius:8px;background-color:#f8f8f8;">
                                                        <img src="${p.mainImageUrl}" alt="${p.name}" class="product-thumnail" style="width:100%;height:100%;object-fit:contain;">
                                                    </figure>
                                                </a>
                                            </div>
                                            <div class="info" style="padding: 15px;">
                                                <h4 class="product-title" style="margin-bottom: 10px;">
                                                    <a href="product?pid=${p.pid}" class="pr-name" style="color:#333;font-size:16px;font-weight:500;text-decoration:none;">${p.name}</a>
                                                </h4>
                                                <div class="price" style="margin-bottom: 15px;">
                                                    <ins><span class="price-amount" style="color:#4CAF50;font-size:18px;font-weight:600;"><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="VNĐ"/></span></ins>
                                                </div>
                                                <div class="slide-down-box">
                                                    <div class="buttons" style="display:flex;gap:10px;justify-content:center;">
                                                        <form action="wishlist" method="post" style="display:inline;">
                                                            <input type="hidden" name="action" value="add">
                                                            <input type="hidden" name="userID" value="<%= session.getAttribute("userID") %>">
                                                            <input type="hidden" name="productID" value="${p.pid}">
                                                            <input type="hidden" name="returnUrl" value="category">
                                                            <button type="submit" class="btn wishlist-btn" style="background:#fff;border:1px solid #4CAF50;color:#4CAF50;padding:8px 15px;border-radius:4px;" onclick="return confirm('Thêm sản phẩm vào wishlist?')">
                                                                <i class="fa fa-heart" aria-hidden="true"></i>
                                                            </button>
                                                        </form>
                                                        <a onclick="addToCart('${p.pid}', 1)" class="btn add-to-cart-btn" style="background:#4CAF50;color:#fff;padding:8px 15px;border-radius:4px;text-decoration:none;">
                                                            <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> Thêm vào giỏ
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="biolife-panigations-block">
                        <ul class="panigation-contain">
                            <li><span class="current-page">1</span></li>
                            <li><a href="#" class="link-page">2</a></li>
                            <li><a href="#" class="link-page">3</a></li>
                            <li><span class="sep">....</span></li>
                            <li><a href="#" class="link-page">20</a></li>
                            <li><a href="#" class="link-page next"><i class="fa fa-angle-right" aria-hidden="true"></i></a></li>
                        </ul>
                    </div>

                </div>
            </div>
        </div>
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
                    <li class="list-item"><a href="#">€ EUR (Euro)</a></li>
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
    <script>
        function submitForm() {
            document.getElementById("filterForm").submit();
        }

        function submitSortForm() {
            document.getElementById("sortForm").submit();
        }
    </script>

</body>

</html> 