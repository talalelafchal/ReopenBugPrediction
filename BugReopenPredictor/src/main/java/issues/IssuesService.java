package issues;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talal on 24.04.17.
 */
public class IssuesService {

    String username;
    String password;
    int numberOfPages;
    String owner;
    String repo;

    public IssuesService(String username, String password, int numberOfPages, String owner, String repo) {
        this.username = username;
        this.password = password;
        this.numberOfPages = numberOfPages;
        this.owner = owner;
        this.repo = repo;
    }

    public int getNumberOfReopenBug() throws ParseException {
        int counter = 0;
        int page = 1;
        for (int i = 0; i <= numberOfPages; i++) {
            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues?state=all&per_page=1000&page=" + page;
            JSONArray allIssuesArray = new GitHubRESTService(username, password, url).getJsonArrayResponse();
            for (int j = 0; j < allIssuesArray.size(); j++) {
                JSONObject jsonIssue = (JSONObject) allIssuesArray.get(j);
                String number = String.valueOf((long) jsonIssue.get("number"));
                JSONArray jsonArrayLabels = (JSONArray) jsonIssue.get("labels");
                for (int k = 0; k < jsonArrayLabels.size(); k++) {
                    JSONObject jsonObjectLabel = (JSONObject) jsonArrayLabels.get(k);
                    String labelName = (String) jsonObjectLabel.get("name");
                    if (labelName != null && labelName.equals("bug")) {
                        url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + number + "/events?per_page=100";
                        JSONArray jsonArrayEvent = new GitHubRESTService(username, password, url).getJsonArrayResponse();
                        //check reopen bug
                        for (int l = 0; l < jsonArrayEvent.size(); l++) {
                            JSONObject objectEvent = (JSONObject) jsonArrayEvent.get(l);
                            String event = (String) objectEvent.get("event");
                            if (event.equals("reopened")) {
                                counter++;
                            }
                        }
                    }
                }
            }
        }
        return counter;
    }


    public void createBugJsonIssuesFile(String fileName) throws ParseException, IOException, InterruptedException {
        List<BugIssue> bugIssueList = createJsonIssues();
        System.out.println("# of bugs issues = " + bugIssueList.size());
        JSONArray bugIssueArray = createJsonObject(bugIssueList);
        writeJsonArrayToFile(bugIssueArray, fileName);
    }


