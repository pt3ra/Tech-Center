package com.tech.center;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {
	
	private static final String FONT = "./src/main/resources/static/Times_New_Roman.ttf";

	public static byte[] generatePdfDocument(List<Order> orders, String fileName) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        BaseFont bf = null;
		try {
			bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
        Font font=new Font(bf,14,Font.NORMAL);

        document.open();
        document.add(new Paragraph("Order List"));

        for (Order order : orders) {
            document.add(new Paragraph(order.toString(), font));
            document.add(new Paragraph(order.getDetails(), font));
            document.add(new Paragraph(order.getDuration().toString(), font));
            document.add(new Paragraph("-----------------------"));
        }

        document.close();
        
        fileName = fileName.endsWith(".pdf") ? fileName : fileName + ".pdf";
        
        return baos.toByteArray();
    }
}
