package util;


import commit.CommitObject;
import issues.BugIssue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Talal on 01.05.17.
 */
public class MapCommitFixBugIssue {
    List<BugIssue> bugIssuesList;
    List<CommitObject> commitsList;
    String mapFilePath;

    public MapCommitFixBugIssue(List<BugIssue> bugIssuesList, List<CommitObject> commitsList, String mapFilePath) throws FileNotFoundException {
        this.bugIssuesList = bugIssuesList;
        this.commitsList = commitsList;
        this.mapFilePath = mapFilePath;
    }


    private HashMap<BugIssue, CommitObject> mapCommitFixToBugIssueByIssueNumber() {
        HashMap<BugIssue, CommitObject> map = new HashMap<>();
        for (CommitObject commit : commitsList
                ) {
            String message = commit.getMessage();

            for (BugIssue bugIssue : bugIssuesList
                    ) {
                String issueNumber = bugIssue.getNumber();
                if (match(message, issueNumber) && commit.getModifiedFiles().size() > 0) {
                    map.put(bugIssue, commit);
                }
            }
        }
        return map;
    }


    public HashMap<BugIssue, CommitObject> mapCommitFixToBugIssue() throws FileNotFoundException {
        HashMap<BugIssue, CommitObject> map = mapCommitFixToBugIssueByIssueNumber();
        System.out.println("First Map approach : " + map.size());


        HashMap<String, String> nameMap = createHashMap();
        for (CommitObject commit : commitsList
                ) {
            Long commitDate = commitDateToMillis(commit.getDate());
            String commitAuthor = commit.getAuthorName();
            for (BugIssue bugIssue : bugIssuesList
                    ) {
                String bugClosedBy = nameMap.get(bugIssue.getClosedBy());
                String bugOpenedBy = nameMap.get(bugIssue.getOpenedBy());
                long bugOpenOn = bugOpenDateToMillis(bugIssue.getOpenedOn());
                // if the map does not contain the bugIssue, check if the user who opened the bug or close it did a commit between the open date and reopened date / or open date and close date
                if ((!map.containsKey(bugIssue)) && (bugOpenOn < commitDate)) {
                    if ((bugClosedBy != null)
                            && (bugClosedBy.equals(commitAuthor))
                            ) {
                        if (bugIssue.isReopen()) {
                            long reopenDate = bugOpenDateToMillis(bugIssue.getReopenOn());
                            if (reopenDate > commitDate && commit.getModifiedFiles().size()>0) {
                                map.put(bugIssue, commit);
                            }
                        } else if (bugIssue.getClosedOn() != null) {
                            long bugIssueCloseDate = bugOpenDateToMillis(bugIssue.getClosedOn());
                            if (commitDate < bugIssueCloseDate && commit.getModifiedFiles().size()>0) {
                                map.put(bugIssue, commit);
                            }
                        }
                    }
//
                    if ((bugOpenedBy != null) && bugOpenedBy.equals(commitAuthor)) {
                        if (bugIssue.isReopen()) {
                            long reopenDate = bugOpenDateToMillis(bugIssue.getReopenOn());
                            if (reopenDate > commitDate && commit.getModifiedFiles().size()>0) {
                                map.put(bugIssue, commit);
                            }
                        } else if (bugIssue.getClosedOn() != null) {
                            long bugIssueCloseDate = bugOpenDateToMillis(bugIssue.getClosedOn());
                            if (commitDate < bugIssueCloseDate && commit.getModifiedFiles().size()>0) {
                                map.put(bugIssue, commit);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("second approach : " + map.size());
        return map;
    }


    private boolean match(String message, String number) {
        return (message.contains("#" + number));
    }

    private Long commitDateToMillis(String dateToformat) {
        String pattern = "yyyy-MM-dd HH:mm:ss Z";
        DateTimeFormatter formatIssue = DateTimeFormat.forPattern(pattern);
        DateTime date = formatIssue.parseDateTime(dateToformat);
        return date.getMillis();

    }


    private Long bugOpenDateToMillis(String issueDateToFormat) {

        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        DateTimeFormatter formatIssue = DateTimeFormat.forPattern(pattern);
        DateTime issueDate = formatIssue.parseDateTime(issueDateToFormat);
        return issueDate.getMillis();
    }

    private HashMap<String, String> createHashMap() throws FileNotFoundException {
        HashMap<String, String> map = new HashMap<>();
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(mapFilePath)));
        while (scanner.hasNextLine()) {
            String[] splitted = scanner.nextLine().split(" = ");
            map.put(splitted[0], splitted[1]);
        }
        return map;
    }

}
