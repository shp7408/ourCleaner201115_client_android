package com.example.ourcleaner_201008_java.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.ourcleaner_201008_java.Ffament_First_Handler;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Fragment.Fragment_First;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


/*라이프 사이클
* onCreate->onCreateDialog-> onCreateView*/
public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    private static final String TAG = "BottomSheetListener";

    NumberPicker numberPicker;

    Button decrement, increment, completeBtn;
    TextView display;

    int currentTimeInt, lastTimeInt;
    Context context;
    Ffament_First_Handler ffament_first_handler;


    public ExampleBottomSheetDialog(Ffament_First_Handler ffament_first_handler){
        this.ffament_first_handler = ffament_first_handler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG+ " 생명", "=== onCreateView ===" );
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

         //numberPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        //NumberPicker numberPicker = new NumberPicker(getContext());
//        numberPicker = new NumberPicker(mcontext);
//
//
//        numberPicker.setMaxValue(15);
//        numberPicker.setMinValue(5);
//        numberPicker.setValue(10);


        decrement=v.findViewById(R.id.decrement);
        increment=v.findViewById(R.id.increment);
        display=v.findViewById(R.id.display);

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== decrement 클릭 ===" );

                String displayStr = display.getText().toString();
                Log.d(TAG, "=== displayStr ==="+ displayStr);

                int num = Integer.valueOf(displayStr);
                Log.d(TAG, "=== num ===" +num);

                num-=1;
                Log.d(TAG, "=== num 1 빼기 완료===" +num);

                display.setText(Integer.toString(num));
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== increment 클릭 ===" );

                String displayStr = display.getText().toString();
                Log.d(TAG, "=== displayStr ==="+ displayStr);

                int num = Integer.valueOf(displayStr);
                Log.d(TAG, "=== num ===" +num);

                num+=1;
                Log.d(TAG, "=== num 1 빼기 완료===" +num);

                display.setText(Integer.toString(num));
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== display 텍스트뷰 ===" );
            }
        });

        display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "=== beforeTextChanged ===start"+start );
                Log.d(TAG, "=== beforeTextChanged ===count"+count );
                Log.d(TAG, "=== beforeTextChanged ===after"+after );

                String displayStr = display.getText().toString();
                Log.d(TAG, "=== displayStr ==="+ displayStr);

                int num = Integer.valueOf(displayStr);
                Log.d(TAG, "=== num ===" +num);

                if(num>5){
                    Log.d(TAG, "=== num 5보다 큼 ==="+num );
                    num=5;
                    display.setText(Integer.toString(num));
                }else if(num<3){
                    Log.d(TAG, "=== num 5보다 큼 ==="+num );
                    num=3;
                    display.setText(Integer.toString(num));
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        completeBtn = v.findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onButtonClicked(10);
                Message msg = new Message();
                msg.what=0;
                msg.arg1=Integer.valueOf(display.getText().toString());
                ffament_first_handler.handleMessage(msg);
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG+ " 생명", "=== onCreate ===" );

        /*다이얼로그 둥근 모서리 위함*/
       setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

        Bundle extra = this.getArguments();
        if(extra != null) {
            extra = getArguments();

            currentTimeInt = extra.getInt("getNeedDefTime");

            Log.e(TAG, "=== getNeedDefTime ==="+currentTimeInt);
        }else{
            Log.e(TAG, "=== getNeedDefTime 비어있음 ===");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG+ " 생명", "=== onResume ===" );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG+ " 생명", "=== onPause ===" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG+ " 생명", "=== onStop ===" );
    }

    public interface BottomSheetListener {
        void onButtonClicked(int getNeedDefTime);
    }

    public interface BottomSheetListener2 {
        void onButtonClicked(int getNeedDefTime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG+ " 생명", "=== onAttach ===" );

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }

    }
}
