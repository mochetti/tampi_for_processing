/*
  Exemplo da função busca(int c)
  
  Clique com o mouse na cor que deseja buscar
*/

import sala49.tampi.*;

color cor;
Tampi t;

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
  size(600,600);  
  t = new Tampi(this);
  fundo();
}

void draw() {  
}

void mousePressed() {
  cor = get(mouseX, mouseY);
  println("cor: " + red(cor), green(cor), blue(cor));
  Blob[] blobs = t.busca(cor);
  fill(0);
  for(Blob b : blobs) ellipse(b.center().x, b.center().y, 10, 10);
}

void fundo() {
  background(0);
  for(int i=50; i<width; i+= 50) {
    for(int j=50; j<height; j+= 50) {
      fill(cores[int(random(cores.length))]);
      ellipse(i, j, 30, 30);
    }
  }
}
