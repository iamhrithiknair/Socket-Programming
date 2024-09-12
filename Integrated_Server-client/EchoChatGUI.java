import java.io.*;
import java.net.*;

public class EchoChatGUI {

    public static final int PORT_NUMBER = 54321;

    public static void main(String[] args) throws IOException {
        // Start both server and client in separate threads
        new Thread(new ServerThread()).start();
        new Thread(new ClientThread("localhost")).start();
    }
}

class ServerThread implements Runnable {
    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(EchoChatGUI.PORT_NUMBER);
            System.out.println("Server started, waiting for connection...");
            Socket socket = server.accept();
            System.out.println("Client connected.");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintWriter writer = new PrintWriter(out, true);

            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

            String clientInput;
            while ((clientInput = reader.readLine()) != null) {
                System.out.println("Client: " + clientInput);
                if ("exit".equalsIgnoreCase(clientInput)) {
                    break;
                }
                System.out.print("Server: ");
                String serverResponse = inputReader.readLine();
                writer.println(serverResponse);
            }
        } catch (IOException ex) {
            System.out.println("Client has closed the connection: " + ex.getMessage());
        } finally {
            try {
                if (server != null) server.close();
            } catch (IOException ex) {
                System.out.println("Error closing server: " + ex.getMessage());
            }
        }
    }
}

class ClientThread implements Runnable {
    private String host;

    public ClientThread(String host) {
        this.host = host;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, EchoChatGUI.PORT_NUMBER);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            String userInput;
            while ((userInput = inputReader.readLine()) != null) {
                writer.println(userInput);
                if (("exit".equalsIgnoreCase(userInput)) || "bye".equalsIgnoreCase(userInput)) {
                    break;
                }
                String serverResponse = reader.readLine();
                System.out.println("Server: " + serverResponse);
            }
        } catch (IOException ex) {
            System.out.println("Client error: " + ex.getMessage());
        }
    }
}
