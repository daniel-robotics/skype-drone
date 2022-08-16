/**
 * @(#)SkypeDrone.java
 *
 * SkypeDrone application
 *
 * @author
 * @version 1.00 2013/9/21
 */

import jssc.*;
import java.io.*;
import java.util.Scanner;
import java.net.*;

public class SkypeDrone
{
	public static final String PORT="port.txt";

	public static void main(String[] args)
	{
	    SerialPort serial=null;
		ServerSocket serve=null;
	    Socket socket=null;
	    BufferedReader reader=null;
	    PrintWriter writer=null;
		String[] names=new SerialPortList().getPortNames();

    	if(names.length!=1)
    	{
    		System.out.println("Unable to find Rhino board. "+names.length+" COM ports detected.");
    		System.exit(0);
    	}

		try
		{
			serial=new SerialPort(names[0]);
			serial.openPort();
			serial.setParams(SerialPort.BAUDRATE_57600,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);
			serial.setDTR(false);
			Thread.sleep(750);
			System.out.println("Connected to Rhino board ("+convert(serial.readHexString(""))+") on "+names[0]);
		}
		catch(Exception e){ e.printStackTrace(); System.exit(0); }

		while(true)
    	{
    		try
    		{
    			if(new File(PORT).exists())
	    			serve=new ServerSocket(new Scanner(new File(PORT)).nextInt());
	    		else if(args.length==1)
	    			serve=new ServerSocket(Integer.parseInt(args[0]));
	    		else
	    		{
	    			System.out.println("No valid port specified as argument or in port.txt");
	    			System.exit(0);
	    		}
	    		System.out.println("Waiting for controller client on port "+serve.getLocalPort());
	    		socket=serve.accept();
	    		reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    		writer=new PrintWriter(socket.getOutputStream(), true);
	    		System.out.println("Connection to controller successful!");
	    		writer.println("Connection successful!");
				String input;
				while((input=reader.readLine())!=null)
					if(input.equalsIgnoreCase("monitor off"))
						try{ Runtime.getRuntime().exec("nircmd monitor off"); System.out.println("Turning monitor off..."); }
						catch(Exception e){ e.printStackTrace(); writer.println("Error turning monitor off"); }
					else if(input.equalsIgnoreCase("monitor on"))
						try{ Runtime.getRuntime().exec("nircmd monitor on"); System.out.println("Turning monitor on..."); }
						catch(Exception e){ e.printStackTrace(); writer.println("Error turning monitor on"); }
					else
					{
						System.out.println("Sending "+input+" to board.");
						serial.writeString(input);
					}
    		}
    		catch(Exception e){e.printStackTrace();}
			finally { try
			{
				System.out.println("Disconnected! Restarting...");
				serve.close();
				socket.close();
				reader.close();
				writer.close();
				Thread.sleep(1000);
			} catch(Exception e){e.printStackTrace();} }
    	}
    }

    public static StringBuilder convert(String hex)
    {
    	StringBuilder output = new StringBuilder();
		for (int i = 0; i < hex.length(); i+=2)
		{
			String str = hex.substring(i, i+2);
			output.append((char)Integer.parseInt(str, 16));
		}
		return output;
    }
}
