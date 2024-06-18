package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{
    public static void main(String[] args) {
        if (args.length != 3) {

            //creating JavaGrepLambdaImp instead of JavaGrepImp
            JavaGrepLambdaImp lambdaImp = new JavaGrepLambdaImp();
            lambdaImp.setRegex(args[0]);
            lambdaImp.setRootPath(args[1]);
            lambdaImp.setOutFile(args[2]);

            try {
                lambdaImp.process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public List<String> readLines(File inputFile){
        try{
            Stream<String> lines = Files.lines(inputFile.toPath());
            return lines.collect(Collectors.toList());
        } catch (IOException ex){
            throw new IllegalArgumentException("Unable to read file", ex);
        }
    }

    @Override
    public List<File> listFiles(String rootDir){
        List<Path> files = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(Paths.get(rootDir))) {

            return paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

