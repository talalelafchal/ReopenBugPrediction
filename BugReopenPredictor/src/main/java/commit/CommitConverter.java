package commit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Talal on 30.04.17.
 */
public class CommitConverter {

    String logFilePath;

    public CommitConverter(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public List<CommitObject> getCommitList() throws FileNotFoundException {
        List<CommitObject> commitList = new ArrayList<>();
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(logFilePath))).useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));
        while (scanner.hasNext()) {
            String commit = scanner.next().replaceAll("^\\n","");
            String[] commitArray = commit.split("\\n");
            String commitInfo = commitArray[0];

            Document doc = Jsoup.parse(commitInfo, "", Parser.xmlParser());
            String commitId = doc.select("commit-id").get(0).text();
            String authorName = doc.select("author-name").get(0).text();
            String date = doc.select("date").get(0).text();
            String message = doc.select("message").get(0).text();

            List<String> modifiedFiles = new ArrayList<>();
            for (int i = 1; i < commitArray.length ; i++) {
                String modifiedFileLine =  commitArray[i];
                if(modifiedFileLine.startsWith("M")){
                    modifiedFiles.add(modifiedFileLine.split("\\s+")[1]);
                }
            }

            CommitObject commitObject = new CommitObject(commitId,authorName,date,message,modifiedFiles);
            commitList.add(commitObject);
        }
        return commitList;
    }

}
