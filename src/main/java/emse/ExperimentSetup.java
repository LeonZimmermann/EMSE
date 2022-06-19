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

import emse.input.CodeLoader;
import emse.models.CodeSample;
import emse.models.Datapoint;
import emse.models.Method;
import emse.output.DatapointWriter;

public class ExperimentSetup extends JFrame {

    private static final String TITLE = "EMSE Experiment";

    private final CodeLoader codeLoader = new CodeLoader();
    private final TimeTracker timeTracker = new TimeTracker();
    private final DatapointWriter datapointWriter = new DatapointWriter();

    private CodeSample currentCodeSample;

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
        currentCodeSample = codeLoader.getCodeSample();
        textArea.setText(currentCodeSample.code);
        timeTracker.reset();
    }

    private void next(boolean printedAnswer) {
        final Method method = currentCodeSample.method;
        final int codeSample = currentCodeSample.id;
        final long timePassed = timeTracker.getTimePassed();
        final boolean correct = printedAnswer == currentCodeSample.printing;
        final Datapoint datapoint = new Datapoint(method, codeSample, timePassed, correct);

        datapointWriter.writeDatapoint(datapoint);

        start();
    }

    public static void main(String[] args) {
        // Start all Swing applications on the EDT.
        SwingUtilities.invokeLater(() -> new ExperimentSetup().setVisible(true));
    }

}