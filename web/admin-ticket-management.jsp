<%-- 
    Document   : admin-ticket-management
    Created on : Jul 7, 2025, 9:03:52 PM
    Author     : ACER
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Ticket Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>.hidden {
                display: none;
            }</style>

        <script>
            function showTicketDetail(ticketID, villageID, typeID, price, status, createdDate, updatedDate) {
                const form = document.getElementById('detailForm');
                form.ticketID.value = ticketID;
                form.villageID.value = villageID;
                form.typeID.value = typeID;
                form.price.value = price;
                form.status.value = status;
                form.createdDate.value = createdDate;
                form.updatedDate.value = updatedDate;
                document.getElementById('modal').classList.remove('hidden');
            }

            function openAddTicketModal() {
                document.getElementById("ticketModal").classList.remove("hidden");
                document.getElementById("addTicketForm").reset();
            }

            function closeModal() {
                document.getElementById("modal").classList.add("hidden");
                document.getElementById("ticketModal").classList.add("hidden");
            }

            function confirmDelete(ticketID) {
                if (confirm("Are you sure you want to delete this ticket?")) {
                    document.getElementById("deleteTicketID").value = ticketID;
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
                <!-- Tabs -->
                <div class="mb-4 flex gap-4">
                    <button  class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">                       
                        <a href="admin-product-management"> Products Management</a>
                    </button>
                    <button  class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                        <a href="admin-ticket-management">  Tickets Management</a>
                    </button>
                </div>


                <div class="mb-6 flex flex-col md:flex-row md:items-center gap-4">
                    <h1 class="text-2xl font-bold mb-6">Ticket List</h1>

                    <div class="flex flex-wrap gap-2 ml-auto">
                        <form action="admin-ticket-management" method="post" class="flex flex-wrap gap-2 items-center">
                            <input type="hidden" name="typeName" value="searchTicket"/>
                            <select name="status" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="2">All Status</option>
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>
                            <select name="villageID" class="border border-gray-300 rounded px-3 py-2 text-sm w-40" id="villageID">
                                <option value="0">All Ticket</option>
                                <c:forEach var="village" items="${listAllVillage}">
                                    <option value="${village.villageID}">${village.villageName}</option>
                                </c:forEach>
                                
                            </select>
                            <button type="submit" class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
                                Search
                            </button>
                        </form>

                        <div class="relative inline-block">
                            <button onclick="toggleExportMenu()"
                                    class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 text-sm">
                                Export
                            </button>
                            <div id="exportMenu" class="hidden absolute z-10 mt-2 w-48 bg-white border rounded shadow-lg">
                                <a href="export-ticket-pdf?cas=1" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">All Ticket</a>
                                <a href="export-ticket-pdf?cas=2" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">By Ticket Type</a>
                                <a href="export-ticket-pdf?cas=3" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Inactive Ticket</a>
                            </div>
                        </div>

                        <button onclick="openAddTicketModal()"
                                class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 text-sm">
                            Add Ticket
                        </button>
                    </div>
                </div>

                <!-- Ticket Table -->
                <table class="w-full table-auto border border-gray-300 text-sm">
                    <thead class="bg-gray-200 text-center">
                        <tr>
                            <th class="p-2 border">Ticket ID</th>
                            <th class="p-2 border">Village Name</th>
                            <th class="p-2 border">Type Ticket</th>
                            <th class="p-2 border">Price (d)</th>
                            <th class="p-2 border">Status</th>
                            <th class="p-2 border">Created Date</th>
                            <th class="p-2 border">Updated Date</th>
                            <th class="p-2 border">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ticket" items="${listTicket}">
                            <tr class="bg-white border text-center">
                                <td class="p-2 border">${ticket.ticketID}</td>
                                <td class="p-2 border">${ticket.villageID}</td>
                                <td class="p-2 border">${ticket.typeID}</td>
                                <td class="p-2 border">${ticket.price}</td>
                                <td class="p-2 border">
                                    <c:choose>
                                        <c:when test="${ticket.status == 1}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="p-2 border">${ticket.createdDate}</td>
                                <td class="p-2 border">${ticket.updatedDate}</td>
                                <td class="p-2 border">
                                    <button class="bg-blue-500 text-white px-2 py-1 rounded hover:bg-blue-700"
                                            onclick="showTicketDetail(
                                                            '${ticket.ticketID}',
                                                            '${ticket.villageID}',
                                                            '${ticket.typeID}',
                                                            '${ticket.price}',
                                                            '${ticket.status}',
                                                            '${ticket.createdDate}',
                                                            '${ticket.updatedDate}')">
                                        View Details
                                    </button>
                                    <button class="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-700"
                                            onclick="confirmDelete('${ticket.ticketID}')">
                                        Delete
                                    </button>
                                    <form id="deleteForm" method="post" action="admin-ticket-management" style="display:none;">
                                        <input type="hidden" name="typeName" value="deleteTicket">
                                        <input type="hidden" name="ticketID" id="deleteTicketID">
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Modal: Ticket Details -->
        <div id="modal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-xl relative text-black">
                <h2 class="text-xl font-semibold mb-4">Ticket Details</h2>
                <form id="detailForm" action="admin-ticket-management" method="post" class="space-y-4">
                    <input type="hidden" name="typeName" value="updateTicket"/>
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label>Ticket ID</label>
                            <input type="text" name="ticketID" class="w-full border p-2 bg-gray-100" readonly/>
                        </div>
                        <div>
                            <label>Village</label>
                            <select name="villageID" class="w-full border p-2" id="villageID"  disabled>
                                <c:forEach var="village" items="${listAllVillage}">
                                    <option value="${village.villageID}">${village.villageName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label>Type</label>
                            <select name="typeID" class="w-full border p-2" id="typeID"  disabled >
                                <c:forEach var="type" items="${listTicketType}">
                                    <option value="${type.typeID}">${type.typeName}</option>
                                </c:forEach>
                            </select>
                        </div>  
                        <div>
                            <label>Price (d)</label>
                            <input type="number" name="price" class="w-full border p-2" step="0.01" min="0" required/>
                        </div>
                        <div>
                            <label>Status</label>
                            <select name="status" class="w-full border p-2" required>
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </div>
                        <div>
                            <label>Created Date</label>
                            <input type="text" name="createdDate" class="w-full border p-2 bg-gray-100" readonly/>
                        </div>
                        <div>
                            <label>Updated Date</label>
                            <input type="text" name="updatedDate" class="w-full border p-2 bg-gray-100" readonly/>
                        </div>
                    </div>
                    <div class="flex justify-end gap-2 pt-4">
                        <button type="button" onclick="closeModal()"
                                class="bg-gray-500 text-white px-4 py-2 rounded">Close
                        </button>
                        <button type="submit"
                                class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Save Changes
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Modal: Add Ticket -->
        <div id="ticketModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" onclick="closeModal()">
            <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-xl relative text-black" onclick="event.stopPropagation()">
                <h2 class="text-xl font-semibold mb-4">Add New Ticket</h2>
                <form id="addTicketForm" action="admin-ticket-management" method="post" class="space-y-4">
                    <input type="hidden" name="typeName" value="createTicket"/>
                    <div class="grid grid-cols-2 gap-4">            
                        <div>
                            <label>Village</label>
                            <select name="villageID" class="w-full border p-2" id="villageID" required>
                                <c:forEach var="village" items="${listAllVillage}">
                                    <option value="${village.villageID}">${village.villageName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label>Type</label>
                            <select name="typeID" class="w-full border p-2" id="typeID" required>
                                <c:forEach var="type" items="${listTicketType}">
                                    <option value="${type.typeID}">${type.typeName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label>Price (đ)</label>
                            <input type="number" name="price" class="w-full border p-2" step="0.01" min="0" required/>
                        </div>
                        <div>
                            <label>Status</label>
                            <select name="status" class="w-full border p-2" required>
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="flex justify-end gap-2 pt-4">
                        <button type="button" onclick="closeModal()"
                                class="bg-gray-500 text-white px-4 py-2 rounded">Close
                        </button>
                        <button type="submit"
                                class="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700">Create
                        </button>
                    </div>
                </form>
            </div>
        </div>

    </body>
</html>
