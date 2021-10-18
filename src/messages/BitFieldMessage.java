package messages;

import java.util.BitSet;

/***********************************************************************************************************************
 * Used for sending the bitfield message, which has a set of bits that represent whether the peer sending it had
 * the corresponding piece of the file.
 **********************************************************************************************************************/

public class BitFieldMessage extends Message
{
    BitSet filesBits;

    public BitFieldMessage(int messageLength, byte messageType, BitSet fileBits)
    {
        super(messageType);
        this.filesBits = fileBits;
        //Store the length of the message in bytes. 8 bits = 1 byte
        this.messageLength = fileBits.length() / 8;
    }
}
