package util;

import issues.GitHubRESTService;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * Created by Talal on 30.04.17.
 */
public class LoginNameService {
    String login;
    String url = "https://api.github.com/users/";

    public LoginNameService(String login) {
        this.login = login;
    }
    public String getName() throws ParseException {
        String requstUrl = url + login;
        JSONObject userObject = new GitHubRESTService("talalelafchal", "ZI6S5N7VkuPW", requstUrl).getJsonObjectResponse();
        return (String) userObject.get("name");
    }

}
