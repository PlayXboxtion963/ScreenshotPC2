package com.playxboxtion233.screenshotpc;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class FindOutDevices {
    ArrayList<String> List = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();
    private long currentTime;
    private long lastRunTime;
    DatagramSocket socket = null;
    private AlertDialog signlechoose;
    public String[] temparray=null;
    public String[] namearray=null;
    private boolean isrun=true;
    Thread mthread;
    public String[] getiparray(){
       return this.temparray;
    }
    public String[] getNamearray(){
        return this.namearray;
    }
    public void startsearch(){
        mthread.start();
    }
    public void stopsearch(){
        mthread.interrupt();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.close();
        socket=null;


    }
    public void Udpreceive(){

            try {
                socket = new DatagramSocket(9832);
            } catch (SocketException e) {
                e.printStackTrace();
            }

       mthread=new Thread() {
           public void run() {
               while (true){
               try {
                   //阻塞代码：sleep、join和wait
                   byte[] buf = new byte[1024];
                   DatagramPacket packet = new DatagramPacket(buf, buf.length);
                   try {
                       socket.receive(packet);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   String ip = packet.getAddress().getHostAddress();//此时接收到的IP
                   String name = new String(packet.getData(), 0, packet.getLength());;
                   if (List.contains(ip) == false) {//如果不存在ip,就向LIST添加一个
                       List.add(ip);
                   }
                   if (nameList.contains(name) == false)
                       nameList.add(name);
                   int size = List.size();
                   temparray = (String[]) List.toArray(new String[size]);
                   int sizex = nameList.size();
                   namearray = (String[]) nameList.toArray(new String[sizex]);
                    System.out.println(ip);


               } catch (Exception e) {
                   //注释掉e.printStackTrace();
                   //添加其他业务代码,跳出阻塞
                   System.out.println("stop");
                   break;
               }

           }}


       };
    }

}
