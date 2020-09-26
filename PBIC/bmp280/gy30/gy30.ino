// Sensor de iluminância GY-30 + Arduino + Nokia Display 5110
// Bibliotecas Utilizadas
#include <Wire.h>
#include <BH1750.h>
 
// Configuração Pinos LCD e variáveis utilizadas
extern uint8_t BigNumbers[];
extern uint8_t luximg[];
 
// Instância e variável para o sensor
BH1750 lightSensor;
String luz;
 
void setup() {
  // Inicialização sensor de luz
  lightSensor.begin();
  
  // Configurações para o LCD
  Serial.begin(9600);
  Serial.println("Sensor de luminosidade ");
  delay(2000); 
}
 
void loop() {
  // Faz a leitura no sensor e atribui os valores
  uint16_t lux = lightSensor.readLightLevel();
  luz = String(lux);
  Serial.print("Valor : ");
  Serial.print(luz);
  Serial.println(" lux");
  delay(150); 
}
