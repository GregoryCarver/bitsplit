import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger
{
    public static void log(int peerID, int expectedPeerID, String message, int type)
    {
        try {
            FileWriter fw = new FileWriter("log_peer_" + String.valueOf(peerID) + ".log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("Peer " + String.valueOf(peerID) + ": " + message);
            out.close();
            switch (type) {
                case 0: 
                    break;
                case 1:   
                    break;
                case 2:   
                    break;
                case 3:
                    break;
                case 4:
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
