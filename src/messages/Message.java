package messages;

import java.util.BitSet;
//Message with no payload. Messages that have a payload inherit from this class
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
    public Message()
    {
        messageLength = 0;
        messageType = NOT_INTERESTED;
    }

    public Message(byte messageType)
    {
        messageLength = 0;
    }
}
