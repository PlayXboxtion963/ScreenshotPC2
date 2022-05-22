package com.playxboxtion233.screenshotpc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class FindOutDevices {
    ArrayList<String> List = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();
    private long currentTime;
    private long lastRunTime;
    Context context;
    DatagramSocket socket = null;
    DatagramSocket socket2 = null;
    private AlertDialog signlechoose;
    public String[] temparray=null;
    public String[] namearray=null;
    private boolean isrun=true;
    Thread mthread;
    Thread sendthread;
    public String[] getiparray(){
       return this.temparray;
    }
    public String[] getNamearray(){
        return this.namearray;
    }
    public void setcontext(Context contextx){
        this.context=contextx;
    }
    public void startsearch(){
        mthread.start();
    }
    public void stopsearch(){
        mthread.interrupt();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socket.close();
        socket=null;


    }
    public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if(dhcp==null) {
            return InetAddress.getByName("255.255.255.255");
        }
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    public void Udpreceive(){

            try {
                socket = new DatagramSocket(9832);
                socket2=new DatagramSocket();
            } catch (SocketException e) {
                e.printStackTrace();
            }

        InetAddress ipBroad = null;
        try {
            ipBroad = getBroadcastAddress(context);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            String sendData = "FindService";
            byte[] data = sendData.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, ipBroad, 58974);   //③
            socket2.send(packet);
            socket2.close();
        } catch (Exception e) {
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
