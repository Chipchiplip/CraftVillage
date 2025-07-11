<%-- 
    Document   : contact-artist
    Created on : Jul 10, 2025, 6:28:03 PM
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
            .contact-container {
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
            .message-list {
                max-height: 400px;
                overflow-y: auto;
                margin-bottom: 20px;
            }

            .message-item {
                padding: 10px;
                border-radius: 8px;
                margin-bottom: 10px;
                width: fit-content;
                max-width: 70%;
                clear: both;
            }

            .message-sent {
                background-color: #d4edda;
                margin-left: auto;
                text-align: right;
            }

            .message-received {
                background-color: #f8d7da;
                margin-right: auto;
                text-align: left;
            }

            .message-content p {
                margin: 0;
                .message-list {
                    max-height: 400px;
                    overflow-y: auto;
                    margin-bottom: 20px;
                }

                .message-item {
                    padding: 10px;
                    border-radius: 8px;
                    margin-bottom: 10px;
                    width: fit-content;
                    max-width: 70%;
                    clear: both;
                }

                .message-sent {
                    background-color: #d4edda;
                    margin-left: auto;
                    text-align: right;
                }

                .message-received {
                    background-color: #f8d7da;
                    margin-right: auto;
                    text-align: left;
                }

                .message-content p {
                    margin: 0;
                }
                .message-list {
                    max-height: 400px;
                    overflow-y: auto;
                }
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
            <!--Hero Section-->
            <div class="hero-section hero-background">
                <h1 class="page-title">Contact</h1>
            </div>
            <!-- Page Contain -->
            <div class="page-contain">
                <div id="main-content" class="main-content">
                    <!--Navigation section-->
                    <div class="container">
                        <nav class="biolife-nav">
                            <ul>
                                <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                                <li class="nav-item"><span class="contact">Contact</span></li>
                                <li class="nav-item"><span class="#">${messageThread.messageName}</span></li>
                        </ul>
                    </nav>
                </div>
                <div class="container">
                    <div class="contact-container">
                        <div class="profile-header">
                            <h4>Contact Artist: ${messageThread.messageName}</h4>
                        </div>

                        <!-- Lịch sử trò chuyện -->
                        <div class="message-list" style="max-height: 400px; overflow-y: auto; padding-right: 10px;">
                            <c:forEach var="msg" items="${listMessage}">
                                <c:choose>
                                    <c:when test="${msg.senderID == sessionScope.acc.userID}">
                                        <div class="message-item message-sent">
                                            <div class="message-content">
                                                <p>${msg.messageContent}</p>
                                                <c:if test="${not empty msg.attachmentUrl}">
                                                    <p>
                                                        <a href="${msg.attachmentUrl}" target="_blank">
                                                            Download Attachment
                                                        </a>
                                                    </p>
                                                </c:if>
                                                <span class="text-muted">
                                                    <fmt:formatDate value="${msg.sentDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </span>
                                            </div>
                                        </div>
                                    </c:when>

                                    <c:otherwise>
                                        <div class="message-item message-received">
                                            <div class="message-content">
                                                <p>${msg.messageContent}</p>
                                                <c:if test="${not empty msg.attachmentUrl}">
                                                    <p>
                                                        <a href="${msg.attachmentUrl}" target="_blank">
                                                            Download Attachment
                                                        </a>
                                                    </p>
                                                </c:if>
                                                <span class="text-muted">
                                                    <fmt:formatDate value="${msg.sentDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </span>
                                            </div>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>

                        <!-- Form gửi tin nhắn -->
                        <form action="contact-artist" method="post" >
                            <input type="hidden" name="sellerID" value="${messageThread.sellerID}" />
                            <input type="hidden" name="threadID" value="${messageThread.threadID}" />
                            <input type="hidden" name="senderID" value="${sessionScope.acc.userID}" />
                            <div class="form-group">
                                <label for="messageContent">Your Message</label>
                                <textarea id="messageContent" name="messageContent" class="form-control" rows="3" required></textarea>
                            </div>
                            <div class="form-group">
                                <label for="attachmentUrl">Attachment (Optional)</label>
                                <input type="text" name="attachmentUrl" class="form-control" id="attachmentUrl">
                            </div>
                            <div class="text-right">
                                <button type="submit" class="btn-update">Send Message</button>
                            </div>
                        </form>
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
