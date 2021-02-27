package com.example.FinalProject.utilities;

import com.example.FinalProject.entities.Student;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class StudentPDFExporter {
    private final List<Student> studentList;

    public StudentPDFExporter(List<Student> studentList) {
        this.studentList = studentList;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Student ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Login", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Faculties", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Student student : studentList) {
            PdfPCell cell = new PdfPCell();
            if (student.isBudget()) {
                cell.setBackgroundColor(Color.green);
                cell.setPhrase(new Phrase(String.valueOf(student.getStudentid())));
                table.addCell(cell);
                cell.setBackgroundColor(Color.green);
                cell.setPhrase(new Phrase(student.getLogin()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.green);
                cell.setPhrase(new Phrase(student.getEmail()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.green);
                cell.setPhrase(new Phrase(student.getFaculties()
                        .stream().findFirst().get().getTitle()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.green);
                cell.setPhrase(new Phrase(String.valueOf(student.isBudget())));
                table.addCell(cell);
            } else {
                cell.setBackgroundColor(Color.red);
                cell.setPhrase(new Phrase(String.valueOf(student.getStudentid())));
                table.addCell(cell);
                cell.setBackgroundColor(Color.red);
                cell.setPhrase(new Phrase(student.getLogin()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.red);
                cell.setPhrase(new Phrase(student.getEmail()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.red);
                cell.setPhrase(new Phrase(student.getFaculties()
                        .stream().findFirst().get().getTitle()));
                table.addCell(cell);
                cell.setBackgroundColor(Color.red);
                cell.setPhrase(new Phrase(String.valueOf(student.isBudget())));
                table.addCell(cell);
            }

        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, new FileOutputStream("Table.pdf"));

            document.open();
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            font.setSize(18);
            font.setColor(Color.BLUE);

            Paragraph p = new Paragraph("List of Students", font);
            p.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(p);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100f);
            table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
            table.setSpacingBefore(10);

            writeTableHeader(table);
            writeTableData(table);

            document.add(table);

            document.close();
        }
    }
}
