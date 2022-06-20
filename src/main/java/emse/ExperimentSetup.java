package emse;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import emse.input.ExpressionGenerationSource;
import emse.input.ExpressionSource;
import emse.models.Datapoint;
import emse.models.Expression;
import emse.models.ExpressionGenerationParameters;
import emse.models.Method;
import emse.output.DatapointWriter;

public class ExperimentSetup extends JFrame {

    private static final String TITLE = "EMSE Experiment";

    private final ExpressionSource expressionSource = new ExpressionGenerationSource();
    private final TimeTracker timeTracker = new TimeTracker();
    private final DatapointWriter datapointWriter = new DatapointWriter();

    private Expression currentExpression;

    private final JPanel panel;
    private RSyntaxTextArea textArea;


    public ExperimentSetup() {

        panel = new JPanel(new BorderLayout());


        JPanel preview = new JPanel(new BorderLayout());
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(event -> {
            panel.removeAll();
            addTextAreaTo(panel);
            addButtonPanelTo(panel);
            repaint();
            pack();
            setLocationRelativeTo(null);
            start();
        });
        preview.add(startButton);
        panel.add(preview);
        setContentPane(panel);

        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    private void addTextAreaTo(final JPanel panel) {
        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setEditable(false);
        final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    private void addButtonPanelTo(final JPanel panel) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        final JButton notPrintedButton = new JButton("Not Printed");
        notPrintedButton.addActionListener(event -> next(false));
        buttonPanel.add(notPrintedButton);

        final JButton printedButton = new JButton("Printed");
        printedButton.addActionListener(event -> next(true));
        buttonPanel.add(printedButton);

        panel.add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void start() {
        currentExpression = expressionSource.getNext();
        textArea.setText(currentExpression.template.expression);
        timeTracker.reset();
    }

    private void next(boolean printedAnswer) {
        final int id = currentExpression.id;
        final Method method = currentExpression.method;
        final long timePassed = timeTracker.getTimePassed();
        final boolean correct = printedAnswer == currentExpression.printing;
        final Datapoint datapoint = new Datapoint(id, method, complexity, numberOfConjunctions, numberOfParameters, result, timePassed, correct);

        datapointWriter.writeDatapoint(datapoint);

        start();
    }

    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(() -> new ExperimentSetup().setVisible(true));
    }

}