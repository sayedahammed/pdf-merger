package com.pdf.pdfmerger;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class PdfMergeController {

    @PostMapping("/merge-pdfs")
    public String mergePDFs(@RequestParam("files") List<MultipartFile> files) {
        try {
            ByteArrayOutputStream mergedPdf = new ByteArrayOutputStream();
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, mergedPdf);
            document.open();

            for (MultipartFile file : files) {
                System.out.println(file.getOriginalFilename());

                PdfReader reader = new PdfReader(file.getInputStream());
                int pageCount = reader.getNumberOfPages();

                for (int pageNum = 1; pageNum <= pageCount; pageNum++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, pageNum);
                    copy.addPage(page);
                }
            }

            document.close();

            // Store the merged PDF in the resources folder
            String resourcesPath = "src/main/resources/";
            String mergedPdfFileName = "merged.pdf";
            String fullFilePath = resourcesPath + mergedPdfFileName;
            File outputFile = new File(fullFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(mergedPdf.toByteArray());
            fileOutputStream.close();

            return mergedPdfFileName;
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return null;
        }
    }
}
