package tampi;

import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PVector;
import websockets.WebsocketClient;

public class Tampi {

	// Referência ao sketch
	PApplet app;

	// Create websocket client object
	WebsocketClient ws;

	// Armazena leituras dos sensores
	int[] sensores = new int[35];
	/*
	 * 0 - bat 1 - mic (volume) 2 - mic (frequencia) em Hz 3 - ldr esquerdo 4 - ldr
	 * direito 5 - encoder esquerdo 6 - encoder direito 7 - IR esquerdo 8 - IR
	 * direito 9 : 16 - tampinhas topo 17 : 22 - tampinhas esquerda 23 : 28 -
	 * tampinhas direita 29 : 32 - analog extras 33 - velocidade esquerda em mm/s 34
	 * - velocidade direita em mm/s 35 - distancia em mm
	 */

	// Distancia máxima lida pelo ultrassônico
	public int distanciaMax = 40;

	// Raio da roda em metros
	public float raioRoda = 0.01f;

	//
	public int velocidade = 100;

	// Ângulo do robo em radianos
	public float ang = 0;

	// Posição do Tampi em metros
	PVector pos = new PVector(0, 0);

	// Tempo para evitar sobrecargar no websocket
	public long ultimaAtualizacao = 0;

	// Controla se o sketch está conectado
	public boolean conectado = false;

	// Controla se estamos esperando uma resposta do Tampi
	public boolean espera = false;

	public final static String VERSION = "##library.prettyVersion##";

	/**
	 * Construtor
	 * 
	 * @param theParent o PApplet responsável
	 */
	public Tampi(PApplet theParent) {
		app = theParent;
		System.out.println("##library.name## ##library.prettyVersion## by ##author.name##");
		System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
	}

	public void init() {
		// Conecta ao servidor no endereço padrão
		init(true);
	}

	public void init(boolean wait) {
		ws = new WebsocketClient(app, this, "ws://192.168.4.1:81", wait);
		espera = true;
		app.delay(50);
	}

	// Callback para os eventos do websocket
	public void webSocketEvent(String msg) {

		System.out.println("Recebido: " + msg);

		// Primeira mensagem do servidor
		if (msg.startsWith("oi")) {
			conectado = true;
			espera = false;
			// Se identifica como PC
			ws.sendMessage("cp");
		}

		// Verifica se o tempo de espera acabou
		else if (msg.startsWith("e")) {
			espera = false;
		}

		// Verifica se estamos recebendo as leituras dos sensores
		else if (msg.startsWith("s")) {
			String valores = msg.substring(1, msg.length());
			String[] leituras = valores.split(",");
			for (int i = 0; i < leituras.length; i++) {
				try {
					sensores[i] = Integer.parseInt(leituras[i]);
				} catch (NumberFormatException e) {
					sensores[i] = -1;
				}
			}
		}

		else {
			System.out.println("Recebido: " + msg);
		}
	}

	public void webSocketOnError(Throwable error) {
		switch (error.getMessage()) {
			case "Connect Timeout":
				System.out.println("Erro: Não foi possível se conectar ao Tampi");
				break;

			default:
				System.out.println(error.getMessage());
				break;
		}
	}

	/**
	 * Comando para atualizar os valores dos sensores
	 *
	 */
	public void atualiza() {
		if (app.millis() - ultimaAtualizacao > 100) {
			ultimaAtualizacao = app.millis();
			enviar("s");
		}
	}

	public void enviar(String msg) {
		while (espera) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		espera = true;
		ws.sendMessage(msg);
	}

	/**
	 * Anda o Tampi em linha reta indefinidamente
	 * 
	 * @param sentido sentido de movimento. true = frente e false = trás
	 */
	public void andar(boolean sentido) {
		if(sentido) enviar("m" + String.valueOf(velocidade) + ',' + String.valueOf(velocidade));
		else enviar("m" + String.valueOf(-velocidade) + ',' + String.valueOf(-velocidade));
	}

	/**
	 * Anda o Tampi em linha reta por uma distância específica
	 * 
	 * @param distancia distância em mm
	 */
	public void andar(int distancia) {
		enviar("a" + String.valueOf(distancia));
	}

	/**
	 * Rotaciona o Tampi no próprio eixo indefinidamente
	 * 
	 * @param sentido sentido de rotação. true = horário e false = antihorário
	 */
	public void girar(boolean sentido) {
		if(sentido) enviar("m" + String.valueOf(velocidade) + ',' + String.valueOf(-velocidade));
		else enviar("m" + String.valueOf(-velocidade) + ',' + String.valueOf(velocidade));
	}

	/**
	 * Rotaciona o Tampi no próprio eixo por ângulo específico
	 * 
	 * @param angulo angulo de giro. ang > 0 = horário e ang < 0 = antihorário
	 */
	public void girar(int angulo) {
		enviar("g" + angulo);
	}

	/**
	 * Reproduz uma frequência na buzina
	 * 
	 * @param freq frequencia da onda sonora em Hz
	 */
	public void buzina(int freq) {
		enviar("");
	}

	/**
	 * Acende ou apaga os faróis do Tampi
	 * 
	 * @param aceso true -> acende; false -> apaga
	 */
	public void farol(boolean aceso) {
		if(aceso) enviar("f0");
		else enviar("f1");
	}

