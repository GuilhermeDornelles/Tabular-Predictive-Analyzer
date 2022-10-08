import java.util.ArrayList;

public class Grammar {
    public ArrayList<Derivation> derivations;

    public Grammar(ArrayList<Derivation> derivations){
        this.derivations = derivations;
    }

    public ArrayList<Derivation> getDerivations(){
        return this.derivations;
    }

    public ArrayList<String> getIndividualFirst(Derivation derivation){
        ArrayList<String> first = new ArrayList<String>();
        for(String der: derivation.getRight()){
            String first_char = Character.toString(der.charAt(0));
            
            if(first_char != "ε"){
                if(Utils.isTerminal(first_char)){
                    first.add(first_char);
                } else {
                    if(Character.toString(der.charAt(1)) == "'"){
                        first_char = String.format("%s -> %s", Character.toString(der.charAt(0)), Character.toString(der.charAt(1)));
                    }
                    
                    first.add(getIndividualFirst(getDerivation(first_char)).get(0));
                    
                }
            }
        }
        return first;
    }

    public ArrayList<ArrayList<String>> first(){
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for(Derivation derivation: this.derivations){
            list.add(getIndividualFirst(derivation));
        }
        return list;
    }

    public Derivation getDerivation(String left){
        for(Derivation derivation: this.derivations){
            if(derivation.getLeft() == left){
                return derivation;
            }
        }

        return null;
    }

    public String toString(){
        String grammar = "";

        for(Derivation der: this.derivations){
            grammar += der.toString();
        }

        return grammar;
    }
}