import java.util.BitSet;

//Simple class to hold some of the peer's info//////////////////////////////Might be able to send objects across tcp as is, send peers directly instead of just info(aka delete this)
public class PeerInfo
{
    public String ip;
    public int port;
    public BitSet fileBits;

    public PeerInfo(String ip, int port, BitSet fileBits)
    {
        this.ip = ip;
        this.port = port;
        this.fileBits = fileBits;
    }
}
