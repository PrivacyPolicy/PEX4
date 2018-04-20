/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.util.ArrayList;

public class PolyChain {

    private  ArrayList<Block> blockchain = new ArrayList<>();
    private static int difficulty = 5;

    public PolyChain() {
    }
    
    public void addBlock(Block b){
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
    
    public void mine(){
        blockchain.get(blockchain.size()-1).mineBlock(difficulty);
    }

    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
// YOUR TASK
        return true;
    }

}
