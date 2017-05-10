package issues;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;


/**
 * Created by Talal on 23.03.17.
 */
public class GitHubRESTService {

    String user;
    String password;
    String url;
    String output;

    public GitHubRESTService(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
        output = getOutputResponse();
    }

    public JSONArray getJsonArrayResponse() throws ParseException {
        JSONParser parser_obj = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser_obj.parse(output);
        return jsonArray;
    }

    public JSONObject getJsonObjectResponse() throws ParseException {
        JSONParser parser_obj = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser_obj.parse(output);
        return jsonObject;
    }

    public String getOutputResponse() {
        String output = null;
        try {
            Client client = Client.create();
            WebResource webResource = client
                    .resource(url);
            client.addFilter(new HTTPBasicAuthFilter(user, password));

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                System.out.println(response.toString());
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            output = response.getEntity(String.class);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return output;
    }


}
