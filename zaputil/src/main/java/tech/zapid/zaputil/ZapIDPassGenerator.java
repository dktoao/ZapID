package tech.zapid.zaputil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ZapIDPassGenerator {

    public static void main(String[] args) {

        String inFile = args[0];
        String outFile = args[1];
        String version = args[2];

        // Open the input file for reading
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(inFile));
        } catch (FileNotFoundException x) {
            throw new Error("Could not find file");
        }

        // Loop through and read each line
        String line = null;
        List<String> fields;
        while (true) {
            try {
                line = in.readLine();
                if (line == null) {
                    break;
                }
            } catch (IOException x) {
                x.printStackTrace();
            }
            fields = CSVUtils.parseLine(line);
            System.out.printf("Ln0: %s, Ln1: %s\n", fields.get(0), fields.get(1));
        }
    }

        /* Old demo code
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
        */
}
