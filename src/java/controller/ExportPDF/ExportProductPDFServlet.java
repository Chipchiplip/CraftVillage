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
import entity.CraftVillage.CraftType;
import entity.CraftVillage.CraftVillage;
import entity.Product.Product;
import entity.Product.ProductCategory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ProductService;
import service.VillageService;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ExportProductPDFServlet", urlPatterns = {"/export-product-pdf"})
public class ExportProductPDFServlet extends HttpServlet {

    private final ProductService pService = new ProductService();
    private final VillageService vService = new VillageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"product-report.pdf\"");

        String cas = request.getParameter("cas");
        List<ProductCategory> listCateCategory = (List<ProductCategory>) request.getAttribute("listCC");
        List<CraftVillage> listCraftVillage = (List<CraftVillage>) request.getAttribute("listAllVillage");
        List<CraftType> listCraftType = (List<CraftType>) request.getAttribute("listVillages");

        try {
            Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Title fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            document.add(new Paragraph("Product Management Report", titleFont));
            document.add(new Paragraph("Generated at: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), dataFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = null;

            String[] headers1 = {"Product ID", "Product Name", "Price(d)", "Stock", "Description", "Category",
                    "Village", "Craft Type", "Click Count", "Average Rating", "Total Reviews", "Created Date", "Updated Date"};
            String[] headers2 = {"CategoryID", "Category Name", "Product ID", "Product Name", "Price(d)",
                    "Stock", "Click Count", "Created Date", "Average Rating", "Total Reviews"};
            String[] headers3 = {"CategoryID", "Category Name", "Product ID", "Product Name"};
            String[] headers4 = {"Category ID", "Category Name", "Product ID", "Product Name", "Average Rating",
                    "Total Reviews", "Price (d)"};

            switch (cas) {
                case "1":
                    document.add(new Paragraph("All Product Report", titleFont));
                    table = new PdfPTable(headers1.length);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{
                            1.5f, 3.0f, 2.0f, 2.0f, 4.0f, 3.0f,
                            2.0f, 2.0f, 2.5f, 2.0f, 2.5f, 2.5f, 2.5f
                    });
                    addTableHeader(table, headers1, headerFont);
                    for (Product product : pService.getAllProductActiveByAdmin()) {
                        addProductRow(product, table, dataFont);
                    }
                    break;

                case "2":
                    document.add(new Paragraph("All Product By Category Report", titleFont));
                    table = new PdfPTable(headers2.length);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{
                            1.5f, 2.5f, 2.5f, 3.5f, 3.0f,
                            2.5f, 2.5f, 1.5f, 3.0f, 1.5f
                    });
                    addTableHeader(table, headers2, headerFont);
                    for (ProductCategory cat : pService.getAllCategory()) {
                        for (Product p : pService.getProductByCategory(cat.getCategoryID())) {
                            table.addCell(cell(cat.getCategoryID(), dataFont));
                            table.addCell(cell(cat.getCategoryName(), dataFont));
                            table.addCell(cell(p.getId(), dataFont));
                            table.addCell(cell(p.getName(), dataFont));
                            table.addCell(cell(p.getPrice(), dataFont));
                            table.addCell(cell(p.getStock(), dataFont));
                            table.addCell(cell(p.getClickCount(), dataFont));
                            table.addCell(cell(p.getCreatedDate(), dataFont));
                            table.addCell(cell(p.getAverageRating(), dataFont));
                            table.addCell(cell(p.getTotalReviews(), dataFont));
                        }
                    }
                    break;

                case "3":
                    document.add(new Paragraph("Out of Stock Product Report", titleFont));
                    table = new PdfPTable(headers3.length);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{1.5f, 2.5f, 2.5f, 3.5f});
                    addTableHeader(table, headers3, headerFont);
                    for (Product product : pService.getProductOutOfStockByAdmin()) {
                        table.addCell(cell(product.getCategoryID(), dataFont));
                        table.addCell(cell(pService.getCategoryNameByCategoryID(product.getCategoryID()), dataFont));
                        table.addCell(cell(product.getId(), dataFont));
                        table.addCell(cell(product.getName(), dataFont));
                    }
                    break;

                case "4":
                    document.add(new Paragraph("All Deactivate Product Report", titleFont));
                    table = new PdfPTable(headers1.length);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{
                            1.5f, 3.0f, 2.0f, 2.0f, 4.0f, 3.0f,
                            2.0f, 2.0f, 2.5f, 2.0f, 2.5f, 2.5f, 2.5f
                    });
                    addTableHeader(table, headers1, headerFont);
                    for (Product product : pService.getSearchProductByAdmin(0, 0, "")) {
                        addProductRow(product, table, dataFont);
                    }
                    break;

                case "5":
                    document.add(new Paragraph("Top Rated Product Report", titleFont));
                    table = new PdfPTable(headers4.length);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{
                            1.5f, 2.5f, 2.5f, 3.5f, 2.5f, 2.5f, 2.5f
                    });
                    addTableHeader(table, headers4, headerFont);
                    for (Product product : pService.getTopRatedByAdmin()) {
                        table.addCell(cell(product.getCategoryID(), dataFont));
                        table.addCell(cell(pService.getCategoryNameByCategoryID(product.getCategoryID()), dataFont));
                        table.addCell(cell(product.getId(), dataFont));
                        table.addCell(cell(product.getName(), dataFont));
                        table.addCell(cell(product.getAverageRating(), dataFont));
                        table.addCell(cell(product.getTotalReviews(), dataFont));
                        table.addCell(cell(product.getPrice(), dataFont));
                    }
                    break;

                default:
                    throw new ServletException("Invalid cas value");
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    private void addTableHeader(PdfPTable table, String[] headers, Font headerFont) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setPadding(4f);
            table.addCell(cell);
        }
    }

    private void addProductRow(Product product, PdfPTable table, Font dataFont) {
        table.addCell(cell(product.getId(), dataFont));
        table.addCell(cell(product.getName(), dataFont));
        table.addCell(cell(product.getPrice(), dataFont));
        table.addCell(cell(product.getStock(), dataFont));
        table.addCell(cell(product.getDescription(), dataFont));
        table.addCell(cell(pService.getCategoryNameByCategoryID(product.getCategoryID()), dataFont));
        table.addCell(cell(vService.getVillageNameByID(product.getVillageID()), dataFont));
        table.addCell(cell(vService.getCraftTypeNameByID(product.getCraftTypeID()), dataFont));
        table.addCell(cell(product.getClickCount(), dataFont));
        table.addCell(cell(product.getAverageRating(), dataFont));
        table.addCell(cell(product.getTotalReviews(), dataFont));
        table.addCell(cell(product.getCreatedDate(), dataFont));
        table.addCell(cell(product.getUpdatedDate(), dataFont));
    }

    private PdfPCell cell(Object value, Font font) {
        String text;
        if (value == null) {
            text = "-";
        } else if (value instanceof Timestamp) {
            text = new SimpleDateFormat("dd/MM/yyyy HH:mm").format((Timestamp) value);
        } else {
            text = value.toString();
        }
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(3f);
        cell.setNoWrap(false);
        return cell;
    }
}