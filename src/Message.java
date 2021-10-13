import java.util.BitSet;

public class Message
{
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
    BitSet message;

    public Message()
    {
        messageLength = 0;
        messageType = CHOKE;
        message = null;
    }

    public Message(byte messageType, BitSet message)
    {
        this.messageType = messageType;
        this.message = message;

        switch(messageType)
        {
            case HAVE:
            case REQUEST:
            {
                this.messageLength = 4;
                this.message = message;
                break;
            }
            case BITFIELD:
            case PIECE:
            {
                this.messageLength = message.length() / 8;
                this.message = message;
                break;
            }
            default:
            {
                this.messageLength = 0;
                this.message = null;
                break;
            }
        }
    }
}
