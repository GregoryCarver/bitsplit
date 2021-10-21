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
import java.io.File;

public class BitSplit
{
    public static void main(String[] args)
    {
        int peerID = Integer.parseInt(args[0]);
        Peer peer = new Peer(peerID, "localhost", 6000 + (peerID - 1001));
        //When a peer process starts a log file is created with the same peerID
        try
        {
            String filename = "log_peer_" + peerID + ".log";
            File logFile = new File(filename);
            if(logFile.createNewFile())
            {
                System.out.println("Log File Successfully Created: " + logFile.getName());
            }
            else
            {
                System.out.println("File already exists");
            }
        }
        catch (IOException e)
        {
            System.out.println("Error Occurred While Writing Log File");
            e.printStackTrace();
        }
        if (peerID != 1001)
        {
            for (int i = 0; i < peerID - 1001; i++)
            {
                peer.SendMessage("localhost", 6000 + i, new HandShake(peer.peerID));
                // try
                // {
                //     String filename = "log_peer_" + 1002+i + ".log";
                //     File logFile = new File(filename);
                //     if(logFile.createNewFile())
                //     {
                //         System.out.println("Log File Successfully Created: " + logFile.getName());
                //     }
                //     else
                //     {
                //         System.out.println("File already exists");
                //     }
                // }
                // catch (IOException e)
                // {
                //     System.out.println("Error Occurred While Writing Log File");
                //     e.printStackTrace();
                // }
            }
            for (int i = 0; i < peerID - 1001; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    peer.connections.get(i).AddMessage(new Message(j));
                }
            }
        }
//        Server testServer = new Server(8000, handShake);
//        testServer.start();
//        peer.SendMessage("0.0.0.0", 8000, new HandShake(peer.peerID));
//        while(true)
//        {
//            if(!peer.GetConnections().get(0).GetInMessages().isEmpty())
//            {
//                System.out.println(peer.GetConnections().get(0).GetInMessages().size());
//                if(peer.GetConnections().get(0).GetInMessages().peek() instanceof HandShake)
//                {
//                    System.out.println(((HandShake)peer.GetConnections().get(0).GetInMessages().remove()).GetPeerID());
//                }
//                break;
//            }
//        }
        /******* End of testing *******///////////////////////////////////////////////////////////////////////////

    }
}
