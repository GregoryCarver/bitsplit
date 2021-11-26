package messages.temp;

import messages.IMessage;

/***********************************************************************************************************************
 * Used for sending the initial HelloTracker to the messages.temp.Tracker. It sends the sending peer's peerID.
 **********************************************************************************************************************/

public class HelloTracker implements IMessage
{
    public int peerID;

    public HelloTracker(int peerID)
    {
        this.peerID = peerID;
    }
}
