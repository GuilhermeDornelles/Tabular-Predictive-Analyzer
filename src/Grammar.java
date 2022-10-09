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
        System.out.println("RECEIVED LEFT: |" + left + "|");
        for(Derivation derivation: this.derivations){
            System.out.println("DERIVATION LEFT: |" + derivation.getLeft() + "|");
            // Compare if is the same String
            if(derivation.getLeft().compareTo(left) == 0){
                System.out.println("FOUND THE SAME LEFT!!! ");
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