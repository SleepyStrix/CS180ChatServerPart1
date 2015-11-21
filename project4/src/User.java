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
import java.util.*;

public class User {

    private String username;
    private String password;
    private SessionCookie cookie;

    public User(String username, String password, SessionCookie cookie) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
    }

    public String getName() {
        return this.username;
    }

    public boolean checkPassword(String password) {
        if (this.password.equals(password)) {
            return true;
        }
        else {
            return false;
        }
    }

    public SessionCookie getCookie() {
        return this.cookie;
    }

    public void setCookie(SessionCookie cookie) {
        this.cookie = cookie;
    }
}
