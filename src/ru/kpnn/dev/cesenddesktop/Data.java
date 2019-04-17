package ru.kpnn.dev.cesenddesktop;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Data extends Thread{
    private Socket listenSocket; //сокет для общения
    private ServerSocket server; // серверсокет
    private BufferedReader listenIn; // поток чтения из сокета
    private BufferedWriter listenOut; // поток записи в соке

    private Socket sendSocket; //сокет для общения

    private BufferedReader sendIn; // поток чтения из сокета
    private BufferedWriter sendOut; // поток записи в сокет

    private String _address = "127.0.0.1";
    private int _port = 3306;
    private static Vector<String> dialog;
    //public static Vector<Dialog> my_dialogs;
    public static Map<String, User> my_users;

    @Override
    public void run(){
        try {
            try  {
                server = new ServerSocket(4004); // серверсокет прослушивает порт 4004
                System.out.println("Server Started!"); // хорошо бы серверу
                //   объявить о своем запуске
                listenSocket = server.accept(); // accept() будет ждать пока
                //кто-нибудь не захочет подключиться
                try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
                    // к созданию потоков ввода/вывода.
                    // теперь мы можем принимать сообщения
                    listenIn = new BufferedReader(new InputStreamReader(listenSocket.getInputStream()));
                    // и отправлять
                    listenOut = new BufferedWriter(new OutputStreamWriter(listenSocket.getOutputStream()));

                    String word = listenIn.readLine(); // ждём пока клиент что-нибудь нам напишет
                    this.newMessege(word);

                } finally { // в любом случае сокет будет закрыт
                    listenSocket.close();
                    // потоки тоже хорошо бы закрыть
                    listenIn.close();
                    listenOut.close();
                }
            } finally {
                System.out.println("Сервер закрыт!");
                server.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }


    public void addUser(User user){
        User tmp =  this.my_users.get(user.login);
        if(tmp == null) {
            System.out.println("Creating New User");
            this.my_users.put(user.login, user);
        }else{
            System.out.println("Changing User IP and Port");
            this.my_users.put(user.login, user);
        }
    }

    public void newMessege(String txt){ this.dialog.addElement(txt);  }

    public static Vector<String> getDialog(){
        return dialog;
    }
    public static Map<String, User> getUsers(){ return my_users; }

    public Data(){
        this.dialog = new Vector<String>();



        this.newMessege("AAAAAA");

        this.my_users = new HashMap<String, User>();
        User usr = new User();
        usr.login = "1111";
        usr.ip = _address;
        usr.port = _port;
        this.addUser(usr);
    }





    public void send(User user, String txt){
        System.out.println("Sending to " + user.login);
        try {
            try {
                // адрес - локальный хост, порт - 4004, такой же как у сервера
                sendSocket = new Socket(user.ip, user.port); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение

                // читать соообщения с сервера
                sendIn = new BufferedReader(new InputStreamReader(sendSocket.getInputStream()));
                // писать туда же
                sendOut = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

                // не напишет в консоль
                sendOut.write(txt + "\n"); // отправляем сообщение на сервер
                sendOut.flush();

                String serverWord = listenIn.readLine(); // ждём, что скажет сервер
                System.out.println(serverWord); // получив - выводим на экран

            } finally { // в любом случае необходимо закрыть сокет и потоки
                System.out.println("Клиент был закрыт...");
                sendSocket.close();
                sendIn.close();
                sendOut.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    public void sendToAll(String txt){
        for(Map.Entry<String, User> item : this.my_users.entrySet()){
            send(item.getValue(), txt);
        }
    }
}
