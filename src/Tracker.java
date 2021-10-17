import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

//Tracks what peers are participating in the torrent
public class Tracker extends Thread
{
    String fileName;
    int filePiecesCount;

    List<PeerInfo> seeders;
    List<PeerInfo> leechers;

    public Tracker(String fileName)
    {
        this.fileName = fileName;
        seeders = new ArrayList<PeerInfo>();
        leechers = new ArrayList<PeerInfo>();
    }

    public void AddSeeder(String ip, int port, int peerID, BitSet fileBits)
    {
        seeders.add(new PeerInfo(ip, port, peerID, fileBits));
    }

    public void AddLeecher(String ip, int port, int peerID, BitSet fileBits)
    {
        leechers.add(new PeerInfo(ip, port, peerID, fileBits));
    }

    public String GetFileName()
    {
        return fileName;
    }

    public int GetFilePiecesCount()
    {
        return filePiecesCount;
    }

    //Need to add server and ability to add and get seeder and leecher info, checkout Objectoutputstream
}
