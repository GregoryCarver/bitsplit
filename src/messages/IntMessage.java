package messages;

/***********************************************************************************************************************
 * Used for sending a message containing an int representing a bitfield index.
 * Used by request and have messages.
 **********************************************************************************************************************/

public class IntMessage extends Message
{
    //Int representing a bitfield index
    public int index;

    public IntMessage(byte messageType, int index)
    {
        super(messageType);
        this.index = index;
        this.messageLength = 4;
    }
}
