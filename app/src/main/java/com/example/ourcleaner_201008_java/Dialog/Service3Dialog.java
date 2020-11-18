package com.example.ourcleaner_201008_java.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.ourcleaner_201008_java.Interface.Service3DialogClickListener;
import com.example.ourcleaner_201008_java.R;

public class Service3Dialog extends Dialog {

    private Context context;
    private Service3DialogClickListener service3DialogClickListener;
    private TextView dialog_alert_title, plusTimeTxt, optionGuidTxt;
    private String titleStr, plusTimeStr,  optionGuidStr;

    private Button plusBtn;
    private ImageButton closeBtn;

    private static final String TAG = "다이얼로그";

    public Service3Dialog(@NonNull Context context, String titleStr, String plusTimeStr, String optionGuidStr, Service3DialogClickListener service3DialogClickListener
    ) {
        super(context);
        this.context = context;
        this.service3DialogClickListener = service3DialogClickListener;
        this.titleStr = titleStr;
        this.plusTimeStr =plusTimeStr;
        this.optionGuidStr=optionGuidStr;
    }

    public Service3Dialog(@NonNull Context context, Service3DialogClickListener service3DialogClickListener) {
        super(context);
        this.context = context;
        this.service3DialogClickListener = service3DialogClickListener;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service3_dialog);
        Log.d(TAG, "=== onCreate ===" );

        dialog_alert_title = findViewById(R.id.dialog_alert_title);
        plusTimeTxt = findViewById(R.id.plusTimeTxt);
        optionGuidTxt = findViewById(R.id.optionGuidTxt);
        plusBtn = findViewById(R.id.plusBtn);
        closeBtn = findViewById(R.id.closeBtn);

        dialog_alert_title.setText(titleStr);
        plusTimeTxt.setText(plusTimeStr);
        optionGuidTxt.setText(optionGuidStr);

        //모듈 세팅 해주어야 함. 자바 람다식 참고
        plusBtn.setOnClickListener(v -> {
            // 추가버튼 클릭
            this.service3DialogClickListener.onPositiveClick();
            dismiss();
        });
        closeBtn.setOnClickListener(v -> {
            // 취소버튼 클릭
            this.service3DialogClickListener.onNegativeClick();
            dismiss();
        });
        }
}
