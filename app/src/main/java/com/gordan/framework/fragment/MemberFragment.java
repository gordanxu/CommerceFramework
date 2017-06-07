package com.gordan.framework.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gordan.framework.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {


    public MemberFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member, container, false);
    }


}
