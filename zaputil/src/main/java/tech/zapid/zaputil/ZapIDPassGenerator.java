package tech.zapid.zaputil;

public class ZapIDPassGenerator {

    public static void main(String[] args) {

        // Make a string and code it up
        String message = "100 N Road B, Seattle WA\nLIC: XQL-053\nEXP: 12/12/20";
        System.out.println("Message:\n" + message);
        byte [] qrCode;
        try {
            qrCode = Util.getQRCode(message, 0);
        } catch (Exception exc) {
            System.out.println("Fail!");
            return;
        }
        System.out.println("QR Code: " + Util.byteToString(qrCode));

        // Make sure that we can decode and validate it
        String newMessage;
        try {
            newMessage = Util.validateQRCode(qrCode);
            System.out.println("Code Valid for:\n" + newMessage);
        } catch (InvalidIDCodeException exc) {
            System.out.println("Code is not valid! (but it should be)");
        }
    }
}
