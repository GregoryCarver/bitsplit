import messages.HandShake;
import messages.IMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/***********************************************************************************************************************
 * Server that either a peer or tracker can use. Listens for client, and then makes connection with them.
 **********************************************************************************************************************/

public class Server extends Thread
{
    int port;
    IMessage message;
    int peerID;
    volatile List<Connection> connections;
    volatile boolean isStopped;

    public Server(int port, IMessage message, int peerID)
    {
        this.port = port;
        this.message = message;
        this.peerID = peerID;
        connections = new ArrayList<Connection>();
        isStopped = false;
    }

    public void run()
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket();
            System.out.println("Listening on port " + port + " at address " + server.getLocalSocketAddress());
            while(!isStopped)
            {
                connections.add(new Connection(server.accept(), message, peerID, connections.size()));
                connections.get(connections.size() - 1).start();
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                System.out.println("Server stopped.");
                server.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        return;
    }

    public void StopServer()
    {
        for(int i = 0; i < connections.size(); i++)
        {
            connections.get(i).EndConnection();
            connections.remove(i);
        }
        isStopped = true;
    }
}
