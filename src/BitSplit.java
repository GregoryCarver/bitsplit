/*****************************************************************************************************************************************
 * BitSplit(p2p application)
 * Team 28: Gregory Carver, Max DiRocco, Christopher Henesy
 * Last Edited: 14 Oct 2021
 * Things to do: Read configs when peer created(add overloaded AddTorrent I think), if object stream works change tracker and setup tracker connection, determine when piece
 *               has been retrieved and update fileBits in the torrent, determine how to pick preferred neighbors, are we using sequential
 *               ids for peers, figure out how to query for peers with correct pieces(cfg files, but I'm going to try to integrate them into an actual tracker object)
 * Left off:
 ****************************************************************************************************************************************/

import messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;

public class BitSplit
{
    public static void main(String[] args)
    {
        //Peer peer = new Peer(7, "1.1.1.1");
        //HandShake handShake = new HandShake(new BitSet(7), 7);

    }

    public class TestServer extends Thread
    {
        ServerSocket server;
        ObjectInputStream input;

        public TestServer()
        {
            try
            {
                server = new ServerSocket(8000);
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
                input = new ObjectInputStream(server.);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
