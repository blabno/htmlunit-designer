package pl.com.it_crowd.htmlunit_designer;

import bsh.EvalError;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EditorForm {
// ------------------------------ FIELDS ------------------------------

    private AppModel appModel;

    private JTextArea editorTextArea;

    private JPanel rootComponent;

// --------------------------- CONSTRUCTORS ---------------------------

    public EditorForm(AppModel appModel)
    {
        this.appModel = appModel;
        setupEditorTextArea();
        setupModelListener();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return rootComponent;
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
        final JScrollPane scrollPane1 = new JScrollPane();
        rootComponent.add(scrollPane1, BorderLayout.CENTER);
        editorTextArea = new JTextArea();
        editorTextArea.setText("/** F5 or CTRL+ENTER to execute */  ");
        scrollPane1.setViewportView(editorTextArea);
    }

    private void setupEditorTextArea()
    {
        editorTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (KeyUtils.isEvaluateCombination(e)) {
                    try {
                        appModel.getInterpreter().eval(editorTextArea.getText());
                    } catch (EvalError evalError) {
                        appModel.getInterpreter().error(evalError);
                        appModel.getInterpreter().println("");
//                        consoleForm.getJConsole().error(evalError);
//                        consoleForm.getJConsole().println();
                    }
                }
            }
        });
        editorTextArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)
            {
                appModel.setEditorTextModified(true);
            }

            public void removeUpdate(DocumentEvent e)
            {
                appModel.setEditorTextModified(true);
            }

            public void changedUpdate(DocumentEvent e)
            {
                appModel.setEditorTextModified(true);
            }
        });
    }

    private void setupModelListener()
    {
        appModel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (AppModel.editorTextProperty.equals(evt.getPropertyName())) {
                    editorTextArea.setText((String) evt.getNewValue());
                }
            }
        });
    }
}