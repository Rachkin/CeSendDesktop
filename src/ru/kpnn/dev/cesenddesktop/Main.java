package ru.kpnn.dev.cesenddesktop;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.println("AAAA");
        Data data = new Data();
        data.start();
        System.out.println("Data Created!"); // хорошо бы серверу`
        //BufferedReader s = new BufferedReader(inputStream);
        //String result = s.readLine(); ;
        sleep(500);
        data.sendToAll("111");

        while(true){
            int type = System.in.read();

        }

    }


}
