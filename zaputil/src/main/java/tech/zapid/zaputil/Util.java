package tech.zapid.zaputil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Util {

    static private IDEncoder findEncoder(int version) throws IndexOutOfBoundsException {
        if (version < 0) {
            throw new IndexOutOfBoundsException("Negative versioning not allowed");
        }
        switch (version) {
            case 0: {
                return new IDEncoderVer0() {
                };
            }
            default: {
                throw new IndexOutOfBoundsException("Invalid Version");
            }
        }
    }

    static public String byteToString(byte[] bArr) {
        return new String(bArr, StandardCharsets.US_ASCII);
    }

    static public byte[] stringToByte(String str) {
        return str.getBytes(StandardCharsets.US_ASCII);
    }

    static String validateQRCode(byte[] code) throws InvalidIDCodeException {

        // Strip off the version number and get the appropriate ID encoder
        ByteBuffer bBuf = ByteBuffer.allocate(4);
        bBuf.put(Arrays.copyOfRange(code, 0, 3));
        int version = bBuf.getInt();

        // Get the correct encoder and check the rest of the message
        byte[] testCode = Arrays.copyOfRange(code, 4, code.length);
        IDEncoder encoder;
        try {
            encoder = findEncoder(version);
        } catch (IndexOutOfBoundsException exc) {
            throw new InvalidIDCodeException();
        }
        return encoder.validate(testCode);
    }

    static byte[] getQrCode(String message, int version) throws IndexOutOfBoundsException {

        // Get the correct encoder and encode the message
        IDEncoder encoder = findEncoder(version);
        byte[] testCode = encoder.encode(message);

        // Allocate a new bit buffer and append the two parts of the code
        ByteBuffer bBuf = ByteBuffer.allocate(testCode.length + 4);
        bBuf.putInt(version);
        bBuf.put(testCode, 4, testCode.length + 4);
        return bBuf.array();
    }
}