	/**
	 * Retorna a tensão da bateria entre 0:1023
	 * 
	 * @return int
	 */
	public int bateria() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[0];
	}
	public int bateria(boolean atualiza) {
		if(!atualiza) return sensores[0];
		else return bateria();
	}

	/**
	 * Retorna o volume lido pelo microfone.
	 * 
	 * @return int
	 */
	public int volMic() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[1];
	}
	public int volMic(boolean atualiza) {
		if(!atualiza) return sensores[1];
		else return volMic();
	}

	/**
	 * Retorna a frequência em Hz lida pelo microfone.
	 * 
	 * @return int
	 */
	public int freqMic() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[2];
	}
	public int freqMic(boolean atualiza) {
		if(!atualiza) return sensores[2];
		else return freqMic();
	}

	/**
	 * Retorna a intensidade lida pelo ldr esquerdo.
	 * 
	 * @return int
	 */
	public int ldrEsq() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[3];
	}
	public int ldrEsq(boolean atualiza) {
		if(!atualiza) return sensores[3];
		else return ldrEsq();
	}

	/**
	 * Retorna a intensidade lida pelo ldr direito.
	 * 
	 * @return int
	 */
	public int ldrDir() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[4];
	}
	public int ldrDir(boolean atualiza) {
		if(!atualiza) return sensores[4];
		else return ldrDir();
	}

	/**
	 * Retorna a intensidade lida pelo encoder esquerdo.
	 * 
	 * @return int
	 */
	public int encoderEsq() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[5];
	}
	public int encoderEsq(boolean atualiza) {
		if(!atualiza) return sensores[5];
		else return encoderEsq();
	}

	/**
	 * Retorna a intensidade lida pelo encoder direito.
	 * 
	 * @return int
	 */
	public int encoderDir() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[6];
	}
	public int encoderDir(boolean atualiza) {
		if(!atualiza) return sensores[6];
		else return encoderDir();
	}

	/**
	 * Retorna a intensidade lida pelo IR esquerdo.
	 * 
	 * @return int
	 */
	public int irEsq() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[7];
	}
	public int irEsq(boolean atualiza) {
		if(!atualiza) return sensores[7];
		else return irEsq();
	}

	/**
	 * Retorna a intensidade lida pelo IR direito.
	 * 
	 * @return int
	 */
	public int irDir() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[8];
	}
	public int irDir(boolean atualiza) {
		if(!atualiza) return sensores[8];
		else return irDir();
	}

	/**
	 * Retorna as leituras das tampinhas do topo
	 * 
	 * @return int[]
	 */
	public int[] tampinhasTopo() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		int[] valores = Arrays.copyOfRange(sensores, 9, 16 + 1);
		return valores;
	}
	public int[] tampinhasTopo(boolean atualiza) {
		if(!atualiza) {
			int[] valores = Arrays.copyOfRange(sensores, 9, 16 + 1);
			return valores;
		}
		else return tampinhasTopo();
	}

	/**
	 * Retorna as leituras das tampinhas da esquerda
	 * 
	 * @return int[]
	 */
	public int[] tampinhasEsq() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		int[] valores = Arrays.copyOfRange(sensores, 17, 22 + 1);
		return valores;
	}
	public int[] tampinhasEsq(boolean atualiza) {
		if(!atualiza) {
			int[] valores = Arrays.copyOfRange(sensores, 17, 22 + 1);
			return valores;
		}
		else return tampinhasEsq();
	}

	/**
	 * Retorna as leituras das tampinhas da direita
	 * 
	 * @return int[]
	 */
	public int[] tampinhasDir() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		int[] valores = Arrays.copyOfRange(sensores, 23, 28 + 1);
		return valores;
	}
	public int[] tampinhasDir(boolean atualiza) {
		if(!atualiza) {
			int[] valores = Arrays.copyOfRange(sensores, 23, 28 + 1);
			return valores;
		}
		else return tampinhasDir();
	}

	/**
	 * Retorna a leitura do pino analógico extra 1.
	 * 
	 * @return int
	 */
	public int aExtra1() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[29];
	}
	public int aExtra1(boolean atualiza) {
		if(!atualiza) return sensores[29];
		else return aExtra1();
	}

	/**
	 * Retorna a leitura do pino analógico extra 2.
	 * 
	 * @return int
	 */
	public int aExtra2() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[30];
	}
	public int aExtra2(boolean atualiza) {
		if(!atualiza) return sensores[30];
		else return aExtra2();
	}

	/**
	 * Retorna a leitura do pino analógico extra 3.
	 * 
	 * @return int
	 */
	public int aExtra3() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[31];
	}
	public int aExtra3(boolean atualiza) {
		if(!atualiza) return sensores[31];
		else return aExtra3();
	}

	/**
	 * Retorna a leitura do pino analógico extra 4.
	 * 
	 * @return int
	 */
	public int aExtra4() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[32];
	}
	public int aExtra4(boolean atualiza) {
		if(!atualiza) return sensores[32];
		else return aExtra4();
	}

	/**
	 * Retorna a velocidade da roda esquerda em mm/s.
	 * 
	 * @return int
	 */
	public int velEsq() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[33];
	}
	public int velEsq(boolean atualiza) {
		if(!atualiza) return sensores[33];
		else return velEsq();
	}

	/**
	 * Retorna a velocidade da roda direita em mm/s.
	 * 
	 * @return int
	 */
	public int velDir() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[34];
	}
	public int velDir(boolean atualiza) {
		if(!atualiza) return sensores[34];
		else return velDir();
	}

	/**
	 * Retorna a distância lida pelo ultrassônico em milímetros.
	 * 
	 * @return int
	 */
	public int distancia() {
		if(app.millis() - ultimaAtualizacao > 300) atualiza();
		return sensores[35];
	}
	public int distancia(boolean atualiza) {
		if(!atualiza) return sensores[35];
		else return distancia();
	}

	/**
	 * return the version of the Library.
	 * 
	 * @return String
	 */
	public static String version() {
		return VERSION;
	}

	/**
	 * Desenha o Tampi na tela
	 * 
	 */
	public void desenha() {


	}

}