import java.text.DecimalFormat;

/**
 * Created by Dan on 11/15/2015.
 */
public class CircularBuffer {

    private String[] buffer;
    private int pointer = 0;
    private int messageCount = 0;
    private int numAvailable = 0;


    public CircularBuffer(int size) {
        this.buffer = new String[size];
    }

    public void put(String message) {
        DecimalFormat df = new DecimalFormat("0000");
        String fourDigit = df.format(messageCount);
        String result = fourDigit + ") " + message;
        buffer[pointer] = result;
        messageCountUp();
        pointerUp();
        if (numAvailable < buffer.length) {
            numAvailable++;
        }

    }

    public String[] getNewest(int numMessages) {
        if (numMessages < 0) {
            return null;
        }
        if (numMessages == 0) {
            return new String[0];
        }
        int minNum = Math.min(numAvailable, numMessages);
        if (minNum <= 0) {
            return new String[0];
        }
        String[] result = new String[minNum];
        int count = minNum - 1;
        for (int i = lastPointer(pointer); count >= 0;) {
            System.out.println("Count: "+ count + " i: " + i);
            System.out.println("num available:" + numAvailable);
            result[count] = buffer[i];
            System.out.println(result[count]);
            i = lastPointer(i);
            count--;
        }
        return result;
    }

    private void pointerUp() {
        pointer = nextPointer(pointer);
    }

    private void messageCountUp() {
        messageCount = nextMessageCount();
    }

    private int nextPointer(int from) {
        int next = from + 1;
        if (next >= buffer.length) {
            next = 0;
        }
        return next;
    }

    private int nextMessageCount() {
        int next = messageCount + 1;
        if (next > 9999) {
            next = 0;
        }
        return next;
    }

    private int lastPointer(int from) {
        int next = from - 1;
        if (next < 0) {
            next = buffer.length - 1;
        }
        return next;
    }

}
