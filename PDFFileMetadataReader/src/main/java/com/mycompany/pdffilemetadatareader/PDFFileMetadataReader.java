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
import java.util.List;
import java.util.Objects;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.text.PDFTextStripper;


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
        ImageIcon imageIcon = new ImageIcon("C:\\Users\\karol\\Documentos\\GitHub\\LectorDeMetadatosDeArchivosPDF\\PDF file metadata reader.png");
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
        JFrame ventanaOpciones = new JFrame("Options");
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
        String author;
        if (info.getAuthor() != null) {
            author = info.getAuthor();
        } else {
            author = "Sin autor";
        }
        String title;
        if (info.getTitle() != null){
            title = info.getTitle();
        } else {
            title = "Sin título";
        }
        String subject;
        if (info.getSubject() != null) {
            subject = info.getSubject();
        } else {
            subject = "Sin asunto";
        }
        String keywords;
        if (info.getKeywords() != null && !info.getKeywords().trim().isEmpty()) {
            keywords = info.getKeywords();
        } else {
            keywords = "Sin palabras clave";
        }
        String fileType = "PDF"; 
        float pdfVersion = document.getVersion();
        String creator;
        if (info.getCreator() != null) {
            creator = info.getCreator();
        } else {
            creator = "Sin aplicación de origen";
        }
        int pageCount = document.getNumberOfPages();
        long fileSize = pdfFile.length();
        int imagesCount = countImagesInPDF(document);
        int imagesFontsCount = countImagesWithFonts(document, "Fuente");
        String pageSize = getPageSize(document);

        document.close();

        // Crea una instancia de PDFFileInfo con la información recopilada
        return new PDFFileInfo(pdfFile, name, author, fileSize, pageSize, pageCount, title, subject, keywords, fileType, pdfVersion, creator, imagesCount,imagesFontsCount);
    } catch (IOException e) {
        e.printStackTrace();
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
        e.printStackTrace();
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
            writer.write("Name,Author,File Size (bytes),Page Size,Pages,Title,Subject,Keywords,FileType,Version,Source application,Images,Fonts\n");
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
        infoText.append("Palabras Clave: ").append(fileInfo.getKeywords()).append(",  ");
        infoText.append("Tipo de Archivo: ").append(fileInfo.getFileType()).append(", ");
        infoText.append("Versión de PDF: ").append(fileInfo.getPdfVersion()).append(", ");
        infoText.append("Aplicación por la que fue creada: ").append(fileInfo.getCreator()).append(", ");
        infoText.append("Cantidad de Imágenes en el Documento: ").append(fileInfo.getImagesCount()).append(", ");
        infoText.append("Cantidad de Fuentes de Imágenes Documento: ").append(fileInfo.getImagesFontsCount()).append(", ");

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
