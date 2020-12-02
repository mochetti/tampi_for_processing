package sala49.tampi;


import processing.core.*;
import processing.event.*;
import websockets.WebsocketClient;
// import websockets.WebsocketClientEvents;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class Tampi {
	
	// myParent is a reference to the parent sketch
	PApplet myParent;

	// Create websocket client object
	WebsocketClient client;
	
	public final static String VERSION = "##library.prettyVersion##";
	

	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the Library.
	 * 
	 * @example Hello
	 * @param theParent the parent PApplet
	 */
	public Tampi(PApplet theParent) {
		myParent = theParent;
		welcome();
	}
	
	
	private void welcome() {
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}

	public void init() {
		// Connect using default address
		client = new WebsocketClient(myParent, "ws://192.168.4.1:81");
		client.sendMessage("message");
	}

	public void andar() {
		client.sendMessage("a");
	}

	public void andar(float qnt) {
		client.sendMessage("a" + qnt + "e");
	}

	public void girar() {
		
	}

	public void buzina() {

	}

	public float ldrEsq() {
		return -5f;
	}

	public void trajeto(PVector eu, PVector alvo) {
		// le a camera
		// pid
		// envia o comando
	}
	
	
	public void mouseEvent(MouseEvent event) {
		int x = event.getX();
		int y = event.getY();

		String s = "";
	  
		switch (event.getAction()) {
		  case MouseEvent.PRESS:
			// do something for the mouse being pressed
			s = x + "	" + y;
			break;
		  case MouseEvent.RELEASE:
			// do something for mouse released
			break;
		  case MouseEvent.CLICK:
			// do something for mouse clicked
			break;
		  case MouseEvent.DRAG:
			// do something for mouse dragged
			break;
		  case MouseEvent.MOVE:
			// do something for mouse moved
			break;
		}
		print(s);
	  }

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

}

