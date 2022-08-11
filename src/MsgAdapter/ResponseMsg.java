package MsgAdapter;

import java.util.Arrays;

import MsgAdapter.MsgDefine.*;

public class ResponseMsg {

    public ResponseCode code;
    public MsgType type;
    public int sendPid;
    protected byte[] data;
    
    public ResponseMsg(byte[] msg) {
        if (msg.length < ResponseCodePos.POS_BOTTOM.ordinal()) {
            this.code = ResponseCode.SERVER_ERR;
            return;
        }
        boolean isMsgTypeValid = msg[ResponseCodePos.TYPE.ordinal()] < MsgType.MSGTYPE_BOTTOM.ordinal();
        boolean isRspCodeValid = msg[ResponseCodePos.CODE.ordinal()] < ResponseCode.ResponseCode_BOTTOM.ordinal();
        if (!isMsgTypeValid || isRspCodeValid) {
            this.code = ResponseCode.SERVER_ERR;
            this.type = MsgType.MSGTYPE_BOTTOM;
            return;
        }
        this.code = ResponseCode.values()[msg[ResponseCodePos.CODE.ordinal()]];
        this.type = MsgType.values()[msg[ResponseCodePos.TYPE.ordinal()]];
        this.sendPid = Utils.ByteTransform.bytes2Int(Arrays.copyOfRange(msg, ResponseCodePos.PID_0.ordinal(), 
                                                    ResponseCodePos.POS_BOTTOM.ordinal()));
        this.data = Arrays.copyOfRange(msg, ResponseCodePos.POS_BOTTOM.ordinal(), msg.length);
        fillData();
    }

    protected void fillData() {}
}
