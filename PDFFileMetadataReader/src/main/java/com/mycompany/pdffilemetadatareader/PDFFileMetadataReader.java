package com.mycompany.pdffilemetadatareader;
import com.formdev.flatlaf.FlatDarkLaf; // Importa la clase de estilo FlatLaf

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PDFFileMetadataReader {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Establece FlatLaf como el estilo de la interfaz gráfica
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("PDF File Metadata Reader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setResizable(false); // La ventana no es redimensionable

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
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica cuando se presiona el botón
                JOptionPane.showMessageDialog(null, "Botón Continuar presionado");
            }
        });
        buttonPanel.add(continueButton);

        // Panel principal que contiene el espacio para la imagen y el botón
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imageLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Agregar el panel principal al marco
        frame.getContentPane().add(mainPanel);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        // Hacer visible la ventana
        frame.setVisible(true);
    }
}
