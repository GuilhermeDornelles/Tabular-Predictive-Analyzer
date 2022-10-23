import java.util.ArrayList;
import java.util.Stack;

public class TabularAnalyzer {
    private Grammar grammar;

    public TabularAnalyzer(String filePath) {
        this.grammar = buildGrammar(filePath);
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
        System.out.println("Reconhendo sentença");
        ArrayList<M> table = this.grammar.getAnalysisTable();
        sentence = sentence.replace(" ", "") + "$";
        Stack<String> stack = new Stack<>();
        // Adiciona $ ao início da pilha
        stack.push("$");
        // Começa pela primeira derivação da gramática
        String init = this.grammar.getFirstDerivation().getLeft();
        stack.push(init);
        
        // Enquando a pilha não estiver vazia
        while (!stack.empty()) {
            System.out.println(String.format("%s    |    %s", stack, sentence));
            // Retira o topo da pilha
            String topOfStack = stack.pop();
            // Pega o atual primeiro caractere da sentença
            String topOfSentence = Character.toString(sentence.charAt(0));
                
            if(topOfSentence.equals("$") && topOfStack.equals("$")){
                return true;
            }
            // Se o topo da pilha for terminal
            if(Utils.isTerminal(topOfStack)){
                // Se os dois topos forem iguais
                if (topOfStack.equals(topOfSentence)){
                    // Remove o primeiro caractere da sentença
                    sentence = sentence.substring(1);
                } 
                // Significa que a frase não é reconhecida
                else {
                    System.out.println("Terminais da pilha e fila não coincidem");
                    return false;
                }
            }
            // Se não for terminal
            else{
                Derivation currDer = null;
                // Para cada elemento da tabela
                for (M m : table) {
                    // if(m.getNonTerminal().equals(topOfStack) && m.getTerminal().equals(topOfSentence)){
                    // Se os valores batem com os topos das pilhas 
                    if(m.getTerminal().equals(topOfStack) && m.getNonTerminal().equals(topOfSentence)){
                        // Se achou, para a procura
                        currDer = m.getDerivation();
                    }
                }
                // Se não achar o elemento na tabela, significa que não aceita a sentenção
                if(currDer == null){
                    System.out.println("Não encontrou M coincidente na tabela, logo, a sentença não é válida");
                    return false;
                }

                String right = currDer.getRight().get(0);
                int rightLength = right.length();
                while (rightLength > 0){
                    String lastCharacter = Utils.returnLastChar(right);
                    right = Utils.removeLastChar(right);
                    if(!lastCharacter.equals("E")){
                        stack.push(lastCharacter);
                    }
                    rightLength = right.length();
                }
            }
            
        }
        System.out.println(String.format("%s    |    %s", stack, sentence));
        return true;
    }

    private static Grammar buildGrammar(String filePath) {
        ArrayList<String> lines = Utils.returnLinesFromFile(filePath);

        if(lines == null){
            return null;
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
            ArrayList<String> arrayList = Utils.transformArrayToArrayList(listRight);

            derivations.add(new Derivation(derivation[0].replace(" ", ""), arrayList));
        }
        return new Grammar(derivations);
    }
}
    