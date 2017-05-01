package util;

import issues.BugIssue;
import issues.CommentObject;
import issues.IssueConverter;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Talal on 30.04.17.
 */
public class NameLoginMapFile {
    private static final String DELIMITER = " = ";
    String path = "output/elasticsearchBugIssues.json";
    String fileName;
    List<BugIssue> bugIssuesList;

    public NameLoginMapFile(String fileName) throws IOException, ParseException {
        this.bugIssuesList = new IssueConverter(path).getIssuesList();
        this.fileName = fileName;
        HashSet<String> users = getListOfUsers();
        writeMapToFile(users);
    }

    private void writeMapToFile(HashSet<String> usersSet) throws ParseException {

        HashMap<String, String> map = getHashMap(usersSet);
        try {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");

            for (String login : map.keySet()) {
                String name = map.get(login);
                writer.println(login + DELIMITER+ name);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }


    }

    private HashMap<String, String> getHashMap(HashSet<String> usersSet) throws ParseException {
        HashMap<String, String> map = new HashMap<>();
        for (String userLogin : usersSet) {
            String name = new LoginNameService(userLogin).getName();
            map.put(userLogin, name);
        }
        return map;
    }

    private HashSet<String> getListOfUsers() {
        HashSet<String> users = new HashSet<>();
        for (BugIssue bugIssue :
                bugIssuesList) {
            users.add(bugIssue.getOpenedBy());
            for (CommentObject commentObject : bugIssue.getCommentList()
                    ) {
                users.add(commentObject.getAuthor());
            }
        }
        return users;
    }

}
