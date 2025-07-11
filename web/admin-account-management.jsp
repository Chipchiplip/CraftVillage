<%-- 
    Document   : admin-account-management
    Created on : Jun 23, 2025, 12:35:10 PM
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
            function openModal(userID, userName, email, fullName, phoneNumber, address, roleID, status, createdDate, updatedDate) {
                document.getElementById('modal').classList.remove('hidden');
                document.getElementById('userID').value = userID;
                document.getElementById('userName').value = userName;
                document.getElementById('email').value = email;
                document.getElementById('fullName').value = fullName;
                document.getElementById('phone').value = phoneNumber;
                document.getElementById('address').value = address;
                document.getElementById('roleID').value = roleID;
                document.getElementById('status').value = status;
                document.getElementById('createdDate').value = createdDate;
                document.getElementById('updatedDate').value = updatedDate;
            }

            function closeModal() {
                document.getElementById('modal').classList.add('hidden');
            }

            function handleSubmitPassword() {
                const pwdInput = document.getElementById("password");
                if (!pwdInput.value) {
                    pwdInput.value = "********"; // Gán giá trị mặc định nếu để trống
                }
                return true; // Cho phép submit
            }

            function openAddAccountModal() {
                document.getElementById("addAccountModal").classList.remove("hidden");
            }

            function closeAddAccountModal() {
                document.getElementById("addAccountModal").classList.add("hidden");
            }

            const exportBtn = document.getElementById("exportBtn");
            const exportMenu = document.getElementById("exportMenu");

            exportBtn.addEventListener("click", function (e) {
                e.preventDefault();
                exportMenu.classList.toggle("hidden");
            });

            document.addEventListener("click", function (e) {
                if (!exportBtn.contains(e.target) && !exportMenu.contains(e.target)) {
                    exportMenu.classList.add("hidden");
                }
            });

            function toggleExportMenu() {
                const menu = document.getElementById("exportMenu");
                menu.classList.toggle("hidden");
            }
        </script>
    </head>
    <body class="bg-gray-100">
        <div class="flex min-h-screen">
            <!-- Sidebar content ở đây -->
            <jsp:include page="admin-sidebar.jsp"></jsp:include>

                <div class="flex-1 p-6">
                <c:if test="${not empty message}">
                    <div id="notification"
                         class="fixed top-5 right-5 z-50 px-4 py-3 rounded shadow-lg text-white transition-opacity duration-500
                         ${error == '1' ? 'bg-green-500' : 'bg-red-500'}">
                        ${message}
                    </div>

                    <script>
                        // Ẩn thông báo sau 4 giây bằng hiệu ứng mờ dần
                        setTimeout(() => {
                            const noti = document.getElementById("notification");
                            if (noti) {
                                noti.style.opacity = '0';
                                setTimeout(() => noti.remove(), 500); // Xoá khỏi DOM sau khi mờ
                            }
                        }, 4000);
                    </script>
                </c:if>    
                <!-- Search and Filter Bar -->
                <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <h1 class="text-2xl font-bold mb-6">Account List (${activeSessions}/${listAccount.size()})</h1>
                    <!-- Role Filter + Request Button -->
                    <div class="flex flex-col md:flex-row items-center gap-2">
                        <form action="admin-account-management" method="post">
                            <input type="hidden" id="typeName" name="typeName" value="searchAccount">
                            <select id="status" name="status" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="1">Activate</option>
                                <option value="0">Deactivate</option>                        
                            </select> 
                            <select id="searchID" name="searchID" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                <option value="0">All Accounts</option>
                                <option value="1">Customer</option>
                                <option value="2">Seller</option>
                                <option value="3">Admin</option>
                                <option value="4">Sort A - Z</option>
                                <option value="5">Sort Z - A</option>    
                                <option value="6">User Name</option>
                                <option value="7">Email</option>
                                <option value="8">Full Name</option>
                                <option value="9">Phone Number</option>
                                <option value="10">UserID</option>
                                <option value="11">New Account Register</option>
                            </select>   
                            <input type="text" id="contentSearch" name="contentSearch" placeholder="Search by Username or Email or Full"
                                   class="border border-gray-300 rounded px-3 py-2 text-sm w-64">
                            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                                Search
                            </button>
                        </form> 
                        <button onclick="openAddAccountModal()" 
                                class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                            Add
                        </button>

                        <div class="relative inline-block">
                            <button onclick="toggleExportMenu()" 
                                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Export
                            </button>
                            <div id="exportMenu" class="hidden absolute z-10 mt-2 w-40 bg-white border rounded shadow-lg">
                                <a href="export-account-pdf?cas=1" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">All Account</a>
                                <a href="export-account-pdf?cas=2" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Customer</a>
                                <a href="export-account-pdf?cas=3" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Seller</a>
                                <a href="export-account-pdf?cas=4" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Admin</a>
                                <a href="export-account-pdf?cas=5" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Deactivate Account</a>
                                <a href="export-account-pdf?cas=6" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Quantity By Role</a>
                                <a href="export-account-pdf?cas=7" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Registration in Year Summary Report</a>
                            </div>
                        </div>
                        <form id="upgradeForm" action="${pageContext.request.contextPath}/admin-process-request" method="get">
                            <button type="submit"
                                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Request Upgrade
                            </button>
                        </form>
                    </div>
                </div>
                <c:choose>
                    <c:when test="${not empty listAccount}">
                        <!-- Hiển thị thông tin account như card -->
                        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
                            <c:forEach var="acc" items="${listAccount}">
                                <div class="bg-white shadow-md rounded-xl p-4 border border-gray-200">
                                    <h2 class="text-lg font-semibold mb-1">${acc.fullName}</h2>
                                    <p class="text-gray-600"><strong>Username:</strong> ${acc.userName}</p>
                                    <p class="text-gray-600"><strong>Email:</strong> ${acc.email}</p>
                                    <p class="text-gray-600">
                                        <strong>Status:</strong> 
                                        <c:choose>
                                            <c:when test="${acc.status == 1}">Active</c:when>
                                            <c:otherwise>Inactive</c:otherwise>
                                        </c:choose>
                                    </p>
                                    <div class="mt-4 flex justify-between items-center">
                                        <button onclick="openModal(
                                                        '${acc.userID}',
                                                        '${acc.userName}',
                                                        '${acc.email}',
                                                        '${acc.fullName}',
                                                        '${acc.phoneNumber}',
                                                        '${acc.address}',
                                                        '${acc.roleID}',
                                                        '${acc.status}',
                                                        '${acc.createdDate}',
                                                        '${acc.updatedDate}'
                                                        )"
                                                class="text-white bg-blue-600 hover:bg-blue-700 px-4 py-1 rounded text-sm">
                                            View Profile
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                    </c:when>
                    <c:otherwise>
                        <div class="text-center text-gray-500 text-sm mt-10">
                            No account found.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <!-- Hidden Popup ------------------------------------------------------------->
        <!-- Modal Popup -->
        <div id="modal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-2xl relative">
                <h2 class="text-xl font-semibold mb-4">User Profile</h2>
                <form action="admin-account-management" method="post" onsubmit="return handleSubmitPassword();">
                    <!-- Hidden Fields -->
                    <input type="hidden" id="typeName" name="typeName" value="updateProfile">
                    <!-- Form Grid -->
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium mb-1">UserID</label>
                            <input type="text" id="userID" name="userID" class="w-full border rounded p-2  bg-gray-100" readonly>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Username</label>
                            <input type="text" id="userName" name="userName" class="w-full border rounded p-2  bg-gray-100" readonly>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Password</label>
                            <input type="password" id="password" name="password" class="w-full border rounded p-2" placeholder="********">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Email</label>
                            <input type="email" id="email" name="email" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Full Name</label>
                            <input type="text" id="fullName" name="fullName" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Phone Number</label>
                            <input type="text" id="phone" name="phoneNumber" class="w-full border rounded p-2" required pattern="^\d{10}$" title="Phone number must be exactly 10 digits">
                        </div>
                        <div class="col-span-2">
                            <label class="block text-sm font-medium mb-1">Address</label>
                            <input type="text" id="address" name="address" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Role</label>
                            <select id="roleID" name="roleID" class="w-full border rounded p-2">
                                <option value="1">Customer</option>
                                <option value="2">Seller</option>
                                <option value="3">Admin</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Status</label>
                            <select id="status" name="status" class="w-full border rounded p-2">
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Create Date</label>
                            <input type="text" id="createdDate" name="createdDate" class="w-full border rounded p-2  bg-gray-100" readonly>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Update Date</label>
                            <input type="text" id="updatedDate" name="updatedDate" class="w-full border rounded p-2  bg-gray-100" readonly>
                        </div>
                    </div>
                    <!-- Action Buttons -->
                    <div class="mt-6 flex justify-end">
                        <button type="button" onclick="window.location.href = 'admin-account-management';" class="mr-3 px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">
                            Cancel
                        </button>
                        <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                            Save Changes
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <!-- Modal Overlay -->
        <div id="addAccountModal" class="fixed inset-0 z-50 hidden bg-black bg-opacity-50 flex items-center justify-center">
            <!-- Modal Content -->
            <div class="bg-white rounded-lg shadow-lg w-full max-w-4xl p-6 overflow-y-auto max-h-[90vh]">
                <h2 class="text-xl font-semibold mb-4">Add New Account</h2>
                <form action="admin-account-management" method="post" onsubmit="return handleSubmitPassword();">
                    <input type="hidden" name="typeName" value="addAccount">
                    <div class="grid grid-cols-2 gap-4">
                        <div>
                            <label class="block text-sm font-medium mb-1">Username</label>
                            <input type="text" name="userName" class="w-full border rounded p-2" required>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Password</label>
                            <input type="password" name="password" class="w-full border rounded p-2" placeholder="********" required>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Email</label>
                            <input type="email" name="email" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Full Name</label>
                            <input type="text" name="fullName" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Phone Number</label>
                            <input type="text" name="phoneNumber" class="w-full border rounded p-2" required pattern="^\d{10}$" title="Phone number must be exactly 10 digits">
                        </div>
                        <div class="col-span-2">
                            <label class="block text-sm font-medium mb-1">Address</label>
                            <input type="text" name="address" class="w-full border rounded p-2">
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Role</label>
                            <select name="roleID" class="w-full border rounded p-2">
                                <option value="1">Customer</option>
                                <option value="2">Seller</option>
                                <option value="3">Admin</option>
                            </select>
                        </div>
                        <div>
                            <label class="block text-sm font-medium mb-1">Status</label>
                            <select name="status" class="w-full border rounded p-2">
                                <option value="1">Active</option>
                                <option value="0">Inactive</option>
                            </select>
                        </div>      
                    </div>
                    <!-- Action Buttons -->
                    <div class="mt-6 flex justify-end">
                        <button type="button" onclick="closeAddAccountModal()" class="mr-3 px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">
                            Cancel
                        </button>
                        <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                            Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
