package Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerHandler {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

            try {
                ServerSocket serverSocket = new ServerSocket(6780);

                System.out.println("Server er klar til at modtage klient");
                Socket socket = serverSocket.accept();

                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());

                    Thread read = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                    System.out.println("Modtaget fra klient: " + bufferedReader.readLine());
                                } catch (Exception e) {
                                    System.out.println("Error " + e.getMessage());
                                    System.exit(0);
                                }
                            }
                        }
                    });

                Thread send = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            String msg = scan.nextLine();
                            if (!msg.equalsIgnoreCase("quit")) {
                                try {
                                    PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                                    printWriter.println(msg);
                                } catch (Exception e) {
                                    System.out.println("Error " + e.getMessage());
                                }
                            } else {
                                System.exit(0);
                            }
                        }
                    }
                });

                read.start();
                send.start();

                if (!read.isAlive()){
                    send.stop();
                }

            } catch (Exception e) {
                System.out.println("Error " + e.getMessage());
            }
        }
}