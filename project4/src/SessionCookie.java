/**
 * Created by Yonggun on 11/17/2015.
 */
import java.util.Random;

public class SessionCookie {
    Random r = new Random();
    long id = r.nextInt(10000);
    public static int timeoutLength = 300000;
    long currentTime = System.currentTimeMillis();

    public boolean hasTimedOut() throws InterruptedException {
        if (currentTime - System.currentTimeMillis() < timeoutLength) {
            return false;
        }
        else
            return true;
    }

    public void updateTimeOfActivity() {
        this.currentTime = System.currentTimeMillis();
    }

    public long getID() {
        return id;
    }

    public SessionCookie(long id) {
        this.id = id;
    }
}
