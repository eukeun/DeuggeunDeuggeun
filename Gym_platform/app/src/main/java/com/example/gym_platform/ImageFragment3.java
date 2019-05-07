package com.example.gym_platform;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image3, container, false);

        ImageView imageView = view.findViewById(R.id.imageView3);

        if (getArguments() != null) {
            Bundle args = getArguments();

            imageView.setImageResource(args.getInt("imgRes"));
        }

        return view;
    }
}