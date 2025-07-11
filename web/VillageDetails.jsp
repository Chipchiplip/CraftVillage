<%-- 
    Document   : VillageDetails
    Created on : Jul 10, 2025, 4:25:01 AM
    Author     : ACER
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
            /* Làm đẹp cho phần đánh giá sao */
            .comment-form-rating .stars {
                display: inline-block;
                margin: 0;
                padding: 0;
            }

            .comment-form-rating .stars a.btn-rating {
                display: inline-block;
                margin-right: 5px;
                font-size: 16px;
                color: #ccc;
                text-decoration: none;
            }

            .comment-form-rating .stars a.btn-rating .fa-star {
                color: #f9ba48; /* Màu của sao đầy đủ */
            }

            .comment-form-rating .stars a.btn-rating .fa-star-o {
                color: #ccc; /* Màu của sao rỗng */
            }

            .comment-form-rating .stars a.btn-rating:hover {
                transform: scale(1.2);
            }

            /* Hiệu ứng khi hover */
            .comment-form-rating .stars a.btn-rating:hover ~ a.btn-rating .fa {
                color: #ccc;
            }

            /* Cải thiện nút gửi đánh giá */
            .review-form-wrapper button[type="submit"] {
                background-color: #4CAF50;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .review-form-wrapper button[type="submit"]:hover {
                background-color: #45a049;
            }

            /* Fix cho Related Products carousel */
            .product-related-box {
                margin-top: 50px;
                margin-bottom: 50px;
            }

            .products-list.biolife-carousel {
                display: block !important;
            }

            .products-list.biolife-carousel .product-item {
                display: inline-block;
                vertical-align: top;
                width: auto;
            }

            .biolife-carousel:not(.slick-initialized) .product-item {
                width: 20%;
                float: left;
            }
        </style>
        <style>
            .desc-expand {
                position: relative;
                z-index: 1;
                overflow: hidden;
                margin-bottom: 140px;
            }

            .desc-expand iframe {
                display: block;
                max-width: 100%;
                width: 100%;
                height: 400px;
                border: 0;
                position: static;
                z-index: 1;
            }

            .product-related-box {
                position: relative;
                z-index: 2;
            }
        </style>
        <style>
            .sumary-village.single-layout {
                display: flex;
                flex-direction: row; /* đảm bảo ảnh trái - text phải */
                flex-wrap: wrap;
                gap: 30px;
                margin-bottom: 30px;
                align-items: flex-start;
            }

            .sumary-village.single-layout .media {
                flex: 1 1 45%;
                max-width: 45%;
            }

            .sumary-village.single-layout .village-attribute {
                flex: 1 1 50%;
                max-width: 50%;
            }

            @media (max-width: 768px) {
                .sumary-village.single-layout {
                    flex-direction: column;
                }
                .sumary-village.single-layout .media,
                .sumary-village.single-layout .village-attribute {
                    max-width: 100%;
                }
            }
        </style>
        <style>
            .ticket-list {
                list-style: none;
                margin: 0;
                padding-left: 20px;
            }
            .ticket-list li {
                margin: 4px 0;
            }
        </style>
        <style>
            .tab-contain {
                display: none;
            }
            .tab-contain.active {
                display: block;
            }
        </style>
        <script>
            function addToCart(productId, quantity) {
                fetch("cart?action=add&id=" + productId + "&quantity=" + quantity, {
                    method: "POST",
                    credentials: 'same-origin'
                })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.text();
                        })
                        .then(data => {
                            if (data.includes("login")) {
                                alert("Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
                                window.location.href = 'Login.jsp';
                            } else {
                                alert("Đã thêm sản phẩm vào giỏ hàng!");
                            }
                        })
                        .catch(error => {
                            console.error("Lỗi:", error);
                            alert("Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng!");
                        });
            }
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
                <h1 class="page-title">Village Details</h1>
            </div>

            <!--Navigation section-->
            <div class="container">
                <nav class="biolife-nav">
                    <ul>
                        <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                        <li class="nav-item"><a href="#" class="permal-link">village</a></li>
                        <li class="nav-item"><span class="current-page">${villageDetails.villageName}</span></li>
                </ul>
            </nav>
        </div>

        <div class="page-contain single-product">
            <div class="container">
                <!-- Main content -->
                <div id="main-content" class="main-content">

                    <!-- summary info -->
                    <div class="sumary-village single-layout">
                        <div class="media">
                            <ul class="biolife-carousel slider-for" data-slick='{"arrows":false,"dots":false,"slidesMargin":30,"slidesToShow":1,"slidesToScroll":1,"fade":true,"asNavFor":".slider-nav"}'>
                                <li><img src="${villageDetails.mainImageUrl}" alt=""  style="
                                         width: 400px;
                                         height: 350px;
                                         object-fit: contain;
                                         display: block;
                                         margin: 0 auto;
                                         border-radius: 8px;
                                         background-color: #f8f8f8;
                                         box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                                         transition: transform 0.3s ease;
                                         "
                                         onmouseover="this.style.transform = 'scale(1.05)'" 
                                         onmouseout="this.style.transform = 'scale(1)'"></li>
                            </ul>
                            <ul class="biolife-carousel slider-nav" data-slick='{"arrows":false,"dots":false,"centerMode":false,"focusOnSelect":true,"slidesMargin":10,"slidesToShow":4,"slidesToScroll":1,"asNavFor":".slider-for"}'>
                                <li><img src="${villageDetails.mainImageUrl}" alt="" style="
                                         width: 88px;
                                         height: 88px;
                                         object-fit: contain;
                                         border-radius: 4px;
                                         border: 2px solid transparent;
                                         background-color: #f8f8f8;
                                         transition: all 0.3s ease;
                                         cursor: pointer;
                                         "
                                         onmouseover="this.style.borderColor = '#4CAF50'" 
                                         onmouseout="this.style.borderColor = 'transparent'"></li>
                            </ul>
                        </div>

                        <div class="village-attribute">
                            <h3 class="title">${villageDetails.villageName}</h3>

                            <div class="rating">
                                <p class="star-rating"><span class="width-80percent"></span></p>
                                <span class="review-count">${villageDetails.totalReviews}</span>
                            </div>

                            <ul class="village-info">
                                <li><strong>Address:</strong> ${villageDetails.address}</li>
                                <li><strong>Contact Phone:</strong> ${villageDetails.contactPhone}</li>
                                <li><strong>Contact Email:</strong> ${villageDetails.contactEmail}</li>
                                <li><strong>Artist:</strong> ${seller.fullName}</li>
                                <li><strong>Opening Hours:</strong> ${villageDetails.openingHours}</li>
                                <li><strong>Closing Days:</strong> ${villageDetails.closingDays}</li>
                                <li><strong>Virtual Tour URL:</strong> 
                                    <c:if test="${not empty villageDetails.virtualTourUrl}">
                                        <a href="${villageDetails.virtualTourUrl}" target="_blank">View Virtual Tour</a>
                                    </c:if>
                                </li>
                                <li><strong>History:</strong> ${villageDetails.history}</li>
                                <li><strong>Special Features:</strong> ${villageDetails.specialFeatures}</li>
                                <li><strong>Famous Products:</strong> ${villageDetails.famousProducts}</li>
                                <li><strong>Cultural Events:</strong> ${villageDetails.culturalEvents}</li>
                                <li><strong>Craft Process:</strong> ${villageDetails.craftProcess}</li>
                                <li><strong>Video Description URL:</strong> 
                                    <c:if test="${not empty villageDetails.videoDescriptionUrl}">
                                        <a href="${villageDetails.videoDescriptionUrl}" target="_blank">Watch Video</a>
                                    </c:if>
                                </li>
                                <li><strong>Travel Tips:</strong> ${villageDetails.travelTips}</li>
                                <li>
                                    <strong>Ticket:</strong>
                                    <ul class="ticket-list">
                                        <c:forEach items="${listTicket}" var="ticket">
                                            <c:set var="typeName" value="" />
                                            <c:forEach items="${ticketType}" var="type">
                                                <c:if test="${type.typeID == ticket.typeID}">
                                                    <c:set var="typeName" value="${type.typeName}" />
                                                </c:if>
                                            </c:forEach>
                                            <li>- 
                                                <a href="ticket-detail?ticketId=${ticket.ticketID}">
                                                    ${typeName}
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </li>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.acc}">
                                        <li><strong>Contact to Artist:</strong>
                                            <a href="contact-artist?villageID=${villageDetails.villageID}&userID=${sessionScope.acc.userID}&sellerID=${seller.userID}">Here</a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li><strong>Contact to Artist:</strong>
                                            <a href="Login.jsp">Here</a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>
                    </div>

                    <!-- Tab info -->
                    <div class="product-tabs single-layout biolife-tab-contain">
                        <div class="tab-head">
                            <ul class="tabs">
                                <li class="tab-element active"><a href="#tab_1st" class="tab-link">Village Descriptions</a></li>
                                <li class="tab-element"><a href="#tab_4th" class="tab-link">Customer Reviews <sup>${villageDetails.totalReviews}</sup></a></li>
                            </ul>
                        </div>
                        <div class="tab-content">
                            <div id="tab_1st" class="tab-contain desc-tab active">
                                <p class="desc">${villageDetails.description}</p>
                                <div class="desc-expand">
                                    <!-- Additional product information can go here -->
                                </div>
                            </div>
                            <div id="tab_4th" class="tab-contain review-tab">
                                <div class="container">

                                    <div class="row">
                                        <!-- LEFT: RATING SUMMARY -->
                                        <div class="col-lg-5 col-md-5 col-sm-6 col-xs-12">
                                            <div class="rating-info">
                                                <p class="index">
                                                    <strong class="rating">
                                                        <c:choose>
                                                            <c:when test="${villageDetails.averageRating != null && villageDetails.averageRating > 0}">
                                                                <fmt:formatNumber value="${villageDetails.averageRating}" pattern="0.0"/>
                                                            </c:when>
                                                            <c:otherwise>0.0</c:otherwise>
                                                        </c:choose>
                                                    </strong> out of 5
                                                </p>

                                                <div class="rating">
                                                    <p class="star-rating">
                                                        <span class="width-<c:choose>
                                                                  <c:when test="${villageDetails.averageRating != null && villageDetails.averageRating > 0}">
                                                                      ${villageDetails.averageRating * 20}
                                                                  </c:when>
                                                                  <c:otherwise>0</c:otherwise>
                                                              </c:choose>percent"></span>
                                                    </p>
                                                </div>

                                                <p class="see-all">
                                                    See all 
                                                    <c:choose>
                                                        <c:when test="${villageDetails.totalReviews > 0}">
                                                            ${villageDetails.totalReviews}
                                                        </c:when>
                                                        <c:otherwise>0</c:otherwise>
                                                    </c:choose>
                                                    reviews
                                                </p>
                                            </div>
                                        </div>

                                        <!-- RIGHT: REVIEW FORM -->
                                        <div class="col-lg-7 col-md-7 col-sm-6 col-xs-12">
                                            <div class="review-form-wrapper">
                                                <span class="title">Submit your review</span>
                                                <c:choose>
                                                    <c:when test="${sessionScope.acc != null}">
                                                        <form action="village" method="post" name="frm-review">
                                                            <input type="hidden" name="villageID" value="${villageDetails.villageID}">
                                                            <input type="hidden" name="userID" value="${sessionScope.account.userID}">
                                                            <div class="comment-form-rating">
                                                                <label>1. Your rating of this village:</label>
                                                                <p class="stars">
                                                                    <span>
                                                                        <c:forEach var="star" begin="1" end="5">
                                                                            <a class="btn-rating" data-value="star-${star}" href="#">
                                                                                <i class="fa fa-star-o" aria-hidden="true"></i>
                                                                            </a>
                                                                        </c:forEach>
                                                                    </span>
                                                                </p>
                                                                <input type="hidden" name="rating" id="selected-rating" value="5">
                                                            </div>

                                                            <p class="form-row">
                                                                <textarea name="reviewText" id="txt-comment" cols="30" rows="10" placeholder="Write your review here..." required></textarea>
                                                            </p>
                                                            <p class="form-row">
                                                                <button type="submit" name="submit">Submit Review</button>
                                                            </p>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="login-request">
                                                            <p>
                                                                Please
                                                                <a href="login?returnUrl=detail?villageID=${villageDetails.villageID}">
                                                                    login
                                                                </a>
                                                                to submit a review.
                                                            </p>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- REVIEW LIST -->
                                    <div class="row" style="margin-top:40px;">
                                        <div class="col-12">
                                            <h3 class="comment-title">
                                                ${villageDetails.totalReviews} Reviews for ${villageDetails.villageName}
                                            </h3>

                                            <ol class="commentlist">
                                                <c:if test="${empty listReview}">
                                                    <li class="no-reviews">
                                                        <p>No reviews yet. Be the first to review this village!</p>
                                                    </li>
                                                </c:if>

                                                <c:forEach var="review" items="${listReview}">
                                                    <li class="review">
                                                        <div class="comment-container" style="border-bottom:1px solid #eee; margin-bottom:15px; padding-bottom:15px;">
                                                            <div class="row">
                                                                <div class="comment-content col-lg-8 col-md-9 col-sm-8 col-xs-12">
                                                                    <p class="comment-in">
                                                                        <span class="post-name">${review.reviewText}</span>
                                                                        <span class="post-date" style="margin-left:10px; font-size:0.9em; color:#999;">
                                                                            <fmt:formatDate pattern="dd/MM/yyyy" value="${review.reviewDate}" />
                                                                        </span>
                                                                    </p>
                                                                    <div class="rating">
                                                                        <p class="star-rating">
                                                                            <span class="width-${review.rating * 20}percent"></span>
                                                                        </p>
                                                                    </div>

                                                                    <c:if test="${not empty review.response}">
                                                                        <div class="review-response" style="background: #f7f7f7; padding: 10px; margin-top: 10px; border-left: 3px solid #4CAF50;">
                                                                            <strong>Response:</strong>
                                                                            <p style="margin:0;">${review.response}</p>
                                                                        </div>
                                                                    </c:if>

                                                                </div>
                                                            </div>
                                                        </div>
                                                    </li>
                                                </c:forEach>
                                            </ol>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Map-->
                    <h4><strong>Village Location</strong></h4>
                    <div class="desc-expand">
                        <c:choose>
                            <c:when test="${not empty villageDetails.mapEmbedUrl}">
                                <iframe 
                                    src="${villageDetails.mapEmbedUrl}" 
                                    width="600" 
                                    height="450" 
                                    style="border:0;"
                                    allowfullscreen="" 
                                    loading="lazy" 
                                    referrerpolicy="no-referrer-when-downgrade">
                                </iframe>
                            </c:when>
                            <c:otherwise>
                                <p>Maps not available.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- related products -->
                <div class="village-related-box single-layout">
                    <div class="biolife-title-box lg-margin-bottom-26px-im">
                        <span class="subtitle">All the best item for You</span>
                        <h3 class="main-title">Related Products</h3>
                    </div>
                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":5, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20 }},{"breakpoint":768, "settings":{ "slidesToShow": 2, "slidesMargin":10}}]}'>
                        <c:forEach var="p" items="${listProduct}">
                            <li class="product-item">
                                <div class="contain-product layout-default">
                                    <div class="product-thumb">
                                        <a href="detail?pid=${p.id}" class="link-to-product">
                                            <figure style="margin:0;padding:0;width:100%;height:270px;overflow:hidden;position:relative;border-radius:8px;background-color:#f8f8f8;">
                                                <img src="${p.img}" alt="${p.name}" class="product-thumnail" style="width:100%;height:100%;object-fit:contain;" onerror="this.src='assets/images/products/placeholder.svg'; this.onerror=null;">
                                            </figure>
                                        </a>
                                    </div>
                                    <div class="info" style="padding: 15px;">
                                        <h4 class="product-title" style="margin-bottom: 10px;">
                                            <a href="detail?pid=${p.id}" class="pr-name" style="color:#333;font-size:16px;font-weight:500;text-decoration:none;">${p.name}</a>
                                        </h4>
                                        <div class="price" style="margin-bottom: 15px;">
                                            <ins><span class="price-amount" style="color:#4CAF50;font-size:18px;font-weight:600;"><fmt:formatNumber value="${p.price}" type="currency"/></span></ins>
                                        </div>
                                        <div class="slide-down-box">
                                            <div class="buttons" style="display:flex;gap:10px;justify-content:center;">
                                                <form action="wishlist" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="add">
                                                    <input type="hidden" name="userID" value="<%= session.getAttribute("userID")%>">
                                                    <input type="hidden" name="productID" value="${p.id}">
                                                    <input type="hidden" name="returnUrl" value="detail?pid=${detail.id}">
                                                    <button type="submit" class="btn wishlist-btn" style="background:#fff;border:1px solid #4CAF50;color:#4CAF50;padding:8px 15px;border-radius:4px;" onclick="return confirm('Thêm sản phẩm vào wishlist?')">
                                                        <i class="fa fa-heart" aria-hidden="true"></i>
                                                    </button>
                                                </form>
                                                <a onclick="addToCart('${p.id}', 1)" class="btn add-to-cart-btn" style="background:#4CAF50;color:#fff;padding:8px 15px;border-radius:4px;text-decoration:none;">
                                                    <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> Add to Cart
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
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
                                                    document.addEventListener("DOMContentLoaded", function () {
                                                        const tabs = document.querySelectorAll(".tab-link");
                                                        const tabContents = document.querySelectorAll(".tab-contain");

                                                        tabs.forEach(tab => {
                                                            tab.addEventListener("click", function (e) {
                                                                e.preventDefault();

                                                                // Remove all active classes
                                                                document.querySelectorAll(".tab-element").forEach(el => el.classList.remove("active"));
                                                                document.querySelectorAll(".tab-contain").forEach(el => el.classList.remove("active"));

                                                                // Add active class to the clicked tab
                                                                this.parentElement.classList.add("active");

                                                                // Show the corresponding tab content
                                                                const target = document.querySelector(this.getAttribute("href"));
                                                                if (target) {
                                                                    target.classList.add("active");
                                                                }
                                                            });
                                                        });
                                                    });
        </script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const ratingBtns = document.querySelectorAll('.btn-rating');
                const ratingInput = document.getElementById('selected-rating');

                // Hiển thị mặc định số sao nếu có sẵn giá trị
                function updateStarsDisplay(rating) {
                    ratingBtns.forEach((btn, i) => {
                        const starIcon = btn.querySelector('i');
                        if (i < rating) {
                            starIcon.className = 'fa fa-star'; // sao đầy
                        } else {
                            starIcon.className = 'fa fa-star-o'; // sao rỗng
                        }
                    });
                }

                updateStarsDisplay(parseInt(ratingInput.value));

                ratingBtns.forEach((btn, index) => {
                    btn.addEventListener('click', function (e) {
                        e.preventDefault();
                        const ratingValue = index + 1;
                        ratingInput.value = ratingValue;
                        updateStarsDisplay(ratingValue);
                    });
                });

                // Hover effect (optional)
                ratingBtns.forEach((btn, index) => {
                    btn.addEventListener('mouseenter', function () {
                        updateStarsDisplay(index + 1);
                    });
                });

                const starsContainer = document.querySelector('.stars');
                if (starsContainer) {
                    starsContainer.addEventListener('mouseleave', function () {
                        updateStarsDisplay(parseInt(ratingInput.value));
                    });
                }
            });
        </script>
    </body>
</html> 