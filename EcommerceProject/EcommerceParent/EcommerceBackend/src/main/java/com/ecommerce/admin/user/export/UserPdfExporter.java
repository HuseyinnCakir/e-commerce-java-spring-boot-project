package com.ecommerce.admin.user.export;

import com.ecommerce.common.entity.User;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {
    public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse,"application/pdf",".pdf");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,httpServletResponse.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);
        Paragraph paragraph = new Paragraph("List of the Users",font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(paragraph);

        PdfPTable pdfTable = new PdfPTable(6);
        pdfTable.setWidthPercentage(100f);
        pdfTable.setSpacingBefore(10);
        pdfTable.setWidths(new float[] {1.5f,3.5f,3.0f,3.0f,1.7f});
        document.add(pdfTable);
        writeTableHeader(pdfTable);
        writeTableData(pdfTable,listUsers);
        document.close();

    }

    private void writeTableData(PdfPTable pdfTable, List<User> listUsers) {

        for (User user: listUsers){

            pdfTable.addCell(String.valueOf(user.getId()));
            pdfTable.addCell(user.getEmail());
            pdfTable.addCell(user.getFirstName());
            pdfTable.addCell(user.getLastName());
            pdfTable.addCell(user.getRoles().toString());
            pdfTable.addCell(String.valueOf(user.isEnabled()));
        }
    }


    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("User ID",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("First Name",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Last Name",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles",font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled",font));
        table.addCell(cell);
    }
}
