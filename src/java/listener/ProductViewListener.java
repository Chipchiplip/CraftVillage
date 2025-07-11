//package listener;
//
//import context.DBContext;
//import DAO.DAO;
//import entity.Account.Account;
//import jakarta.servlet.ServletRequestEvent;
//import jakarta.servlet.ServletRequestListener;
//import jakarta.servlet.annotation.WebListener;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.Comparator;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@WebListener
//public class ProductViewListener implements ServletRequestListener {
//    private static final String PRODUCT_VIEWS_KEY = "productViewCounts";
//    private static final String LAST_VIEWED_PRODUCT = "lastViewedProductId";
//    private static final Logger LOGGER = Logger.getLogger(ProductViewListener.class.getName());
//    private DAO dao;
//
//    public ProductViewListener() {
//        this.dao = new DAO();
//    }
//
//    @Override
//    public void requestInitialized(ServletRequestEvent sre) {
//        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
//        String requestURI = request.getRequestURI();
//        String contextPath = request.getContextPath();
//        String action = request.getParameter("action");
//
//        if (isProductViewRequest(requestURI, contextPath, action, request)) {
//            handleProductView(request);
//        }
//    }
//
//    private boolean isProductViewRequest(String requestURI, String contextPath, String action, HttpServletRequest request) {
//        // Original condition
//        boolean isOriginalPattern = requestURI.endsWith(contextPath + "/products") && "viewProduct".equals(action);
//        
//        // Check for detail page patterns - improved detection
//        boolean isDetailPage = requestURI.contains("/detail") || 
//                              requestURI.endsWith("/Detail.jsp") ||
//                              (request.getParameter("pid") != null);
//        
//        // Add debugging to track request patterns
//        if (isOriginalPattern || isDetailPage) {
//            LOGGER.log(Level.INFO, "Detected product view: URI={0}, pid={1}", 
//                new Object[]{requestURI, request.getParameter("pid")});
//        }
//        
//        return isOriginalPattern || isDetailPage;
//    }
//
//    private void handleProductView(HttpServletRequest request) {
//        try {
//            // Check for both 'pid' (used in Detail.jsp) and 'id' parameters
//            String productIdParam = request.getParameter("pid");
//            
//            // If 'pid' is null, check for 'id' which might be used in other views
//            if (productIdParam == null) {
//                productIdParam = request.getParameter("id");
//            }
//            
//            if (productIdParam == null) {
//                LOGGER.log(Level.WARNING, "No product ID found in request parameters");
//                return;
//            }
//            
//            int productId = Integer.parseInt(productIdParam);
//            HttpSession session = request.getSession();
//            Integer lastViewed = (Integer) session.getAttribute(LAST_VIEWED_PRODUCT);
//
//            // Get user ID if user is logged in
//            Integer userId = null;
//            Account user = (Account) session.getAttribute("user");
//            if (user != null) {
//                userId = user.getUserID();
//            }
//
//            // Only record a new view if this is a different product or first view
//            if (lastViewed == null || lastViewed != productId) {
//                session.setAttribute(LAST_VIEWED_PRODUCT, productId);
//                
//                // Record click in database using DAO
//                try {
//                    DAO dao = new DAO();
//                    dao.incrementProductClickCount(productId);
//                    LOGGER.log(Level.INFO, "Recorded click for product {0}", productId);
//                } catch (Exception e) {
//                    LOGGER.log(Level.SEVERE, "Failed to record product click: {0}", e.getMessage());
//                }
//            }
//        } catch (NumberFormatException e) {
//            LOGGER.log(Level.WARNING, "Invalid product ID format", e);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error in handleProductView", e);
//        }
//    }
//
//    @Override
//    public void requestDestroyed(ServletRequestEvent sre) {
//        // No processing needed
//    }
//
//    private Map<Integer, Integer> getViewCountsFromContext(HttpServletRequest request) {
//        return (Map<Integer, Integer>) request.getServletContext().getAttribute(PRODUCT_VIEWS_KEY);
//    }
//
//    public static Map<Integer, Integer> getProductViewCounts(HttpServletRequest request) {
//        return (Map<Integer, Integer>) request.getServletContext().getAttribute(PRODUCT_VIEWS_KEY);
//    }
//
//    public static Map<Integer, Integer> getSortedProductViewCounts(HttpServletRequest request) {
//        Map<Integer, Integer> viewCounts = getProductViewCounts(request);
//        
//        if (viewCounts == null || viewCounts.isEmpty()) {
//            return new TreeMap<>();
//        }
//
//        TreeMap<Integer, Integer> sortedMap = new TreeMap<>(new Comparator<Integer>() {
//            @Override
//            public int compare(Integer key1, Integer key2) {
//                Integer view1 = viewCounts.getOrDefault(key1, 0);
//                Integer view2 = viewCounts.getOrDefault(key2, 0);
//                int valueCompare = view2.compareTo(view1);
//                return valueCompare != 0 ? valueCompare : key1.compareTo(key2);
//            }
//        });
//
//        sortedMap.putAll(viewCounts);
//        return sortedMap;
//    }
//}