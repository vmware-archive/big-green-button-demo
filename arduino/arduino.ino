const int buttonPin = 15;

int buttonState = 0;
int lastButtonState = 0;
int lastPing = 0;

void setup() {
  pinMode(buttonPin, INPUT_PULLUP);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  buttonState = digitalRead(buttonPin);

  if (buttonState != lastButtonState) {
    if (buttonState == LOW) {
      Serial.print("B");
    }
  }

  if (lastPing >= 20) {
    Serial.print("P");
    lastPing = 0;
  }

  lastButtonState = buttonState;
  lastPing++;

  delay(50);
}
