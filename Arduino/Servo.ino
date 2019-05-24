/*
#include <Servo.h>

// Pines utilizados
#define PSERVO 9

Servo servo;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  // Vincular servo con el pin 9
  servo.attach(PSERVO);
  //Posicion Inicial 0 grados
  servo.write(90);
}

void loop() {
  servo.write(180);
  delay(1000);
  servo.write(90);
  delay(1000);
  servo.write(0);
  delay(1000);
  servo.write(90);
  Serial.print(servo.read());
  delay(1000);
}
*/
