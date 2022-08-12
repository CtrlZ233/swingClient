package MsgAdapter;

import MsgAdapter.MsgDefine.*;
import Utils.ByteTransform;

public class MsgBuilder {

    private MsgType type = MsgType.MSGTYPE_BOTTOM;
    private int pid = -1;

    public MsgBuilder(MsgType msgType, int sendPid) {
        this.type = msgType;
        this.pid = sendPid;
    }

    public byte[] msgPackage(byte[]... datas) {
        int len = 1;
        for (int i = 0; i < datas.length; ++i) {
            len += datas[i].length;
        }
        byte[] bytesPid = ByteTransform.int2Bytes(this.pid);
        len = len + bytesPid.length;
        byte[] msg = new byte[len + 1];
        msg[0] = (byte)this.type.ordinal();

        System.arraycopy(bytesPid, 0, msg, 1, bytesPid.length);
        int index = 1 + bytesPid.length;
        for (int i = 0; i < datas.length; ++i) {
            System.arraycopy(datas[i], 0, msg, index, datas[i].length);
            index = index + datas[i].length;
        }
        msg[index] = '\0';
        return msg;
    }

    

}
