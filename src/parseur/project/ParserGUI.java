package parseur.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Arrays;

public class ParserGUI extends JFrame {
    private JTextField inputField;
    private JTextArea resultArea;
    private JButton parseButton;
    private JButton clearButton;
    private JPanel examplesPanel;
    private JLabel statusLabel;
    private Timer fadeTimer;
    private Color originalBackground;
    
    // Custom colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    
    public ParserGUI() {
        setTitle("Analyseur de Phrases Françaises");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Create main content panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Create and style input panel
        createInputPanel(mainPanel);
        
        // Create and style result panel
        createResultPanel(mainPanel);
        
        // Create status label
        statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Create examples panel
        createExamplesPanel();
        
        // Add main components to frame
        add(mainPanel, BorderLayout.CENTER);
        add(examplesPanel, BorderLayout.EAST);
        
        // Initialize fade timer
        setupFadeTimer();
        
        // Set frame properties
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));
    }
    
    private void createInputPanel(JPanel mainPanel) {
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(BACKGROUND_COLOR);
        
        // Create and style instruction label
        JLabel instructionLabel = new JLabel("Entrez une phrase en français :");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setForeground(PRIMARY_COLOR);
        
        // Create and style input field
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        
        // Style buttons
        parseButton = createStyledButton("Analyser", PRIMARY_COLOR);
        clearButton = createStyledButton("Effacer", null);
        
        buttonsPanel.add(parseButton);
        buttonsPanel.add(clearButton);
        
        // Add components to input panel
        inputPanel.add(instructionLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Add action listeners
        parseButton.addActionListener(e -> parseSentence());
        clearButton.addActionListener(e -> clearAll());
        inputField.addActionListener(e -> parseSentence());
    }
    
    private void createResultPanel(JPanel mainPanel) {
        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        resultArea.setBackground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    
    private void createExamplesPanel() {
        examplesPanel = new JPanel();
        examplesPanel.setLayout(new BoxLayout(examplesPanel, BoxLayout.Y_AXIS));
        examplesPanel.setBackground(BACKGROUND_COLOR);
        examplesPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 10, 0, 0),
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Exemples",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                PRIMARY_COLOR
            )
        ));
        
        String[] examples = {
            "je fais le projet.",
            "le chat mange le fromage.",
            "un enfant lit un livre.",
            "nous voyons la voiture.",
            "elle prend une maison.",
            "hier il a mangé le fromage.",
            "les oiseaux chantent doucement.",
            "mon ami parle français."
        };
        
        for (String example : examples) {
            JButton exampleButton = new JButton(example);
            exampleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            exampleButton.setBorderPainted(false);
            exampleButton.setContentAreaFilled(false);
            exampleButton.setForeground(PRIMARY_COLOR);
            exampleButton.setFont(new Font("Arial", Font.PLAIN, 12));
            exampleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            exampleButton.addActionListener(e -> {
                inputField.setText(example);
                inputField.requestFocus();
            });
            
            // Hover effect
            exampleButton.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    exampleButton.setFont(exampleButton.getFont().deriveFont(Font.BOLD));
                }
                
                public void mouseExited(MouseEvent e) {
                    exampleButton.setFont(exampleButton.getFont().deriveFont(Font.PLAIN));
                }
            });
            
            examplesPanel.add(exampleButton);
            examplesPanel.add(Box.createVerticalStrut(5));
        }
    }
    
    private JButton createStyledButton(String text, Color mainColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        if (mainColor != null) {
            button.setBackground(mainColor);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.DARK_GRAY);
        }
        
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (mainColor != null) {
                    button.setBackground(mainColor.brighter());
                } else {
                    button.setBackground(Color.LIGHT_GRAY);
                }
            }
            
            public void mouseExited(MouseEvent e) {
                if (mainColor != null) {
                    button.setBackground(mainColor);
                } else {
                    button.setBackground(Color.WHITE);
                }
            }
        });
        
        return button;
    }
    
    private void setupFadeTimer() {
        fadeTimer = new Timer(50, null);
        fadeTimer.setRepeats(true);
    }
    
    private void showStatusMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Reset and restart fade effect
        fadeTimer.stop();
        fadeTimer = new Timer(50, new ActionListener() {
            float alpha = 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                alpha -= 0.05f;
                if (alpha <= 0) {
                    fadeTimer.stop();
                    statusLabel.setText(" ");
                } else {
                    statusLabel.setForeground(new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        (int)(alpha * 255)
                    ));
                }
            }
        });
        fadeTimer.start();
    }
    
    private void clearAll() {
        inputField.setText("");
        resultArea.setText("");
        showStatusMessage("Champs effacés", Color.GRAY);
        inputField.requestFocus();
    }
    
    private void parseSentence() {
        String sentence = inputField.getText().trim();
        if (sentence.isEmpty()) {
            showStatusMessage("Veuillez entrer une phrase à analyser", ERROR_COLOR);
            return;
        }
        
        RecursiveParser parser = new RecursiveParser(sentence);
        boolean isValid = parser.parse();
        
        StringBuilder result = new StringBuilder();
        result.append("Phrase analysée: ").append(sentence).append("\n\n");
        result.append("Résultat: ");
        
        if (isValid) {
            result.append("Valide\n");
            showStatusMessage("Analyse complétée avec succès", SUCCESS_COLOR);
        } else {
            result.append("Invalide\n");
            showStatusMessage("La phrase est invalide", ERROR_COLOR);
        }
        
        resultArea.setText(result.toString());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Try to set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Set custom colors for components
                UIManager.put("TextField.caretForeground", PRIMARY_COLOR);
                UIManager.put("TextArea.caretForeground", PRIMARY_COLOR);
                UIManager.put("TextField.selectionBackground", PRIMARY_COLOR.brighter());
                UIManager.put("TextArea.selectionBackground", PRIMARY_COLOR.brighter());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new ParserGUI().setVisible(true);
        });
    }
}