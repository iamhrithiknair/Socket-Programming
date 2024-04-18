import java.io.*;
import java.net.*;

public class EchoClient{
	public static void main(String args[]) throws IOException{
		new EchoClient(args[0]);
	}
	public EchoClient(String host) throws IOException{
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
			
			 
            		//SENDING MESSAGE TO SERVER THROUGH USERINPUT FROM CLIENT
            		BufferedReader userInputReader = new BufferedReader(new  
            		InputStreamReader(System.in));
            		
            		String userInput;
            		while ((userInput = userInputReader.readLine()) != null) {
                	// Send user input to the server
                	PrintWriter writer = new PrintWriter(out, true);
                	writer.println(userInput);
                	
                	//Read server response
            		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            		String serverResponse = reader.readLine();
            		System.out.println("Server's response: " + serverResponse);
                	
            		

		}
		}
		catch (IOException ex) {
            		System.out.println("Error occurred while communicating with the server");
            		}
		finally{
			try{
				if(in != null) in.close();
				if(out != null) out.close();
				socket.close();
			}
			catch(IOException ex){
				System.out.println("Error occurred!");
				System.exit(1);
			}
		}
	}
}
