package com.playxboxtion233.screenshotpc;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.TextView;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.UUID;

public class Bluetoothheart extends MainActivity {
    private String MAC="FB:35:A2:DE:F5:49";
    public static final UUID SERIVER_UUID = UUID.fromString("0000180d-0000-1000-8000-00805F9B34FB");
    public static final UUID CHARACTER_UUID =  UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    byte[] value;
    BluetoothClient mClient ;
    Context context;
    public String heartrate;
    TextView mtext;
    public void setContext(Context context){
        this.context=context;
    }//传上下文
    public void setMac(String mac) {
        this.MAC = mac;
    }//传MAC
    public String getheartrate(){
        return this.heartrate;
    }//取得心率
    public void setTextview(TextView textview){
        this.mtext=textview;
    }//传入要改的textview
    public void startble(){
        mClient = new BluetoothClient(context);
        mClient.openBluetooth();
        mClient.connect(MAC, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {

                }
            }
        });
        mClient.notify(MAC, SERIVER_UUID, CHARACTER_UUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                byte[] Temp=new byte[1];
                Temp[0]=value[1];
                heartrate=Arrays.toString(Temp);
                heartrate=heartrate.substring(1, 3);
                mtext.setText("♥心率"+heartrate);
                System.out.println(Integer.valueOf(heartrate));

            }

            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                }
            }
        });
    }
}
