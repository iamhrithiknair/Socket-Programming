import java.io.*;
import java.net.*;

public class Server extends Thread{
	
	public static final int PORT_NUMBER = 54321;
	protected Socket socket;
	
	public static void main(String args[]){
		ServerSocket server = null;		
		try{
			server = new ServerSocket(PORT_NUMBER);
			while(true){
				new Server(server.accept());
				}
			}
		catch(IOException ex){
			System.out.println("Unable to start server");
			System.exit(1);
		}
		
		finally{
			try{
				server.close();
				}
			
			catch(IOException ex){
				System.out.println("Error occurred!");
				System.exit(1);
			}
		}
	}
	private Server(Socket socket){
		this.socket = socket;
		start();
	}
	
	public void run(){
		InputStream in = null;
		OutputStream out = null;
		try{
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//Read client input
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String clientInput = reader.readLine();
			System.out.println("Received from client: " +clientInput);
			
			//Send response back to the client
			String serverResponse = "Hello this is from server";
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(serverResponse);
			}
		catch(IOException ex){
			System.out.println("Unable to get streams from client");
			
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
		
			
