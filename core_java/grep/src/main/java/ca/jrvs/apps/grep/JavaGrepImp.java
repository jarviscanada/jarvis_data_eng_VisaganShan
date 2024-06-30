package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String rootPath;
    private String outFile;

    public static void main(String[] args) {
        if (args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //use default logger config NOT WORKING *****
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }

    @Override
    public void process() throws IOException {
        List<String> matchedLines = new ArrayList<>();
        List<File> files = listFiles(getRootPath());
        for (File file : files) {
            List<String> lines = readLines(file);
            for (String line : lines) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        File directory = new File(rootDir); // Creates a File object representing root directory
        List<File> files = new ArrayList<>(); // Creates an empty ArrayList of File objects.
        listFilesRecursive(directory, files);
        return files;
    }

    private void listFilesRecursive(File directory, List<File> files){
        File[] fileArray = directory.listFiles(); // Stores the files found in root directory
        if (fileArray != null) {
            for (File file : fileArray) {
                if (file.isDirectory()) {
                    listFilesRecursive(file, files);
                } else {
                    files.add(file);
                }
            }
        }
    }

    @Override
    public List<String> readLines(File inputFile) throws IllegalArgumentException{
        try{
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null){
                lines.add(line);
            }
            br.close();
            return lines;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Unable to read file", ex);
        }
    }

    @Override
    public boolean containsPattern(String line) {
        if(line == null || line.isEmpty()) return false;
        return line.matches(".*" + regex + ".*");
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
            bw.close();
        } catch(Exception ex){
            throw new IOException("Write Failed", ex);
        }
    }

    @Override
    public String getRootPath() { return rootPath; }

    @Override
    public void setRootPath(String rootPath) { this.rootPath = rootPath; }

    @Override
    public String getRegex() { return regex; }

    @Override
    public void setRegex(String regex) { this.regex = regex; }

    @Override
    public String getOutFile() { return outFile; }

    @Override
    public void setOutFile(String outFile) { this.outFile = outFile; }
}