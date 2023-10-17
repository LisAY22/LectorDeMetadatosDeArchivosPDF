package com.mycompany.pdffilemetadatareader;


import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.AttributeSet;


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
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
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
        ingresarRutaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton continuarMismaRutaButton = new JButton("Continuar con la misma ruta");
        continuarMismaRutaButton.setBackground(new Color(232, 36, 36));
        continuarMismaRutaButton.setForeground(Color.WHITE);
        continuarMismaRutaButton.setOpaque(true);
        continuarMismaRutaButton.setBorderPainted(false);
        continuarMismaRutaButton.setFocusPainted(false);
        continuarMismaRutaButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
            pdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            Timer tooltipTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pdfButton.setToolTipText(null); // Ocultar el tooltip
                    ((Timer) e.getSource()).stop(); // Detener el temporizador
                }
            });

            pdfButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    pdfButton.setToolTipText("Abrir " + fileInfo.getName());
                    tooltipTimer.restart(); // Reiniciar el temporizador al mover el mouse
                }
            });

            
            pdfButton.addActionListener(e -> mostrarInformacionPDF(fileInfo, pdfFiles)); // Agregar acción al hacer clic en el botón
            pdfButtonPanel.add(pdfButton, gbc); // Agregar botón al panel
            gbc.gridy++; // Cambiar de fila en el grid
        }

        // Panel principal que contendrá los dos subpaneles
        JPanel mainPanel = new JPanel(new BorderLayout());
        

        // Panel para los botones de ordenar (a la derecha)
        JPanel orderButtonPanel = new JPanel();
        orderButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        orderButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        // Botones de ordenar
        JButton orderAuthorButton = new JButton("Ordenar por Autor");
        // Configurar apariencia del botón
        orderAuthorButton.setBackground(new Color(232, 36, 36));
        orderAuthorButton.setPreferredSize(new Dimension(150, 30));
        orderAuthorButton.setForeground(Color.WHITE);
        orderAuthorButton.setOpaque(true);
        orderAuthorButton.setBorderPainted(false);
        orderAuthorButton.setFocusPainted(false);
        orderAuthorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton orderSubjectButton = new JButton("Ordenar por Asunto");
        // Configurar apariencia del botón
        orderSubjectButton.setBackground(new Color(232, 36, 36));
        orderSubjectButton.setPreferredSize(new Dimension(150, 30));
        orderSubjectButton.setForeground(Color.WHITE);
        orderSubjectButton.setOpaque(true);
        orderSubjectButton.setBorderPainted(false);
        orderSubjectButton.setFocusPainted(false);
        orderSubjectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton orderNameButton = new JButton("Ordenar por Nombre");
        // Configurar apariencia del botón
        orderNameButton.setBackground(new Color(232, 36, 36));
        orderNameButton.setPreferredSize(new Dimension(150, 30));
        orderNameButton.setForeground(Color.WHITE);
        orderNameButton.setOpaque(true);
        orderNameButton.setBorderPainted(false);
        orderNameButton.setFocusPainted(false);
        orderNameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Panel para el botón de regresar (a la izquierda)
        JPanel backButtonPanel = new JPanel();
        backButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        JButton backButton = new JButton("←");
        backButton.setFont(backButton.getFont().deriveFont(Font.PLAIN, 24));
        backButton.setBackground(new Color(232, 36, 36));
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Timer tooltipTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    backButton.setToolTipText(null); // Ocultar el tooltip
                    ((Timer) e.getSource()).stop(); // Detener el temporizador
                }
            });

            backButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    backButton.setToolTipText("Regresar a Options");
                    tooltipTimer.restart(); // Reiniciar el temporizador al mover el mouse
                }
            });

        // Agregar botones a los paneles respectivos
        orderButtonPanel.add(orderNameButton);
        orderButtonPanel.add(orderAuthorButton);
        orderButtonPanel.add(orderSubjectButton);
        backButtonPanel.add(backButton);

        // Agregar los paneles al panel principal
        mainPanel.add(backButtonPanel, BorderLayout.WEST);
        mainPanel.add(orderButtonPanel, BorderLayout.CENTER);
        
        backButton.addActionListener (e -> {
            ventanaOpciones.setVisible(true);
            ventanaVistaArchivos.dispose();
            
        });

        // Agregar acciones para los botones de ordenar
        // Acción para ordenar por nombre
        orderNameButton.addActionListener(e -> {
            // Ordena la lista pdfFiles por el nombre del archivo (case-insensitive)
            pdfFiles.sort((pdfFileInfo1, pdfFileInfo2)
                    -> String.CASE_INSENSITIVE_ORDER.compare(pdfFileInfo1.getName(), pdfFileInfo2.getName()));
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualiza los botones en la interfaz gráfica
        });

        // Acción para ordenar por autor
        orderAuthorButton.addActionListener(e -> {
            // Ordena la lista pdfFiles por el nombre del autor (case-insensitive)
            pdfFiles.sort((pdfFileInfo1, pdfFileInfo2)
                    -> String.CASE_INSENSITIVE_ORDER.compare(pdfFileInfo1.getAuthor(), pdfFileInfo2.getAuthor()));
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualiza los botones en la interfaz gráfica
        });
        
        // Acción para ordenar por asunto
        orderSubjectButton.addActionListener(e -> {
            // Ordena la lista pdfFiles por el asunto del archivo (case-insensitive)
            pdfFiles.sort((pdfFileInfo1, pdfFileInfo2)
                    -> String.CASE_INSENSITIVE_ORDER.compare(pdfFileInfo1.getSubject(), pdfFileInfo2.getSubject()));
            actualizarBotonesPDF(pdfFiles, pdfButtonPanel); // Actualiza los botones en la interfaz gráfica
        });


        // Configurar barra de desplazamiento para el panel de botones
        JScrollPane scrollPane = new JScrollPane(pdfButtonPanel); // Agregar panel de botones a un JScrollPane
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Ajustar el valor de la velocidad de desplazamiento

        // Agregar paneles a la ventana principal
        ventanaVistaArchivos.add(mainPanel, BorderLayout.NORTH); // Agregar panel de botones de ordenar en la parte superior
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
            pdfButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            Timer tooltipTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    pdfButton.setToolTipText(null); // Ocultar el tooltip
                    ((Timer) e.getSource()).stop(); // Detener el temporizador
                }
            });

            pdfButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    pdfButton.setToolTipText("Abrir " + fileInfo.getName());
                    tooltipTimer.restart(); // Reiniciar el temporizador al mover el mouse
                }
            });
            
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
        infoFrame.setSize(500, 400);
        infoFrame.setResizable(false);
        infoFrame.setLocationRelativeTo(null);

        // Crear un panel para contener los TextField
        JPanel TextFieldPanel = new JPanel();
        TextFieldPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        
        JButton backButton = new JButton("←");
        backButton.setFont(backButton.getFont().deriveFont(Font.PLAIN, 24));
        backButton.setBackground(new Color(232, 36, 36));
        backButton.setPreferredSize(new Dimension(50, 30));
        backButton.setForeground(Color.WHITE);
        backButton.setOpaque(true);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        Timer tooltipTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    backButton.setToolTipText(null); // Ocultar el tooltip
                    ((Timer) e.getSource()).stop(); // Detener el temporizador
                }
            });

            backButton.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    backButton.setToolTipText("Regresar a File view");
                    tooltipTimer.restart(); // Reiniciar el temporizador al mover el mouse
                }
            });
        
        backButtonPanel.add(backButton);
        
        backButton.addActionListener (e -> {
            continuarMismaRuta();
            infoFrame.dispose();
        });
        
        JTextField nombreTextField = new JTextField("Nombre: " + fileInfo.getName());
        nombreTextField.setPreferredSize(new Dimension(400, 30));
        nombreTextField.setEditable(false);
        nombreTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        nombreTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem editarItem = new JMenuItem("Editar nombre");
                    editarItem.addActionListener(actionEvent -> {
                        String nuevoNombre = JOptionPane.showInputDialog(infoFrame, "Editar Nombre", fileInfo.getName().replace(".pdf", ""));
                        if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                            fileInfo.setName(nuevoNombre + ".pdf");
                            nombreTextField.setText("Nombre: " + fileInfo.getName());
                            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                            JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                            actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
                        }
                    });
                    popupMenu.add(editarItem);
                    popupMenu.show(nombreTextField, e.getX(), e.getY());
                }
            }
        });
        
        JTextField tituloTextField = new JTextField("Título: " + fileInfo.getTitle());
        tituloTextField.setPreferredSize(new Dimension(400, 30));
        tituloTextField.setEditable(false);
        tituloTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        tituloTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem editarItem = new JMenuItem("Editar título");
                    editarItem.addActionListener(actionEvent -> {
                        String nuevoTitulo = JOptionPane.showInputDialog(infoFrame, "Editar título", fileInfo.getTitle());
                        if (nuevoTitulo != null && !nuevoTitulo.isEmpty()) {
                            fileInfo.setTitle(nuevoTitulo);
                            tituloTextField.setText("Titulo: " + fileInfo.getTitle());
                            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                            JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                            actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
                        }
                    });
                    popupMenu.add(editarItem);
                    popupMenu.show(tituloTextField, e.getX(), e.getY());
                }
            }
        });
        
        JTextField autorTextField = new JTextField("Autor: " + fileInfo.getAuthor());
        autorTextField.setPreferredSize(new Dimension(400, 30));
        autorTextField.setEditable(false);
        autorTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        autorTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem editarItem = new JMenuItem("Editar autor");
                    editarItem.addActionListener(actionEvent -> {
                        String nuevoAutor = JOptionPane.showInputDialog(infoFrame, "Editar Autor", fileInfo.getAuthor());
                        if (nuevoAutor != null && !nuevoAutor.isEmpty()) {
                            fileInfo.setAuthor(nuevoAutor);
                            autorTextField.setText("Autor: " + fileInfo.getAuthor());
                            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                            JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                            actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
                        }
                    });
                    popupMenu.add(editarItem);
                    popupMenu.show(autorTextField, e.getX(), e.getY());
                }
            }
        });

        JTextField asuntoTextField = new JTextField("Asunto: " + fileInfo.getSubject());
        asuntoTextField.setPreferredSize(new Dimension(400, 30));
        asuntoTextField.setEditable(false);
        asuntoTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        asuntoTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem editarItem = new JMenuItem("Editar asunto");
                    editarItem.addActionListener(actionEvent -> {
                        String nuevoAsunto = JOptionPane.showInputDialog(infoFrame, "Editar Asunto", fileInfo.getSubject());
                        if (nuevoAsunto != null && !nuevoAsunto.isEmpty()) {
                            fileInfo.setSubject(nuevoAsunto);
                            asuntoTextField.setText("Asunto: " + fileInfo.getSubject());
                            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                            JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                            actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
                        }
                    });
                    popupMenu.add(editarItem);
                    popupMenu.show(asuntoTextField, e.getX(), e.getY());
                }
            }
        });

        
        JTextField palabrasClaveTextField = new JTextField("Palabras Clave: " + fileInfo.getKeywords());
        palabrasClaveTextField.setPreferredSize(new Dimension(400, 30));
        palabrasClaveTextField.setEditable(false);
        palabrasClaveTextField.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        palabrasClaveTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu popupMenu = new JPopupMenu();
                    JMenuItem editarItem = new JMenuItem("Editar palabras clave");
                    editarItem.addActionListener(actionEvent -> {
                        String nuevasPalabrasClave = JOptionPane.showInputDialog(infoFrame, "Editar palabras clave", fileInfo.getKeywords());
                        if (nuevasPalabrasClave != null && !nuevasPalabrasClave.isEmpty()) {
                            fileInfo.setKeywords(nuevasPalabrasClave);
                            palabrasClaveTextField.setText("Palabras Clave: " + fileInfo.getKeywords());
                            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
                            JPanel pdfButtonPanel = new JPanel(new GridBagLayout());
                            actualizarBotonesPDF(pdfFiles, pdfButtonPanel);
                        }
                    });
                    popupMenu.add(editarItem);
                    popupMenu.show(palabrasClaveTextField, e.getX(), e.getY());
                }
            }
        });

        JTextField tamañoArchivoTextField = new JTextField("Tamaño del Archivo: " + fileInfo.getFileSize() + " bytes");
        tamañoArchivoTextField.setEditable(false);
        tamañoArchivoTextField.setPreferredSize(new Dimension(400, 30));
        
        JTextField tamañoPaginaTextField = new JTextField("Tamaño de Página: " + fileInfo.getPageSize());
        tamañoPaginaTextField.setEditable(false);
        tamañoPaginaTextField.setPreferredSize(new Dimension(400, 30));

        JTextField numPaginasTextField = new JTextField("Número de Páginas: " + fileInfo.getPageCount());
        numPaginasTextField.setEditable(false);
        numPaginasTextField.setPreferredSize(new Dimension(400, 30));

        JTextField versionTextField = new JTextField ("Versión: " + fileInfo.getPdfVersion());
        versionTextField.setEditable(false);
        versionTextField.setPreferredSize(new Dimension(400, 30));

        JTextField tipoArchivoTextField = new JTextField("Tipo de Archivo: " + fileInfo.getFileType());
        tipoArchivoTextField.setEditable(false);
        tipoArchivoTextField.setPreferredSize(new Dimension(400, 30));
        
        JTextField creadorTextField = new JTextField("Aplicación con la que fue Creada: " + fileInfo.getCreator());
        creadorTextField.setEditable(false);
        creadorTextField.setPreferredSize(new Dimension(400, 30));
        
        JTextField imagenesTextField = new JTextField("Cantidad de Imágenes: " + fileInfo.getImagesCount());
        imagenesTextField.setEditable(false);
        imagenesTextField.setPreferredSize(new Dimension(400, 30));

        JTextField imagenesFuentesTextField = new JTextField("Cantidad de Fuentes de Imágenes: " + fileInfo.getImagesFontsCount());
        imagenesFuentesTextField.setEditable(false);
        imagenesFuentesTextField.setPreferredSize(new Dimension(400, 30));
        
        JButton verEditarResumenButton = new JButton("Ver y editar resumen");
        // Configurar apariencia del botón
        verEditarResumenButton.setBackground(new Color(232, 36, 36));
        verEditarResumenButton.setPreferredSize(new Dimension(150, 30));
        verEditarResumenButton.setForeground(Color.WHITE);
        verEditarResumenButton.setOpaque(true);
        verEditarResumenButton.setBorderPainted(false);
        verEditarResumenButton.setFocusPainted(false);
        verEditarResumenButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        verEditarResumenButton.addActionListener(e -> verEditarResumenPDF(fileInfo, pdfFiles));
        
        
        TextFieldPanel.add(nombreTextField, gbc);
        TextFieldPanel.add(tituloTextField, gbc);
        TextFieldPanel.add(autorTextField, gbc);
        TextFieldPanel.add(asuntoTextField, gbc);
        TextFieldPanel.add(palabrasClaveTextField, gbc);
        TextFieldPanel.add(tamañoArchivoTextField, gbc);
        TextFieldPanel.add(tamañoPaginaTextField, gbc);
        TextFieldPanel.add(numPaginasTextField, gbc);
        TextFieldPanel.add(tipoArchivoTextField, gbc);
        TextFieldPanel.add(versionTextField, gbc);
        TextFieldPanel.add(creadorTextField, gbc);
        TextFieldPanel.add(imagenesTextField, gbc);
        TextFieldPanel.add(imagenesFuentesTextField, gbc);
        TextFieldPanel.add(verEditarResumenButton);
    
        // Crear el JScrollPane y agregar el panel de botones
        JScrollPane scrollPane = new JScrollPane(TextFieldPanel); // Agregar panel de botones a un JScrollPane
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16); // Ajustar el valor de la velocidad de desplazamiento

        // Agregar el panel del boton y el JScrollPane a la ventana
        infoFrame.add(backButtonPanel, BorderLayout.NORTH);
        infoFrame.getContentPane().add(scrollPane);

        infoFrame.setVisible(true);
    }
    
    private static void verEditarResumenPDF(PDFFileInfo fileInfo, List<PDFFileInfo> pdfFiles) {
        // Crear una nueva ventana para editar el resumen
        JFrame summaryFrame = new JFrame("File Summary");
        summaryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        summaryFrame.setSize(500, 400);
        summaryFrame.setResizable(false);
        summaryFrame.setLocationRelativeTo(null);

        JTextPane resumenTextPane = new JTextPane();
        resumenTextPane.setText(fileInfo.getSummary());
        resumenTextPane.setEditable(true); // Habilita la edición del texto

        // Agregar botones para el formato de texto
        JButton boldButton = new JButton("Negrita");
        JButton italicButton = new JButton("Itálica");
        JButton underlineButton = new JButton("Subrayado");

        boldButton.addActionListener(e -> applyStyle(resumenTextPane, "bold"));
        italicButton.addActionListener(e -> applyStyle(resumenTextPane, "italic"));
        underlineButton.addActionListener(e -> applyStyle(resumenTextPane, "underline"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(boldButton);
        buttonPanel.add(italicButton);
        buttonPanel.add(underlineButton);

        // Botón para guardar las actualizaciones
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(evt -> {
            String updatedSummary = resumenTextPane.getText();
            fileInfo.setSummary(updatedSummary);
            PDFSaveInfo.guardarInformacionEnArchivo(pdfFiles, csvFileName);
            JOptionPane.showMessageDialog(summaryFrame, "Resumen guardado exitosamente.");
        });

        // Crear un JScrollPane para el JTextPane
        JScrollPane scrollPane = new JScrollPane(resumenTextPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Agregar componentes al JFrame de edición de resumen
        summaryFrame.getContentPane().setLayout(new BorderLayout());
        summaryFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        summaryFrame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        summaryFrame.getContentPane().add(saveButton, BorderLayout.SOUTH);
        summaryFrame.setVisible(true);
    }
    
    // Método para aplicar/quitar negrita, cursiva o subrayado al texto seleccionado
    private static void applyStyle(JTextPane textPane, String style) {
        StyledDocument doc = textPane.getStyledDocument();
        int start = textPane.getSelectionStart();
        int end = textPane.getSelectionEnd();
        SimpleAttributeSet attributes = new SimpleAttributeSet();
        AttributeSet originalAttributes = doc.getCharacterElement(start).getAttributes();

        switch (style) {
            case "bold" -> StyleConstants.setBold(attributes, !StyleConstants.isBold(originalAttributes));
            case "italic" -> StyleConstants.setItalic(attributes, !StyleConstants.isItalic(originalAttributes));
            case "underline" -> StyleConstants.setUnderline(attributes, !StyleConstants.isUnderline(originalAttributes));
            default -> {
            }
        }

        doc.setCharacterAttributes(start, end - start, attributes, false);
    }

}
