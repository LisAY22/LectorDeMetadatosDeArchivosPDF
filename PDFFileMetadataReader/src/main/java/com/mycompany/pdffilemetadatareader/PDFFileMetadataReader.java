package com.mycompany.pdffilemetadatareader;

import com.formdev.flatlaf.FlatDarkLaf;

import java.text.DecimalFormat;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.io.*;
import java.io.FileOutputStream;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


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
                // Si es la primera vez, se ingresará la ruta
                if (isFirstTime) {
                    ingresarRuta();
                // Si no es la primera vez se abrira una ventana con dos opciones
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
    // Configurar el cuadro de diálogo para seleccionar solo directorios (carpetas)
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    // Mostrar el cuadro de diálogo y esperar a que el usuario seleccione una carpeta
    int result = fileChooser.showOpenDialog(null);
    // Verificar si el usuario seleccionó una carpeta
    if (result == JFileChooser.APPROVE_OPTION) {
        // Obtener la carpeta seleccionada por el usuario
        File selectedFolder = fileChooser.getSelectedFile();
        // Buscar archivos PDF dentro de la carpeta y sus subcarpetas, usando la función buscarArchivosPDF
        List<PDFFileInfo> pdfFiles = buscarArchivosPDF(selectedFolder);
        // Guardar la información de los archivos PDF en un archivo, usando la función guardarInformacionEnArchivo
        guardarInformacionEnArchivo(pdfFiles);
        // Mostrar un mensaje al usuario para indicar que la búsqueda se ha completado y los datos se han guardado
        JOptionPane.showMessageDialog(null, "Búsqueda completada y datos guardados.");
    }
    }

    
    private static List<PDFFileInfo> buscarArchivosPDF(File folder) {
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
    // Crea una lista para almacenar las rutas de las imágenes encontradas
    List<String> images = new ArrayList<>();
    try {
        // Itera a través de todas las páginas del documento PDF
        for (int pageNumber = 0; pageNumber < document.getNumberOfPages(); pageNumber++) {
            // Obtiene una página específica del PDF
            PDPage page = document.getPage(pageNumber);
            // Obtiene los recursos de la página, como imágenes y fuentes
            PDResources resources = page.getResources();
            // Obtiene los nombres de los objetos XObject (que pueden ser imágenes)
            Iterable<COSName> xObjectNames = resources.getXObjectNames();
            // Itera a través de los nombres de objetos XObject
            for (COSName xObjectName : xObjectNames) {
                // Verifica si el objeto XObject actual es una imagen
                if (resources.isImageXObject(xObjectName)) {
                    // Obtiene el objeto XObject de la imagen
                    PDImageXObject imageXObject = (PDImageXObject) resources.getXObject(xObjectName);
                    
                    // Agrega la representación en cadena del objeto COS a la lista de imágenes
                    // Esto puede ser útil para identificar la imagen, pero normalmente es una referencia interna
                    images.add(imageXObject.getCOSObject().toString());
                }
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    // Devuelve la lista de rutas de las imágenes encontradas en el PDF
    return images;
    }

    
    private static Set<String> extractFontsFromPDF(String pdfFilePath) throws IOException {
    // Crea un conjunto para almacenar los nombres de las fuentes únicas encontradas en el PDF
    Set<String> fonts = new HashSet<>();   
    // Carga el documento PDF desde el archivo especificado
    PDDocument document = Loader.loadPDF(new File(pdfFilePath));   
    // Itera a través de todas las páginas del documento PDF
    for (PDPage page : document.getPages()) {
        // Obtiene los recursos de la página, que pueden incluir fuentes
        PDResources resources = page.getResources();      
        // Obtiene los nombres de las fuentes utilizadas en la página
        for (COSName fontName : resources.getFontNames()) {
            // Obtiene la fuente asociada al nombre de la fuente
            PDFont font = resources.getFont(fontName);
            
            // Verifica si la fuente es de tipo PDType0Font (fuente compuesta) o PDType1Font (fuente simple)
            if (font instanceof PDType0Font) {
                PDType0Font type0Font = (PDType0Font) font;
                
                // Agrega el nombre de la fuente al conjunto de fuentes (fuentes únicas)
                fonts.add(type0Font.getName());
            } else if (font instanceof PDType1Font) {
                PDType1Font type1Font = (PDType1Font) font;
                
                // Agrega el nombre de la fuente al conjunto de fuentes (fuentes únicas)
                fonts.add(type1Font.getName());
            }
        }
    }    
    // Cierra el documento PDF
    document.close();   
    // Devuelve el conjunto de nombres de fuentes únicas encontradas en el PDF
    return fonts;
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
