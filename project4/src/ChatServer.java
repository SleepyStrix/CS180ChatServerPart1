import java.util.*;

/**
 * <b> CS 180 - Project 4 - Chat Server Skeleton </b>
 * <p>
 * 
 * This is the skeleton code for the ChatServer Class. This is a private chat
 * server for you and your friends to communicate.
 * 
 * @author (Your Name) <(YourEmail@purdue.edu)>
 * 
 * @lab (Your Lab Section)
 * 
 * @version (Today's Date)
 *
 */
public class ChatServer {

    private User[] users;
    private int maxMessages;
    private CircularBuffer cb;
    private int userCount = 0;
	
	public ChatServer(User[] users, int maxMessages) {
        this.users = users;
        this.userCount = users.length;
        this.users = Arrays.copyOf(this.users, 100);
        this.maxMessages = maxMessages;
        cb = new CircularBuffer(this.maxMessages);
		// TODO Complete the constructor
	}

	/**
	 * This method begins server execution.
	 */
	public void run() {
		boolean verbose = false;
		System.out.printf("The VERBOSE option is off.\n\n");
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.printf("Input Server Request: ");
			String command = in.nextLine();

			// this allows students to manually place "\r\n" at end of command
			// in prompt
			command = replaceEscapeChars(command);

			if (command.startsWith("kill"))
				break;

			if (command.startsWith("verbose")) {
				verbose = !verbose;
				System.out.printf("VERBOSE has been turned %s.\n\n", verbose ? "on" : "off");
				continue;
			}

			String response = null;
			try {
				response = parseRequest(command);
			} catch (Exception ex) {
				response = MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR,
						String.format("An exception of %s occurred.", ex.getMessage()));
			}

			// change the formatting of the server response so it prints well on
			// the terminal (for testing purposes only)
			if (response.startsWith("SUCCESS\t"))
				response = response.replace("\t", "\n");

			// print the server response
			if (verbose)
				System.out.printf("response:\n");
			System.out.printf("\"%s\"\n\n", response);
		}

		in.close();
	}

	/**
	 * Replaces "poorly formatted" escape characters with their proper values.
	 * For some terminals, when escaped characters are entered, the terminal
	 * includes the "\" as a character instead of entering the escape character.
	 * This function replaces the incorrectly inputed characters with their
	 * proper escaped characters.
	 * 
	 * @param str
	 *            - the string to be edited
	 * @return the properly escaped string
	 */
	private static String replaceEscapeChars(String str) {
		str = str.replace("\\r", "\r");
		str = str.replace("\\n", "\n");
		str = str.replace("\\t", "\t");

		return str;
	}

	/**
	 * Determines which client command the request is using and calls the
	 * function associated with that command.
	 * 
	 * @param request
	 *            - the full line of the client request (CRLF included)
	 * @return the server response
	 */
	public String parseRequest(String request) {
        request = replaceEscapeChars(request);
        int endIndex = request.indexOf("\r\n");
        if (endIndex == -1) {
            return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
        }
        request = request.substring(0, endIndex);
        String[] parsed = request.split("\t");
        /*if (parsed.length < 3) {
            return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
        }*/

        switch (parsed[0]) {
            case ("ADD-USER") : {
                //check if valid number of arguments
                if (parsed.length != 4) {
                    return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
                }
                //find user
                User u = findUserByCookie(parsed[1]);
                if (u == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);
                }
                //check if cookie null
                if (u.getCookie() == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR);
                }
                //check for cookie timeout
                if (u.getCookie().hasTimedOut()) {
                    u.setCookie(null);
                    return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                }
                return addUser(parsed);
            }
            case ("USER-LOGIN") : {
                if (parsed.length != 3) {
                    return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
                }
                return userLogin(parsed);
            }
            case ("POST-MESSAGE") : {
                if (parsed.length != 3) {
                    return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
                }
                //find user
                User u = findUserByCookie(parsed[1]);
                if (u == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);
                }
                //check if cookie null
                if (u.getCookie() == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR);
                }
                //check for cookie timeout
                if (u.getCookie().hasTimedOut()) {
                    u.setCookie(null);
                    return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                }
                return postMessage(parsed, parsed[0]);
            }

            case ("GET-MESSAGES") : {
                if (parsed.length != 3) {
                    return MessageFactory.makeErrorMessage(MessageFactory.FORMAT_COMMAND_ERROR);
                }
                //find user
                User u = findUserByCookie(parsed[1]);
                if (u == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.USERNAME_LOOKUP_ERROR);
                }
                //check if cookie null
                if (u.getCookie() == null) {
                    return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_ERROR);
                }
                //check for cookie timeout
                if (u.getCookie().hasTimedOut()) {
                    u.setCookie(null);
                    return MessageFactory.makeErrorMessage(MessageFactory.COOKIE_TIMEOUT_ERROR);
                }
                return getMessages(parsed);
            }

            default : {
                return MessageFactory.makeErrorMessage(MessageFactory.UNKNOWN_COMMAND_ERROR);
            }
        }
	}

	public String addUser(String[] args) {
        long cookieID = Long.parseLong(args[1]);
        String username = args[2];
        String password = args[3];
        if (!stringIsAlphanumeric(username)) {
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR, "Invalid username format: " +
                    username);
        }
        if (!stringIsAlphanumeric(password)) {
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR, "Invalid password format: " +
                    password);
        }
        if (username.length() < 1 || username.length() > 20){
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR, "Invalid username length: " +
                    username);
        }
        if (password.length() < 4 || password.length() > 40){
            return MessageFactory.makeErrorMessage(MessageFactory.INVALID_VALUE_ERROR, "Invalid password length: " +
                    password);
        }
        users[userCount] = new User(username, password, new SessionCookie());
        userCount++;
		return "SUCCESS\r\n";
	}

	public String userLogin(String[] args) {
        // TODO: Replace the following code with the actual code
		return null;
	}

	public String postMessage(String[] args, String name) {
        // TODO: Replace the following code with the actual code
		return null;
	}

    public String getMessages(String[] args) {
        // TODO: Replace the following code with the actual code
        return null;
    }

    /**
     * Finds user in users array by name
     * @param name name of user to search for
     * @return user, else null
     */
    User findUser(String name) {
        for (User u : users) {
            if (u.getName().equals(name)){
                return u;
            }
        }
        return null;
    }

    User findUserByCookie(String idString) {
       int id = Integer.parseInt(idString);
        for (User u : users) {
            if (u.getCookie().getID() == id) {
                return u;
            }
        }
        return null;
    }

    boolean stringIsAlphanumeric(String s) {
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }


}
