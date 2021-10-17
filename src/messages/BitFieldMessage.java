package messages;

import java.util.BitSet;

public class BitFieldMessage extends Message
{
    BitSet filesBits;

    public BitFieldMessage(int messageLength, byte messageType, BitSet fileBits)
    {
        super(messageType);
        this.filesBits = fileBits;
        this.messageLength = fileBits.length() / 8;
    }
}
