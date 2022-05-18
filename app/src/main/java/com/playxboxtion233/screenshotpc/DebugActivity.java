package com.playxboxtion233.screenshotpc;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.arialyy.aria.core.Aria;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;

import java.util.Arrays;
import java.util.UUID;

import aria.apache.commons.net.util.Base64;

public class DebugActivity extends AppCompatActivity implements View.OnClickListener {
    private static String URIx = null;
    private Button btn;
    private int zhendong=1;
    private String MAC="FB:35:A2:DE:F5:49";
    public static final UUID SERIVER_UUID = UUID.fromString("0000180d-0000-1000-8000-00805F9B34FB");

    public static final UUID CHARACTER_UUID =  UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    TextView mtext;
    BluetoothClient mClient ;
    byte[] value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Aria.download(this).register();
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);

        mClient = new BluetoothClient(this);
        mClient.openBluetooth();
        mtext=findViewById(R.id.dmaw);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
            System.out.println("点击了");
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
                            System.out.println(value[1]);
                            byte[] Temp=new byte[1];
                            Temp[0]=value[1];
                            mtext.setText("心率"+"♥"+Arrays.toString(Temp));
                    }

                    @Override
                    public void onResponse(int code) {
                        if (code == REQUEST_SUCCESS) {
                        }
                    }
                });
                break;
            default:
                break;
        }
    }



}