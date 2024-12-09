package parseur.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class ParserGUI extends JFrame {
    private JTextField inputField;
    private JTextArea resultArea;
    private JButton parseButton;
    private JButton clearButton;
    private JPanel examplesPanel;

    public ParserGUI() {
        setTitle("French Sentence Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        ((JPanel)getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel instructionLabel = new JLabel("Entrez une phrase en français:");
        inputPanel.add(instructionLabel, BorderLayout.NORTH);
        inputPanel.add(inputField, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        parseButton = new JButton("Analyser");
        clearButton = new JButton("Effacer");
        buttonsPanel.add(parseButton);
        buttonsPanel.add(clearButton);
        inputPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Create result area
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Create examples panel
        createExamplesPanel();

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(examplesPanel, BorderLayout.SOUTH);

        // Add action listeners
        parseButton.addActionListener(e -> parseSentence());
        clearButton.addActionListener(e -> {
            inputField.setText("");
            resultArea.setText("");
        });

        inputField.addActionListener(e -> parseSentence());

        // Set frame properties
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(500, 400));
    }

    private void createExamplesPanel() {
        examplesPanel = new JPanel();
        examplesPanel.setLayout(new BoxLayout(examplesPanel, BoxLayout.Y_AXIS));
        examplesPanel.setBorder(BorderFactory.createTitledBorder("Exemples de phrases valides:"));

        String[] examples = {
            "je fais le projet",
            "le chat mange le fromage",
            "un enfant lit un livre",
            "nous voyons la voiture",
            "elle prend une maison",
            "hier il a mangé le fromage"
        };

        for (String example : examples) {
            JButton exampleButton = new JButton(example);
            exampleButton.setAlignmentX(Component.LEFT_ALIGNMENT);
            exampleButton.setBorderPainted(false);
            exampleButton.setContentAreaFilled(false);
            exampleButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            exampleButton.addActionListener(e -> inputField.setText(example));
            examplesPanel.add(exampleButton);
        }
    }

    private void parseSentence() {
        String sentence = inputField.getText().trim();
        if (sentence.isEmpty()) {
            resultArea.setText("Veuillez entrer une phrase à analyser.");
            return;
        }

        RecursiveParser parser = new RecursiveParser(sentence);
        boolean isValid = parser.parse();

        StringBuilder result = new StringBuilder();
        result.append("Phrase: ").append(sentence).append("\n");
        result.append("Résultat: ").append(isValid ? "Valide ✓" : "Invalide ✗").append("\n\n");

        resultArea.setText(result.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new ParserGUI().setVisible(true);
        });
    }
}
