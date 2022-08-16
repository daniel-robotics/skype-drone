/**
 * @(#)SkypeDroneController.java
 *
 * SkypeDroneController application
 *
 * @author
 * @version 1.00 2013/9/21
 */
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.BorderLayout;

public class SkypeDroneController
{
	public static final String defaultInput="minds1.no-ip.org:80";


	public static String host;
	public static int port;
	public static Socket socket=null;
	public static PrintWriter writer=null;
	public static BufferedReader reader=null;
	public static String message="Connecting to SkypeDrone...";
	public static GraphicPanel gp=new GraphicPanel();
    public static ButtonPanel bp=new ButtonPanel();
    public static String lastMessage="-1";

    public static void main(String[] args)
    {
    	JFrame f=new JFrame("SkypeDrone Controller");
    	f.setSize(300,165);
    	f.setResizable(false);
    	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	f.getContentPane().add(gp, BorderLayout.CENTER);
    	f.getContentPane().add(bp, BorderLayout.SOUTH);
    	f.setVisible(true);

    	String s=JOptionPane.showInputDialog(f,"Enter the SkypeDrone's IP address and port",defaultInput);
    	if(s==null)
    		System.exit(0);
    	host=s.substring(0,s.lastIndexOf(":"));
    	port=Integer.parseInt(s.substring(s.lastIndexOf(":")+1));

		try
		{
			socket=new Socket(InetAddress.getByName(host),port);
			writer=new PrintWriter(socket.getOutputStream(), true);
			reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			gp.addKeyListener(gp);
			bp.mtoggle.setEnabled(true);
			String input;
			while((input=reader.readLine())!=null)
			{
				message=input;
				gp.repaint();
			}
		}
		catch(Exception e){ e.printStackTrace(); }
		finally{ try
		{
			writer.close();
			reader.close();
			socket.close();
		}
		catch(Exception e){e.printStackTrace();} }
		System.exit(0);
}

    public static class GraphicPanel extends JPanel implements KeyListener
    {
    	boolean up=false;
    	boolean down=false;
    	boolean left=false;
    	boolean right=false;

    	ImageGetter getter=new ImageGetter();
    	Image forward=null;
    	Image backward=null;
    	Image spinright=null;
    	Image spinleft=null;
    	Image forwright=null;
    	Image forwleft=null;
    	Image backright=null;
    	Image backleft=null;
    	Image stop=null;

    	public GraphicPanel()
    	{
			forward=getter.get("forward.png");
			backward=getter.get("backward.png");
			spinright=getter.get("spinright.png");
			spinleft=getter.get("spinleft.png");
			forwright=getter.get("forwardright.png");
			forwleft=getter.get("forwardleft.png");
			backright=getter.get("backwardright.png");
			backleft=getter.get("backwardleft.png");
			stop=getter.get("stop.png");
			setFocusable(true);
			repaint();
    	}

    	private class ImageGetter
    	{
    		public Image get(String filename)
    		{
    			return new ImageIcon(this.getClass().getClassLoader().getResource(filename)).getImage();
    		}
    	}

    	public void paintComponent(Graphics g)
    	{
    		super.paintComponent(g);
    		g.drawString("Use arrow keys to move SkypeDrone around.",26,20);
    		g.drawString(message,26,93);
    		if(up)
				if(down)
					stop(g);
				else if(left)
					forwardleft(g);
				else if(right)
					forwardright(g);
				else
					forward(g);
    		else if(down)
				if(up)
					stop(g);
				else if(left)
					backwardleft(g);
				else if(right)
					backwardright(g);
				else
					backward(g);
    		else if(left)
				if(up)
					forwardleft(g);
				else if(down)
					backwardleft(g);
				else if(right)
					stop(g);
				else
					spinleft(g);
    		else if(right)
				if(up)
					forwardright(g);
				else if(left)
					stop(g);
				else if(down)
					backwardright(g);
				else
					spinright(g);
    		else
				stop(g);
    	}

