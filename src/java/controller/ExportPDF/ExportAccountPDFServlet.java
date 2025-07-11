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
import entity.Account.Account;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.AccountService;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet(name = "ExportPDFServlet", urlPatterns = {"/export-account-pdf"})
public class ExportAccountPDFServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"account-report.pdf\"");

        String cas = request.getParameter("cas");

        try {
            Document document = new Document(PageSize.A3.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8);
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);

            document.add(new Paragraph("Account Management Report", titleFont));
            document.add(new Paragraph("Generated at: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()), dataFont));
            document.add(Chunk.NEWLINE);

            PdfPTable table = null;

            String[] headers1 = {
                    "User ID", "UserName", "Full Name", "Email", "Address",
                    "Phone Number", "Created Date", "RoleID", "Last Login"
            };
            String[] headers2 = {"Role Name", "Quantity"};

            switch (cas) {
                case "1":
                    document.add(new Paragraph("All Account Report", titleFont));
                    document.add(new Paragraph("RoleID: 1=Customer, 2=Seller, 3=Admin", dataFont));
                    table = createTable(headers1, headerFont);
                    for (Account acc : new AccountService().getAllAccounts()) {
                        addAccountRow(table, acc, dataFont);
                    }
                    break;
                case "2":
                    document.add(new Paragraph("All Customer Account Report", titleFont));
                    document.add(new Paragraph("RoleID: 1=Customer, 2=Seller, 3=Admin", dataFont));
                    table = createTable(headers1, headerFont);
                    for (Account acc : new AccountService().getSearchAccount(1, 1, "")) {
                        addAccountRow(table, acc, dataFont);
                    }
                    break;
                case "3":
                    document.add(new Paragraph("All Seller Account Report", titleFont));
                    document.add(new Paragraph("RoleID: 1=Customer, 2=Seller, 3=Admin", dataFont));
                    table = createTable(headers1, headerFont);
                    for (Account acc : new AccountService().getSearchAccount(1, 2, "")) {
                        addAccountRow(table, acc, dataFont);
                    }
                    break;
                case "4":
                    document.add(new Paragraph("All Admin Account Report", titleFont));
                    document.add(new Paragraph("RoleID: 1=Customer, 2=Seller, 3=Admin", dataFont));
                    table = createTable(headers1, headerFont);
                    for (Account acc : new AccountService().getSearchAccount(1, 3, "")) {
                        addAccountRow(table, acc, dataFont);
                    }
                    break;
                case "5":
                    document.add(new Paragraph("All Deactivate Account Report", titleFont));
                    document.add(new Paragraph("RoleID: 1=Customer, 2=Seller, 3=Admin", dataFont));
                    table = createTable(headers1, headerFont);
                    for (Account acc : new AccountService().getSearchAccount(0, 0, "")) {
                        addAccountRow(table, acc, dataFont);
                    }
                    break;
                case "6":
                    document.add(new Paragraph("Quantity By Account Role Report", titleFont));
                    table = createTable(headers2, headerFont);
                    table.addCell(cell("Customer", dataFont));
                    table.addCell(cell(String.valueOf(new AccountService().getSearchAccount(1, 1, "").size()), dataFont));
                    table.addCell(cell("Seller", dataFont));
                    table.addCell(cell(String.valueOf(new AccountService().getSearchAccount(1, 2, "").size()), dataFont));
                    table.addCell(cell("Admin", dataFont));
                    table.addCell(cell(String.valueOf(new AccountService().getSearchAccount(1, 3, "").size()), dataFont));
                    break;
                case "7":
                    int year = java.time.Year.now().getValue();
                    document.add(new Paragraph("Monthly Registration Summary Report - Year " + year, titleFont));
                    document.add(Chunk.NEWLINE);
                    table = new PdfPTable(2);
                    table.setWidthPercentage(100);
                    table.setWidths(new float[]{2.0f, 3.0f});

                    table.addCell(headerCell("Month", headerFont));
                    table.addCell(headerCell("Total Registrations", headerFont));

                    Map<Integer, Integer> registrationMap = new AccountService().getRegistrationSummaryByMonthYear(year);

                    for (int month = 1; month <= 12; month++) {
                        table.addCell(cell("Month " + month, dataFont));
                        table.addCell(cell(String.valueOf(registrationMap.getOrDefault(month, 0)), dataFont));
                    }
                    break;
                default:
                    throw new ServletException("Invalid case value.");
            }

            if (table != null) {
                document.add(table);
            }
            document.close();

        } catch (DocumentException e) {
            throw new IOException("Error generating PDF", e);
        }
    }

    private PdfPTable createTable(String[] headers, Font headerFont) throws DocumentException {
        PdfPTable table = new PdfPTable(headers.length);
        table.setWidthPercentage(100);
        float[] widths = new float[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = 2.5f;
        }
        table.setWidths(widths);
        for (String header : headers) {
            table.addCell(headerCell(header, headerFont));
        }
        return table;
    }

    private PdfPCell headerCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4f);
        return cell;
    }

    private void addAccountRow(PdfPTable table, Account acc, Font font) {
        table.addCell(cell(acc.getUserID(), font));
        table.addCell(cell(acc.getUserName(), font));
        table.addCell(cell(acc.getFullName(), font));
        table.addCell(cell(acc.getEmail(), font));
        table.addCell(cell(acc.getAddress(), font));
        table.addCell(cell(acc.getPhoneNumber(), font));
        table.addCell(cell(acc.getCreatedDate(), font));
        table.addCell(cell(acc.getRoleID(), font));
        table.addCell(cell(acc.getLastLoginDate(), font));
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