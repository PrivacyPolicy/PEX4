/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

/**
 *
 * @author Dean
 */
import java.io.Serializable;
import java.security.MessageDigest;

public class Block implements Serializable {

    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    //Block Constructor.
    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input, 
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer(); //
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Calculate new hash based on blocks contents
    public String calculateHash() {
        return applySha256(
                previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + data);
    }

    public void mineBlock(int difficulty) {
        do {
            nonce++;
            hash = calculateHash();
        } while (!isValidHash(hash, difficulty));
    }
    
    private static boolean isValidHash(String hash, int difficulty) {
        return firstNCharactersAre(hash, difficulty, '0');
    }
    
    private static boolean firstNCharactersAre(String hash, int difficulty, char c) {
        if (hash.length() < difficulty) return false;
        for (int i = 0; i < difficulty; i++) {
            if (hash.charAt(i) != c) return false;
        }
        return true;
    }

    public String getHash() {
        return hash;
    }

}
