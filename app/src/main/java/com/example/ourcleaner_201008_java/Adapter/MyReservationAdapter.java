package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.DTO.MyReservationDTO;
import com.example.ourcleaner_201008_java.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.example.ourcleaner_201008_java.R.drawable.background2;

/* 고객용 - 내예약 목록 보는 어댑터 */
public class MyReservationAdapter extends RecyclerView.Adapter<MyReservationAdapter.Viewholder> {

    private static final String TAG = "나의예약 어댑터";
    ArrayList<MyReservationDTO> reservationDTOArrayList; //리사이클러 뷰에 넣을 데이터 객체 리스트
    Context mContext;

    //메인에서 뷰 객체를 클릭했을 때
    private OnListItemSelectedInterface mListener;

    public interface OnListItemSelectedInterface{
        void onItemSelected(View v, int position);
    }

//    public MyReservationAdapter(ArrayList<MyReservationDTO> myReservationDTOArrayList) {
//        this.reservationDTOArrayList = myReservationDTOArrayList;
//
//    }
    //클릭 리스너 구현 시 필요한 어댑터 생성자
    public MyReservationAdapter(Context context, ArrayList<MyReservationDTO> myReservationDTOArrayList, OnListItemSelectedInterface listener){
        this.mContext = context;
        this.mListener = listener;
        this.reservationDTOArrayList = myReservationDTOArrayList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_myreservation, parent, false); //row xml 파일 연결하는 부분

