package UI;

import java.util.HashMap;

import UI.Utils.ToasterFrame;

public class UIManager {
    public enum UIType {
        LOGIN,
        REGISTER,
        MAIN,
        UI_BOTTOM
    }
    
    private HashMap<UIType, ToasterFrame> uis = new HashMap<>();
    private static volatile UIManager INSTANCE = null;
    
    private ToasterFrame currentUI = null;

    private UIManager() {}

    static public UIManager getInstance() {
        if (INSTANCE == null) {
            synchronized (UIManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new UIManager();
                }
            }
        }
        return INSTANCE;
    }

    public void registUI(UIType type, ToasterFrame ui) {
        if (uis.get(type) != null) {
            System.out.printf("ui [%s] is exist", type.name());
            return;
        }

        uis.put(type, ui);
    }

    public ToasterFrame getUI(UIType type) {
        return uis.get(type);
    }

    public ToasterFrame getCurrentUI() {
        return currentUI;
    }

    public void setCurrentUI(ToasterFrame ui) {
        currentUI = ui;
    }
    

}
