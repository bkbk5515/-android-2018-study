//package com.example.konote.weetalk;
//
//
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
////import org.json.simple.JSONArray;
////import org.json.simple.JSONObject;
//
//public class SocketClient {
//
//    HashMap<String, DataOutputStream> clients;
//    HashMap<String, DataOutputStream> room_user;
//    ArrayList<String> userlist;
//    private ServerSocket ServerSocket = null;
//
//    public static void main(String[] args) {
//        new SocketClient().start();
//    }
//
//    public SocketClient() {
//
//        // 연결부 hashmap 생성자(Key, value) 선언
//        clients = new HashMap<String, DataOutputStream>();
//        room_user = new HashMap<String, DataOutputStream>();
//        userlist = new ArrayList<String>();
//        // clients 동기화
//        Collections.synchronizedMap(clients);
//    }
//
//    private void start() {
//
//        // Port 값은 편의를위해 5001로 고정 (Random값으로 변경가능)
//        int port = 5001;
//        Socket socket = null;
//
//        try {
//            // 서버소켓 생성후 while문으로 진입하여 accept(대기)하고 접속시 ip주소를 획득하고 출력한뒤
//            // MultiThread를 생성한다.
//            ServerSocket = new ServerSocket(port);
//            System.out.println("접속대기중");
//            while (true) {
//                socket = ServerSocket.accept();
//                InetAddress ip = socket.getInetAddress();
////                System.out.println(ip + "  connected");
//                new MultiThread(socket).start();
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//    }
//
//    class MultiThread extends Thread {
//
//        Socket socket = null;
//
//        String mac = null;
//        String msg = null;
//
//        JSONObject jsonObject;
//        String room_number = null;
//        String user_name = null;
//
//        DataInputStream input;
//        DataOutputStream output;
//
//        public MultiThread(Socket socket) {
//            this.socket = socket;
//            try {
//                // 객체를 주고받을 Stream생성자를 선언한다.
//                input = new DataInputStream(socket.getInputStream());
//                output = new DataOutputStream(socket.getOutputStream());
//
//            } catch (IOException e) {
//            }
//        }
//
//        public void run() {
//
//            try {
//                // 접속된후 바로 Mac 주소를 받아와 출력하고 clients에 정보를 넘겨주고 클라이언트에게 mac주소를보낸다.
//
//                mac = input.readUTF();
//                System.out.println("쳇 액티비티에서 접속시 보낸 정보  : " + mac);
//                jsonObject = new JSONObject(mac);
//                room_number = jsonObject.get("roomNumber").toString();
//                user_name = jsonObject.get("userName").toString();
//
//                System.out.println("방번호 : " + room_number);
//                System.out.println("참여자 : " + user_name);
//                clients.put(user_name, output);// 원래 mac+ 아웃풋
//                String _null = "";
//                sendMsg("----- " + user_name + "님 입장 -----", _null);
////                sendMsg(user_name, _null); // --------------------------------------여기까지
//
//                jdbc(room_number, user_name);
//
//                room_user.clear();
//                jdbc_getroom(room_number);
//
//                System.out.println("userlist size : " + userlist.size());
//
//                for (int i = 0; i < userlist.size(); i++) {
//                    String user = userlist.get(i);
//                    room_user.put(user, clients.get(user));
//                }
//                System.out.println("room_user : " + room_user.toString());
//
//                System.out.println("output : " + output);
//
//                // 그후에 채팅메세지수신시
//                while (input != null) {
//                    try {
//                        String temp = input.readUTF();
//                        sendMsg(temp, user_name);
//                        System.out.println(user_name + " : " + temp);
//                    } catch (IOException e) {
//                        sendMsg("No massege", _null);
//                        break;
//                    }
//                }
//            } catch (IOException e) {
//                System.out.println(e);
//            }
//        }
//
//        // 메세지수신후 클라이언트에게 Return 할 sendMsg 메소드
//        private void sendMsg(String msg, String mac) {
//
//            // clients의 Key값을 받아서 String 배열로선언
//            Iterator<String> it = clients.keySet().iterator();
//
//            // Return 할 key값이 없을때까지
//            while (it.hasNext()) {
//                try {
//                    OutputStream dos = clients.get(it.next());
//                    DataOutputStream output = new DataOutputStream(dos);
//                    //////////////
//
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("userName", mac);
//                    jsonObject.put("userMsg", msg);
//
//                    output.writeUTF(jsonObject.toString());//json 을 리턴함
//                } catch (IOException e) {
//                    System.out.println(e);
//                }
//            }
//        }
//    }
//
//    public void jdbc(String room_no, String user_id) {
//        Connection connection = null;
//        Statement st = null;
//
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?useSSL=false", "fnzlghen", "fnzlghen2!");
////            connection = DriverManager.getConnection("jdbc:mysql://localhost:5001;databaseName=db;user=fnzlghen;password=fnzlghen2!;");
//            st = connection.createStatement();  // 자바에서 디비 데이터를 쿼리문을 통해 제어할 수 있게 connection에서 createStatement() 메소드를 통해 Statement 객체 생성
//
//            String sql;
//
//            sql = "insert into RoomUserList(roomnumber,username) select" + "'" + room_no + "','" + user_id + "'" + "from dual where not exists(select * from RoomUserList where roomnumber=" + "'" + room_no + "'" + "and username=" + "'" + user_id + "')";
//
//            st.executeUpdate(sql);     //작성한 쿼리문을 executeQuery() 메소드를 통해 보낸다  그리고 ResultSet객체는 결과값을 객체에 저장
//
//            st.close();
//            connection.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (st != null)
//                    st.close();
//            } catch (SQLException se2) {
//                se2.printStackTrace();
//            }
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }
//
//
//    }
//
//    public void jdbc_getroom(String room_no) {
//        Connection connection = null;
//        Statement st = null;
//        String get_user_id = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db?useSSL=false", "fnzlghen", "fnzlghen2!");
////            connection = DriverManager.getConnection("jdbc:mysql://localhost:5001;databaseName=db;user=fnzlghen;password=fnzlghen2!;");
//            st = connection.createStatement();  // 자바에서 디비 데이터를 쿼리문을 통해 제어할 수 있게 connection에서 createStatement() 메소드를 통해 Statement 객체 생성
//            System.out.println(room_no);
//            String sql;
//            sql = "select username from RoomUserList where roomnumber =" + "'" + room_no + "'";
//
//            ResultSet rs = st.executeQuery(sql); //작성한 쿼리문을 executeQuery() 메소드를 통해 보낸다  그리고 ResultSet객체는 결과값을 객체에 저장
//            userlist.clear();
//            while (rs.next()) { //ResultSet에는 next()메소드가 있어서 이 메소드로 실행결과의 다음 행 위치로 이동
//                get_user_id = rs.getString("username"); // 만들어 준 디비의 해당 칼럼 자료형
//                System.out.println("디비에서 가져온 userID : " + get_user_id);
//                userlist.add(get_user_id);
//            }
//            System.out.println(userlist);
//
//            rs.close();
//            st.close();
//            connection.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (st != null)
//                    st.close();
//            } catch (SQLException se2) {
//                se2.printStackTrace();
//            }
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }
//        }
//    }
//}