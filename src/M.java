public class M {
    private String nonTerminal;
    private String terminal;
    private Derivation derivation;

    public M(String nonTerminal, String terminal, Derivation derivation){
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
        this.derivation = derivation;
    }

    public String getNonTerminal() {
        return this.nonTerminal;
    }

    public String getTerminal() {
        return this.terminal;
    }

    public Derivation getDerivation() {
        return this.derivation;
    }

    public String toString(){
        return String.format("M[%s,%s] = %s",this.nonTerminal, this.terminal, this.derivation);
    }
}
