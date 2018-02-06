package server1;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


 class connect_server
{
	
	
	private static ServerSocket server = null;
	private static Socket client = null;
	private static BufferedReader in = null;
	private static String line;
	private static boolean isConnected=true;
	private static Robot robot;
	private static int SERVER_PORT=0 ;
	
	
	public static void main(String [] args)
	{
		
			
		
		for(int i=1;i<1023;i++)
		{
			try
			{
			robot = new Robot();
			server = new ServerSocket(i); 
			if(server!=null)
			{
			SERVER_PORT=i;
			System.out.println("Server is created at port "+server.getLocalPort());	

			break;

			
			}	
		
		

		}
		catch (IOException e) {
			server = null;
			//System.out.println("Error in opening Socket");
			//System.exit(-1);
		}
			
			
		
		catch (AWTException e) {
			System.out.println("Error in creating robot instance");
			System.exit(-1);
		}

		}
		try{
			server.setSoTimeout(100000);
			System.out.println("Waiting for connection....");	
		client = server.accept();
		in = new BufferedReader(new InputStreamReader(client.getInputStream()) );
		

		while(isConnected)
		{
			System.out.println("Connected to the client");
			try {
				line  = in.readLine();
				System.out.println(line);
				if(line.equalsIgnoreCase("Next"))
				{
					//Simulate press and release of key 'n'
					System.out.println("N is pressed");
					robot.keyPress(KeyEvent.VK_N);
					robot.keyRelease(KeyEvent.VK_N);
				}
				//if user clicks on previous
				else if(line.equalsIgnoreCase("Previous")){
					//Simulate press and release of key 'p'
					System.out.println("Prev is pressed");
					robot.keyPress(KeyEvent.VK_P);
					robot.keyRelease(KeyEvent.VK_P);		        	
				}
				//if user clicks on play/pause
				else if(line.equalsIgnoreCase("Play")){
					//Simulate press and release of spacebar
					System.out.println("Play is pressed");
					robot.keyPress(KeyEvent.VK_SPACE);
					robot.keyRelease(KeyEvent.VK_SPACE);
				}
				else if(line.contains(","))
				{
					float disx = Float.parseFloat(line.split(",")[0]);
					float disy = Float.parseFloat(line.split(",")[1]);
					Point p = MouseInfo.getPointerInfo().getLocation();
					double newX = p.getX();
					double newY  = p.getY();
					robot.mouseMove((int)(newX+disx), (int)(newY+disy));
				}
				
				else if(line.contains("left_click"))
				{
					robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				}
				
				
			}
		
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
		catch(Exception e)
		{
			e.printStackTrace();
		}
}	
}	
	

