package com.example.ourcleaner_201008_java.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ourcleaner_201008_java.CircleTransform;
import com.example.ourcleaner_201008_java.DTO.ChatRoomDTO;
import com.example.ourcleaner_201008_java.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/*
* 고객용에서 채팅방 목록 보여주는 어댑터
* */

public class Chat_Room_Adapter extends RecyclerView.Adapter<Chat_Room_Adapter.ViewHolder>{

    private static final String TAG = "Chat_Room_Adapter : ";

    ArrayList<ChatRoomDTO> chatRoomDTOArrayList;
    Context context;

    //메인에서 뷰 객체를 클릭했을 때
    private Chat_Room_Adapter.OnListItemSelectedInterface mListener;

    public Chat_Room_Adapter(ArrayList<ChatRoomDTO> chatRoomDTOArrayList, FragmentActivity activity, Handler handler) {
    }

    //메인 뉴스피드의 각 객체를 클릭했을 때, 아이템 클릭 인터페이스
    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);

    }

    public Chat_Room_Adapter(ArrayList<ChatRoomDTO> chatRoomDTOArrayList, Context context, OnListItemSelectedInterface mListener) {
        this.chatRoomDTOArrayList = chatRoomDTOArrayList;
        this.context = context;
        this.mListener = mListener;
    }

    public Chat_Room_Adapter(ArrayList<ChatRoomDTO> chatRoomDTOArrayList, Context context) {
        this.chatRoomDTOArrayList = chatRoomDTOArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Chat_Room_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_chatlist, parent, false); //row xml 파일 연결하는 부분

        Log.d(TAG, "onCreateViewHolder start  :");

        Chat_Room_Adapter.ViewHolder vh = new Chat_Room_Adapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_Room_Adapter.ViewHolder holder, int position) {

        ChatRoomDTO chatRoomDTO = chatRoomDTOArrayList.get(position);

        Picasso.get()
                .load(chatRoomDTO.getWhomSent3())
                .transform(new CircleTransform())
                .into(holder.whoSendImgView);

        holder.whoSendTxt.setText(chatRoomDTO.getWhomSent2()+" 매니저님");
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

        ImageView whoSendImgView;
        TextView whoSendTxt, whatSendTxt, whenSendTxt, unreadTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            whoSendImgView = itemView.findViewById(R.id.whoSendImgView);

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
