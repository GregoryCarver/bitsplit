package messages;

/***********************************************************************************************************************
 * Used for sending a message containing an int representing a bitfield index.
 * Used by request and have messages.
 **********************************************************************************************************************/

public class IntMessage extends Message
{
    //Int representing a bitfield index
    int index;

    public IntMessage(int messageLength, byte messageType, int index)
    {
        super(messageType);
        this.index = index;
        this.messageLength = 4;
    }
}
