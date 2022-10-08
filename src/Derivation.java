import java.util.ArrayList;

public class Derivation{
    private String left;
    private ArrayList<String> right;

    public Derivation(String left, ArrayList<String> right){
        this.left = left;
        this.right = right;
    }

    public String toString(){
        String right = "";
        int i=0;
        for(String index: this.right){
            if(i>0){
                right += "|";
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
}