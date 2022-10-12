import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Grammar {
    private ArrayList<Derivation> derivations;
    private ArrayList<M> analysisTable;

    public ArrayList<M> getAnalysisTable() {
        return analysisTable;
    }

    public Grammar(ArrayList<Derivation> derivations) {
        this.derivations = derivations;
        buildFirst();
        buildFollow();
        buildAnalysisTable();
    }

    public Derivation getFirstDerivation() {
        return this.derivations.get(0);
    }

    public ArrayList<Derivation> getDerivations() {
        return this.derivations;
    }

    public void buildFirst() {
        for (Derivation derivation : this.derivations) {
            ArrayList<String> individualFirst = this.getIndividualFirst(derivation);
            for (String first : individualFirst) {
                derivation.addFirst(first);
            }
        }
    }

    public void buildFollow() {
        for (Derivation derivation : this.derivations) {
            ArrayList<String> individualFollow = this.getIndividualFollow(derivation);
            for (String follow : individualFollow) {
                derivation.addFollow(follow);
            }
        }
    }

    private ArrayList<Derivation> returnAllPossibleDerivations(){
        ArrayList<Derivation> allDerivations = new ArrayList<>();
        for (Derivation derivation : this.derivations) {
            for (String right : derivation.getRight()){
                Derivation newDer = new Derivation(derivation.getLeft(), right);
                allDerivations.add(newDer);
            }
        }
        return allDerivations;
    }

    public ArrayList<M> buildAnalysisTable() {
        analysisTable = new ArrayList<>();
        ArrayList<Derivation> allPossibleDerivations = returnAllPossibleDerivations();
        for (Derivation der : allPossibleDerivations) {
            String firstChar = returnFirstChar(der.getRight().get(0));

            if(Utils.isTerminal(firstChar)){
                M newM = new M(der.getLeft(), firstChar, der);
                analysisTable.add(newM);
                continue;
            }
            else if (!Utils.isCharValid(firstChar)){
                Derivation derLeft = this.getDerivation(der.getLeft());
                ArrayList<String> followDaEsquerda = derLeft.getAllFollow();
                for (String terminal : followDaEsquerda) {
                    M newM = new M(der.getLeft(), terminal, der);
                    analysisTable.add(newM);
                }
            }
            else{
                Derivation derFirstChar = this.getDerivation(firstChar);
                ArrayList<String> firstDoFirst = derFirstChar.getAllFirst();
                for (String terminal : firstDoFirst) {
                    M newM = new M(der.getLeft(), terminal, der);
                    analysisTable.add(newM);
                }
            }
        }
        return analysisTable;
    }

    public ArrayList<String> getAllFirst() {
        ArrayList<String> list = new ArrayList<>();
        for (Derivation derivation : this.derivations) {
            String first = "";
            ArrayList<String> individualFirst = derivation.getAllFirst();
            for (int i = 0; i < individualFirst.size(); i++) {
                if (i + 1 == individualFirst.size()) {
                    first += individualFirst.get(i);
                } else {
                    first += individualFirst.get(i) + ", ";
                }
            }
            list.add(String.format("First(%s) = %s", derivation.getLeft(), first));
        }
        return list;
    }

    public ArrayList<String> getAllFollow() {
        ArrayList<String> list = new ArrayList<>();
        for (Derivation derivation : this.derivations) {
            String follow = "";
            ArrayList<String> individualFollow = derivation.getAllFollow();
            if(individualFollow != null){
                for (int i = 0; i < individualFollow.size(); i++) {
                    if (i + 1 == individualFollow.size()) {
                        follow += individualFollow.get(i);
                    } else {
                        follow += individualFollow.get(i) + ", ";
                    }
                }
                list.add(String.format("Follow(%s) = %s", derivation.getLeft(), follow));
            }
        }
        return list;
    }

    public ArrayList<String> getIndividualFirst(Derivation derivation) {
        ArrayList<String> listOfFirsts = new ArrayList<String>();
        for (String possibilityOfDerivation : derivation.getRight()) {
            String first_char = returnFirstChar(possibilityOfDerivation);
            if (Utils.isCharValid(first_char)) {
                if (Utils.isTerminal(first_char)) {
                    listOfFirsts.add(first_char);
                } else {
                    Derivation fullDerivation = getDerivation(first_char);
                    ArrayList<String> individualFirst = getIndividualFirst(fullDerivation);
                    listOfFirsts.addAll(individualFirst);
                }
            } else {
                listOfFirsts.add(first_char);
            }
        }
        return listOfFirsts;
    }

    public ArrayList<String> getIndividualFollow(Derivation derivation) {
        ArrayList<String> followList = new ArrayList<>();
        if (derivation == this.getFirstDerivation()) {
            followList.add("$");
        }
        String derivationLeft = derivation.getLeft();
        ArrayList<String> derivationListToVerify = new ArrayList<>();
        for (Derivation der : this.derivations) {
            derivationListToVerify.addAll(der.getAllDerivationPossibilitiesThatContain(derivationLeft)); 
        }
        for (String stringToAnalise : derivationListToVerify) {
            Derivation nonTerminalFather = this.getLeftFromString(stringToAnalise);
            int foundAtIndex = stringToAnalise.indexOf(derivationLeft);
            if(stringToAnalise.length() <= foundAtIndex + derivationLeft.length()){

                ArrayList<String> followsToAdd = nonTerminalFather.getAllFollow();
                if(followsToAdd != null && !followsToAdd.isEmpty()){
                    followList.addAll(followsToAdd);
                }
            } else {
                String next = Character.toString(stringToAnalise.charAt(foundAtIndex + (derivationLeft.length())));

                int i = 1;
                while (stringToAnalise.length() > foundAtIndex + (derivationLeft.length()) + i &&
                       Character.toString(stringToAnalise.charAt(foundAtIndex + (derivationLeft.length()) + i)).compareTo("'") == 0){
                    i++;
                    next += "'";
                };
                
                if(Utils.isTerminal(next)){
                    followList.add(next);
                } else {
                    Derivation nextDerivation = getDerivation(next);
                
                    // adiciona todos os firsts do next
                    followList.addAll(nextDerivation.getAllFirst());

                    // se existe E nos firsts, pega o Follow
                    if(nextDerivation.containsEmptyInFirst()){
                        followList.addAll(nextDerivation.getAllFollow());
                    }
                }
            }
        }
        return cleanRepeatedAndEmpty(followList);
    }

    private Derivation getLeftFromString(String derivated){
        for(Derivation der : this.derivations){
            ArrayList<String> derRights = der.getRight();
            for (String possibility : derRights){
                if(possibility.compareTo(derivated) == 0)
                    return der;
            }
        }
        return null;
    }

    private ArrayList<String> cleanRepeatedAndEmpty(ArrayList<String> list) {
        // Removing repeated
        Set<String> setAux = new LinkedHashSet<>(list);
        ArrayList<String> newList = new ArrayList<>(setAux);

        // Cleaning Empty Symbol "E"
        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i).compareTo(Utils.EMPTY_SYMBOL) == 0) {
                newList.remove(newList.get(i));
            }
        }
        return newList;
    }

    private String returnFirstChar(String possibilityOfDerivation) {
        if (possibilityOfDerivation.length() == 0)
            return "";
        String firstChar = Character.toString(possibilityOfDerivation.charAt(0));
        // logic to concatenate in firstChar all ' in case we have A', A'', A''', etc
        if (possibilityOfDerivation.length() >= 1) {
            for (int i = 1; i < possibilityOfDerivation.length(); i++) {
                if (Character.toString(possibilityOfDerivation.charAt(i)).compareTo("'") == 0) {
                    firstChar += possibilityOfDerivation.charAt(i);
                } else
                    break;
            }
        }
        return firstChar;
    }

    public Derivation getDerivation(String left) {
        for (Derivation derivation : this.derivations) {
            if (derivation.getLeft().compareTo(left) == 0) {
                return derivation;
            }
        }
        return null;
    }

    public String toString() {
        String grammar = "";

        for (Derivation der : this.derivations) {
            grammar += der.toString() + "\n";
        }

        return grammar;
    }
}