package pl.com.it_crowd.htmlunit_designer;

import java.awt.event.KeyEvent;

public final class KeyUtils {
// -------------------------- STATIC METHODS --------------------------

    public static boolean isEvaluateCombination(KeyEvent e)
    {
        boolean controlPressed = (e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) == KeyEvent.CTRL_DOWN_MASK;
        return (controlPressed && e.getKeyChar() == KeyEvent.VK_ENTER) || e.getKeyCode() == KeyEvent.VK_F5;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private KeyUtils()
    {

    }
}
