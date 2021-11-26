package messages.temp;

import messages.IMessage;

import java.util.List;

/***********************************************************************************************************************
 * messages.temp.Tracker uses this to initialize connections with peers.
 **********************************************************************************************************************/

public class TrackerInfo implements IMessage
{
    public String fileName;
    public int filePiecesCount;

    public List<PeerInfo> seeders;
    public List<PeerInfo> leechers;

    public TrackerInfo(String fileName, int filePiecesCount, List<PeerInfo> seeders, List<PeerInfo> leechers)
    {
        this.fileName = fileName;
        this.filePiecesCount = filePiecesCount;
        this.seeders = seeders;
        this.leechers = leechers;
    }
}
