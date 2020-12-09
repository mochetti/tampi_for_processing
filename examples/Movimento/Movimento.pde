import sala49.tampi.*;

Tampi t;
void setup() {
t = new Tampi(this);
t.init();
}
void draw() {
}

void webSocketEvent(String msg){
 println(msg);
}