    private List<BugIssue> createJsonIssues() throws ParseException, InterruptedException {
        int requestCounter = 0;
        int page = 1;
        ArrayList<BugIssue> bugIssuesList = new ArrayList();
        for (int i = 0; i <= numberOfPages; i++) {
            String url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues?state=all&per_page=1000&page=" + page;
            if (requestCounter == 4999) {
                System.out.println("sleeping ......");
                System.out.println(System.currentTimeMillis());
                Thread.sleep(1000 * 60 * 62);
                System.out.println("working ......");
                requestCounter = 0;
            }
            JSONArray allIssuesArray = new GitHubRESTService(username, password, url).getJsonArrayResponse();
            requestCounter++;
            for (int j = 0; j < allIssuesArray.size(); j++) {
                JSONObject jsonIssue = (JSONObject) allIssuesArray.get(j);
                String number = String.valueOf((long) jsonIssue.get("number"));
                //labels
                JSONArray jsonArrayLabels = (JSONArray) jsonIssue.get("labels");
                for (int k = 0; k < jsonArrayLabels.size(); k++) {
                    JSONObject jsonObjectLabel = (JSONObject) jsonArrayLabels.get(k);
                    String labelName = (String) jsonObjectLabel.get("name");
                    if (labelName != null && labelName.equals("bug")) {
                        String id = String.valueOf((long) jsonIssue.get("id"));
                        String title = (String) jsonIssue.get("title");
                        String description = (String) jsonIssue.get("body");
                        String openedOn = (String) jsonIssue.get("created_at");
                        String closedOn = (String) jsonIssue.get("closed_at");
                        JSONObject user = (JSONObject) jsonIssue.get("user");
                        String openedBy = (String) user.get("login");

                        long commentsNumber = (long) jsonIssue.get("comments");
                        //get comments
                        List<CommentObject> commentList = new ArrayList<>();
                        if (commentsNumber > 0) {

                            url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + number + "/comments?per_page=100";
                            if (requestCounter == 4999) {
                                System.out.println("Sleeping .....");
                                System.out.println(System.currentTimeMillis());
                                Thread.sleep(1000 * 60 * 62);
                                requestCounter = 0;
                                System.out.println("working .....");
                            }
                            JSONArray jsonCommentsArray = new GitHubRESTService(username, password, url).getJsonArrayResponse();
                            requestCounter++;
                            for (int m = 0; m < jsonCommentsArray.size(); m++) {
                                JSONObject object = (JSONObject) jsonCommentsArray.get(m);
                                String date = (String) object.get("created_at");
                                String text = (String) object.get("body");
                                JSONObject userComment = (JSONObject) object.get("user");
                                String author = (String) userComment.get("login");
                                CommentObject commentObject = new CommentObject(author, date, text);
                                commentList.add(commentObject);
                            }

                        }


                        url = "https://api.github.com/repos/" + owner + "/" + repo + "/issues/" + number + "/events?per_page=100";
                        if (requestCounter == 4999) {
                            System.out.println("Sleeping .....");
                            System.out.println(System.currentTimeMillis());
                            Thread.sleep(1000 * 60 * 62);
                            requestCounter = 0;
                            System.out.println("Working .....");
                        }
                        JSONArray jsonArrayEvent = new GitHubRESTService(username, password, url).getJsonArrayResponse();
                        requestCounter++;
                        //check for reopen Bug
                        boolean reopen = false;
                        for (int l = 0; l < jsonArrayEvent.size(); l++) {
                            JSONObject objectEvent = (JSONObject) jsonArrayEvent.get(l);

                            String event = (String) objectEvent.get("event");
                            if (event.equals("reopened")) {
                                System.out.println("---------------- reopened bug --------------");
                                System.out.println(number);
                                System.out.println("---------------------------------------------");
                                reopen = true;
                            }
                        }

                        BugIssue bugIssue = new BugIssue(id, number, title, description, commentList, openedBy, openedOn, reopen, closedOn);
                        bugIssuesList.add(bugIssue);

                    }

                }

            }
            page++;
        }
        return bugIssuesList;

    }


    private JSONArray createJsonObject(List<BugIssue> bugIssuesList) {

        JSONArray issueJSONArray = new JSONArray();
        for (BugIssue bugIssue : bugIssuesList
                ) {
            JSONObject bugIssueJson = new JSONObject();

            bugIssueJson.put("id", bugIssue.getIssueid());
            bugIssueJson.put("number", bugIssue.getNumber());
            bugIssueJson.put("title", bugIssue.getTitle());
            bugIssueJson.put("description", bugIssue.getDescription());
            bugIssueJson.put("opened_by", bugIssue.getOpenedBy());
            bugIssueJson.put("opened_on", bugIssue.getOpenedOn());
            bugIssueJson.put("reopen", bugIssue.isReopen());
            bugIssueJson.put("closed_on", bugIssue.getClosedOn());

            // comments
            JSONArray allCommentsArray = new JSONArray();
            for (CommentObject commentObject : bugIssue.getCommentList()
                    ) {
                JSONObject commentParameter = new JSONObject();
                commentParameter.put("author", commentObject.getAuthor());
                commentParameter.put("date", commentObject.getDate());
                commentParameter.put("text", commentObject.getText());
                allCommentsArray.add(commentParameter);
            }
            bugIssueJson.put("comments", allCommentsArray);
            issueJSONArray.add(bugIssueJson);
        }

        return issueJSONArray;
    }


    private void writeJsonArrayToFile(JSONArray array, String fileName) throws IOException {
        FileWriter jsonWriter = new FileWriter(fileName);
        array.writeJSONString(jsonWriter);
        jsonWriter.close();
    }
}
