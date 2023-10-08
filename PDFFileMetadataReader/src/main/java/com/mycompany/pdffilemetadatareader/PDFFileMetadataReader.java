package com.mycompany.pdffilemetadatareader;
import com.formdev.flatlaf.FlatDarkLaf; // Importar la clase de estilo FlatLaf que se encuentra almacenada de manera local

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.Loader;

public class PDFFileMetadataReader {

    public static void main(String[] args) {
        // Establecer FlatLaf como el estilo de la interfaz gráfica
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

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
                mostrarVentanaOpciones();
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

            String title = info.getTitle();
            String subject = info.getSubject();
            String keywords = info.getKeywords();
            String fileType = "PDF"; // Tipo de archivo
            float pdfVersion = document.getVersion();
            String creator = info.getAuthor();
            int pageCount = document.getNumberOfPages();

            long fileSize = pdfFile.length();

            document.close();

            return new PDFFileInfo(pdfFile, fileSize, pageCount, title, subject, keywords, fileType, pdfVersion, creator);
            }   
        catch (IOException e) {
            e.printStackTrace();
            return null;
            }
    }

    private static void guardarInformacionEnArchivo(List<PDFFileInfo> pdfFiles) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("pdfInfo.dat"))) {
            outputStream.writeObject(pdfFiles);
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
        infoText.append("Nombre: ").append(fileInfo.getTitle()).append(", ");
        infoText.append("Tamaño: ").append(fileInfo.getFileSize()).append(" bytes, ");
        infoText.append("Páginas: ").append(fileInfo.getPageCount()).append(", ");

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