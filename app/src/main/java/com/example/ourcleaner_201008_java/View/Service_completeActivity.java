package com.example.ourcleaner_201008_java.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.R;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Service_completeActivity extends AppCompatActivity {

    private static final String TAG = "서비스신청 완료 엑티비티";

    /* 현재 엑티비티의 정보 담는 객체 */
    ServiceDTO serviceDTO3, serviceDTO2server; //이전 엑티비티에서 객체 받아오기 위한 객체, 다음 엑티비티로 객체 보내기 위한 객체

    //숫자 천 자리에 콤마 찍기
    DecimalFormat formatter = new DecimalFormat("###,###");

    int needDefTime, needDefCost; //평 수에 따라 책정된 시간(분), 가격

    int ironCostInt, fridgeCostInt;

    TextView serReqTxt, serTimeTxt, serPlaceTxt, serManagerTxt, paymentTxt;
    String serReqStr;
    Button paymentBtn, reserveBtn;
    LinearLayout priceDefLayout, priceIronLayout, pricefridgeLayout, priceResultLayout;
    TextView priceDefTxt, priceDefNumTxt, priceIronTxt, priceIronNumTxt, pricefridgeTxt, pricefridgeNumTxt, priceResultTxt, priceResutNumTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_complete);
        Log.d(TAG, "=== onCreate ===" );

        //intent로 받을 때 사용하는 코드
        Intent intent = getIntent();
        Log.d(TAG, "Service1_TimeActivity에서 인텐트 받음 intent :"+intent);

        serviceDTO3 = (ServiceDTO) intent.getSerializableExtra("serviceDTO3");
        Log.d(TAG, "=== serviceDTO3 ===" + serviceDTO3.getCurrentUser() );

        serReqTxt = findViewById(R.id.serReqTxt);
        serReqTxt.setText(serviceDTO3.getVisitDate()+"("+serviceDTO3.getVisitDay()+")"+"\n예약을 완료해주세요.");
        Log.d(TAG, "=== serReqTxt ===" + serReqTxt.getText());

        serTimeTxt = findViewById(R.id.serTimeTxt);
        serTimeTxt.setText(serviceDTO3.getVisitDate()+"("+serviceDTO3.getVisitDay()+") / "+timeIntToHourMin1(serviceDTO3.getStartTime()) +" / "+timeIntToHourMin2(serviceDTO3.getNeedDefTime()));

        serPlaceTxt = findViewById(R.id.serPlaceTxt);
        serPlaceTxt.setText(serviceDTO3.getMyplaceDTO().getAddress()+" "+serviceDTO3.getMyplaceDTO().getDetailAddress()+" ("+serviceDTO3.getMyplaceDTO().getSizeStr()+")");

        serManagerTxt = findViewById(R.id.serManagerTxt);
        paymentTxt = findViewById(R.id.paymentTxt);

        paymentBtn = findViewById(R.id.paymentBtn);
        reserveBtn = findViewById(R.id.reserveBtn);

        priceDefLayout = findViewById(R.id.priceDefLayout);
        priceIronLayout = findViewById(R.id.priceIronLayout);
        pricefridgeLayout = findViewById(R.id.pricefridgeLayout);
        priceResultLayout = findViewById(R.id.priceResultLayout);

        // 선택한 장소의 평 수에 따라서 책정한 기본 가격 세팅
        priceDefNumTxt = findViewById(R.id.priceDefNumTxt);
        if(serviceDTO3.getMyplaceDTO().getSizeIndexint()>=0 && serviceDTO3.getMyplaceDTO().getSizeIndexint()<=4){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 8평이하 ~ 12평 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 0 ~ 인덱스 4 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 210;
            needDefCost = 45000;

        }else if(serviceDTO3.getMyplaceDTO().getSizeIndexint()>=5 && serviceDTO3.getMyplaceDTO().getSizeIndexint()<=38){
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 13평 ~ 50평 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 5 ~ 인덱스 38 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 270;
            needDefCost = 55000;

        }else{
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수가 51평 ~ 101평 이상 까지. ==="+ serviceDTO3.getMyplaceDTO().getSizeStr());
            Log.d(TAG, "=== 리사이클러 뷰 클릭 -> 평 수 인덱스 39 ~ 인덱스 93 ==="+ serviceDTO3.getMyplaceDTO().getSizeIndexint());

            needDefTime = 330;
            needDefCost = 65000;
        }

        priceDefNumTxt.setText(formatter.format(needDefCost)+" 원");

        // 선택한 장소의 유료 선택지에 따라서 레이아웃 보이게 함.
        // 기본 가격
        priceIronNumTxt = findViewById(R.id.priceIronNumTxt);
        pricefridgeNumTxt = findViewById(R.id.pricefridgeNumTxt);



        if((Boolean) serviceDTO3.getServiceplus().get("다림질")){
            ironCostInt = 6600;

            priceIronNumTxt.setText(formatter.format(ironCostInt)+" 원");
            priceIronLayout.setVisibility(View.VISIBLE);
        }
        if((Boolean) serviceDTO3.getServiceplus().get("냉장고")){
            fridgeCostInt = 26400;

            pricefridgeNumTxt.setText(formatter.format(fridgeCostInt)+" 원");
            pricefridgeLayout.setVisibility(View.VISIBLE);
        }

        priceResutNumTxt = findViewById(R.id.priceResutNumTxt);

        priceResutNumTxt.setText(formatter.format(needDefCost+ironCostInt+fridgeCostInt)+" 원");




    }

    //int 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin1(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr =  minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = hour + "시 ";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = hour +"시  "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }

    public String timeIntToHourMin2(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        //이거 추가 해야 함.
        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr =  minutes + "분";
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

}