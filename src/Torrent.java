import messages.PeerInfo;

import java.util.*;

//Torrent object used by peers to separate different files and determine who is participating in a particular torrent
public class Torrent
{
    //Need to either get info directly from the tracker server, or update this tracker periodically
    Tracker tracker;
    List<PeerInfo> preferredNeighbors;
    //Bits representing whether the peer has that piece
    Dictionary<String, BitSet> fileBits;
    Connection trackerConnection;

    public Torrent(Tracker tracker, int peerID, Connection trackerConnection)
    {
        this.tracker = tracker;
        preferredNeighbors = new ArrayList<PeerInfo>();
        fileBits = new Hashtable<>();
        fileBits.put(String.valueOf(peerID), new BitSet(tracker.GetFilePiecesCount()));
        this.trackerConnection = trackerConnection;
    }

}