package util;

import commit.CommitObject;
import issues.BugIssue;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Talal on 01.05.17.
 */
public class MapCommitFixBugIssue {
    List<BugIssue> bugIssuesList;
    List<CommitObject> commitsList;

    public MapCommitFixBugIssue(List<BugIssue> bugIssuesList, List<CommitObject> commitsList) {
        this.bugIssuesList = bugIssuesList;
        this.commitsList = commitsList;
    }

    public HashMap<BugIssue, CommitObject> mapCommitFixToBugIssue() {
        HashMap<BugIssue, CommitObject> map = new HashMap<>();
        for (CommitObject commit : commitsList
                ) {
            String message = commit.getMessage();

            for (BugIssue bugIssue : bugIssuesList
                    ) {
                String issueNumber = bugIssue.getNumber();
                if (match(message, issueNumber)) {
                    map.put(bugIssue, commit);
                }
            }
        }
        return map;
    }

    private boolean match(String message, String number) {
        String lowerCaseMessage = message.toLowerCase();
        return (lowerCaseMessage.contains("#" + number) &&
                (lowerCaseMessage.contains("fix") || lowerCaseMessage.contains("fixed") || lowerCaseMessage.contains("fixes") || lowerCaseMessage.contains("close") || lowerCaseMessage.contains("closed")));
    }

}
