package com.prog.kafeecar;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class Catalogo_Vendedor_Fragment extends Fragment implements Adaptador_Lista_Catalogo.RecyclerItemClick, SearchView.OnQueryTextListener {

    private View mainView;

    private LinearLayout verVehiculo;

    private LinearLayout verCatalogo;
    private Adaptador_Lista_Catalogo adptadorlistaview;

    private PatioVenta patio;
    private Vehiculo vMostrar;
    private SearchView busqueda_placa;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.catalogo_vendedor, container, false);
        patio = Patioventainterfaz.patioventa;
        //Layouts
        verCatalogo = mainView.findViewById(R.id.vehiculos_catalogo_vendedor);
        verVehiculo = mainView.findViewById(R.id.visualizar_vehiculo_ca_vn_sv);
        //Search view catalogo
        busqueda_placa = mainView.findViewById(R.id.busqueda_placa_vn_bar);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (verVehiculo.getVisibility() == View.VISIBLE) {
                    irListaCatalogo();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        AutoCompleteTextView filtros = mainView.findViewById(R.id.v_filtros_vn_ddm);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.filtros_vehiculos);
        filtros.setAdapter(adapt);
        filtros.setOnItemClickListener((parent, view, position, id) -> busqueda_placa.setQueryHint(adapt.getItem(position)));

        busqueda_placa.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }

    public void cargar() {
        RecyclerView listaview = mainView.findViewById(R.id.lista_autos_vendedor_rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Catalogo(patio.getVehiculos().copiar(),this);
        listaview.setAdapter(adptadorlistaview);
    }
    public void irListaCatalogo(){
        cargar();
        verVehiculo.setVisibility(View.GONE);
        verCatalogo.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    public void visualizarVehiculoVendedor(){
        Patioventainterfaz.v_aux_cita = vMostrar;
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
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                v_img.setImageBitmap(bitmap);
            });
        }catch (IOException e){
            Toast.makeText(mainView.getContext(), "Error 101: No se pudo cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void itemClick(String placa) {
        try {
            vMostrar = patio.buscarVehiculos("Placa", placa);
            irVer();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error 102: búsqueda fallida del vehículo", Toast.LENGTH_SHORT).show();
        }
    }

    private void irVer() {
        verCatalogo.setVisibility(View.GONE);
        verVehiculo.setVisibility(View.VISIBLE);
        visualizarVehiculoVendedor();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        adptadorlistaview.buscar(newText,busqueda_placa.getQueryHint().toString());
        return false;
    }
}
