package messages;

import java.io.File;
import java.util.BitSet;

public class PieceMessage extends Message
{
    public int index;
    public byte[] file;

    public PieceMessage(byte messageType, int index, byte[] file)
    {
        super(messageType);
        this.index = index;
        this.messageLength = file.length;
        this.file = file;
    }
}
