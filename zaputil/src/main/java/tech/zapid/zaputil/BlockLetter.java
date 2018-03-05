package tech.zapid.zaputil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BlockLetter {

    // Public members
    public String message;
    public byte[] code;
    public byte[] hash;

    private static BiMap charMap = new BiMap();

    static {
        charMap.put('0', 0x00e9d72e);
        charMap.put('1', 0x01f210c4);
        charMap.put('2', 0x01f1322e);
        charMap.put('3', 0x00e8b22e);
        charMap.put('4', 0x01087e31);
        charMap.put('5', 0x00f83c3f);
        charMap.put('6', 0x00e8bc3e);
        charMap.put('7', 0x0011111f);
        charMap.put('8', 0x00e8ba2e);
        charMap.put('9', 0x01087a3e);
        charMap.put('A', 0x0118fe2e);
        charMap.put('B', 0x00f8be2f);
        charMap.put('C', 0x00e8862e);
        charMap.put('D', 0x00f8c62f);
        charMap.put('E', 0x01f09c3f);
        charMap.put('F', 0x00109c3f);
        charMap.put('G', 0x00e8e43e);
        charMap.put('H', 0x0118fe31);
        charMap.put('I', 0x01f2109f);
        charMap.put('J', 0x0032109f);
        charMap.put('K', 0x01149d31);
        charMap.put('L', 0x01f08421);
        charMap.put('M', 0x0118d771);
        charMap.put('N', 0x011cd671);
        charMap.put('O', 0x00e8c62e);
        charMap.put('P', 0x0010be2f);
        charMap.put('Q', 0x0164c62e);
        charMap.put('R', 0x0114be2f);
        charMap.put('S', 0x00f8383e);
        charMap.put('T', 0x0042109f);
        charMap.put('U', 0x00e8c631);
        charMap.put('V', 0x00454631);
        charMap.put('W', 0x00aac631);
        charMap.put('X', 0x01151151);
        charMap.put('Y', 0x00421151);
        charMap.put('Z', 0x01f1111f);
        charMap.put(',', 0x00420000);
        charMap.put('.', 0x00400000);
        charMap.put('!', 0x00401082);
        charMap.put('#', 0x00afabea);
        charMap.put('$', 0x00fa38be);
        charMap.put('(', 0x01042110);
        charMap.put(')', 0x00110841);
        charMap.put('+', 0x00023880);
        charMap.put('-', 0x00003800);
        charMap.put('=', 0x000701c0);
        charMap.put('?', 0x0040322e);
        charMap.put(':', 0x00020080);
        charMap.put(';', 0x00108020);
        charMap.put('/', 0x00111110);
        charMap.put('\\', 0x01041041);
        charMap.put(' ', 0x00000000);
        charMap.put('\n', 0x01ffffff);
    }

    public BlockLetter(String m) throws IllegalArgumentException {
        message = validMessage(m);
        code = encode(message);
        try {
            hash = getHash(message);
        } catch (NoSuchAlgorithmException err) {
            hash = new byte[] {0x0};
        }
    }

    public static byte[] encode(String m) throws IllegalArgumentException {

        // Validate the message
        m = validMessage(m);
        // Convert to byte
        ByteBuffer bBuff = ByteBuffer.allocate(m.length() * 4);
        for (int ii = 0; ii < m.length(); ii++)
            bBuff.putInt(ii*4, charMap.getF(m.charAt(ii)));
        // Convert to big-endian byte array and return
        return bBuff.array();
    }

    public static String decode(byte[] bArr) {

        // Decode the integer
        ByteBuffer bBuff = ByteBuffer.wrap(bArr);
        char[] cArr = new char[bArr.length / 4];
        for (int ii = 0; ii < cArr.length; ii++)
            cArr[ii] = charMap.getR(bBuff.getInt(ii*4));
        // Convert to string and return
        return String.valueOf(cArr);

    }

    public static byte[] getHash(String m) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(m.getBytes(StandardCharsets.US_ASCII));
        return md.digest();
    }

    public static String validMessage(String m) throws IllegalArgumentException {

        m = m.toUpperCase();
        for (int ii = 0; ii < m.length(); ii++) {
            if (charMap.getF(m.charAt(ii)) == null)  {
                throw new IllegalArgumentException("Illegal character for initialization");
            }
        }
        return m;
    }
}
