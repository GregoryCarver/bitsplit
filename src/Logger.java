import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger
{
    public static void log(int peerID, int expectedPeerID, String message, String type)
    {
        try {
            FileWriter fw = new FileWriter("log_peer_" + String.valueOf(peerID) + ".log", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println("Peer " + String.valueOf(peerID) + ": " + message);
            out.close();
            switch (type) {
                case "choke": 
                    break;
                case "balls":   
                    break;

            }
        } catch (IOException e) {
            System.out.println("Failed to log message!");
        }
    }
}