        Log.d(TAG, "onCreateViewHolder start  :");
        Viewholder vh = new Viewholder(v);
        return vh;
    }

    // 뷰에 데이터 넣기
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Log.d(TAG, "=== 뷰에 데이터 넣기 onBindViewHolder ===");
        MyReservationDTO myReservationDTO = reservationDTOArrayList.get(position);

        holder.reserveDateTxt.setText(myReservationDTO.getServiceDate()+ " "+myReservationDTO.getServiceTime());
        Log.d(TAG, "=== getServiceDate ===" +myReservationDTO.getServiceDate());

        if(myReservationDTO.getServiceState().equals("매칭 대기 중")) {
            Log.e(TAG, "=== 매칭 대기 중 인 경우, 회색 배경으로 변경 ===" );
            Log.e(TAG, "=== getServiceDate ===" +myReservationDTO.getServiceState());


            holder.reserveStateTxt.setText(myReservationDTO.getServiceState());
            holder.reserveStateTxt.setBackgroundResource(background2);

            /* 글자 색 변경하는 코드 */
            int blackColcor = ContextCompat.getColor(mContext, R.color.black);
            holder.reserveStateTxt.setTextColor(blackColcor);


        } else if(myReservationDTO.getServiceState().equals("매칭 완료")){
            Log.e(TAG, "=== 매칭 완료인 경우, 검은색 배경 그대로 진행 서비스까지 남은 날짜 / 매니저 이름 보여주기 ===" );
            Log.e(TAG, "=== onBindViewHolder getServiceDate ===" +myReservationDTO.getServiceDate());

            holder.reserveStateTxt.setText("청소 전");



        } else if(myReservationDTO.getServiceState().equals("예약 취소")){
            Log.e(TAG, "=== 예약 취소인 경우, 회색 배경 그대로 진행 서비스까지 남은 날짜 / 매니저 이름 보여주기 ===" );
            Log.e(TAG, "=== getServiceDate ===" +myReservationDTO.getServiceState());

            holder.reserveDateTxt.setText(myReservationDTO.getServiceDate()+" 예약 취소");

            holder.reserveStateTxt.setText(myReservationDTO.getServiceState());
            holder.reserveStateTxt.setBackgroundResource(background2);

            /* 글자 색 변경하는 코드 */
            int greyColcor = ContextCompat.getColor(mContext, R.color.grey2);
            holder.reserveStateTxt.setTextColor(greyColcor);



        } else if(myReservationDTO.getServiceState().equals("이동 중")){
            Log.e(TAG, "=== 이동 중인 경우, 검은색 배경 그대로 진행 ===" );

            holder.reserveStateTxt.setText("이동 중");

            // TODO: 2020-12-18 시연 당일 날짜 확인해야 함!!



        } else if(myReservationDTO.getServiceState().equals("청소 중")){
            Log.e(TAG, "=== 청소 중인 경우, 검은색 배경 그대로 진행 ===" );

            holder.reserveStateTxt.setText("청소 중");

        } else if(myReservationDTO.getServiceState().equals("청소 완료")){
            Log.e(TAG, "=== 청소 완료인 경우, 검은색 배경 그대로 진행 ===" );

            holder.reserveStateTxt.setText("청소 완료");

        }


        holder.reserveDetailTxt.setText(myReservationDTO.getPlaceName());

    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView reserveDateTxt;
        public TextView reserveStateTxt;
        public TextView reserveDetailTxt;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //itemView.을 붙이는 이유 : 25번 줄의 v가 부모 v는 아래 뷰홀더의 레이아웃 View 임.(super)
            reserveDateTxt = itemView.findViewById(R.id.reserveDateTxt);
            reserveStateTxt = itemView.findViewById(R.id.reserveStateTxt);
            reserveDetailTxt = itemView.findViewById(R.id.reserveDetailTxt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    mListener.onItemSelected(view,getAdapterPosition());
                    Log.d(TAG, "position  :"+ getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "=== serviceDTOArrayList.size() ===" + reservationDTOArrayList.size());
        return reservationDTOArrayList.size(); // 리사이클러 뷰의 전체 크기를 설정하는 부분
    }


    /* 12.30 형태로 두 날짜 사이의 차이 구하는 메서드 // 에러 시, 0을 return*/
    public long calDateBetweenAandB(String a, String b)
    {
        // TODO: 2020-12-18 시연할 때, 날짜 2021년 넘어가면 바꿔야 함...ㅎㅎ
        //입력할 때, 12.15 이런 식임

        String nowYear1 = "2020";
        String month1 = a.substring(0,2); //12월
        String date1 = a.substring(3); // 20일
        Log.e(TAG, "=== month1 ===" +month1 );
        Log.e(TAG, "=== date1 ===" +date1 );

        String nowYear2 = "2020";
        String month2 = b.substring(0,2); //12월
        String date2 = b.substring(3); // 20일
        Log.e(TAG, "=== month2 ===" +month2 );
        Log.e(TAG, "=== date2 ===" +date2 );

        try{
            String dateAll1 = nowYear1+"-"+month1+"-"+date1;
            String dateAll2 = nowYear2+"-"+month2+"-"+date2;
            Log.e(TAG, "=== calDateBetweenAandB === dateAll1 " +dateAll1);
            Log.e(TAG, "=== calDateBetweenAandB === dateAll2 " +dateAll2);

            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

            Date firstDate = sdf.parse(dateAll1);
            Date secondDate = sdf.parse(dateAll2);

            long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            System.out.println(String.format("A %s , B %s Diff %s Days", a, b, diff));
            Log.e(TAG, "=== calDateBetweenAandB ===" +String.format("A %s , B %s Diff %s Days", a, b, diff));
            Log.e(TAG, "=== calDateBetweenAandB diff ===" + "D - "+diff);

            /* 1일 추가함 */
            return diff+1;

        }catch (Exception e){
            Log.d(TAG, "=== calDateBetweenAandB 에러코드 ===" +e  );
        }
        return 0;
    }


    /* 오늘부터 12.30일 형태의 날짜 사이 차이를 구하는 메서드. // 에러 시, 0을 return */
    public long calDateBetweenNowandB(String b)
    {
        // TODO: 2020-12-18 시연할 때, 날짜 2021년 넘어가면 바꿔야 함...ㅎㅎ
        //입력할 때, 12.15 이런 식임

        String nowYear2 = "2021";
        String month2 = b.substring(0,2); //12월
        String date2 = b.substring(3); // 20일
        Log.e(TAG, "=== month2 ===" +month2 );
        Log.e(TAG, "=== date2 ===" +date2 );

        try{

            String dateAll2 = nowYear2+"-"+month2+"-"+date2;
            Log.e(TAG, "=== calDateBetweenAandB === dateAll2 " +dateAll2);

            String format = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);

            Date nowDate = new Date();
            Date secondDate = sdf.parse(dateAll2);

            long diffInMillies = Math.abs(secondDate.getTime() - nowDate.getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
//            System.out.println(String.format("A %s , B %s Diff %s Days", a, b, diff));
            Log.e(TAG, "=== calDateBetweenAandB ===" +String.format("A %s , B %s Diff %s Days", nowDate.getTime(), b, diff));
            Log.e(TAG, "=== calDateBetweenAandB diff ===" + "D - "+diff);

            if(diff>0){
                diff= diff+1;
            }

            /* 1일 추가함 */
            return diff;

        }catch (Exception e){
            Log.d(TAG, "=== calDateBetweenAandB 에러코드 ===" +e  );
        }
        return 0;
    }

}

