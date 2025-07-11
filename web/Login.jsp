<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <link rel="stylesheet" href="assets/css/main-color.css">
        <script>
            function sendOTP() {
                const input = $('#emailInput').val();
                $.ajax({
                    url: 'forgot-password',
                    method: 'POST',
                    data: {
                        action: 'sendOTP',
                        input: input
                    },
                    success: function (response) {
                        alert(response.message);
                        if (response.success) {
                            $('#step1').hide();
                            $('#step2').show();
                        }
                    }
                });
            }

            function verifyOTP() {
                const otp = $('#otpInput').val();
                $.ajax({
                    url: 'forgot-password',
                    method: 'POST',
                    data: {
                        action: 'verifyOTP',
                        otp: otp
                    },
                    success: function (response) {
                        alert(response.message);
                        if (response.success) {
                            $('#step2').hide();
                            $('#step3').show();
                        }
                    }
                });
            }

            function resetPassword() {
                const password = $('#newPassword').val();
                $.ajax({
                    url: 'forgot-password',
                    method: 'POST',
                    data: {
                        action: 'resetPassword',
                        newPassword: password
                    },
                    success: function (response) {
                        alert(response.message);
                        if (response.success) {
                            $('#forgotPasswordModal').modal('hide');
                            location.reload();
                        }
                    }
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
                <h1 class="page-title">Craft Villages</h1>
            </div>

            <!--Navigation section-->
            <div class="container">
                <nav class="biolife-nav">
                    <ul>
                        <li class="nav-item"><a href="index-2.html" class="permal-link">Home</a></li>
                        <li class="nav-item"><span class="current-page">Authentication</span></li>
                    </ul>
                </nav>
            </div>

            <div class="page-contain login-page">

                <!-- Main content -->

                <div id="main-content" class="main-content">

                    <div class="container">


                        <div class="row">

                        <%
                            String usernameCookieSaved = "";
                            String passwordCookieSaved = "";
                            Cookie[] cookieListFromBrowser = request.getCookies();
                            if (cookieListFromBrowser != null) {
                                for (Cookie c : cookieListFromBrowser) {
                                    if (c.getName().equals("COOKIE_USERNAME")) {
                                        usernameCookieSaved = c.getValue();
                                    }
                                    if (c.getName().equals("COOKIE_PASSWORD")) {
                                        passwordCookieSaved = c.getValue();
                                    }
                                }
                            }
                        %>
                        <!--Form Sign In-->
                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                            <div class="signin-container">
                                <form action="login" name="frm-login" method="post" onsubmit="console.log('Form submitted via POST');
                                    return true;">
                                    <!-- Add success message display -->
                                    <c:if test="${not empty registerSuccess}">
                                        <div class="alert alert-success" style="color: #155724; background-color: #d4edda; border: 1px solid #c3e6cb; padding: 10px; border-radius: 4px; margin: 10px 0;">
                                            ${registerSuccess}
                                        </div>
                                        <c:remove var="registerSuccess" scope="session" />
                                    </c:if>
                                    <p class="form-row">
                                        <label for="fid-name">Username or Email:<span class="requite">*</span></label>
                                        <c:choose>
                                            <c:when test="${not empty registeredEmail}">
                                                <input type="text" id="fid-name" name="user" value="${registeredEmail}" class="txt-input" placeholder="Enter your username or email">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" id="fid-name" name="user" value="<%= usernameCookieSaved%>" class="txt-input" placeholder="Enter your username or email">
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <!-- Remove registeredEmail from session after use -->
                                    <c:remove var="registeredEmail" scope="session" />
                                    <p class="form-row">
                                        <label for="fid-pass">Password:<span class="requite">*</span></label>
                                        <input type="password" id="fid-pass" name="pass" value="<%= passwordCookieSaved%>" class="txt-input">
                                    </p>
                                    <p class="remember-me">
                                        <input type="checkbox" name="rememberMe" id="rememberMe"  />
                                        <label for="rememberMe">Remember me</label>
                                        <br>

                                    </p>
                                    <!-- Add error message display -->
                                    <c:if test="${not empty error}">
                                        <div class="alert alert-danger" style="color: red; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin: 10px 0;">
                                            ${error}
                                        </div>
                                    </c:if>

                                    <c:if test="${not empty mess}">
                                        <div class="alert alert-danger" style="color: red; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; border-radius: 4px; margin: 10px 0;">
                                            ${mess}
                                        </div>
                                    </c:if>

                                    <p class="form-row wrap-btn">

                                        <button class="btn btn-submit btn-bold" type="submit" onclick="console.log('Submit button clicked');">sign in</button>

                                    <div class="form-row">
                                        <a href="#" class="link-to-help" data-toggle="modal" data-target="#forgotPasswordModal">
                                            Forgot password?
                                        </a>
                                    </div>

                                    </p>

                                </form>

                            </div>

                        </div>

                        <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/CraftVillage/login&response_type=code&client_id=599094914158-74a3ge5i54m4sukpj2op8ecs7n4fkf9s.apps.googleusercontent.com&approval_prompt=force">

                            Sign in with Google

                        </a>

                        <!--Go to Register form-->

                        <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">

                            <div class="register-in-container">

                                <div class="intro">

                                    <h4 class="box-title">New Customer?</h4>

                                    <p class="sub-title">Create an account with us and you'll be able to:</p>

                                    <ul class="lis">

                                        <li>Check out faster</li>

                                        <li>Save multiple shipping anddesses</li>

                                        <li>Access your order history</li>

                                        <li>Track new orders</li>

                                        <li>Save items to your Wishlist</li>

                                    </ul>

                                    <!-- Update link to registration page -->

                                    <a href="register" class="btn btn-bold">Create an account</a>

                                </div>

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
        <!--        modal-->
        <div class="modal fade" id="forgotPasswordModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Forgot Password</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <!-- Bước 1: Nhập email -->
                        <div id="step1">
                            <div class="form-group">
                                <label>Email or Email</label>
                                <input type="text" class="form-control" id="emailInput">
                            </div>
                            <button class="btn btn-primary" onclick="sendOTP()">Send OTP</button>
                        </div>

                        <!-- Bước 2: Nhập OTP -->
                        <div id="step2" style="display:none">
                            <div class="form-group">
                                <label>Enter OTP</label>
                                <input type="text" class="form-control" id="otpInput">
                                <small class="text-muted">OTP code is valid for 1 minute</small>
                            </div>
                            <button class="btn btn-primary" onclick="verifyOTP()">Confirm</button>
                        </div>

                        <!-- Bước 3: Đặt mật khẩu mới -->
                        <div id="step3" style="display:none">
                            <div class="form-group">
                                <label>New password</label>
                                <input type="password" class="form-control" id="newPassword">
                            </div>
                            <button class="btn btn-primary" onclick="resetPassword()">Change password</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>

</html>