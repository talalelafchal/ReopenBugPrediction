package social;

import commit.CommitObject;
import issues.BugIssue;
import issues.BugIssueAnalyzer;
import issues.CommentObject;
import issues.CommentsAnalyzer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import util.MapCommitFixBugIssue;
import util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


/**
 * Created by Talal on 01.05.17.
 */
public class DeveloperSocialHistory {
    String mapFilePath;
    List<CommitObject> commitList;
    List<BugIssue> bugIssuesList;
    HashMap<String, String> loginNameMap;
    List<CommitObject> fixCommitList;
    List<Pair> listOfCollaboration;

    public DeveloperSocialHistory(String mapFilePath, List<CommitObject> commitList, List<BugIssue> bugIssuesList) throws FileNotFoundException {
        this.mapFilePath = mapFilePath;
        this.commitList = commitList;
        this.bugIssuesList = bugIssuesList;
        loginNameMap = createHashMap();
        HashMap<BugIssue, CommitObject> map = new MapCommitFixBugIssue(bugIssuesList, commitList).mapCommitFixToBugIssue();
        this.fixCommitList = new ArrayList<>(map.values());
        listOfCollaboration = getCollaborationList();
    }

    private List<Pair> getCollaborationList() {
        List<Pair> allPairsList = new ArrayList<>();
        for (BugIssue bugIssue : bugIssuesList
                ) {
            List<CommentObject> commentList = bugIssue.getCommentList();
            if (commentList != null && commentList.size() > 1) {
                CommentsAnalyzer commentsAnalyzer = new CommentsAnalyzer(commentList);
                List<Pair> pairList = getPairList(commentsAnalyzer.getAllCommentersPair(), bugIssue.getOpenedOn());
                for (Pair pair : pairList
                        ) {
                    allPairsList.add(pair);
                }
            }
        }
        return allPairsList;
    }

    //Overall experience of the developer opening the bug (#past commits)
    public int numberOfPastCommitsOfDevelopperWhoOpenedTheBugIssue(String login, String bugOpenDate) {
        int count = 0;
        Long openDate = bugOpenDateToMillis(bugOpenDate);
        String developerName = loginNameMap.get(login);
        if (!developerName.equals("null")) {
            for (CommitObject commit : commitList
                    ) {
                long commitDate = commitDateToMillis(commit.getDate());
                if (commit.getAuthorName().equals(developerName) &&
                        openDate > commitDate) {
                    count++;
                }
            }
        }
        return count;

    }


    //Bug fixing experience of the developer opening the bug (#past bug fixes)
    public int numberofBugFixingCommitOfDevelopperWhoOpenedTheBugIssue(String login, String bugOpenDate) {
        int counter = 0;
        Long openDate = bugOpenDateToMillis(bugOpenDate);
        String developerName = loginNameMap.get(login);

        if (!developerName.equals("nul")) {
            for (CommitObject commitFix : fixCommitList
                    ) {
                long commitFixDate = commitDateToMillis(commitFix.getDate());
                if (commitFix.getAuthorName().equals(developerName) &&
                        openDate > commitFixDate) {
                    counter++;
                }
            }
        }

        return counter;
    }

    //Overall experience of the developer fixing the bug (#past commits)
    public int numberOfCommitsOftheDeveloperWhoFixedTheBug(String FixCommitAuthor, String commitFixingDate) {
        int counter = 0;
        long date = commitDateToMillis(commitFixingDate);
        if (FixCommitAuthor.equals("null")) {
            return 0;
        }

        for (CommitObject fixCommit : commitList
                ) {
            long commitDate = commitDateToMillis(fixCommit.getDate());
            if (fixCommit.getAuthorName().equals(FixCommitAuthor) && date > commitDate) {
                counter++;
            }
        }
        return counter;

    }

    //	Bug fixing experience of the developer fixing the bug (#past bug fixes)
    public int numberOfFixCommitsOftheDeveloperWhoFixedTheBug(String fixCommitAuthor, String commitFixingDate) {
        int counter = 0;
        long date = commitDateToMillis(commitFixingDate);
        if (!fixCommitAuthor.equals("null")) {
            for (CommitObject fixCommit : fixCommitList) {
                long commitDate = commitDateToMillis(fixCommit.getDate());
                if (fixCommit.getAuthorName().equals(fixCommitAuthor) && date > commitDate) {
                    counter++;
                }
            }
        }
        return counter;
    }


    //Social network strength: #pairs of developers who took part to the issue discussion that already collaborated in the past/#pairs of developers who took part to the issue discussion

    public double SocialNetworkStrength(BugIssue bugIssue) {
        double contribution = 0.0;
        List<CommentObject> commentList = bugIssue.getCommentList();
        if (commentList == null) {
            return 0;
        }

        if (commentList.size() > 1) {
            CommentsAnalyzer commentsAnalyzer = new CommentsAnalyzer(commentList);
            List<Pair> pairList = getPairList(commentsAnalyzer.getAllCommentersPair(), bugIssue.getOpenedOn());
            if (pairList.size() > 0) {
                contribution = getSocialContribution(pairList);
            }
        }
        return contribution;
    }

    private double getSocialContribution(List<Pair> pairList) {
        double counter = 0.0;
        for (Pair pair : pairList
                ) {
            if (collaborated(pair)) {
                counter++;
            }

        }

        return counter / pairList.size();

    }


    private boolean collaborated(Pair pair) {
        for (Pair allPair : listOfCollaboration
                ) {
            if (bugOpenDateToMillis(pair.getDate()) > bugOpenDateToMillis(allPair.getDate()) &&
                    (
                            (pair.getProgrammer1().equals(allPair.getProgrammer1()) && pair.getProgrammer2().equals(allPair.getProgrammer2())) ||
                                    (pair.getProgrammer1().equals(allPair.getProgrammer2()) && pair.getProgrammer2().equals(allPair.getProgrammer1()))
                    )
                    ) {
                return true;
            }
        }
        return false;
    }


    private List<Pair> getPairList(List<List<String>> allCommentersPair, String openedOn) {
        List<Pair> pairList = new ArrayList<>();
        for (List<String> pair : allCommentersPair
                ) {
            Pair pairObject = new Pair(pair.get(0), pair.get(1), openedOn);
            pairList.add(pairObject);
        }
        return pairList;
    }


    private Long bugOpenDateToMillis(String issueDateToFormat) {

        String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
        DateTimeFormatter formatIssue = DateTimeFormat.forPattern(pattern);
        DateTime issueDate = formatIssue.parseDateTime(issueDateToFormat);
        return issueDate.getMillis();
    }


    private Long commitDateToMillis(String dateToformat) {
        String pattern = "yyyy-MM-dd HH:mm:ss Z";
        DateTimeFormatter formatIssue = DateTimeFormat.forPattern(pattern);
        DateTime date = formatIssue.parseDateTime(dateToformat);
        return date.getMillis();

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
