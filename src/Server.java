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
    List<Connection> connections;

    public Server(int port, IMessage message)
    {
        this.port = port;
        this.message = message;
        connections = new ArrayList<Connection>();
    }

    public void run()
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(port);
            System.out.println("Listening on port " + port + " at address " + server.getLocalSocketAddress());
            while(true)
            {
                connections.add(new Connection(server.accept(), message));
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
                server.close();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
