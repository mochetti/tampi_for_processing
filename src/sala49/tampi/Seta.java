package sala49.tampi;

public class Seta {
    float ang = 0;
    float amp = 0;

    Seta(float a, float n) {
        amp = a;
        ang = n;
    }
    
    void mostra(float x, float y) {
        app.stroke(255);
        app.strokeWeight(1);
        float nAmp = map(amp, 0, 0.0005, s/10, s/2);
        if(nAmp > s/2) nAmp = s/2;
        float xp = x + nAmp*sin(ang);
        float yp = y + nAmp*cos(ang);
        line(xp, yp, x - nAmp*sin(ang), y - nAmp*cos(ang));
        line(xp, yp, xp - nAmp*sin(PI/6 + ang), yp - nAmp*cos(PI/6 + ang));
        line(xp, yp, xp - nAmp*sin(-PI/6 + ang), yp - nAmp*cos(-PI/6 + ang));
    }   
}
