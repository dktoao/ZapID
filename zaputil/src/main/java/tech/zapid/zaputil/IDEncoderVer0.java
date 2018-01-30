package tech.zapid.zaputil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class IDEncoderVer0 implements IDEncoder {

    @Override
    public byte[] encode(String m) {

        // Parts of the message
        String message;
        String checksum;

        // hide the message
        Base64.Encoder b64 = Base64.getEncoder();
        m = m.toUpperCase();
        message = Util.byteToString(b64.encode(Util.stringToByte(BlockLetter.validMessage(m))));

        // Calculate the checksum
        checksum = md5(m);
        return Util.stringToByte(message + "@" + checksum);
    }

    @Override
    public String validate(byte[] code) throws InvalidIDCodeException {
        
        String message;
        String checksum;
        // Collect the message
        try {
            String codeStr = Util.byteToString(code);
            String[] parts = codeStr.split("@");
            Base64.Decoder b64 = Base64.getDecoder();
            message = Util.byteToString(b64.decode(Util.stringToByte(parts[0])));
            checksum = parts[1];
        } catch (Exception err) {
            throw new InvalidIDCodeException();
        }

        String newChecksum = md5(message);
        if (checksum.equals(newChecksum)) {
            return message;
        } else {
            throw new InvalidIDCodeException();
        }
    }

    private String md5(String m) {

        // Calculate the checksum
        MessageDigest md;
        Base64.Encoder b64 = Base64.getEncoder();
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException err) {
            throw new Error("Feature not supported");
        }
        md.update(Util.stringToByte(m));
        return Util.byteToString(b64.encode(md.digest()));
    }
}
