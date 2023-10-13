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
import java.util.Comparator;
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
        continueButton.setPreferredSize(new Dimension(200, 50)); // Ajusta el tamaño del botón
        
        
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                // Si es la primera vez, se ingresará la ruta
                if (isFirstTime) {
                    ingresarRuta();
                    mostrarVentanaOpciones();
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
        // Cargar información de archivos PDF desde un archivo
        List<PDFFileInfo> pdfFilesOriginal = cargarInformacionDesdeArchivo();
        List<PDFFileInfo> pdfFiles = cargarInformacionDesdeArchivo(); // Hacer una copia de la lista original

        // Crear una ventana para mostrar los archivos PDF
        JFrame ventanaVistaArchivos = new JFrame("File view");
        ventanaVistaArchivos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configurar el comportamiento de cierre
        ventanaVistaArchivos.setSize(900, 400); // Configurar tamaño de la ventana
        ventanaVistaArchivos.setResizable(false); // Evitar que la ventana sea redimensionable

        // Panel para mostrar los archivos PDF como botones en una cuadrícula
        JPanel pdfButtonPanel = new JPanel(new GridBagLayout()); // Usar GridBagLayout para organizar los botones
        GridBagConstraints gbc = new GridBagConstraints(); // Configuración del GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Ajustar espacios entre los botones

        // Crear botones para cada archivo PDF y agregarlos al panel
        for (PDFFileInfo fileInfo : pdfFiles) {
            JButton pdfButton = new JButton(fileInfo.getName() + "  |  Autor: " + fileInfo.getAuthor() + "  |  Asunto: " + fileInfo.getSubject());
            pdfButton.setPreferredSize(new Dimension(700, 40)); // Configurar el tamaño del botón
            pdfButton.addActionListener(e -> mostrarInformacionPDF(fileInfo)); // Agregar acción al hacer clic en el botón
            pdfButtonPanel.add(pdfButton, gbc); // Agregar botón al panel
            gbc.gridy++; // Cambiar de fila en el grid
        }

        // Panel para los botones de ordenar
        JPanel orderButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Usar FlowLayout para organizar los botones de ordenar
        orderButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Ajustar espacios alrededor del panel

        // Crear botones para ordenar por nombre, autor y asunto, y agregarlos al panel
        JButton orderAuthorButton = new JButton("Ordenar por Autor");
        // Configurar apariencia del botón
        orderAuthorButton.setBackground(new Color(232, 36, 36));
        orderAuthorButton.setPreferredSize(new Dimension(150, 30));
        orderAuthorButton.setForeground(Color.WHITE);
        orderAuthorButton.setOpaque(true);
        orderAuthorButton.setBorderPainted(false);
        orderAuthorButton.setFocusPainted(false);

        JButton orderSubjectButton = new JButton("Ordenar por Asunto");
        // Configurar apariencia del botón
        orderSubjectButton.setBackground(new Color(232, 36, 36));
        orderSubjectButton.setPreferredSize(new Dimension(150, 30));
        orderSubjectButton.setForeground(Color.WHITE);
        orderSubjectButton.setOpaque(true);
        orderSubjectButton.setBorderPainted(false);
        orderSubjectButton.setFocusPainted(false);

        JButton orderNameButton = new JButton("Ordenar por Nombre");
        // Configurar apariencia del botón
        orderNameButton.setBackground(new Color(232, 36, 36));
        orderNameButton.setPreferredSize(new Dimension(150, 30));
        orderNameButton.setForeground(Color.WHITE);
        orderNameButton.setOpaque(true);
        orderNameButton.setBorderPainted(false);
        orderNameButton.setFocusPainted(false);

        // Agregar botones al panel de botones de ordenar
        orderButtonPanel.add(orderNameButton);
        orderButtonPanel.add(orderAuthorButton);
        orderButtonPanel.add(orderSubjectButton);

        // Agregar acciones para los botones de ordenar
        // Acción para ordenar por nombre
        orderNameButton.addActionListener(e -> {
            pdfFiles.clear();
            pdfFiles.addAll(pdfFilesOriginal); // Restaurar el orden original
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualizar los botones en la interfaz gráfica
        });

        // Acción para ordenar por autor
        orderAuthorButton.addActionListener(e -> {
            pdfFiles.sort(Comparator.comparing(PDFFileInfo::getAuthor, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(PDFFileInfo::getSubject, Comparator.nullsLast(Comparator.naturalOrder())));
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualizar los botones en la interfaz gráfica
        });

        // Acción para ordenar por asunto
        orderSubjectButton.addActionListener(e -> {
            pdfFiles.sort(Comparator.comparing(PDFFileInfo::getSubject, Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(PDFFileInfo::getAuthor, Comparator.nullsLast(Comparator.naturalOrder())));
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualizar los botones en la interfaz gráfica
        });

        // Configurar barra de desplazamiento para el panel de botones
        JScrollPane scrollPane = new JScrollPane(pdfButtonPanel); // Agregar panel de botones a un JScrollPane
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Ajustar el valor de la velocidad de desplazamiento

        // Agregar paneles a la ventana principal
        ventanaVistaArchivos.add(orderButtonPanel, BorderLayout.NORTH); // Agregar panel de botones de ordenar en la parte superior
        ventanaVistaArchivos.getContentPane().add(scrollPane); // Agregar panel de botones de archivos PDF con barra de desplazamiento

        // Configurar posición y visibilidad de la ventana principal
        ventanaVistaArchivos.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        ventanaVistaArchivos.setVisible(true); // Hacer la ventana visible
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
    
    private static void actualizarBotonesPDF(List<PDFFileInfo> pdfFiles, JPanel pdfButtonPanel) {
        // Limpiar todos los botones existentes en el panel
        pdfButtonPanel.removeAll();

        // Configurar la disposición de los nuevos botones
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Ajustar espacios entre los botones

        // Crear y agregar nuevos botones para cada archivo PDF en la lista
        for (PDFFileInfo fileInfo : pdfFiles) {
            // Crear un botón con el nombre, autor y asunto del archivo PDF
            JButton pdfButton = new JButton(fileInfo.getName() + "  |  Autor: " + fileInfo.getAuthor() + "  |  Asunto: " + fileInfo.getSubject());
            pdfButton.setPreferredSize(new Dimension(700, 40)); // Configurar el tamaño del botón
            pdfButton.addActionListener(e -> mostrarInformacionPDF(fileInfo)); // Agregar acción al hacer clic en el botón
            pdfButtonPanel.add(pdfButton, gbc); // Agregar botón al panel
            gbc.gridy++; // Moverse a la siguiente fila en el grid
        }

        // Validar y repintar el panel para reflejar los cambios
        pdfButtonPanel.revalidate();
        pdfButtonPanel.repaint();
    }

    private static void mostrarInformacionPDF(PDFFileInfo fileInfo) {
        // Crear un área de texto para mostrar la información
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false); // Hacer el área de texto no editable
        infoTextArea.setFont(new Font("Arial", Font.PLAIN, 14)); // Configurar la fuente y tamaño del texto
        infoTextArea.setMargin(new Insets(10, 10, 10, 10)); // Configurar los márgenes del área de texto

        // Configurar el texto con la información del archivo PDF
        infoTextArea.setText("Nombre: " + fileInfo.getName() + "\n"
                + "Autor: " + fileInfo.getAuthor() + "\n"
                + "Asunto: " + fileInfo.getSubject() + "\n\n"
                + "Tamaño del Archivo: " + fileInfo.getFileSize() + " bytes\n"
                + "Tamaño de la Página: " + fileInfo.getPageSize() + "\n"
                + "Número de Páginas: " + fileInfo.getPageCount() + "\n\n"
                + "Detalles del PDF:\n"
                + "   - Versión: " + fileInfo.getPdfVersion() + "\n"
                + "   - Tipo de Archivo: " + fileInfo.getFileType() + "\n"
                + "   - Creador: " + fileInfo.getCreator() + "\n\n"
                + "Imágenes:\n"
                + "   - Cantidad: " + fileInfo.getImagesCount() + "\n"
                + "   - Cantidad de Fuentes de Imágenes: " + fileInfo.getImagesFontsCount() + "\n\n"
                + "Palabras Clave: " + fileInfo.getKeywords());

        // Crear una nueva ventana para mostrar la información
        JFrame infoFrame = new JFrame("File Information");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Configurar el comportamiento de cierre
        infoFrame.setSize(400, 300); // Configurar tamaño de la ventana
        infoFrame.setResizable(false); // Evitar que la ventana sea redimensionable
        infoFrame.getContentPane().add(new JScrollPane(infoTextArea)); // Agregar el área de texto con barra de desplazamiento
        infoFrame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        infoFrame.setVisible(true); // Hacer la ventana visible
    }

}
