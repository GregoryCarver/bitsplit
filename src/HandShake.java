import java.util.BitSet;

public class HandShake
{
    final static String handShakeHeader = "P2PFILESHARINGPROJ";
    BitSet zeroBits;
    int peerID;

    public HandShake(BitSet zeroBits, int peerID)
    {
        this.zeroBits = zeroBits;
        this.peerID = peerID;
    }
}
