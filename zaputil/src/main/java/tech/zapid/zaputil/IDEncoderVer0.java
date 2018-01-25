package tech.zapid.zaputil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class IDEncoderVer0 implements IDEncoder {

    @Override
    public byte[] encode(String m) {

        // Parts of the message
        String message;
        String checksum;

        // hide the message
        Base64.Encoder b64 = Base64.getEncoder();
        message = Util.byteToString(b64.encode(BlockLetter.encode(m)));

        // Calculate the checksum
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException err) {
            throw new Error("Feature not supported");
        }
        md.update(Util.stringToByte(m));
        checksum = Util.byteToString(md.digest());
        return Util.stringToByte(message + "@" + checksum);
    }

    @Override
    public String validate(byte[] code) throws InvalidIDCodeException {
        
        String message;

        // Collect the message
        try {
            String codeStr = Util.byteToString(code);
            String[] parts = codeStr.split("@");
            Base64.Decoder b64 = Base64.getDecoder();
            message = BlockLetter.decode(b64.decode(Util.stringToByte(parts[0])));
        } catch (Exception err) {
            throw new InvalidIDCodeException();
        }

        byte[] checkCode = encode(message);
        if (Arrays.equals(code, checkCode)) {
            return message;
        } else {
            throw new InvalidIDCodeException();
        }
    }
}
