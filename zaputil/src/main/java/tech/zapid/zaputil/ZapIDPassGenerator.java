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
                                    + fields.get(0)
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
                    message = BlockLetter.validMessage(message);
                    System.out.println(message);
                    byte[] code = Util.getQRCode(message, version);
                    String message2 = null;
                    try {
                        message2 = Util.validateQRCode(code);
                    } catch (InvalidIDCodeException x) {
                        System.out.println("Code is not valid (but it should be)");
                    }
                    System.out.println(Util.byteToString(code));
                    if (message.equals(message2)) {
                        System.out.println("Great! The codes match!");
                    } else {
                        System.out.println("Crap! The codes do not match!");
                    }
                    break;
            }
        }
    }
}
