package UI.Utils;
import javax.swing.JFrame;
import javax.swing.JPanel;

import UI.Toaster.Toaster;

public abstract class ToasterFrame extends JFrame implements ToasterFrameInterface {
    protected final Toaster toaster;
    protected JPanel mainJPanel;
    public ToasterFrame() {
        mainJPanel = getMainJPanel();
        toaster = new Toaster(mainJPanel);
    }

    public Toaster getToaster() {
        return toaster;
    }

}
