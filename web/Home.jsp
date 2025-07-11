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
        <link rel="shortcut icon" type="image/x-icon" href="hinhanh/Logo/cropped-Favicon-1-32x32.png" />
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/animate.min.css">
        <link rel="stylesheet" href="assets/css/font-awesome.min.css">
        <link rel="stylesheet" href="assets/css/nice-select.css">
        <link rel="stylesheet" href="assets/css/slick.min.css">
        <link rel="stylesheet" href="assets/css/style.css">
        <link rel="stylesheet" href="assets/css/main-color03-green.css">
        <script src="assets/js/music-player.js"></script>
       
        
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
        <!-- HEADER/MENU trên cùng -->
        <jsp:include page="Menu.jsp"></jsp:include>

        <div class="page-contain">
            <!-- VIDEO SECTION -->
            <div class="video-section" style="position: relative; width: 100%; height: 600px; overflow: hidden;">
                <div class="video-container" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;">
                    <iframe style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); width: 100vw; height: 56.25vw; min-height: 100%; min-width: 177.77vh;" 
                            src="https://www.youtube.com/embed/dp_Ak9rtTVo?autoplay=1&mute=1&loop=1&playlist=dp_Ak9rtTVo&controls=0&showinfo=0&rel=0" 
                            frameborder="0" 
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                            allowfullscreen>
                    </iframe>
                </div>
                <div class="video-overlay" style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.4); display: flex; justify-content: center; align-items: center;">
                    
                </div>
            </div>

            <!-- 360° TOUR SECTION -->
            <div class="tour-section" style="padding: 60px 0; background: #f9f9f9; margin-top: 100px;">
                <div class="container">
                    <div class="biolife-title-box text-center">
                        <span class="subtitle">Trải nghiệm làng nghề qua 360°</span>
                        <h3 class="main-title">Khám phá không gian làng nghề</h3>
                    </div>
                    <div class="row" style="margin-top: 40px;">
                        <div class="col-md-4">
                            <div class="tour-item" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 15px rgba(0,0,0,0.1);">
                                <img src="hinhanh/village/thanh-ha.jpg" alt="Làng gốm Thanh Hà" style="width: 100%; height: 250px; object-fit: cover;">
                                <div class="tour-content" style="padding: 20px;">
                                    <h4>Làng gốm Thanh Hà</h4>
                                    <p>Khám phá nghệ thuật làm gốm truyền thống qua tour 360°</p>
                                    <a href="tour360?village=thanh-ha" class="btn btn-outline-primary">Vào xem tour</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="tour-item" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 15px rgba(0,0,0,0.1);">
                                <img src="hinhanh/village/kim-bong.jpg" alt="Làng mộc Kim Bồng" style="width: 100%; height: 250px; object-fit: cover;">
                                <div class="tour-content" style="padding: 20px;">
                                    <h4>Làng mộc Kim Bồng</h4>
                                    <p>Trải nghiệm nghề mộc truyền thống qua tour 360°</p>
                                    <a href="tour360?village=kim-bong" class="btn btn-outline-primary">Vào xem tour</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="tour-item" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 15px rgba(0,0,0,0.1);">
                                <img src="hinhanh/village/non-nuoc.jpg" alt="Làng đá Non Nước" style="width: 100%; height: 250px; object-fit: cover;">
                                <div class="tour-content" style="padding: 20px;">
                                    <h4>Làng đá Non Nước</h4>
                                    <p>Khám phá nghệ thuật điêu khắc đá qua tour 360°</p>
                                    <a href="tour360?village=non-nuoc" class="btn btn-outline-primary">Vào xem tour</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- FEATURES SECTION -->
            <div class="features-section" style="padding: 60px 0; background: white;">
                <div class="container">
                    <div class="row">
                        <div class="col-md-4">
                            <div class="feature-item text-center">
                                <i class="fa fa-check-circle" style="font-size: 3em; color: #4CAF50;"></i>
                                <h4>Sản phẩm chính hãng</h4>
                                <p>Từ các làng nghề truyền thống</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="feature-item text-center">
                                <i class="fa fa-shopping-cart" style="font-size: 3em; color: #4CAF50;"></i>
                                <h4>Mua hàng online</h4>
                                <p>Tiện lợi và an toàn</p>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="feature-item text-center">
                                <i class="fa fa-truck" style="font-size: 3em; color: #4CAF50;"></i>
                                <h4>Giao hàng miễn phí</h4>
                                <p>Cho đơn hàng nội thành</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!--Block 03: Product Tabs-->
            <div class="product-tab z-index-20 sm-margin-top-193px xs-margin-top-30px" id="product-list">
                <div class="container" >
                    <div class="biolife-title-box" >
                        <div class="product-tab z-index-20 sm-margin-top-193px xs-margin-top-30px" id="product-list">
                            <div class="container" >
                                <div class="biolife-title-box" >
                                    <span class="subtitle">Sản phẩm nổi bật</span>
                                    <h3 class="main-title">Sản phẩm được yêu thích nhất</h3>
                                </div>
                                <div class="biolife-tab biolife-tab-contain sm-margin-top-34px">
                                    <div class="tab-head tab-head__icon-top-layout icon-top-layout">
                                    </div>
                                    <%-- Thiết lập thông tin phân trang --%>
                                    <c:set var="pageSize" value="9"/>
                                    <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
                                    <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
                                    <c:set var="end" value="${start + pageSize}"/>
                                    <c:set var="totalProducts" value="${listP.size()}"/>
                                    <c:set var="totalPages" value="${(totalProducts % pageSize == 0) ? (totalProducts / pageSize) : (totalProducts / pageSize + 1)}"/>

                                    <div class="row">
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
                                                                            <input type="hidden" name="returnUrl" value="home">
                                                                            <button type="submit" class="btn wishlist-btn" style="background:#fff;border:1px solid #4CAF50;color:#4CAF50;padding:8px 15px;border-radius:4px;" onclick="return confirm('Thêm sản phẩm vào wishlist?')">
                                                                                <i class="fa fa-heart" aria-hidden="true"></i>
                                                                            </button>
                                                                        </form>
                                                                        <a onclick="addToCart('${o.id}')" class="btn add-to-cart-btn" style="background:#4CAF50;color:#fff;padding:8px 15px;border-radius:4px;text-decoration:none;">
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
                                    <%-- PHÂN TRANG --%>
                                    <div class="pagination">
                                        <c:if test="${currentPage > 1}">
                                            <a href="home?page=${currentPage - 1}#product-list" class="prev">&laquo; Previous</a>
                                        </c:if>


                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <a href="home?page=${i}#product-list" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <a href="home?page=${currentPage + 1}#product-list" class="next">Next &raquo;</a>
                                        </c:if>
                                    </div>






                                    <%-- CSS CHO PHÂN TRANG --%>
                                    <style>
                                        .pagination {
                                            text-align: center;
                                            margin-top: 20px;
                                        }
                                        .pagination a {
                                            padding: 8px 16px;
                                            margin: 4px;
                                            border: 1px solid #ddd;
                                            text-decoration: none;
                                            color: #333;
                                            border-radius: 4px;
                                        }
                                        .pagination a:hover, .pagination a.active {
                                            background-color: #4CAF50;
                                            color: white;
                                        }
                                        .pagination a.prev, .pagination a.next {
                                            font-weight: bold;
                                        }
                                    </style>
                                </div>
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


            <div id="biolife-quickview-block" class="biolife-quickview-block">
                <div class="quickview-container">
                    <a href="#" class="btn-close-quickview" data-object="open-quickview-block"><span class="biolife-icon icon-close-menu"></span></a>
                    <div class="biolife-quickview-inner">
                        <div class="media">
                            <ul class="biolife-carousel quickview-for" data-slick='{"arrows":false,"dots":false,"slidesMargin":30,"slidesToShow":1,"slidesToScroll":1,"fade":true,"asNavFor":".quickview-nav"}'>
                                <li><img src="assets/images/details-product/detail_01.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_02.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_03.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_04.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_05.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_06.jpg" alt="" width="500" height="500"></li>
                                <li><img src="assets/images/details-product/detail_07.jpg" alt="" width="500" height="500"></li>
                            </ul>
                            <ul class="biolife-carousel quickview-nav" data-slick='{"arrows":true,"dots":false,"centerMode":false,"focusOnSelect":true,"slidesMargin":10,"slidesToShow":3,"slidesToScroll":1,"asNavFor":".quickview-for"}'>
                                <li><img src="assets/images/details-product/thumb_01.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_02.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_03.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_04.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_05.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_06.jpg" alt="" width="88" height="88"></li>
                                <li><img src="assets/images/details-product/thumb_07.jpg" alt="" width="88" height="88"></li>
                            </ul>
                        </div>
                        <div class="product-attribute">
                            <h4 class="title"><a href="#" class="pr-name">National Fresh Fruit</a></h4>
                            <div class="rating">
                                <p class="star-rating"><span class="width-80percent"></span></p>
                            </div>

                            <div class="price price-contain">
                                <ins><span class="price-amount"><span class="currencySymbol">£</span>85.00</span></ins>
                                <del><span class="price-amount"><span class="currencySymbol">£</span>95.00</span></del>
                            </div>
                            <p class="excerpt">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris vel maximus lacus. Duis ut mauris eget justo dictum tempus sed vel tellus.</p>
                            <div class="from-cart">
                                <div class="qty-input">
                                    <input type="text" name="qty12554" value="1" data-max_value="20" data-min_value="1" data-step="1">
                                    <a href="#" class="qty-btn btn-up"><i class="fa fa-caret-up" aria-hidden="true"></i></a>
                                    <a href="#" class="qty-btn btn-down"><i class="fa fa-caret-down" aria-hidden="true"></i></a>
                                </div>
                                <div class="buttons">
                                    <a href="#" class="btn add-to-cart-btn btn-bold">add to cart</a>
                                </div>
                            </div>

                            <div class="product-meta">
                                <div class="product-atts">
                                    <div class="product-atts-item">
                                        <b class="meta-title">Categories:</b>
                                        <ul class="meta-list">
                                            <li><a href="#" class="meta-link">Milk & Cream</a></li>
                                            <li><a href="#" class="meta-link">Fresh Meat</a></li>
                                            <li><a href="#" class="meta-link">Fresh Fruit</a></li>
                                        </ul>
                                    </div>
                                    <div class="product-atts-item">
                                        <b class="meta-title">Tags:</b>
                                        <ul class="meta-list">
                                            <li><a href="#" class="meta-link">food theme</a></li>
                                            <li><a href="#" class="meta-link">organic food</a></li>
                                            <li><a href="#" class="meta-link">organic theme</a></li>
                                        </ul>
                                    </div>
                                    <div class="product-atts-item">
                                        <b class="meta-title">Brand:</b>
                                        <ul class="meta-list">
                                            <li><a href="#" class="meta-link">Fresh Fruit</a></li>
                                        </ul>
                                    </div>
                                </div>
                                <span class="sku">SKU: N/A</span>
                                <div class="biolife-social inline add-title">
                                    <span class="fr-title">Share:</span>
                                    <ul class="socials">
                                        <li><a href="#" title="twitter" class="socail-btn"><i class="fa fa-twitter" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="facebook" class="socail-btn"><i class="fa fa-facebook" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="pinterest" class="socail-btn"><i class="fa fa-pinterest" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="youtube" class="socail-btn"><i class="fa fa-youtube" aria-hidden="true"></i></a></li>
                                        <li><a href="#" title="instagram" class="socail-btn"><i class="fa fa-instagram" aria-hidden="true"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Scroll Top Button -->
            <a class="btn-scroll-top" 
               style="position: fixed;
               left: 50px !important; /* Ghi đè lên CSS gốc nếu có */
               ">
                <i class="biolife-icon icon-left-arrow"></i>
            </a>

            <script src="assets/js/jquery-3.4.1.min.js"></script>
            <script src="assets/js/bootstrap.min.js"></script>
            <script src="assets/js/jquery.countdown.min.js"></script>
            <script src="assets/js/jquery.nice-select.min.js"></script>
            <script src="assets/js/jquery.nicescroll.min.js"></script>
            <script src="assets/js/slick.min.js"></script>
            <script src="assets/js/biolife.framework.js"></script>
            <script src="assets/js/functions.js"></script>
            <!-- Replace your entire chat widget implementation with this code -->
            <style>
                #chat-button {
                    position: fixed;
                    bottom: 20px;
                    right: 20px;
                    width: 60px;
                    height: 60px;
                    border-radius: 50%;
                    background-color: #4CAF50;
                    box-shadow: 0 3px 10px rgba(0,0,0,0.3);
                    cursor: pointer;
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 9999;
                    transition: transform 0.3s;
                }

                #chat-button:hover {
                    transform: scale(1.05);
                }

                #chat-container {
                    position: fixed;
                    bottom: 90px;
                    right: 20px;
                    width: 350px;
                    height: 500px;
                    background-color: white;
                    border-radius: 10px;
                    box-shadow: 0 5px 15px rgba(0,0,0,0.3);
                    z-index: 9998;
                    display: none;
                    overflow: hidden;
                    flex-direction: column;
                }

                .chat-header {
                    background: #4CAF50;
                    color: white;
                    padding: 15px;
                    text-align: center;
                    font-size: 18px;
                    display: flex;
                    justify-content: space-between;
                }

                .chat-close {
                    cursor: pointer;
                    font-weight: bold;
                    font-size: 20px;
                }

                .chat-messages {
                    padding: 15px;
                    height: 380px;
                    overflow-y: auto;
                    display: flex;
                    flex-direction: column;
                }

                .message {
                    margin-bottom: 15px;
                    clear: both;
                    max-width: 80%;
                }

                .user-message {
                    background: #e0e0e0;
                    color: #333;
                    padding: 10px 15px;
                    border-radius: 18px;
                    float: right;
                    align-self: flex-end;
                }

                .bot-message {
                    background: #4CAF50;
                    color: white;
                    padding: 10px 15px;
                    border-radius: 18px;
                    float: left;
                    align-self: flex-start;
                }

                .chat-input {
                    display: flex;
                    padding: 10px;
                    border-top: 1px solid #e0e0e0;
                }

                .chat-input input {
                    flex: 1;
                    padding: 10px;
                    border: 1px solid #e0e0e0;
                    border-radius: 4px;
                    margin-right: 10px;
                }

                .chat-input button {
                    background: #4CAF50;
                    color: white;
                    border: none;
                    padding: 10px 15px;
                    border-radius: 4px;
                    cursor: pointer;
                }
            </style>

            <!-- Chat Button -->
            <div id="chat-button">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path>
                </svg>
            </div>

            <!-- Chat Container (Replaces iframe with direct interface) -->
            <div id="chat-container">
                <div class="chat-header">
                    <span>Wiish - Hỗ trợ mua tinh dầu</span>
                    <span class="chat-close" id="chat-close">&times;</span>
                </div>
                <div class="chat-messages" id="chat-messages">
                    <!-- Messages will be added here dynamically -->
                </div>
                <div class="chat-input">
                    <input type="text" id="user-input" placeholder="Nhập câu hỏi của bạn...">
                    <button id="send-button">Gửi</button>
                </div>
            </div>

            <script>
                                                                                        document.addEventListener('DOMContentLoaded', function () {
                                                                                            const chatButton = document.getElementById('chat-button');
                                                                                            const chatContainer = document.getElementById('chat-container');
                                                                                            const chatClose = document.getElementById('chat-close');
                                                                                            const messagesContainer = document.getElementById('chat-messages');
                                                                                            const userInput = document.getElementById('user-input');
                                                                                            const sendButton = document.getElementById('send-button');

                                                                                            // Add welcome message when chat is first opened
                                                                                            let isFirstOpen = true;

                                                                                            // Open chat
                                                                                            chatButton.addEventListener('click', function () {
                                                                                                chatContainer.style.display = 'flex';

                                                                                                if (isFirstOpen) {
                                                                                                    addBotMessage("Xin chào! Mình là Wiish - trợ lý ảo của shop tinh dầu. Bạn cần hỗ trợ gì nào?");
                                                                                                    isFirstOpen = false;
                                                                                                }

                                                                                                userInput.focus();
                                                                                            });

                                                                                            // Close chat
                                                                                            chatClose.addEventListener('click', function () {
                                                                                                chatContainer.style.display = 'none';
                                                                                            });

                                                                                            // Send message
                                                                                            function sendMessage() {
                                                                                                const message = userInput.value.trim();
                                                                                                if (message === '')
                                                                                                    return;

                                                                                                // Add user message to chat
                                                                                                addUserMessage(message);
                                                                                                userInput.value = '';

                                                                                                // Show typing indicator
                                                                                                const typingIndicator = document.createElement('div');
                                                                                                typingIndicator.className = 'message bot-message';
                                                                                                typingIndicator.id = 'typing-indicator';
                                                                                                typingIndicator.innerText = 'Đang trả lời...';
                                                                                                messagesContainer.appendChild(typingIndicator);
                                                                                                messagesContainer.scrollTop = messagesContainer.scrollHeight;

                                                                                                // Send request to Flask backend
                                                                                                fetch('http://localhost:5000/chat', {
                                                                                                    method: 'POST',
                                                                                                    headers: {
                                                                                                        'Content-Type': 'application/json'
                                                                                                    },
                                                                                                    body: JSON.stringify({message: message})
                                                                                                })
                                                                                                        .then(response => response.json())
                                                                                                        .then(data => {
                                                                                                            // Remove typing indicator
                                                                                                            const indicator = document.getElementById('typing-indicator');
                                                                                                            if (indicator)
                                                                                                                messagesContainer.removeChild(indicator);

                                                                                                            // Add bot response
                                                                                                            addBotMessage(data.response);
                                                                                                        })
                                                                                                        .catch(error => {
                                                                                                            // Remove typing indicator
                                                                                                            const indicator = document.getElementById('typing-indicator');
                                                                                                            if (indicator)
                                                                                                                messagesContainer.removeChild(indicator);

                                                                                                            // Show error message
                                                                                                            addBotMessage("Xin lỗi, có lỗi khi kết nối với máy chủ. Vui lòng thử lại sau.");
                                                                                                            console.error('Error:', error);
                                                                                                        });
                                                                                            }

                                                                                            // Add event listeners for sending
                                                                                            sendButton.addEventListener('click', sendMessage);
                                                                                            userInput.addEventListener('keypress', function (e) {
                                                                                                if (e.key === 'Enter') {
                                                                                                    sendMessage();
                                                                                                }
                                                                                            });

                                                                                            // Function to add user message to chat
                                                                                            function addUserMessage(message) {
                                                                                                const messageDiv = document.createElement('div');
                                                                                                messageDiv.className = 'message user-message';
                                                                                                messageDiv.innerText = message;
                                                                                                messagesContainer.appendChild(messageDiv);
                                                                                                messagesContainer.scrollTop = messagesContainer.scrollHeight;
                                                                                            }

                                                                                            // Function to add bot message to chat
                                                                                            function addBotMessage(message) {
                                                                                                const messageDiv = document.createElement('div');
                                                                                                messageDiv.className = 'message bot-message';

                                                                                                // Handle markdown-like formatting from Python
                                                                                                message = message.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
                                                                                                message = message.replace(/\n/g, '<br>');

                                                                                                messageDiv.innerHTML = message;
                                                                                                messagesContainer.appendChild(messageDiv);
                                                                                                messagesContainer.scrollTop = messagesContainer.scrollHeight;
                                                                                            }
                                                                                        });
                            </script>
                            </body>

                            </html>