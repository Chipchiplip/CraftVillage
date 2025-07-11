package controller.CraftVillage;

import entity.Account.Account;
import entity.CraftVillage.CraftReview;
import entity.CraftVillage.CraftVillage;
import entity.Product.Product;
import entity.Ticket.Ticket;
import entity.Ticket.TicketType;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import service.AccountService;
import service.ProductService;
import service.ReviewService;
import service.TicketService;
import service.VillageService;

@WebServlet(name = "CraftVillageDetails", urlPatterns = {"/village"})
public class CraftVillageDetails extends HttpServlet {

    VillageService vService = new VillageService();
    ProductService pService = new ProductService();
    TicketService tService = new TicketService();
    ReviewService rService = new ReviewService();
    AccountService aService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String villageID = request.getParameter("villageID");
        String userID = request.getParameter("userID");
        String rating = request.getParameter("rating");
        String reviewText = request.getParameter("reviewText");

        boolean result = rService.addVillageReview(new CraftReview(Integer.parseInt(villageID), Integer.parseInt(userID), Integer.parseInt(rating), reviewText));

        String url = "village?id=" + villageID;
        response.sendRedirect("village?id=" + villageID);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String villageIDStr = request.getParameter("id");
            int villageID = Integer.parseInt(villageIDStr);

            CraftVillage villageDetails = vService.getVillageById(villageID);
            if (villageDetails == null) {
                throw new RuntimeException("Village not found");
            }

            List<Product> listProduct = pService.getProductsByVillage(villageID);
            List<Ticket> listTicket = tService.getTicketsByVillage(villageID);
            List<CraftReview> listReview = rService.getAllVillageReviewByAdmin(villageID);
            Account seller = aService.getAccountById(villageDetails.getSellerId());
            List<TicketType> ticketType = tService.getAllTicketType();

            // Tính trung bình rating (nếu có review)
            double totalRating = 0;
            for (CraftReview review : listReview) {
                totalRating += review.getRating();
            }
            int reviewCount = listReview.size();
            BigDecimal averageRating = BigDecimal.valueOf(reviewCount > 0 ? totalRating / reviewCount : 0);

            // Gán lại thông tin review cho village
            villageDetails.setTotalReviews(reviewCount);
            villageDetails.setAverageRating(averageRating);

            // Đẩy dữ liệu lên JSP
            request.setAttribute("villageDetails", villageDetails);
            request.setAttribute("listProduct", listProduct);
            request.setAttribute("listTicket", listTicket);
            request.setAttribute("listReview", listReview);
            request.setAttribute("seller", seller);
            request.setAttribute("ticketType", ticketType);

            request.getRequestDispatcher("VillageDetails.jsp").forward(request, response);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading village details");
        }
    }

}
