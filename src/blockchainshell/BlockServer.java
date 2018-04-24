/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockchainshell;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;

public class BlockServer {

    private static final int PORT = 2018;
    private static HashSet<String> names = new HashSet<>();
    private static HashSet<String> userNames = new HashSet<>();
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    private static int usersConnected = 0;
    private static PolyChain pc;

    public static void main(String[] args) {
        System.out.println(new Date() + "\nChat Server online.\n");
        pc = new PolyChain();
        pc.addBlock(new Block("Hi im the first block", "0"));
        pc.mine();
        pc.addBlock(new Block("Yo im the second block", pc.getBlockchain().get(pc.getBlockchain().size() - 1).hash));
        pc.mine();
        System.out.println("\nBlockchain is Valid: " + pc.isChainValid());
        pc.printBlock();

        try (ServerSocket chatServer = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = chatServer.accept();
                System.out.println("new client connecting...");
                new ClientHandler(socket).start();
            }
        } catch (IOException ioe) {
        }
    }

    private static String names() {
        StringBuilder nameList = new StringBuilder();

        for (String name : userNames) {
            nameList.append(", ").append(name);
        }

        return "In lobby: " + nameList.substring(2);
    }

    private static class ClientHandler extends Thread {

        private String name;
        private String serverSideName;
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        private SecureRandom ran = new SecureRandom();

        public ClientHandler(Socket socket) {
            this.socket = socket;

        }

        @Override
        public void run() {
            int count = 0;
            String response = "";
            String name = "";
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("got output stream");
                in = new ObjectInputStream(socket.getInputStream());
                System.out.println("sending note to new client... hello");
                out.writeObject("Hello \n");

                System.out.println("said hello");
                out.flush();

                try {
                    name = (String) (in.readObject());
                } catch (ClassNotFoundException ex) {
                }
                name = name.toLowerCase();
                System.out.println(name + " has joined the network");

                synchronized (names) {
                    System.out.println("Now conducting duplicate checks");
                    if (names.contains(name)) {
                        System.out.println("We found a duplicate");
                        name += count++;
                    }
                }

                out.writeObject("NAME_ACCEPTED \n");
                out.flush();
                System.out.println(name + " connected. IP: " + socket.getInetAddress().getHostAddress());

                userNames.add(name);
                names.add(name);
                writers.add(out);
                sendMessagesToAll("CONNECT " + name);
                out.writeObject("INFO " + ++usersConnected + names() + "\n");
                out.flush();

                System.out.println("Sending blockchain to new client");
                out.writeObject(pc);
                out.flush();
                System.out.println("Successfully sent blockchain to new client");

                while (true) {
                    try {
                        readObject();
                    } catch (ClassNotFoundException ex) {
                        System.err.println("Class not found");
                    }
                }

            } catch (IOException e) {
                {
                    System.out.println(name + " disconnected.");
                    userNames.remove(name);
                    names.remove(serverSideName);
                    writers.remove(out);
                    sendMessagesToAll("DISCONNECT" + name);
                    usersConnected--;
                }
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

        private void readObject() throws IOException, ClassNotFoundException {
            Object response = in.readObject();
            String message;
            if (response instanceof Block) {
                Block newBlock = (Block) response;
                if (pc.chainWouldBeValidWith(newBlock)) {
                    pc.addBlock(newBlock);
                    message = "BLOCK_ACCEPTED";
                    sendBlockToAll(newBlock);
                } else {
                    message = "BLOCK_REJECTED";
                }
            } else if (response instanceof PolyChain) {
                message = "DO NOT SEND ME A POLYCHAIN YOU IDIOT";
            } else if (response instanceof Transaction) {
                Transaction newTransaction = (Transaction) response;
                if (pc.isValidTransaction(newTransaction)) {
                    pc.addTransaction(newTransaction);
                    sendTransactionToAll(newTransaction);
                    message = "TRANSACTION_ACCEPTED";
                } else {
                    message = "TRANSACTION_REJECTED_STOP_TRYING_TO_STEAL_MY_MONEY";
                }
            } else {
                message = "UKNOWN_OBJECT_SENT";
            }
            
            out.writeObject(message);
            out.flush();
        }
        
        private static void sendObject(Object object) {
            if (!writers.isEmpty()) {

                for (ObjectOutputStream writer : writers) {
                    try {
                        writer.writeObject(object);
                        writer.flush();
                    } catch (IOException ex) {
                        writers.remove(writer);
                        usersConnected--;
                    }
                }
            }
        }

        private static void sendMessagesToAll(String... messages) {
            System.out.println("messaging all");
            for (String message : messages) {
                System.out.println("sending " + message + " to all");
                sendObject(message + "\n");
            }
        }

        private static void sendBlockToAll(Block nb) {
            System.out.println("messaging all a new block");
            sendObject(nb);
        }

        private static void sendTransactionToAll(Transaction transaction) {
            System.out.println("messaging all a new transaction");
            sendObject(transaction);
        }
    }

}
