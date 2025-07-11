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

        <style>
            select, input, textarea {
                color: #000 !important;
                background-color: #fff !important;
                font-size: 1rem;
            }

            /* Style select giống Tailwind ví dụ */
            select {
                border: 1px solid #d1d5db; /* border-gray-300 */
                border-radius: 0.25rem;     /* rounded */
                padding: 0.5rem 0.75rem;    /* py-2 px-3 */
                font-size: 0.875rem;        /* text-sm */
                width: 10rem;               /* w-40 */
                background-color: #fff;
            }

            label {
                font-size: 1rem;
            }

            #notification {
                position: fixed;
                top: 20px;
                right: 20px;
                color: #fff;
                padding: 12px 20px;
                border-radius: 5px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.3);
                opacity: 1;
                transition: opacity 0.5s ease;
                max-width: 300px;
                z-index: 9999;
                font-family: sans-serif;
            }

            #notification.success {
                background-color: #28a745;
            }

            #notification.error {
                background-color: #dc3545;
            }
        </style>

        <script>
            $(document).ready(function () {
                $("#businessType").change(function () {
                    var selected = $(this).val();

                    if (selected === "Individual") {
                        $("#personalFields").show();
                        $("#companyFields").hide();

                        $("#idCardNumber").prop("required", true);
                        $("#idCardFrontUrl").prop("required", true);
                        $("#idCardBackUrl").prop("required", true);

                        $("#businessLicense").prop("required", false);
                        $("#taxCode").prop("required", false);
                        $("#documentUrl").prop("required", false);
                        $("#businessLicense").val("");
                        $("#taxCode").val("");
                        $("#documentUrl").val("");

                    } else if (selected === "Craft Village") {
                        $("#personalFields").hide();
                        $("#companyFields").show();

                        $("#idCardNumber").prop("required", false);
                        $("#idCardFrontUrl").prop("required", false);
                        $("#idCardBackUrl").prop("required", false);
                        $("#idCardNumber").val("");
                        $("#idCardFrontUrl").val("");
                        $("#idCardBackUrl").val("");

                        $("#businessLicense").prop("required", true);
                        $("#taxCode").prop("required", true);
                        $("#documentUrl").prop("required", true);

                    } else {
                        $("#personalFields").hide();
                        $("#companyFields").hide();

                        $("#idCardNumber").prop("required", false);
                        $("#idCardFrontUrl").prop("required", false);
                        $("#idCardBackUrl").prop("required", false);
                        $("#businessLicense").prop("required", false);
                        $("#taxCode").prop("required", false);
                        $("#documentUrl").prop("required", false);

                        $("#idCardNumber").val("");
                        $("#idCardFrontUrl").val("");
                        $("#idCardBackUrl").val("");
                        $("#businessLicense").val("");
                        $("#taxCode").val("");
                        $("#documentUrl").val("");
                    }
                });

                $('input[name="categoryOption"]').on('change', function () {
                    if ($('#selectExisting').is(':checked')) {
                        $('#existingCategoryDiv').show();
                        $('#businessVillageCategrySelect').prop('required', true);
                        $('#newCategoryDiv').hide();
                        $('#businessVillageCategry').prop('required', false).val("");
                    } else if ($('#enterNew').is(':checked')) {
                        $('#existingCategoryDiv').hide();
                        $('#businessVillageCategrySelect').prop('required', false).val("");
                        $('#newCategoryDiv').show();
                        $('#businessVillageCategry').prop('required', true);
                    }
                });

                $('input[name="productCategoryOption"]').on('change', function () {
                    if ($('#selectExistingProduct').is(':checked')) {
                        $('#existingProductCategoryDiv').show();
                        $('#productProductCategorySelect').prop('required', true);
                        $('#newProductCategoryDiv').hide();
                        $('#productProductCategory').prop('required', false).val("");
                    } else if ($('#enterNewProduct').is(':checked')) {
                        $('#existingProductCategoryDiv').hide();
                        $('#productProductCategorySelect').prop('required', false).val("");
                        $('#newProductCategoryDiv').show();
                        $('#productProductCategory').prop('required', true);
                    }
                });
            });
        </script>
    </head>
    <body class="biolife-body">

        <jsp:include page="Menu.jsp" />

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
        <!--Navigation section-->
        <div class="container">
            <nav class="biolife-nav">
                <ul>
                    <li class="nav-item"><a href="home" class="permal-link">Home</a></li>
                    <li class="nav-item"><a href="userprofile" class="permal-link">Profile</a></li>
                    <li class="nav-item"><span class="current-page">Upgrade Account</span></li>
                </ul>
            </nav>
        </div>
        <div class="container mt-4">
            <div class="card">
                <div class="card-header text-center">
                    <h2 class="text-3xl font-bold">Register Seller Account</h2>
                    <p class="mb-4 mt-2 text-base">Complete the form below to become a seller.</p>
                </div>


                <div class="card-body">
                    <form action="request-upgrade" method="post">
                        <input type="hidden" name="userID" value="${sessionScope.acc.userID}"/>

                        <div class="mb-3">
                            <label for="businessType" class="block mb-2">Business Type</label>
                            <select id="businessType" name="businessType">
                                <option value="">-- Select business type --</option>
                                <option value="Individual">Individual</option>
                                <option value="Craft Village">Craft Village</option>
                            </select>
                        </div>

                        <fieldset class="border p-3 mb-3">
                            <label class="block mb-2">Village Category / Industry</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="categoryOption" id="selectExisting" value="select" checked>
                                <label class="form-check-label" for="selectExisting">
                                    Select from existing categories
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="categoryOption" id="enterNew" value="new">
                                <label class="form-check-label" for="enterNew">
                                    Enter a new category
                                </label>
                            </div>

                            <div id="existingCategoryDiv" class="mt-3">
                                <label for="businessVillageCategrySelect" class="block mb-2">Existing Categories</label>
                                <select id="businessVillageCategrySelect" name="businessVillageCategrySelect">
                                    <option value="">-- Select category --</option>
                                    <c:forEach var="c" items="${listVillages}">
                                        <option value="${c.typeID}">${c.typeName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div id="newCategoryDiv" class="mt-3" style="display: none;">
                                <label for="businessVillageCategry" class="block mb-2">New Category</label>
                                <input type="text" id="businessVillageCategry" name="businessVillageCategry" class="form-control" placeholder="Type new category here"/>
                            </div>
                        </fieldset>

                        <div class="mb-3">
                            <label for="businessVillageName" class="block mb-2">Name of Individual / Organization / Village</label>
                            <input type="text" id="businessVillageName" name="businessVillageName" class="form-control" required/>
                        </div>

                        <div class="mb-3">
                            <label for="businessVillageAddress" class="block mb-2">Business Address</label>
                            <input type="text" id="businessVillageAddress" name="businessVillageAddress" class="form-control" required/>
                        </div>

                        <fieldset class="border p-3 mb-3">
                            <label class="block mb-2">Product Category</label>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="productCategoryOption" id="selectExistingProduct" value="select" checked>
                                <label class="form-check-label" for="selectExistingProduct">
                                    Select from existing categories
                                </label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input" type="radio" name="productCategoryOption" id="enterNewProduct" value="new">
                                <label class="form-check-label" for="enterNewProduct">
                                    Enter a new category
                                </label>
                            </div>

                            <div id="existingProductCategoryDiv" class="mt-3">
                                <label for="productProductCategorySelect" class="block mb-2">Existing Categories</label>
                                <select id="productProductCategorySelect" name="productProductCategorySelect">
                                    <option value="">-- Select category --</option>
                                    <c:forEach var="c" items="${listCC}">
                                        <option value="${c.cid}">${c.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div id="newProductCategoryDiv" class="mt-3" style="display: none;">
                                <label for="productProductCategory" class="block mb-2">New Product Category</label>
                                <input type="text" id="productProductCategory" name="productProductCategory" class="form-control" placeholder="Type new product category here"/>
                            </div>
                        </fieldset>

                        <div class="mb-3">
                            <label for="profileVillagePictureUrl" class="block mb-2">Profile Picture / Logo</label>
                            <input type="text" id="profileVillagePictureUrl" name="profileVillagePictureUrl" class="form-control" required/>
                        </div>

                        <div class="mb-3">
                            <label for="contactPerson" class="block mb-2">Contact Person</label>
                            <input type="text" id="contactPerson" name="contactPerson" class="form-control" required/>
                        </div>

                        <div class="mb-3">
                            <label for="contactPhone" class="block mb-2">Contact Phone</label>
                            <input type="text" id="contactPhone" name="contactPhone" class="form-control" required pattern="^\d{10}$" title="Phone number must be exactly 10 digits"/>
                        </div>

                        <div class="mb-3">
                            <label for="contactEmail" class="block mb-2">Contact Email</label>
                            <input type="email" id="contactEmail" name="contactEmail" class="form-control" required/>
                        </div>

                        <div id="personalFields" class="mt-4" style="display: none;">
                            <h5>Personal Information (for Individuals)</h5>
                            <div class="mb-3">
                                <label for="idCardNumber" class="block mb-2">ID Card Number</label>
                                <input type="text" id="idCardNumber" name="idCardNumber" class="form-control"/>
                            </div>
                            <div class="mb-3">
                                <label for="idCardFrontUrl" class="block mb-2">ID Card Front Photo</label>
                                <input type="text" id="idCardFrontUrl" name="idCardFrontUrl" class="form-control"/>
                            </div>
                            <div class="mb-3">
                                <label for="idCardBackUrl" class="block mb-2">ID Card Back Photo</label>
                                <input type="text" id="idCardBackUrl" name="idCardBackUrl" class="form-control"/>
                            </div>
                        </div>

                        <div id="companyFields" class="mt-4" style="display: none;">
                            <h5>Company Information</h5>
                            <div class="mb-3">
                                <label for="businessLicense" class="block mb-2">Business License Number</label>
                                <input type="text" id="businessLicense" name="businessLicense" class="form-control"/>
                            </div>
                            <div class="mb-3">
                                <label for="taxCode" class="block mb-2">Tax Code</label>
                                <input type="text" id="taxCode" name="taxCode" class="form-control"/>
                            </div>
                            <div class="mb-3">
                                <label for="documentUrl" class="block mb-2">Business License Document</label>
                                <input type="text" id="documentUrl" name="documentUrl" class="form-control"/>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="note" class="block mb-2">Note (optional)</label>
                            <textarea id="note" name="note" class="form-control"></textarea>
                        </div>

                        <div class="text-end">
                            <button type="submit" class="btn btn-success">
                                Submit Seller Registration
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <jsp:include page="Footer.jsp" />

    </body>
</html>
