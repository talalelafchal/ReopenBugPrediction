import java.io.*;

/**
 * Created by Talal on 23.04.17.
 */
public class GitCommands {

    public static void cloneGitRepositoryUnix(String repositoryAddress, String destinationFolder,
                                              String gitCommand, String tmpFolder) throws IOException, InterruptedException {

        //[START] Create a temporary file where we write the commands
        //that we want to execute from command line
        File commandsToExecute = new File(tmpFolder + "/clone.sh");
        commandsToExecute.createNewFile();
        //[END]

        //[START] We print the command that we want to execute
        PrintWriter pw = new PrintWriter(commandsToExecute);
        pw.println("cd " + destinationFolder);
        pw.println(gitCommand + " clone " + repositoryAddress);
        pw.close();
        //[END]


        //[START] Make the file executable (i.e., adds execution
        //permissions to the created file)
        Runtime rt = Runtime.getRuntime();
        String cmd = "chmod u+x " + tmpFolder + "/clone.sh";
        Process process = rt.exec(cmd);

        String line = null;

        BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        while ((line = stdoutReader.readLine()) != null) {
            System.out.println(line);
        }

        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
        while ((line = stderrReader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
        //[END]

        //[START] Execute the command that we stored
        //in commands.sh (i.e., in this case, clones
        //the repository)
        cmd = "sh " + tmpFolder + "clone.sh";
        process = rt.exec(cmd);

        line = null;

        stdoutReader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));
        while ((line = stdoutReader.readLine()) != null) {
            System.out.println(line);
        }

        stderrReader = new BufferedReader(new InputStreamReader(
                process.getErrorStream()));
        while ((line = stderrReader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
        //[END]

    }
}
