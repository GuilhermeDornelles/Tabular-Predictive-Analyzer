import java.util.ArrayList;

public class Grammar {
    public ArrayList<Derivation> derivations;

    public Grammar(ArrayList<Derivation> derivations){
        this.derivations = derivations;
    }

    public ArrayList<Derivation> getDerivations(){
        return this.derivations;
    }

    public ArrayList<String> getAllFirst(){
        ArrayList<String> list = new ArrayList<>();
        for(Derivation derivation: this.derivations){
            String first = "";
            ArrayList<String> individualFirst = getIndividualFirst(derivation);
            for(int i=0; i<individualFirst.size(); i++){
                if(i+1 == individualFirst.size()){
                    first += individualFirst.get(i);
                } else {
                    first += individualFirst.get(i) +", ";
                }
            }
            list.add(String.format("First(%s) = %s", derivation.getLeft(), first));
        }
        return list;
    }

    public ArrayList<String> getIndividualFirst(Derivation derivation){
        ArrayList<String> listOfFirsts = new ArrayList<String>();
        for(String possibilityOfDerivation: derivation.getRight()){
            String first_char = returnFirstChar(possibilityOfDerivation);
            if(Utils.isCharValid(first_char)) {
                if(Utils.isTerminal(first_char)){
                        listOfFirsts.add(first_char);
                } else {
                    Derivation fullDerivation = getDerivation(first_char);
                    ArrayList<String> individualFirst = getIndividualFirst(fullDerivation);
                    listOfFirsts.addAll(individualFirst);
                }
            }
        }
        return listOfFirsts;
    }

    private String returnFirstChar(String possibilityOfDerivation){
        if(possibilityOfDerivation.length() == 0) return "";
        String firstChar = Character.toString(possibilityOfDerivation.charAt(0));
        // logic to concatenate in firstChar all ' in case we have A', A'', A''', etc
        if(possibilityOfDerivation.length() >= 1){
            for (int i = 1; i < possibilityOfDerivation.length(); i++) {
                if(Character.toString(possibilityOfDerivation.charAt(i)).compareTo("'") == 0){
                    firstChar += possibilityOfDerivation.charAt(i);
                }
                else break;
            }
        }
        return firstChar;
    }

    public ArrayList<ArrayList<String>> first(){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for(Derivation derivation: this.derivations){
            list.add(getIndividualFirst(derivation));
        }
        return list;
    }

    public Derivation getDerivation(String left){
        System.out.println("RECEIVED LEFT: |" + left + "|");
        for(Derivation derivation: this.derivations){
            // System.out.println("DERIVATION LEFT: |" + derivation.getLeft() + "|");
            if(derivation.getLeft().compareTo(left) == 0){
                System.out.println("FOUND DERIVATION FOR LEFT " + left + " = " + derivation);
                return derivation;
            }
        }
        return null;
    }

    public String toString(){
        String grammar = "";

        for(Derivation der: this.derivations){
            grammar += der.toString() + "\n";
        }

        return grammar;
    }
}