/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class PolyChain implements Serializable {

    private HashMap<String, Double> wallets = new HashMap<>();
    private ArrayList<Block> blockchain = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private static int difficulty = 5;

    public PolyChain() {
    }
    
    public void addTransaction(Transaction t) {
        transactions.add(t);
        addToBalance(wallets, t.getFrom(), -t.getAmount());
        addToBalance(wallets, t.getTo(), t.getAmount());
    }

    public void addBlock(Block b) {
        blockchain.add(b);
        addToBalance(wallets, b.getOwner(), 1);
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }
    
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public HashMap<String, Double> getWallets() {
        return wallets;
    }

    public void printBlock() {
        for (Block b : blockchain) {
            System.out.println("Hash for Block " + (blockchain.indexOf(b) + 1) + ": " + b.getHash());
        }
    }

    public void mine() {
        blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    }
    
    public boolean chainWouldBeValidWith(Block newBlock) {
        Block lastBlock = blockchain.get(blockchain.size() - 1);
        return blockPairIsValid(lastBlock, newBlock);
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            if (!blockPairIsValid(previousBlock, currentBlock)) return false;
        }
        return true;
    }

    private boolean blockPairIsValid(Block previousBlock, Block currentBlock) {
        //compare registered hash and calculated hash:
        if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
            System.out.println("Current Hashes not equal");
            return false;
        }
        //compare previous hash and registered previous hash
        if (!previousBlock.hash.equals(currentBlock.previousHash)) {
            System.out.println("Previous Hashes not equal");
            return false;
        }
        //check if hash is solved
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
            System.out.println("This block hasn't been mined");
            return false;
        }
        return true;
    }

    public boolean isValidTransaction(Transaction newTransaction) {
        if (newTransaction.getAmount() <= 0) {
            return false;
        }
        double amountInSendersWallet = wallets.get(newTransaction.getFrom());
        return amountInSendersWallet >= newTransaction.getAmount();
    }

    private void addToBalance(HashMap<String, Double> wallets, String person, double amount) {
        double oldBalance = wallets.getOrDefault(person, (double) 0);
        wallets.put(person, oldBalance + amount);
    }

}
