package MsgHandler;

import MsgAdapter.RegisterRspMsg;
import MsgAdapter.MsgDefine.ResponseCode;
import UI.UIManager;
import UI.Toaster.Toaster;
import UI.UIManager.UIType;
import UI.Utils.ToasterFrame;

public class RegisterRspHandler implements MsgHandler {
    @Override
    public void handleMessage(byte[] msg) {
        RegisterRspMsg rspMsg = new RegisterRspMsg(msg);
        Toaster toaster = UIManager.getInstance().getUI(UIType.REGISTER).getToaster();
        if (rspMsg.code != ResponseCode.OK) {
            toaster.error(rspMsg.info);
        } else {
            toaster.info("注册成功，请重新登录");
            getUIContext(UIType.REGISTER).setVisible(false);
            getUIContext(UIType.LOGIN).setVisible(true);
            UIManager.getInstance().setCurrentUI(getUIContext(UIType.LOGIN));
        }
    }

    private ToasterFrame getUIContext(UIType type) {
        return UIManager.getInstance().getUI(type);
    }
    
}
