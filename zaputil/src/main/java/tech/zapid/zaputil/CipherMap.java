package tech.zapid.zaputil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class CipherMap {

    private class CipherInfo {
        Character symbol;
        Integer code;

        CipherInfo(Character s, Integer c) {
            symbol = s;
            code = c;
        }
    }

    private Map<Character, Integer> symbolMap;
    private Map<Integer, Integer> codeMap;
    private ArrayList<CipherInfo> lookupList;
    private Integer length;

    CipherMap() {
        symbolMap = new HashMap<>();
        codeMap = new HashMap<>();
        lookupList = new ArrayList<>();
        length = 0;
    }

    void put(char s, int c) {
        if (length >= 64) {
            throw new Error("CipherMap can only contain up to 64 items.");
        }
        symbolMap.put(s, length);
        codeMap.put(c, length);
        lookupList.add(new CipherInfo(s, c));
        length++;
    }

    public Integer length() {
        return length;
    }

    Integer getIndex(Character s) throws IllegalArgumentException {
        Integer idx = symbolMap.get(s);
        if (idx == null) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The symbol %s has not been included in the cipher", s));
        }
        return idx;
    }

    Integer getIndex(Integer c) throws IllegalArgumentException {
        Integer idx = codeMap.get(c);
        if (idx == null) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The code %d has not been included in the cipher", c));
        }
        return idx;
    }

    Integer lookup(Character s) throws IllegalArgumentException {
        Integer idx = symbolMap.get(s);
        if (idx == null) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The symbol %s has not been included in the cipher", s));
        }
        return lookupList.get(idx).code;
    }

    Character lookup(Integer c) throws IllegalArgumentException {
        Integer idx = codeMap.get(c);
        if (idx == null) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The code %d has not been included in the cipher", c));
        }
        return lookupList.get(idx).symbol;
    }

    Character symbolAt(Integer idx) throws IllegalArgumentException {
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The provided index %d is out of bounds", idx));
        }
        return lookupList.get(idx).symbol;
    }

    Integer codeAt(Integer idx) throws IllegalArgumentException {
        if (idx < 0 || idx >= length) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "The provided index %d is out of bounds", idx));
        }
        return lookupList.get(idx).code;
    }
}