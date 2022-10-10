import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TabularAnalyzer {
    private Grammar grammar;

    public TabularAnalyzer(String filePath) {
        this.grammar = buildGrammar(filePath);
        System.out.println("PRINTING GRAMMAR");
        System.out.println(this.grammar);
    }

    public String printAllFirst() {
        String output = "";
        for (String first : this.grammar.getAllFirst()) {
            output += first + "\n";
        }
        return output;
    }

    public Grammar getGrammar() {
        return this.grammar;
    }

    public String printGrammar() {
        return this.grammar.toString();
    }

    private static Grammar buildGrammar(String filePath) {
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

        ArrayList<Derivation> derivations = new ArrayList<>();
        String[] derivation;
        for (String line : lines) {
            line = line.replaceAll(" ", "");
            derivation = line.split("->");
            String[] listRight = derivation[1].split("\\|");
            for (String right : listRight) {
                right = right.replace(" ", "");
                right = right.replace("\n", "");
            }
            // Converting array to Arraylist
            ArrayList<String> arrayList = new ArrayList<>();
            for (String der : listRight) {
                arrayList.add(der);
            }

            derivations.add(new Derivation(derivation[0].replace(" ", ""), arrayList));
        }

        return new Grammar(derivations);
    }
}
