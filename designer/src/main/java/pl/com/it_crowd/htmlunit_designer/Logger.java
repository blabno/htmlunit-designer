package pl.com.it_crowd.htmlunit_designer;

public final class Logger {
// ------------------------------ FIELDS ------------------------------

    private String simpleName;

// -------------------------- STATIC METHODS --------------------------

    public static Logger getLogger(Class clazz)
    {
        return new Logger(clazz.getSimpleName());
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private Logger(String simpleName)
    {
        this.simpleName = simpleName;
    }

// -------------------------- OTHER METHODS --------------------------

    public void error(String msg, Throwable cause)
    {
        System.err.println(simpleName + ": " + msg);
        cause.printStackTrace(System.err);
    }
}
