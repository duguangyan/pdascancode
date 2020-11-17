package com.hxb.pdascancode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getName();

    private ScanCodeBroadcastReceiver setOnReceive;

    private EditText et;

    /**
     * 初始化
     *
     * @param onReceiveCode
     * @param editTexts
     */
    protected void initBarcodeBroadcastReceiver(ScanCodeBroadcastReceiver.OnReceiveCode onReceiveCode, EditText[] editTexts) {
        if (!TextUtils.isEmpty(Declare.Action)
                && !TextUtils.isEmpty(Declare.StringExtra)) {
            setOnReceive = new ScanCodeBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            //"android.intent.ACTION_DECODE_DATA"
            intentFilter.addAction(Declare.Action);
            registerReceiver(setOnReceive, intentFilter);
            setOnReceive.setOnReceive(TAG, onReceiveCode, editTexts);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = findViewById(R.id.et);

        et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 监听到回车键
                    //do some
                    Toast.makeText(MainActivity.this, "监听到回车事件", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        initBarcodeBroadcastReceiver(new ScanCodeBroadcastReceiver.OnReceiveCode() {
            @Override
            public void notifyThread(String code, int id) {
                Toast.makeText(MainActivity.this, "监听到广播,code:" + code + ",id:" + id, Toast.LENGTH_LONG).show();
            }
        }, new EditText[]{et});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁
        if (setOnReceive != null) {
            unregisterReceiver(setOnReceive);
        }
    }


}
