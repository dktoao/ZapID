/**
 * Test cases written for the BlockLetter Class
 */

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.junit.Assert.*;

import tech.zapid.zaputil.BlockLetter;

public class TestBlockLetter {

    @Test
    public void test6BitEncoder() {
        final String testMessage = "Test Message 1!";
        final byte[] testCode = new byte[] {
                (byte)0x78, (byte)0xF7, (byte)0x5E,
                (byte)0xDD, (byte)0x73, (byte)0xDD,
                (byte)0x74, (byte)0xB4, (byte)0x4F,
                (byte)0xDC, (byte)0x29, (byte)0xC0 };
        byte[] code = BlockLetter.encode6(testMessage);
        assertArrayEquals(code, testCode);
        String message = BlockLetter.decode6(code);
        assertTrue(testMessage.toUpperCase().equals(message));
    }

    @Test
    public void test32BitEncoder() {
        final String testMessage = "T M 2";
        final int[] testCode = new int[] {
                0x0042109f, 0x00000000, 0x0118d771, 0x00000000, 0x01f1322e };
        byte[] code = BlockLetter.encode32(testMessage);
        ByteBuffer bBuff = ByteBuffer.allocate(testCode.length * 4);
        IntBuffer iBuff = bBuff.asIntBuffer();
        iBuff.put(testCode);
        assertArrayEquals(code, bBuff.array());
    }

}
