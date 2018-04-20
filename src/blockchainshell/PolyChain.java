/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.Serializable;
import java.util.ArrayList;

public class PolyChain implements Serializable {

    private ArrayList<Block> blockchain = new ArrayList<>();
    private static int difficulty = 5;

    public PolyChain() {
    }

    public void addBlock(Block b) {
        blockchain.add(b);
    }

    public ArrayList<Block> getBlockchain() {
        return blockchain;
    }

    public void printBlock() {
        for (Block b : blockchain) {
            System.out.println("Hash for Block " + (blockchain.indexOf(b) + 1) + ": " + b.getHash());
        }
    }

    public void mine() {
        blockchain.get(blockchain.size() - 1).mineBlock(difficulty);
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
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
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

}
