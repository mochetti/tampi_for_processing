/*
  Desenha na tela o trajeto percorrido pelo Tampi
  
  Tampi for Processing
*/

import tampi.*;

Tampi t;
PVector pos;
double tempo;
float ang = 0;
float raio = 0.1;
float L = 0.1;
float teste = 0;

void setup() {
  size(600,600);  
  t = new Tampi(this);
  pos = new PVector(0, 0);
  
  tempo = millis();
}

void draw() {
  //background(255);
  teste += 0.1;
  translate(width/2, height/2);
  // Recebe as velocidades de cada roda
  float velEsq = 0;
  float velDir = sin(teste);
  //println("vel = " + velEsq + "  " + velDir);
  
  float v = raio * (velEsq + velDir) / 2;
  float dAng = raio/L * (velDir - velEsq);
  ang += dAng;
  
  float dx = v * cos(ang);
  float dy = v * sin(ang);
  
  pos.x += dx;
  pos.y += dy;
  
  println(pos);
  
  fill(0);
  ellipse(pos.x * 200, pos.y * 200, 5, 5);
  
  //// Ajusta o valor para a tela;
  //d = map(d, 0, t.distanciaMax, 0, width/2);
  
  //// Limpa a tela quando o círculo se completa
  //if(angulo % 360 == 0) background(0);
}
