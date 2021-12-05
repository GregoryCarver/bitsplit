import messages.*;

import java.net.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    int pieceCount;
    volatile boolean isPeerListening;

    public  Peer(int peerID)
    {
        this.peerID = peerID;
        SetupPeer();
        this.pieceCount = (fileSize / pieceSize) + ((fileSize % pieceSize) == 0 ? 0 : 1);
        connections = new ArrayList<>();
        myFileBits = new BitSet(pieceCount);
        myRequestedBits = new BitSet(pieceCount);
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
            myFileBits.set(1, pieceCount - 1);
            File theFile = new File(fileDir + "/" + fileName);
            SplitFile(theFile);
        }
        else
        {
            myFileBits.set(0, pieceCount - 1);
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
                connections.get(connections.size() - 1).start();
            }
        }
    }

    public void HandleMessage(IMessage message, int peerID, int peerPosition)
    {
        Connection fromConnection = connections.get(peerPosition);
        //Check if handshake and the right peerID ////////////////////////////////////make sure this is right peerID(which peer)
        if(message instanceof HandShake && ((HandShake)message).peerID == peerID)
        {
            for(int i = 0; i < pieceCount; i++)
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
                        fileBits.put(peerID, new BitSet(pieceCount));
                        fileBits.get(peerID).set(index, true);
                    }
                    ///***************************************************test********************need to figure out if this thread will work for requests
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
                    ///*******************************************test***************************************************
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
                    IntMessage rmsg = ((IntMessage)m);
                    if(!isChoked.get(peerID) && myFileBits.get(rmsg.index))
                    {
                        byte[] pieceContent = new byte[0];
                        try
                        {
                            pieceContent = Files.readAllBytes(Paths.get(String.valueOf(this.peerID) + String.valueOf(rmsg.index)));
                        } catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        PieceMessage pmsg = new PieceMessage(Message.PIECE, rmsg.index, pieceContent);
                        fromConnection.AddMessage(pmsg);
                    }
                    break;
                default                     :
                    //check if need piece still, just in case, download piece
                    PieceMessage pmsg = ((PieceMessage)m);
                    myRequestedBits.clear(pmsg.index);
                    if(!myFileBits.get(pmsg.index))
                    {
                        myFileBits.set(pmsg.index);
                        File output = new File("peer_" + String.valueOf(this.peerID) + "/" + String.valueOf(pmsg.index));
                        try
                        {
                            Files.write(output.toPath(), pmsg.file);
                        } catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    //if (they have the piece you want, and you still need pieces) send another request
                    int nextClearIndex = myFileBits.nextClearBit(0);
                    if(nextClearIndex != -1 && fileBits.get(peerID).get(nextClearIndex))
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run()
                            {
                                myRequestedBits.set(nextClearIndex, true);
                                while(myRequestedBits.get(nextClearIndex))
                                {
                                    fromConnection.AddMessage(new IntMessage(Message.REQUEST, nextClearIndex));
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
                    }
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
                FileWriter writer = new FileWriter("peer_" + String.valueOf(peerID) + "/" + String.valueOf(pieceCount++));
                BufferedWriter buffer = new BufferedWriter(writer);
                while(charChecker != -1 && byteCount++ < pieceSize)
                {
                    char currChar = (char)charChecker;
                    buffer.write(currChar);
                    charChecker = reader.read();
                }
                byteCount = 0;
                buffer.flush();
                buffer.close();
            }

            reader.close();
        } catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    public boolean GotAllPieces()
    {
        for(int i = 0; i < pieceCount; i++)
        {
            if(myFileBits.get(i) == false)
            {
                return false;
            }
        }
        return true;
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
                Connection currConnection = server.connections.get(i);
                if(currConnection.GetInMessages().size() > 0)
                {
                    HandleMessage(currConnection.GetInMessages().remove(), currConnection.GetPeerID(), currConnection.GetPeerPos());
                }
            }
        }
    }
}
