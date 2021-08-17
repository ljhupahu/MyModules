package org.my;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/8/16 10:48
 */
public class PDFToWordPDFBOX {
    public static void main(String[] args) {
        String pdfPath = "D:\\Work\\development\\WorkSpace\\My\\MyModules\\tools\\pdf\\src\\main\\java\\org\\my\\sample6.pdf";
        PDDocument doc = null;
        OutputStream fos = null;
        Writer writer = null;
        PDFTextStripper stripper = null;
        try {
            doc = PDDocument.load(new File(pdfPath));
            fos = new FileOutputStream(pdfPath.substring(0, pdfPath.indexOf(".")) + ".doc");
            writer = new OutputStreamWriter(fos, "UTF-8");
            stripper = new PDFTextStripper();
            int pageNumber = doc.getNumberOfPages();
            stripper.setSortByPosition(true);
            stripper.setStartPage(1);
            stripper.setEndPage(pageNumber);
            stripper.writeText(doc, writer);
            writer.close();
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("end..");
    }
}
