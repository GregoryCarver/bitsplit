import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger
{
    public static void log(int peerID, int expectedPeerID, String message, int type)
    {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String logFile = "log_peer_" + String.valueOf(peerID) + ".log";
            FileWriter fw = new FileWriter(logFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("Peer " + String.valueOf(peerID) + ": " + message);
            out.close();
            LocalTime time1 = LocalTime.now();
            switch (type) {
                case 0:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " is unchoked by " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 1:   
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " is choked by " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 2:   
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " received the 'interested' message from " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 3:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " received the 'not interested' message from " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 4:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " received the 'have' message from " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 5:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " makes a connection to Peer " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 6:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " is connected from Peer " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 7:
                    //needs to have the prefered neighbor list passed in and iterated through
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " has the preferred neighbors " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 8:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " has the optimistically unchoked neighbor " + expectedPeerID + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 9:
                    //needs to be given the total number of pieces that the peer has and the piece index
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " has downloaded the piece " + 1 + " from  " + expectedPeerID
                    + ". Now the number of pieces it has is " + 1 + ".");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 10:
                    fw.write(time1.format(formatter) + ": Peer " + peerID + " has downloaded the complete file.");
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;

            }
        } catch (IOException e) {
            System.out.println("Failed to log message!");
        }
    }
}
