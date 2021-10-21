import messages.HandShake;
import messages.IMessage;
import messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/***********************************************************************************************************************
 * Class used to run the connections made. Can be used to listen on a server socket, or used my a client process to
 * connect to a server. Sends a single message, waits for a response, and then closes.
 * Used by: Peers, and Tracker
 **********************************************************************************************************************/
////////////////////////////////////////////////////////////////got to fix connection to be persistent I believe
class Connection extends Thread
{
    Socket clientSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    volatile Queue<IMessage> messagesOut;
    volatile Queue<IMessage> messagesIn;
    boolean handshake;
    int expectedPeerID;
    int peerID;

    //Make the connection in the constructor
    public Connection(Socket clientSocket, IMessage message, int peerID)
    {
        this.peerID = peerID;
        this.messagesOut = new LinkedList<>();
        messagesOut.add(message);
        this.messagesIn = new LinkedList<>();

        this.clientSocket = clientSocket;
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + " in port " + clientSocket.getPort() + ".");
        //Writes to the log file created earlier, uses LocalTime to determine the time.
        //Formatter converts the time into a string.
        /*
        Had to add a peerID parameter to the constructor to make this work, but if theres
        another way to do this feel free to change it.
        */
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String peer1 = "log_peer_" + peerID + ".log";
        String peer2 = "log_peer_" + expectedPeerID + ".log";
        try
        {
            FileWriter writer1 = new FileWriter(peer1);
            LocalTime time1 = LocalTime.now();
            writer1.write(time1.format(formatter) + ": Peer " + peerID + "makes a connection to Peer " + expectedPeerID);
            writer1.close();
            System.out.println("Successfully wrote to " + peer1 + ".");
        }
        catch(IOException e)
        {
            System.out.println("An Error Occurred when Trying to Write to the Log File for " + peer1 + ".");
            e.printStackTrace();
        }
        try
        {
            FileWriter writer2 = new FileWriter(peer2);
            LocalTime time2 = LocalTime.now();
            writer2.write(time2.format(formatter) + ": Peer " + peerID + "makes a connection to Peer " + expectedPeerID);
            writer2.close();
            System.out.println("Successfully wrote to " + peer2 + ".");
        }
        catch(IOException e)
        {
            System.out.println("An Error Occurred when Trying to Write to the Log File for " + peer2 + ".");
            e.printStackTrace();
        }
    }
    //When the thread is started, send the queued messages and wait for responses
    public void run()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try
        {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());

            while(true)
            {
                //If there is a message to be sent, send it
                while(!messagesOut.isEmpty())
                {
                    output.writeObject(messagesOut.remove());
                    output.flush();
                }

                messagesIn.add((IMessage)input.readObject());
                if (messagesIn.peek() != null)
                {
                    if (messagesIn.peek() instanceof HandShake)
                    {
                        HandShake hs = (HandShake) messagesIn.peek();
                        assert hs != null;
                        this.expectedPeerID = hs.GetPeerID();
                        System.out.println("HandShake: " + hs.GetPeerID());
                    }
                    else {
                        Message msg = (Message) messagesIn.peek();
                        assert msg != null;
                        String logfile;
                        switch (msg.getMessageType()) {
                            case 0:
                                logfile = "log_peer_" + expectedPeerID + ".log";
                                try
                                {
                                    FileWriter writer1 = new FileWriter(logfile);
                                    LocalTime time1 = LocalTime.now();
                                    writer1.write(time1.format(formatter) + ": Peer " + expectedPeerID + " is unchoked by " + peerID);
                                    writer1.close();
                                    System.out.println("Successfully wrote to " + logfile + ".");
                                }
                                catch(IOException e)
                                {
                                    System.out.println("An Error Occurred when Trying to Write to the Log File for " + logfile + ".");
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                logfile = "log_peer_" + expectedPeerID + ".log";
                                try
                                {
                                    FileWriter writer1 = new FileWriter(logfile);
                                    LocalTime time1 = LocalTime.now();
                                    writer1.write(time1.format(formatter) + ": Peer " + expectedPeerID + " is choked by " + peerID);
                                    writer1.close();
                                    System.out.println("Successfully wrote to " + logfile + ".");
                                }
                                catch(IOException e)
                                {
                                    System.out.println("An Error Occurred when Trying to Write to the Log File for " + logfile + ".");
                                    e.printStackTrace();
                                }
                                break;
                            case 2:
                                logfile = "log_peer_" + expectedPeerID + ".log";
                                try
                                {
                                    FileWriter writer1 = new FileWriter(logfile);
                                    LocalTime time1 = LocalTime.now();
                                    writer1.write(time1.format(formatter) + ": Peer " + expectedPeerID + " received the 'interested' message from " + peerID);
                                    writer1.close();
                                    System.out.println("Successfully wrote to " + logfile + ".");
                                }
                                catch(IOException e)
                                {
                                    System.out.println("An Error Occurred when Trying to Write to the Log File for " + logfile + ".");
                                    e.printStackTrace();
                                }
                                break;
                            case 3:
                                logfile = "log_peer_" + expectedPeerID + ".log";
                                try
                                {
                                    FileWriter writer1 = new FileWriter(logfile);
                                    LocalTime time1 = LocalTime.now();
                                    writer1.write(time1.format(formatter) + ": Peer " + expectedPeerID + " received the 'not interested' message from " + peerID);
                                    writer1.close();
                                    System.out.println("Successfully wrote to " + logfile + ".");
                                }
                                catch(IOException e)
                                {
                                    System.out.println("An Error Occurred when Trying to Write to the Log File for " + logfile + ".");
                                    e.printStackTrace();
                                }
                                break;
                            case 4:
                                logfile = "log_peer_" + expectedPeerID + ".log";
                                try
                                {
                                    FileWriter writer1 = new FileWriter(logfile);
                                    LocalTime time1 = LocalTime.now();
                                    writer1.write(time1.format(formatter) + ": Peer " + expectedPeerID + " received the 'have' message from " + peerID);
                                    writer1.close();
                                    System.out.println("Successfully wrote to " + logfile + ".");
                                }
                                catch(IOException e)
                                {
                                    System.out.println("An Error Occurred when Trying to Write to the Log File for " + logfile + ".");
                                    e.printStackTrace();
                                }
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            default:
                                break;
                        }
                    }
                    messagesIn.remove();
                }
            }

        }
        catch(IOException | ClassNotFoundException e)
        {
            System.out.println("Connection closed on port: " + clientSocket.getPort());
        }
        finally
        {
            try {
                input.close();
                output.close();
                clientSocket.close();
            }
            catch (IOException e)
            {
                System.out.println("Connection closed on port: " + clientSocket.getPort());
            }
        }
    }

    public void AddMessage(IMessage message)
    {
        messagesOut.add(message);
    }

    public Queue<IMessage> GetInMessages()
    {
        return messagesIn;
    }
}