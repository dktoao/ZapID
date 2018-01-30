package tech.zapid.zaputil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZapIDPassGenerator {

    private enum CsvFileSection {NA, GLOBAL, HEADER, INDIVIDUAL}

    public static void main(String[] args) {

        String inFile = args[0];
        String outFile = args[1];
        int version = Integer.parseInt(args[2]);

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
        String globalInfo = null;
        Map<Integer, String> localInfo = new HashMap<>();
        CsvFileSection section = CsvFileSection.NA;
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
            // Find what section we are in
            if ( fields.get(0).equals("GLOBAL")) {
                section = CsvFileSection.GLOBAL;
                continue;
            } else if (fields.get(0).equals("INDIVIDUAL")) {
                section = CsvFileSection.HEADER;
                continue;
            }
            // Logic depending on the section
            switch (section) {
                case NA:
                    break;
                case GLOBAL:
                    if (fields.get(0).charAt(0) == '_') {
                        if (fields.get(0).charAt(1) == '_') {
                            continue;
                        } else {
                            if (globalInfo == null) {
                                globalInfo = fields.get(1);
                            } else {
                                globalInfo += ("\n" + fields.get(1));
                            }
                        }
                    } else {
                        if (globalInfo == null) {
                            globalInfo = (fields.get(0)
                                    + ": "
                                    + fields.get(1));
                        } else {
                            globalInfo += ("\n"
                                    + fields.get(0).substring(1)
                                    + ": "
                                    + fields.get(1));
                        }
                    }
                    break;
                case HEADER:
                    for (int ii = 0; ii < fields.size(); ii++) {
                        String field = fields.get(ii);
                        if (field.charAt(0) == '_') {
                            if (field.charAt(1) =='_') {
                                continue;
                            } else {
                                localInfo.put(ii, "%s");
                            }
                        } else {
                            localInfo.put(ii, (field + ": %s"));
                        }
                    }
                    section = CsvFileSection.INDIVIDUAL;
                    break;
                case INDIVIDUAL:
                    String message = globalInfo;
                    for (int key: localInfo.keySet()) {
                        if (message == null) {
                            message = String.format(localInfo.get(key), fields.get(key));
                        } else {
                            message += ("\n" + String.format(localInfo.get(key), fields.get(key)));
                        }
                    }
                    System.out.println(message);
                    System.out.println(Util.byteToString(Util.getQRCode(message, version)));
                    break;
            }

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
