package messages.temp;

import messages.IMessage;

import java.util.BitSet;

/***********************************************************************************************************************
 * Simple class to hold some of the peer's info, used with communication with the tracker.
 **********************************************************************************************************************/

public class PeerInfo implements IMessage
{
    public String hostName;
    public int port;
    public int peerID;
    public boolean isSeeder;

    public PeerInfo(String hostName, int port, int peerID, boolean isSeeder)
    {
        this.hostName = hostName;
        this.port = port;
        this.peerID = peerID;
        this.isSeeder = isSeeder;
    }
}
