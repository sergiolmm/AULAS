
void setup() {
  Serial.begin(9600);
  Serial.println(" 14CORE | MiCS 5524 / 6814 TEST CODE ");
  Serial.println(" Initializing ...............");
  delay(2000);
  Serial.println("Data Communication Ready");
  Serial.println("Reading ................");
  delay(1000);
}
 
void loop() {
  int senseval1 = analogRead(A0);
  int senseval2 = analogRead(A1);
  int senseval3 = analogRead(A2);
  
  Serial.print(" Reading A0 :");
  Serial.print(senseval1);
  Serial.println("");
  Serial.print(" Reading A1 :");
  Serial.print(senseval2);
  Serial.println("");
  Serial.print(" Reading A2 :");
  Serial.print(senseval3);
  Serial.println("");
  delay(1000);
}
