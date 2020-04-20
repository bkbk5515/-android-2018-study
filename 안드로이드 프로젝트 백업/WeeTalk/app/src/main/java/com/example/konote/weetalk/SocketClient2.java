package com.example.konote.weetalk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class SocketClient2 {
    HashMap<String, DataOutputStream> clients;
    HashMap<String, DataOutputStream> room_user;
    ArrayList<String> userlist;

    private ServerSocket ServerSocket = null;

    public static void main(String[] args) {
        new SocketClient2().start();
    }

    public SocketClient2() {

        // 연결부 hashmap 생성자(Key, value) 선언
        clients = new HashMap<String, DataOutputStream>();
        room_user = new HashMap<String, DataOutputStream>();
        userlist = new ArrayList<String>();
        Collections.synchronizedMap(clients);
    }

    private void start() {

        // Port 값은 편의를위해 8787 고정 (Random값으로 변경가능)
        int port = 8787;
        Socket socket = null;

        try {
            // 서버소켓 생성후 while문으로 진입하여 accept(대기)하고 접속시 ip주소를 획득하고 출력한뒤
            // MultiThread를 생성한다.
            ServerSocket = new ServerSocket(port);
            System.out.println("접속대기중");
            while (true) {
                socket = ServerSocket.accept();
                InetAddress ip = socket.getInetAddress();
                System.out.println(ip + "  connected");
                new MultiThread(socket).start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    class MultiThread extends Thread {

        Socket socket = null;

        String id_listno = null;
        String id = null;
        String listno = null;

        JSONObject in_id_listno;

        DataInputStream input;
        DataOutputStream output;

        public MultiThread(Socket socket) {
            this.socket = socket;
            try {
                // 객체를 주고받을 Stream생성자를 선언한다.
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
            }
        }

        public void run() {
            try {
                // 접속된후 바로 id 주소를 받아와 출력하고 clients에 정보를 넘겨주고 클라이언트에게 id보낸다.

                id_listno = input.readUTF();
                System.out.println("id_listno  : " + id_listno);
                in_id_listno = new JSONObject(id_listno);

                id = in_id_listno.get("id").toString();
                listno = (String) in_id_listno.get("listno");
                System.out.println("id  : " + id);
                System.out.println("listno  : " + listno);

                clients.put(id, output);

                jdbc(listno, id);

                room_user.clear();

                jdbc_getroom(listno);

                System.out.println("userlist size : " + userlist.size());
                for (int i = 0; i < userlist.size(); i++) {
                    String user = userlist.get(i);
                    room_user.put(user, clients.get(user));
                }
                System.out.println("room_user : " + room_user.toString());
                System.out.println("output : " + output);

                // 그후에 채팅메세지수신시
                while (input != null) {
                    try {
                        String temp = input.readUTF();
                        sendMsg(temp);
                        System.out.println("temp : " + temp);
                    } catch (IOException e) {
                        sendMsg("No massege");
                        break;
                    }
                }
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        // 메세지수신후 클라이언트에게 Return 할 sendMsg 메소드
        private void sendMsg(String msg) {

            // clients의 Key값을 받아서 String 배열로선언
            Iterator<String> it = room_user.keySet().iterator();

            // Return 할 key값이 없을때까지
            while (it.hasNext()) {
                try {
                    OutputStream dos = room_user.get(it.next());
                    System.out.println("msg : " + msg);
                    if (dos != null) {
                        DataOutputStream output = new DataOutputStream(dos);
                        output.writeUTF(msg);
                    }
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public void jdbc(String room_no, String user_id) {
        Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myservice?useSSL=false", "root", "ehdtjsdlz1!");
            st = connection.createStatement();  // 자바에서 디비 데이터를 쿼리문을 통해 제어할 수 있게 connection에서 createStatement() 메소드를 통해 Statement 객체 생성

            String sql;

            sql = "insert into chat_room(room_no,user_id) select" + "'" + room_no + "','" + user_id + "'" + "from dual where not exists(select * from chat_room where room_no=" + "'" + room_no + "'" + "and user_id=" + "'" + user_id + "')";

            st.executeUpdate(sql);     //작성한 쿼리문을 executeQuery() 메소드를 통해 보낸다  그리고 ResultSet객체는 결과값을 객체에 저장

            st.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void jdbc_getroom(String room_no) {
        Connection connection = null;
        Statement st = null;
        String get_user_id = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myservice?useSSL=false", "root", "ehdtjsdlz1!");
            st = connection.createStatement();  // 자바에서 디비 데이터를 쿼리문을 통해 제어할 수 있게 connection에서 createStatement() 메소드를 통해 Statement 객체 생성
            System.out.println(room_no);
            String sql;
            sql = "select user_id from chat_room where room_no =" + "'" + room_no + "'";

            ResultSet rs = st.executeQuery(sql); //작성한 쿼리문을 executeQuery() 메소드를 통해 보낸다  그리고 ResultSet객체는 결과값을 객체에 저장
            userlist.clear();
            while (rs.next()) {  //ResultSet에는 next()메소드가 있어서 이 메소드로 실행결과의 다음 행 위치로 이동
                get_user_id = rs.getString("user_id"); // 만들어 준 디비의 해당 칼럼 자료형
                userlist.add(get_user_id);
            }
            System.out.println(userlist);

            rs.close();
            st.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}

