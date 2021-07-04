package com.prog.kafeecar;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Catalogo_Vendedor_Fragment extends Fragment implements Adaptador_Lista_Catalogo_vn.RecyclerItemClick {

    private View mainView;

    private ScrollView verVehiculo;

    private LinearLayout verCatalogo;

    private PatioVenta patio;
    private Vehiculo vMostrar;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones
        Button agendarCita_btn = mainView.findViewById(R.id.irAgendarCita_ca_vn_btn);

        //Layouts
        verCatalogo = mainView.findViewById(R.id.vehiculos_catalogo_vendedor);
        verVehiculo = mainView.findViewById(R.id.visualizar_vehiculo_ca_vn_sv);

        agendarCita_btn.setOnClickListener(v -> {
            verCatalogo.setVisibility(View.GONE);
            verVehiculo.setVisibility(View.GONE);
            //aniadir la visibilidad del agendar una cita
        });

        return mainView;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.lista_autos_vendedor_rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        Adaptador_Lista_Catalogo_vn adptadorlistaview = new Adaptador_Lista_Catalogo_vn(patio.getVehiculos(), this);
        listaview.setAdapter(adptadorlistaview);
    }

    @SuppressLint("SetTextI18n")
    public void visualizarVehiculoVendedor(){
        ImageView v_img = mainView.findViewById(R.id.vehiculo_ca_vn_img);
        TextView titulo = mainView.findViewById(R.id.auto_titulo_ca_vn_txt);
        TextView placa = mainView.findViewById(R.id.ver_placa_ca_vn_txt);
        TextView matricula = mainView.findViewById(R.id.ver_matricula_ca_vn_txt);
        TextView matriculado = mainView.findViewById(R.id.ver_matriculado_ca_vn_txt);
        TextView marca = mainView.findViewById(R.id.ver_marca_ca_vn_txt);
        TextView modelo = mainView.findViewById(R.id.ver_modelo_ca_vn_txt);
        TextView anio = mainView.findViewById(R.id.ver_anio_ca_vn_txt);
        TextView color = mainView.findViewById(R.id.ver_color_ca_vn_txt);
        TextView precioInicial = mainView.findViewById(R.id.ver_pinicial_ca_vn_txt);
        TextView preciVenta = mainView.findViewById(R.id.ver_pventa_ca_vn_txt);
        TextView promocion = mainView.findViewById(R.id.ver_promocion_ca_vn_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descipcion_ca_vn_txt);

        String titulo_str = vMostrar.getMarca()+" "+vMostrar.getModelo();
        titulo.setText(titulo_str);
        placa.setText(vMostrar.getPlaca());
        matricula.setText(vMostrar.getMatricula());
        if(vMostrar.isMatriculado()){
            matriculado.setText("Si");
        }else{
            matriculado.setText("No");
        }
        marca.setText(vMostrar.getMarca());
        modelo.setText(vMostrar.getModelo());
        anio.setText(String.valueOf(vMostrar.getAnio()));
        color.setText(vMostrar.getColor());
        precioInicial.setText(String.valueOf(vMostrar.getPrecioInicial()));
        preciVenta.setText(String.valueOf(vMostrar.getPrecioVenta()));
        promocion.setText(String.valueOf(vMostrar.getPromocion()));
        descripcion.setText(vMostrar.getDescripcion());

        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());
        Glide.with(mainView)
                .load(filePath)
                .into(v_img);
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                v_img.setImageBitmap(bitmap);
            });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void itemClick(String placa) {
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            irVer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void irVer() {
        verCatalogo.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.VISIBLE);
        visualizarVehiculoVendedor();
    }
}
