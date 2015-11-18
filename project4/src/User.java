/**
 * Created by Yonggun on 11/17/2015.
 */
import java.util.*;

public class User {
    String username ="";
    String password ="";
    SessionCookie cookie ;
    Scanner s = new Scanner(System.in);

    public User(String username, String password, SessionCookie cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getName() {
        return username;
    }

    public boolean checkPassword(String password) {
        if(s.hasNextLine()) {
            if (s.nextLine() == password) {
                return true;
            }
        }
        return false;
    }

    public SessionCookie getCookie() {
        return cookie;
    }

    public void setCookie(SessionCookie cookie) {
        this.cookie = cookie;
    }
}
