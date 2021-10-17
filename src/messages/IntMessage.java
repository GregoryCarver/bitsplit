package messages;

public class IntMessage extends Message
{
    int index;

    public IntMessage(int messageLength, byte messageType, int index)
    {
        super(messageType);
        this.index = index;
        this.messageLength = 4;
    }
}
