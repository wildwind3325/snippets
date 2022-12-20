package org.clkg;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketSample {

    public static void server() {
        try {
            ServerSocket ss = new ServerSocket(21000);
            Socket s = ss.accept();
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("This is server.");
            pw.flush();
            System.out.println(br.readLine());
            pw.close();
            br.close();
            s.close();
            ss.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void client() {
        try {
            Socket s = new Socket("127.0.0.1", 21000);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println("This is client.");
            pw.flush();
            System.out.println(br.readLine());
            pw.close();
            br.close();
            s.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
