package com.example.asuspc.test2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by asuspc on 2016/7/16.
 */
public class MyinfoFragment extends Fragment implements View.OnClickListener{

    private Button login;
    private View root_view;

    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle saveInstanceState){

        root_view = inflater.inflate(R.layout.home,container,false);
        login = (Button) root_view.findViewById(R.id.button8);

//给button设置响应事件
        View.OnClickListener listener0 = null;
        listener0 = new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent0 = new Intent(getActivity()
                        .getApplicationContext(), LoginActivity.class);
                startActivity(intent0);
            }
        };
        login.setOnClickListener(listener0);


        return root_view;
    }

    @Override
    public void onClick(View v) {

    }
}
