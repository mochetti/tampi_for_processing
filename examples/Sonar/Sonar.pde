/*
  Exemplo com a função distancia();
  
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
  
  // Recebe a distância do ultrassônico do Tampi;
  float d = t.distancia();
  println("distancia = " + d);
  
  // Ajusta o valor para a tela;
  d = map(d, 0, t.distanciaMax, 0, width/2);
  
  // Desenha a linha partindo da origem
  stroke(255);
  line(0, 0, d * cos(angulo * PI / 180), d * sin(angulo * PI / 180));
  
  // Limpa a tela quando o círculo se completa
  if(angulo % 360 == 0) background(0);
  
  // Rotaciona o tampi
  t.girar(1);
  angulo += 1;
}
