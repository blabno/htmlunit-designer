package pl.com.it_crowd.htmlunit_designer;

import bsh.EvalError;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowAdapter;
import com.gargoylesoftware.htmlunit.WebWindowEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainFrame extends JFrame {
// ------------------------------ FIELDS ------------------------------

    private static Logger logger = Logger.getLogger(MainFrame.class);

    private XHRRequestsForm XHRRequestsForm;

    private AppModel appModel;

    private JTabbedPane centralTabs;

    private ConsoleForm consoleForm;

    private JTextArea editorTextArea;

    private JavaScriptWatchesForm javaScriptWatchesForm;

    private JTextArea pageSourceTextArea;

    private JTextArea requestSourceTextArea;

    private JPanel rootComponent;

    private WatchesForm watchesForm;

    private JSplitPane watchesPanel;

// --------------------------- CONSTRUCTORS ---------------------------

    public MainFrame()
    {
        appModel = new AppModel();
        appModel.getSettings().addPropertiChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (Settings.watchesVisibleProperty.equals(evt.getPropertyName())) {
                    if ((Boolean) evt.getNewValue()) {
                        showWatches();
                    } else {
                        hideWatches();
                    }
                } else if (Settings.javaScriptWatchesVisibleProperty.equals(evt.getPropertyName())) {
                    if ((Boolean) evt.getNewValue()) {
                        showJavaScriptWatches();
                    } else {
                        hideJavaScriptWatches();
                    }
                }
            }
        });
        $$$setupUI$$$();
        setContentPane(rootComponent);
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(new SaveAction()));
        menubar.add(fileMenu);
        JMenu viewMenu = new JMenu("View");
        menubar.add(viewMenu);
        viewMenu.add(createJCheckBoxMenuItem("Watches", appModel.getSettings().isWatchesVisible(), new ToggleWatchesAction()));
        viewMenu.add(createJCheckBoxMenuItem("JavaScript Watches", appModel.getSettings().isJavaScriptWatchesVisible(), new ToggleJavaScriptWatchesAction()));
        if (!appModel.getSettings().isWatchesVisible()) {
            hideWatches();
        }
        if (!appModel.getSettings().isJavaScriptWatchesVisible()) {
            hideJavaScriptWatches();
        }
        appModel.getWebClient().addWebWindowListener(new WebWindowAdapter() {
            @Override
            public void webWindowContentChanged(WebWindowEvent event)
            {
                Page page = event.getNewPage();
                WebWindow currentWindow = event.getWebWindow().getWebClient().getCurrentWindow();
                if (currentWindow != null && !currentWindow.equals(event.getWebWindow())) {
                    return;
                }
                if (page instanceof SgmlPage) {
                    pageSourceTextArea.setText(((SgmlPage) page).asXml());
                } else {
                    System.out.println(page);
                }
            }
        });
        appModel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if (AppModel.selectedResponseProperty.equals(evt.getPropertyName())) {
                    WebResponse response = (WebResponse) evt.getNewValue();
                    if (response == null) {
                        requestSourceTextArea.setText("");
                        return;
                    }
                    WebRequest request = response.getWebRequest();
                    StringBuilder builder = new StringBuilder(request.getRequestParameters().toString());
                    builder.append("\n\n");
                    builder.append(response.getContentAsString());
                    requestSourceTextArea.setText(builder.toString());
                    requestSourceTextArea.repaint();
                }
            }
        });
        editorTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e)
            {
                if (KeyUtils.isEvaluateCombination(e)) {
                    try {
                        appModel.getInterpreter().eval(editorTextArea.getText());
                        consoleForm.getJConsole().enter();
                    } catch (EvalError evalError) {
                        consoleForm.getJConsole().error(evalError);
                        consoleForm.getJConsole().println();
                    }
                }
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new SaveAction().actionPerformed(null);
            }
        });
        consoleForm.getJConsole().loadHistory("history.txt");
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
        createUIComponents();
        rootComponent = new JPanel();
        rootComponent.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOrientation(0);
        rootComponent.add(splitPane1, BorderLayout.CENTER);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane1.setLeftComponent(splitPane2);
        centralTabs = new JTabbedPane();
        centralTabs.setPreferredSize(new Dimension(400, 200));
        splitPane2.setLeftComponent(centralTabs);
        final JScrollPane scrollPane1 = new JScrollPane();
        centralTabs.addTab("Editor", scrollPane1);
        editorTextArea = new JTextArea();
        editorTextArea.setText("");
        scrollPane1.setViewportView(editorTextArea);
        final JScrollPane scrollPane2 = new JScrollPane();
        centralTabs.addTab("Curren page source", scrollPane2);
        pageSourceTextArea = new JTextArea();
        pageSourceTextArea.setEditable(false);
        scrollPane2.setViewportView(pageSourceTextArea);
        final JScrollPane scrollPane3 = new JScrollPane();
        centralTabs.addTab("Request", scrollPane3);
        requestSourceTextArea = new JTextArea();
        requestSourceTextArea.setEditable(false);
        scrollPane3.setViewportView(requestSourceTextArea);
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setOrientation(0);
        splitPane2.setRightComponent(splitPane3);
        watchesPanel = new JSplitPane();
        watchesPanel.setOrientation(0);
        splitPane3.setRightComponent(watchesPanel);
        watchesForm = new WatchesForm();
        watchesPanel.setLeftComponent(watchesForm.$$$getRootComponent$$$());
        javaScriptWatchesForm = new JavaScriptWatchesForm();
        watchesPanel.setRightComponent(javaScriptWatchesForm.$$$getRootComponent$$$());
        splitPane3.setLeftComponent(XHRRequestsForm.$$$getRootComponent$$$());
        splitPane1.setRightComponent(consoleForm.$$$getRootComponent$$$());
    }

    private JCheckBoxMenuItem createJCheckBoxMenuItem(String text, boolean state, Action action)
    {
        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem(action);
        checkBoxMenuItem.setState(state);
        checkBoxMenuItem.setText(text);
        return checkBoxMenuItem;
    }

    private void createUIComponents()
    {
        consoleForm = new ConsoleForm(appModel);
        XHRRequestsForm = new XHRRequestsForm(appModel);
    }

    private void hideJavaScriptWatches()
    {
        if (watchesPanel.equals(javaScriptWatchesForm.$$$getRootComponent$$$().getParent())) {
            watchesPanel.remove(javaScriptWatchesForm.$$$getRootComponent$$$());
            rootComponent.revalidate();
        }
    }

    private void hideWatches()
    {
        if (watchesPanel.equals(watchesForm.$$$getRootComponent$$$().getParent())) {
            watchesPanel.remove(watchesForm.$$$getRootComponent$$$());
            rootComponent.revalidate();
        }
    }

    private void showJavaScriptWatches()
    {
        if (!watchesPanel.equals(javaScriptWatchesForm.$$$getRootComponent$$$().getParent())) {
            watchesPanel.setBottomComponent(javaScriptWatchesForm.$$$getRootComponent$$$());
            rootComponent.revalidate();
        }
    }

    private void showWatches()
    {
        if (!watchesPanel.equals(watchesForm.$$$getRootComponent$$$().getParent())) {
            watchesPanel.setTopComponent(watchesForm.$$$getRootComponent$$$());
            rootComponent.revalidate();
        }
    }

