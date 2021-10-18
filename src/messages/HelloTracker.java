package messages;

/***********************************************************************************************************************
 * Used for sending the initial HelloTracker to the Tracker. It sends the sending peer's peerID.
 **********************************************************************************************************************/

public class HelloTracker implements IMessage
{
    int peerID;

    public HelloTracker(int peerID)
    {
        this.peerID = peerID;
    }
}
