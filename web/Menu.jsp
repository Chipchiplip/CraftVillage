<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<header id="header" class="header-area style-01 layout-03">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <div class="header-top bg-main hidden-xs">
        <div class="container">
            <div class="top-bar left">
                <ul class="horizontal-menu">
                    <li>
                        <a href="mailto:dcvhoang.work@gmail.com?subject=Phản Hồi Về Chất Lượng Sản phẩm email&body=Nội dung email">
                            <i class="fa fa-envelope" aria-hidden="true"></i>dcvhoang.work@gmail.com
                        </a>
                    </li>
                    <li><a href="https://www.youtube.com/@Achipdichdao" target="_blank"><i class="fa fa-facebook" aria-hidden="true"></i>Da Nang Craft Village</a></li>
                </ul>
            </div>
            <div class="top-bar right" style="max-width: unset !important;">
                <ul class="horizontal-menu">
                    <c:if test="${sessionScope.account != null}">
                        <li style="min-width: unset !important;">
                            <a href="logout" class="login-link" style="min-width: unset !important; display: inline-block;"><i></i>Logout</a>
                        </li>
                        <li><a href="userprofile" class="login-link"><i class="biolife-icon icon-login"></i>Hello ${sessionScope.account.fullName}</a></li>
                        </c:if>
                        <c:if test="${sessionScope.account == null}">
                        <li><a href="Login.jsp" class="login-link"><i class="biolife-icon icon-login"></i>Login/Register</a></li>
                        </c:if>
                </ul>
            </div>
        </div>
    </div>
    <div class="header-middle biolife-sticky-object">
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-2 col-md-6 col-xs-6">
                    <a href="home" class="biolife-logo"><img src="hinhanh/Logo/logocraft.png" alt="craft logo" width="220" height="60"></a>
                </div>
                <div class="col-lg-6 col-md-7 hidden-sm hidden-xs">
                    <div class="primary-menu">
                        <ul class="menu biolife-menu clone-main-menu clone-primary-menu" id="primary-menu" data-menuname="main menu">
                            <li class="menu-item"><a href="home">Home</a></li>
                            <li class="menu-item"><a href="product" class="menu-name" data-title="Product">Product</a></li>
                            <li class="menu-item menu-item-has-children has-child">
                                <a href="#" class="menu-name" data-title="Craft Types">Craft Types</a>
                                <ul class="sub-menu">
                                    <c:forEach var="c" items="${listVillages}">
                                        <li class="menu-item">
                                            <a href="craftVillage?typeID=${c.typeID}">${c.typeName}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                            <li class="menu-item"><a href="tour360" class="menu-name">360° Tour</a></li>
                            <li class="menu-item menu-item-has-children has-child">
                                <a href="#" class="menu-name" data-title="Craft Villages">Craft Villages</a>
                                <ul class="sub-menu">
                                    <c:forEach items="${listAllVillage}" var="village">
                                        <li class="menu-item"><a href="village?id=${village.villageID}">${village.villageName}</a></li>
                                        </c:forEach>
                                </ul>
                            </li>
                            <c:choose>
                                <c:when test="${not empty sessionScope.acc}">
                                    <!-- Đã đăng nhập -->
                                    <li class="menu-item">
                                        <a href="contact?userID=${sessionScope.acc.userID}" class="menu-name" data-title="Product">
                                            Contact
                                        </a>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <!-- Chưa đăng nhập -->
                                    <li class="menu-item">
                                        <a href="Login.jsp" class="menu-name" data-title="Product">
                                            Contact
                                        </a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${sessionScope.account.roleID == 3}">
                                <li class="menu-item"><a href="admin">Dashboard</a></li>
                                </c:if>
                                <c:if test="${sessionScope.account.roleID == 2 }">
                                <li class="menu-item"><a href="seller">Dashboard</a></li>
                                </c:if>
                        </ul>
                    </div>
                </div>
                <div class="col-lg-3 col-md-3 col-md-6 col-xs-6">
                    <div class="biolife-cart-info">
                        <!-- Mobile search -->
                        <div class="mobile-search">
                            <a href="javascript:void(0)" class="open-searchbox"><i class="biolife-icon icon-search"></i></a>
                            <div class="mobile-search-content">
                                <form action="search" method="post" class="form-search" name="mobile-seacrh">
                                    <a href="#" class="btn-close"><span class="biolife-icon icon-close-menu"></span></a>
                                    <input type="text" name="txt" class="input-text" value="" placeholder="Search here...">
                                    <button type="submit" class="btn-submit">go</button>
                                </form>
                            </div>
                        </div>
                        <!-- Wishlist -->
                        <div class="wishlist-block hidden-sm hidden-xs">
                            <a href="wishlist" class="link-to">
                                <span class="icon-qty-combine">
                                    <i class="icon-heart-bold biolife-icon"></i>
                                    <span class="qty">
                                        ${sessionScope.wishlistCount != null ? sessionScope.wishlistCount : "0"}
                                    </span>
                                </span>
                            </a>
                        </div>
                        <!-- User account -->
                        <div class="minicart-block">
                            <div class="minicart-contain">
                                <c:if test="${sessionScope.account != null}">

                                </c:if>

                            </div>
                        </div>
                        <!-- Wishlist -->
                        <!-- Shopping cart -->
                        <div class="minicart-block">
                            <div class="minicart-contain">
                                <a href="cart" class="link-to">
                                    <span class="icon-qty-combine">
                                        <i class="icon-cart-mini biolife-icon"></i>
                                    </span>
                                    <span class="title">Cart</span>

                                </a>
                            </div>
                        </div>

                        <!-- Mobile menu toggle -->
                        <div class="mobile-menu-toggle">
                            <a class="btn-toggle" data-object="open-mobile-menu" href="javascript:void(0)">
                                <span></span>
                                <span></span>
                                <span></span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="header-bottom hidden-sm hidden-xs">
        <div class="container">
            <div class="row">
                <div class="col-lg-3 col-md-4">
                    <div class="vertical-menu vertical-category-block">
                        <div class="block-title">
                            <span class="menu-icon">
                                <span class="line-1"></span>
                                <span class="line-2"></span>
                                <span class="line-3"></span>
                            </span>
                            <span class="menu-title">All Categories</span>
                            <span class="angle" data-tgleclass="fa fa-caret-down"><i class="fa fa-caret-up" aria-hidden="true"></i></span>
                        </div>
                        <div class="wrap-menu">
                            <ul class="menu clone-main-menu">
                                <c:forEach items="${listCC}" var="category">
                                    <li class="list-group-item text-white"><a href="category?categoryID=${category.categoryID}">${category.categoryName}</a></li>
                                    </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="col-lg-9 col-md-8 padding-top-2px">
                    <div class="header-search-bar layout-01">
                        <form action="search" class="form-search" name="desktop-seacrh" onsubmit="return syncSearch();">
                            <input type="hidden" name="action" value="search">
                            <input type="text" name="txt" class="input-text" value="${param.txt}" placeholder="Search here..."
                                   <button type="submit" class="btn-submit"><i class="biolife-icon icon-search"></i></button>
                        </form>
                    </div>
                    <div class="live-info">
                        <p class="telephone"><i class="fa fa-phone" aria-hidden="true"></i><b class="phone-number">0914 145 788</b></p>
                        <p class="working-time">7 am - 5 pm</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

</header>