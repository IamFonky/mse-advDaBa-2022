package mse.advDB;

import java.io.File;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



class FileComparator implements Comparator<File> {
    Pattern p = Pattern.compile("\\d+");

@Override
public int compare(File fileA, File fileB) {

    int fileANum = 0;
    Matcher mA = p.matcher(fileA.getName());
    if(mA.find()){
        fileANum = Integer.parseInt(mA.group());
    }

    int fileBNum = 0;
    Matcher mB = p.matcher(fileB.getName());
    if(mB.find()){
        fileBNum = Integer.parseInt(mB.group());
    }

    return fileANum-fileBNum;
}
}