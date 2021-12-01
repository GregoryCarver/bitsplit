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
            switch (type) {
                case 0:
                LocalTime time1 = LocalTime.now();
                fw.write(time1.format(formatter) + ": Peer " + expectedPeerID + " is unchoked by " + peerID);
                fw.close();
                System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 1:   
                    LocalTime time1 = LocalTime.now();
                    fw.write(time1.format(formatter) + ": Peer " + expectedPeerID + " is choked by " + peerID);
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 2:   
                    LocalTime time1 = LocalTime.now();
                    fw.write(time1.format(formatter) + ": Peer " + expectedPeerID + " received the 'interested' message from " + peerID);
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 3:
                    LocalTime time1 = LocalTime.now();
                    fw.write(time1.format(formatter) + ": Peer " + expectedPeerID + " is not interested in " + peerID);
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 4:
                    LocalTime time1 = LocalTime.now();
                    fw.write(time1.format(formatter) + ": Peer " + expectedPeerID + " has " + peerID);
                    fw.close();
                    System.out.println("Successfully wrote to " + logFile + ".");
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;

            }
        } catch (IOException e) {
            System.out.println("Failed to log message!");
        }
    }
}
