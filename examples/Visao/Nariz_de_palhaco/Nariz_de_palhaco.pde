// Necessita da biblioteca de video do Processing
import processing.video.*;
//import tampi.*;

Capture cam;
//Tampi t;
color c;
ArrayList<Blob> blobs = new ArrayList<Blob>(); 

void setup() {
  size(640, 480);

  String[] cameras = Capture.list();
  cam = new Capture(this, cameras[0]);
  cam.start();
  
}

void draw() {
  if (cam.available() == true) {
    cam.read();
  }
  //background();
  image(cam, 0, 0, width, height);
  busca(c);
  //println(blobs.size());
  for(Blob b : blobs) {
    ellipse(b.center().x, b.center().y, 10, 10);
  }
}

void mouseClicked() {
  c = get(mouseX, mouseY);
  println("r: " + red(c) + "  g: " + green(c) + "  b: " + blue(c));
}

void busca(color cor) {
    for(int x=0; x<width; x++) {
      for(int y=0; y<height; y++) {
      color p = get(x, y);
      if(sameColor(p, cor)) {
        point(x, y);
        //if(blobs.size() > 0) {
        //  for(Blob b : blobs) {
        //    if(b.isNear(x, y)) {
        //      b.add(x, y);
        //    }
        //  }
        //}
        //else {
        //  blobs.add(new Blob(x, y));
        //}
      }
    }
  }
}
  
boolean sameColor(color c1, color c2) {
  int trig = 20;
  if(abs(red(c1)-red(c2)) < trig  && abs(green(c1)-green(c2)) < trig &&  abs(blue(c1)-blue(c2))<trig) {
  return true;
  }
  else {
  return false;
  }
}
