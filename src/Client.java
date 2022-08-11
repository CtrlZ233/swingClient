import Service.Service;
import UI.UIManager;
import UI.UIManager.UIType;
import UI.Utils.ToasterFrame;

public class Client {
    public static void main(String[] args) {
        ToasterFrame ui = UIManager.getInstance().getUI(UIType.LOGIN);
        UIManager.getInstance().setCurrentUI(ui);
        startService();
    }

    private static void startService() {
        Thread recv = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Service.getInstance() == null) {
                    try {
                        // 每秒重试
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Service.getInstance().recv();
            }
        });

        Thread msgDispatch = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Service.getInstance() == null) {
                    try {
                        // 每秒重试
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Service.getInstance().messageDispatch();
            } 
        });

        recv.start();
        msgDispatch.start();
    }
}
