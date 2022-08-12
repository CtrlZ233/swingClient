package Service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Queue;

import MsgAdapter.ResponseMsg;
import MsgAdapter.MsgDefine.*;
import MsgHandler.LoginRspHandler;
import MsgHandler.MsgHandler;
import MsgHandler.RegisterRspHandler;
import MsgHandler.ConnectRspMsgHandler;

public class Service {
    private static int MaxWaitMsgNum = 32;
    private static volatile Service INSTANCE = null;
    private int pid = -1;
    private Socket sock;
    private String host = "127.0.0.1";
    private int port = 8080;
    private Queue<byte[]> msgPool = new ArrayDeque<>();
    private HashMap<MsgType, MsgHandler> handlers = new HashMap<>();

    private Service() throws Exception {
        this.sock = new Socket(host, port);
        handlers.put(MsgType.REGISTER, new RegisterRspHandler());
        handlers.put(MsgType.LOGIN, new LoginRspHandler());
        handlers.put(MsgType.CONNECTION, new ConnectRspMsgHandler());
    }

    public static Service getInstance() {
        if (INSTANCE == null) {
            synchronized (Service.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new Service();
                    } catch (Exception e) {
                        return null;
                    }
                    
                }
            }
        }
        return INSTANCE;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int newPid) {
        pid = newPid;
    }

    synchronized public void send(byte[] data) {
        try {
            System.out.println(data.length);
            DataOutputStream out = new DataOutputStream(this.sock.getOutputStream());
            out.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageDispatch() {
        while (true) {
            byte[] msgByte = new byte[0];
            synchronized (msgPool) {
                while (msgPool.isEmpty()) {
                    try {
                        msgPool.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                msgByte = msgPool.poll();

                System.out.println(new String(msgByte));
                
                msgPool.notifyAll();
            }

            if (msgByte.length < ResponseCodePos.POS_BOTTOM.ordinal()) {
                continue;
            }
            ResponseMsg msg = new ResponseMsg(msgByte);
            for (int i = 0; i < msgByte.length; ++i) {
                System.out.println((int)msgByte[i]);
            }
            MsgHandler handlers = this.handlers.get(msg.type);
            if (handlers == null) {
                System.out.printf("cannot handle this message, message type: %s\n", msg.type.name());
                continue;
            }
            handlers.handleMessage(msgByte);
        }
    }

    public void recv() {
        while (true) {
            byte[] accept = new byte[255];
            int len = 0;
            try {
                DataInputStream in = new DataInputStream(this.sock.getInputStream());
                len = in.read(accept);
            } catch (IOException e) {
                System.exit(-1);
            }
            synchronized (msgPool) {
                while (msgPool.size() == MaxWaitMsgNum) {
                    try {
                        msgPool.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (len == 0) {
                    continue;
                }
                
                msgPool.add(Arrays.copyOfRange(accept, 0, len + 1));
                msgPool.notifyAll();
            }
        }
    }
    
}
