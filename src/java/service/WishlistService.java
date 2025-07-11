package service;

import java.util.List;
import DAO.WishlistDAO;
import DAO.CartDAO;
import entity.CartWishList.Wishlist;
import entity.CartWishList.Cart;
import entity.CartWishList.CartItem;

public class WishlistService implements IWishlistService {
    private WishlistDAO wDAO = new WishlistDAO();
    private CartDAO cartDAO = new CartDAO();

    // ===== NEW METHODS FOR CONTROLLER LAYER =====
    
    @Override
    public List<WishlistDAO.WishlistWithProduct> getWishlistWithProductDetails(int userId) {
        try {
            if (userId <= 0) {
                return null;
            }
            return wDAO.getWishlistWithProductDetails(userId);
        } catch (Exception e) {
            System.err.println("Error getting wishlist with product details for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void debugWishlistData(int userId) {
        try {
            if (userId > 0) {
                wDAO.debugWishlistData(userId);
            }
        } catch (Exception e) {
            System.err.println("Error debugging wishlist data for user " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean addProductToWishlist(int userId, int productId) {
        try {
            if (userId <= 0 || productId <= 0) {
                return false;
            }
            
            // Check if product already in wishlist
            if (isInWishlist(userId, productId)) {
                System.out.println("Product " + productId + " already in wishlist for user " + userId);
                return false; // Already in wishlist
            }
            
            return wDAO.addToWishlist(userId, productId);
        } catch (Exception e) {
            System.err.println("Error adding product to wishlist: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean moveItemToCart(int userId, int productId, int wishlistId) {
        try {
            if (userId <= 0 || productId <= 0) {
                return false;
            }
            
            // Get or create user's cart
            Cart userCart = cartDAO.getCartByUser(userId);
            if (userCart == null) {
                userCart = new Cart();
                userCart.setUserID(userId);
                int cartID = cartDAO.createCart(userCart);
                userCart.setCartID(cartID);
            }
            
            // Add product to cart
            CartItem cartItem = new CartItem();
            cartItem.setCartID(userCart.getCartID());
            cartItem.setProductID(productId);
            cartItem.setQuantity(1);
            
            boolean addedToCart = cartDAO.addCartItem(cartItem);
            
            if (addedToCart) {
                // Remove from wishlist after successful add to cart
                if (wishlistId > 0) {
                    deleteWishlist(wishlistId);
                } else {
                    // If wishlistId not provided, remove by userId and productId
                    removeFromWishlist(userId, productId);
                }
                return true;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Error moving item to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean moveAllItemsToCart(int userId) {
        try {
            if (userId <= 0) {
                return false;
            }
            
            List<WishlistDAO.WishlistWithProduct> wishlistItems = 
                getWishlistWithProductDetails(userId);
            
            if (wishlistItems == null || wishlistItems.isEmpty()) {
                System.out.println("Wishlist is empty for user " + userId);
                return false;
            }
            
            // ✅ REFACTORED: Use CartService instead of direct CartDAO access
            service.ICartService cartService = new service.CartService();
            
            int movedCount = 0;
            for (WishlistDAO.WishlistWithProduct item : wishlistItems) {
                // Add to cart using CartService
                boolean addedToCart = cartService.addProductToCart(userId, item.getProductID(), 1);
                
                if (addedToCart) {
                    // Remove from wishlist after successful add to cart
                    deleteWishlist(item.getWishlistID());
                    movedCount++;
                }
            }
            
            System.out.println("Moved " + movedCount + " items from wishlist to cart for user " + userId);
            return movedCount > 0;
        } catch (Exception e) {
            System.err.println("Error moving all items to cart: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ===== EXISTING METHODS =====

    @Override
    public List<Wishlist> getAllWishlists() {
        try {
            return wDAO.getAllWishlists();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Wishlist getWishlistById(int id) {
        try {
            return wDAO.getWishlistById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addWishlist(Wishlist wishlist) {
        try {
            if (wishlist == null || wishlist.getUserID() <= 0 || wishlist.getProductID() <= 0) {
                return false;
            }
            return wDAO.addWishlist(wishlist);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateWishlist(Wishlist wishlist) {
        try {
            if (wishlist == null || wishlist.getWishlistID() <= 0) {
                return false;
            }
            return wDAO.updateWishlist(wishlist);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteWishlist(int id) {
        try {
            if (id <= 0) {
                return false;
            }
            return wDAO.removeFromWishlist(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Wishlist> getWishlistsByUserId(int userId) {
        try {
            if (userId <= 0) {
                return null;
            }
            return wDAO.getWishListByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getWishListCount(int userID) {
        try {
            if (userID <= 0) {
                return 0;
            }
            return wDAO.getWishlistCount(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // Additional helper methods
    @Override
    public boolean addToWishlist(int userId, int productId) {
        // ✅ REFACTORED: Delegate to main implementation to avoid code duplication
        return addProductToWishlist(userId, productId);
    }
    
    @Override
    public boolean removeFromWishlist(int userId, int productId) {
        try {
            List<Wishlist> userWishlist = getWishlistsByUserId(userId);
            if (userWishlist != null) {
                for (Wishlist w : userWishlist) {
                    if (w.getProductID() == productId) {
                        return deleteWishlist(w.getWishlistID());
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isInWishlist(int userId, int productId) {
        try {
            List<Wishlist> userWishlist = getWishlistsByUserId(userId);
            if (userWishlist != null) {
                for (Wishlist w : userWishlist) {
                    if (w.getProductID() == productId) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 