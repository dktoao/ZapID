package tech.zapid.zaputil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Class for reading and writing TSV files that contain a batch of IDs
 */
public class BatchID {

    private enum CsvFileSection {NA, GLOBAL, HEADER, INDIVIDUAL, END}

    public int length;

    private String[] globalFields;
    private String[] globalValues;
    private String[] individualFields;
    private String[][] individualValues;

    public BatchID(String inFile) {

        // Open the input file for reading
        BufferedReader batch;
        try {
            batch = new BufferedReader(new FileReader(inFile));
        } catch (FileNotFoundException x) {
            throw new Error("Could not find file");
        }

        // Loop through and read each line
        String line;
        String[] sepLine;
        List<String> fields = new ArrayList<>();
        List<String> values = new ArrayList<>();
        List<String[]> arrValues = new ArrayList<>();
        CsvFileSection section = CsvFileSection.NA;
        while (true) {
            try {
                line = batch.readLine();
                if (line == null) {
                    section = CsvFileSection.END;
                }
            } catch (IOException x) {
                throw new Error("Could not find file");
            }
            if (line == null) {
                sepLine = new String[1];
                sepLine[0] = "";
            } else {
                sepLine = line.split("\t");
            }
            // Find what section we are in
            if (sepLine[0].equals("GLOBAL")) {
                section = CsvFileSection.GLOBAL;
                continue;
            } else if (sepLine[0].equals("INDIVIDUAL")) {
                section = CsvFileSection.HEADER;
                // Update global fields
                globalFields = new String[fields.size()];
                globalFields = fields.toArray(globalFields);
                globalValues = new String[values.size()];
                globalValues = values.toArray(globalValues);
                // Reset fields for individual use
                fields = new ArrayList<>();
                continue;
            } else if (section == CsvFileSection.END) {
                // Update individual values here
                individualValues = new String[arrValues.size()][arrValues.get(0).length];
                for (int ii = 0; ii < arrValues.size(); ii++) {
                    individualValues[ii] = arrValues.get(ii);
                }
                length = arrValues.size();
                break;
            }
            // Logic depending on the section
            switch (section) {
                case NA:
                    break;
                case GLOBAL:
                    fields.add(sepLine[0]);
                    values.add(sepLine[1]);
                    break;
                case HEADER:
                    individualFields = sepLine;
                    section = CsvFileSection.INDIVIDUAL;
                    break;
                case INDIVIDUAL:
                    arrValues.add(sepLine);
                    break;
            }
        }
        try {
            batch.close();
        } catch (IOException x) {
            x.printStackTrace();
            throw new Error("Could not close the file");
        }
    }

    public void writeSignedFile(String outFile, int version) {

        // Open the output file
        BufferedWriter batch;
        try {
            batch = new BufferedWriter(new FileWriter(outFile));
        } catch (IOException x) {
            throw new Error("Could not open output file");
        }

        // Write the Global variables
        try {
            batch.write("GLOBAL\n");
        } catch (IOException x) {
            x.printStackTrace();
            throw new Error("Could not write to file.");
        }
        StringJoiner joiner;
        for (int ii = 0; ii < globalFields.length; ii++) {
            joiner = new StringJoiner("\t");
            joiner.add(globalFields[ii]);
            joiner.add(globalValues[ii]);
            try {
                batch.write(joiner.toString() + "\n");
            } catch (IOException x) {
                x.printStackTrace();
                throw new Error("Could not write to file.");
            }
        }

        // Write the local variables
        try {
            batch.write("INDIVIDUAL\n");
        } catch (IOException x) {
            x.printStackTrace();
            throw new Error("Could not write to file");
        }
        joiner = new StringJoiner("\t");
        for (int ii = 0; ii < individualFields.length; ii++) {
            joiner.add(individualFields[ii]);
        }
        joiner.add("__ZapID");
        try {
            batch.write(joiner.toString() + "\n");
        } catch (IOException x) {
            x.printStackTrace();
            throw new Error("Could not write to file");
        }
        for (int ii = 0; ii < individualValues.length; ii++) {
            joiner = new StringJoiner("\t");
            for (int jj = 0; jj < individualValues[0].length; jj++) {
                joiner.add(individualValues[ii][jj]);
            }
            byte[] qrCode;
            try {
                qrCode = Util.getQRCode(getMessage(ii), version);
            } catch (IndexOutOfBoundsException x) {
                throw new Error("Invalid pass version");
            }
            joiner.add(Util.byteToString(qrCode));
            try {
                batch.write(joiner.toString() + "\n");
            } catch (IOException x) {
                x.printStackTrace();
                throw new Error("Could not write to file");
            }
        }
    }

    public String getMessage(int index) {

        String message = "";
        String field;
        String value;
        // Do globals
        for (int ii = 0; ii < globalFields.length; ii++) {
            field = globalFields[ii];
            value = globalValues[ii];
            message = addMessageLine(message, field, value);
        }
        // Do individuals
        for (int ii = 0; ii < individualFields.length; ii++) {
            field = individualFields[ii];
            value = individualValues[index][ii];
            message = addMessageLine(message, field, value);
        }
        return message;
    }

    private static String addMessageLine(String old, String field, String value) {

        String returnStr;
        if (field.charAt(0) == '_') {
            if (field.charAt(1) == '_') {
                returnStr = old;
            } else {
                if (old.equals("")) {
                    returnStr = value;
                } else {
                    returnStr = (old + "\n" + value);
                }
            }
        } else {
            if (old.equals("")) {
                returnStr = (field + ": " + value);
            } else {
                returnStr = (old + "\n" + field + ": " + value);
            }
        }
        return returnStr;
    }
}
