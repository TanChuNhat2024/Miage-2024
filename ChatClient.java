
import java.io.*;
import java.net.*;

public class ChatClient {

    private final String host;
    private final int port;
    private final String username;

    public ChatClient(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    public void start() throws IOException {
        Socket socket = new Socket(host, port);
        System.out.println("Connecté au serveur sur " + host + ":" + port);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println(username);

        new Thread(() -> {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            writer.println(username + ": " + message);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.out.println("Usage: java ChatClient <host> <port> <username>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String username = args[2];

        ChatClient client = new Chat