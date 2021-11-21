package messages.temp;

import messages.IMessage;
import messages.PeerInfo;

import java.util.List;

/***********************************************************************************************************************
 * Tracker uses this to update the peers with the other peer
 **********************************************************************************************************************/

public class TrackerUpdate implements IMessage
{
    public List<PeerInfo> seeders;
    public List<PeerInfo> leechers;

    public TrackerUpdate(List<PeerInfo> seeders, List<PeerInfo> leechers)
    {
        this.seeders = seeders;
        this.leechers = leechers;
    }
}
