import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Utils {
    public static boolean isTerminal(String value){
        if(value.toLowerCase() == value){
            return true;
        }
        return false;
    }
    public static boolean isCharValid(String strChar){
        return strChar.compareTo("E") != 0;
    }

    public static <T> ArrayList<T> transformArrayToArrayList(T[] array){
        return new ArrayList<>(Arrays.asList(array));
    }

    public static ArrayList<String> returnLinesFromFile(String filePath){
        File file = new File(filePath);
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lines;
    }
}
