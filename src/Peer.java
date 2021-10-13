import java.net.*;
import java.io.*;
import java.util.BitSet;

public class Peer
{
    String ip;
    int serverPort;
    int port;
    BitSet[] neighborBits;

    Socket peerSocket;
    DataInputStream input;
    DataOutputStream out;

    public Peer()
    {

    }
    public boolean CreateConnection()
    {
        boolean wasSuccessful = false;

        return wasSuccessful;
    }

}
