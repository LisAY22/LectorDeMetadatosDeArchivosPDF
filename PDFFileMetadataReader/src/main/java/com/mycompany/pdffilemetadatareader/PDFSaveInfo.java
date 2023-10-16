package com.mycompany.pdffilemetadatareader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;


public class PDFSaveInfo {
    public static void guardarInformacionEnArchivo(List<PDFFileInfo> pdfFiles, String csvFileName) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("pdfInfo.dat"))) {
        outputStream.writeObject(pdfFiles);
        
        // Guardar la información en un archivo CSV
        guardarInformacionEnCSV(pdfFiles, csvFileName);
        
    } catch (IOException e) {
        e.printStackTrace(System.out);
    }
    }

    
    private static void guardarInformacionEnCSV(List<PDFFileInfo> pdfFiles, String csvFileName) {
        try (FileWriter writer = new FileWriter(csvFileName)) {
            // Escribir el encabezado CSV
            writer.write("Name,Author,File Size (bytes),Page Size,Pages,Title,Subject,Keywords,FileType,Version,Source application,Images,Fonts,Summary\n");
            // Llenar el archivo CSV con los datos de los archivos PDF
            for (PDFFileInfo fileInfo : pdfFiles) {
                StringBuilder line = new StringBuilder();
                line.append(fileInfo.getName()).append(", ");
                line.append(fileInfo.getAuthor()).append(", ");
                line.append(fileInfo.getFileSize()).append(", ");
                line.append(fileInfo.getPageSize()).append(", ");
                line.append(fileInfo.getPageCount()).append(", ");
                line.append(fileInfo.getTitle()).append(", ");
                line.append(fileInfo.getSubject()).append(", ");
                line.append(fileInfo.getKeywords()).append(", ");
                line.append(fileInfo.getFileType()).append(", ");
                line.append(fileInfo.getPdfVersion()).append(", ");
                line.append(fileInfo.getCreator()).append(", ");
                line.append(fileInfo.getImagesCount()).append(", ");
                line.append(fileInfo.getImagesFontsCount()).append(", ");
                line.append(fileInfo.getSummary()).append(", ");
                writer.write(line.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

}