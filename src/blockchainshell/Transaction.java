package blockchainshell;

import java.io.Serializable;

public class Transaction implements Serializable {
    
    private final String from;
    private final String to;
    private final double amount;

    public Transaction(String from, String to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%s->%s (%f)", from, to, amount);
    }
    
    public static class Builder {

        private String from;
        private String to;
        private double amount;

        public Builder() {
        }

        public Builder setFrom(String from) {
            this.from = from;
            return this;
        }

        public Builder setTo(String to) {
            this.to = to;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Transaction build() {
            return new Transaction(from, to, amount);
        }

    }


}
