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
    <!-- filepath: d:\KI4\PRJ301\DuanNho\DuAnBanHang\DuAnBanHang\web\Category.jsp -->
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            // Danh sách tên danh mục
            var categoryMap = {
                1: "Tinh dầu xông phòng",
                2: "Tinh dầu xe hơi",
                3: "Tinh dầu massage",
                4: "Tinh dầu dạng xịt"
            };

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
        <h1 class="page-title">Details</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                <li class="nav-item"><a href="product" class="permal-link">product</a></li>
                <li class="nav-item"><span class="current-page">${productName}</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain single-product">
        <div class="container">
            <!-- Main content -->
            <div id="main-content" class="main-content">

                <!-- summary info -->
                <div class="sumary-product single-layout">
                    <div class="media">
                        <ul class="biolife-carousel slider-for" data-slick='{"arrows":false,"dots":false,"slidesMargin":30,"slidesToShow":1,"slidesToScroll":1,"fade":true,"asNavFor":".slider-nav"}'>
                            <li><img src="${img}" alt=""  style="
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
                            <li><img src="${img}" alt="" style="
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
                    <div class="product-attribute">
                        <h3 class="title">${productName}</h3>
                        <div class="rating">
                            <p class="star-rating"><span class="width-80percent"></span></p>
                            <span class="review-count">(04 Reviews)</span>
                        </div>
                        <div class="price">
                            <ins><span class="price-amount"><span class="currencySymbol"></span><fmt:formatNumber value="${price}" type="currency"/></span></ins>
                        </div>
                    </div>
                    <div class="action-form">
                        <div class="quantity-box">
                            <span class="title">Quantity:</span>
                            <div class="qty-input">
                                <input type="text" 
                                       id="quantity" 
                                       name="quantity" 
                                       value="1" 
                                       data-max_value="${detail.stock}" 
                                       data-min_value="1" 
                                       data-step="1"
                                       oninput="this.value = this.value > '${detail.stock}' ? '${detail.stock}' : Math.abs(this.value)">
                                <a  class="qty-btn btn-up" onclick="event.preventDefault();
                                    var qtyUp = document.getElementById('quantity'); 
                                    var maxStock = parseInt('${detail.stock}');
                                    if (parseInt(qtyUp.value) < maxStock)
                                        qtyUp.value++;
                                    else
                                        alert('Số lượng vượt quá hàng tồn kho! (Còn ' + maxStock + ' sản phẩm)');"><i class="fa fa-caret-up" aria-hidden="true"></i>
                                </a>
                                <a  class="qty-btn btn-down" onclick="event.preventDefault(); var qtyDown = document.getElementById('quantity'); if (qtyDown.value > 1)qtyDown.value--;">
                                    <i class="fa fa-caret-down" aria-hidden="true"></i>
                                </a>
                            </div>
                            <p class="stock-info">Còn lại: ${detail.stock} sản phẩm</p>
                        </div>

                        <div class="buttons">
                            <a onclick="addToCart('${detail.id}', document.getElementById('quantity').value)" class="btn add-to-cart-btn">
                                <i class="fa fa-cart-plus"></i> Add to cart
                            </a>
                        </div>

                        <div class="social-media">
                            <ul class="social-list">
                                <li><a href="#" class="social-link"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                <li><a href="#" class="social-link"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                <li><a href="#" class="social-link"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                <li><a href="#" class="social-link"><i class="fa fa-share-alt" aria-hidden="true"></i></a></li>
                                <li><a href="#" class="social-link"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                            </ul>
                        </div>
                        <div class="acepted-payment-methods">
                            <ul class="payment-methods">
                                <li><img src="assets/images/card1.jpg" alt="" width="51" height="36"></li>
                                <li><img src="assets/images/card2.jpg" alt="" width="51" height="36"></li>
                                <li><img src="assets/images/card3.jpg" alt="" width="51" height="36"></li>
                                <li><img src="assets/images/card4.jpg" alt="" width="51" height="36"></li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- Tab info -->
                <div class="product-tabs single-layout biolife-tab-contain">
                    <div class="tab-head">
                        <ul class="tabs">
                            <li class="tab-element active"><a href="#tab_1st" class="tab-link">Products Descriptions</a></li>
                            <li class="tab-element"><a href="#tab_4th" class="tab-link">Customer Reviews <sup>(3)</sup></a></li>
                        </ul>
                    </div>
                    <div class="tab-content">
                        <div id="tab_1st" class="tab-contain desc-tab active">
                            <p class="desc">${description}</p>
                            <div class="desc-expand">
                                <!-- Additional product information can go here -->
                            </div>
                        </div>
                        <div id="tab_4th" class="tab-contain review-tab">
                            <div class="container">
                                <div class="row">
                                    <div class="col-lg-5 col-md-5 col-sm-6 col-xs-12">
                                        <div class="rating-info">
                                            <p class="index"><strong class="rating"><c:choose><c:when test="${averageRating != null and averageRating > 0}"><fmt:formatNumber value="${averageRating}" pattern="0.0"/></c:when><c:otherwise>0.0</c:otherwise></c:choose></strong>out of 5</p>
                                            <div class="rating">
                                                <p class="star-rating"><span class="width-<c:choose><c:when test="${averageRating != null and averageRating > 0}">${averageRating * 20}</c:when><c:otherwise>0</c:otherwise></c:choose>percent"></span></p>
                                            </div>
                                            <p class="see-all">See all <c:choose><c:when test="${reviewCount > 0}">${reviewCount}</c:when><c:otherwise>0</c:otherwise></c:choose> reviews</p>
                                            <ul class="options">
                                                <li>
                                                    <div class="detail-for">
                                                        <span class="option-name">5 stars</span>
                                                        <span class="progres">
                                                            <span class="line-100percent">
                                                                <span class="percent width-${ratingDistribution != null && ratingDistribution[4] > 0 && reviewCount > 0 ? (ratingDistribution[4] * 100 / reviewCount) : 0}percent"></span>
                                                            </span>
                                                        </span>
                                                        <span class="number">${ratingDistribution != null ? ratingDistribution[4] : 0}</span>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="detail-for">
                                                        <span class="option-name">4 stars</span>
                                                        <span class="progres">
                                                            <span class="line-100percent">
                                                                <span class="percent width-${ratingDistribution != null && ratingDistribution[3] > 0 ? (ratingDistribution[3] * 100 / reviewCount) : 0}percent"></span>
                                                            </span>
                                                        </span>
                                                        <span class="number">${ratingDistribution != null ? ratingDistribution[3] : 0}</span>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="detail-for">
                                                        <span class="option-name">3 stars</span>
                                                        <span class="progres">
                                                            <span class="line-100percent">
                                                                <span class="percent width-${ratingDistribution != null && ratingDistribution[2] > 0 ? (ratingDistribution[2] * 100 / reviewCount) : 0}percent"></span>
                                                            </span>
                                                        </span>
                                                        <span class="number">${ratingDistribution != null ? ratingDistribution[2] : 0}</span>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="detail-for">
                                                        <span class="option-name">2 stars</span>
                                                        <span class="progres">
                                                            <span class="line-100percent">
                                                                <span class="percent width-${ratingDistribution != null && ratingDistribution[1] > 0 ? (ratingDistribution[1] * 100 / reviewCount) : 0}percent"></span>
                                                            </span>
                                                        </span>
                                                        <span class="number">${ratingDistribution != null ? ratingDistribution[1] : 0}</span>
                                                    </div>
                                                </li>
                                                <li>
                                                    <div class="detail-for">
                                                        <span class="option-name">1 star</span>
                                                        <span class="progres">
                                                            <span class="line-100percent">
                                                                <span class="percent width-${ratingDistribution != null && ratingDistribution[0] > 0 ? (ratingDistribution[0] * 100 / reviewCount) : 0}percent"></span>
                                                            </span>
                                                        </span>
                                                        <span class="number">${ratingDistribution != null ? ratingDistribution[0] : 0}</span>
                                                    </div>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <div class="col-lg-7 col-md-7 col-sm-6 col-xs-12">
                                        <div class="review-form-wrapper">
                                            <span class="title">Submit your review</span>
                                            <c:choose>
                                                <c:when test="${sessionScope.acc != null}">
                                                    <!-- Check if user can review products from their orders -->
                                                    <c:set var="canReview" value="false" />
                                                    <c:set var="reviewMessage" value="" />
                                                    
                                                    <!-- This will be populated by the controller -->
                                                    <c:choose>
                                                        <c:when test="${canUserReviewProduct == true}">
                                                            <c:set var="canReview" value="true" />
                                                        </c:when>
                                                        <c:when test="${canUserReviewProduct == false && reviewMessage != null}">
                                                            <c:set var="reviewMessage" value="${reviewMessage}" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="reviewMessage" value="You can only review products from orders that have been delivered and paid." />
                                                        </c:otherwise>
                                                    </c:choose>
                                                    
                                                    <c:choose>
                                                        <c:when test="${canReview == true}">
                                                            <form action="review" method="post" name="frm-review">
                                                                <input type="hidden" name="action" value="submit-from-order">
                                                                <input type="hidden" name="productID" value="${detail.id}">
                                                                <input type="hidden" name="orderID" value="${orderIDForReview}">
                                                                <div class="comment-form-rating">
                                                                    <label>1. Your rating of this product:</label>
                                                                    <p class="stars">
                                                                        <span>
                                                                            <a class="btn-rating" data-value="star-1" href="#"><i class="fa fa-star-o" aria-hidden="true"></i></a>
                                                                            <a class="btn-rating" data-value="star-2" href="#"><i class="fa fa-star-o" aria-hidden="true"></i></a>
                                                                            <a class="btn-rating" data-value="star-3" href="#"><i class="fa fa-star-o" aria-hidden="true"></i></a>
                                                                            <a class="btn-rating" data-value="star-4" href="#"><i class="fa fa-star-o" aria-hidden="true"></i></a>
                                                                            <a class="btn-rating" data-value="star-5" href="#"><i class="fa fa-star-o" aria-hidden="true"></i></a>
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
                                                            <div class="review-restriction">
                                                                <p class="restriction-message">${reviewMessage}</p>
                                                                <p><strong>To leave a review:</strong></p>
                                                                <ul>
                                                                    <li>✓ Purchase this product</li>
                                                                    <li>✓ Wait for order to be delivered</li>
                                                                    <li>✓ Complete payment</li>
                                                                    <li>✓ Review each product once per order</li>
                                                                </ul>
                                                                <a href="order-history" class="btn btn-link">Check Your Orders</a>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="login-request">
                                                        <p>Please <a href="login?returnUrl=detail?pid=${detail.id}">login</a> to submit a review.</p>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                <div id="comments">
                                    <h3 class="comment-title">${reviewCount} Reviews for ${productName}</h3>
                                    <ol class="commentlist">
                                        <c:if test="${reviews.size() == 0}">
                                            <li class="no-reviews">
                                                <p>No reviews yet. Be the first to review this product!</p>
                                            </li>
                                        </c:if>
                                        <c:forEach var="review" items="${reviews}">
                                            <li class="review">
                                                <div class="comment-container">
                                                    <div class="row">
                                                        <div class="comment-content col-lg-8 col-md-9 col-sm-8 col-xs-12">
                                                            <p class="comment-in">
                                                                <span class="post-name">${review.reviewText}</span>
                                                                <span class="post-date">
                                                                    <fmt:formatDate pattern="dd/MM/yyyy" value="${review.reviewDate}" />
                                                                </span>
                                                            </p>
                                                            <div class="rating">
                                                                <p class="star-rating">
                                                                    <span class="width-${review.rating * 20}percent"></span>
                                                                </p>
                                                            </div>
                                                            <p class="author">by: <b>${review.userName}</b></p>
                                                        </div>
                                                        <div class="comment-review-form col-lg-3 col-lg-offset-1 col-md-3 col-sm-4 col-xs-12">
                                                            <span class="title">Was this review helpful?</span>
                                                            <ul class="actions">
                                                                <li><a href="#" class="btn-act like" data-type="like"><i class="fa fa-thumbs-up" aria-hidden="true"></i>Yes</a></li>
                                                                <li><a href="#" class="btn-act hate" data-type="dislike"><i class="fa fa-thumbs-down" aria-hidden="true"></i>No</a></li>
                                                                <li><a href="#" class="btn-act report" data-type="dislike"><i class="fa fa-flag" aria-hidden="true"></i>Report</a></li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                    <c:if test="${reviewCount > 5}">
                                        <div class="biolife-panigations-block version-2">
                                            <ul class="panigation-contain">
                                                <li><span class="current-page">1</span></li>
                                                <li><a href="#" class="link-page">2</a></li>
                                                <c:if test="${reviewCount > 10}">
                                                    <li><a href="#" class="link-page">3</a></li>
                                                    <li><span class="sep">....</span></li>
                                                    <li><a href="#" class="link-page">${Math.ceil(reviewCount/5)}</a></li>
                                                </c:if>
                                                <li><a href="#" class="link-page next"><i class="fa fa-angle-right" aria-hidden="true"></i></a></li>
                                            </ul>
                                            <div class="result-count">
                                                <p class="txt-count"><b>1-5</b> of <b>${reviewCount}</b> reviews</p>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- related products -->
                <div class="product-related-box single-layout">
                    <div class="biolife-title-box lg-margin-bottom-26px-im">
                        <span class="subtitle">All the best item for You</span>
                        <h3 class="main-title">Related Products</h3>
                    </div>
                    <ul class="products-list biolife-carousel nav-center-02 nav-none-on-mobile" data-slick='{"rows":1,"arrows":true,"dots":false,"infinite":false,"speed":400,"slidesMargin":0,"slidesToShow":5, "responsive":[{"breakpoint":1200, "settings":{ "slidesToShow": 4}},{"breakpoint":992, "settings":{ "slidesToShow": 3, "slidesMargin":20 }},{"breakpoint":768, "settings":{ "slidesToShow": 2, "slidesMargin":10}}]}'>
                        <c:forEach var="p" items="${listPP}">
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
                                                    <input type="hidden" name="userID" value="<%= session.getAttribute("userID") %>">
                                                    <input type="hidden" name="productID" value="${p.id}">
                                                    <input type="hidden" name="returnUrl" value="detail?pid=${detail.id}">
                                                    <button type="submit" class="btn wishlist-btn" style="background:#fff;border:1px solid #4CAF50;color:#4CAF50;padding:8px 15px;border-radius:4px;" onclick="return confirm('Thêm sản phẩm vào wishlist?')">
                                                        <i class="fa fa-heart" aria-hidden="true"></i>
                                                    </button>
                                                </form>
                                                <a onclick="addToCart('${p.id}', 1)" class="btn add-to-cart-btn" style="background:#4CAF50;color:#fff;padding:8px 15px;border-radius:4px;text-decoration:none;">
                                                    <i class="fa fa-cart-arrow-down" aria-hidden="true"></i> Thêm vào giỏ
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
        $(document).ready(function () {
            const ratingBtns = document.querySelectorAll('.btn-rating');
            const ratingInput = document.getElementById('selected-rating');

            ratingBtns.forEach((btn, index) => {
                btn.addEventListener('click', function (e) {
                    e.preventDefault();

                    // Set the rating value (index + 1 because stars are 1-based)
                    const ratingValue = index + 1;
                    ratingInput.value = ratingValue;

                    // Update the visual appearance
                    ratingBtns.forEach((b, i) => {
                        const star = b.querySelector('i');
                        if (i <= index) {
                            star.className = 'fa fa-star';
                        } else {
                            star.className = 'fa fa-star-o';
                        }
                    });
                });
            });

            // Check if hash contains tab=reviews to activate the reviews tab
            if (window.location.hash === '#tab_4th' || window.location.search.includes('tab=reviews')) {
                // Find the review tab link and trigger a click
                const reviewTabLink = document.querySelector('a[href="#tab_4th"]');
                if (reviewTabLink) {
                    setTimeout(() => {
                        reviewTabLink.click();
                    }, 100);
                }
            }
        });
        $(document).ready(function () {
            const ratingBtns = document.querySelectorAll('.btn-rating');
            const ratingInput = document.getElementById('selected-rating');

            // Hiển thị mặc định 5 sao khi trang được tải
            function initRating() {
                const defaultRating = ratingInput.value || 5;
                updateStarsDisplay(defaultRating);
            }

            // Cập nhật hiển thị sao dựa trên giá trị đánh giá
            function updateStarsDisplay(rating) {
                ratingBtns.forEach((btn, i) => {
                    const star = btn.querySelector('i');
                    if (i < rating) {
                        star.className = 'fa fa-star'; // Sao đầy đủ
                    } else {
                        star.className = 'fa fa-star-o'; // Sao rỗng
                    }
                });
            }

            // Sự kiện click cho các nút sao
            ratingBtns.forEach((btn, index) => {
                btn.addEventListener('click', function (e) {
                    e.preventDefault();

                    // Set the rating value (index + 1 because stars are 1-based)
                    const ratingValue = index + 1;
                    ratingInput.value = ratingValue;

                    // Update the visual appearance
                    updateStarsDisplay(ratingValue);
                });

                // Thêm hiệu ứng hover
                btn.addEventListener('mouseenter', function () {
                    // Hiển thị sao khi hover
                    ratingBtns.forEach((b, i) => {
                        const star = b.querySelector('i');
                        if (i <= index) {
                            star.className = 'fa fa-star';
                        } else {
                            star.className = 'fa fa-star-o';
                        }
                    });
                });
            });

            // Xử lý sự kiện khi chuột rời khỏi vùng sao
            const starsContainer = document.querySelector('.stars');
            if (starsContainer) {
                starsContainer.addEventListener('mouseleave', function () {
                    // Khôi phục trạng thái sao dựa trên giá trị đã chọn
                    const currentRating = parseInt(ratingInput.value) || 5;
                    updateStarsDisplay(currentRating);
                });
            }

            // Khởi tạo sao khi trang tải
            initRating();

            // Check if hash contains tab=reviews to activate the reviews tab
            if (window.location.hash === '#tab_4th' || window.location.search.includes('tab=reviews')) {
                // Find the review tab link and trigger a click
                const reviewTabLink = document.querySelector('a[href="#tab_4th"]');
                if (reviewTabLink) {
                    setTimeout(() => {
                        reviewTabLink.click();
                    }, 100);
                }
            }

            // Đảm bảo carousel được khởi tạo đúng cách
            setTimeout(function() {
                $('.biolife-carousel').each(function() {
                    if (!$(this).hasClass('slick-initialized')) {
                        $(this).slick();
                    }
                });
            }, 500);
        });
    </script>

</body>
</html> 