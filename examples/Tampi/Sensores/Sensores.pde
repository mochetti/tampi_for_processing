/*
  Exemplo com as leituras de todos os sensores do Tampi
  
  Tampi for Processing
*/
import tampi.*;

Tampi t;
void setup() {
  t = new Tampi(this);
  t.init();
}
void draw() {
  println("Bateria: " + t.bateria()*100/1023 + "%");
  println("Volume mic: " + t.volMic());
  println("Freq mic: " + t.freqMic() + "Hz");
  println("LDR Esq: " + t.ldrEsq());
  println("LDR Dir: " + t.ldrDir());
  println("Encoder Esq: " + t.encoderEsq());
  println("Encoder Dir: " + t.encoderDir());
  println("IR Esq: " + t.irEsq());
  println("IR Dir: " + t.irDir());
  println("Tampinhas topo: " + t.tampinhasTopo());
  println("Tampinhas esq: " + t.tampinhasEsq());
  println("Tampinhas dir: " + t.tampinhasDir());
  println("Analog extra 1: " + t.aExtra1());
  println("Analog extra 2: " + t.aExtra2());
  println("Analog extra 3: " + t.aExtra3());
  println("Analog extra 4: " + t.aExtra4());
  println("Velocidade esq: " + t.velEsq() + "mm/s");
  println("Velocidade dir: " + t.velDir() + "mm/s");
  println("Distância: " + t.distancia() + "mm");
}
