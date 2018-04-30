package tech.zapid.zaputil;

import java.nio.ByteBuffer;

public class BlockLetter {

    // Public members
//    public String message;
//    public byte[] code;
//    public byte[] hash;

    private static CipherMap charMap = new CipherMap();

    // WARNING: Altering the order of the inputs below will affect all codes that use the
    // blockletter encoding, always add to the bottom!
    static {
        charMap.put('\0', 0x00001000); // 0  -> 0x00 -> 000000
        charMap.put('0', 0x00e9d72e);  // 1  -> 0x01 -> 000001
        charMap.put('1', 0x01f210c4);  // 2  -> 0x02 -> 000010
        charMap.put('2', 0x01f1322e);  // 3  -> 0x03 -> 000011
        charMap.put('3', 0x00e8b22e);  // 4  -> 0x04 -> 000100
        charMap.put('4', 0x01087e31);  // 5  -> 0x05 -> 000101
        charMap.put('5', 0x00f83c3f);  // 6  -> 0x06 -> 000110
        charMap.put('6', 0x00e8bc3e);  // 7  -> 0x07 -> 000111
        charMap.put('7', 0x0011111f);  // 8  -> 0x08 -> 001000
        charMap.put('8', 0x00e8ba2e);  // 9  -> 0x09 -> 001001
        charMap.put('9', 0x01087a3e);  // 10 -> 0x0A -> 001010
        charMap.put('A', 0x0118fe2e);  // 11 -> 0x0B -> 001011
        charMap.put('B', 0x00f8be2f);  // 12 -> 0x0C -> 001100
        charMap.put('C', 0x00e8862e);  // 13 -> 0x0D -> 001101
        charMap.put('D', 0x00f8c62f);  // 14 -> 0x0E -> 001110
        charMap.put('E', 0x01f09c3f);  // 15 -> 0x0F -> 001111
        charMap.put('F', 0x00109c3f);  // 16 -> 0xA0 -> 010000
        charMap.put('G', 0x00e8e43e);  // 17 -> 0xA1 -> 010001
        charMap.put('H', 0x0118fe31);  // 18 -> 0xA2 -> 010010
        charMap.put('I', 0x01f2109f);  // 19 -> 0xA3 -> 010011
        charMap.put('J', 0x0032109f);  // 20 -> 0xA4 -> 010100
        charMap.put('K', 0x01149d31);  // 21 -> 0xA5 -> 010101
        charMap.put('L', 0x01f08421);  // 22 -> 0xA6 -> 010110
        charMap.put('M', 0x0118d771);  // 23 -> 0xA7 -> 010111
        charMap.put('N', 0x011cd671);  // 24 -> 0xA8 -> 011000
        charMap.put('O', 0x00e8c62e);  // 25 -> 0xA9 -> 011001
        charMap.put('P', 0x0010be2f);  // 26 -> 0xAA -> 011010
        charMap.put('Q', 0x0164c62e);  // 27 -> 0xAB -> 011011
        charMap.put('R', 0x0114be2f);  // 28 -> 0xAC -> 011100
        charMap.put('S', 0x00f8383e);  // 29 -> 0xAD -> 011101
        charMap.put('T', 0x0042109f);  // 30 -> 0xAE -> 011110
        charMap.put('U', 0x00e8c631);  // 31 -> 0xAF -> 011111
        charMap.put('V', 0x00454631);  // 32 -> 0xB0 -> 100000
        charMap.put('W', 0x00aac631);  // 33 -> 0xB1 -> 100001
        charMap.put('X', 0x01151151);  // 34 -> 0xB2 -> 100010
        charMap.put('Y', 0x00421151);  // 35 -> 0xB3 -> 100011
        charMap.put('Z', 0x01f1111f);  // 36 -> 0xB4 -> 100100
        charMap.put(',', 0x00420000);  // 37 -> 0xB5 -> 100101
        charMap.put('.', 0x00400000);  // 38 -> 0xB6 -> 100110
        charMap.put('!', 0x00401082);  // 39 -> 0xB7 -> 100111
        charMap.put('#', 0x00afabea);  // 40 -> 0xB8 -> 101000
        charMap.put('$', 0x00fa38be);  // 41 -> 0xB9 -> 101001
        charMap.put('(', 0x01042110);  // 42 -> 0xBA -> 101010
        charMap.put(')', 0x00110841);  // 43 -> 0xBB -> 101011
        charMap.put('+', 0x00023880);  // 44 -> 0xBC -> 101100
        charMap.put('-', 0x00003800);  // 45 -> 0xBD -> 101101
        charMap.put('=', 0x000701c0);  // 46 -> 0xBE -> 101110
        charMap.put('?', 0x0040322e);  // 47 -> 0xBF -> 101111
        charMap.put(':', 0x00020080);  // 48 -> 0xC0 -> 110000
        charMap.put(';', 0x00108020);  // 49 -> 0xC1 -> 110001
        charMap.put('/', 0x00111110);  // 50 -> 0xC2 -> 110010
        charMap.put('\\', 0x01041041); // 51 -> 0xC3 -> 110011
        charMap.put('&', 0x0164C8A2);  // 52 -> 0xC4 -> 110100
        charMap.put('"', 0x0000014A);  // 53 -> 0xC5 -> 110101
        charMap.put('\'', 0x00000084); // 54 -> 0xC6 -> 110110
        charMap.put(' ', 0x00000000);  // 55 -> 0xC7 -> 110111
        charMap.put('\n', 0x01ffffff); // 56 -> 0xC8 -> 111000
    }

/*
    public BlockLetter(String m) throws IllegalArgumentException {
        message = validMessage(m);
        code = encode(message);
        try {
            hash = getHash(message);
        } catch (NoSuchAlgorithmException err) {
            hash = new byte[] {0x0};
        }
    }
*/

