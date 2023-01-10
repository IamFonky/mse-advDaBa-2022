package mse.advDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class JSONParser {
    public static void parse(String sourceFilePath, String outputFilePath, long MAX_FILE_SIZE, long MAX_NODES)
            throws FileNotFoundException, IOException {
        File sourceFile = new File(sourceFilePath);
        FileReader fr = new FileReader(sourceFile);
        BufferedReader br = new BufferedReader(fr);

        int bracesCounter = 0;
        long currentFileSize = 0;
        long nbNodes = 0;
        int fileNumber = 1;
        long percent = 0;
        long lastPercent = -1;

        File files = new File("files");
        for (File file : files.listFiles()) {
            file.delete();
        }
        files.delete();
        files.mkdir();

        FileWriter fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");

        while (br.ready() && nbNodes <= MAX_NODES) {

            String line = br.readLine();

            if (line.indexOf("{") > -1) {
                if (bracesCounter == 0) {
                    nbNodes++;
                }
                bracesCounter++;
            }
            if (line.indexOf("}") > -1) {
                bracesCounter--;
            }

            System.out.println(bracesCounter + " - " + nbNodes);

            line = line.replaceAll("NumberInt\\((\\d+)\\)", "$1");

            currentFileSize += line.getBytes().length;
            percent = currentFileSize * 100 / Files.size(sourceFile.toPath());

            if (percent != lastPercent) {
                System.out.println(
                        currentFileSize + " / " + Files.size(sourceFile.toPath()) + " : " + percent + "% -- file #"
                                + fileNumber + " - "
                                + nbNodes + " - ");
                lastPercent = percent;
            }

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

                fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");
                fw.write("[\n");
            } else {
                fw.write(line + "\n");
            }
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
                currentFileSize + " / " + Files.size(sourceFile.toPath()) + " : " + percent + "% -- file #"
                        + fileNumber + " - " + MAX_FILE_SIZE + " - "
                        + String.valueOf(currentFileSize >= MAX_FILE_SIZE)
                        + " - " + nbNodes);
        lastPercent = percent;

        br.close();
        fr.close();
    }
}
