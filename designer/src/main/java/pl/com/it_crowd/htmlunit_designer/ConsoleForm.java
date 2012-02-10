package pl.com.it_crowd.htmlunit_designer;

import bsh.EvalError;
import bsh.Interpreter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConsoleForm {
// ------------------------------ FIELDS ------------------------------

    private static Logger logger = Logger.getLogger(ConsoleForm.class);

    private JConsole JConsole;

    private JPanel JConsolePanel;

    private AppModel appModel;

    private JButton clearButton;

    private JPanel consolePanel;

    /**
     * Funkcje:
     * -lista requestow
     * -szczegoly requestu
     * -zrodlo obecnej strony
     * -edytor skryptu /textarea
     * -liczba jobow
     * -odczyt historii komend z pliku
     * -zapis historii komend do pliku
     * -konsola javascript
     */

    private Layout layout = Layout.MULTI_LINE;

    private JPanel rootComponent;

    private JSplitPane splitPane;

    private JButton switchLayoutButton;

    private JTextArea textArea;

    private JScrollPane textareaScrollPane;

// --------------------------- CONSTRUCTORS ---------------------------

    public ConsoleForm(AppModel appModel)
    {
        appModel.setInterpreter(new Interpreter(JConsole));
        this.appModel = appModel;
        appModel.getInterpreter().setConsole(JConsole);
        switchLayoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (layout.equals(Layout.SINGLE_LINE)) {
                    setMultiLineLayout();
                } else {
                    setSingleLineLayout();
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                JConsole.clear();
            }
        });
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (KeyUtils.isEvaluateCombination(e)) {
                    try {
                        ConsoleForm.this.appModel.getInterpreter().eval(textArea.getText());
                        getJConsole().enter();
                    } catch (EvalError evalError) {
                        getJConsole().error(evalError);
                        getJConsole().println();
                    }
                }
            }
        });
        setSingleLineLayout();
//        TODO add better name completion
        JConsole.setNameCompletion(appModel.getNameCompletion());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public JPanel getRootComponent()
    {
        return rootComponent;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return rootComponent;
    }

    public JConsole getJConsole()
    {
        return JConsole;
    }

    public void setSingleLineLayout()
    {
        if (Layout.MULTI_LINE.equals(layout)) {
            layout = Layout.SINGLE_LINE;
            consolePanel.removeAll();
            consolePanel.add(JConsole, BorderLayout.CENTER);
            consolePanel.revalidate();
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        rootComponent = new JPanel();
        rootComponent.setLayout(new BorderLayout(0, 0));
        rootComponent.setMinimumSize(new Dimension(133, 200));
        rootComponent.setPreferredSize(new Dimension(133, 400));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        panel1.setMaximumSize(new Dimension(2147483647, 29));
        panel1.setMinimumSize(new Dimension(133, 29));
        panel1.setPreferredSize(new Dimension(133, 29));
        rootComponent.add(panel1, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.EAST);
        clearButton = new JButton();
        clearButton.setText("Clear");
        panel2.add(clearButton, BorderLayout.CENTER);
        switchLayoutButton = new JButton();
        switchLayoutButton.setText("Switch layout");
        panel2.add(switchLayoutButton, BorderLayout.EAST);
        consolePanel = new JPanel();
        consolePanel.setLayout(new BorderLayout(0, 0));
        rootComponent.add(consolePanel, BorderLayout.CENTER);
        splitPane = new JSplitPane();
        splitPane.setContinuousLayout(false);
        splitPane.setDividerLocation(235);
        splitPane.setOrientation(1);
        consolePanel.add(splitPane, BorderLayout.CENTER);
        textareaScrollPane = new JScrollPane();
        splitPane.setRightComponent(textareaScrollPane);
        textArea = new JTextArea();
        textArea.setText("/** F5 or CTRL+ENTER to execute */  ");
        textareaScrollPane.setViewportView(textArea);
        JConsolePanel = new JPanel();
        JConsolePanel.setLayout(new BorderLayout(0, 0));
        splitPane.setLeftComponent(JConsolePanel);
        JConsole = new JConsole();
        JConsolePanel.add(JConsole, BorderLayout.CENTER);
    }

    private void setMultiLineLayout()
    {
        if (Layout.SINGLE_LINE.equals(layout)) {
            layout = Layout.MULTI_LINE;
            consolePanel.removeAll();
            consolePanel.add(splitPane);
            splitPane.setLeftComponent(JConsole);
            splitPane.setRightComponent(textareaScrollPane);
            consolePanel.revalidate();
        }
    }

// -------------------------- ENUMERATIONS --------------------------

    private enum Layout {
        MULTI_LINE,
        SINGLE_LINE
    }
}
