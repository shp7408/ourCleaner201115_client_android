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

import com.android.volley.Response;
import com.example.ourcleaner_201008_java.DTO.MyReservationDTO;
import com.example.ourcleaner_201008_java.DTO.MyplaceDTO;
import com.example.ourcleaner_201008_java.DTO.ServiceDTO;
import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.Fragment.FragmentReservation;
import com.example.ourcleaner_201008_java.View.MainActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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

        holder.reserveDateTxt.setText(myReservationDTO.getServiceDate());
        holder.reserveStateTxt.setText(myReservationDTO.getServiceState());
//        holder.reserveDetailTxt.setText(myReservationDTO.getPlaceName() + " " + myReservationDTO.getServiceTime());
        holder.reserveDetailTxt.setText(myReservationDTO.getServiceTime());

        Log.e(TAG, "=== getServiceDate ===" +myReservationDTO.getServiceDate());
        Log.e(TAG, "=== getServiceDate ===" +myReservationDTO.getServiceState());
        Log.e(TAG, "=== getServiceDate ===" +myReservationDTO.getPlaceName() + " " + myReservationDTO.getServiceTime());
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


}

