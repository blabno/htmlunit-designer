package pl.com.it_crowd.htmlunit_designer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
// ------------------------------ FIELDS ------------------------------

    public static final String javaScriptWatchesVisibleProperty = "javaScriptWatchesVisible";

    public static final String watchesVisibleProperty = "watchesVisible";

    private static Logger logger = Logger.getLogger(Settings.class);

    private static final String settingsFileName = "settings.properties";

    private boolean javaScriptWatchesVisible;

    private Properties properties;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private boolean watchesVisible;

// --------------------------- CONSTRUCTORS ---------------------------

    public Settings()
    {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(settingsFileName));
        } catch (IOException e) {
            logger.error("Cannot read settings.properties", e);
        }
        watchesVisible = Boolean.parseBoolean(properties.getProperty(watchesVisibleProperty));
        javaScriptWatchesVisible = Boolean.parseBoolean(properties.getProperty(javaScriptWatchesVisibleProperty));
        addPropertiChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt)
            {
                properties.setProperty(evt.getPropertyName(), evt.getNewValue() == null ? "" : evt.getNewValue().toString());
                save();
            }
        });
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public PropertyChangeSupport getPropertyChangeSupport()
    {
        return propertyChangeSupport;
    }

    public void setPropertyChangeSupport(PropertyChangeSupport propertyChangeSupport)
    {
        this.propertyChangeSupport = propertyChangeSupport;
    }

    public boolean isJavaScriptWatchesVisible()
    {
        return javaScriptWatchesVisible;
    }

    public boolean isWatchesVisible()
    {
        return watchesVisible;
    }

// -------------------------- OTHER METHODS --------------------------

    public void addPropertiChangeListener(PropertyChangeListener listener)
    {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void save()
    {
        try {
            File file = new File(settingsFileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Cannot create file " + file.getAbsolutePath());
                }
            }
            properties.store(new FileOutputStream(file), "Saved automatically");
        } catch (FileNotFoundException e) {
            logger.error("Cannot save settings to file", e);
        } catch (IOException e) {
            logger.error("Cannot save settings to file", e);
        }
    }

    public void setJavaScriptWatchesVisible(boolean javaScriptWatchesVisible)
    {
        boolean oldValue = this.javaScriptWatchesVisible;
        this.javaScriptWatchesVisible = javaScriptWatchesVisible;
        propertyChangeSupport.firePropertyChange(javaScriptWatchesVisibleProperty, oldValue, javaScriptWatchesVisible);
    }

    public void setWatchesVisible(boolean watchesVisible)
    {
        boolean oldValue = this.watchesVisible;
        this.watchesVisible = watchesVisible;
        propertyChangeSupport.firePropertyChange(watchesVisibleProperty, oldValue, watchesVisible);
    }
}
