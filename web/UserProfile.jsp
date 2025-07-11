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
        <title>User Profile - Da Nang Craft Village</title>
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
        <style>
            .profile-container {
                max-width: 800px;
                margin: 0 auto;
                padding: 30px;
                background-color: #f7f7f7;
                border-radius: 5px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .form-group {
                margin-bottom: 20px;
            }
            .form-control {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }
            .btn-update {
                background-color: #4CAF50;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
            }
            .btn-update:hover {
                background-color: #45a049;
            }
            .profile-header {
                margin-bottom: 30px;
                text-align: center;
            }
            .alert {
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 4px;
            }
            .alert-success {
                background-color: #dff0d8;
                border-color: #d6e9c6;
                color: #3c763d;
            }
            .alert-danger {
                background-color: #f2dede;
                border-color: #ebccd1;
                color: #a94442;
            }
            .btn-change-password {
                background-color: #2196F3;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 4px;
                cursor: pointer;
                font-size: 16px;
                text-decoration: none;
                display: inline-block;
                margin-top: 10px;
            }
            .btn-change-password:hover {
                background-color: #0b7dda;
            }
            .form-control[readonly] {
                background-color: #f8f9fa;
                color: #6c757d;
                cursor: not-allowed;
            }
            .text-muted {
                color: #6c757d !important;
                font-size: 0.875rem;
                margin-top: 0.25rem;
            }
        </style>
    </head>
    <body class="biolife-body">
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
        <!-- Page Contain -->
        <div class="page-contain">
            <div id="main-content" class="main-content">
                <!--Navigation section-->
                <div class="container">
                    <nav class="biolife-nav">
                        <ul>
                            <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                            <li class="nav-item"><span class="current-page">Profile</span></li>
                        </ul>
                    </nav>
                </div>
                <div class="container">
                    <div class="profile-container">
                        <div class="profile-header">
                            <h2>User Profile</h2>
                            <p>Edit your account information below</p>
                        </div>
                        
                        <!-- Debug info - This will help identify issues -->
                        <div style="background-color: #f8f9fa; padding: 10px; margin-bottom: 15px; border-radius: 4px;">
                            <p>Session check: User ${sessionScope.acc != null ? 'found' : 'not found'} in session.</p>
                            <c:if test="${sessionScope.acc != null}">
                                <p>Username: ${sessionScope.acc.userName}</p>
                                <p>ID: ${sessionScope.acc.userID}</p>
                            </c:if>
                        </div>
                        
                        <c:if test="${not empty message}">
                            <div class="alert ${messageType == 'success' ? 'alert-success' : 'alert-danger'}">
                                ${message}
                            </div>
                        </c:if>
                        
                        <c:if test="${sessionScope.acc != null}">
                            <form action="updateProfile" method="post">
                                <input type="hidden" name="action" value="update">
                                
                                <div class="form-group">
                                    <label for="username">Username</label>
                                    <input type="text" id="username" name="username" class="form-control" value="${sessionScope.acc.userName}" readonly>
                                    <small class="form-text text-muted">Username cannot be changed</small>
                                </div>
                                
                                <div class="form-group">
                                    <label for="email">Email Address</label>
                                    <input type="email" id="email" name="email" class="form-control" value="${sessionScope.acc.email}" required>
                                    <small class="form-text text-muted">Make sure this email is not already registered</small>
                                </div>
                                
                                <div class="form-group">
                                    <label for="fullName">Full Name</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" value="${sessionScope.acc.fullName}">
                                </div>
                                
                                <div class="form-group">
                                    <label for="phone">Phone Number</label>
                                    <input type="text" id="phone" name="phone" class="form-control" value="${sessionScope.acc.phoneNumber}">
                                    <small class="form-text text-muted">Make sure this phone number is not already registered</small>
                                </div>
                                
                                <div class="form-group">
                                    <label for="address">Address</label>
                                    <textarea id="address" name="address" class="form-control" rows="3">${sessionScope.acc.address}</textarea>
                                </div>
                                
                                <button type="submit" class="btn-update">Update Profile</button>
                            </form>
                            
                            <div style="margin-top: 30px; border-top: 1px solid #ddd; padding-top: 20px;">
                                <h4>Password Management</h4>
                                <p>To change your password, please click the button below:</p>
                                <a href="${pageContext.request.contextPath}/changePassword" class="btn-change-password">Change Password</a>
                            </div>
                            <div style="margin-top: 30px; border-top: 1px solid #ddd; padding-top: 20px;">
                                <h4>Upgrade Account</h4>
                                <p>Upgrade to become part of us, connecting the best to everyone.</p>
                                <a href="${pageContext.request.contextPath}/request-upgrade" class="btn-change-password">Upgrade Account</a>
                               <a href="${pageContext.request.contextPath}/list-request?typeForm=3&userID=${sessionScope.acc.userID}" class="btn-change-password">Request List</a>                            </div>
                        </c:if>
                        
                        <c:if test="${sessionScope.acc == null}">
                            <div class="alert alert-danger">
                                You must be logged in to view this page. <a href="Login.jsp">Click here to login</a>
                            </div>
                        </c:if>
                        
                        <!-- Display success/error messages from controller -->
                        <c:if test="${not empty success}">
                            <div class="alert alert-success">
                                ${success}
                            </div>
                        </c:if>
                        
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">
                                ${error}
                            </div>
                        </c:if>
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
    </body>
</html> 