package MsgHandler;

import MsgAdapter.ConnectRspMsg;
import MsgAdapter.MsgDefine.ResponseCode;

import UI.UIManager;
import UI.Toaster.Toaster;

import Service.Service;
public class ConnectRspMsgHandler implements MsgHandler {
    @Override
    public void handleMessage(byte[] msg) {
        ConnectRspMsg rspMsg = new ConnectRspMsg(msg);
        if (rspMsg.code != ResponseCode.OK || rspMsg.allocPid <= 0) {
            Toaster toaster = UIManager.getInstance().getCurrentUI().getToaster();
            toaster.error("服务器错误");
            return;
        }
        Service.getInstance().setPid(rspMsg.allocPid);
    }
    
}
