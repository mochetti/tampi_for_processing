/*
  Exemplo com as funções ldrEsq() e ldrDir();
  
  Tampi for Processing
*/

import tampi.*;

Tampi t;

void setup() {
  size(600,600);  
  t = new Tampi(this);
  background(0);
}

void draw() {
  // Recebe os valores do Tampi
  float ldrEsq = t.ldrEsq();
  float ldrDir = t.ldrDir();
  println("esq = " + ldrEsq + "  dir = " + ldrDir);
  
  // Ajusta os valores para o intervalo de brilho
  ldrEsq = map(ldrEsq, 0, 1023, 0, 255);
  ldrDir = map(ldrDir, 0, 1023, 0, 255);
  
  // Desenha os círculos
  fill(ldrEsq);
  ellipse(width/4, height/2, 200, 200);
  fill(ldrDir);
  ellipse(3 * width/4, height/2, 200, 200);
}
