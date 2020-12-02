import sala49.tampi.*;

Tampi t;
void setup() {
t = new Tampi(this);
}
void draw() {
println(t.mouseEvent(new MouseEvent));
}
