package com.hxb.pdascancode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.IntentFilter;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

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


        String url = "http://10.0.5.73:8081/";
        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(url);
        // 这行代码一定加上否则效果不会出现
        WebSettings webSettings = webView.getSettings();

        // 授权
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Toast.makeText(getApplicationContext(),"duguangyan",Toast.LENGTH_LONG).show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });





        // 监听输入框（已经隐藏）
        et = findViewById(R.id.et);
        et.setEnabled(false);
        et.setVisibility(View.GONE);
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


        // 监听广播事件
        initBarcodeBroadcastReceiver(new ScanCodeBroadcastReceiver.OnReceiveCode() {
            @Override
            public void notifyThread(String code, int id) {
                webView.loadUrl("javascript:andriodCallBack('"+code+"')");
                // Toast.makeText(MainActivity.this, "监听到广播,code:" + code + ",id:" + id, Toast.LENGTH_LONG).show();
            }
        }, new EditText[]{et});

//        // h5网页加载进度
//        webView.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress == 100) {
////                    progressBar.setVisibility(View.GONE);
//
//                }
//            }
//        });


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
