import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Tracker
{
    String fileName;
    int filePieces;

    public List<PeerInfo> seeders;
    public List<PeerInfo> leechers;

    public Tracker(String fileName)
    {
        this.fileName = fileName;
        seeders = new ArrayList<PeerInfo>();
        leechers = new ArrayList<PeerInfo>();
    }

    public void AddSeeder(String ip, int port, BitSet fileBits)
    {
        seeders.add(new PeerInfo(ip, port, fileBits));
    }

    public void AddLeecher(String ip, int port, BitSet fileBits)
    {
        leechers.add(new PeerInfo(ip, port, fileBits));
    }

    public int GetFilePiecesCount()
    {
        return filePieces;
    }

    //Need to add server and ability to add and get seeder and leecher info, checkout Objectoutputstream
}
