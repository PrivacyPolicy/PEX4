package blockchainshell;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pex4.ReadThread;

public class CommunicationTest {
    
    private PolyChain blockChain;

    public CommunicationTest() throws Exception {
        String url = "localhost"; //"cop3330.hpc.lab";
        short port = 2018;
        InetAddress address = InetAddress.getByName(url);
        Socket socket = new Socket(address, port);
        ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
        ReadThread thread = new ReadThread(inStream, new ReadThread.ReadCallback() {
            @Override
            public void receivedString(String response) {
                System.out.println("Recieved message: " + response.trim());
            }

            @Override
            public void receivedBlock(Block response) {
                System.out.println("Received Block: " + response.getHash());
                blockChain.addBlock(response);
            }

            @Override
            public void receivedBlockChain(PolyChain response) {
                try {
                    System.out.println("Recieved blockchain: " + response.getBlockchain().toString());
                    blockChain = response;
                    Block b = new Block("modena", getLatestHash());
                    b.mineBlock(5);
                    sendBlock(outStream, b);
                } catch (IOException ex) {
                    Logger.getLogger(CommunicationTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        thread.start();
        sendMessage(outStream, "modena");
    }
    
    private String getLatestHash() {
        return blockChain.getBlockchain().get(
                blockChain.getBlockchain().size() - 1).getHash();
    }
    
    public static void main(String[] args) throws Exception {
        new CommunicationTest();
    }
    
    public static void sendMessage(ObjectOutputStream outStream, String message) throws IOException {
        System.out.println("Sending message: " + message);
        outStream.writeObject(message);
        outStream.flush();
    }

    private static void sendBlock(ObjectOutputStream outStream, Block b) throws IOException {
        System.out.println("Sending block: " + b.getHash());
        outStream.writeObject(b);
        outStream.flush();
    }
    
}