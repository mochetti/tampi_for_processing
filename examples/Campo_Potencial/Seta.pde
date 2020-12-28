class Seta {
  float ang = 0;
  float amp = s;
  
  Seta(float a, boolean rad) {
    if(rad) ang = a + PI/2;
    else ang = (a + 90) * PI / 180 ;
  }
  
  Seta(float a, float n) {
    amp = a;
    ang = n;
  }
  
  void mostra(float x, float y) {
    stroke(255);
    strokeWeight(1);
    float nAmp = map(amp, 0, 0.0005, s/10, s/2);
    if(nAmp > s/2) nAmp = s/2;
    float xp = x + nAmp*sin(ang);
    float yp = y + nAmp*cos(ang);
    line(xp, yp, x - nAmp*sin(ang), y - nAmp*cos(ang));
    line(xp, yp, xp - nAmp*sin(PI/6 + ang), yp - nAmp*cos(PI/6 + ang));
    line(xp, yp, xp - nAmp*sin(-PI/6 + ang), yp - nAmp*cos(-PI/6 + ang));
  }
}
