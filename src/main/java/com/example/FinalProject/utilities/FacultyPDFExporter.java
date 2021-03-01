package com.example.FinalProject.utilities;

import com.example.FinalProject.entities.Faculty;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
/**
 * Class for exporting faculties to pdf format
 */
public class FacultyPDFExporter {
    private final List<Faculty> faculties;

    public FacultyPDFExporter(List<Faculty> faculties){
        this.faculties = faculties;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Faculty id", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Title", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Places", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Budget Places", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Contract Places", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Students", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Faculty faculty : faculties) {
            table.addCell(String.valueOf(faculty.getFacultyid()));
            table.addCell(faculty.getTitle());
            table.addCell(String.valueOf(faculty.getTotalPlaces()));
            table.addCell(String.valueOf(faculty.getBudgetPlaces()));
            table.addCell(String.valueOf(faculty.getContractPlaces()));
            table.addCell(String.valueOf(faculty.getStudents().size()));
        }
    }

    public void export() throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream("facultyTable.pdf"));

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Faculties", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 1.5f, 3.0f, 2f,2f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
