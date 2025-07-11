<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Admin - Product & Ticket Management</title>
        <script src="https://cdn.tailwindcss.com"></script>
        <style>.hidden {
                display: none;
            }</style>

        <script>
            function showSection(sectionId) {
                document.getElementById('productSection').classList.add('hidden');
                document.getElementById('ticketSection').classList.add('hidden');
                document.getElementById(sectionId).classList.remove('hidden');
            }

            function showProductDetail(pid, name, price, description, stock, status, villageID, categoryID, craftTypeID,
                    mainImageUrl, clickCount, createdDate, updatedDate,
                    sku, weight, dimensions, materials, careInstructions,
                    warranty, averageRating, totalReviews) {
                const form = document.getElementById('detailForm');
                form.querySelector('[name="pid"]').value = pid;
                form.name.value = name;
                form.price.value = price;
                form.description.value = description;
                form.stock.value = stock;
                form.status.value = status;
                form.villageID.value = villageID;
                form.categoryID.value = categoryID;
                form.craftTypeID.value = craftTypeID;
                form.mainImageUrl.value = mainImageUrl;
                form.clickCount.value = clickCount;
                form.createdDate.value = createdDate;
                form.updatedDate.value = updatedDate;
                form.sku.value = sku;
                form.weight.value = weight;
                form.dimensions.value = dimensions;
                form.materials.value = materials;
                form.careInstructions.value = careInstructions;
                form.warranty.value = warranty;
                form.averageRating.value = averageRating;
                form.totalReviews.value = totalReviews;
                document.getElementById('modal').classList.remove('hidden');
            }

            function toggleExportMenu() {
                const menu = document.getElementById('exportMenu');
                menu.classList.toggle('hidden');
            }

            function openAddProductModal() {
                const modal = document.getElementById("productModal");
                if (modal) {
                    modal.classList.remove("hidden");
                }
                const form = document.getElementById("addProductForm");
                if (form) {
                    form.reset();
                }
            }

            function closeModal() {
                const modal = document.getElementById("productModal");
                if (modal) {
                    modal.classList.add("hidden");
                }
                document.getElementById('modal').classList.add('hidden');
            }
            function confirmDelete(pid) {
                if (confirm("Are you sure you want to delete this product?")) {
                    document.getElementById("deletePid").value = pid;
                    document.getElementById("deleteForm").submit();
                }
            }

        </script>
        <!-- Bootstrap Icons (nếu muốn dấu chấm than đẹp) -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

        <!-- Tippy.js CSS -->
        <link href="https://unpkg.com/tippy.js@6/themes/light.css" rel="stylesheet" />

        <style>
            /* Bạn có thể chỉnh kích thước icon nếu muốn */
            #tooltip-icon {
                cursor: pointer;
                font-size: 24px;
            }
            table tr:hover {
                background-color: #f9fafb;
            }
            .table-button {
                display: block;
                width: 100%;
                text-align: center;
                margin-bottom: 4px;
            }
        </style>
    </head>
    <body class="bg-gray-100">
        <div class="flex min-h-screen">
            <jsp:include page="admin-sidebar.jsp" />
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

                <!-- Product Section -->
                <div id="productSection">
                    <div class="mb-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                        <h1 class="text-2xl font-bold mb-6">Product List (${listProduct.size()})</h1>

                        <div class="flex flex-col md:flex-row items-center gap-2">
                            <!-- Dấu chấm than -->
                            <i id="tooltip-icon" class="bi bi-exclamation-circle-fill text-warning"></i>

                            <!-- Chuỗi tooltip (ẩn) -->
                            <c:set var="tooltipContent" value="" />

                            <c:forEach var="c" items="${listCC}">
                                <c:set var="tooltipContent" value="${tooltipContent}${c.categoryID} - ${c.categoryName}<br/>" />
                            </c:forEach>

                            <!-- Popper.js & Tippy.js -->
                            <script src="https://unpkg.com/@popperjs/core@2"></script>
                            <script src="https://unpkg.com/tippy.js@6"></script>

                            <script>
                        // Lấy nội dung tooltip từ JSP biến tooltipContent
                        var tooltipContent = `<c:out value="${tooltipContent}" escapeXml="false"/>`;

                        tippy('#tooltip-icon', {
                            content: tooltipContent,
                            allowHTML: true,
                            theme: 'light',
                        });
                            </script>
                            <form action="admin-product-management" method="post" class="flex flex-wrap gap-2 items-center">
                                <input type="hidden" name="typeName" value="searchProduct" />

                                <select name="status" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                    <option value="1">Active</option>
                                    <option value="0">Inactive</option>
                                </select>

                                <select name="searchID" class="border border-gray-300 rounded px-3 py-2 text-sm w-40">
                                    <option value="0">All Products</option>
                                    <option value="1">By Category</option>
                                    <option value="2">Sort A - Z</option>
                                    <option value="3">Sort Z - A</option>
                                    <option value="4">Product Name</option>
                                    <option value="5">Product ID</option>
                                    <option value="6">Price</option>
                                    <option value="7">New Product Post</option>
                                </select>

                                <input type="text" name="contentSearch" placeholder="Search by Name, ID, Price"
                                       class="border border-gray-300 rounded px-3 py-2 text-sm w-64" />

                                <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
                                    Search
                                </button>
                            </form>

                            <div class="relative inline-block">
                                <button onclick="toggleExportMenu()"
                                        class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                    Export
                                </button>
                                <div id="exportMenu" class="hidden absolute z-10 mt-2 w-48 bg-white border rounded shadow-lg">
                                    <a href="export-product-pdf?cas=1" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">All Products</a>
                                    <a href="export-product-pdf?cas=2" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">By Category</a>
                                    <a href="export-product-pdf?cas=3" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Out of Stock</a>
                                    <a href="export-product-pdf?cas=4" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Inactive Products</a>
                                    <a href="export-product-pdf?cas=5" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">Top Rated Products</a>                                 
                                </div>
                            </div>

                            <button onclick="openAddProductModal()"
                                    class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm">
                                Add Product
                            </button>
                        </div>
                    </div>

                    <!-- Product Table -->
                    <table class="w-full table-auto border border-gray-300 text-sm">
                        <thead class="bg-gray-200 text-center">
                            <tr>
                                <th class="p-3 border w-20">Product ID</th>
                                <th class="p-3 border w-24">Image</th>
                                <th class="p-3 border w-48">Name</th>
                                <th class="p-3 border w-32">Price</th>
                                <th class="p-3 border w-24">Stock</th>
                                <th class="p-3 border w-56">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="product" items="${listProduct}">
                                <tr class="bg-white border text-center align-middle hover:bg-gray-50">
                                    <td class="p-3 border">${product.pid}</td>
                                    <td class="p-3 border">
                                        <img src="${product.mainImageUrl}" alt="Image" class="w-20 h-20 object-cover mx-auto rounded" />
                                    </td>
                                    <td class="p-3 border">${product.name}</td>
                                    <td class="p-3 border">${product.price}</td>
                                    <td class="p-3 border">${product.stock}</td>
                                    <td class="p-3 border">
                                        <div class="flex flex-col md:flex-row md:justify-center gap-2">
                                            <!-- View Details -->
                                            <button class="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-700 text-sm"
                                                    onclick="showProductDetail(
                                                                    '${product.pid}',
                                                                    '${fn:escapeXml(product.name)}',
                                                                    '${product.price}',
                                                                    '${empty product.description ? '' : fn:escapeXml(product.description)}',
                                                                    '${product.stock}',
                                                                    '${product.status}',
                                                                    '${product.villageID}',
                                                                    '${product.categoryID}',
                                                                    '${product.craftTypeID}',
                                                                    '${product.mainImageUrl}',
                                                                    '${product.clickCount}',
                                                                    '${product.createdDate}',
                                                                    '${product.updatedDate}',
                                                                    '${empty product.sku ? '' : product.sku}',
                                                                    '${empty product.weight ? '' : product.weight}',
                                                                    '${empty product.dimensions ? '' : product.dimensions}',
                                                                    '${empty product.materials ? '' : product.materials}',
                                                                    '${empty product.careInstructions ? '' : product.careInstructions}',
                                                                    '${empty product.warranty ? '' : product.warranty}',
                                                                    '${empty product.averageRating ? '' : product.averageRating}',
                                                                    '${empty product.totalReviews ? '' : product.totalReviews}'
                                                                    )">
                                                View
                                            </button>

                                            <!-- Review -->
                                            <form method="get" action="<c:url value='/admin-preview-management'/>">
                                                <input type="hidden" name="pid" value="${product.pid}" />
                                                <input type="hidden" name="name" value="${product.name}" />
                                                <button type="submit"
                                                        class="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600 text-sm">
                                                    Review
                                                </button>
                                            </form>

                                            <!-- Delete -->
                                            <button class="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-700 text-sm"
                                                    onclick="confirmDelete('${product.pid}')">
                                                Delete
                                            </button>
                                        </div>

                                        <!-- Hidden form for delete -->
                                        <form id="deleteForm" method="post" action="<c:url value='/admin-product-management'/>" style="display:none;">
                                            <input type="hidden" name="typeName" value="deleteProduct">
                                            <input type="hidden" name="pid" id="deletePid">
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Ticket Section -->
                <div id="ticketSection" class="hidden">
                    <h2 class="text-xl font-bold mb-4">Ticket List</h2>
                    <p class="text-gray-600">Danh sách vé sẽ được hiển thị ở đây...</p>
                </div>
            </div>

            <!-- Modal: Product Details -->
            <div id="modal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-3xl relative text-black overflow-y-auto max-h-[90vh]">
                    <h2 class="text-xl font-semibold mb-4">Product Details</h2>
                    <form id="detailForm" action="admin-product-management" method="post" class="space-y-4">
                        <input type="hidden" name="typeName" value="updateProduct" />
                        <div class="grid grid-cols-2 gap-4">
                            <div><label>Product ID</label><input type="text" name="pid" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>Name</label><input type="text" name="name" class="w-full border p-2" required /></div>
                            <div><label>Price(đ)</label><input type="number" name="price" class="w-full border p-2" min="0.01" step="0.01" required /></div>
                            <div class="col-span-2"><label>Description</label><textarea name="description" class="w-full border p-2"></textarea></div>
                            <div><label>Stock(unit)</label><input type="number" name="stock" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>Add Stock(unit)</label><input type="number" name="stockAdd" class="w-full border p-2" min="0" value="0" /></div>
                            <div><label>Status</label>
                                <select id="status" name="status" class="w-full border rounded p-2" required>
                                    <option value="1">Active</option>
                                    <option value="0">Inactive</option>
                                </select>
                            </div>
                            <div>
                                <label>Village</label>
                                <select name="villageID" class="w-full border p-2" id="villageID" required>
                                    <c:forEach var="type" items="${listAllVillage}">
                                        <option value="${type.villageID}">${type.villageName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label>Category</label>
                                <select name="categoryID" class="w-full border p-2" id="categoryID" required>
                                    <c:forEach var="typeC" items="${listCC}">
                                        <option value="${typeC.categoryID}">${typeC.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label>Craft Type</label>
                                <select name="craftTypeID" class="w-full border p-2" id="craftTypeID" required>
                                    <c:forEach var="typeCC" items="${listVillages}">
                                        <option value="${typeCC.typeID}">${typeCC.typeName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div><label>Main Image URL</label><input type="text" name="mainImageUrl" class="w-full border p-2" /></div>
                            <div><label>Click Count</label><input type="text" name="clickCount" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>Created Date</label><input type="text" name="createdDate" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>Updated Date</label><input type="text" name="updatedDate" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>SKU</label><input type="text" name="sku" class="w-full border p-2" /></div>
                            <div><label>Weight(kg)</label><input type="text" name="weight" class="w-full border p-2" required /></div>
                            <div><label>Dimensions</label><input type="text" name="dimensions" class="w-full border p-2" /></div>
                            <div><label>Materials</label><input type="text" name="materials" class="w-full border p-2" /></div>
                            <div><label>Care Instructions</label><input type="text" name="careInstructions" class="w-full border p-2" /></div>
                            <div><label>Warranty</label><input type="text" name="warranty" class="w-full border p-2" /></div>
                            <div><label>Average Rating</label><input type="number" name="averageRating" class="w-full border p-2 bg-gray-100" readonly /></div>
                            <div><label>Total Reviews</label><input type="number" name="totalReviews" class="w-full border p-2 bg-gray-100" readonly /></div>
                        </div>
                        <div class="flex justify-end gap-2 pt-4">
                            <button type="button" onclick="closeModal()" class="bg-gray-500 text-white px-4 py-2 rounded">Close</button>
                            <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-800">Save Changes</button>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Modal: Add Product -->
            <div id="productModal" class="hidden fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" onclick="closeModal()">
                <div class="bg-white p-6 rounded-xl shadow-xl w-full max-w-3xl relative text-black overflow-y-auto max-h-[90vh]" onclick="event.stopPropagation()">
                    <h2 class="text-xl font-semibold mb-4">Add New Product</h2>
                    <form id="addProductForm" action="admin-product-management" method="post" class="space-y-4">
                        <input type="hidden" name="typeName" value="createProduct" />
                        <div class="grid grid-cols-2 gap-4">
                            <div><label>Name</label><input type="text" name="name" class="w-full border p-2" required=""/></div>
                            <div><label>Price</label><input type="number"  name="price"  class="w-full border p-2"  required step="0.01"  min="0.01" class="w-full border p-2" required="" /></div>
                            <div class="col-span-2"><label>Description</label><textarea name="description" class="w-full border p-2"></textarea></div>
                            <div><label>Stock(unit)</label><input type="number" name="stock" class="w-full border p-2" min="0" required=""/></div>
                            <div><label>SKU</label><input type="text" name="sku" class="w-full border p-2" /></div>
                            <div><label>Status</label>
                                <select id="status" name="status" class="w-full border rounded p-2">
                                    <option value="1">Active</option>
                                    <option value="0">Inactive</option>
                                </select>
                            </div>
                            <div>
                                <label>Village</label>
                                <select name="villageID" class="w-full border p-2" id="villageID">
                                    <c:forEach var="type" items="${listAllVillage}">
                                        <option value="${type.villageID}">${type.villageName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label>Category</label>
                                <select name="categoryID" class="w-full border p-2" id="categoryID">
                                    <c:forEach var="typeC" items="${listCC}">
                                        <option value="${typeC.categoryID}">${typeC.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <label>Craft Type</label>
                                <select name="craftTypeID" class="w-full border p-2" id="craftTypeID">
                                    <c:forEach var="typeCC" items="${listVillages}">
                                        <option value="${typeCC.typeID}">${typeCC.typeName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div><label>Main Image Url</label><input type="text" name="mainImageUrl" class="w-full border p-2" /></div>
                            <div><label>Weight(kg)</label><input type="text" name="weight" class="w-full border p-2" required=""/></div>
                            <div><label>Dimensions</label><input type="text" name="dimensions" class="w-full border p-2" /></div>
                            <div><label>Materials</label><input type="text" name="materials" class="w-full border p-2" /></div>
                            <div><label>Care Instructions</label><input type="text" name="careInstructions" class="w-full border p-2" /></div>
                            <div><label>Warranty</label><input type="text" name="warranty" class="w-full border p-2" /></div>
                        </div>
                        <div class="flex justify-end gap-2 pt-4">
                            <button type="button" onclick="closeModal()" class="bg-gray-500 text-white px-4 py-2 rounded">Close</button>
                            <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-800">Create</button>
                        </div>
                    </form>
                </div>
            </div>

        </div>
    </body>
</html>
