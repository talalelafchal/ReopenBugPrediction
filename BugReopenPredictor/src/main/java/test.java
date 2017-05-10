import commit.CommitConverter;
import commit.CommitObject;
import issues.*;
import org.json.simple.parser.ParseException;
import issues.IssueConverter;
import util.MapCommitFixBugIssue;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Talal on 26.04.17.
 */
public class test {
    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
//        List<CommitObject> listCommit = new CommitConverter("output/logSpring.txt").getCommitList();
//        List<BugIssue> listBug = new IssueConverter("output/springBugIssues.json").getIssuesList();
//        HashMap<BugIssue, CommitObject> map = new MapCommitFixBugIssue(listBug, listCommit,"output/springMapFile.txt").mapCommitFixToBugIssue();
//        int reopen = map.keySet().stream().filter(x -> x.isReopen() == true).collect(Collectors.toList()).size();
//        System.out.println("# of issue bugs : " + map.size());
//        System.out.println("# of reopen bugs : " + reopen);


        String url = "https://github.com/spring-projects/spring-boot";
        FileInputStream fs = new FileInputStream("config.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String username = br.readLine();
        String password = br.readLine();
        String destinationFolder = br.readLine();
        String gitCommand = br.readLine();
        br.close();

        int reopenBug = new IssuesService(username, password, 44, "spring-projects", "spring-security").getNumberOfReopenBug();
        System.out.println("# of reopen Bug " + reopenBug);

//        HashMap<BugIssue,CommitObject> map = new MapCommitFixBugIssue(listBug,listCommit).mapCommitFixToBugIssue();
//        for (BugIssue key: map.keySet()
//             ) {
//            int index = listCommit.indexOf(map.get(key));
//            System.out.println(index);
//        }
//        System.out.println(map.size());
//        for (BugIssue bugIssue : listBug
//                ) {
//            List<CommentObject> commentList = bugIssue.getCommentList();
//            if (commentList != null) {
//                CommentsAnalyzer commentsAnalyzer = new CommentsAnalyzer(commentList);
//                int numberOfCommenters = commentsAnalyzer.getNumberOfCommenter();
//                List<List<String>> pairs = commentsAnalyzer.getAllCommentersPair();
//                System.out.println(pairs.size());
//            }
//        }
    }

    public static void TestMetric() {
        try {
            List<BugIssue> bugList = new IssueConverter("output/elasticsearchBugIssues.json").getIssuesList();
            for (BugIssue bugIssue : bugList
                    ) {
                BugIssueAnalyzer bugIssueAnalyzer = new BugIssueAnalyzer(bugIssue);
                List<CommentObject> commentList = bugIssue.getCommentList();
                if (commentList != null) {
                    CommentsAnalyzer commentsAnalyzer = new CommentsAnalyzer(commentList);
                    boolean stepToReprodeuce = bugIssueAnalyzer.isStepToReproduce();
                    boolean expectedBehaviour = bugIssueAnalyzer.isExpectedBehaviour();
                    int numberOfCommenters = commentsAnalyzer.getNumberOfCommenter();
                    int descriptionLength = bugIssueAnalyzer.getCommentsDescriptionWordsCount();

                    System.out.println(bugIssue.getNumber() + " stepToreproduce : " + stepToReprodeuce + " expectedBeh : "
                            + expectedBehaviour + " numberOfCommenters : " + numberOfCommenters + " description length : " + descriptionLength);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
