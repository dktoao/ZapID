package tech.zapid.zaputil;

import java.util.HashMap;
import java.util.Map;

public class BiMap {

    private Map<Character, Integer> forward;
    private Map<Integer, Character> reverse;

    public BiMap() {
        forward = new HashMap<>();
        reverse = new HashMap<>();
    }

    public void put(Character k, Integer v) {
        forward.put(k, v);
        reverse.put(v, k);
    }

    public Integer getF(Character k) {
            return forward.get(k);
    }

    public Character getR(Integer v) {
        return reverse.get(v);
    }


}
