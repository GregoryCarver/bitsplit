import messages.HandShake;
import messages.IMessage;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread
{
    int port;
    IMessage message;

    public Server(int port, IMessage message)
    {
        this.port = port;
        this.message = message;
    }

    public void run()
    {
        ServerSocket server = null;
        try
        {
            server = new ServerSocket(port);
            System.out.println("Listening on port " + port + ".");
            while(true)
            {
                new Connection(server.accept(), message);
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
