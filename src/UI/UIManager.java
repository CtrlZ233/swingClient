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
    private HashMap<UIType, UIFactory> creators = new HashMap<>();
    private static volatile UIManager INSTANCE = null;
    
    private ToasterFrame currentUI = null;

    private UIManager() {
        creators.put(UIType.LOGIN, new LoginUIFactory());
        creators.put(UIType.REGISTER, new RegisterUIFactory());
    }

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

    synchronized public ToasterFrame getUI(UIType type) {
        ToasterFrame ui = uis.get(type);
        if (ui != null) {
            return ui;
        }
        UIFactory creator = creators.get(type);
        ui = creator.Create();
        uis.put(type, ui);
        return ui;
    }

    public ToasterFrame getCurrentUI() {
        return currentUI;
    }

    public void setCurrentUI(ToasterFrame ui) {
        currentUI = ui;
    }

    private class LoginUIFactory implements UIFactory {
        @Override
        public ToasterFrame Create() {
            return new LoginUI();
        }
    }

    private class RegisterUIFactory implements UIFactory {
        @Override
        public ToasterFrame Create() {
            return new RegisterUI();
        }
    }

}

