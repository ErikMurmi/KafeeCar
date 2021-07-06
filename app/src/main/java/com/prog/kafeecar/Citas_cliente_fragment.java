package com.prog.kafeecar;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Citas_cliente_fragment extends Fragment {

    private View mainview;

    private Button deacartarnuevacita;
    private Button guardarcitanueva;
    private Button guardarcitaeditada;
    private Button descartareditarcita;
    private Button regresar;
    private Button actualizarvercita;
    private Button buscarCitaFecha;


    private ScrollView vercita;
    private ScrollView catalogoAutosCliente;

    private LinearLayout editarcita;
    private LinearLayout nuevacita;

    private PatioVenta patio;
    private Vehiculo vMostrar;
    private Patioventainterfaz PatioIterfaz = new Patioventainterfaz();

    private RecyclerView listaview;

    private Adaptador_Lista_Cliente_Cita adptadorlistaview;



    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Catalogo_Cliente_fragment catplaca;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.lista_citas, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones aniadir cita nueva
        deacartarnuevacita=mainview.findViewById(R.id.descartar_NC_btn);
        guardarcitanueva = mainview.findViewById(R.id.guardar_clita_nueva_NC_btn);
        //Botones actualizar cita cuando se edita
        guardarcitaeditada=mainview.findViewById(R.id.guardar_cita_nueva_EC_btn);
        descartareditarcita = mainview.findViewById(R.id.descartar_EC_btn);

        //boton de ver cita
        regresar = mainview.findViewById(R.id.regresar_cita_VC_btn);
        actualizarvercita=mainview.findViewById(R.id.actualizar_cita_VC_btn);
        //boton de Lista Cita
        buscarCitaFecha=mainview.findViewById(R.id.buscar_listacita_btn);
        //Botton que entre a cita grande
        //layouts/Scroll
        catalogoAutosCliente=mainview.findViewById(R.id.catalogoautos_cliente_scl);
        vercita = mainview.findViewById(R.id.ver_cita_Scroll);

        editarcita = mainview.findViewById(R.id.editar_cita_linear);

        nuevacita=mainview.findViewById(R.id.nueva_cita_cliente_lay);


        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        buscarCitaFecha.setOnClickListener(v ->{
            try{
                buscarPorFecha();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        guardarcitanueva.setOnClickListener(v ->{
            try {
                citaNueva(vMostrar.getPlaca());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        deacartarnuevacita.setOnClickListener(v -> {
            mainview.setVisibility(View.GONE);
            catalogoAutosCliente.setVisibility(View.VISIBLE);
        });
        guardarcitaeditada.setOnClickListener(v ->{
            try {
                modificarCita();
                mainview.setVisibility(View.GONE);
                catalogoAutosCliente.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        descartareditarcita.setOnClickListener(v -> {
            mainview.setVisibility(View.VISIBLE);
            vercita.setVisibility(View.GONE);
        });

        vercita.setOnClickListener(v -> {
            try {
                visualizarCitacliente();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        descartareditarcita.setOnClickListener(v -> {
            mainview.setVisibility(View.VISIBLE);
            vercita.setVisibility(View.GONE);
        });

        actualizarvercita.setOnClickListener(v -> {
            mainview.setVisibility(View.GONE);
            editarcita.setVisibility(View.VISIBLE);
        });

        regresar.setOnClickListener(v -> {
            mainview.setVisibility(View.VISIBLE);
            editarcita.setVisibility(View.GONE);
        });

        return mainview;
    }


    public void visualizarCitacliente() throws Exception {
        TextView textodia;
        TextView textomes;
        TextView textoanio;
        TextView textohoras;
        TextView textovendedor;
        TextView textovehiculo;
        TextView textodescripcion;


        textodia = mainview.findViewById(R.id.dia_cita_VC_etxt);
        textomes = mainview.findViewById(R.id.mes_cita_VC_etxt);
        textoanio = mainview.findViewById(R.id.anio_cita_VC_etxt);
        textohoras = mainview.findViewById(R.id.hora_cita_VC_etxt);
        textovendedor = mainview.findViewById(R.id.nomvendedor_cita_VC_etxt);
        textovehiculo = mainview.findViewById(R.id.vehiculo_cita_VC_etxt);
        textodescripcion = mainview.findViewById(R.id.descripcion_cita_VC_etxt);
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = patio.buscarCitas("cliente", cliente.getCedula(), cliente.getCedula());
        String fecha = Patioventainterfaz.getFechaMod(cita.getFechaCita());
        String dia = fecha.split("-")[2];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[0];
        textohoras.setText(cita.getHora());
        textovehiculo.setText(cita.getVehiculo().toString());
        textodescripcion.setText(cita.getVehiculo().getDescripcion());
        textovendedor.setText(cita.getVendedorCita().getNombre());
        textoanio.setText(anio);
        textomes.setText(mes);
        textodia.setText(dia);

    }

    public void citaNueva(String placa) throws Exception {
        int c = 0;
        EditText textodia= mainview.findViewById(R.id.dia_cita_nueva_etxt);
        EditText textomes = mainview.findViewById(R.id.mes_cita_nueva_etxt);
        EditText textoanio = mainview.findViewById(R.id.anio_cita_nueva_etxt);
        EditText textohoras = mainview.findViewById(R.id.hora_cita_nueva_cl_etxt);
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        String fechacita_str = "";

        int hora = -1;
        if ((!isEmpty(textodia) && !isEmpty(textomes)) && (!isEmpty(textoanio) && !isEmpty(textohoras))) {
            fechacita_str = textodia.getText().toString() + "-" + textomes.getText().toString() + "-" + textoanio.getText().toString();
            hora = Integer.parseInt(textohoras.getText().toString());
        } else {
            c++;
            Toast.makeText(mainview.getContext(), "Campos de fecha vacíos", Toast.LENGTH_SHORT).show();
        }


        Vehiculo m_vehiculo = patio.buscarVehiculos("Placa",vMostrar.getPlaca());

        try {
            vMostrar = patio.buscarVehiculos("Placa",placa);
            m_vehiculo = vMostrar;
        } catch (Exception e) {
            Toast t= Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG);
            t.show();
        }
        //Cargar imagen
        StorageReference filePath = mStorageRef.child("Vehiculos/"+vMostrar.getimagen());
        ImageView vehiculo_img = mainview.findViewById(R.id.imagen_auto_cita_NC_img);
        textodia = mainview.findViewById(R.id.dia_cita_nueva_etxt);
        String dia_str = textodia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if (dia < 1 || dia > 30) {
            Toast.makeText(mainview.getContext(), "Dia invalido", Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        textomes = mainview.findViewById(R.id.mes_cita_nueva_etxt);
        String mes_str = textomes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if (mes < 1 || mes > 12) {
            Toast.makeText(mainview.getContext(), "mes invalido", Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        textoanio = mainview.findViewById(R.id.anio_cita_nueva_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if (anio < 2021 || anio > 2022) {
            Toast.makeText(mainview.getContext(), "año invalido", Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        textohoras = mainview.findViewById(R.id.hora_cita_nueva_cl_etxt);
        String horas_str = textohoras.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if (horas < 0 || horas > 24) {
            Toast.makeText(mainview.getContext(), "horas invalidas", Toast.LENGTH_SHORT).show();
            textohoras.setText("");
            c++;
        }
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(),"jpg");
            filePath.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    vehiculo_img.setImageBitmap(bitmap);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }



        String resolucion_str = " ";

        if (c != 0) {

            Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            String h = horas_str;
            int entero = Integer.parseInt(h);

            Vendedor ve = patio.buscarVendedores("cedula", "1800370809");
            Vehiculo v = patio.buscarVehiculos("matricula", "A001");
            Cliente cliente = patio.buscarClientes("cedula", "1752866291");
            Cita aux = new Cita(fecha, entero, resolucion_str, cliente, ve, v);
            patio.aniadirCita(aux);
        }


    }

    public void modificarCita() throws Exception
    {
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textohoras;
        EditText textoplaca;

        int c=0;

        textoplaca = mainview.findViewById(R.id.placa_etxt);
        String placa_str = textoplaca.getText().toString();

        textodia = mainview.findViewById(R.id.dia_cita_nueva_etxt);
        String dia_str = textodia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if(dia<1||dia>30)
        {
            Toast.makeText(mainview.getContext(),"Dia invalido",Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        textomes = mainview.findViewById(R.id.mes_cita_nueva_etxt);
        String mes_str = textomes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if(mes<1||mes>12)
        {
            Toast.makeText(mainview.getContext(),"mes invalido",Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        textoanio = mainview.findViewById(R.id.anio_cita_nueva_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if(anio<2021||anio>2022)
        {
            Toast.makeText(mainview.getContext(),"año invalido",Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        textohoras = mainview.findViewById(R.id.horaeditarcita_txt);
        String horas_str = textohoras.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if(horas<0||horas>24)
        {
            Toast.makeText(mainview.getContext(),"horas invalidas",Toast.LENGTH_SHORT).show();
            textohoras.setText("");
            c++;
        }


        if(c!=0)
        {

            Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            Vehiculo v = patio.buscarVehiculos("placa",placa_str);
            if(patio.asignarVendedor(horas_str,fecha)!=null){
                Vendedor vendedor = patio.asignarVendedor(horas_str,fecha);
                int entero = Integer.parseInt(horas_str);
                Cita ac = patio.buscarCitas("correo","Hola@gmail.com",Patioventainterfaz.usuarioActual.getCedula());
                ac.actualizar(fecha,entero,v,vendedor,(Cliente) Patioventainterfaz.usuarioActual);
            }
            else
            {
                Toast.makeText(mainview.getContext(),"No tenemos personal disponible a esa hora",Toast.LENGTH_SHORT).show();
                textoanio.setText("");
                textodia.setText("");
                textomes.setText("");
                textohoras.setText("");
                textoplaca.setText("");
            }

        }

    }


    public void visualizarcitaEditable() throws Exception
    {
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textohoras;
        EditText textoplaca;
        textoplaca = mainview.findViewById(R.id.placa_etxt);
        textodia = mainview.findViewById(R.id.dia_cita_nueva_etxt);
        textomes = mainview.findViewById(R.id.mes_cita_nueva_etxt);
        textoanio = mainview.findViewById(R.id.anio_cita_nueva_etxt);
        textohoras = mainview.findViewById(R.id.hora_cita_nueva_cl_etxt);
        PatioVenta p = new PatioVenta();
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = p.buscarCitas("cliente",cliente.getCedula(),cliente.getCedula());
        textohoras.setText(cita.getHora());
        textoplaca.setText(cita.getVehiculo().getPlaca());
        String fecha =  Patioventainterfaz.getFechaMod(cita.getFechaCita());
        String dia = fecha.split("-")[2];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[0];
        textoanio.setText(anio);
        textomes.setText(mes);
        textodia.setText(dia);
    }

    public void cargar() throws Exception {
        Cita cita1=(Cita)patio.getCitas().getInicio().getDato();
        listaview=mainview.findViewById(R.id.listacitas_Rv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mainview.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview=new Adaptador_Lista_Cliente_Cita( patio.getCitas().listabusqueda(cita1.getCliente()));
        listaview.setAdapter(adptadorlistaview);
        listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }
    public void buscarPorFecha() throws Exception {
        Lista citasencotradas;
       EditText buscarDia;
        EditText buscarMes;
        EditText buscarAnio;
        //EditText buscarHora;
        int c=0;
        buscarDia = mainview.findViewById(R.id.buscar_dia_CC_etxt);
        buscarMes = mainview.findViewById(R.id.buscar_mes_CC_etxt);
        buscarAnio = mainview.findViewById(R.id.buscar_anio_CC_etxt);
        //buscarHora = mainview.findViewById(R.id.buscar_hora_CC_etxt);
        buscarDia = mainview.findViewById(R.id.buscar_dia_CC_etxt);
        String dia_str = buscarDia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if(dia<1||dia>30)
        {
            Toast.makeText(mainview.getContext(),"Dia invalido",Toast.LENGTH_SHORT).show();
            buscarDia.setText("");
            c++;
        }

        buscarMes = mainview.findViewById(R.id.buscar_mes_CC_etxt);
        String mes_str = buscarMes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if(mes<1||mes>12)
        {
            Toast.makeText(mainview.getContext(),"mes invalido",Toast.LENGTH_SHORT).show();
            buscarMes.setText("");
            c++;
        }

        buscarAnio = mainview.findViewById(R.id.buscar_anio_CC_etxt);
        String anio_str = buscarAnio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if(anio<2021||anio>2022)
        {
            Toast.makeText(mainview.getContext(),"año invalido",Toast.LENGTH_SHORT).show();
            buscarAnio.setText("");
            c++;
        }

       /* buscarHora = mainview.findViewById(R.id.buscar_hora_CC_etxt);
        String horas_str = buscarHora.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if(horas<0||horas>24)
        {
            Toast.makeText(mainview.getContext(),"horas invalidas",Toast.LENGTH_SHORT).show();
            buscarHora.setText("");
            c++;
        }*/


        if(c!=0)
        {

            Date fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            Lista citaaux= patio.buscarporfecha(fecha);

            listaview=mainview.findViewById(R.id.listacitas_Rv);
            RecyclerView.LayoutManager manager=new LinearLayoutManager(mainview.getContext());
            listaview.setLayoutManager(manager);
            listaview.setItemAnimator(new DefaultItemAnimator());
            adptadorlistaview=new Adaptador_Lista_Cliente_Cita( citaaux);
            listaview.setAdapter(adptadorlistaview);
            listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));



        }
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}