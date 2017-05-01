import issues.IssuesService;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by Talal on 23.04.17.
 */
public class Main {

    //https://github.com/elastic/elasticsearch
    static String username;
    static String password;

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {

        String url = "https://github.com/elastic/elasticsearch";
        FileInputStream fs = new FileInputStream("config.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        username = br.readLine();
        password = br.readLine();
        String destinationFolder = br.readLine();
        String gitCommand = br.readLine();
//        GitCommands.cloneGitRepositoryUnix(,destinationFolder,gitCommand,destinationFolder);
        GitCommands.LogFile(destinationFolder,gitCommand,destinationFolder,"elasticsearch");
    }


    public void createBugIssuesFile() throws ParseException, InterruptedException, IOException {
        IssuesService issuesService = new IssuesService(username,password,243,"elastic","elasticsearch");
        issuesService.createBugJsonIssuesFile("output/elasticsearchBugIssues.json");
    }





}
