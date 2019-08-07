package com.speshfood.womscarrier;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public
class orderFragment extends Fragment {
    CircleImageView image;
    TextView textView;
    Button booknow;
    @Nullable
    @Override
    public
    View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate (R.layout.fragment_order,container,false);

        image=rootView.findViewById(R.id.circleView);
        textView=rootView.findViewById(R.id.textview);
        booknow=rootView.findViewById(R.id.booknow);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
