/*****************************************************************************************************************************************
 * BitSplit(p2p application)
 * Team 28: Gregory Carver, Max DiRocco, Christopher Henesy
 * Last Edited: 14 Oct 2021
 * Things to do: Read configs when peer created, if object stream works change tracker and setup tracker connection, determine when piece
 *               has been retrieved and update fileBits in the torrent, determine how to pick preferred neighbors, are we using sequential
 *               ids for peers, figure out how to query for peers with correct pieces(check tracker I'm guessing)
 * Left off in AddTorrent and need to add server functionality to tracker
 ****************************************************************************************************************************************/


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Peer
{
    private static final int serverPort = 8000;
    //File torrents currently participating in
    List<Torrent> torrents;
    int peerID;
    String ip;

    public class Connection extends Thread
    {
        Socket clientSocket;
        DataInputStream input;
        DataOutputStream output;

        public Connection(String ip, int port)
        {
            try
            {
                clientSocket = new Socket(ip, port);
                System.out.println("Connected to " + ip + " in port " + port);

            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        public void run()
        {
            try
            {
                output = new DataOutputStream(clientSocket.getOutputStream());
                output.flush();
                input = new DataInputStream(clientSocket.getInputStream());
                while(true)
                {

                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public  Peer(int peerID, String ip)
    {
        ////////////////////////////Need to read config files to setup peer values
        torrents = new ArrayList<Torrent>();
        this.peerID = peerID;
        this.ip = ip;
    }

    public void SendHandshake(String ip, int port)
    {
        new Connection(ip, port);
    }

    public void AddTorrent(String trackerIP, int trackerPort)
    {
        //Form object stream connection with tracker, return tracker
        Tracker tracker;
        Socket clientSocket;
        DataInputStream input;
        DataOutputStream output;

        //Keep connection open while in torrents. Used to update the torrent with tracker info
        // ( might make different connection types derived from connection(like object connection, data connection, etc.)

        torrents.add(new Torrent(tracker));
    }
}
