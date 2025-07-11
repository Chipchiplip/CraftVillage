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
import entity.Ticket.Ticket;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.TicketService;
import service.VillageService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ExportTicketPDFServlet", urlPatterns = {"/export-ticket-pdf"})
public class ExportTicketPDFServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ExportTicketPDFServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"ticket_report.pdf\"");

        String cas = request.getParameter("cas");
        int status;
        String reportTitle;

        switch (cas) {
            case "1":
                status = 1;
                reportTitle = "All Active Tickets Report";
                break;
            case "2":
                status = 2;
                reportTitle = "Tickets Sorted by TypeID Report";
                break;
            case "3":
                status = 0;
                reportTitle = "All Inactive Tickets Report";
                break;
            default:
                throw new ServletException("Invalid cas value: " + cas);
        }

        try {
            Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            document.add(new Paragraph("Ticket Management Report", titleFont));
            document.add(new Paragraph("Generated at: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), dataFont));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph(reportTitle, titleFont));
            document.add(Chunk.NEWLINE);

            String[] headers = new String[]{
                    "Ticket ID", "Village Name", "Ticket Type", "Price (VND)", "Status", "Created Date", "Updated Date"
            };

            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2.5f, 2.5f, 2.0f, 1.5f, 3.0f, 3.0f});

            // Add header
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(4f);
                table.addCell(cell);
            }

            TicketService tService = new TicketService();
            VillageService vService = new VillageService();

            List<Ticket> list;
            if ("2".equals(cas)) {
                list = tService.getTickeReportByAdmin(status);
            } else {
                list = tService.getTickeReportByAdmin(status);
            }

            for (Ticket ticket : list) {
                table.addCell(cell(ticket.getTicketID(), dataFont));
                table.addCell(cell(vService.getVillageNameByID(ticket.getVillageID()), dataFont));
                table.addCell(cell(tService.getTicketNameByID(ticket.getTypeID()), dataFont));
                table.addCell(cell(ticket.getPrice(), dataFont));
                table.addCell(cell(ticket.getStatus() == 1 ? "Active" : "Inactive", dataFont));
                table.addCell(cell(ticket.getCreatedDate(), dataFont));
                table.addCell(cell(ticket.getUpdatedDate(), dataFont));
            }

            document.add(table);
            document.close();
            response.getOutputStream().flush();

        } catch (DocumentException e) {
            LOGGER.log(Level.SEVERE, "Error generating PDF", e);
            response.setContentType("text/plain");
            response.getWriter().write("Error generating PDF: " + e.getMessage());
        }
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
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setPadding(3f);
        cell.setNoWrap(false);
        return cell;
    }
}