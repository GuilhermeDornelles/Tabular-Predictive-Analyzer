import java.util.ArrayList;
import java.util.Stack;

public class TabularAnalyzer {
    private Grammar grammar;

    public TabularAnalyzer(String filePath) {
        this.grammar = buildGrammar(filePath);
        System.out.println("Gramática Criada:");
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

    public boolean readSentence(String sentence){
        ArrayList<M> table = this.grammar.getAnalysisTable();
        sentence = sentence.replace(" ", "") + "$";
        Stack<String> stack = new Stack<>();
        stack.push("$");
        String init = this.grammar.getFirstDerivation().getLeft();
        stack.push(init);
        
        while (!stack.empty()) {
            String topOfStack = stack.pop();
            String topOfSentence = Character.toString(sentence.charAt(0));
            if(Utils.isTerminal(topOfStack)){
                if (topOfStack.equals(topOfSentence)){
                    sentence = sentence.substring(1);
                }
                return false;
            }
            else{
                Derivation currDer = null;
                for (M m : table) {
                    if(m.getNonTerminal().equals(topOfStack) && m.getTerminal().equals(topOfSentence)){
                        currDer = m.getDerivation();
                    }
                }
                if(currDer == null) return false;

                String right = currDer.getRight().get(0);
                int rightLength = right.length();
                while (rightLength > 0){
                    // char lastCharacter = right.charAt(rightLength-1);
                    String lastCharacter = Utils.returnLastChar(right);
                    stack.push(lastCharacter);
                    rightLength -= 1;
                }
            }
        }

        return true;
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
    