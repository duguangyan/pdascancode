package com.hxb.pdascancode;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 条码通知
 * 广播接收后模拟回车输出
 *
 * @author h6900
 */
public class ScanCodeBroadcastReceiver extends BroadcastReceiver {

    /**
     * 需要监听pda扫描枪的文本框
     */
    private EditText[] editTexts;
    /**
     * pda扫描枪监听
     */
    private OnReceiveCode onReceive;
    /**
     * 传入的页面classname
     */
    private String TAG = "";

    public void addEditText(EditText editText) {
        List list = new ArrayList();
        list.addAll(Arrays.asList(editTexts));
        list.add(editText);
        editTexts = (EditText[]) list.toArray(new EditText[list.size()]);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String code = intent.getStringExtra(Declare.StringExtra);
        Log.i(TAG, "--" + MyApplication.isTopActivity(TAG));
        if (MyApplication.isTopActivity(TAG)) {
            int id = outputEditTextByFocus(code);
            onReceive.notifyThread(code, id);
        }
    }

    /**
     * 给获取焦点的输入框输出广播
     *
     * @param code 广播返回的code
     * @return 当前获取焦点的输入框id
     */
    private int outputEditTextByFocus(String code) {
        for (EditText editText : editTexts) {
            Log.i(TAG, editText.hasFocus() + "--" + editText.getId() + "");
            if (editText.hasFocus()) {
                Log.i(TAG, editText.getId() + "");
                editText.setText(code);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 模拟回车按键
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);  //传入不同的keycode就ok了
                    }
                }).start();
                return editText.getId();
            }
        }
        return 0;
    }


    public interface OnReceiveCode {
        /**
         * 通知
         *
         * @param code
         * @param id
         */
        void notifyThread(String code, int id);
    }

    public void setOnReceive(String tag, OnReceiveCode onReceive, EditText[] editTexts) {
        this.TAG = tag;
        this.onReceive = onReceive;
        this.editTexts = editTexts;
    }


}
