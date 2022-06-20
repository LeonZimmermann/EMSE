package emse;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import emse.input.ExpressionGenerationSource;
import emse.input.ExpressionSource;
import emse.models.Datapoint;
import emse.models.Expression;
import emse.models.Method;
import emse.output.CsvDatapointWriter;
import emse.output.DatapointWriter;

public class ExperimentSetup extends JFrame {

    private static final String TITLE = "EMSE Experiment";

    private final ExpressionSource expressionSource = new ExpressionGenerationSource();
    private final DatapointWriter datapointWriter = new CsvDatapointWriter();
    private final TimeTracker timeTracker = new TimeTracker();

    private Expression currentExpression;

    private final JPanel panel;
    private RSyntaxTextArea textArea;
    private final JCheckBox withParenthesisCheckbox;

    private boolean firstExecution = true;

    public ExperimentSetup() {

        panel = new JPanel(new BorderLayout());

        JPanel preview = new JPanel(new BorderLayout());
        withParenthesisCheckbox = new JCheckBox("With Parenthesis?");
        preview.add(withParenthesisCheckbox, BorderLayout.PAGE_START);
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
        preview.add(startButton, BorderLayout.CENTER);
        panel.add(preview);
        setContentPane(panel);

        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    private void addTextAreaTo(final JPanel panel) {
        textArea = new RSyntaxTextArea(20, 200);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        textArea.setEditable(false);
        final RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

    }

    private void addButtonPanelTo(final JPanel panel) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());

        final JButton falseButton = new JButton("False");
        falseButton.addActionListener(event -> next(false));
        buttonPanel.add(falseButton);

        final JButton trueButton = new JButton("True");
        trueButton.addActionListener(event -> next(true));
        buttonPanel.add(trueButton);

        panel.add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void start() {
        final Method method = withParenthesisCheckbox.isSelected() ? Method.BOOLEAN_WITH_PARENTHESIS : Method.BOOLEAN_NO_PARENTHESIS;
        expressionSource.setMethod(method);
        currentExpression = expressionSource.getNext();
        textArea.setText(currentExpression.template.expression);
        timeTracker.reset();
    }

    private void next(boolean answer) {
        final long timePassed = timeTracker.getTimePassed();
        final boolean correct = answer == currentExpression.template.result;
        final Datapoint datapoint = new Datapoint(
                currentExpression.id,
                currentExpression.template.generationParameters.getMethod(),
                currentExpression.template.generationParameters.getComplexity(),
                currentExpression.template.generationParameters.getNumberOfConjunctions(),
                currentExpression.template.generationParameters.getNumberOfParameters(),
                currentExpression.template.result,
                timePassed,
                correct);
        // Skip first execution to prevent spike in time taken to give an answer
        if (!firstExecution) {
            datapointWriter.writeDatapoint(datapoint);
        } else {
            firstExecution = false;
        }
        start();
    }

    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(() -> new ExperimentSetup().setVisible(true));
    }

}
