package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Citas_Admin_Fragment extends Fragment {

    private LinearLayout verCita;
    private View mainview;
    private PatioVenta patio;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.citas_admin,container,false);
        patio = Patioventainterfaz.patioventa;
        return mainview;
    }

}
