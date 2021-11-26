package messages.temp;

import messages.IMessage;

/***********************************************************************************************************************
 * Used for sending the initial HelloPeer to the peer trying to join the torrent. It sends the name of the file the
 * messages.temp.Tracker is tracking.
 **********************************************************************************************************************/

public class HelloPeer implements IMessage
{
    public String fileName;

    public HelloPeer(String fileName)
    {
        this.fileName = fileName;
    }
}
