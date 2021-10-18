package messages;

/***********************************************************************************************************************
 * Used for sending the initial HelloPeer to the peer trying to join the torrent. It sends the name of the file the
 * Tracker is tracking.
 **********************************************************************************************************************/

public class HelloPeer implements IMessage
{
    String fileName;

    public HelloPeer(String fileName)
    {
        this.fileName = fileName;
    }
}
