<%-- 
    Document   : admin-vreview-management
    Created on : Jul 9, 2025, 4:58:13 PM
    Author     : ACER
--%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Craft Review Page</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <script>
            function toggleResponseForm(id) {
                const form = document.getElementById('responseForm-' + id);
                if (form) {
                    form.classList.toggle('hidden');
                }
            }

            function confirmDeleteReview(id) {
                if (confirm("Are you sure you want to delete this review?")) {
                    document.getElementById("deleteReviewID").value = id;
                    document.getElementById("deleteForm").submit();
                }
            }
        </script>
    </head>
    <body>
        <div class="flex min-h-screen">
            <jsp:include page="admin-sidebar.jsp"/>

            <div class="flex-1 p-6">

                <!-- Notification -->
                <c:if test="${not empty message}">
                    <div id="notification"
                         class="fixed top-5 right-5 z-50 px-4 py-3 rounded shadow-lg text-white
                         ${error == '1' ? 'bg-green-500' : 'bg-red-500'} opacity-100 transition-opacity duration-500">
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

                <!-- Title, Back, and Search -->
                <div class="flex flex-col md:flex-row md:items-center md:justify-between w-full mb-6 gap-4">
                    <!-- Left side -->
                    <div class="flex items-center gap-4">
                        <a href="admin-product-management"
                           class="text-gray-500 hover:text-gray-700 underline text-sm">
                            ← Back
                        </a>
                        <h1 class="text-2xl font-bold">${name} Review List (New: ${listReviewToday.size()})</h1>
                    </div>

                    <!-- Right side (Search) -->
                    <form action="admin-preview-management" method="post" class="flex gap-2 items-center">
                        <input type="hidden" name="villageID" value="${villageID}"/>
                        <input type="hidden" name="villageName" value="${villageName}"/>
                        <input type="hidden" name="typeName" value="searchReview"/>
                        <input type="text" name="userID" placeholder="User ID"
                               class="border border-gray-300 rounded px-3 py-2 text-sm w-48"/>
                        <button type="submit"
                                class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                            Search
                        </button>
                    </form>
                </div>

                <!-- Review Table -->
                <div class="overflow-x-auto">
                    <table class="w-full table-auto border border-gray-300 text-sm">
                        <thead class="bg-gray-200 text-center">
                            <tr>
                                <th class="p-2 border">Review ID</th>
                                <th class="p-2 border">User ID</th>
                                <th class="p-2 border">Rating</th>
                                <th class="p-2 border">Text</th>
                                <th class="p-2 border">Date</th>
                                <th class="p-2 border">Response</th>
                                <th class="p-2 border">Response Date</th>
                                <th class="p-2 border">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${listVReview}">
                                <tr class="bg-white border text-center align-top">
                                    <td class="p-2 border">${r.reviewID}</td>
                                    <td class="p-2 border">${r.userID}</td>
                                    <td class="p-2 border">${r.rating}</td>
                                    <td class="p-2 border whitespace-pre-line text-left">${r.reviewText}</td>
                                    <td class="p-2 border">${r.reviewDate}</td>
                                    <td class="p-2 border whitespace-pre-line text-left text-green-700">${r.response}</td>
                                    <td class="p-2 border">${r.responseDate}</td>
                                    <td class="p-2 border">
                                        <div class="flex flex-col gap-1">
                                            <!-- Respond -->
                                            <button type="button"
                                                    class="bg-yellow-500 text-white px-2 py-1 rounded hover:bg-yellow-600"
                                                    onclick="toggleResponseForm('${r.reviewID}')">
                                                Respond
                                            </button>

                                            <!-- Delete -->
                                            <button type="button"
                                                    class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700"
                                                    onclick="confirmDeleteReview('${r.reviewID}')">
                                                Delete
                                            </button>

                                            <!-- Respond Form -->
                                            <form id="responseForm-${r.reviewID}" method="post"
                                                  action="admin-preview-management" class="hidden mt-2 space-y-2">
                                                <input type="hidden" name="villageID" value="${villageID}"/>
                                                <input type="hidden" name="villageName" value="${villageName}"/>
                                                <input type="hidden" name="typeName" value="respondReview"/>
                                                <input type="hidden" name="reviewID" value="${r.reviewID}"/>
                                                <textarea name="responseText" placeholder="Enter response..."
                                                          class="w-full border rounded p-2 text-sm"></textarea>
                                                <button type="submit"
                                                        class="bg-blue-600 text-white px-2 py-1 rounded hover:bg-blue-800">
                                                    Submit
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Hidden Delete Form -->
                <form id="deleteForm" method="post" action="admin-preview-management" style="display: none;">
                    <input type="hidden" name="villageID" value="${villageID}"/>
                    <input type="hidden" name="villageName" value="${villageName}"/>
                    <input type="hidden" name="typeName" value="deleteReview"/>
                    <input type="hidden" name="reviewID" id="deleteReviewID"/>
                </form>
            </div>
        </div>
    </body>
</html>
