import messages.HandShake;
import messages.IMessage;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Peer
{
    //File torrents currently participating in
    List<Torrent> torrents;
    int serverPort;
    int peerID;
    String ip;
    Server server;

    public  Peer(int peerID, String ip, int serverPort)
    {
        ////////////////////////////********************Need to read config files to setup peer values
        torrents = new ArrayList<Torrent>();
        this.peerID = peerID;
        this.ip = ip;
    }

    public void AddTorrent(String trackerIP, int trackerPort)
    {
        //Form object stream connection with tracker
        Tracker tracker = null;
        Socket clientSocket;
        DataInputStream input;
        DataOutputStream output;
        //Start listening after joining torrent
        server = new Server(serverPort, new HandShake(new BitSet(80), peerID));

        //Connect with the tracker
        try
        {
            clientSocket = new Socket(trackerIP, trackerPort);
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        //Keep connection open while in torrents. Used to update the torrent with tracker info
        // ( might make different connection types derived from connection(like object connection, data connection, etc.)

        torrents.add(new Torrent(tracker));
    }

    public void run()
    {

    }
}
