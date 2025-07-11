<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>❤️ Danh sách yêu thích - CraftVillage</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/biolife.css">
    
    <style>
        .wishlist-item {
            transition: all 0.3s ease;
            border-radius: 12px;
            overflow: hidden;
        }
        .wishlist-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
        }
        .product-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
        .price-tag {
            font-size: 1.2rem;
            font-weight: bold;
            color: #28a745;
        }
        .btn-cart {
            background: linear-gradient(135deg, #28a745, #20c997);
            border: none;
            color: white;
            transition: all 0.3s ease;
        }
        .btn-cart:hover {
            background: linear-gradient(135deg, #20c997, #28a745);
            transform: translateY(-2px);
            color: white;
        }
        .btn-remove {
            background: linear-gradient(135deg, #dc3545, #c82333);
            border: none;
            color: white;
        }
        .btn-remove:hover {
            background: linear-gradient(135deg, #c82333, #dc3545);
            color: white;
        }
        .empty-wishlist {
            min-height: 400px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
        }
        .stats-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px;
            padding: 1.5rem;
        }
        .breadcrumb {
            background: rgba(255,255,255,0.1);
            backdrop-filter: blur(10px);
            border-radius: 10px;
        }
        .wishlist-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .alert {
            border-radius: 10px;
            border: none;
        }
        .btn-action {
            border-radius: 8px;
            padding: 0.5rem 1rem;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="wishlist-header">
        <div class="container">
            <!-- Breadcrumb -->
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-3">
                    <li class="breadcrumb-item">
                        <a href="home" class="text-white-50">
                            <i class="fas fa-home"></i> Trang chủ
                        </a>
                    </li>
                    <li class="breadcrumb-item active text-white">
                        <i class="fas fa-heart"></i> Danh sách yêu thích
                    </li>
                </ol>
            </nav>
            
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="mb-2">
                        <i class="fas fa-heart text-danger"></i> Danh sách yêu thích của bạn
                    </h1>
                    <p class="mb-0 text-white-75">Quản lý các sản phẩm yêu thích và chuyển vào giỏ hàng</p>
                </div>
                <div class="col-md-4 text-end">
                    <div class="stats-card">
                        <h3 class="mb-1">${wishlistWithProducts != null ? wishlistWithProducts.size() : 0}</h3>
                        <small>Sản phẩm yêu thích</small>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <!-- Messages -->
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <strong>Lỗi!</strong> ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <strong>Thành công!</strong> ${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Action Buttons -->
        <div class="row mb-4">
            <div class="col-md-6">
                <a href="home" class="btn btn-outline-secondary btn-action">
                    <i class="fas fa-arrow-left"></i> Trở về trang chủ
                </a>
            </div>
            <div class="col-md-6 text-end">
                <c:if test="${wishlistWithProducts != null && wishlistWithProducts.size() > 0}">
                    <form action="wishlist" method="post" class="d-inline">
                        <input type="hidden" name="action" value="moveAllToCart">
                        <input type="hidden" name="userID" value="${userID}">
                        <button type="submit" class="btn btn-cart btn-action" 
                                onclick="return confirm('Bạn có muốn chuyển tất cả sản phẩm vào giỏ hàng?')">
                            <i class="fas fa-cart-plus"></i> Chuyển tất cả vào giỏ hàng
                        </button>
                    </form>
                </c:if>
            </div>
        </div>

        <!-- Wishlist Content -->
        <div class="row">
            <c:choose>
                <c:when test="${wishlistWithProducts != null && wishlistWithProducts.size() > 0}">
                    <c:forEach var="item" items="${wishlistWithProducts}" varStatus="status">
                        <div class="col-lg-6 col-xl-4 mb-4">
                            <div class="card wishlist-item h-100 border-0 shadow-sm">
                                <div class="card-body">
                                    <div class="row g-3">
                                        <div class="col-4">
                                            <img src="${pageContext.request.contextPath}/${item.productImage != null ? item.productImage : 'assets/images/products/default.jpg'}" 
                                                 alt="${item.productName}" 
                                                 class="product-image">
                                        </div>
                                        <div class="col-8">
                                            <h6 class="card-title mb-2">
                                                <a href="detail?pid=${item.productID}" 
                                                   class="text-decoration-none text-dark">
                                                    ${item.productName}
                                                </a>
                                            </h6>
                                            <div class="price-tag mb-2">
                                                <fmt:formatNumber value="${item.productPrice}" type="currency" 
                                                                currencyCode="VND" pattern="#,##0 ₫"/>
                                            </div>
                                            <small class="text-muted">
                                                <i class="fas fa-calendar-plus"></i>
                                                <fmt:formatDate value="${item.addedDate}" pattern="dd/MM/yyyy"/>
                                            </small>
                                        </div>
                                    </div>
                                    
                                    <div class="mt-3 d-flex gap-2">
                                        <!-- Add to Cart Button -->
                                        <form action="wishlist" method="post" class="flex-fill">
                                            <input type="hidden" name="action" value="addToCart">
                                            <input type="hidden" name="productID" value="${item.productID}">
                                            <input type="hidden" name="wishlistID" value="${item.wishlistID}">
                                            <input type="hidden" name="userID" value="${userID}">
                                            <button type="submit" class="btn btn-cart btn-sm w-100">
                                                <i class="fas fa-cart-plus"></i> Thêm vào giỏ
                                            </button>
                                        </form>
                                        
                                        <!-- Remove Button -->
                                        <form action="wishlist" method="post">
                                            <input type="hidden" name="action" value="remove">
                                            <input type="hidden" name="wishlistID" value="${item.wishlistID}">
                                            <input type="hidden" name="userID" value="${userID}">
                                            <button type="submit" class="btn btn-remove btn-sm" 
                                                    onclick="return confirm('Bạn có muốn xóa sản phẩm này khỏi danh sách yêu thích?')"
                                                    title="Xóa khỏi danh sách yêu thích">
                                                <i class="fas fa-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="col-12">
                        <div class="empty-wishlist">
                            <div class="text-center">
                                <i class="fas fa-heart-broken fa-5x text-muted mb-4"></i>
                                <h3 class="text-muted mb-3">Danh sách yêu thích trống</h3>
                                <p class="text-muted mb-4">
                                    Bạn chưa có sản phẩm nào trong danh sách yêu thích.<br>
                                    Hãy khám phá và thêm những sản phẩm bạn yêu thích!
                                </p>
                                <div class="d-flex gap-3 justify-content-center">
                                    <a href="home" class="btn btn-primary btn-lg">
                                        <i class="fas fa-shopping-bag"></i> Khám phá sản phẩm
                                    </a>
                                    <a href="product" class="btn btn-outline-primary btn-lg">
                                        <i class="fas fa-list"></i> Xem tất cả sản phẩm
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Additional Info -->
        <c:if test="${wishlistWithProducts != null && wishlistWithProducts.size() > 0}">
            <div class="row mt-5">
                <div class="col-12">
                    <div class="card border-0 bg-light">
                        <div class="card-body text-center">
                            <h5 class="card-title">
                                <i class="fas fa-info-circle text-primary"></i> Thông tin hữu ích
                            </h5>
                            <div class="row">
                                <div class="col-md-4">
                                    <i class="fas fa-heart text-danger fa-2x mb-2"></i>
                                    <h6>Lưu yêu thích</h6>
                                    <small class="text-muted">Lưu những sản phẩm bạn quan tâm để xem sau</small>
                                </div>
                                <div class="col-md-4">
                                    <i class="fas fa-cart-plus text-success fa-2x mb-2"></i>
                                    <h6>Chuyển vào giỏ</h6>
                                    <small class="text-muted">Dễ dàng thêm sản phẩm từ wishlist vào giỏ hàng</small>
                                </div>
                                <div class="col-md-4">
                                    <i class="fas fa-share-alt text-info fa-2x mb-2"></i>
                                    <h6>Chia sẻ</h6>
                                    <small class="text-muted">Chia sẻ danh sách yêu thích với bạn bè</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
    </div>

    <!-- Footer spacing -->
    <div style="height: 60px;"></div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                });
            }, 5000);
        });

        // Smooth animations
        document.addEventListener('DOMContentLoaded', function() {
            const cards = document.querySelectorAll('.wishlist-item');
            cards.forEach((card, index) => {
                card.style.animationDelay = `${index * 0.1}s`;
                card.classList.add('animate__animated', 'animate__fadeInUp');
            });
        });

        // Confirmation for bulk actions
        function confirmMoveAll() {
            const count = <c:out value="${wishlistWithProducts != null ? wishlistWithProducts.size() : 0}"/>;
            return confirm('Bạn có muốn chuyển tất cả ' + count + ' sản phẩm vào giỏ hàng không?');
        }
    </script>
</body>
</html>
