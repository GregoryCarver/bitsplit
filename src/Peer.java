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
    int serverPort;
    int peerID;
    String hostName;
    Server server;
    List<Connection> connections;
    boolean hasFile;
    BitSet myFileBits;
    volatile BitSet myRequestedBits;
    Map<Integer, BitSet> fileBits;
    Map<Integer, Boolean> isChoked;
    Map<Integer, Boolean> isInterested;
    int numOfPrefNeighbors;
    int unchokingInterval;
    int optimisticInterval;
    int fileSize;
    String fileName;
    int pieceSize;
    volatile boolean isPeerListening;

    public  Peer(int peerID)
    {
        this.peerID = peerID;
        SetupPeer();
        try {
            String logFile = "log_peer_" + String.valueOf(peerID) + ".log";
            File log = new File(logFile);
            if (log.createNewFile()) {
              System.out.println("File created: " + log.getName());
            } else {
              System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        connections = new ArrayList<>();
        myFileBits = new BitSet((fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1));
        myRequestedBits = new BitSet((fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1));
        isChoked = new HashMap<>();
        isInterested = new HashMap<>();
        //Start listening for peers
        server = new Server(serverPort, new HandShake(peerID), peerID);
        server.start();
        //Connects the peers with previously joined peers(from the PeerInfo.cfg file
        ConnectPeers();

        isPeerListening = true;
        File fileDir = new File("peer_" + String.valueOf(peerID));
        if(fileDir.list().length != 0)
        {
            myFileBits.set(0, myFileBits.size());
            File theFile = new File(fileDir + "/" + fileName);
            SplitFile(theFile);
        }
    }

    public List<Connection> GetConnections()
    {
        return connections;
    }

    public void SetupPeer()
    {
        File cfg = new File("Common.cfg");
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

        //While there's data to read, load common data
        while(cfgReader != null && cfgReader.hasNextLine())
        {
            tokenizer = new StringTokenizer(cfgReader.nextLine());
            String temp = tokenizer.nextToken();
            if(temp.compareTo("NumberOfPreferredNeighbors") == 0)
            {
                numOfPrefNeighbors = Integer.parseInt(tokenizer.nextToken());
            }
            else if(temp.compareTo("UnchokingInterval") == 0)
            {
                unchokingInterval = Integer.parseInt(tokenizer.nextToken());
            }
            else if(temp.compareTo("OptimisticUnchokingInterval") == 0)
            {
                optimisticInterval = Integer.parseInt(tokenizer.nextToken());
            }
            else if(temp.compareTo("FileName") == 0)
            {
                fileName = tokenizer.nextToken();
            }
            else if(temp.compareTo("FileSize") == 0)
            {
                fileSize = Integer.parseInt(tokenizer.nextToken());
            }
            else
            {
                pieceSize = Integer.parseInt(tokenizer.nextToken());
            }

        }
    }

    //Connect the peers to all the peers before it in the PeerInfo.cfg file
    public void ConnectPeers()
    {
        File cfg = new File("PeerInfo.cfg");
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
                connections.add(new Connection(clientSocket, new HandShake(Integer.parseInt(tempPeerID)), Integer.parseInt(tempPeerID), connections.size()));
            }
        }
    }

    public void HandleMessage(IMessage message, int peerID, int peerPosition)
    {
        Connection fromConnection = connections.get(peerPosition);
        //Check if handshake and the right peerID ////////////////////////////////////make sure this is right peerID(which peer)
        if(message instanceof HandShake && ((HandShake)message).peerID == peerID)
        {
            for(int i = 0; i < myFileBits.size(); i++)
            {
                if(myFileBits.get(i) == true)
                {
                    fromConnection.AddMessage(new BitFieldMessage(Message.BITFIELD, myFileBits));
                }
            }
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
                    int index = ((IntMessage)m).index;
                    if(fileBits.containsKey(peerID))
                    {
                        fileBits.get(peerID).set(index, true);
                    }
                    else
                    {
                        int pieceCount = (fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1);
                        fileBits.put(peerID, new BitSet(pieceCount));
                        fileBits.get(peerID).set(index, true);
                    }
                    ///***********************************************************************need to figure out what to do if request is never responded to
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            myRequestedBits.set(index, true);
                            while(myRequestedBits.get(index) == true)
                            {
                                fromConnection.AddMessage(new IntMessage(Message.REQUEST, index));
                                try
                                {
                                    Thread.sleep(1000);
                                } catch(InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    //Send request for the index'th piece
                    //fromConnection.AddMessage(new IntMessage(Message.REQUEST, index));
                    //Update myRequestedBits so that we know it's requesting that piece

                    break;
                case Message.BITFIELD       :
                    BitFieldMessage msg = ((BitFieldMessage)m);
                    for(int i = 0; i < msg.filesBits.size(); i++)
                    {
                        //If there are still pieces needed, then send an interested message
                        if(msg.filesBits.get(i) == true && myFileBits.get(i) == false)
                        {
                            fromConnection.AddMessage(new Message(Message.INTERESTED));
                            break;
                        }
                    }
                    //else send a not interested message
                    fromConnection.AddMessage(new Message());
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

    public void SplitFile(File file)
    {
        try
        {
            FileReader reader = new FileReader(file);
            int charChecker = -1;
            int byteCount = 0;
            int pieceCount = 0;
            while((charChecker = reader.read()) != -1)
            {
                FileWriter writer = new FileWriter("peer_" + String.valueOf(peerID) + "/" + String.valueOf(byteCount++) + ".file");
                while(charChecker != -1 && byteCount++ < pieceSize)
                {
                    char currChar = (char)charChecker;
                    writer.write(currChar);
                    charChecker = reader.read();
                }

            }
        } catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public void run()
    {
        while(isPeerListening)
        {
            for(int i = 0; i < connections.size(); i++)
            {
                Connection currConnection = connections.get(i);
                if(currConnection.GetInMessages().size() > 0)
                {
                    HandleMessage(currConnection.GetInMessages().remove(), currConnection.GetPeerID(), currConnection.GetPeerPos());
                }
            }
            for(int i = 0; i < server.connections.size(); i++)
            {
                Connection currConnection = connections.get(i);
                if(currConnection.GetInMessages().size() > 0)
                {
                    HandleMessage(currConnection.GetInMessages().remove(), currConnection.GetPeerID(), currConnection.GetPeerPos());
                }
            }
        }
    }
}
