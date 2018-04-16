int PIN_lamp = 9;

const int numReadings = 5;

int readings[numReadings];      // the readings from the analog input
int index = 0;                  // the index of the current reading
int total = 0;                  // the running total
int average = 0;                // the average

#define maxLampInt 100
void setup() 
{
  // put your setup code here, to run once:
  pinMode(PIN_lamp, OUTPUT);
  digitalWrite(PIN_lamp, LOW);
  Serial.begin(115200);
  
  for (int thisReading = 0; thisReading < numReadings; thisReading++)
    readings[thisReading] = 0;
    
  analogReference(EXTERNAL);
}

float lampInts = 0;
bool booting = true;
int bootLampWait = 1000;
int out;

long lastUpdate = 0;
void loop() {
  
  if(Serial.available() > 0)
  {
      String in = Serial.readStringUntil('|');
      if(in == "s") 
      {
        booting = false;
        analogWrite(PIN_lamp, maxLampInt);
      }
  }
  
  if(booting)
  {
    analogWrite(PIN_lamp, lampInts);
    lampInts += .00035;
    if(lampInts > maxLampInt)
    {
      lampInts = 0; //maxLampInt;
    }
  }
/*
  if(booting)
  {
    delay(bootLampWait);
    digitalWrite(PIN_lamp, HIGH);
    delay(70);
    digitalWrite(PIN_lamp, LOW);
    bootLampWait -= 300;
  } 
  */
  

  /*
  if(bootLampWait < 0)
  {
    digitalWrite(PIN_lamp, HIGH);
    booting = false;
  }
  */
  // put your main code here, to run repeatedly:
  int in = analogRead(A0);

  total= total - readings[index];         
  // read from the sensor:  
  readings[index] = in; 
  // add the reading to the total:
  total= total + readings[index];       
  // advance to the next position in the array:  
  index = index + 1;                    

  // if we're at the end of the array...
  if (index >= numReadings)              
    // ...wrap around to the beginning: 
    index = 0;                           

  // calculate the average:
  average = total / numReadings;         

//Serial.println(in);
  
  static int updateTime =  14;
  if(((millis() - lastUpdate) > updateTime) || lastUpdate == 0)
  {
    in = average;
    if(in != out)
    {
      lastUpdate = millis();
      if(!booting)
      {
        //digitalWrite(PIN_lamp, HIGH);
        //booting = false;
      }
      out = in;
      if(out < 10)
        Serial.print("000");
      else if(out < 100)
        Serial.print("00");
      else if(out < 1000)
        Serial.print("0");  
      Serial.print(  out);
      Serial.print("|");
    }
  }
}
