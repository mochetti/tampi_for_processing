/*
  Exemplo da função busca(int c)
  
  Clique com o mouse na cor que deseja buscar
  Aperte uma tecla para gerar um novo padrão de fundo
  
  Tampi for Processing
*/

import tampi.*;

Visao visao;
color cor;

color[] cores = {
  color(255, 0, 0),
  color(0, 255, 0),
  color(0, 0, 255),
  color(255),
  color(255, 255, 0), 
  color(255, 0, 255),
  color(0, 255, 255),
  };

void setup() {
  // define as dimensões da tela
  size(600,600);  
  // cria um objeto Visao
  visao = new Visao(this);
  // desenha um fundo
  fundo();
}

void draw() {  
}

// Função chamada quando o mouse é apertado
void mousePressed() {
  // Pega a cor do pixel onde o mouse está
  cor = get(mouseX, mouseY);
  println("cor: " + red(cor), green(cor), blue(cor));
  // Filtra pixels pretos
  if(cor == -16777216) return;
  
  // Recebe os blobs encontrados com a cor do pixel
  Blob[] blobs = visao.busca(cor);
  
  // Marca os centros dos blobs encontrados
  fill(0);
  for(Blob b : blobs) ellipse(b.center().x, b.center().y, 10, 10);
}

// Função chamada quando qualquer tecla for pressionada
void keyPressed() {
  fundo();
}

// Função que desenha o fundo
void fundo() {
  background(0);
  for(int i=50; i<width; i+= 50) {
    for(int j=50; j<height; j+= 50) {
      // Pega uma cor aleatória do array cores[]
      fill(cores[int(random(cores.length))]);
      ellipse(i, j, 30, 30);
    }
  }
}
