package sala49.tampi;

import java.util.ArrayList;

import processing.core.PApplet;
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

	public Blob[] busca (int c) {
		ArrayList<Blob> blobAux = new ArrayList<Blob>();
		//Variavel de quantidade de blobs encontrados da cor que está sendo buscada
		// int encontramos = 0;
		// Procura por todo o campo
		for (int x = 0; x < app.width; x++) {
		  for (int y = 0; y < app.height; y++ ) {
	
		  // int loc = x + y * width;
		  // What is current color
		  int currentColor = app.get(x, y);
		
		  // Compara as cores
		  /*
			caso a cor do pixel que está sendo avaliado que passa pelo filtroCor seja uma cor real e
		   a cor do pixel que está sendo avaliado seja a mesma cor que está sendo procurada. As propriedades da cor que está sendo procurada
		   está dentro da array cores na posição "c"
		   */
		  if (msmCor(currentColor, c)) {
			// Verifica se algum elemento dessa cor já foi encontrado aqui perto
			//Caso seja, veja se o tamanho da array blobs é maior que 0 (já contém algum blob salvo).
			if (blobAux.size() > 0) {
			//Booleana que testa se encontrou um blob no campo da cor c
			boolean found = false;
		
			/*
			  Checo dentro dos blobs já existentes se o blob tá perto da posição x, y e se tem a mesma cor. Se tiver perto e tiver a mesma cor, adiciona como mesmo elemento de blob
			 */
			for (Blob b : blobAux) {
			  /*
			  Caso o ponto x, y (loc)/pixel que está sendo avaliado no momento esteja próximo do blob em questão
			   e a cor do blob é igual à cor que está sendo procurada, a função add() do elemento blob define novos limites
			   (em x e y) tanto max quanto minimos para sabermos se aquele ponto faz parte do todo ou não
			   */
			  if (b.isNear(x, y) && b.cor == c) {
			  b.add(x, y);
			  //a variável found atualiza para true, assim sabemos que encontramos um novo ponto que compoem o todo
			  found = true;
			  break;
			  }
			}
			/*
			  Caso a variável found retorne falso (o ponto nao compoem o todo próximo)
			 ele entende que é o primeiro ponto que contem um novo blob na região. Gera um novo blob b na posição do ponto
			 com a cor c em questão. Atualiza também o valor do encontramos++ para sabermos que encontramos um novo blob.
			 Na próxima iterada provavelmente iremos entrar no bloco de cima, buscanndo os pontos próximos
			 */
		
			//Caso seja uma cor, mas não estava próximo de nenhum blob existente de mesma cor, é um novo elemento de blob
			if (!found) {
			  // É o pioneiro na região
			  //println("VISÃO: Novo blob encontrado");
			  Blob b = new Blob(x, y, c);
			  blobAux.add(b);
			//   encontramos++;
			  //ellipse(x, y, 30, 30);
			}
			}
			// Este é o primeiro blob
			else {
			// Primeiro blob no campo todo
			//faz a mesma coisa que os outros. Gera novo elemento de blob, na posição x, y, com a cor c em questão e add na array
			//println("VISÃO: Primeiro blob encontrado");
			Blob b = new Blob(x, y, c);
			blobAux.add(b);
			// encontramos++;
			//fill(255);
			//point(x, y);
			}
		
			// Debug
			//stroke(255);
			//strokeWeight(1);
			//point(x, y);
		  }
		  }
		}
		
		// Confere se os blobs tem um numero minimo de pixels
		//Caso tenha algum com menos q o numero minimo de pixel, foi impostor e subtrai um nos blobs encontrados
		
		for (int i = 0; i < blobAux.size(); i++) {
		  //Elimina impostores
		  if (blobAux.get(i).numPixels < 15)
		  blobAux.remove(i);
		  //println(blobAux.get(i).cor);
		}
		
		
		//  for (int i = 0; i < blobAux.size(); i++) {
		//    line(blobAux.get(i).minx, blobAux.get(i).miny, blobAux.get(i).maxx, blobAux.get(i).miny);
		//    line(blobAux.get(i).maxx, blobAux.get(i).miny, blobAux.get(i).maxx, blobAux.get(i).maxy);
		//    line(blobAux.get(i).maxx, blobAux.get(i).maxy, blobAux.get(i).minx, blobAux.get(i).maxy);
		//    line(blobAux.get(i).minx, blobAux.get(i).maxy, blobAux.get(i).minx, blobAux.get(i).miny);
		//  }
	
		Blob[] blobs = new Blob[blobAux.size()];
		for (int i = 0; i < blobs.length; i++) blobs[i] = blobAux.get(i).clone();
		return blobs;
		
	}

	private static int red(int color) {
		int mask = 255<<16; //16711680
		return (color & mask)>>16;
	}
	private static int green(int color) {
		int mask = 255<<8; //65280 
		return (color & mask)>>8;
	}
	private static int blue(int color) {
		int mask = 255;
		return (color & mask);
	}

	// Compara duas cores por intervalos em cada componente
	private boolean msmCor(int c1, int c2) {
		// limite de diferenca de cor
		int lim = 5;
		// float brightnessc1 = app.brightness(c1);
		// float brightnessc2 = app.brightness(c2);
		// talvez seja necessario criar um limite diferente p cada componente
		if (Math.abs(red(c1) - red(c2)) < lim && Math.abs(green(c1) - green(c2)) < lim && Math.abs(blue(c1) - blue(c2)) < lim) return true;
		else return false;
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


	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

}