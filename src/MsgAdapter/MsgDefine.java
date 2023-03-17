package MsgAdapter;

public class MsgDefine {
    public enum MsgType {
        CONNECTION,
        DISCONNECTION,
        REGISTER,
        LOGIN,
        MSGTYPE_BOTTOM,
    }
    
    public enum ResponseCode {
        OK,
        INVALID_REQ,
        SERVER_ERR,
        CONN_MAX,
        ResponseCode_BOTTOM
    }
    
    public enum ResponseCodePos {
        CODE,
        TYPE,
        PID_0,
        PID_1,
        PID_2,
        PID_3,
        POS_BOTTOM
    }
}

