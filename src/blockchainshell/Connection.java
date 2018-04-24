package blockchainshell;

import java.io.ObjectOutputStream;

class Connection {
    
    public String clientName;
    public ObjectOutputStream outStream;

    public Connection() {
    }

    public Connection(String clientName, ObjectOutputStream outStream) {
        this.clientName = clientName;
        this.outStream = outStream;
    }

}
