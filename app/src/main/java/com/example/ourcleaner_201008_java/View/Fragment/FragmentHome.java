package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.MainActivity;
import com.example.ourcleaner_201008_java.View.Service1_TimeActivity;

//public class FragmentHome extends Fragment implements View.OnClickListener{
public class FragmentHome extends Fragment {
private static final String TAG = "FragmentHome";

    /*프래그먼트에서 메인엑티비티 컨텍스트 필요한 경우*/
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_home,container,false);

        Button reserve_one_btn = (Button)rootview.findViewById(R.id.reserve_one_btn);
        reserve_one_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== 1회 예약하기 버튼 클릭!!!!! ===" );

                Intent intent = new Intent(getActivity(), Service1_TimeActivity.class);

                intent.putExtra("serviceRegularStr","no");

                startActivity(intent);

            }
        });

        Button reserve_re_btn = (Button)rootview.findViewById(R.id.reserve_re_btn);
        reserve_re_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== 정기 예약하기 버튼 클릭!!!!! ===" );

                Intent intent = new Intent(getActivity(), Service1_TimeActivity.class);

                intent.putExtra("serviceRegularStr","yes");

                startActivity(intent);
            }
        });

        return rootview;


    }


//    @Override
//    public void onClick(View view) {
//        //UserManagement API 요청을 담당
//        UserManagement.getInstance()
//                //requestLogout : 로그아웃 요청
//                //파라미터 : logout 요청 결과에 대한 callback
//                .requestLogout(new LogoutResponseCallback() {
//                    @Override
//                    public void onCompleteLogout() {
//                        Log.d(TAG, "=== onCompleteLogout : 로그아웃 되었습니다. ===");
//                        //Toast.makeText(MainActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
//
//                        Intent intent = new Intent(getContext(), LoginActivity.class);
//                        startActivity(intent);
//                    }
//                });
//    }


}

