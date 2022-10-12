import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Grammar {
    private ArrayList<Derivation> derivations;

    public Grammar(ArrayList<Derivation> derivations) {
        this.derivations = derivations;
        buildFirst();
        buildFollow();
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
        // ArrayList<Derivation> derivationListToVerify = 
        for (Derivation der : this.derivations) {
            derivationListToVerify.addAll(der.getAllDerivationPossibilitiesThatContain(derivationLeft)); 
        }
        // System.out.println("Derivation Left: "+ derivationLeft + " | Derivation list to verify: " + derivationListToVerify);

        for (String stringToAnalise : derivationListToVerify) {
            Derivation nonTerminalFather = this.getLeftFromString(stringToAnalise);
            int foundAtIndex = stringToAnalise.indexOf(derivationLeft);
            // System.out.println("Nao terminal achado: " + nonTerminalFather + " da derivacao " + stringToAnalise);
            if(stringToAnalise.length() <= foundAtIndex + derivationLeft.length()){

                ArrayList<String> followsToAdd = nonTerminalFather.getAllFollow();
                // System.out.println("follows to add " + followsToAdd); 

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
                
                    // adiciona todos os firsts do next (menos E)
                    followList.addAll(nextDerivation.getAllFirst());

                    // lança o q falta da regra 3
                    if(nextDerivation.containsEmptyInFirst()){
                        followList.addAll(nextDerivation.getAllFollow());
                    }
                    
                }
            }
            // System.out.println("Follow agora: " + followList);
        }
        cleanRepeatedAndEmpty(followList);
        return followList;
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

    private void cleanRepeatedAndEmpty(ArrayList<String> list) {
        // Removing repeated
        Set<String> setAux = new LinkedHashSet<>(list);
        ArrayList<String> newList = new ArrayList<>(setAux);

        // Cleaning Empty
        for (String s : newList) {
            if (s.compareTo(Utils.EMPTY_SYMBOL) == 0) {
                newList.remove(s);
            }
        }
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

    public ArrayList<ArrayList<String>> first() {
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for (Derivation derivation : this.derivations) {
            list.add(getIndividualFirst(derivation));
        }
        return list;
    }

    public Derivation getDerivation(String left) {
        // System.out.println("RECEIVED LEFT: |" + left + "|");
        for (Derivation derivation : this.derivations) {
            // System.out.println("DERIVATION LEFT: |" + derivation.getLeft() + "|");
            if (derivation.getLeft().compareTo(left) == 0) {
                // System.out.println("FOUND DERIVATION FOR LEFT " + left + " = " + derivation);
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


// for (String right : index.getRight()) {
            //     for (int i = 0; i < right.length(); i++) {
            //         String letter = Character.toString(right.charAt(i));
            //         String next = "";
            //         if (letter.compareTo(derivationLeft) == 0) {
            //             // verify if it's not in the last position
            //             if ((i + 1) >= right.length()) {
            //                 next = "NOTHING";
            //             } else if (Character.toString(right.charAt(i + 1)).compareTo("'") == 0) {
            //                 if ((i + 2) >= right.length()) {
            //                     next = "NOTHING";
            //                 } else {
            //                     next = Character.toString(right.charAt(i + 2));
            //                 }
            //             } else {
            //                 next = Character.toString(right.charAt(i + 1));
            //             }
            //             // MOST IMPORTANT
            //             if (Utils.isTerminal(next)) {
            //                 followList.add(next);
            //             } else {
            //                 if (next.compareTo("NOTHING") != 0) {
            //                     Derivation derivationAux = this.getDerivation(next);
            //                     ArrayList<String> listAux = derivationAux.getAllFirst();
            //                     for (String f : listAux) {
            //                         followList.add(f);
            //                     }
            //                     listAux.clear();
            //                     if (derivationAux.containsEmptyInFirst()) {
            //                         listAux = getIndividualFollow(index);
            //                     }
            //                     for (String f : listAux) {
            //                         followList.add(f);
            //                     }
            //                 }
            //                 // Means that next is nothing and will receive the follow from the left
            //                 else {
            //                     // It will prevent that a derivation received itself many times
            //                     if (index.getLeft().compareTo(letter) != 0) {
            //                         ArrayList<String> listAux = index.getAllFollow();
            //                         if (listAux == null) {
            //                             listAux = getIndividualFollow(index);
            //                         }

            //                         for (String f : listAux) {
            //                             followList.add(f);
                //                     }
                //                 }
                //             }
                //         }
                //     }
                // }