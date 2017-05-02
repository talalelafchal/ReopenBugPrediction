import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKNumber;
import com.github.mauricioaniche.ck.CKReport;
import commit.CommitConverter;
import commit.CommitObject;
import issues.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.parser.ParseException;
import issues.IssueConverter;
import util.MapCommitFixBugIssue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Talal on 26.04.17.
 */
public class test {
    public static void main(String[] args) throws IOException, ParseException {
     double a = 0.05;
        System.out.println(a);

        List<BugIssue> listBug = new IssueConverter("output/elasticsearchBugIssues.json").getIssuesList();
//        List<CommitObject> listCommit = new CommitConverter("output/logElasticSearch.txt").getCommitList();
//        HashMap<BugIssue,CommitObject> map = new MapCommitFixBugIssue(listBug,listCommit).mapCommitFixToBugIssue();
//
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
//
//            }
//
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
