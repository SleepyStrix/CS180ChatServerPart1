/**
 * Session Cookie
 *
 * Stores session id and time for a user
 *
 * @author Daniel Hewlett dhewlett@purdue.edu, Yonggun Yoon yoon126@purdue.edu
 *
 * @lab LC2, 807
 *
 * @version 11/20/15
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
