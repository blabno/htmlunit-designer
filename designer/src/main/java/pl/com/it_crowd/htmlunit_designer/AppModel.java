package pl.com.it_crowd.htmlunit_designer;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.util.NameCompletion;
import bsh.util.NameCompletionTable;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppModel {
// ------------------------------ FIELDS ------------------------------

    public static final String editorFileProperty = "editorFile";

    public static final String editorTextModifiedProperty = "editorTextModified";

    public static final String editorTextProperty = "editorText";

    public static final String responsesProperty = "responses";

    public static final String selectedResponseProperty = "selectedResponse";

    private static Logger logger = Logger.getLogger(AppModel.class);

    private File editorFile;

    private String editorText;

    private boolean editorTextModified;

    private Interpreter interpreter;

    private Thread interpreterThread;

    private NameCompletionTable nameCompletion;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private List<WebResponse> responses;

    private WebResponse selectedResponse;

    private Settings settings;

    private List<WebResponse> unmodifiableResponses;

    private WebClient webClient;

// --------------------------- CONSTRUCTORS ---------------------------

    public AppModel()
    {
        settings = new Settings();
        nameCompletion = new NameCompletionTable();
        nameCompletion.add("print(");
        nameCompletion.add("import ");
        responses = new ArrayList<WebResponse>();
        unmodifiableResponses = Collections.unmodifiableList(responses);
        webClient = new WebClient(BrowserVersion.FIREFOX_3_6);
        setup(webClient);
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public File getEditorFile()
    {
        return editorFile;
    }

    public String getEditorText()
    {
        return editorText;
    }

    public Interpreter getInterpreter()
    {
        return interpreter;
    }

    public void setInterpreter(Interpreter interpreter)
    {
        this.interpreter = interpreter;
    }

    public NameCompletion getNameCompletion()
    {
        return nameCompletion;
    }

    public WebResponse getSelectedResponse()
    {
        return selectedResponse;
    }

    public Settings getSettings()
    {
        return settings;
    }

    public WebClient getWebClient()
    {
        return webClient;
    }

    public boolean isEditorTextModified()
    {
        return editorTextModified;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void clearResponses()
    {
        responses.clear();
        propertyChangeSupport.firePropertyChange(responsesProperty, null, responses);
    }

    public List<WebResponse> getResponses()
    {
        return unmodifiableResponses;
    }

    public void setEditorFile(File editorFile)
    {
        File oldValue = this.editorFile;
        this.editorFile = editorFile;
        propertyChangeSupport.firePropertyChange(editorFileProperty, oldValue, editorFile);
    }

    public void setEditorText(String editorText)
    {
        String oldValue = this.editorText;
        this.editorText = editorText;
        propertyChangeSupport.firePropertyChange(editorTextProperty, oldValue, editorText);
    }

    public void setEditorTextModified(boolean editorTextModified)
    {
        boolean oldValue = this.editorTextModified;
        this.editorTextModified = editorTextModified;
        propertyChangeSupport.firePropertyChange(editorTextModifiedProperty, oldValue, editorTextModified);
    }

    public void setSelectedResponse(WebResponse selectedResponse)
    {
        WebResponse oldValue = this.selectedResponse;
        this.selectedResponse = selectedResponse;
        propertyChangeSupport.firePropertyChange(selectedResponseProperty, oldValue, selectedResponse);
    }

    public void startInterpreterThread()
    {
        if (interpreterThread == null) {
            interpreterThread = new Thread(interpreter, "Interpreter");
            interpreterThread.start();
            addWebClientToInterpreter();
        }
    }

    private void addWebClientToInterpreter()
    {
        try {
            interpreter.set("client", getWebClient());
            if (!nameCompletion.contains("client")) {
                nameCompletion.add("client");
            }
        } catch (EvalError evalError) {
            logger.error("Cannot set webClient into interpreter context", evalError);
        }
    }

    private void setup(WebClient webClient)
    {
        new WebConnectionWrapper(webClient) {
            @Override
            public WebResponse getResponse(WebRequest request) throws IOException
            {
                WebResponse response = super.getResponse(request);
                responses.add(response);
                propertyChangeSupport.firePropertyChange(responsesProperty, null, responses);
                return response;
            }
        };
    }
}
