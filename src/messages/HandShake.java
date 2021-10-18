package messages;

import java.util.BitSet;

/***********************************************************************************************************************
 * Used for sending the initial Handshake. Sends a field of 0 bits, and the peerID of the sending peer.
 **********************************************************************************************************************/

public class HandShake implements  IMessage
{
    public final static String handShakeHeader = "P2PFILESHARINGPROJ";
    //10 bytes of 0 bits
    public BitSet zeroBits;
    //PeerID of the sending peer
    public int peerID;

    public HandShake(BitSet zeroBits, int peerID)
    {
        this.zeroBits = zeroBits;
        this.peerID = peerID;
    }
    public HandShake(int peerID)
    {
        this.zeroBits = new BitSet(80);
        this.peerID = peerID;
    }
}
