package pl.com.it_crowd.htmlunit_designer;

import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class XHRRequestsForm {
// ------------------------------ FIELDS ------------------------------

    private AppModel appModel;

    private JPanel rootComponent;

    private JTable table;

// --------------------------- CONSTRUCTORS ---------------------------

    public XHRRequestsForm(AppModel appModel)
    {
        this.appModel = appModel;
        table.setModel(new MyTableModel());
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(0).setMaxWidth(40);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e)
            {
                List<WebResponse> responses = XHRRequestsForm.this.appModel.getResponses();
                if (e.getFirstIndex() < 0) {
                    return;
                }
                if (responses.size() <= e.getFirstIndex()) {
                    XHRRequestsForm.this.appModel.setSelectedResponse(null);
                } else {
                    XHRRequestsForm.this.appModel.setSelectedResponse(responses.get(e.getFirstIndex()));
                }
            }
        });
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
        table = new JTable();
        scrollPane1.setViewportView(table);
    }

// -------------------------- INNER CLASSES --------------------------

    private class MyTableModel extends AbstractTableModel implements PropertyChangeListener {
// --------------------------- CONSTRUCTORS ---------------------------

        private MyTableModel()
        {
            appModel.addPropertyChangeListener(this);
        }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface PropertyChangeListener ---------------------

        public void propertyChange(PropertyChangeEvent evt)
        {
            if (AppModel.responsesProperty.equals(evt.getPropertyName())) {
                fireTableDataChanged();
            }
        }

// --------------------- Interface TableModel ---------------------

        public int getRowCount()
        {
            return appModel.getResponses().size();
        }

        public int getColumnCount()
        {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            if (appModel.getResponses().size() <= rowIndex) {
                return null;
            }
            WebResponse response = appModel.getResponses().get(rowIndex);
            WebRequest request = response.getWebRequest();
            switch (columnIndex) {
                case 0:
                    return request.getHttpMethod();
                case 1:
                    return request.getUrl();
                default:
                    return null;
            }
        }
    }
}