    public static byte[] encode6(String m) throws IllegalArgumentException {

        // Validate the message
        m = validMessage(m);
        // Calculate the size of the needed buffer and allocate
        double fracByte = m.length() * (6.0 / 8.0);
        int nBytes = (int)Math.ceil(fracByte);
        ByteBuffer bBuff = ByteBuffer.allocate(nBytes);
        // Go through every 4 characters and encode them in 3 bytes
        int idxMessage = 0;
        byte first;
        byte second;
        //while (idxString < m.length()) {
        for (int idxTriple = 0; idxTriple < nBytes; idxTriple++) {
            if (idxTriple % 3 == 0) {
                // Pack the first byte
                first = (byte)(charMap.getIndex(m.charAt(idxMessage)) & 0x3F); // 0011 1111
                if (idxMessage+1 < m.length()) {
                    second = (byte) (charMap.getIndex(m.charAt(idxMessage + 1)) & 0x30); // 0011 0000
                } else {
                    second = (byte)0x00;
                }
                bBuff.put(idxTriple, (byte)((first << 2) | (second >> 4)));
            } else if (idxTriple % 3 == 1) {
                // pack the second byte
                first = (byte)(charMap.getIndex(m.charAt(idxMessage + 1)) & 0x0F); // 0000 1111
                if (idxMessage+2 < m.length()) {
                    second = (byte) (charMap.getIndex(m.charAt(idxMessage + 2)) & 0x3C); // 0011 1100
                } else {
                    second = (byte)0x00;
                }
                bBuff.put(idxTriple, (byte)((first << 4) | (second >> 2)));
            } else if (idxTriple % 3 == 2) {
                // pack the third byte
                first = (byte)(charMap.getIndex(m.charAt(idxMessage + 2)) & 0x03); // 0000 0011
                if (idxMessage+3 < m.length()) {
                    second = (byte) (charMap.getIndex(m.charAt(idxMessage + 3)) & 0x3F); // 0011 1111
                } else {
                    second = (byte)0x00;
                }
                bBuff.put(idxTriple, (byte)((first << 6) | second));
                // Update Message index
                idxMessage += 4;
            }
        }
        return bBuff.array();
    }

    public static String decode6(byte[] bArr) throws IllegalArgumentException {

        // Calculate the size of the string from the buffer
        double fracStrLen = bArr.length * (8.0 / 6.0);
        int nChars = (int)Math.floor(fracStrLen);
        char[] messageBuffer = new char[nChars];
        // Go through every 3 bytes and decode 4 characters
        int idxTriple = 0;
        byte first;
        byte second;
        //while (idxTriple < bArr.length) {
        for (int idxMessage = 0; idxMessage < nChars; idxMessage++) {
            if (idxMessage % 4 == 0) {
                // Retrieve the first character
                first = (byte) ((bArr[idxTriple] & 0xFC) >> 2);
                messageBuffer[idxMessage] = charMap.symbolAt((int)first);
            } else if (idxMessage % 4 == 1) {
                // Retrieve the second character
                first = (byte) ((bArr[idxTriple] & 0x03) << 4);
                second = (byte) ((bArr[idxTriple + 1] & 0xF0) >> 4);
                messageBuffer[idxMessage] = charMap.symbolAt((int)(first | second));
            } else if (idxMessage % 4 == 2) {
                // Retrieve the third character
                first = (byte) ((bArr[idxTriple + 1] & 0x0F) << 2);
                second = (byte) ((bArr[idxTriple + 2] & 0xC0) >> 6);
                messageBuffer[idxMessage] = charMap.symbolAt((int)(first | second));
            } else if (idxMessage % 4 == 3) {
                // Retrieve the last character
                second = (byte) ((bArr[idxTriple + 2] & 0x3F));
                messageBuffer[idxMessage] = charMap.symbolAt((int)second);
                // Update the index
                idxTriple += 3;
            }
        }
        String retStr = new String(messageBuffer);
        if (retStr.charAt(retStr.length()-1) == '\0') {
            retStr = retStr.substring(0, retStr.length()-1);
        }
        return retStr;
    }

    public static byte[] encode32(String m) throws IllegalArgumentException {

        // Validate the message
        m = validMessage(m);
        // Convert to byte
        ByteBuffer bBuff = ByteBuffer.allocate(m.length() * 4);
        for (int ii = 0; ii < m.length(); ii++)
            bBuff.putInt(ii*4, charMap.lookup(m.charAt(ii)));
        // Convert to big-endian byte array and return
        return bBuff.array();
    }

    public static String decode32(byte[] bArr) {

        // Decode the integer
        ByteBuffer bBuff = ByteBuffer.wrap(bArr);
        char[] cArr = new char[bArr.length / 4];
        for (int ii = 0; ii < cArr.length; ii++)
            cArr[ii] = charMap.lookup(bBuff.getInt(ii*4));
        // Convert to string and return
        return String.valueOf(cArr);

    }

    static String validMessage(String m) throws IllegalArgumentException {

        m = m.toUpperCase();
        for (int ii = 0; ii < m.length(); ii++) {
            if (charMap.lookup(m.charAt(ii)) == null)  {
                throw new IllegalArgumentException("Illegal character for initialization");
            }
        }
        return m;
    }
}
