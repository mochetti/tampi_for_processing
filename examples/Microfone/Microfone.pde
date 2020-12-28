/*
  Exemplo da função microfone()
  
  Tampi for Processing
*/

import sala49.tampi.*;

Tampi t;
float angulo = 0;

void setup() {
  size(600,600);  
  t = new Tampi(this);
}

void draw() { 
  // Desloca a origem para o centro da tela
  translate(width/2, height/2);
  
  // Recebe o volume do microfone do Tampi;
  float v = t.microfone();
  println("volume = " + v);
  
  // Ajusta o valor para a tela;
  v = map(v, 0, t.volumeMax, 0, width/2);
  
  // Desenha a linha partindo da origem
  stroke(255);
  line(0, 0, v * cos(angulo * PI / 180), v * sin(angulo * PI / 180));
  
  // Limpa a tela quando o círculo se completa
  if(angulo % 360 == 0) background(0);
  
  angulo += 1;
}
