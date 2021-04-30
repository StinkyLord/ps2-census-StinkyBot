package stinkybot.commandlisteners.utilities;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class Utils {

    public static String writeListToFile(List<String> list){
        String fileName = "list_" + RandomStringUtils.random(6, 'A', 'Z' + 1, false, false) + ".txt";
        try (FileOutputStream fos = new FileOutputStream(fileName);
             OutputStreamWriter streamWriter = new OutputStreamWriter(fos);
             BufferedWriter bw = new BufferedWriter(streamWriter)) {
            for (String s : list) {
                bw.write(s + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
