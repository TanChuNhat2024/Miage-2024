
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private final int port;
    private final List<ClientHandler> clients;

    public ChatServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur démarré sur le port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Nouveau client connecté : " + clientSocket.getInetAddress().getHostAddress());

            ClientHandler handler = new ClientHandler(clientSocket);
            clients.add(handler);
            new Thread(handler).start();
        }
    }

    private class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final BufferedReader reader;
        private final PrintWriter writer;

        public ClientHandler(Socket clientSocket) throws IOException {
            this.clientSocket = clientSocket;
            this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.writer = new PrintWriter(clientSocket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = reader.readLine();
                    if (message == null) {
                        break;
                    }

                    System.out.println("Message reçu de " + clientSocket.getInetAddress().getHostAddress() + " : " + message);

                    for (ClientHandler client : clients) {
                        if (client != this) {
                            client.sendMessage(message);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessage(String message) {
            writer.println(message);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java ChatServer <port>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        ChatServer server = new ChatServer(port);
        server.start();
    }
}