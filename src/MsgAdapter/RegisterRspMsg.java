package MsgAdapter;

public class RegisterRspMsg extends ResponseMsg {
    public String info;
    public RegisterRspMsg(byte[] msg) {
        super(msg);
        this.info = new String(this.data);
    }
}
