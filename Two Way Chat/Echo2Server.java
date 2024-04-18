import java.io.*;
import java.net.*;

public class Echo2Server extends Thread{
	
	public static final int PORT_NUMBER = 54321;
	protected Socket socket;
	
	public static void main(String args[]){
		
		ServerSocket server = null;
		
		try{	
			
			server = new ServerSocket(PORT_NUMBER);
			
			while(true){
				
				new Echo2Server(server.accept());
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
		
	private Echo2Server(Socket socket){
		
		this.socket = socket;
		start();
		
		}
		
	public void run(){
		
		InputStream in = null;
		OutputStream out = null;
		
		try{
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//first we receive message from client - so handle that first
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String clientInput;
			
			while((clientInput = reader.readLine()) != null){
				
				
				
				if("exit".equals(clientInput)){
					
					System.exit(1);
					}
					
				System.out.println("Client's response: " + clientInput);
					
				//send message to client
				
				//reading from server terminal
				BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
				String input = inputReader.readLine();
				
				PrintWriter writer = new PrintWriter(out, true);
				writer.println(input);
				
			}
			 
			 
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
				
				System.out.println("Error occured!");
				System.exit(1);
			}
		}
	}
}
			
		
				