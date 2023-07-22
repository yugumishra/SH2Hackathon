package visual;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Util {
    public static String scanShader(String filename) {
        File f = new File(filename);
        Scanner scan = null;
        try {
           scan = new Scanner(f); 
        }catch (FileNotFoundException e) {
            System.err.println("The shader file " + filename + " was not found");
        }

        StringBuilder sb = new StringBuilder();
        while(scan.hasNextLine()) {
            sb.append(scan.nextLine());
            sb.append("\n");
        }
        return sb.toString();
    }
}
