package sala49.tampi;


import processing.core.*;
import websockets.WebsocketClient;

public class Tampi {
	
	// myParent is a reference to the parent sketch
	PApplet app;

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
		app = theParent;
		System.out.println("##library.name## ##library.prettyVersion## by ##author##");
	}

	public void init() {
		// Connect using default address
		client = new WebsocketClient(app, "ws://192.168.4.1:81");
		// client.sendMessage("message");
	}

	public PVector busca(int cor) {
		boolean found = false;
		Blob b = new Blob();
		for(int x=0; x<app.width; x++) {
			for(int y=0; y<app.height; y++){
			int p = app.pixels[y*app.width + x];
			if(app.red(p) - app.red(cor) < 20 && app.green(p) - app.green(cor) < 20 && app.blue(p) - app.blue(cor) < 20){
				if(found && b.isNear(x, y)) {
					b.add(x, y);
				}
				else {
					b = new Blob(x, y);
					found = true;
				}
			}
			}
		}
		return b.center();
	}

	// private int red(int color) {
	// 	int mask = 255<<16; //16711680
	// 	return (color & mask)>>16;
	// }
	// private int green(int color) {
	// 	int mask = 255<<8; //65280 
	// 	return (color & mask)>>8;
	// }
	// private int blue(int color) {
	// 	int mask = 255;
	// 	return (color & mask);
	// }

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


	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

}