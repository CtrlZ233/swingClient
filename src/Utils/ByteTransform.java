package Utils;

import java.util.Arrays;

public class ByteTransform {

    // 低字节序
    public static byte[] int2Bytes(int i) {
        byte[] ans = new byte[4];
        ans[0] = (byte)(i & 0xff);
        ans[1] = (byte)(i >> 8 & 0xff);
        ans[2] = (byte)(i >> 16 & 0xff);
        ans[3] = (byte)(i >> 24 & 0xff);
        return ans;
    }

    // 低字节序
    public static int bytes2Int(byte[] b) {
        int ans = 0;
        for (int i = 0; i < b.length; ++i) {
            ans += (b[i] & 0xff) << (i * 8);
        }
        return ans;
    }

    // 固定长度补0
    public static byte[] fillZero(byte[] b, int len) {
        if (b.length >= len) {
            return Arrays.copyOfRange(b, 0, len);
        }
        byte[] ans = new byte[len];
        System.arraycopy(b, 0, ans, 0, b.length);
        for (int i = b.length; i < len; ++i) {
            ans[i] = 0;
        }
        return ans;
    }
}