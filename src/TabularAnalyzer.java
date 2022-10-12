import java.util.ArrayList;

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

    public String printAllFollow() {
        String output = "";
        for (String follow : this.grammar.getAllFollow()) {
            output += follow + "\n";
        }
        return output;
    }

    public String printTabularAnalyzer(){
        String output = "";
        for (M m : this.grammar.getAnalysisTable()) {
            output += m + "\n";
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
        ArrayList<String> lines = Utils.returnLinesFromFile(filePath);

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
            ArrayList<String> arrayList = Utils.transformArrayToArrayList(listRight);

            derivations.add(new Derivation(derivation[0].replace(" ", ""), arrayList));
        }
        return new Grammar(derivations);
    }
}
