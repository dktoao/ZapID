package tech.zapid.zaputil;

interface IDEncoder {

    byte[] encode(String m);
    String validate(byte[] code) throws InvalidIDCodeException;

}
