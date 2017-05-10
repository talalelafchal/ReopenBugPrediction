package issues;

import com.google.gson.JsonObject;
import issues.BugIssue;
import issues.CommentObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talal on 30.04.17.
 */

public class IssueConverter {
    public String jsonFilePath;

    public IssueConverter(String jsonfilePath) {
        this.jsonFilePath = jsonfilePath;
    }

    public List<BugIssue> getIssuesList() throws IOException, ParseException {
        List<BugIssue> bugIssuesList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(jsonFilePath));
        for (Object jsonObject : jsonArray) {
            JSONObject jsonBugIssue = (JSONObject) jsonObject;

            String issueId = (String) jsonBugIssue.get("id");
            String number = (String) jsonBugIssue.get("number");
            String title = (String) jsonBugIssue.get("title");
            String description = (String) jsonBugIssue.get("description");
            String openedBy = (String) jsonBugIssue.get("opened_by");
            String openedOn = (String) jsonBugIssue.get("opened_on");
            boolean reopened = (boolean) jsonBugIssue.get("reopen");
            String reopenOn = (String) jsonBugIssue.get("reopenOn");
            String closedOn = (String) jsonBugIssue.get("closed_on");
            String closedBy = (String) jsonBugIssue.get("closed_by");
            List<CommentObject> commentList = new ArrayList<>();
            JSONArray comments = (JSONArray) jsonBugIssue.get("comments");

            if (comments != null) {
                for (Object comment : comments) {
                    JSONObject commentJsonObject = (JSONObject) comment;
                    String author = (String) commentJsonObject.get("author");
                    String date = (String) commentJsonObject.get("date");
                    String text = (String) commentJsonObject.get("text");
                    CommentObject commentObject = new CommentObject(author, date, text);
                    commentList.add(commentObject);
                }
            }
            BugIssue bugIssue = new BugIssue(issueId, number, title, description, commentList, openedBy, openedOn, reopened, closedOn, reopenOn, closedBy);
            bugIssuesList.add(bugIssue);

        }
        return bugIssuesList;
    }
}
