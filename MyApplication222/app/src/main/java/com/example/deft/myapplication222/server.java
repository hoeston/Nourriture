package com.example.deft.myapplication222;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Deft on 2016/7/7.
 */

public class server{

    private static String[] infos = new String[2];

    public static void main(String args[]) {

        server server = new server();
        server.init();



    }
        public void init() {
            try {
                ServerSocket serverSocket = new ServerSocket(4700);
                while (true) {
                    // 一旦有堵塞, 则表示服务器与客户端获得了连接
                    Socket client = serverSocket.accept();
                    // 处理这次连接
                    new HandlerThread(client);
                }
            } catch (Exception e) {
                System.out.println("服务器异常: " + e.getMessage());
            }
        }

        private class HandlerThread implements Runnable {
            private Socket socket;
            public HandlerThread(Socket client) {
                socket = client;
                new Thread(this).start();
            }

            public void run() {
                try {
                    String line;
                    BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //由Socket对象得到输入流，并构造相应的BufferedReader对象
                    PrintWriter os=new PrintWriter(socket.getOutputStream());
                    //由Socket对象得到输出流，并构造PrintWriter对象
                    BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
                    //由系统标准输入设备构造BufferedReader对象
                    System.out.println("Client:"+is.readLine());
                    //在标准输出上打印从客户端读入的字符串
                    for(int i = 0;i < 2;i++){
                        infos[i] = is.readLine();
                    }
                    //如果该字符串为 "bye"，则停止循环

                    os.println("0/1");
                    //向客户端输出该字符串
                    os.flush();
                    //刷新输出流，使Client马上收到该字符串
                    //从系统标准输入读入一字符串
                    //继续循环
                    os.close(); //关闭Socket输出流
                    is.close(); //关闭Socket输入流
                } catch (Exception e) {
                    System.out.println("服务器 run 异常: " + e.getMessage());
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (Exception e) {
                            socket = null;
                            System.out.println("服务端 finally 异常:" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

