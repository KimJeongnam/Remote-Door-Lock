#include <deprecated.h>
#include <MFRC522.h>
#include <MFRC522Extended.h>
#include <require_cpp11.h>

#include <SoftwareSerial.h>

#define BUF_SIZE 100
#define RelayPin 6

void DoorControll(){
    digitalWrite(RelayPin, HIGH);
    delay(1000);
    digitalWrite(RelayPin, LOW);
}

SoftwareSerial BTSerial(2, 3); //Connect HC-06. Use your (TX, RX) settings

String string;
int string_size = 0;
int flag = 0; //데이터 수신시 문자열의끝까지 다받을시1 다받지못했을경우0 

void setup()  
{
  Serial.begin(9600);
  Serial.println("Hello!");
  pinMode(RelayPin, OUTPUT);
  digitalWrite(RelayPin, LOW);
  BTSerial.begin(9600);  // set the data rate for the BT port
}

void loop()
{
  // BT –> Data –> Serial
  //블루투스시리얼에 수신된 데이터가 있는지 확인
  if (BTSerial.available()) {
    //읽은 문자하나당 string에 붙여서 더한다
    char buf = BTSerial.read();
    string.concat(buf);

    //만약 읽은문자가 개행이라면 string의끝을 null값으로 대체한다.
    if(buf == '\n'){
      string[--string_size]='\0';
      flag = 1; //문자열을 다받았으니1
    }
    Serial.write(buf);
  }
  // Serial –> Data –> BT
  if (Serial.available()) {
    BTSerial.write(Serial.read());
  }
  //string데이터가 "DoorSwitch"라면 DoorControll()함수호출
  if(string.equals("DoorSwitch")){
    DoorControll();
    Serial.println();
    Serial.println("Door Controll!");
  }
  //도어락컨트롤후 혹은 수신된 프로토콜처리 후 플레그, 문자열초기화
  if(flag == 1){
    Serial.print("receive String : ");
    Serial.println(string);
    Serial.println(string.length());
    flag = 0;
    string = "";
  }
}
