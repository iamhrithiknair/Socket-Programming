import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chat {

    public static final int PORT_NUMBER = 54321;

    public static void main(String[] args) throws IOException {
        // Start both server and client in separate threads with GUI
        SwingUtilities.invokeLater(() -> new ServerGUI());
        SwingUtilities.invokeLater(() -> new ClientGUI("localhost"));
    }
}

// GUI for the Server
class ServerGUI extends JFrame implements Runnable {
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter writer;

    public ServerGUI() {
        // Set up GUI
        setTitle("Server Chat");
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        inputField = new JTextField(50);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        panel.add(inputField, BorderLayout.SOUTH);
        panel.add(sendButton, BorderLayout.EAST);

        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Start server in a new thread
        new Thread(this).start();
    }

    @Override
    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(Chat.PORT_NUMBER);
            chatArea.append("Server started, waiting for connection...\n");
            Socket socket = server.accept();
            chatArea.append("Client connected.\n");

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            writer = new PrintWriter(out, true);

            String clientInput;
            while ((clientInput = reader.readLine()) != null) {
                chatArea.append("Client: " + clientInput + "\n");
                if ("exit".equalsIgnoreCase(clientInput)) {
                    break;
                }
            }
        } catch (IOException ex) {
            chatArea.append("Client disconnected: " + ex.getMessage() + "\n");
        } finally {
            try {
                if (server != null) server.close();
            } catch (IOException ex) {
                chatArea.append("Error closing server: " + ex.getMessage() + "\n");
            }
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty() && writer != null) {
            chatArea.append("Server: " + message + "\n");
            writer.println(message);
            inputField.setText("");
        }
    }
}

// GUI for the Client
class ClientGUI extends JFrame implements Runnable {
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter writer;
    private String host;

    public ClientGUI(String host) {
        this.host = host;

        // Set up GUI
        setTitle("Client Chat");
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        inputField = new JTextField(50);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        panel.add(inputField, BorderLayout.SOUTH);
        panel.add(sendButton, BorderLayout.EAST);

        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Start client in a new thread
        new Thread(this).start();
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(host, Chat.PORT_NUMBER);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            this.writer = writer;
            chatArea.append("Connected to server.\n");

            String serverResponse;
            while ((serverResponse = reader.readLine()) != null) {
                chatArea.append("Server: " + serverResponse + "\n");
            }
        } catch (IOException ex) {
            chatArea.append("Client error: " + ex.getMessage() + "\n");
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty() && writer != null) {
            chatArea.append("Client: " + message + "\n");
            writer.println(message);
            inputField.setText("");
            if ("exit".equalsIgnoreCase(message)) {
                System.exit(0);
            }
            if("bye".equalsIgnoreCase(message)){
                chatArea.append("Disconnecting from the server in 3 seconds..\n");
                writer.println("bye");
                try{
                    Thread.sleep(3000); //introducing delay before closing
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.exit(0);
            }
            
        }
    }
}

