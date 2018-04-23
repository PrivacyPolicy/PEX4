package blockchainshell;

public class Transaction {
    
    private final String publicKey;
    private final String recipientPublicKey;
    private final double amount;
    private final String signature;

    private Transaction(String publicKey, String recipientPublicKey, double amount, String signature) {
        this.publicKey = publicKey;
        this.recipientPublicKey = recipientPublicKey;
        this.amount = amount;
        this.signature = signature;
    }
    
    public static class Builder {

        private String publicKey;
        private String recipientPublicKey;
        private double amount;
        private String signature;

        public Builder() {
        }

        public Builder setPublicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder setRecipientPublicKey(String recipientPublicKey) {
            this.recipientPublicKey = recipientPublicKey;
            return this;
        }

        public Builder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder setSignature(String signature) {
            this.signature = signature;
            return this;
        }

        public Transaction build() {
            return new Transaction(publicKey, recipientPublicKey, amount, signature);
        }

    }


}
