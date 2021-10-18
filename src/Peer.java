import messages.HandShake;
import messages.HelloPeer;
import messages.HelloTracker;
import messages.IMessage;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/***********************************************************************************************************************
 * Peer type that can connect to other peers or trackers. After connecting to a tracker, it can then exchange pieces of
 * the associated file with the other peers involved. Peer has a server that it listens for messages from other peers or
 * a tracker.
 **********************************************************************************************************************/

class Peer
{
    //File torrents currently participating in
    List<Torrent> torrents;
    int serverPort;
    int peerID;
    String ip;
    Server server;
    List<Connection> connections;

    public  Peer(int peerID, String ip, int serverPort)
    {
        ////////////////////////////********************Need to read config files to setup peer values
        torrents = new ArrayList<Torrent>();
        this.peerID = peerID;
        this.ip = ip;
        this.serverPort = serverPort;
        connections = new ArrayList<>();
        //Start listening for peers or trackers
        server = new Server(serverPort, new HandShake(peerID));
        server.start();
    }

    public void AddTorrent(String trackerIP, int trackerPort)
    {
        //Form object stream connection with tracker
        Tracker tracker = null;
        Socket clientSocket = null;
        DataInputStream input;
        DataOutputStream output;

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
        torrents.add(new Torrent(tracker, peerID, new Connection(clientSocket, new HelloTracker(peerID))));
    }

    public List<Connection> GetConnections()
    {
        return connections;
    }

    //*********************************************Used to test for now, might become real method
    public void SendMessage(String ip, int port, IMessage message)
    {
        Socket clientSocket;

        try
        {
            clientSocket = new Socket(ip, port);
            connections.add(new Connection(clientSocket, message));
            connections.get(connections.size() - 1).start();
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