    	public void keyPressed(KeyEvent e)
    	{
    		int k=e.getKeyCode();
    		switch(k)
    		{
    			case KeyEvent.VK_UP:
    				up=true; break;
    			case KeyEvent.VK_DOWN:
    				down=true; break;
    			case KeyEvent.VK_LEFT:
    				left=true; break;
    			case KeyEvent.VK_RIGHT:
    				right=true; break;
    		}
			repaint();
    	}

    	public void keyReleased(KeyEvent e)
    	{
    		int k=e.getKeyCode();
    		switch(k)
    		{
    			case KeyEvent.VK_UP:
    				up=false; break;
    			case KeyEvent.VK_DOWN:
    				down=false; break;
    			case KeyEvent.VK_LEFT:
    				left=false; break;
    			case KeyEvent.VK_RIGHT:
    				right=false; break;
    		}
			repaint();
    	}

    	public void keyTyped(KeyEvent e){}


    	public void forward(Graphics g)
    	{
			g.drawImage(forward,125,25,50,50,null);
			g.drawString("Forward",26,35);
			if(writer!=null && !lastMessage.equals("1"))
				writer.println(lastMessage="1");
    	}
       	public void forwardleft(Graphics g)
    	{
			g.drawImage(forwleft,125,25,50,50,null);
			g.drawString("Forward-Left",26,35);
			if(writer!=null && !lastMessage.equals("2"))
				writer.println(lastMessage="2");
    	}
       	public void forwardright(Graphics g)
    	{
			g.drawImage(forwright,125,25,50,50,null);
			g.drawString("Forward-Right",26,35);
			if(writer!=null && !lastMessage.equals("3"))
				writer.println(lastMessage="3");
    	}
       	public void backward(Graphics g)
    	{
			g.drawImage(backward,125,25,50,50,null);
			g.drawString("Backward",26,35);
			if(writer!=null && !lastMessage.equals("4"))
				writer.println(lastMessage="4");
    	}
       	public void backwardleft(Graphics g)
    	{
			g.drawImage(backleft,125,25,50,50,null);
			g.drawString("Back-Left",26,35);
			if(writer!=null && !lastMessage.equals("5"))
				writer.println(lastMessage="5");
    	}
       	public void backwardright(Graphics g)
    	{
			g.drawImage(backright,125,25,50,50,null);
			g.drawString("Back-Right",26,35);
			if(writer!=null && !lastMessage.equals("6"))
				writer.println(lastMessage="6");
    	}
       	public void spinleft(Graphics g)
    	{
			g.drawImage(spinleft,125,25,50,50,null);
			g.drawString("Spin Left",26,35);
			if(writer!=null && !lastMessage.equals("7"))
				writer.println(lastMessage="7");
    	}
       	public void spinright(Graphics g)
    	{
			g.drawImage(spinright,125,25,50,50,null);
			g.drawString("Spin Right",26,35);
			if(writer!=null && !lastMessage.equals("8"))
				writer.println(lastMessage="8");
    	}
    	public void stop(Graphics g)
    	{
			g.drawImage(stop,125,25,50,50,null);
			g.drawString("Stop",26,35);
			if(writer!=null && !lastMessage.equals("10"))
				writer.println(lastMessage="10");
    	}
    }

    public static class ButtonPanel extends JPanel
    {
    	public boolean monitor=true;
    	public JButton mtoggle=null;

    	public ButtonPanel()
    	{
    		mtoggle=new JButton("Turn screen off");
    		mtoggle.setEnabled(false);
    		mtoggle.addActionListener( new ActionListener(){ public void actionPerformed(ActionEvent e) {
    			if(monitor)
    			{
    				monitor=false;
    				mtoggle.setText("Turn screen on");
    				writer.println("monitor off");
    			}
    			else
    			{
    				monitor=true;
    				mtoggle.setText("Turn screen off");
    				writer.println("monitor on");
    			}
    			gp.requestFocus();
    		} } );
    		add(mtoggle);
    	}
    }
}
