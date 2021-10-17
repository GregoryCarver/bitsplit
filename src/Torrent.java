import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

//Torrent object used by peers to separate different files and determine who is participating in a particular torrent
public class Torrent
{
    //Need to either get info directly from the tracker server, or update this tracker periodically
    Tracker tracker;
    List<PeerInfo> preferredNeighbors;
    //Bits representing whether the peer has that piece
    BitSet fileBits;
    //******************maybe a hashtable for the fileBits of all other peers

    public Torrent(Tracker tracker)
    {
        this.tracker = tracker;
        preferredNeighbors = new ArrayList<PeerInfo>();
        fileBits = new BitSet(tracker.GetFilePiecesCount());
    }

}