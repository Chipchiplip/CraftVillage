<%-- 
    Document   : admin-process-request
    Created on : Jul 6, 2025, 1:30:22 AM
    Author     : ACER
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Account Management</title>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
        <script>
            function toggleExportMenu() {
                const menu = document.getElementById('exportMenu');
                menu.classList.toggle('hidden');
            }

            function openPopup(id) {
                document.getElementById("popup-" + id).classList.remove("hidden");
            }
            function closePopup(id) {
                document.getElementById("popup-" + id).classList.add("hidden");
            }

            function toggleRejectReason(id) {
                let status = document.getElementById("statusSelect-" + id).value;
                const reasonDiv = document.getElementById("rejectReasonDiv-" + id);
                if (status == 2) {
                    reasonDiv.classList.remove("hidden");
                } else {
                    reasonDiv.classList.add("hidden");
                }
            }
        </script>
    </head>
    <body class="bg-gray-100">
        <div class="flex min-h-screen">

            <!-- Sidebar -->
            <jsp:include page="admin-sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="flex-1 p-6">

                    <!-- Notification -->
                <c:if test="${not empty message}">
                    <div id="notification"
                         class="fixed top-5 right-5 z-50 px-4 py-3 rounded shadow-lg text-white transition-opacity duration-500
                         ${error == '1' ? 'bg-green-500' : 'bg-red-500'}">
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

                <!-- Search & Export -->
                <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <h1 class="text-2xl font-bold mb-6">Request List</h1>
                    <div class="flex flex-col md:flex-row items-center gap-2">
                        <form action="admin-process-request" method="post">
                            <input type="hidden" name="typeName" id="typeName" value="searchRequest" >
                            <select id="verificationStatus" name="verificationStatus" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="3">All Request</option>
                                <option value="0">Processing</option>
                                <option value="1">Approved</option>
                                <option value="2">Rejected</option>
                            </select>
                            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Search
                            </button>
                        </form>

                        <div class="relative inline-block">
                            <button onclick="toggleExportMenu()"
                                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Export
                            </button>
                            <div id="exportMenu" class="hidden absolute z-10 mt-2 w-40 bg-white border rounded shadow-lg">
                                <a href="export-request-pdf?cas=3" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">All Request</a>
                                <a href="export-request-pdf?cas=0" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Processing</a>
                                <a href="export-request-pdf?cas=1" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Approved</a>
                                <a href="export-request-pdf?cas=2" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Rejected</a>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- List -->
                <c:if test="${not empty listRequest}">
                    <div class="overflow-x-auto bg-white rounded shadow">
                        <table class="min-w-full text-sm text-left">
                            <thead class="bg-gray-200 text-gray-600 uppercase text-xs">
                                <tr>
                                    <th class="px-4 py-3">Verification ID</th>
                                    <th class="px-4 py-3">Seller ID</th>
                                    <th class="px-4 py-3">Business Type</th>
                                    <th class="px-4 py-3">Contact Person</th>
                                    <th class="px-4 py-3">Contact Phone</th>
                                    <th class="px-4 py-3">Contact Email</th>
                                    <th class="px-4 py-3">Status</th>
                                    <th class="px-4 py-3 text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-gray-200">
                                <c:forEach var="item" items="${listRequest}">
                                    <tr class="hover:bg-gray-50">
                                        <td class="px-4 py-3">${item.verificationID}</td>
                                        <td class="px-4 py-3">${item.sellerID}</td>
                                        <td class="px-4 py-3">${item.businessType}</td>
                                        <td class="px-4 py-3">${item.contactPerson}</td>
                                        <td class="px-4 py-3">${item.contactPhone}</td>
                                        <td class="px-4 py-3">${item.contactEmail}</td>
                                        <td class="px-4 py-3">
                                            <c:choose>
                                                <c:when test="${item.verificationStatus == 0}">
                                                    <span class="inline-block px-2 py-1 text-xs text-yellow-800 bg-yellow-100 rounded">Processing</span>
                                                </c:when>
                                                <c:when test="${item.verificationStatus == 1}">
                                                    <span class="inline-block px-2 py-1 text-xs text-green-800 bg-green-100 rounded">Approved</span>
                                                </c:when>
                                                <c:when test="${item.verificationStatus == 2}">
                                                    <span class="inline-block px-2 py-1 text-xs text-red-800 bg-red-100 rounded">Rejected</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="inline-block px-2 py-1 text-xs text-gray-800 bg-gray-100 rounded">Unknown</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-4 py-3 text-center">
                                            <button onclick="openPopup(${item.verificationID})"
                                                    class="inline-block px-3 py-1 text-white bg-blue-600 rounded hover:bg-blue-700 text-xs">
                                                View Details
                                            </button>
                                        </td>
                                    </tr>

                                    <!-- POPUP -->
                                    <tr>
                                        <td colspan="8">
                                            <div id="popup-${item.verificationID}"
                                                 class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center hidden z-50">
                                                <div class="bg-white w-full max-w-3xl p-6 rounded shadow-lg overflow-y-auto max-h-[90vh]">
                                                    <h2 class="text-xl font-bold mb-4">Request Details - ID ${item.verificationID}</h2>

                                                    <form action="admin-process-request?adminID=${sessionScope.acc.userID}" method="post">
                                                        <!-- Hidden inputs to send ALL fields -->
                                                        <input type="hidden" name="typeName" value="processRequest" />
                                                        <input type="hidden" name="verificationID" value="${item.verificationID}" />
                                                        <input type="hidden" name="sellerID" value="${item.sellerID}" />
                                                        <input type="hidden" name="businessType" value="${item.businessType}" />
                                                        <input type="hidden" name="businessVillageCategrySelect" value="${item.businessVillageCategrySelect}" />
                                                        <input type="hidden" name="businessVillageCategry" value="${item.businessVillageCategry}" />
                                                        <input type="hidden" name="businessVillageName" value="${item.businessVillageName}" />
                                                        <input type="hidden" name="businessVillageAddress" value="${item.businessVillageAddress}" />
                                                        <input type="hidden" name="productProductCategory" value="${item.productProductCategory}" />
                                                        <input type="hidden" name="productProductCategorySelect" value="${item.productProductCategorySelect}" />
                                                        <input type="hidden" name="profileVillagePictureUrl" value="${item.profileVillagePictureUrl}" />
                                                        <input type="hidden" name="contactPerson" value="${item.contactPerson}" />
                                                        <input type="hidden" name="contactPhone" value="${item.contactPhone}" />
                                                        <input type="hidden" name="contactEmail" value="${item.contactEmail}" />
                                                        <input type="hidden" name="idCardNumber" value="${item.idCardNumber}" />
                                                        <input type="hidden" name="idCardFrontUrl" value="${item.idCardFrontUrl}" />
                                                        <input type="hidden" name="idCardBackUrl" value="${item.idCardBackUrl}" />
                                                        <input type="hidden" name="businessLicense" value="${item.businessLicense}" />
                                                        <input type="hidden" name="taxCode" value="${item.taxCode}" />
                                                        <input type="hidden" name="documentUrl" value="${item.documentUrl}" />
                                                        <input type="hidden" name="note" value="${item.note}" />
                                                        <input type="hidden" name="verifiedBy" value="${item.verifiedBy}" />
                                                        <input type="hidden" name="verifiedDate" value="${item.verifiedDate}" />
                                                        <input type="hidden" name="createdDate" value="${item.createdDate}" />

                                                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                                                            <!-- Chỉ hiển thị dữ liệu ra màn hình -->
                                                            <p><strong>Verification ID:</strong> ${item.verificationID}</p>
                                                            <p><strong>Seller ID:</strong> ${item.sellerID}</p>
                                                            <p><strong>Business Type:</strong> ${item.businessType}</p>
                                                            <p><strong>Business Village Category:</strong> ${item.businessVillageCategry}</p>
                                                            <p><strong>Business Village Category Select:</strong> ${item.businessVillageCategrySelect}</p>
                                                            <p><strong>Business Village Name:</strong> ${item.businessVillageName}</p>
                                                            <p><strong>Business Village Address:</strong> ${item.businessVillageAddress}</p>
                                                            <p><strong>Product Product Category:</strong> ${item.productProductCategory}</p>
                                                            <p><strong>Product Category Select:</strong> ${item.productProductCategorySelect}</p>
                                                            <p><strong>Profile Village Picture URL:</strong>
                                                                <a href="${item.profileVillagePictureUrl}" target="_blank" class="text-blue-600 underline">View Image</a>
                                                            </p>
                                                            <p><strong>Contact Person:</strong> ${item.contactPerson}</p>
                                                            <p><strong>Contact Phone:</strong> ${item.contactPhone}</p>
                                                            <p><strong>Contact Email:</strong> ${item.contactEmail}</p>
                                                            <p><strong>ID Card Number:</strong> ${item.idCardNumber}</p>
                                                            <p><strong>ID Card Front URL:</strong>
                                                                <a href="${item.idCardFrontUrl}" target="_blank" class="text-blue-600 underline">View Image</a>
                                                            </p>
                                                            <p><strong>ID Card Back URL:</strong>
                                                                <a href="${item.idCardBackUrl}" target="_blank" class="text-blue-600 underline">View Image</a>
                                                            </p>
                                                            <p><strong>Business License:</strong> ${item.businessLicense}</p>
                                                            <p><strong>Tax Code:</strong> ${item.taxCode}</p>
                                                            <p><strong>Document URL:</strong>
                                                                <a href="${item.documentUrl}" target="_blank" class="text-blue-600 underline">View Document</a>
                                                            </p>
                                                            <p><strong>Note:</strong> ${item.note}</p>
                                                            <p><strong>Created Date:</strong> ${item.createdDate}</p>
                                                            <p><strong>Verified Date:</strong> 
                                                                <c:if test="${item.verificationStatus != 0}">
                                                                    ${item.verifiedDate}
                                                                </c:if>
                                                            </p>
                                                            <p><strong>Verified By:</strong>
                                                                <c:choose>
                                                                    <c:when test="${item.verificationStatus == 0}">
                                                                        -
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${item.verifiedBy}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </p>
                                                        </div>

                                                        <div class="mt-4">
                                                            <c:choose>
                                                                <c:when test="${item.verificationStatus == 0}">
                                                                    <label class="block text-sm font-medium mb-1">Verification Status:</label>
                                                                    <select name="verificationStatus" id="statusSelect-${item.verificationID}"
                                                                            class="w-full border border-gray-300 rounded px-3 py-2 text-sm"
                                                                            onchange="toggleRejectReason(${item.verificationID})">
                                                                        <option value="0" selected>Processing</option>
                                                                        <option value="1">Approved</option>
                                                                        <option value="2">Rejected</option>
                                                                    </select>

                                                                    <div id="rejectReasonDiv-${item.verificationID}" class="mt-4 hidden">
                                                                        <label class="block text-sm font-medium mb-1">Reject Reason:</label>
                                                                        <textarea name="rejectReason" rows="3" class="w-full border border-gray-300 rounded px-3 py-2 text-sm">${item.rejectReason}</textarea>
                                                                    </div>

                                                                    <div class="mt-6 flex justify-end gap-3">
                                                                        <button type="button" onclick="closePopup(${item.verificationID})"
                                                                                class="px-4 py-2 bg-gray-400 text-white rounded hover:bg-gray-500">
                                                                            Cancel
                                                                        </button>
                                                                        <button type="submit"
                                                                                class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-green-700">
                                                                            Save
                                                                        </button>
                                                                    </div>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <p class="mt-4"><strong>Verification Status:</strong>
                                                                        <c:choose>
                                                                            <c:when test="${item.verificationStatus == 1}">Approved</c:when>
                                                                            <c:when test="${item.verificationStatus == 2}">Rejected</c:when>
                                                                            <c:otherwise>Unknown</c:otherwise>
                                                                        </c:choose>
                                                                    </p>
                                                                    <c:if test="${item.verificationStatus == 2}">
                                                                        <p class="mt-2"><strong>Reject Reason:</strong> ${item.rejectReason}</p>
                                                                    </c:if>

                                                                    <div class="mt-6 flex justify-end">
                                                                        <button type="button" onclick="closePopup(${item.verificationID})"
                                                                                class="px-4 py-2 bg-gray-400 text-white rounded hover:bg-gray-500">
                                                                            Close
                                                                        </button>
                                                                    </div>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </form>                                    
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>

                <c:if test="${empty listRequest}">
                    <p class="text-gray-500">No requests found.</p>
                </c:if>
            </div>
        </div>
    </body>
</html>