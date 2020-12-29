package sala49.tampi;


import processing.core.PApplet;
import websockets.WebsocketClient;

public class Tampi {
	
	// Referência ao sketch
	PApplet app;

	// Create websocket client object
	WebsocketClient client;

	// Distancia máxima lida pelo ultrassônico
	public int distanciaMax = 40;

	// Volume máximo lido pelo microfone
	public int volumeMax = 1023;

	// Raio da roda em metros
	public float raioRoda = 0.01f;

	// Ângulo do robo em radianos
	public float ang = 0;
	
	public final static String VERSION = "##library.prettyVersion##";
	
	/**
	 * Construtor
	 * 
	 * @param theParent o PApplet responsável
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

	public void andar() {
		client.sendMessage("a");
	}

	public void andar(float qnt) {
		client.sendMessage("a" + qnt + "e");
	}

	/**
	 * Rotaciona o Tampi no próprio eixo indefinidamente
	 * 
	 * @param sentido sentido de rotação. true = horário e false = antihorário
	 */
	public void girar(boolean sentido) {

	}

	/**
	 * Rotaciona o Tampi no próprio eixo ang graus
	 * 
	 * @param ang angulo de giro. ang > 0 = horário e ang < 0 = antihorário
	 */
	public void girar(float ang) {
		
	}

	
	public void buzina() {

	}

	/**
	 * Retorna a distância lida pelo ultrassônico em centímetros.
	 * 
	 * @return float
	 */
	public float distancia() {
		return (float) Math.random() * 40;
	}

	/**
	 * Retorna o volume lido pelo microfone.
	 * 
	 * @return float
	 */
	public float microfone() {
		return (float) Math.random() * 1023;
	}

	/**
	 * Retorna a intensidade lida pelo ldr esquerdo.
	 * 
	 * @return int
	 */
	public int ldrEsq() {
		return (int) (Math.random() * 1023);
	}

	/**
	 * Retorna a intensidade lida pelo ldr direito.
	 * 
	 * @return int
	 */
	public int ldrDir() {
		return (int) (Math.random() * 1023);
	}

	/**
	 * Retorna a intensidade lida pelo IR esquerdo.
	 * 
	 * @return int
	 */
	public int irEsq() {
		return (int) (Math.random() * 1023);
	}

	/**
	 * Retorna a intensidade lida pelo IR direito.
	 * 
	 * @return int
	 */
	public int irDir() {
		return (int) (Math.random() * 1023);
	}

	/**
	 * Retorna a velocidade da roda esquerda em m/s.
	 * 
	 * @return float
	 */
	public float velEsq() {
		return (float) (Math.random() * 1023);
	}

	/**
	 * Retorna a velocidade da roda direita em m/s.
	 * 
	 * @return float
	 */
	public float velDir() {
		return (float) (Math.random() * 1023);
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