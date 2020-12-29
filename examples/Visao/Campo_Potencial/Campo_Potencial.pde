/*
  Exemplo com as funções campoPotencial() e trajeto();
  Gera um campo de vetores baseado nas coordenadas do alvo e dos obstáculos.
  Gera uma trajetória a partir do campoPotencial();
  
  As coordenadas do mouse definem as coordenadas do alvo (vermelho).
  Clique com o mouse para definir as coordenadas da origem (branco).
  As coordenadas dos obtáculos são aleatórias (verdes).
  Pressione qualquer tecla para ativar/desativar o trajeto.
  
  Tampi for Processing
 */

import sala49.tampi.*;
Visao visao;

PVector alvo, origem;
PVector[] obstaculos;
PVector[][] campo;
int res = 25;
boolean trajeto = false;

void setup() {
  size(400, 400);
  alvo = new PVector(width/2, height/2);
  obstaculos = new PVector[3];
  for (int i=0; i<obstaculos.length; i++) obstaculos[i] = new PVector(random(width), random(height));
  
  origem = new PVector();
  visao = new Visao(this);
}

void draw() {
  background(0);
  
  // alvo tem as coordenadas do mouse
  alvo.x = mouseX;
  alvo.y = mouseY;
  
  // desenha a origem
  fill(255);
  ellipse(origem.x, origem.y, 10, 10);

  // desenha os obstáculos
  fill(0, 255, 0);
  for (PVector obs : obstaculos) ellipse(obs.x, obs.y, 10, 10);

  // desenha o alvo
  fill(255, 0, 0);
  ellipse(alvo.x, alvo.y, 10, 10);
  
  // gera o campo potencial
  campo = visao.campoPotencial(alvo, obstaculos, 5, res, true);
  
  // gera o trajeto
  if(trajeto) visao.trajeto(origem, campo, true);
}

// função chamada quando qualquer tecla é pressionada
void keyPressed() {
  trajeto = !trajeto;
}

// função chamada quando o mouse é pressionado
void mousePressed() {
  origem.x = mouseX;
  origem.y = mouseY;
}
