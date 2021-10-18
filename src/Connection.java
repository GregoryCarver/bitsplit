import messages.IMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

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

    //Make the connection in the constructor
    public Connection(Socket clientSocket, IMessage message)
    {
        this.messagesOut = new LinkedList<>();
        messagesOut.add(message);
        this.messagesIn = new LinkedList<>();

        this.clientSocket = clientSocket;
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + " in port " + clientSocket.getPort() + ".");
    }
    //When the thread is started, send the queued messages and wait for responses
    public void run()
    {
        try
        {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());

            while(true)
            {
                //If there is a message to be sent, send it
                if(!messagesOut.isEmpty())
                {
                    output.writeObject(messagesOut.remove());
                    output.flush();
                }

                messagesIn.add((IMessage)input.readObject());
            }

        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
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