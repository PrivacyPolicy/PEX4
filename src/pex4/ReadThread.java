package pex4;

import blockchainshell.Block;
import blockchainshell.PolyChain;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReadThread extends Thread {
    
    final private ObjectInputStream stream;
    final private ReadCallback callback;
    private boolean isAlive = true;

    public ReadThread(ObjectInputStream stream, ReadCallback callback) {
        this.stream = stream;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (isAlive) {
            try {
                Object response = stream.readObject();
                if (response instanceof String) {
                    callback.receivedString((String) response);
                } else if (response instanceof Block) {
                    callback.receivedBlock((Block) response);
                } else if (response instanceof PolyChain) {
                    callback.receivedBlockChain((PolyChain) response);
                } else {
                    callback.receivedString("Unknown response (" + response.getClass() + ")");
                }
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            } catch (ClassNotFoundException ex) {
            }
        }
    }
    
    public void kill() {
        isAlive = false;
    }
    
    public interface ReadCallback {

        public abstract void receivedString(String response);

        public abstract void receivedBlock(Block response);

        public abstract void receivedBlockChain(PolyChain response);
        
    }

}
