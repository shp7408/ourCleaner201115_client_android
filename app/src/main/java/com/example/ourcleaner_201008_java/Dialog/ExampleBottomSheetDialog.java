package com.example.ourcleaner_201008_java.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.ourcleaner_201008_java.Ffament_First_Handler;
import com.example.ourcleaner_201008_java.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.concurrent.TimeUnit;


/*라이프 사이클
* onCreate->onCreateDialog-> onCreateView*/
public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;

    private static final String TAG = "BottomSheetListener";

    Button decrement, increment, completeBtn;
    TextView display;

    /* 뷰페이저 fragment first에서 받아온 데이터 */
    int getNeedDefTime, getNeedDefCost, getSizeIndexint, time, cost; // 뒤에 두개는 기준이 되는 부분
    String getSizeStr, nowTimeStr;

    Context context;
    Ffament_First_Handler ffament_first_handler;

    public interface BottomSheetListener {
        void onButtonClicked(int getNeedDefTime);
    }

    public ExampleBottomSheetDialog(Ffament_First_Handler ffament_first_handler){
        this.ffament_first_handler = ffament_first_handler;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG+ " 생명", "=== onCreateView ===" );
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);


        decrement=v.findViewById(R.id.decrement);
        increment=v.findViewById(R.id.increment);
        display=v.findViewById(R.id.display);

        if(getSizeIndexint >= 0 && getSizeIndexint <= 4){
            Log.d(TAG, "=== 평 수가 8평이하 ~ 12평 까지. ==="+ getSizeStr);
            Log.d(TAG, "=== 평 수 인덱스 0 ~ 인덱스 4 ==="+ getSizeIndexint);
            time = 180;
            cost = 30000;
        }else if(getSizeIndexint >= 5 && getSizeIndexint <= 38){
            Log.d(TAG, "=== 평 수가 13평 ~ 50평 까지. ==="+ getSizeStr);
            Log.d(TAG, "=== 평 수 인덱스 5 ~ 인덱스 38 ==="+ getSizeIndexint);
            time = 240;
            cost = 42000;
        }else{
            Log.d(TAG, "=== 평 수가 51평 ~ 101평 이상 까지. ==="+ getSizeStr);
            Log.d(TAG, "=== 평 수 인덱스 39 ~ 인덱스 93 ==="+ getSizeIndexint);
            time = 300;
            cost = 54000;
        }

        if((Integer.parseInt(nowTimeStr)*60) == time){
            Log.d(TAG, "=== nowTimeStr === 같음 추천하기 " +nowTimeStr );
            Log.d(TAG, "=== time ===" +time);
            display.setText(nowTimeStr+" (추천)");
        }else{
            Log.d(TAG, "=== nowTimeStr === 다름" +nowTimeStr );
            Log.d(TAG, "=== time ===" +time);
            display.setText(nowTimeStr);
        }



        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== decrement 클릭 ===" );

                String displayStr = display.getText().toString().substring(0,1);;
                Log.d(TAG, "=== displayStr ==="+ displayStr);

                int num = Integer.valueOf(displayStr);
                Log.d(TAG, "=== num ===" +num);

                if(num<4){
                    Toast.makeText(getActivity(), "3시간 이상 예약 가능합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    num-=1;
                    Log.d(TAG, "=== num 1 빼기 완료===" +num);

                    display.setText(Integer.toString(num));
                }
            }
        });

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== increment 클릭 ===" );

                String displayStr = display.getText().toString().substring(0,1);
                Log.d(TAG, "=== displayStr ==="+ displayStr);

                int num = Integer.valueOf(displayStr);
                Log.d(TAG, "=== num ===" +num);

                if(num>4){
                    Toast.makeText(getActivity(), "5시간 이하로 예약 가능합니다.", Toast.LENGTH_SHORT).show();

                }else{
                    num+=1;
                    Log.d(TAG, "=== num 1 빼기 완료 ===" +num);

                    display.setText(Integer.toString(num));
                }


            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=== display 텍스트뷰 ===" );
            }
        });



        completeBtn = v.findViewById(R.id.completeBtn);
        completeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onButtonClicked(Integer.valueOf(display.getText().toString().substring(0,1)));


                Message msg = new Message();
                msg.what=0;

                /* fragment first 프래그먼트로 보내는 부분 */
                msg.arg1=Integer.valueOf(display.getText().toString().substring(0,1));
                ffament_first_handler.handleMessage(msg);
                dismiss();
            }
        });

        return v;
    }


    /* fragment first 부분에서 데이터 받는 부분임 */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG+ " 생명", "=== onCreate ===" );

        /*다이얼로그 둥근 모서리 위함*/
       setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);

        Bundle extra = this.getArguments();
        if(extra != null) {

            extra = getArguments();

            nowTimeStr = extra.getString("nowTimeStr");
            getNeedDefTime = extra.getInt("getNeedDefTime");
            getNeedDefCost = extra.getInt("getNeedDefCost");
            getSizeIndexint = extra.getInt("getSizeIndexint");
            getSizeStr = extra.getString("getSizeStr");

            Log.e(TAG, "=== getSizeStr ==="+getSizeStr);
            Log.e(TAG, "=== getNeedDefTime ==="+getNeedDefTime);
            Log.e(TAG, "=== getNeedDefCost ==="+getNeedDefCost);
            Log.e(TAG, "=== getSizeIndexint ==="+getSizeIndexint);
            Log.e(TAG, "=== getSizeStr ==="+getSizeStr);

        }else{
            Log.e(TAG, "=== getNeedDefTime 비어있음 ===");
        }

    }





    //int 형태의 정수 -> "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = hour + "시간";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시간 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
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
