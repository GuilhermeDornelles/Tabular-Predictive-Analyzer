import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TabularAnalyzer {
    private Grammar grammar;
    public TabularAnalyzer(Grammar grammar){
        this.grammar = grammar;
    }

    public Grammar getGrammar(){
        return this.grammar;
    }

    public String toString(){
        return "TODO";
    }

    public static Grammar makeGrammar(String filePath) throws FileNotFoundException{
        File file = new File(filePath);
        Scanner sc = new Scanner(file);

        ArrayList<String> lines = new ArrayList<>();

        while (sc.hasNextLine()){
            lines.add(sc.nextLine());
        }
        sc.close();
        ArrayList<Derivation> derivations = new ArrayList<>();
        String[] derivation;
        for(String line: lines){
            derivation = line.split("->");
            String[] listRight = derivation[1].split("|");
            for(String right: listRight){
                right = right.replaceAll(" ", "");
                right = right.replaceAll("\n", "");
            }
            // Converting array to Arraylist
            ArrayList<String> arrayList = new ArrayList<>();
            for(String der: listRight){
                arrayList.add(der);
            }

            derivations.add(new Derivation(derivation[0].replaceAll(" ", ""), arrayList));
        }

        return new Grammar(derivations);
    }
}
