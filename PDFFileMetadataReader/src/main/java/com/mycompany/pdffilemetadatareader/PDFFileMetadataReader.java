package com.mycompany.pdffilemetadatareader;


import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class PDFFileMetadataReader {
    
    private static JFrame ventanaOpciones;
    private static JFrame ventanaVistaArchivos;
    private static String csvFileName = "metadata.csv"; // Nombre del archivo CSV

    public static void main(String[] args) {
        // Establecer FlatLaf como el estilo de la interfaz gráfica
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace(System.out);
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
        ventanaOpciones = new JFrame("Options");
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
        List<PDFFileInfo> pdfFiles = PDFObtainInfo.buscarArchivosPDF(selectedFolder);
        // Guardar la información de los archivos PDF en un archivo, usando la función guardarInformacionEnArchivo
        PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
        // Mostrar un mensaje al usuario para indicar que la búsqueda se ha completado y los datos se han guardado
        JOptionPane.showMessageDialog(null, "Búsqueda completada y datos guardados.");
    }
    }
    
    
    private static void continuarMismaRuta() {
        ventanaOpciones.dispose();
        // Cargar información de archivos PDF desde un archivo
        List<PDFFileInfo> pdfFilesOriginal = cargarInformacionDesdeArchivo();
        List<PDFFileInfo> pdfFiles = cargarInformacionDesdeArchivo(); // Hacer una copia de la lista original

        // Crear una ventana para mostrar los archivos PDF
        ventanaVistaArchivos = new JFrame("File view");
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
            pdfButton.addActionListener(e -> mostrarInformacionPDF(fileInfo, pdfFiles)); // Agregar acción al hacer clic en el botón
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
        
        JButton regresar = new JButton("Regresar");
        regresar.setBackground(new Color(232, 36, 36));
        regresar.setPreferredSize(new Dimension(150, 30));
        regresar.setForeground(Color.WHITE);
        regresar.setOpaque(true);
        regresar.setBorderPainted(false);
        regresar.setFocusPainted(false);

        // Agregar botones al panel de botones de ordenar
        orderButtonPanel.add(regresar);
        orderButtonPanel.add(orderNameButton);
        orderButtonPanel.add(orderAuthorButton);
        orderButtonPanel.add(orderSubjectButton);
        
        regresar.addActionListener (e -> {
            ventanaOpciones.setVisible(true);
            ventanaVistaArchivos.dispose();
            
        });

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
            e.printStackTrace(System.out);
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
            pdfButton.addActionListener(e -> mostrarInformacionPDF(fileInfo, pdfFiles)); // Agregar acción al hacer clic en el botón
            pdfButtonPanel.add(pdfButton, gbc); // Agregar botón al panel
            gbc.gridy++; // Moverse a la siguiente fila en el grid
        }

        // Validar y repintar el panel para reflejar los cambios
        pdfButtonPanel.revalidate();
        pdfButtonPanel.repaint();
    }

    private static void mostrarInformacionPDF(PDFFileInfo fileInfo, List<PDFFileInfo> pdfFiles) {
        ventanaVistaArchivos.dispose();
        // Crear una nueva ventana para mostrar la información
        JFrame infoFrame = new JFrame("File Information");
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setSize(500, 300);
        infoFrame.setResizable(false);
        infoFrame.setLocationRelativeTo(null);

        // Crear un panel para contener los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JButton regresar = new JButton("Regresar");
        regresar.setBackground(new Color(232, 36, 36));
        regresar.setPreferredSize(new Dimension(150, 30));
        regresar.setForeground(Color.WHITE);
        regresar.setOpaque(true);
        regresar.setBorderPainted(false);
        regresar.setFocusPainted(false);
        regresar.addActionListener (e -> {
            continuarMismaRuta();
            infoFrame.dispose();
        });
        
        JButton nombreButton = new JButton("Nombre: " + fileInfo.getName());
        nombreButton.setPreferredSize(new Dimension(400, 30));
        nombreButton.addActionListener(e -> {
            String nuevoNombre = JOptionPane.showInputDialog(infoFrame, "Editar Nombre", fileInfo.getName());
            if (nuevoNombre != null) {
                fileInfo.setName(nuevoNombre);
                // Actualizar el texto del botón con el nuevo nombre
                nombreButton.setText("Nombre: " + fileInfo.getName());
                // Guardar la información actualizada en los archivos
                PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                // Actualizar la interfaz gráfica para reflejar los cambios
                actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
            }
        });
        
        JButton tituloButton = new JButton("Titulo: " + fileInfo.getTitle());
        tituloButton.setPreferredSize(new Dimension(400, 30));
        tituloButton.addActionListener(e -> {
            String nuevoTitulo = JOptionPane.showInputDialog(infoFrame, "Editar Titulo", fileInfo.getTitle());
            if (nuevoTitulo != null) {
                fileInfo.setTitle(nuevoTitulo);
                tituloButton.setText("Titulo: " + fileInfo.getTitle());
                // Guardar la información actualizada en los archivos
                PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                // Actualizar la interfaz gráfica para reflejar los cambios
                actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
            }
        });
        
        JButton autorButton = new JButton("Autor: " + fileInfo.getAuthor());
        autorButton.setPreferredSize(new Dimension(400, 30));
        autorButton.addActionListener(e -> {
            String nuevoAutor = JOptionPane.showInputDialog(infoFrame, "Editar Autor", fileInfo.getAuthor());
            if (nuevoAutor != null) {
                fileInfo.setAuthor(nuevoAutor);
                autorButton.setText("Autor: " + fileInfo.getAuthor());
                // Guardar la información actualizada en los archivos
                PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                // Actualizar la interfaz gráfica para reflejar los cambios
                actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
            }
        });

        JButton asuntoButton = new JButton("Asunto: " + fileInfo.getSubject());
        asuntoButton.setPreferredSize(new Dimension(400, 30));
        asuntoButton.addActionListener(e -> {
            String nuevoAsunto = JOptionPane.showInputDialog(infoFrame, "Editar Asunto", fileInfo.getSubject());
            if (nuevoAsunto != null) {
                fileInfo.setSubject(nuevoAsunto);
                asuntoButton.setText("Asunto: " + fileInfo.getSubject());
                // Guardar la información actualizada en los archivos
                PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                // Actualizar la interfaz gráfica para reflejar los cambios
                actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
            }
        });
        
        JButton palabrasClaveButton = new JButton("Palabras Clave: " + fileInfo.getKeywords());
        palabrasClaveButton.setPreferredSize(new Dimension(400, 30));
        palabrasClaveButton.addActionListener(e -> {
            String nuevasPalabrasClave = JOptionPane.showInputDialog(infoFrame, "Editar Palabras Clave", fileInfo.getKeywords());
            if (nuevasPalabrasClave != null) {
                fileInfo.setKeywords(nuevasPalabrasClave);
                palabrasClaveButton.setText("Palabras Clave: " + fileInfo.getKeywords());
                // Guardar la información actualizada en los archivos
                PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                // Actualizar la interfaz gráfica para reflejar los cambios
                actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
            }
        });

        JLabel tamañoArchivoLabel = new JLabel("Tamaño del Archivo: " + fileInfo.getFileSize() + " bytes");
        tamañoArchivoLabel.setPreferredSize(new Dimension(400, 30));
        
        JLabel tamañoPaginaLabel = new JLabel("Tamaño de Página: " + fileInfo.getPageSize());
        tamañoPaginaLabel.setPreferredSize(new Dimension(400, 30));

        JLabel numPaginasLabel = new JLabel("Número de Páginas: " + fileInfo.getPageCount());
        numPaginasLabel.setPreferredSize(new Dimension(400, 30));

        JLabel versionLabel = new JLabel ("Versión: " + fileInfo.getPdfVersion());
        versionLabel.setPreferredSize(new Dimension(400, 30));

        JLabel tipoArchivoLabel = new JLabel("Tipo de Archivo: " + fileInfo.getFileType());
        tipoArchivoLabel.setPreferredSize(new Dimension(400, 30));
        
        JLabel creadorLabel = new JLabel("Aplicación con la que fue Creada: " + fileInfo.getCreator());
        creadorLabel.setPreferredSize(new Dimension(400, 30));
        
        JLabel imagenesLabel = new JLabel("Cantidad de Imágenes: " + fileInfo.getImagesCount());
        imagenesLabel.setPreferredSize(new Dimension(400, 30));

        JLabel imagenesFuentesLabel = new JLabel("Cantidad de Fuentes de Imágenes: " + fileInfo.getImagesFontsCount());
        imagenesFuentesLabel.setPreferredSize(new Dimension(400, 30));
        
        buttonPanel.add(regresar, gbc);
        buttonPanel.add(nombreButton, gbc);
        buttonPanel.add(tituloButton, gbc);
        buttonPanel.add(autorButton, gbc);
        buttonPanel.add(asuntoButton, gbc);
        buttonPanel.add(palabrasClaveButton, gbc);
        buttonPanel.add(tamañoArchivoLabel, gbc);
        buttonPanel.add(tamañoPaginaLabel, gbc);
        buttonPanel.add(numPaginasLabel, gbc);
        buttonPanel.add(tipoArchivoLabel, gbc);
        buttonPanel.add(versionLabel, gbc);
        buttonPanel.add(creadorLabel, gbc);
        buttonPanel.add(imagenesLabel, gbc);
        buttonPanel.add(imagenesFuentesLabel, gbc);
    
        // Crear el JScrollPane y agregar el panel de botones
        JScrollPane scrollPane = new JScrollPane(buttonPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Agregar el JScrollPane a la ventana
        infoFrame.add(scrollPane);

        infoFrame.setVisible(true);
    }

}
