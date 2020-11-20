package com.example.ourcleaner_201008_java.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ourcleaner_201008_java.R;
import com.example.ourcleaner_201008_java.View.MainActivity;
import com.example.ourcleaner_201008_java.View.MoreService_Acount_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_Area_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_MyPlace_Activity;
import com.example.ourcleaner_201008_java.View.MoreService_Payment_Activity;
import com.example.ourcleaner_201008_java.View.Service1_TimeActivity;

public class FragmentMore extends Fragment {
    private static final String TAG = "FragmentMore";

    /*프래그먼트에서 메인엑티비티 컨텍스트 필요한 경우*/
    MainActivity mainActivity;

    TextView moreServiceArea, moreMyPayment, moreMyAcount, moreMyPlace;

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
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_more,container,false);

        moreServiceArea = rootview.findViewById(R.id.moreServiceArea);
        moreServiceArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== moreServiceArea버튼 클릭 ===" );

                //있으면 넘어감
                Intent intent = new Intent(getActivity(), MoreService_Area_Activity.class);
                startActivity(intent);

            }
        });

        moreMyPayment = rootview.findViewById(R.id.moreMyPayment);
        moreMyPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== moreMyPayment 버튼 클릭 ===" );

                //있으면 넘어감
                Intent intent = new Intent(getActivity(), MoreService_Payment_Activity.class);
                startActivity(intent);
            }
        });

        moreMyAcount = rootview.findViewById(R.id.moreMyAcount);
        moreMyAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== moreMyAcount 버튼 클릭 ===" );

                //있으면 넘어감
                Intent intent = new Intent(getActivity(), MoreService_Acount_Activity.class);
                startActivity(intent);

            }
        });

        moreMyPlace = rootview.findViewById(R.id.moreMyPlace);
        moreMyPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "=== moreMyPlace 버튼 클릭 ===" );

                //있으면 넘어감
                Intent intent = new Intent(getActivity(), MoreService_MyPlace_Activity.class);
                startActivity(intent);

            }
        });

        return rootview;
    }
}
