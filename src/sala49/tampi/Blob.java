package sala49.tampi;

import processing.core.PVector;

// Classe que gerencia os objetos lidos pela camera baseado na cor

public class Blob {
  // Canto superior esquerdo e inferior direito
  float minx;
  float miny;
  float maxx;
  float maxy;
  int cor;
  // int id = -1;
  int numPixels = 0;

  Blob(float x, float y) {
    minx = x;
    miny = y;
    maxx = x;
    maxy = y;
  }
  Blob(float x, float y, int c) {
    minx = x;
    miny = y;
    maxx = x;
    maxy = y;
    cor = c;
  }

  Blob(Blob b) {
    minx = b.minx;
    miny = b.miny;
    maxx = b.maxx;
    maxy = b.maxy;
    cor = b.cor;
    // id = b.id;
    numPixels = b.numPixels;
  }

  Blob() {
  }
  
  protected Blob clone() {
    Blob b = new Blob(this);
    return b;
  }

  public PVector center() {
    PVector centro = new PVector();
    centro.x = (minx+maxx)/2;
    centro.y = (miny+maxy)/2;
    
    return centro;
  }

  void add(float x, float y) {
    minx = Math.min(minx, x);
    miny = Math.min(miny, y);
    maxx = Math.max(maxx, x);
    maxy = Math.max(maxy, y);
    numPixels++;
  }
  
  // Limpa as coordenadas do blob
  void reset() {
    minx = Float.MAX_VALUE;
    miny = Float.MAX_VALUE;
    maxx = 0;
    maxy = 0;
    numPixels = 0;
  }
  
  //// Verifica se um ponto (x, y) faz parte do blob
  boolean isNear(float x, float y) {
    int distancia = 10;
    float cx = Math.max(Math.min(x, maxx), minx);
    float cy = Math.max(Math.min(y, maxy), miny);
    float d = distSq(cx, cy, x, y);

  if (d < distancia*distancia) return true;
    return false;
  }

  float size() {
    return (maxx-minx)*(maxy-miny); 
  }

  private float distSq(float ax, float ay, float bx, float by) {
      return (ax-bx)*(ax-bx) + (ay-by)*(ay-by);
  }
}