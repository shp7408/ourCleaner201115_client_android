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

import com.example.ourcleaner_201008_java.DTO.ChatRoomDTO;
import com.example.ourcleaner_201008_java.R;

import java.util.ArrayList;

/*
* 매니저용 채팅방 목록 보여주는 어댑터
*  */
public class Manager_Chat_Room_Adapter extends RecyclerView.Adapter<Manager_Chat_Room_Adapter.ViewHolder>{

    private static final String TAG = "매니저용 채팅방 목록 보여주는 어댑터 ";

    ArrayList<ChatRoomDTO> chatRoomDTOArrayList;
    Context context;

    //메인에서 뷰 객체를 클릭했을 때
    private Manager_Chat_Room_Adapter.OnListItemSelectedInterface mListener;

    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }

    public Manager_Chat_Room_Adapter(ArrayList<ChatRoomDTO> chatRoomDTOArrayList, Context context) {
        this.chatRoomDTOArrayList = chatRoomDTOArrayList;
        this.context = context;
    }

    public Manager_Chat_Room_Adapter(ArrayList<ChatRoomDTO> chatRoomDTOArrayList, Context context, OnListItemSelectedInterface mListener) {
        this.chatRoomDTOArrayList = chatRoomDTOArrayList;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Manager_Chat_Room_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_manager_chatlist, parent, false); //row xml 파일 연결하는 부분

        Log.d(TAG, "onCreateViewHolder start  :");

        Manager_Chat_Room_Adapter.ViewHolder vh = new Manager_Chat_Room_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Manager_Chat_Room_Adapter.ViewHolder holder, int position) {

        ChatRoomDTO chatRoomDTO = chatRoomDTOArrayList.get(position);

        holder.whoSendTxt.setText(chatRoomDTO.getWhomSent2()+" 고객님");
        holder.whenSendTxt.setText(chatRoomDTO.getWhenSend());

        if(chatRoomDTO.getUnreadNum()>0){
            holder.unreadTxt.setVisibility(View.VISIBLE);
            holder.unreadTxt.setText(Integer.toString(chatRoomDTO.getUnreadNum()));
        }else{
            holder.unreadTxt.setVisibility(View.GONE);
        }

        if(chatRoomDTO.getLastMessage().contains("http://52.79.179.66/uploads")){
            holder.whatSendTxt.setText("사진을 전송하였습니다.");
        }else{
            holder.whatSendTxt.setText(chatRoomDTO.getLastMessage());
        }

    }

    @Override
    public int getItemCount() {
        return chatRoomDTOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView whoSendTxt, whatSendTxt, whenSendTxt, unreadTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            whoSendTxt = itemView.findViewById(R.id.whoSendTxt);
            whatSendTxt = itemView.findViewById(R.id.whatSendTxt);
            whenSendTxt = itemView.findViewById(R.id.whenSendTxt);
            unreadTxt = itemView.findViewById(R.id.unreadTxt);

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




}
