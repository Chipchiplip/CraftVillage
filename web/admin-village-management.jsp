<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Craft Village Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <script src="https://unpkg.com/@popperjs/core@2"></script>
        <script src="https://unpkg.com/tippy.js@6"></script>
        <script>
            function confirmDelete(villageID) {
                if (confirm("Are you sure you want to delete this ticket?")) {
                    document.getElementById("deleteVillageID").value = villageID;
                    document.getElementById("deleteForm").submit();
                }
            }
            
            function toggleExportMenu() {
                const menu = document.getElementById('exportMenu');
                menu.classList.toggle('hidden');
            }
        </script>
    </head>
    <body class="bg-gray-100">
        <div class="flex min-h-screen">
            <jsp:include page="admin-sidebar.jsp"/>
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

                <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <h1 class="text-2xl font-bold mb-6">
                        Village List (${listAllVillage.size()})
                    </h1>

                    <div class="flex flex-col md:flex-row items-center gap-2">
                        <i id="tooltip-icon" class="bi bi-exclamation-circle-fill text-warning"></i>

                        <c:set var="tooltipContent" value=""/>
                        <c:forEach var="c" items="${listVillages}">
                            <c:set var="tooltipContent" value="${tooltipContent}${c.typeID} - ${c.typeName}<br/>"/>
                        </c:forEach>

                        <script>
                            var tooltipContent = `<c:out value="${tooltipContent}" escapeXml="false"/>`;
                            tippy('#tooltip-icon', {
                                content: tooltipContent,
                                allowHTML: true,
                                theme: 'light',
                            });
                        </script>

                        <form action="admin-village-management" method="post" class="flex flex-wrap gap-2 items-center">
                            <input type="hidden" name="typeName" value="searchVillage"/>

                            <select name="status"
                                    class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>

                            <select name="searchID"
                                    class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="0">All Village</option>
                                <option value="1">By Category</option>
                                <option value="2">Sort A - Z</option>
                                <option value="3">Sort Z - A</option>
                                <option value="4">Village Name</option>
                                <option value="5">Village ID</option>
                                <option value="6">Location</option>
                                <option value="7">New Village Post</option>
                            </select>

                            <input type="text" name="contentSearch" placeholder="Search by Name, ID"
                                   class="border border-gray-300 rounded px-3 py-2 text-sm w-64"/>

                            <button type="submit"
                                    class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Search
                            </button>
                        </form>

                        <div class="relative inline-block">
                            <button onclick="toggleExportMenu()"
                                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Export
                            </button>
                            <div id="exportMenu"
                                 class="hidden absolute z-10 mt-2 w-48 bg-white border rounded shadow-lg">
                                <a href="export-village-pdf?cas=1"
                                   class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">All Village</a>
                                <a href="export-village-pdf?cas=2"
                                   class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">By Category</a>
                                <a href="export-village-pdf?cas=3"
                                   class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Inactive Village</a>
                                <a href="export-village-pdf?cas=4"
                                   class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Top Rated Products</a>
                            </div>
                        </div>

                        <button onclick="openAddVillageModal()"
                                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                            Add Village
                        </button>
                    </div>
                </div>

                <!-- Village Table -->
                <table class="w-full table-auto border border-gray-300 text-sm">
                    <thead class="bg-gray-200 text-center">
                        <tr>
                            <th class="p-3 border">Village ID</th>
                            <th class="p-3 border">Image</th>
                            <th class="p-3 border">Type ID</th>
                            <th class="p-3 border">Village Name</th>
                            <th class="p-3 border">Description</th>
                            <th class="p-3 border">Address</th>
                            <th class="p-3 border">Status</th>
                            <th class="p-3 border">Created Date</th>
                            <th class="p-3 border">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${listAllVillage}">
                            <tr class="text-center border hover:bg-gray-50">
                                <td class="p-3 border">${v.villageID}</td>
                                <td class="p-3 border">
                                    <img src="${v.mainImageUrl}" alt="Image"
                                         class="w-16 h-16 object-cover mx-auto rounded"/>
                                </td>
                                <td class="p-3 border">${v.typeID}</td>
                                <td class="p-3 border">${v.villageName}</td>
                                <td class="p-3 border text-left max-w-xs truncate">${v.description}</td>
                                <td class="p-3 border">${v.address}</td>
                                <td class="p-3 border">
                                    <c:choose>
                                        <c:when test="${v.status == 1}">
                                            <span class="text-green-600 font-semibold">Active</span>
                                        </c:when>
                                        <c:when test="${v.status == 0}">
                                            <span class="text-gray-500">Hidden</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-yellow-600">Pending</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="p-3 border">${v.createdDate}</td>
                                <td class="p-3 border">
                                    <div class="flex gap-1 justify-center flex-wrap">
                                        <button onclick="editVillage(this)"
                                                class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600 text-sm"
                                                data-village-id="${v.villageID}"
                                                data-village-name="${fn:escapeXml(v.villageName)}"
                                                data-type-id="${v.typeID}"
                                                data-description="${fn:escapeXml(v.description)}"
                                                data-address="${fn:escapeXml(v.address)}"
                                                data-latitude="${v.latitude}"
                                                data-longitude="${v.longitude}"
                                                data-contact-phone="${v.contactPhone}"
                                                data-contact-email="${v.contactEmail}"
                                                data-status="${v.status}"
                                                data-click-count="${v.clickCount}"
                                                data-last-clicked="${v.lastClicked}"
                                                data-created-date="${v.createdDate}"
                                                data-updated-date="${v.updatedDate}"
                                                data-seller-id="${v.sellerId}"
                                                data-opening-hours="${fn:escapeXml(v.openingHours)}"
                                                data-closing-days="${fn:escapeXml(v.closingDays)}"
                                                data-average-rating="${v.averageRating}"
                                                data-total-reviews="${v.totalReviews}"
                                                data-map-embed-url="${fn:escapeXml(v.mapEmbedUrl)}"
                                                data-virtual-tour-url="${fn:escapeXml(v.virtualTourUrl)}"
                                                data-history="${fn:escapeXml(v.history)}"
                                                data-special-features="${fn:escapeXml(v.specialFeatures)}"
                                                data-famous-products="${fn:escapeXml(v.famousProducts)}"
                                                data-cultural-events="${fn:escapeXml(v.culturalEvents)}"
                                                data-craft-process="${fn:escapeXml(v.craftProcess)}"
                                                data-video-description-url="${fn:escapeXml(v.videoDescriptionUrl)}"
                                                data-travel-tips="${fn:escapeXml(v.travelTips)}"
                                                data-main-image-url="${fn:escapeXml(v.mainImageUrl)}">
                                            View
                                        </button>
                                        <a href="admin-vreview-management?villageID=${v.villageID}&villageName=${v.villageName}"
                                           class="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600 text-sm">Review</a>
                                        <!-- Delete -->
                                        <button class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-700 text-sm"
                                                onclick="confirmDelete('${v.villageID}')">
                                            Delete
                                        </button>

                                        <!-- Hidden form for delete -->
                                        <form id="deleteForm" method="post" action="<c:url value='/admin-village-management'/>" style="display:none;">
                                            <input type="hidden" name="typeName" value="deleteVillage">
                                            <input type="hidden" name="villageID" id="deleteVillageID">
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Modal Add Village -->
                <div id="villageAddModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-4xl relative text-black overflow-y-auto max-h-[90vh]">
                        <h2 class="text-xl font-semibold mb-4">Add Village</h2>
                        <form id="villageAddForm" action="admin-village-management" method="post" class="space-y-4">
                            <input type="hidden" name="typeName" value="addVillage">
                            <!-- fields giống edit nhưng không có ID -->
                            <div class="grid grid-cols-2 gap-4">
                                <div><label>Village Name</label><input type="text" name="villageName" class="w-full border p-2" required/></div>
                                <div>
                                    <label>Type Name</label>
                                    <select name="typeID" class="w-full border p-2" required>
                                        <c:forEach var="typeCC" items="${listVillages}">
                                            <option value="${typeCC.typeID}">${typeCC.typeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-span-2"><label>Description</label><textarea name="description" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Address</label><input type="text" name="address" class="w-full border p-2" required/></div>
                                <div><label>Latitude</label><input type="text" name="latitude" class="w-full border p-2"/></div>
                                <div><label>Longitude</label><input type="text" name="longitude" class="w-full border p-2"/></div>
                                <div><label>Phone</label><input type="text" name="contactPhone" class="w-full border p-2" pattern="^\d{10}$" title="Phone number must be exactly 10 digits"/></div>
                                <div><label>Email</label><input type="text" name="contactEmail" class="w-full border p-2"/></div>
                                <div><label>Status</label>
                                    <select name="status" class="w-full border p-2">
                                        <option value="1">Active</option>
                                        <option value="0">Hidden</option>
                                    </select>
                                </div>
                                <div><label>Seller ID</label><input type="text" name="sellerId" class="w-full border p-2" required/></div>
                                <div><label>Opening Hours</label><input type="text" name="openingHours" class="w-full border p-2"/></div>
                                <div><label>Closing Days</label><input type="text" name="closingDays" class="w-full border p-2"/></div>
                                <div class="col-span-2"><label>Map Embed URL</label><input type="text" name="mapEmbedUrl" class="w-full border p-2"/></div>
                                <div class="col-span-2"><label>Virtual Tour URL</label><input type="text" name="virtualTourUrl" class="w-full border p-2"/></div>
                                <div class="col-span-2"><label>History</label><textarea name="history" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Special Features</label><textarea name="specialFeatures" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Famous Products</label><textarea name="famousProducts" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Cultural Events</label><textarea name="culturalEvents" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Craft Process</label><textarea name="craftProcess" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Video Description URL</label><input type="text" name="videoDescriptionUrl" class="w-full border p-2"/></div>
                                <div class="col-span-2"><label>Travel Tips</label><textarea name="travelTips" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Main Image URL</label><input type="text" name="mainImageUrl" class="w-full border p-2"/></div>
                            </div>

                            <div class="flex justify-end gap-2">
                                <button type="button" onclick="closeAddVillageModal()" class="bg-gray-500 text-white px-4 py-2 rounded">Cancel</button>
                                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-800">Create</button>
                            </div>
                        </form>
                    </div>
                </div>
                <!-- Modal Edit Village -->
                <div id="villageModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-4xl relative text-black overflow-y-auto max-h-[90vh]">
                        <h2 class="text-xl font-semibold mb-4">Edit Village</h2>
                        <form id="villageForm" action="admin-village-management" method="post" class="space-y-4">
                            <input type="hidden" name="typeName" value="updateVillage">
                            <input type="hidden" name="villageID" id="villageID" />

                            <div class="grid grid-cols-2 gap-4">
                                <div><label>Village Name</label><input type="text" name="villageName" id="villageName" class="w-full border p-2" required /></div>
                                <div>
                                    <label>Type Name</label>
                                    <select name="typeID" class="w-full border p-2" id="typeID" required>
                                        <c:forEach var="typeCC" items="${listVillages}">
                                            <option value="${typeCC.typeID}">${typeCC.typeName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-span-2"><label>Description</label><textarea name="description" id="description" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Address</label><input type="text" name="address" id="address" class="w-full border p-2" required /></div>
                                <div><label>Latitude</label><input type="text" name="latitude" id="latitude" class="w-full border p-2" /></div>
                                <div><label>Longitude</label><input type="text" name="longitude" id="longitude" class="w-full border p-2" /></div>
                                <div><label>Phone</label><input type="text" name="contactPhone" id="contactPhone" class="w-full border p-2" pattern="^\d{10}$" title="Phone number must be exactly 10 digits"/></div>
                                <div><label>Email</label><input type="text" name="contactEmail" id="contactEmail" class="w-full border p-2" /></div>
                                <div><label>Status</label>
                                    <select name="status" id="status" class="w-full border p-2">
                                        <option value="1">Active</option>
                                        <option value="0">Hidden</option>
                                    </select>
                                </div>
                                <div><label>Click Count</label><input type="number" name="clickCount" id="clickCount" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div><label>Last Clicked</label><input type="text" name="lastClicked" id="lastClicked" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div><label>Created Date</label><input type="text" name="createdDate" id="createdDate" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div><label>Updated Date</label><input type="text" name="updatedDate" id="updatedDate" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div><label>Seller ID</label><input type="text" name="sellerId" id="sellerId" class="w-full border p-2" required /></div>
                                <div><label>Opening Hours</label><input type="text" name="openingHours" id="openingHours" class="w-full border p-2" /></div>
                                <div><label>Closing Days</label><input type="text" name="closingDays" id="closingDays" class="w-full border p-2" /></div>
                                <div><label>Average Rating</label><input type="text" name="averageRating" id="averageRating" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div><label>Total Reviews</label><input type="number" name="totalReviews" id="totalReviews" class="w-full border p-2 bg-gray-100" readonly /></div>
                                <div class="col-span-2"><label>Map Embed URL</label><input type="text" name="mapEmbedUrl" id="mapEmbedUrl" class="w-full border p-2" /></div>
                                <div class="col-span-2"><label>Virtual Tour URL</label><input type="text" name="virtualTourUrl" id="virtualTourUrl" class="w-full border p-2" /></div>
                                <div class="col-span-2"><label>History</label><textarea name="history" id="history" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Special Features</label><textarea name="specialFeatures" id="specialFeatures" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Famous Products</label><textarea name="famousProducts" id="famousProducts" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Cultural Events</label><textarea name="culturalEvents" id="culturalEvents" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Craft Process</label><textarea name="craftProcess" id="craftProcess" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Video Description URL</label><input type="text" name="videoDescriptionUrl" id="videoDescriptionUrl" class="w-full border p-2" /></div>
                                <div class="col-span-2"><label>Travel Tips</label><textarea name="travelTips" id="travelTips" class="w-full border p-2"></textarea></div>
                                <div class="col-span-2"><label>Main Image URL</label><input type="text" name="mainImageUrl" id="mainImageUrl" class="w-full border p-2" /></div>
                            </div>

                            <div class="flex justify-end gap-2">
                                <button type="button" onclick="closeVillageModal()" class="bg-gray-500 text-white px-4 py-2 rounded">Cancel</button>
                                <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-800">Save</button>
                            </div>
                        </form>
                    </div>
                </div>

            </div>
        </div>

        <script>
            function openAddVillageModal() {
                document.getElementById('villageAddForm').reset();
                document.getElementById('villageAddModal').classList.remove('hidden');
            }

            function closeAddVillageModal() {
                document.getElementById('villageAddModal').classList.add('hidden');
            }

            function closeVillageModal() {
                document.getElementById('villageModal').classList.add('hidden');
            }

            function editVillage(button) {
                document.getElementById('villageID').value = button.dataset.villageId || "";
                document.getElementById('villageName').value = button.dataset.villageName || "";
                document.getElementById('typeID').value = button.dataset.typeId || "";
                document.getElementById('description').value = button.dataset.description || "";
                document.getElementById('address').value = button.dataset.address || "";
                document.getElementById('latitude').value = button.dataset.latitude || "";
                document.getElementById('longitude').value = button.dataset.longitude || "";
                document.getElementById('contactPhone').value = button.dataset.contactPhone || "";
                document.getElementById('contactEmail').value = button.dataset.contactEmail || "";
                document.getElementById('status').value = button.dataset.status || "";
                document.getElementById('clickCount').value = button.dataset.clickCount || "";
                document.getElementById('lastClicked').value = button.dataset.lastClicked || "";
                document.getElementById('createdDate').value = button.dataset.createdDate || "";
                document.getElementById('updatedDate').value = button.dataset.updatedDate || "";
                document.getElementById('sellerId').value = button.dataset.sellerId || "";
                document.getElementById('openingHours').value = button.dataset.openingHours || "";
                document.getElementById('closingDays').value = button.dataset.closingDays || "";
                document.getElementById('averageRating').value = button.dataset.averageRating || "";
                document.getElementById('totalReviews').value = button.dataset.totalReviews || "";
                document.getElementById('mapEmbedUrl').value = button.dataset.mapEmbedUrl || "";
                document.getElementById('virtualTourUrl').value = button.dataset.virtualTourUrl || "";
                document.getElementById('history').value = button.dataset.history || "";
                document.getElementById('specialFeatures').value = button.dataset.specialFeatures || "";
                document.getElementById('famousProducts').value = button.dataset.famousProducts || "";
                document.getElementById('culturalEvents').value = button.dataset.culturalEvents || "";
                document.getElementById('craftProcess').value = button.dataset.craftProcess || "";
                document.getElementById('videoDescriptionUrl').value = button.dataset.videoDescriptionUrl || "";
                document.getElementById('travelTips').value = button.dataset.travelTips || "";
                document.getElementById('mainImageUrl').value = button.dataset.mainImageUrl || "";

                document.getElementById('villageModal').classList.remove('hidden');
            }
        </script>
    </body>
</html>
