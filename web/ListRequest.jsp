<%-- 
    Document   : ListRequest
    Created on : Jul 6, 2025, 12:52:55 AM
    Author     : ACER
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8"/>
        <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Da Nang Craft Village</title>

        <!-- Fonts and CSS -->
        <link href="https://fonts.googleapis.com/css?family=Cairo:400,600,700&display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Poppins:600&display=swap" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Playfair+Display:400i,700i" rel="stylesheet"/>
        <link href="https://fonts.googleapis.com/css?family=Ubuntu&display=swap" rel="stylesheet"/>

        <link rel="shortcut icon" type="image/x-icon" href="hinhanh/Logo/cropped-Favicon-1-32x32.png"/>
        <link rel="stylesheet" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="assets/css/animate.min.css"/>
        <link rel="stylesheet" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/nice-select.css"/>
        <link rel="stylesheet" href="assets/css/slick.min.css"/>
        <link rel="stylesheet" href="assets/css/style.css"/>
        <link rel="stylesheet" href="assets/css/main-color03-green.css"/>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">

        <script src="assets/js/jquery-3.4.1.min.js"></script>

        

    </head>
    <body class="biolife-body">

        <jsp:include page="Menu.jsp" />
        <!--Navigation section-->
        <div class="container">
            <nav class="biolife-nav">
                <ul>
                    <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                    <li class="nav-item"><a href="userprofile" class="permal-link">Profile</a></li>
                    <li class="nav-item"><span class="current-page">List Request</span></li>
                </ul>
            </nav>
        </div>
        <c:if test="${not empty message}">
            <div id="notification" class="${error == '1' ? 'success' : 'error'}">
                ${message}
            </div>

            <script>
                setTimeout(() => {
                    const noti = document.getElementById("notification");
                    if (noti) {
                        noti.style.opacity = '0';
                        setTimeout(() => noti.remove(), 500);
                    }
                }, 4000);
            </script>
        </c:if>

        <div class="container mt-4">
    <h2 class="text-2xl font-bold mb-4">Danh sách Yêu cầu Seller Verification</h2>

    <c:choose>
        <c:when test="${not empty requestList}">
            <div class="overflow-x-auto">
                <table class="table table-bordered table-striped text-sm">
                    <thead class="bg-green-500 text-white">
                        <tr>
                            <th>#</th>
                            <th>Verification ID</th>
                            <th>Seller ID</th>
                            <th>Business Type</th>
                            <th>Business Village Categry</th>
                            <th>Business Village Name</th>
                            <th>Business Village Address</th>
                            <th>Product Category</th>
                            <th>Profile Picture URL</th>
                            <th>Contact Person</th>
                            <th>Contact Phone</th>
                            <th>Contact Email</th>
                            <th>ID Card Number</th>
                            <th>ID Card Front URL</th>
                            <th>ID Card Back URL</th>
                            <th>Business License</th>
                            <th>Tax Code</th>
                            <th>Document URL</th>
                            <th>Note</th>
                            <th>Created Date</th>
                            <th>Verification Status</th>
                            <th>Verified By</th>
                            <th>Verified Date</th>
                            <th>Reject Reason</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="req" items="${requestList}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${req.verificationID}</td>
                                <td>${req.sellerID}</td>
                                <td>${req.businessType}</td>
                                <td>${req.businessVillageCategry}</td>
                                <td>${req.businessVillageName}</td>
                                <td>${req.businessVillageAddress}</td>
                                <td>${req.productProductCategory}</td>
                                <td>
                                    <c:if test="${not empty req.profileVillagePictureUrl}">
                                        <a href="${req.profileVillagePictureUrl}" target="_blank" class="text-blue-600 hover:underline">View</a>
                                    </c:if>
                                </td>
                                <td>${req.contactPerson}</td>
                                <td>${req.contactPhone}</td>
                                <td>${req.contactEmail}</td>
                                <td>${req.idCardNumber}</td>

                                <!-- ID Card Front URL -->
                                <td>
                                    <c:if test="${not empty req.idCardFrontUrl}">
                                        <a href="${req.idCardFrontUrl}" target="_blank" class="text-blue-600 hover:underline">View PDF</a>
                                    </c:if>
                                </td>

                                <!-- ID Card Back URL -->
                                <td>
                                    <c:if test="${not empty req.idCardBackUrl}">
                                        <a href="${req.idCardBackUrl}" target="_blank" class="text-blue-600 hover:underline">View PDF</a>
                                    </c:if>
                                </td>

                                <!-- Business License -->
                                <td>
                                    <c:if test="${not empty req.businessLicense}">
                                        <a href="${req.businessLicense}" target="_blank" class="text-blue-600 hover:underline">View PDF</a>
                                    </c:if>
                                </td>

                                <!-- Tax Code -->
                                <td>
                                    <c:if test="${not empty req.taxCode}">
                                        <a href="${req.taxCode}" target="_blank" class="text-blue-600 hover:underline">View PDF</a>
                                    </c:if>
                                </td>

                                <!-- Document URL -->
                                <td>
                                    <c:if test="${not empty req.documentUrl}">
                                        <a href="${req.documentUrl}" target="_blank" class="text-blue-600 hover:underline">View PDF</a>
                                    </c:if>
                                </td>

                                <td>${req.note}</td>

                                <!-- Created Date -->
                                <td>
                                    <c:if test="${not empty req.createdDate}">
                                        <fmt:formatDate value="${req.createdDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </c:if>
                                </td>

                                <!-- Verification Status -->
                                <td>
                                    <c:choose>
                                        <c:when test="${req.verificationStatus == 0}">
                                            <span class="text-yellow-500 font-semibold">Processing</span>
                                        </c:when>
                                        <c:when test="${req.verificationStatus == 1}">
                                            <span class="text-green-600 font-semibold">Approved</span>
                                        </c:when>
                                        <c:when test="${req.verificationStatus == 2}">
                                            <span class="text-red-600 font-semibold">Rejected</span>
                                        </c:when>
                                        <c:otherwise>
                                            Unknown
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>${req.verifiedBy}</td>
                                <td>
                                    <c:if test="${not empty req.verifiedDate}">
                                        <fmt:formatDate value="${req.verifiedDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </c:if>
                                </td>
                                <td>${req.rejectReason}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <p class="text-red-500">Hiện tại bạn chưa gửi yêu cầu Seller Verification nào.</p>
        </c:otherwise>
    </c:choose>
</div>


        <jsp:include page="Footer.jsp" />

    </body>
</html>
