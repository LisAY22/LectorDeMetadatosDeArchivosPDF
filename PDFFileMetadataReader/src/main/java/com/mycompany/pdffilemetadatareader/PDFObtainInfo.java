package com.mycompany.pdffilemetadatareader;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;


public class PDFObtainInfo {
    public static List<PDFFileInfo> buscarArchivosPDF(File folder) {
    List<PDFFileInfo> pdfFiles = new ArrayList<>();
    // Llama a la función recursiva para buscar archivos PDF dentro de la carpeta y sus subcarpetas
    buscarArchivosPDFRecursivamente(folder, pdfFiles);
    return pdfFiles;
    }

    
    private static void buscarArchivosPDFRecursivamente(File folder, List<PDFFileInfo> pdfFiles) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // Si el elemento es una carpeta, llama recursivamente a esta función
                        buscarArchivosPDFRecursivamente(file, pdfFiles);
                    } else if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                        // Si el elemento es un archivo PDF, obtiene su información utilizando la función obtenerInformacionPDF()
                        PDFFileInfo fileInfo = obtenerInformacionPDF(file);

                        // Verifica si la información no es nula antes de agregarla a la lista de archivos PDF encontrados
                        if (fileInfo != null) {
                            pdfFiles.add(fileInfo);
                        }
                    }
                }
            }
        }
    }

    
    private static PDFFileInfo obtenerInformacionPDF(File pdfFile) {
    try {
        String name;
        String author;
        String title;
        String subject;
        String keywords;
        String fileType;
        float pdfVersion;
        String creator;
        int pageCount;
        long fileSize;
        int imagesCount;
        int imagesFontsCount;
        String pageSize;
        String summary;
        StyledDocument summaryDocument;
        
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDDocumentInformation info = document.getDocumentInformation();
            name = pdfFile.getName();
            if (info.getAuthor() != null && !info.getAuthor().trim().isEmpty()) {
                author = info.getAuthor();
            } else {
                author = "Sin autor";
            }   if (info.getTitle() != null && !info.getTitle().trim().isEmpty()){
                title = info.getTitle();
            } else {
                title = "Sin título";
            }   if (info.getSubject() != null && !info.getSubject().trim().isEmpty()) {
                subject = info.getSubject();
            } else {
                subject = "Sin asunto";
            }   if (info.getKeywords() != null && !info.getKeywords().trim().isEmpty()) {
                keywords = info.getKeywords();
            } else {
                keywords = "Sin palabras clave";
            }   
            fileType = "PDF";
            pdfVersion = document.getVersion();
            if (info.getCreator() != null && !info.getCreator().trim().isEmpty()) {
                creator = info.getCreator();
            } else {
                creator = "Sin aplicación de origen";
            }   
            pageCount = document.getNumberOfPages();
            fileSize = pdfFile.length();
            imagesCount = countImagesInPDF(document);
            imagesFontsCount = countImagesWithFonts(document, "Fuente");
            pageSize = getPageSize(document);
            if (getPDFSummary(document) != null && !getPDFSummary(document).trim().isEmpty()) {
                summary = getPDFSummary(document);
            } else {
                summary = "Sin Resumen";
            }
            StringBuilder summaryBuilder = new StringBuilder();
            summaryBuilder.append(summary);

            summaryDocument = new DefaultStyledDocument();
            try {
                summaryDocument.insertString(0, summaryBuilder.toString(), null);
            } catch (BadLocationException e) {
                e.printStackTrace(System.out);
            }
            
        }

        // Crea una instancia de PDFFileInfo con la información recopilada
        return new PDFFileInfo(pdfFile, name, author, fileSize, pageSize, pageCount, title, subject, keywords, fileType, pdfVersion, creator, imagesCount,imagesFontsCount, summary, summaryDocument);
    } catch (IOException e) {
        e.printStackTrace(System.out);
        return null;
        }
    }
    
    
    private static int countImagesInPDF(PDDocument document) {
    int count = 0;

    for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
        PDPage page = document.getPage(pageNumber);
        PDResources resources = page.getResources();
        Iterable<COSName> xObjectNames = resources.getXObjectNames();

        for (COSName xObjectName : xObjectNames) {
            if (resources.isImageXObject(xObjectName)) {
                // El recurso XObject es una imagen, incrementa el contador de imágenes.
                count++;
            } else {
                // El recurso XObject no es una imagen, puede ser una gráfica u otro elemento.
                count++; // Incrementa el contador de gráficas (o recursos no reconocidos como imágenes).
            }
        }
    }
    return count;
    }
    
    
    private static int countImagesWithFonts(PDDocument document, String wordToCount) {
    int count = 0;
    try {
        PDFTextStripper stripper = new PDFTextStripper();
        String pdfText = stripper.getText(document);
        // Contar las ocurrencias de la palabra en el texto del PDF
        int index = pdfText.toLowerCase().indexOf(wordToCount.toLowerCase());
        while (index != -1) {
            count++;
            index = pdfText.toLowerCase().indexOf(wordToCount.toLowerCase(), index + 1);
        }
    } catch (IOException e) {
        e.printStackTrace(System.out);
    }
    return count;
}

    
    private static String getPageSize(PDDocument file) {
    // Crea un objeto DecimalFormat para formatear los valores de tamaño de página
    DecimalFormat df = new DecimalFormat("###.#"); 
    // Obtiene la primera página del documento
    PDPage page = file.getPage(0);
    String pageSize;
    // Obtiene el rectángulo de medios (mediaBox) de la página
    PDRectangle mediaBox = page.getMediaBox();
    // Calcula el ancho y el alto de la página en pulgadas (dividiendo por 72 puntos por pulgada)
    double width = mediaBox.getWidth() / 72;
    double height = mediaBox.getHeight() / 72;
    // Compara el ancho y el alto con tamaños de página estándar para determinar el tipo de página
    if (((Objects.equals(df.format(width), "8.5")) || ((Objects.equals(df.format(height), "11.0"))))) {
        pageSize = "Carta";
    } else if (((Objects.equals(df.format(width), "8.3")) || ((Objects.equals(df.format(height), "11.8"))))) {
        pageSize = "Oficio";
    } else {
        // Si no coincide con ningún tamaño estándar, se muestra el ancho y el alto en pulgadas
        pageSize = df.format(width) + " x " + df.format(height);
    }  
    // Devuelve el tamaño de la página como una cadena de texto
    return pageSize;
    }
    
    private static String getPDFSummary(PDDocument document) throws IOException {
        PDFTextStripper pdfStripper = new PDFTextStripper() {
            int newLineCount = 0;
            boolean newLineRequired = false;

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                if (text.trim().isEmpty()) {
                    if (newLineRequired) {
                        newLineCount++;
                    }
                } else {
                    if (newLineCount > 3) {
                        newLineCount = 3;
                    }
                    while (newLineCount > 0) {
                        super.writeString("\n", null);
                        newLineCount--;
                    }
                    newLineRequired = false;
                    super.writeString(text, textPositions);
                }
            }

            @Override
            protected void writeLineSeparator() throws IOException {
                newLineRequired = true;
            }
        };
        String text = pdfStripper.getText(document);
        
        List<String> summary = new ArrayList<>();
        String[] sentences = text.split("\\.");

        for (String sentence : sentences) {

            if (sentence.split(" ").length > 40) {
                summary.add(sentence);
            }
        }

        String joinedSummary = String.join(".\n", summary);
        return joinedSummary.trim();
    }
}