// -------------------------- INNER CLASSES --------------------------

    private class SaveAction extends AbstractAction {
// --------------------------- CONSTRUCTORS ---------------------------

        private SaveAction()
        {
            super("Save");
        }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ActionListener ---------------------

        public void actionPerformed(ActionEvent e)
        {
            consoleForm.getJConsole().saveHistory("history.txt");
            appModel.getSettings().save();
        }
    }

    private class ToggleJavaScriptWatchesAction extends AbstractAction {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ActionListener ---------------------

        public void actionPerformed(ActionEvent e)
        {
            boolean state = ((JCheckBoxMenuItem) e.getSource()).getState();
            appModel.getSettings().setJavaScriptWatchesVisible(state);
        }
    }

    private class ToggleWatchesAction extends AbstractAction {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface ActionListener ---------------------

        public void actionPerformed(ActionEvent e)
        {
            boolean state = ((JCheckBoxMenuItem) e.getSource()).getState();
            appModel.getSettings().setWatchesVisible(state);
        }
    }

// --------------------------- main() method ---------------------------

    public static void main(String[] args)
    {
        MainFrame dialog = new MainFrame();
        dialog.setMinimumSize(new Dimension(400, 200));
        dialog.pack();
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dialog.appModel.startInterpreterThread();
    }
}
