package com.mycompany.pdffilemetadatareader;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.FileOutputStream;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.text.DecimalFormat;
import java.util.Objects;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

public class PDFFileMetadataReader {
    
    private static String csvFileName = "metadata.csv"; // Nombre del archivo CSV

    public static void main(String[] args) {
        // Establecer FlatLaf como el estilo de la interfaz gráfica
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        // Verificar si ya existe un archivo CSV
        boolean isFirstTime = !new File(csvFileName).exists();

        JFrame frame = new JFrame("PDF File Metadata Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setResizable(false);

        // Panel para la imagen
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\lisaj\\OneDrive\\Documentos\\GitHub\\LectorDeMetadatosDeArchivosPDF\\PDF file metadata reader.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setPreferredSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));

        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Hacemos el panel transparente
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Alineamos el botón a la derecha
        JButton continueButton = new JButton("Continuar →"); // Agregamos la flecha a la derecha
        continueButton.setForeground(Color.WHITE); // Color del texto blanco
        continueButton.setBackground(new Color(232, 36, 36)); // Color de fondo #E82424
        continueButton.setOpaque(true); // Establece que el botón sea opaco
        continueButton.setBorderPainted(false); // Quita el borde del botón
        continueButton.setFocusPainted(false); // Deshabilita el efecto de resaltado
        continueButton.setPreferredSize(new Dimension(150, 50)); // Ajusta el tamaño del botón
        // Ajustar el tamaño del botón
        continueButton.setPreferredSize(new Dimension(200, 50));
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (isFirstTime) {
                    ingresarRuta();
                } else {
                    mostrarVentanaOpciones();
                }
            }
        });
        buttonPanel.add(continueButton);

        // Panel principal que contiene la imagen y el botón
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void mostrarVentanaOpciones() {
        JFrame ventanaOpciones = new JFrame("Opciones");
        ventanaOpciones.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaOpciones.setSize(400, 300);
        ventanaOpciones.setResizable(false);

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Expandir en ambas direcciones
        gbc.insets = new Insets(10, 10, 10, 10); // Espacio entre botones

        JButton ingresarRutaButton = new JButton("Ingresar nueva ruta");
        ingresarRutaButton.setBackground(new Color(232, 36, 36));
        ingresarRutaButton.setForeground(Color.WHITE);
        ingresarRutaButton.setOpaque(true);
        ingresarRutaButton.setBorderPainted(false);
        ingresarRutaButton.setFocusPainted(false);
        
        JButton continuarMismaRutaButton = new JButton("Continuar con la misma ruta");
        continuarMismaRutaButton.setBackground(new Color(232, 36, 36));
        continuarMismaRutaButton.setForeground(Color.WHITE);
        continuarMismaRutaButton.setOpaque(true);
        continuarMismaRutaButton.setBorderPainted(false);
        continuarMismaRutaButton.setFocusPainted(false);

        // Configurar tamaño preferido para los botones
        Dimension buttonSize = new Dimension(300, 80);
        ingresarRutaButton.setPreferredSize(buttonSize);
        continuarMismaRutaButton.setPreferredSize(buttonSize);

        // Configurar restricciones de cuadrícula para los botones
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(ingresarRutaButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(continuarMismaRutaButton, gbc);

        ingresarRutaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ingresarRuta();
            }
        });
        continuarMismaRutaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                continuarMismaRuta();
            }
        });

        ventanaOpciones.getContentPane().add(buttonPanel);
        ventanaOpciones.setLocationRelativeTo(null);
        ventanaOpciones.setVisible(true);
    }
    
    private static void ingresarRuta() {
        // Mostrar un cuadro de diálogo para que el usuario seleccione una carpeta
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            List<PDFFileInfo> pdfFiles = buscarArchivosPDF(selectedFolder);
            // Guardar la información en un archivo
            guardarInformacionEnArchivo(pdfFiles);
            JOptionPane.showMessageDialog(null, "Búsqueda completada y datos guardados.");
        }
    }
    
    private static List<PDFFileInfo> buscarArchivosPDF(File folder) {
    List<PDFFileInfo> pdfFiles = new ArrayList<>();
    buscarArchivosPDFRecursivamente(folder, pdfFiles);
    return pdfFiles;
    }

    private static void buscarArchivosPDFRecursivamente(File folder, List<PDFFileInfo> pdfFiles) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        buscarArchivosPDFRecursivamente(file, pdfFiles);
                    } else if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                        PDFFileInfo fileInfo = obtenerInformacionPDF(file);
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
        PDDocument document = Loader.loadPDF(pdfFile);
        PDDocumentInformation info = document.getDocumentInformation();
        
        String name = pdfFile.getName();
        String author = info.getAuthor();
        String title = info.getTitle();
        String subject = info.getSubject();
        String keywords = info.getKeywords();
        String fileType = "PDF"; 
        float pdfVersion = document.getVersion();
        String creator = info.getCreator();
        int pageCount = document.getNumberOfPages();
        long fileSize = pdfFile.length();
        List<String> images = obtenerImagenesDesdePDF(document);
        Set<String> fonts = extractFontsFromPDF(pdfFile.getAbsolutePath());
        String pageSize = getPageSize(document);

        document.close();

        // Crea una instancia de PDFFileInfo con la información recopilada
        return new PDFFileInfo(pdfFile, name, author, fileSize, pageSize, pageCount, title, subject, keywords, fileType, pdfVersion, creator, images, fonts);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
        }
    }
    
    private static List<String> obtenerImagenesDesdePDF(PDDocument document) {
    List<String> images = new ArrayList<>();
    try {
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            PDPage page = document.getPage(pageNumber);
            PDResources resources = page.getResources();
            Iterable<COSName> xObjectNames = resources.getXObjectNames();

            for (COSName xObjectName : xObjectNames) {
                if (resources.isImageXObject(xObjectName)) {
                    PDImageXObject imageXObject = (PDImageXObject) resources.getXObject(xObjectName);
                    // Aquí puedes procesar la imagen, por ejemplo, guardarla o realizar otras operaciones
                    // Puedes obtener información adicional sobre la imagen, como su formato, ancho, alto, etc.
                    // PDImageXObject tiene métodos como getSuffix(), getWidth(), getHeight(), etc.
                    // Agrega el camino de la imagen a la lista
                    images.add(imageXObject.getCOSObject().toString());
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return images;
    }
    
    private static Set<String> extractFontsFromPDF(String pdfFilePath) throws IOException {
    Set<String> fonts = new HashSet<>();
    PDDocument document = Loader.loadPDF(new File(pdfFilePath));
    
    for (PDPage page : document.getPages()) {
        PDResources resources = page.getResources();
        for (COSName fontName : resources.getFontNames()) {
            PDFont font = resources.getFont(fontName);
            if (font instanceof PDType0Font) {
                PDType0Font type0Font = (PDType0Font) font;
                fonts.add(type0Font.getName());
            } else if (font instanceof PDType1Font) {
                PDType1Font type1Font = (PDType1Font) font;
                fonts.add(type1Font.getName());
            }
        }
    }
    document.close();
    return fonts;
    }

    private static String getPageSize(PDDocument file) {
    DecimalFormat df = new DecimalFormat("###.#");
    PDPage page = file.getPage(0);

    String pageSize;

    PDRectangle mediaBox = page.getMediaBox();

    double width = mediaBox.getWidth() / 72;
    double height = mediaBox.getHeight() / 72;

    if (((Objects.equals(df.format(width), "8.5")) || ((Objects.equals(df.format(height), "11.0"))))) {
        pageSize = "Carta";
    } else if (((Objects.equals(df.format(width), "8.3")) || ((Objects.equals(df.format(height), "11.8"))))) {
        pageSize = "Oficio";
    } else {
        pageSize = df.format(width) + " x " + df.format(height);
    }
    return pageSize;
    }

    private static void guardarInformacionEnArchivo(List<PDFFileInfo> pdfFiles) {
    try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("pdfInfo.dat"))) {
        outputStream.writeObject(pdfFiles);
        
        // Guardar la información en un archivo CSV
        guardarInformacionEnCSV(pdfFiles);
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private static void guardarInformacionEnCSV(List<PDFFileInfo> pdfFiles) {
        try (FileWriter writer = new FileWriter(csvFileName)) {
            // Escribir el encabezado CSV
            writer.write("Nombre,Autor,Tamaño de Archivo (bytes),Tamaño de Página,Páginas,Título,Asunto,Palabras Clave,Tipo de Archivo,Versión de PDF,Aplicación por la que fue creada,Lista de Imágenes en el Documento,Lista de Fuentes en el Documento\n");

            // Llenar el archivo CSV con los datos de los archivos PDF
            for (PDFFileInfo fileInfo : pdfFiles) {
                StringBuilder line = new StringBuilder();
                line.append(fileInfo.getName()).append(",");
                line.append(fileInfo.getAuthor()).append(",");
                line.append(fileInfo.getFileSize()).append(",");
                line.append(fileInfo.getPageSize()).append(",");
                line.append(fileInfo.getPageCount()).append(",");
                line.append(fileInfo.getTitle()).append(",");
                line.append(fileInfo.getSubject()).append(",");
                line.append(fileInfo.getKeywords()).append(",");
                line.append(fileInfo.getFileType()).append(",");
                line.append(fileInfo.getPdfVersion()).append(",");
                line.append(fileInfo.getCreator()).append(",");
                line.append(fileInfo.getImages()).append(",");
                line.append(fileInfo.getFonts()).append(",");
                writer.write(line.toString() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void continuarMismaRuta() {
    // Cargar la información desde el archivo
    List<PDFFileInfo> pdfFiles = cargarInformacionDesdeArchivo();

    // Construir una cadena de texto con la información de los archivos PDF
    StringBuilder infoText = new StringBuilder();
    for (PDFFileInfo fileInfo : pdfFiles) {
        infoText.append("Nombre: ").append(fileInfo.getName()).append(", ");
        infoText.append("Autor: ").append(fileInfo.getAuthor()).append(", ");
        infoText.append("Tamaño de Archivo: ").append(fileInfo.getFileSize()).append(" bytes, ");
        infoText.append("Tamaño de Página: ").append(fileInfo.getPageSize()).append(", ");
        infoText.append("Páginas: ").append(fileInfo.getPageCount()).append(", ");
        infoText.append("Titulo: ").append(fileInfo.getTitle()).append(", ");
        infoText.append("Asunto: ").append(fileInfo.getSubject()).append(", ");
        infoText.append("Palabras Clave: ").append(fileInfo.getKeywords()).append(", ");
        infoText.append("Tipo de Archivo: ").append(fileInfo.getFileType()).append(", ");
        infoText.append("Versión de PDF: ").append(fileInfo.getPdfVersion()).append(", ");
        infoText.append("Aplicación por la que fue creada: ").append(fileInfo.getCreator()).append(", ");
        infoText.append("Lista de Imágenes en el Documento: ").append(fileInfo.getImages()).append(", ");
        infoText.append("Lista de Fuentes en el Documento: ").append(fileInfo.getFonts()).append(", ");

        infoText.append("\n"); // Agrega un salto de línea entre cada archivo
    }

    // Muestra la cadena de texto en un cuadro de diálogo
    JOptionPane.showMessageDialog(null, "Información cargada desde el archivo:\n" + infoText.toString());
    }

    
    private static List<PDFFileInfo> cargarInformacionDesdeArchivo() {
        List<PDFFileInfo> pdfFiles = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("pdfInfo.dat"))) {
            pdfFiles = (List<PDFFileInfo>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pdfFiles;
    }
}
