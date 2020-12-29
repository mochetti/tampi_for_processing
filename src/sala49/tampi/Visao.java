package sala49.tampi;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class Visao {

    // Referência ao sketch
    PApplet app;

    /**
	 * Construtor
	 * 
	 * @param theParent o PApplet responsável
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
	 * Retorna um array com a sequencia de células ideal
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

        // destino aproximado
        // int Dx = (int) destino.x / res;
        // int Dy = (int) destino.y / res;

        int[][] trajeto = new int[30][2];
        trajeto[0][0] = x;
        trajeto[0][1] = y;

        int passo = 1;

        while(x < campo.length && x >= 0 && y < campo[0].length && y >= 0 && passo < 30) {

            float ang = atan2(campo[x][y].y, campo[x][y].x);

            // encontra a seta mais próxima
            x = (int) (x * res + res/2 + res * cos(ang - (float) Math.PI/2)) / res;
            // if(xb >= x) xb = x-1;
            // if(xb < 0) xb = 0;
            y = (int) (y * res + res/2 + res * sin(ang + (float) Math.PI/2)) / res;
            // if(yb >= y) yb = y-1;
            // if(yb < 0) yb = 0;
            

            trajeto[passo][0] = x;
            trajeto[passo][1] = y;
            passo++;

            //println(xa, ya);
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

	// Compara duas cores por intervalos em cada componente
	private boolean msmCor(int c1, int c2) {
		// limite de diferenca de cor
		int lim = 5;
		// float brightnessc1 = app.brightness(c1);
		// float brightnessc2 = app.brightness(c2);
		// talvez seja necessario criar um limite diferente p cada componente
        if (Math.abs(app.red(c1) - app.red(c2)) < lim && 
            Math.abs(app.green(c1) - app.green(c2)) < lim && 
            Math.abs(app.blue(c1) - app.blue(c2)) < lim) return true;
		else return false;
      }
      
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
