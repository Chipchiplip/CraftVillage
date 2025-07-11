package controller.ExportPDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import entity.CraftVillage.CraftType;
import entity.CraftVillage.CraftVillage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.VillageService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "ExportVillagePDFServlet", urlPatterns = {"/export-village-pdf"})
public class ExportVillagePDFServlet extends HttpServlet {

    private final VillageService vService = new VillageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"village-report.pdf\"");

        String cas = request.getParameter("cas");

        List<CraftVillage> listCraftVillage = (List<CraftVillage>) request.getAttribute("listAllVillage");
        List<CraftType> listCraftType = (List<CraftType>) request.getAttribute("listVillages");

        try {
            Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            document.add(new Paragraph("Village Management Report", titleFont));
            document.add(new Paragraph("Generated at: "
                    + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), dataFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table;

            String[] headers1 = {
                    "Village ID", "Type ID", "Village Name", "Description", "Address", "Latitude",
                    "Longitude", "Contact Phone", "Contact Email", "Status", "Click Count", "Last Clicked",
                    "Main Image URL", "Created Date", "Updated Date", "Seller ID", "Opening Hours",
                    "Closing Days", "Average Rating", "Total Reviews", "Map Embed URL", "Virtual Tour URL",
                    "History", "Special Features", "Famous Products", "Cultural Events",
                    "Craft Process", "Video Description URL", "Travel Tips"
            };

            String[] headers2 = {
                    "Type ID", "Type Name", "Village ID", "Village Name", "Address",
                    "Click Count", "Created Date", "Average Rating", "Total Reviews"
            };

            String[] headers4 = {
                    "Village ID", "Village Name", "Type ID", "Average Rating",
                    "Total Reviews", "Address", "Created Date"
            };

            float[] widths1 = new float[headers1.length];
                    for (int i = 0; i < widths1.length; i++) {
                        widths1[i] = 3.0f;
                    }
            switch (cas) {
                case "1":
                    document.add(new Paragraph("All Village Report", titleFont));
                    table = new PdfPTable(headers1.length);
                    table.setWidthPercentage(100);
                    
                    table.setWidths(widths1);
                    addTableHeader(table, headers1, headerFont);
                    for (CraftVillage village : listCraftVillage) {
                        addVillageRow(village, table, dataFont);
                    }
                    break;

                case "2":
                    document.add(new Paragraph("All Village By Craft Type Report", titleFont));
                    table = new PdfPTable(headers2.length);
                    table.setWidthPercentage(100);
                    float[] widths2 = {2f, 4f, 2f, 4f, 4f, 2f, 3f, 2f, 2f};
                    table.setWidths(widths2);
                    addTableHeader(table, headers2, headerFont);
                    for (CraftType type : listCraftType) {
                        for (CraftVillage v : vService.getVillageByCategory(type.getTypeID())) {
                            table.addCell(cell(type.getTypeID(), dataFont));
                            table.addCell(cell(type.getTypeName(), dataFont));
                            table.addCell(cell(v.getVillageID(), dataFont));
                            table.addCell(cell(v.getVillageName(), dataFont));
                            table.addCell(cell(v.getAddress(), dataFont));
                            table.addCell(cell(v.getClickCount(), dataFont));
                            table.addCell(cell(v.getCreatedDate(), dataFont));
                            table.addCell(cell(v.getAverageRating(), dataFont));
                            table.addCell(cell(v.getTotalReviews(), dataFont));
                        }
                    }
                    break;

                case "3":
                    document.add(new Paragraph("All Deactivated Village Report", titleFont));
                    table = new PdfPTable(headers1.length);
                    table.setWidthPercentage(100);
                    table.setWidths(widths1);
                    addTableHeader(table, headers1, headerFont);
                    for (CraftVillage village : vService.getSearchVillageByAdmin(0, 0, "")) {
                        addVillageRow(village, table, dataFont);
                    }
                    break;

                case "4":
                    document.add(new Paragraph("Top Rated Village Report", titleFont));
                    table = new PdfPTable(headers4.length);
                    table.setWidthPercentage(100);
                    float[] widths4 = {2f, 4f, 2f, 2f, 2f, 4f, 3f};
                    table.setWidths(widths4);
                    addTableHeader(table, headers4, headerFont);
                    for (CraftVillage v : vService.getTopRatedByAdmin()) {
                        table.addCell(cell(v.getVillageID(), dataFont));
                        table.addCell(cell(v.getVillageName(), dataFont));
                        table.addCell(cell(v.getTypeID(), dataFont));
                        table.addCell(cell(v.getAverageRating(), dataFont));
                        table.addCell(cell(v.getTotalReviews(), dataFont));
                        table.addCell(cell(v.getAddress(), dataFont));
                        table.addCell(cell(v.getCreatedDate(), dataFont));
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

    private void addVillageRow(CraftVillage v, PdfPTable table, Font font) {
        table.addCell(cell(v.getVillageID(), font));
        table.addCell(cell(v.getTypeID(), font));
        table.addCell(cell(v.getVillageName(), font));
        table.addCell(cell(v.getDescription(), font));
        table.addCell(cell(v.getAddress(), font));
        table.addCell(cell(v.getLatitude(), font));
        table.addCell(cell(v.getLongitude(), font));
        table.addCell(cell(v.getContactPhone(), font));
        table.addCell(cell(v.getContactEmail(), font));
        table.addCell(cell(v.getStatus(), font));
        table.addCell(cell(v.getClickCount(), font));
        table.addCell(cell(v.getLastClicked(), font));
        table.addCell(cell(v.getMainImageUrl(), font));
        table.addCell(cell(v.getCreatedDate(), font));
        table.addCell(cell(v.getUpdatedDate(), font));
        table.addCell(cell(v.getSellerId(), font));
        table.addCell(cell(v.getOpeningHours(), font));
        table.addCell(cell(v.getClosingDays(), font));
        table.addCell(cell(v.getAverageRating(), font));
        table.addCell(cell(v.getTotalReviews(), font));
        table.addCell(cell(v.getMapEmbedUrl(), font));
        table.addCell(cell(v.getVirtualTourUrl(), font));
        table.addCell(cell(v.getHistory(), font));
        table.addCell(cell(v.getSpecialFeatures(), font));
        table.addCell(cell(v.getFamousProducts(), font));
        table.addCell(cell(v.getCulturalEvents(), font));
        table.addCell(cell(v.getCraftProcess(), font));
        table.addCell(cell(v.getVideoDescriptionUrl(), font));
        table.addCell(cell(v.getTravelTips(), font));
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
