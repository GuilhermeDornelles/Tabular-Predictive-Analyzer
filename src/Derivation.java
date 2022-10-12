import java.util.ArrayList;

public class Derivation {
    private String left;
    private ArrayList<String> right;
    private ArrayList<String> first;
    private ArrayList<String> follow;

    public Derivation(String left, ArrayList<String> right) {
        this.left = left;
        this.right = right;
        this.first = new ArrayList<>();
        this.follow = new ArrayList<>();
    }

    public boolean derivatesEmpty(){
        for(String option: this.right){
            if(option.contains(Utils.EMPTY_SYMBOL)){
                return true;
            }
        }
        return false;
    }

    public boolean containsEmptyInFirst(){
        for(String option: this.first){
            if(option.compareTo(Utils.EMPTY_SYMBOL) == 0){
                return true;
            }
        }
        return false;
    }

    public boolean containsInRight(String letter){
        for(String option: this.right){
            if(option.contains(letter)){
                return true;
            }
        }
        return false;
    }

    private boolean verifyFollow(String follow) {
        for (String f : this.follow) {
            if (f.compareTo(follow) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean verifyFirst(String first) {
        for (String f : this.first) {
            if (f.compareTo(first) == 0) {
                return true;
            }
        }
        return false;
    }

    public boolean addFirst(String first) {
        if (!verifyFirst(first)) {
            this.first.add(first);
            return true;
        }
        return false;
    }

    public boolean addFollow(String follow) {
        if (!verifyFollow(follow)) {
            this.follow.add(follow);
            return true;
        }
        return false;
    }

    public ArrayList<String> getAllFirst() {
        if(this.first.size() > 0){
            return this.first;
        }
        return null;
    }

    public ArrayList<String> getAllFollow() {
        if(this.follow.size() > 0){
            return this.follow;
        }
        return null;
    }

    public String toString() {
        String right = "";
        int i = 0;
        for (String index : this.right) {
            if (i > 0) {
                right += " | ";
            }

            right += index;

            i++;
        }
        return (String.format("%s -> %s", this.left, right));
    }

    public String getLeft() {
        return this.left;
    }

    public ArrayList<String> getRight() {
        return this.right;
    }

    public ArrayList<String> getAllDerivationPossibilitiesThatContain(String derivationLeft) {
        ArrayList<String> listPossibilitiesString = new ArrayList<>();
        for (String possibility : this.getRight()) {
            if(possibility.contains(derivationLeft)){
                int foundAtIndex = possibility.indexOf(derivationLeft);
                if(possibility.length() <= foundAtIndex + derivationLeft.length()){
                    listPossibilitiesString.add(possibility);
                    continue;
                }
                String nextCharOfDerivationLeft = Character.toString(possibility.charAt(foundAtIndex + (derivationLeft.length())));
                if(nextCharOfDerivationLeft.compareTo("'") != 0){
                    listPossibilitiesString.add(possibility);
                }
            }
        }
        return listPossibilitiesString;
    }
}