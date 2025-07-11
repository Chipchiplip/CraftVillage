/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ExportPDF;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Account.SellerVerification;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import service.SellerVerificationService;

/**
 *
 * @author ACER
 */
@WebServlet(name = "ExportRequestPDFServlet", urlPatterns = {"/export-request-pdf"})
public class ExportRequestPDFServlet extends HttpServlet {

    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ExportRequestPDFServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExportRequestPDFServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"request-report.pdf\"");

    String cas = request.getParameter("cas");
    int status;
    String reportTitle;

    switch (cas) {
        case "0":
            status = 0;
            reportTitle = "All Processing Request Report";
            break;
        case "1":
            status = 1;
            reportTitle = "All Approved Request Report";
            break;
        case "2":
            status = 2;
            reportTitle = "All Rejected Request Report";
            break;
        case "3":
            status = 3;
            reportTitle = "All Request Report";
            break;
        default:
            throw new AssertionError("Invalid cas value: " + cas);
    }

    try {
        Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6);
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 7);

        document.add(new Paragraph("Seller Verification Report", titleFont));
        document.add(new Paragraph("Generated at: " + new Date(), dataFont));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph(reportTitle, titleFont));
        document.add(Chunk.NEWLINE);

        String[] headers = new String[]{
            "Verification ID", "Seller ID", "Business Type",
            "Business Village Category", "Business Village Name",
            "Business Village Address", "Product Product Category",
            "Profile Village Picture URL", "Contact Person", "Contact Phone",
            "Contact Email", "ID Card Number", "ID Card Front URL",
            "ID Card Back URL", "Business License", "Tax Code",
            "Document URL", "Note", "Verification Status", "Verified By",
            "Verified Date", "Reject Reason", "Created Date"
        };

        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{
            1.5f, 1.5f, 2.0f, 3.0f, 3.0f, 4.5f,
            3.0f, 4.0f, 2.0f, 2.0f, 3.5f, 2.0f,
            3.0f, 3.0f, 2.5f, 2.0f, 3.5f, 2.5f,
            1.5f, 1.5f, 2.0f, 3.0f, 2.0f
        });

        // Header cells
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(3f);
            cell.setMinimumHeight(20f);
            cell.setNoWrap(false); // Cho phép xuống dòng
            table.addCell(cell);
        }

        SellerVerificationService sService = new SellerVerificationService();
        List<SellerVerification> list = sService.getSellerVertificationFormByAdmin(status);

        for (SellerVerification sellerForm : list) {
            table.addCell(cell(sellerForm.getVerificationID(), dataFont));
            table.addCell(cell(sellerForm.getSellerID(), dataFont));
            table.addCell(cell(sellerForm.getBusinessType(), dataFont));
            table.addCell(cell(sellerForm.getBusinessVillageCategry(), dataFont));
            table.addCell(cell(sellerForm.getBusinessVillageName(), dataFont));
            table.addCell(cell(sellerForm.getBusinessVillageAddress(), dataFont));
            table.addCell(cell(sellerForm.getProductProductCategory(), dataFont));
            table.addCell(cell(sellerForm.getProfileVillagePictureUrl(), dataFont));
            table.addCell(cell(sellerForm.getContactPerson(), dataFont));
            table.addCell(cell(sellerForm.getContactPhone(), dataFont));
            table.addCell(cell(sellerForm.getContactEmail(), dataFont));
            table.addCell(cell(sellerForm.getIdCardNumber(), dataFont));
            table.addCell(cell(sellerForm.getIdCardFrontUrl(), dataFont));
            table.addCell(cell(sellerForm.getIdCardBackUrl(), dataFont));
            table.addCell(cell(sellerForm.getBusinessLicense(), dataFont));
            table.addCell(cell(sellerForm.getTaxCode(), dataFont));
            table.addCell(cell(sellerForm.getDocumentUrl(), dataFont));
            table.addCell(cell(sellerForm.getNote(), dataFont));
            table.addCell(cell(sellerForm.getVerificationStatus(), dataFont));
            table.addCell(cell(sellerForm.getVerifiedBy(), dataFont));
            table.addCell(cell(sellerForm.getVerifiedDate(), dataFont));
            table.addCell(cell(sellerForm.getRejectReason(), dataFont));
            table.addCell(cell(sellerForm.getCreatedDate(), dataFont));
        }

        document.add(table);
        document.close();
        response.getOutputStream().flush();

    } catch (DocumentException e) {
        e.printStackTrace();
        response.setContentType("text/plain");
        response.getWriter().write("Error generating PDF: " + e.getMessage());
    }
}

private PdfPCell cell(Object value, Font font) {
    String text;
    if (value == null) {
        text = "";
    } else if (value instanceof Timestamp) {
        text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Timestamp) value);
    } else {
        text = value.toString();
    }

    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setPadding(3f);
    cell.setMinimumHeight(15f);
    cell.setNoWrap(false); // Cho phép wrap cho data cũng được
    return cell;
}
 
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}