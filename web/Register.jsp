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
    <link rel="shortcut icon" type="image/x-icon" href="hinh anh/Logo/cropped-Favicon-1-32x32.png" />
    <link rel="stylesheet" href="assets/css/bootstrap.min.css">
    <link rel="stylesheet" href="assets/css/animate.min.css">
    <link rel="stylesheet" href="assets/css/font-awesome.min.css">
    <link rel="stylesheet" href="assets/css/nice-select.css">
    <link rel="stylesheet" href="assets/css/slick.min.css">
    <link rel="stylesheet" href="assets/css/style.css">
    <link rel="stylesheet" href="assets/css/main-color.css">
    
    <!-- Add this script to check for CSS/JS loading errors -->
    <script>
        function handleResourceError(e) {
            console.error('Failed to load resource:', e.target.src || e.target.href);
        }
        window.addEventListener('error', function(e) {
            if (e.target.tagName === 'LINK' || e.target.tagName === 'SCRIPT') {
                handleResourceError(e);
            }
        }, true);
    </script>
</head>

<script>
    // Improve preloader handling
    window.onload = function() {
        try {
            document.getElementById("biof-loading").style.display = "none";
            console.log("Page loaded successfully");
        } catch (error) {
            console.error("Error during page load:", error);
        }
    };
</script>

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
    <c:catch var="menuException">
        <jsp:include page="Menu.jsp"></jsp:include>
    </c:catch>
    <c:if test="${not empty menuException}">
        <div class="alert alert-danger">Error loading menu: ${menuException.message}</div>
    </c:if>

    <!--Hero Section-->
    <div class="hero-section hero-background">
        <h1 class="page-title">Create Account</h1>
    </div>

    <!--Navigation section-->
    <div class="container">
        <nav class="biolife-nav">
            <ul>
                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                <li class="nav-item"><span class="current-page">Register</span></li>
            </ul>
        </nav>
    </div>

    <div class="page-contain login-page">

        <!-- Main content -->
        <div id="main-content" class="main-content">
            <div class="container">

                <div class="row">
                    <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 mx-auto">
                        <div class="signin-container">
                            <h3 class="box-title">Create your Account</h3>
                            
                            <!-- Display error message if any -->
                            <c:if test="${not empty error}">
                                <p style="color: red">${error}</p>
                            </c:if>
                            
                            <form action="register" name="frm-register" method="post">
                                <p class="form-row">
                                    <label for="username">Username:<span class="requite">*</span></label>
                                    <input type="text" id="username" name="userName" value="${param.userName}" class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <label for="email">Email Address:<span class="requite">*</span></label>
                                    <input type="email" id="email" name="email" value="${param.email}" class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <label for="fullName">Full Name:</label>
                                    <input type="text" id="fullName" name="fullName" value="${param.fullName}" class="txt-input">
                                </p>
                                <p class="form-row">
                                    <label for="pass">Password:<span class="requite">*</span></label>
                                    <input type="password" id="pass" name="password" value="" class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <label for="re-pass">Confirm Password:<span class="requite">*</span></label>
                                    <input type="password" id="re-pass" name="rePassword" value="" class="txt-input" required>
                                </p>
                                <p class="form-row">
                                    <label for="address">Address:</label>
                                    <input type="text" id="address" name="address" value="${param.address}" class="txt-input">
                                </p>
                                <p class="form-row">
                                    <label for="phone">Phone Number:</label>
                                    <input type="text" id="phone" name="phoneNumber" value="${param.phoneNumber}" class="txt-input">
                                </p>
                                <p class="form-row wrap-btn">
                                    <button class="btn btn-submit btn-bold" type="submit">Register</button>
                                    <a href="login" class="link-to-help">Already have an account? Login</a>
                                </p>
                            </form>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <c:catch var="footerException">
        <jsp:include page="Footer.jsp"></jsp:include>
    </c:catch>
    <c:if test="${not empty footerException}">
        <div class="alert alert-danger">Error loading footer: ${footerException.message}</div>
    </c:if>

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
    
    <!-- Add this before closing body tag to check all resources loaded -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            console.log('DOM fully loaded');
        });
    </script>
</body>

</html>