package de.unistuttgart.iste.rss.oo.examples.final;

public class Invoice {

    public final static String TAX_REG="DE12345";
    
    private final Integer sum;
    
    public Invoice(final Integer sum) {
        this.sum = sum;
    }

    public Integer getSum() {
        return sum;
    }

}
