# 📱 Car-Talk_Android 📱
## ⚙ Car-Talk Android Application 기능
### 📢 1. 정보 수신
타 기기에서 Advertising한 신호를 앱으로도 수신할 수 있습니다. <br>
수신은 앱에 접속하여 모드 작동 버튼 클릭하여 이용할 수 있습니다. <br>

✏︎ 알림에 표시되는 내용
* 알림종류(좌측에 색상으로 표시) <br>
응급, 사고/공사, 기타 알림으로 나누어 알림 종류를 표시한다.<br>
이때 알림 종류는 사용자가 시각적으로 쉽게 인지할 수 있도록 각각 빨강, 노랑, 파랑색 바를 좌측에 위치시킨다.<br>
* 차량번호(차선) <br>
&nbsp; 차량 번호는 응급, 사고 알람에서만 사용 가능하도록 한다. <br>
&nbsp; 이 경우 사용자가 직접 차선을 입력해야한다.<br>
* 알림내용 <br>
&nbsp; 송신된 번호에 따라 차등하여 메시지를 수신함 <br>

<img src = "application\message.PNG" width = "800" height = "200" alt = "messge contents">

* 시간(우측 하단에 표시) 
* 알림 삭제(우측 상단에 표시)<br><br>

### ✅ 2.필터링
체크박스를 체크하여 앱에서 원하는 내용의 알림만 수신할 수 있도록 합니다.
체크박스는 응급알림 / 사고 및 공사 알림 / 기타(끼어들기 및 커브길) 알림 세가지 종류로 구성됩니다. <br>

### 🔔 3.푸시알림
알림받은 내용을 핸드폰 푸시알림으로 뜰 수 있도록 설정합니다.
알림은 소리 또는 진동으로 설정 가능합니다.
푸시 알림의 우측에는 알림 종류 별로 이미지를 다르게 할당합니다.

### 🍓 4. 구체적 앱 동작
<img src = "application\aplication active.jpg" alt = "application active">
1. 앱이 시작되면 약 1초간 시작화면이 표시된다.<br>
2. 시작화면 이후 "스캐닝 시작" 버튼이 화면의 중앙에 생성되는데, 버튼을 누르면 타 차량에서 보내는 알림을 수신할 수 있다.<br>
3. 스캐닝을 시작하는 화면에서 3개의 가로줄로 이루어진 설정 버튼을 누르면 Setting 화면으로 이동한다. 본 화면에서는 Push Alarm, Sound Setting, Vibarate setting 을 설정할 수 있다.<br>
&nbsp;&nbsp; 3-1. 즉 Push Alarm을 끄거나 알람의 소리, 진동을 설정할 수 있다.<br>
&nbsp;&nbsp; 3-1. 이때 각 항목을 스위치하여 켜면 굵은 글씨로, 끄면 얇은 글씨로 표시된다.<br>
4. 스캐닝 화면에서는 체크박스를 통하여 알람의 종류를 구분하여 알림 받을 수 있다.<br>
&nbsp;&nbsp; 4-1. 알림의 종류는 응급 상황, 사고/공사, 기타 세가지로 나뉜다.<br>
&nbsp;&nbsp; 4-2. 체크박스로 나눈 알림의 종류별로 표시되는 색상도 다르게 설정하였다.(빨강, 노랑, 파랑색 세로 바 이용하였다.) <br>
&nbsp;&nbsp; 4-2. "앞에 차량이 있습니다. 천천히 가세요." 등의 메시지를 화면에 구분선으로 구분지어 각 알림을 표시해 준다.<br>
5. 푸시알람은 위의 사진과 같이 표시된다.<br>
6. 스캐닝 종료하기 버튼을 클릭하면 스캐닝이 종료된다.<br>
