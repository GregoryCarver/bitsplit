/*****************************************************************************************************************************************
 * BitSplit(p2p application)
 * Team 28: Gregory Carver, Max DiRocco, Christopher Henesy
 * Last Edited: 17 Oct 2021
 * Things to do: Read configs when peer created(add overloaded AddTorrent I think), determine when piece
 *               has been retrieved and update fileBits in the torrent, determine how to pick preferred neighbors,
 *               figure out how to query for peers with correct pieces(cfg files, but I'm going to try to integrate them into an actual tracker object)
 * Left off: create test server in this class for testing the messages
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
        /******* Used for testing *******///////////////////////////////////////////////////////////////////////////
        Peer peer = new Peer(7, "1.0.0.0", 8001);
        HandShake handShake = new HandShake(new BitSet(80), peer.peerID);
        Server testServer = new Server(8000, handShake);
        testServer.start();
        peer.SendMessage("0.0.0.0", 8000, new HandShake(peer.peerID));
        while(true)
        {
            if(!peer.GetConnections().get(0).GetInMessages().isEmpty())
            {
                System.out.println(peer.GetConnections().get(0).GetInMessages().size());
                if(peer.GetConnections().get(0).GetInMessages().peek() instanceof HandShake)
                {
                    System.out.println(((HandShake)peer.GetConnections().get(0).GetInMessages().remove()).GetPeerID());
                }
                break;
            }
        }
        /******* End of testing *******///////////////////////////////////////////////////////////////////////////

    }
}
