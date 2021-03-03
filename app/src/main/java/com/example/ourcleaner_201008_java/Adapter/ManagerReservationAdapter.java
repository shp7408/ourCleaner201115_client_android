package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.DTO.ManagerWaitingDTO;
import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
/* 매니저용 -
매칭대기 서비스 화면에 있는 서비스 목록 모두 보여주는 어댑터
 업무 목록 화면에 있는 서비스 목록 보여주는 어댑터

 둘 다 씀!! */
public class ManagerReservationAdapter extends RecyclerView.Adapter<ManagerReservationAdapter.MyViewHolder> {

    private static final String TAG = "매니저용 예약대기 어댑터";

    ArrayList<ManagerWaitingDTO> managerWaitingDTOArrayList;

    Context mContext;

    //메인에서 뷰 객체를 클릭했을 때
    private ManagerReservationAdapter.OnListItemSelectedInterface mListener;

    private MyViewHolder myViewHolder;

    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }

    public ManagerReservationAdapter(Context context, ArrayList<ManagerWaitingDTO> managerWaitingDTOArrayList, OnListItemSelectedInterface listener) {
        this.mContext = context;
        this.managerWaitingDTOArrayList = managerWaitingDTOArrayList;
        this.mListener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_manager_all_reservation, parent, false); //row xml 파일 연결하는 부분

        Log.d(TAG, "onCreateViewHolder start  :");

       MyViewHolder vh = new ManagerReservationAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "=== 뷰에 데이터 넣기 onBindViewHolder ===" );

        ManagerWaitingDTO managerWaitingDTO = managerWaitingDTOArrayList.get(position);

        holder.reserveTimeTxt.setText(timeIntToHourMin(managerWaitingDTO.getStartTimeInt()) + " 시작");
        holder.reserveDateTxt.setText(managerWaitingDTO.getDateStr() +" "+timeIntToHourMin(managerWaitingDTO.getStartTimeInt()) + " 시작");
        holder.reserveDetailTxt.setText(managerWaitingDTO.getAddressStr()+" ("+managerWaitingDTO.getMyplaceDTO_sizeStr()+")");
//        holder.reserveStateTxt.setText(timeIntToHourMin2(managerWaitingDTO.getNeedTimeInt()));

    }






    @Override
    public int getItemCount() {
        return managerWaitingDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reserveTimeTxt;
        public TextView reserveDateTxt; //장소 이름과 평수 스트링 값이 들어가야 함
        public TextView reserveStateTxt; //주소와 상세주소가 들어가야 함
        public TextView reserveDetailTxt; //주소와 상세주소가 들어가야 함


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //itemView.을 붙이는 이유 : 25번 줄의 v가 부모 v는 아래 뷰홀더의 레이아웃 View 임.(super)

            reserveTimeTxt = itemView.findViewById(R.id.reserveTimeTxt);
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

    //int 형태의 정수를 "3시간 30분" String으로 나타내는 메서드
    public String timeIntToHourMin(int plusTimeInt){

        long hour = TimeUnit.MINUTES.toHours(plusTimeInt); // 분을 시간으로 변경
        Log.d(TAG, "=== hour ===" +hour);

        long minutes = TimeUnit.MINUTES.toMinutes(plusTimeInt) - TimeUnit.HOURS.toMinutes(hour); // 시간으로 변경하고, 나머지 분
        Log.d(TAG, "=== minutes ==="+minutes );

        String plusTimeStr;

        if(hour==0){
            Log.d(TAG, "=== hour==0  ===" );
            plusTimeStr = ""+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else if(minutes==0){
            Log.d(TAG, "=== minutes==0 ===" );
            plusTimeStr = ""+ hour + "시";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }else{
            Log.d(TAG, "=== hour 랑 minutes 둘 다 0이 아닌, 경우 ===" );
            plusTimeStr = ""+ hour +"시 "+ minutes + "분";
            Log.d(TAG, "=== plusTimeStr ===" +plusTimeStr);
        }

        return plusTimeStr;
    }



}
