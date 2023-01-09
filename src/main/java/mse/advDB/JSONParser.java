package mse.advDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONParser {
    public static void parse(String sourceFilePath, String outputFilePath, long MAX_FILE_SIZE)
            throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(sourceFilePath);
        BufferedReader br = new BufferedReader(fr);

        int bracesCounter = 0;
        int squareBracesCounter = 0;
        long currentFileSize = 0;
        int fileNumber = 1;
        boolean running = true;
        // base

        FileWriter fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");

        while (br.ready() && running) {

            String line = br.readLine();
            currentFileSize += line.getBytes().length;

            if (line.indexOf("{") > -1) {
                bracesCounter++;
            }
            if (line.indexOf("}") > -1) {
                bracesCounter--;
            }
            if (line.indexOf("[") > -1) {
                squareBracesCounter++;
            }
            if (line.indexOf("]") > -1) {
                squareBracesCounter--;
            }

            // if (squareBracesCounter == 0) {
            //     running = false;
            // } else {

                line = line.replaceAll("NumberInt\\((\\d+)\\)", "$1");

                if (bracesCounter == 0 && currentFileSize >= MAX_FILE_SIZE) {

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
            // }
        }

        fw.flush();
        fw.close();

        if (fileNumber > 1) {
            File lastFile = new File(outputFilePath + String.valueOf(fileNumber) + ".json");
            lastFile.delete();
        }
        br.close();
        fr.close();
    }
}
