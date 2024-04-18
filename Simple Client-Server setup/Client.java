import java.io.*;
import java.net.*;

public class Client{
	public static void main(String args[]) throws IOException{
		new Client(args[0]);
	}
	public Client(String host) throws IOException{
		Socket socket;
		try{
			socket = new Socket(host, Server.PORT_NUMBER);
		}
		catch(UnknownHostException ex){
			System.out.println(host + "is not a valid host name!");
			return;
		}
		catch(IOException ex){
			System.out.println("Error communicating with" + host);
			return;
		}
		InputStream in = null;
		OutputStream out = null;
		try{
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			 // Send a message to the server
            		String message = "Hi this from client";
            		PrintWriter writer = new PrintWriter(out, true);
            		writer.println(message);
            		
            		// Read server response
            		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            		String serverResponse = reader.readLine();
            		System.out.println("Received from server: " + serverResponse);

		}
		finally{
			try{
				in.close();
				out.close();
				socket.close();
			}
			catch(IOException ex){
				System.out.println("Error occurred!");
				System.exit(1);
			}
		}
	}
}
