package mse.advDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Pattern;

public class JSONParser {

    private static final Pattern OPEN_BRACES_REGEX = Pattern.compile("\"(.*)\\{(.*)\"");
    private static final Pattern CLOSE_BRACES_REGEX = Pattern.compile("\"(.*)\\}(.*)\"");

    public static void parse(String sourceFilePath, String outputFilePath, long MAX_FILE_SIZE, long MAX_NODES)
            throws FileNotFoundException, IOException {
        File sourceFile = new File(sourceFilePath);
        FileReader fr = new FileReader(sourceFile);
        BufferedReader br = new BufferedReader(fr);

        int bracesCounter = 0;
        long currentFileSize = 0;
        long totalSize = 0;
        long nbNodes = 0;
        int fileNumber = 1;
        long percent = 0;
        long lastPercent = -1;

        // long test = 0;

        File files = new File("files");
        for (File file : files.listFiles()) {
            file.delete();
        }
        files.delete();
        files.mkdir();

        FileWriter fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");

        System.out.println("Parsing json file...");

        while (br.ready() && nbNodes <= MAX_NODES) {

            String line = br.readLine();

            if (line.indexOf("{") > -1 && !OPEN_BRACES_REGEX.matcher(line).find()) {
                if (bracesCounter == 0) {
                    nbNodes++;
                }
                bracesCounter++;
            }
            if (line.indexOf("}") > -1 && !CLOSE_BRACES_REGEX.matcher(line).find()) {
                bracesCounter--;
            }

            // System.out.println(bracesCounter + " - " + nbNodes);

            line = line.replaceAll("NumberInt\\((\\d+)\\)", "$1");

            currentFileSize += line.getBytes().length;
            totalSize += line.getBytes().length;
            percent = totalSize * 100 / Files.size(sourceFile.toPath());

            if (percent != lastPercent) {
                System.out.println(
                        totalSize + " / " + Files.size(sourceFile.toPath()) + " : " + percent + "% -- file #"
                                + fileNumber + " - node #"
                                + nbNodes);
                lastPercent = percent;
            }

            // if((nbNodes == 17407 || nbNodes == 17408 ) && test < 400){

            if (bracesCounter == 0 && (currentFileSize >= MAX_FILE_SIZE || nbNodes >= MAX_NODES)) {
                currentFileSize = 0;

                int pos = line.lastIndexOf(",");
                if (pos > -1) {
                    fw.write(line.substring(0, pos) + "\n");
                } else {
                    fw.write(line + "\n");
                }
                fw.write("\n]");
                fileNumber++;
                fw.flush();
                fw.close();

                File lastFile = new File(outputFilePath + String.valueOf(fileNumber-1) +
                ".json");
                lastFile.setReadable(true, false);
                lastFile.setWritable(true, false);

                fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");
                fw.write("[\n");
            } else {
                fw.write(line + "\n");
            }

            // System.out.println(bracesCounter + " - " + line);
            // test++;
            // if(test > 400){
            // nbNodes = MAX_NODES;
            // break;
            // }
            // }

        }

        fw.flush();
        fw.close();

        if (fileNumber > 1) {
            File lastFile = new File(outputFilePath + String.valueOf(fileNumber) +
                    ".json");
            lastFile.delete();
            if (fileNumber > 2) {
                lastFile = new File(outputFilePath + String.valueOf(fileNumber - 1) +
                        ".json");
                lastFile.delete();
            }
        }

        System.out.println(
                totalSize + " / " + Files.size(sourceFile.toPath()) + " : " + percent + "% -- file #"
                        + fileNumber + " - node #"
                        + nbNodes);

        br.close();
        fr.close();
    }
}
