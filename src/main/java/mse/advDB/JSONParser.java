package mse.advDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONParser {
    public static void parse(String sourceFilePath, String outputFilePath, long MAX_FILE_SIZE, long MAX_NODES)
            throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(sourceFilePath);
        BufferedReader br = new BufferedReader(fr);

        int bracesCounter = 0;
        long currentFileSize = 0;
        long nbNodes = 0;
        int fileNumber = 1;

        File file = new File("files");
        file.delete();
        file.mkdir();

        FileWriter fw = new FileWriter(outputFilePath + String.valueOf(fileNumber) + ".json");

        while (br.ready() && nbNodes <= MAX_NODES) {

            String line = br.readLine();
            currentFileSize += line.getBytes().length;

            if(bracesCounter == 0){
                nbNodes++;
            }
            if (line.indexOf("{") > -1) {
                bracesCounter++;
            }
            if (line.indexOf("}") > -1) {
                bracesCounter--;
            }

            line = line.replaceAll("NumberInt\\((\\d+)\\)", "$1");

            if (bracesCounter == 0 && (currentFileSize >= MAX_FILE_SIZE || nbNodes >= MAX_NODES)) {

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
            File lastFile = new File(outputFilePath + String.valueOf(fileNumber) + ".json");
            lastFile.delete();
            if (fileNumber > 2) {
                lastFile = new File(outputFilePath + String.valueOf(fileNumber - 1) + ".json");
                lastFile.delete();
            }
        }

        br.close();
        fr.close();
    }
}
