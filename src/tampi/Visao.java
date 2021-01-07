package tampi;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Visao {

    // Referência ao sketch
    PApplet app;

    /**
	 * Construtor
	 * 
	 * @param theParent o PApplet responsável pelo sketch
	 */
	public Visao(PApplet theParent) {
		app = theParent;
	}

    /**
	 * Retorna um array com os blobs de uma cor específica encontrados na tela
	 * 
	 * @param c a cor que se deseja buscar
     * @return Blob[]
	 */
    public Blob[] busca (int c) {

        // Array dinâmico para salvar os blobs
		ArrayList<Blob> blobAux = new ArrayList<Blob>();

		// Procura por todo o campo
		for (int x = 0; x < app.width; x++) {
		    for (int y = 0; y < app.height; y++ ) {
	
                // Pega a cor do pixel
                int currentColor = app.get(x, y);
                
                // Compara as cores
                if (msmCor(currentColor, c)) {
                    // Verifica se algum blob já foi encontrado
                    if (blobAux.size() > 0) {
                        // Booleana que testa se encontrou um blob no campo da cor c
                        boolean found = false;
                     
                        // Itera por todos os blobs já encontrados
                        for (Blob b : blobAux) {
                            // Verifica se esse pixel está perto o suficiente do blob avaliado e se possui a mesma cor
                            if (b.isNear(x, y) && b.cor == c) {
                                // Adiciona o pixel ao blob
                                b.add(x, y);
                                found = true;
                                break;
                            }
                        }

                        // Se chegamos até aqui, o pixel faz parte de um novo blob
                        if (!found) {
                            Blob b = new Blob(x, y, c);
                            blobAux.add(b);
                        }
                    }
                    // Este é o primeiro blob encontrado
                    else {
                        Blob b = new Blob(x, y, c);
                        blobAux.add(b);
                    }
                }
		    }
	    }
		
		// Elimina os blobs que não possuam um número minimo de pixels		
		for (int i = 0; i < blobAux.size(); i++) {
            if (blobAux.get(i).numPixels < 15)
            blobAux.remove(i);
		}
        
        // Desenha as dimensões máximas dos bobs
		//  for (int i = 0; i < blobAux.size(); i++) {
		//    line(blobAux.get(i).minx, blobAux.get(i).miny, blobAux.get(i).maxx, blobAux.get(i).miny);
		//    line(blobAux.get(i).maxx, blobAux.get(i).miny, blobAux.get(i).maxx, blobAux.get(i).maxy);
		//    line(blobAux.get(i).maxx, blobAux.get(i).maxy, blobAux.get(i).minx, blobAux.get(i).maxy);
		//    line(blobAux.get(i).minx, blobAux.get(i).maxy, blobAux.get(i).minx, blobAux.get(i).miny);
		//  }
    
        // Array de tamanho fixo com os blobs encontrados
		Blob[] blobs = new Blob[blobAux.size()];
		for (int i = 0; i < blobs.length; i++) blobs[i] = blobAux.get(i).clone();
		return blobs;
    }

    /**
	 * Retorna um array os vetores do campo potencial
	 * 
	 * @param alvo coordenada do alvo
     * @param obstaculos array com as coordenadas dos obstaculos
     * @param intensidade fator de intensidade do alvo
     * @param resolucao tamanho de cada célula do campo
     * @param debug mostra as setas na tela
     * @return PVector[][]
	 */
    public PVector[][] campoPotencial(PVector alvo, PVector[] obstaculos, float intensidade, int resolucao, boolean debug) {
        // Calcula a quantidade de células em cada eixo
        int qx = app.width / resolucao;
        int qy = app.height / resolucao;

        PVector[][] campo = new PVector[qx][qy];

        // Obstaculos virtuais nas bordas
        PVector[] obsVirtuais = new PVector[2 * qx + 2 * qy];
        for(int i=0; i<qx; i++) obsVirtuais[i] = new PVector(i * resolucao, 0);
        for(int i=0; i<qx; i++) obsVirtuais[i + qx] = new PVector(i * resolucao, app.height);
        for(int i=0; i<qy; i++) obsVirtuais[i + 2*qx] = new PVector(0, i * resolucao);
        for(int i=0; i<qy; i++) obsVirtuais[i + 2*qx + qy] = new PVector(app.width, i * resolucao);        

        for(int i=0; i<qx; i++) {
            for(int j=0; j<qy; j++) {
                // Coordenadas do centro da célula
                float xp = i*resolucao+resolucao/2;
                float yp = j*resolucao+resolucao/2;
                // Calcula o angulo do alvo
                float ang = (float) Math.PI/2 - atan2(alvo.y - yp, alvo.x - xp);
                // Vetores
                float ampX = intensidade * cos(ang) / distSq(xp, yp, alvo.x, alvo.y);
                float ampY = intensidade * sin(ang) / distSq(xp, yp, alvo.x, alvo.y);
                // Calcula o campo dos obstáculos
                for(PVector obs : obstaculos) {
                    // Calcula o angulo do obstáculo
                    ang = (float) - Math.PI/2 - atan2(obs.y - yp, obs.x - xp);
                    // Calcula os vetores x e y
                    ampX += cos(ang) / distSq(xp, yp, obs.x, obs.y);
                    ampY += sin(ang) / distSq(xp, yp, obs.x, obs.y);
                }
                for(PVector obs : obsVirtuais){
                    // Calcula o angulo do obstáculo
                    ang = (float) - Math.PI/2 - atan2(obs.y - yp, obs.x - xp);
                    // Calcula os vetores x e y
                    ampX += cos(ang) / distSq(xp, yp, obs.x, obs.y) / 10;
                    ampY += sin(ang) / distSq(xp, yp, obs.x, obs.y) / 10;
                }
                // A amplitude total é a soma vetorial do alvo com todos os obstáculos
                float amp = (float) Math.sqrt(ampX*ampX + ampY*ampY);
                ang = atan2(ampY, ampX);
                campo[i][j] = new PVector(amp * cos(ang), amp * sin(ang));
            }
        }

        if(!debug) return campo;

        // Desenha as setas
        for(int i=0; i<qx; i++) {
            for(int j=0; j<qy; j++) {
                float xp = i*resolucao+resolucao/2;
                float yp = j*resolucao+resolucao/2;
                float amp = (float) Math.sqrt(campo[i][j].x*campo[i][j].x + campo[i][j].y*campo[i][j].y);
                float ang = atan2(campo[i][j].y, campo[i][j].x);
                seta(xp, yp, amp, ang, resolucao);
            }
        }

        return campo;
    }

    /**
     * Desenha uma seta na tela
     * 
     * @param x componente no eixo x
     * @param y componente no eixo y
     * @param amp amplitude (tamanho) da seta
     * @param ang ângulo da seta em radianos
     * @param res lado de uma célula em pixels
     */
    void seta(float x, float y, float amp, float ang, float res) {
        app.stroke(255);
        app.strokeWeight(1);
        float nAmp = PApplet.map(amp, 0f, 1/(res*res), res / 10, res / 2);
        if(nAmp > res/2) nAmp = res/2;
        float xp = x + nAmp * sin(ang);
        float yp = y + nAmp * cos(ang);
        app.line(xp, yp, x - nAmp * sin(ang), y - nAmp * cos(ang));
        app.line(xp, yp, xp - nAmp * sin((float) Math.PI/6 + ang), yp - nAmp * cos((float) Math.PI/6 + ang));
        app.line(xp, yp, xp - nAmp * sin((float)-Math.PI/6 + ang), yp - nAmp * cos((float)-Math.PI/6 + ang));
    }

    /**
	 * Retorna um array com a sequência de células que seguem o campo
	 * 
	 * @param origem coordenada da origem
     * @param campo array com os vetores do campo
     * @param debug mostra as setas na tela
     * @return PVector[][]
	 */
    public int[][] trajeto(PVector origem, PVector[][] campo, boolean debug) {
        // calcula a resolução
        int res = app.width / campo.length;

        // origem aproximada
        int x = (int) origem.x / res;
        int y = (int) origem.y / res;

        // inicializa o array trajeto
        ArrayList<int[]> trajetoAux = new ArrayList<int[]>();

        // a primeira coordenada do trajeto é a própria origem aproximada
        int[] aux = {x, y};
        trajetoAux.add(aux);

        boolean existe = false;

        // Itera enquanto estiver dentro do campo até o tamanho do array
        while(x < campo.length && x >= 0 && y < campo[0].length && y >= 0 && !existe) {

            float ang = atan2(campo[x][y].y, campo[x][y].x);

            // encontra a seta mais próxima
            x = (int) (x * res + res/2 + res * cos(ang - (float) Math.PI/2)) / res;
            y = (int) (y * res + res/2 + res * sin(ang + (float) Math.PI/2)) / res;

            // verifica se é um ponto repetido
            
            for(int[] i : trajetoAux) {
                if(x == i[0] && y == i[1]) existe = true;
            }
            if(!existe) {
                // adiciona a coordenada ao array trajeto
                aux = new int[2];
                aux[0] = x;
                aux[1] = y;
                trajetoAux.add(aux);
            }
        }

        // Array de tamanho fixo com as células do trajeto
        int[][] trajeto = new int[trajetoAux.size()][2];
        for(int i=0; i<trajeto.length; i++) {
            trajeto[i] = trajetoAux.get(i);
        }
        
        if(!debug) return trajeto;

        // Desenha o trajeto
        for(int i=0; i<trajeto.length - 1; i++) {
            float xa = trajeto[i][0] * res + res/2;
            float ya = trajeto[i][1] * res + res/2;
            float xb = trajeto[i + 1][0] * res + res/2;
            float yb = trajeto[i + 1][1] * res + res/2;
            app.fill(255);
            app.line(xa, ya, xb, yb);
        }
        
        return trajeto;
    } 
    
    // private static int red(int color) {
	// 	int mask = 255<<16; //16711680
	// 	return (color & mask)>>16;
	// }
	// private static int green(int color) {
	// 	int mask = 255<<8; //65280 
	// 	return (color & mask)>>8;
	// }
	// private static int blue(int color) {
	// 	int mask = 255;
	// 	return (color & mask);
	// }

	/**
     * Verifica se duas cores são semelhantes comparando suas componentes
     * @param c1 primeira cor
     * @param c2 segunda cor
     * 
     * @return boolean
     */
	private boolean msmCor(int c1, int c2) {
		// limite de diferenca de cada componente
		int lim = 5;
		// float brightnessc1 = app.brightness(c1);
		// float brightnessc2 = app.brightness(c2);
        if (Math.abs(app.red(c1) - app.red(c2)) < lim && 
            Math.abs(app.green(c1) - app.green(c2)) < lim && 
            Math.abs(app.blue(c1) - app.blue(c2)) < lim) return true;
		else return false;
      }
    
    /**
     * Retorna a distância euclidiana ao quadrado entre dois pontos
     * 
     * @param x1 componente x do primeiro ponto
     * @param y1 componente y do primeiro ponto
     * @param x2 componente x do segundo ponto
     * @param y2 componente y do segundo ponto
     * 
     * @return float
     */
    private float distSq(float x1, float y1, float x2, float y2) {
        return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
      }

    private float sin(float ang) {
        return (float) Math.sin(ang);
    }
    private float cos(float ang) {
        return (float) Math.cos(ang);
    }
    private float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }
    
}
