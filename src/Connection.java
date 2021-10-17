import messages.IMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection extends Thread
{
    Socket clientSocket;
    ObjectInputStream input;
    ObjectOutputStream output;
    //***********make this a queue to execute in while loop?
    IMessage message;

    public Connection(Socket clientSocket, IMessage message)
    {
        this.message = message;
        this.clientSocket = clientSocket;
        System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + " in port " + clientSocket.getPort() + ".");
    }
    public void run()
    {
        try
        {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());
            try
            {
                output.writeObject(message);
                output.flush();
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}