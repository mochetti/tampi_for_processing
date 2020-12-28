class Robo {
  PVector pos;
  float ang = 0;
  Robo() {
  pos = new PVector(width/2, height/2);
  }
  
  // mostra o robo e sua direção
  void show() {
    fill(0, 255, 0);
    ellipse(pos.x, pos.y, 10, 10);
    stroke(255);
    strokeWeight(2);
    line(pos.x, pos.y, pos.x + 15*cos(ang), pos.y + 15*sin(ang));
    stroke(0);
  }
}
