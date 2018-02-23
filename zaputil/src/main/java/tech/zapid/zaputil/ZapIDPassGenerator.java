package tech.zapid.zaputil;

public class ZapIDPassGenerator {

    public static void main(String[] args) {

        String inFile = args[0];
        String outFile = args[1];
        int version = Integer.parseInt(args[2]);

        BatchID batch = new BatchID(inFile);
        for (int ii = 0; ii < batch.length; ii++) {
            String message = batch.getMessage(ii);
            System.out.print(message);
            System.out.print("\n");
            byte[] code = Util.getQRCode(message, version);
            System.out.print(Util.byteToString(code));
            System.out.print("\n");
            try {
                Util.validateQRCode(code);
                System.out.println("Code is valid :)");
            } catch (InvalidIDCodeException x) {
                System.out.println("Code is invalid :(");
            }
        }
        batch.writeSignedFile(outFile, version);
    }
}
