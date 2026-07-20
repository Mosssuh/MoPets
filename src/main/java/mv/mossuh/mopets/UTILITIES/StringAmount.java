package mv.mossuh.mopets.UTILITIES;

public class StringAmount {

    private String string = "NONE";
    private double amount = 1;
    public StringAmount(String string, Double amount) {
        if (string != null) { this.string = string; }
        if (amount != null) { this.amount = amount; }
    }

    public String getString() { return string; }
    public double getAmount() { return amount; }
}
