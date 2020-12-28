/*
  Exemplo com a função campoPotencial();
 Gera um campo de vetores baseado nas coordenadas do alvo e dos obstáculos
 
 Tampi for Processing
 */

import sala49.tampi.*;
Visao visao;

PVector alvo, eu;
PVector[] obstaculos;
int x, y, s = 30;
PVector[][] campo;

void setup() {
  size(400, 400);
  alvo = new PVector(width/2, height/2);
  obstaculos = new PVector[3];
  for (int i=0; i<obstaculos.length; i++) obstaculos[i] = new PVector(random(width), random(height));
  eu = new PVector();
  x = width / s;
  y = height / s;

  visao = new Visao(this);
}

void draw() {
  background(0);
  atualiza();
  campo = visao.campoPotencial(alvo, obstaculos, mouseX*s/width, s);
  desenha();
}

void atualiza() {
  // origem tem as coordenadas do mouse
  eu.x = mouseX;
  eu.y = mouseY;
}

//void trajeto() {
//  // origem
//  int xa = int(eu.x) / s;
//  int ya = int(eu.y) / s;
//  int xb, yb;

//  // destino aproximado
//  int dx = int(destino.x) / s;
//  int dy = int(destino.y) / s;

//  while(xa != dx || ya != dy) {
//    // encontra a seta mais próxima
//    fill(255);
//    ellipse(xa*s + s/2, ya*s + s/2, 5, 5);

//    xb = int(xa*s + s/2 + s*cos(campo[xa][ya].ang - PI/2)) / s;
//    if(xb >= x) xb = x-1;
//    if(xb < 0) xb = 0;
//    yb = int(ya*s + s/2 + s*sin(campo[xa][ya].ang + PI/2)) / s;
//    if(yb >= y) yb = y-1;
//    if(yb < 0) yb = 0;

//    line(xa*s + s/2, ya*s + s/2, xb*s + s/2, yb*s + s/2);
//    xa = xb;
//    ya = yb;

//    //println(xa, ya);
//  }

//}

void desenha() {
  // eu
  fill(255);
  ellipse(eu.x, eu.y, 10, 10);

  // obstáculos
  fill(0, 255, 0);
  for (PVector obs : obstaculos) ellipse(obs.x, obs.y, 10, 10);

  // alvo
  fill(255, 0, 0);
  ellipse(alvo.x, alvo.y, 10, 10);

  for (int i=0; i<x; i++) {
    for (int j=0; j<y; j++) {
      float xp = i*s+s/2;
      float yp = j*s+s/2;
      float amp = sqrt(campo[i][j].x*campo[i][j].x + campo[i][j].y*campo[i][j].y);
      float ang = atan2(campo[i][j].y, campo[i][j].x);
      Seta s = new Seta(amp, ang);
      s.mostra(xp, yp);
    }
  }
}
