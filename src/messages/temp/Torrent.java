/*
package messages.temp;

import messages.temp.PeerInfo;
import messages.temp.TrackerInfo;

import java.util.*;

*/
/***********************************************************************************************************************
 * messages.temp.Torrent object used by peers to separate different files and determine who is participating in a particular torrent.
 *
 * ***Using cfg files for the tracker info for project, can change later to generalize tracker.
 **********************************************************************************************************************//*


public class Torrent
{
    //Store tracker info
    TrackerInfo trackerInfo;
    List<PeerInfo> preferredNeighbors;
    //Bits representing whether the peer has that piece
    Dictionary<Integer, BitSet> fileBits;
    Connection trackerConnection;

    public Torrent(int peerID, Connection trackerConnection)
    {
        preferredNeighbors = new ArrayList<PeerInfo>();
        fileBits = new Hashtable<>();
        fileBits.put(peerID, new BitSet(trackerInfo.filePiecesCount));
        this.trackerConnection = trackerConnection;
    }
}*/
