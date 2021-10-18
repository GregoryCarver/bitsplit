import messages.HelloPeer;
import messages.PeerInfo;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/***********************************************************************************************************************
 * Tracks what peers are participating in the torrent and updates them of the state of all the neighbors. Has a server
 * that listens for peers to join the torrent, and also periodically updates the peers with the seeders and leechers
 * lists.
 *
 * Instances of this are stored in the torrents that peers store
 **********************************************************************************************************************/

public class Tracker extends Thread
{
    //Tracker port
    int port;
    //Name of the file this tracker corresponds to
    String fileName;
    //How many pieces this file is
    int filePiecesCount;
    //Size of each piece of the file
    int pieceSize;
    //Server for listening for peers and updating peers
    Server server;

    List<PeerInfo> seeders;
    List<PeerInfo> leechers;

    public Tracker(String fileName, int filePiecesCount, int port)
    {
        this.fileName = fileName;
        this.filePiecesCount = filePiecesCount;
        this.port = port;
        seeders = new ArrayList<PeerInfo>();
        leechers = new ArrayList<PeerInfo>();
        server = new Server(port, new HelloPeer(fileName));
        server.start();
    }

    public void AddSeeder(String ip, int port, int peerID, BitSet fileBits)
    {
        seeders.add(new PeerInfo(ip, port, peerID, fileBits));
    }

    public void AddLeecher(String ip, int port, int peerID, BitSet fileBits)
    {
        leechers.add(new PeerInfo(ip, port, peerID, fileBits));
    }

    //Getters, may not need after all though
    public String GetFileName()
    {
        return fileName;
    }

    public int GetFilePiecesCount()
    {
        return filePiecesCount;
    }

    public List<PeerInfo> GetSeeders() { return seeders; }

    public List<PeerInfo> GetLeechers() { return leechers; }
}
