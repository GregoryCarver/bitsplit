public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Command Line Argument: " + args[0]);
        Peer peer = new Peer(Integer.parseInt(args[0]));
    }
}
