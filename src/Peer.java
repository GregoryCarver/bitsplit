import messages.*;

import java.net.*;
import java.io.*;
import java.util.*;

/***********************************************************************************************************************
 * Peer type that can connect to other peers or trackers. After connecting to a tracker, it can then exchange pieces of
 * the associated file with the other peers involved. Peer has a server that it listens for messages from other peers or
 * a tracker.
 **********************************************************************************************************************/

class Peer extends Thread
{
    //File torrents currently participating in
    List<Torrent> torrents;
    int serverPort;
    int peerID;
    String hostName;
    Server server;
    List<Connection> connections;
    //Added for the project
    boolean hasFile;
    BitSet myFileBits;
    //Dictionaries to hold the peers' status
    // key   = peerIDs of the various peers
    // value = obvious from variable name
    Map<Integer, BitSet> fileBits;
    Map<Integer, Boolean> isChoked;
    Map<Integer, Boolean> isInterested;
    int fileSize;
    int pieceSize;

    public  Peer(int peerID, String hostName, int serverPort)
    {
        ////////////////////////////********************Need to read config(just meta now) files to setup peer values
        //fileSize = cfg file
        //pieceSize = cfg file
        torrents = new ArrayList<Torrent>();
        this.peerID = peerID;
        this.hostName = hostName;
        this.serverPort = serverPort;
        connections = new ArrayList<>();
        myFileBits = new BitSet((fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1));
        fileBits = new HashMap<>();
        isChoked = new HashMap<>();
        isInterested = new HashMap<>();
        //Start listening for peers
        server = new Server(serverPort, new HandShake(peerID));
        server.start();
        //Connects the peers with previously joined peers(from the PeerInfo.cfg file
        ConnectPeers();
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
        catch(IOException e)
        {
            e.printStackTrace();
        }

        //Keep connection open while in torrents. Used to update the torrent with tracker info
        torrents.add(new Torrent(peerID, new Connection(clientSocket, new HelloTracker(peerID), -7)));
    }

    public List<Connection> GetConnections()
    {
        return connections;
    }

    //*********************************************Used to test for now, might become real method
    public void SendMessage(String hostName, int port, IMessage message, int peerID)
    {
        Socket clientSocket;

        try
        {
            clientSocket = new Socket(hostName, port);
            connections.add(new Connection(clientSocket, message, peerID));
            connections.get(connections.size() - 1).start();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //Connect the peers to all the peers before it in the PeerInfo.cfg file
    public void ConnectPeers()
    {
        File cfg = new File("/PeerInfo.cfg");
        Scanner cfgReader = null;
        //Store data from file
        try
        {
            cfgReader = new Scanner(cfg);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        StringTokenizer tokenizer = null;

        //While there's still more to read either load this peer's data, or connect with previously joined peers
        while(cfgReader != null && cfgReader.hasNextLine())
        {
            tokenizer = new StringTokenizer(cfgReader.nextLine());
            String tempPeerID = tokenizer.nextToken();
            if (String.valueOf(peerID).equals(tempPeerID))
            {
                this.hostName = tokenizer.nextToken();
                this.serverPort = Integer.parseInt(tokenizer.nextToken());
                if(Integer.parseInt(tokenizer.nextToken()) == 1)
                {
                    this.hasFile = true;
                }
                else
                {
                    this.hasFile = false;
                }
                break;
            }
            else
            {
                Socket clientSocket = null;
                try
                {
                    clientSocket = new Socket(hostName, serverPort);
                } catch(IOException e)
                {
                    e.printStackTrace();
                }
                connections.add(new Connection(clientSocket, new HandShake(Integer.parseInt(tempPeerID)), Integer.parseInt(tempPeerID)));
            }
        }
    }

    public void HandleMessage(IMessage message, int peerID)
    {
        //Check if handshake and the right peerID ////////////////////////////////////make sure this is right peerID(which peer)
        if(message instanceof HandShake && ((HandShake)message).peerID == peerID)
        {

        }
        else
        {
            Message m = (Message)message;
            switch(m.messageType)
            {
                case Message.CHOKE          :
                    isChoked.put(peerID, true);
                    break;
                case Message.UNCHOKE        :
                    isChoked.put(peerID, false);
                    break;
                case Message.INTERESTED     :
                    isInterested.put(peerID, true);
                    break;
                case Message.NOT_INTERESTED :
                    isInterested.put(peerID, false);
                    break;
                case Message.HAVE           :
                    if(fileBits.containsKey(peerID))
                    {
                        fileBits.get(peerID).set(((IntMessage)m).index, true);
                    }
                    else
                    {
                        int pieceCount = (fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1);
                        fileBits.put(peerID, new BitSet(pieceCount));
                        fileBits.get(peerID).set(((IntMessage)m).index, true);
                    }
                    //****************************************************************************************************SEND REQUEST
                    //update requested bitset
                    break;
                case Message.BITFIELD       :
                    BitFieldMessage msg = ((BitFieldMessage)m);
                    for(int i = 0; i < msg.filesBits.size(); i++)
                    {
                        if(msg.filesBits.get(i) == true && myFileBits.get(i) == false)
                        {
                            //******************************************************************************************SEND INTERESTED
                        }
                    }
                    ////***********************************************************************************************SEND NOT INTERESTED
                    break;
                case Message.REQUEST        :
                    if(!isChoked.get(peerID))
                    {
                        //***********************************************************************************************SEND PIECE
                    }
                    break;
                default                     :
                    //(might check if need piece still, just in case) download piece
                    //if (unchoked, they have the piece you want, and you still need pieces) send another request
                    //update requested bitset
                    //
                    break;
            }
        }
    }

    public void run()
    {
        for(int i = 0; i < connections.size(); i++)
        {
            if(connections.get(i).GetInMessages().size() > 0)
            {
                HandleMessage(connections.get(i).GetInMessages().remove(), connections.get(i).GetPeerID());
            }
        }
        for(int i = 0; i < server.connections.size(); i++)
        {

        }
    }
}
