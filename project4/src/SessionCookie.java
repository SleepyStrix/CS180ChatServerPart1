/**
 * Created by Yonggun on 11/17/2015.
 */
import java.util.Random;

public class SessionCookie {
    private long id;
    public static int timeoutLength = 300;
    private long startTime;

    public SessionCookie() {
        this(new Random().nextInt(10000));
    }

    public SessionCookie(long id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
    }

    public boolean hasTimedOut() {
        if (System.currentTimeMillis() - startTime <= (timeoutLength * 1000)) {
            return false;
        }
        else {
            return true;
        }
    }

    public void updateTimeOfActivity() {
        this.startTime = System.currentTimeMillis();
    }

    public long getID() {
        return this.id;
    }
}
