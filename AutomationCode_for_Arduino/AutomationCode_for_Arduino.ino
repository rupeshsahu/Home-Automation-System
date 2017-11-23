int Pin12 = 12;
String readString;

void setup() {
  Serial.begin(9600);
  pinMode(Pin12, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(8, OUTPUT);
   
}

void loop() {
  while (Serial.available()) {
    delay(3);  
    char c = Serial.read();
    readString += c; 
  }
  if (readString.length() >0) {
    Serial.println(readString);
    if (readString == "TOA")     
    {
      digitalWrite(Pin12, HIGH);
    }
    if (readString == "TFA")
    {
      digitalWrite(Pin12, LOW);
    }
    if (readString == "TOB")     
    {
      digitalWrite(11, HIGH);
    }
    
    if (readString == "TFB")
    {
      digitalWrite(11, LOW);
    }

     if (readString == "TOC")     
    {
      digitalWrite(10, HIGH);
    }
    
    if (readString == "TFC")
    {
      digitalWrite(10, LOW);
    }
     if (readString == "TOD")     
    {
      digitalWrite(8, HIGH);
    }
    
    if (readString == "TFD")
    {
      digitalWrite(8, LOW);
    }
    readString="";
  } 
}

