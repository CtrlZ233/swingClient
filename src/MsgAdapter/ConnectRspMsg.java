package MsgAdapter;

import Utils.ByteTransform;

public class ConnectRspMsg extends ResponseMsg {
    public int allocPid;
    public ConnectRspMsg(byte[] msg) {
        super(msg);
        this.allocPid = ByteTransform.bytes2Int(this.data);
    }
}
