package messages;
import java.util.BitSet;

/***********************************************************************************************************************
 * Message with no payload. Messages that have a payload inherit from this class.
 * Inherited by: IntMessage; BitFieldMessage
 **********************************************************************************************************************/

public class Message implements IMessage
{
    //May not need this
    public static final byte CHOKE              = 0,
                             UNCHOKE            = 1,
                             INTERESTED         = 2,
                             NOT_INTERESTED     = 3,
                             HAVE               = 4,
                             BITFIELD           = 5,
                             REQUEST            = 6,
                             PIECE              = 7;

    int messageLength;
    byte messageType;

    //Default message
    public Message(int type)
    {
        messageLength = 0;
        messageType = NOT_INTERESTED;
    }

    //Message length will default to 0 unless this is a message with a payload, then it get overridden in that child
    //constructor.
    public Message(byte messageType)
    {
        this.messageType = messageType;
        messageLength = 0;
    }

    public byte getMessageType()
    {
        return this.messageType;
    }

}
