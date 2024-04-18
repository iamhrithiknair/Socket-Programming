import java.io.*;
import java.net.*;

public class Echo2Client{
	
	public static void main(String args[]) throws IOException{
		
		new Echo2Client(args[0]);
		}
		
	public Echo2Client (String host) throws IOException{
		
		Socket socket = null;
		
		try{
			
			socket = new Socket(host, Echo2Server.PORT_NUMBER);
			
			}
		
		catch(UnknownHostException ex){	
			
			System.out.println("hostname not identified" + host);
			return;
			
			
			}
			
		catch(IOException ex){	
			
			System.out.println("Error communicating with: " + host);
			
			}
			
		InputStream in = null;
		OutputStream out = null;
		
		try{
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String userInput;
			
			while((userInput = input.readLine()) != null){	
			
				PrintWriter writer = new PrintWriter(out, true);
				writer.println(userInput);
				
				if("exit".equals(userInput)){
					
					break;
					}
					
				
				//read server response
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String server_response = reader.readLine();
				System.out.println("Server's Response: " + server_response);
				
			}
			
		}
			
			
		catch(IOException ex){
			
			System.out.println("Unable to get streams from server!");
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

				
				