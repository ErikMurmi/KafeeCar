package com.prog.kafeecar;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.prog.kafeecar.Patioventainterfaz.getFechaMod;

public class Citas_Cliente_Fragment extends Fragment implements Adaptador_Lista_Cliente_Cita.RecyclerItemClick, SearchView.OnQueryTextListener {
    private String TAG = "Citas_Cliete";
    private View mainView;
    //private Button deacartarnuevacita;
    //private Button guardarcitanueva;
    //private Button guardarcitaeditada;
    //private Button descartareditarcita;
   // private Button regresar;
    //private Button actualizarvercita;
    //private Button buscarCitaFecha;
    private FloatingActionButton irAniadirCita;
    private Cita cita_mostrar;


   // private ScrollView vercita;
    //private ScrollView catalogoAutosCliente;
   // private LinearLayout nuevacita;

    private LinearLayout editarCita;
    private LinearLayout verCitaLista;
    private LinearLayout verCita;

    private PatioVenta patio;
    private Vehiculo vMostrar;
    //private Patioventainterfaz PatioIterfaz = new Patioventainterfaz();

    private RecyclerView listaview;

    private SearchView busqueda_citas;
    private Adaptador_Lista_Cliente_Cita adptadorlistaview;

    private SimpleDateFormat sdf = Patioventainterfaz.sdf;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private final Cliente cliente_actual = (Cliente) Patioventainterfaz.usuarioActual;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.citas_cliente, container, false);
        patio = Patioventainterfaz.patioventa;
        verCitaLista = mainView.findViewById(R.id.citas_cliente_CC_lyt);
        editarCita = mainView.findViewById(R.id.add_cita_cli_lyt);
        verCita = mainView.findViewById(R.id.ver_ci_cli_lyt);
        busqueda_citas = mainView.findViewById(R.id.busqueda_c_cli_srv);

        //Botones aniadir cita nueva
        //deacartarnuevacita=mainview.findViewById(R.id.descartar_NC_btn);
        //guardarcitanueva = mainview.findViewById(R.id.guardar_clita_nueva_NC_btn);
        //Botones actualizar cita cuando se edita
        //guardarcitaeditada=mainview.findViewById(R.id.guardar_cita_nueva_EC_btn);
        //descartareditarcita = mainview.findViewById(R.id.descartar_EC_btn);

        //boton de ver cita

        //irAniadirCita = mainview.findViewById(R.id.ir_aniadir_cita_cli_fbtn);

        //regresar = mainview.findViewById(R.id.regresar_cita_VC_btn);
        //actualizarvercita=mainview.findViewById(R.id.actualizar_cita_VC_btn);
        //boton de Lista Cita
        //buscarCitaFecha=mainview.findViewById(R.id.buscar_listacita_btn);
        //Botton que entre a cita grande
        //layouts/Scroll
        //catalogoAutosCliente = mainview.findViewById(R.id.catalogo_vehiculo_cliente_Rv);
        //vercita = mainview.findViewById(R.id.ver_cita_Scroll);
        //cita_cliente_lyt = mainview.findViewById(R.id.citas_cliente_CC_lyt);
        //editarcita = mainview.findViewById(R.id.editar_cita_linear);


        //ver_ci_cli_lyt = mainview.findViewById(R.id.ver_ci_cli_lyt);

        //aniadirCita = mainview.findViewById(R.id.add_cita_cli_lyt);


        /*irAniadirCita.setOnClickListener(v -> {
            irAniadirCita.setVisibility(View.GONE);
            ver_ci_cli_lyt.setVisibility(View.GONE);
            aniadirCita.setVisibility(View.VISIBLE);
            adaptadorAniadir();
        });*/

        //nuevacita=mainview.findViewById(R.id.nueva_cita_cliente_lay);

        /*buscarCitaFecha.setOnClickListener(v ->{
            try{
                //buscarPorFecha();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

        /*guardarcitanueva.setOnClickListener(v ->{
            try {
                citaNueva(vMostrar.getPlaca());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });*/

        /*deacartarnuevacita.setOnClickListener(v -> {
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
        });*/

        /*descartareditarcita.setOnClickListener(v -> {
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
        });*/

        /*if(Patioventainterfaz.CITA_CON_VEHICULO){
            irAniadirCita.callOnClick();
        }


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        busqueda_citas.setOnQueryTextListener(this);
        cargar();
        return mainView;
    }


    public void adaptadorAniadir(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_ci_cli_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        //TextView cedula_vendedor_ci_ad_txt = mainview.findViewById(R.id.cedula_vendedor_ci_cli_etxt);
        //cedula_vendedor_ci_ad_txt.setText(Patioventainterfaz.usuarioActual.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_ci_cli_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public void visualizarCitacliente() throws Exception {
        TextView textodia;
        TextView textomes;
        TextView textoanio;
        TextView textohoras;
        TextView textovendedor;
        TextView textovehiculo;
        TextView textodescripcion;


        textodia = mainView.findViewById(R.id.dia_cita_VC_etxt);
        textomes = mainView.findViewById(R.id.mes_cita_VC_etxt);
        textoanio = mainView.findViewById(R.id.anio_cita_VC_etxt);
        textohoras = mainView.findViewById(R.id.hora_cita_VC_etxt);
        textovendedor = mainView.findViewById(R.id.nomvendedor_cita_VC_etxt);
        textovehiculo = mainView.findViewById(R.id.vehiculo_cita_VC_etxt);
        textodescripcion = mainView.findViewById(R.id.descripcion_cita_VC_etxt);
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = patio.buscarCita("cliente", cliente.getCedula(), cliente.getCedula());
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

    /*public void citaNueva(String placa) throws Exception {
        int c = 0;
        EditText textodia= mainView.findViewById(R.id.dia_cita_nueva_etxt);
        EditText textomes = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        EditText textoanio = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        EditText textohoras = mainView.findViewById(R.id.hora_cita_nueva_cl_etxt);
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        String fechacita_str = "";

        int hora = -1;
        if ((!isEmpty(textodia) && !isEmpty(textomes)) && (!isEmpty(textoanio) && !isEmpty(textohoras))) {
            fechacita_str = textodia.getText().toString() + "-" + textomes.getText().toString() + "-" + textoanio.getText().toString();
            hora = Integer.parseInt(textohoras.getText().toString());
        } else {
            c++;
            Toast.makeText(mainView.getContext(), "Campos de fecha vacíos", Toast.LENGTH_SHORT).show();
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
        ImageView vehiculo_img = mainView.findViewById(R.id.imagen_auto_cita_NC_img);
        textodia = mainView.findViewById(R.id.dia_cita_nueva_etxt);
        String dia_str = textodia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if (dia < 1 || dia > 30) {
            Toast.makeText(mainView.getContext(), "Dia invalido", Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        textomes = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        String mes_str = textomes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if (mes < 1 || mes > 12) {
            Toast.makeText(mainView.getContext(), "mes invalido", Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        textoanio = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if (anio < 2021 || anio > 2022) {
            Toast.makeText(mainView.getContext(), "año invalido", Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        textohoras = mainView.findViewById(R.id.hora_cita_nueva_cl_etxt);
        String horas_str = textohoras.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if (horas < 0 || horas > 24) {
            Toast.makeText(mainView.getContext(), "horas invalidas", Toast.LENGTH_SHORT).show();
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


    }/*

    /*public void modificarCita() throws Exception
    {
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textohoras;
        EditText textoplaca;

        int c=0;

        textoplaca = mainView.findViewById(R.id.placa_etxt);
        String placa_str = textoplaca.getText().toString();

        textodia = mainView.findViewById(R.id.dia_cita_nueva_etxt);
        String dia_str = textodia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if(dia<1||dia>30)
        {
            Toast.makeText(mainView.getContext(),"Dia invalido",Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        textomes = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        String mes_str = textomes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if(mes<1||mes>12)
        {
            Toast.makeText(mainView.getContext(),"mes invalido",Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        textoanio = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if(anio<2021||anio>2022)
        {
            Toast.makeText(mainView.getContext(),"año invalido",Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        textohoras = mainView.findViewById(R.id.horaeditarcita_txt);
        String horas_str = textohoras.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if(horas<0||horas>24)
        {
            Toast.makeText(mainView.getContext(),"horas invalidas",Toast.LENGTH_SHORT).show();
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
                Cita ac = patio.buscarCita("correo","Hola@gmail.com",Patioventainterfaz.usuarioActual.getCedula());
                ac.actualizar(fecha,entero,v,vendedor,(Cliente) Patioventainterfaz.usuarioActual);
            }
            else
            {
                Toast.makeText(mainView.getContext(),"No tenemos personal disponible a esa hora",Toast.LENGTH_SHORT).show();
                textoanio.setText("");
                textodia.setText("");
                textomes.setText("");
                textohoras.setText("");
                textoplaca.setText("");
            }

        }

    }/*


    /*public void visualizarcitaEditable() throws Exception
    {
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textohoras;
        EditText textoplaca;
        textoplaca = mainView.findViewById(R.id.placa_etxt);
        textodia = mainView.findViewById(R.id.dia_cita_nueva_etxt);
        textomes = mainView.findViewById(R.id.mes_cita_nueva_etxt);
        textoanio = mainView.findViewById(R.id.anio_cita_nueva_etxt);
        textohoras = mainView.findViewById(R.id.hora_cita_nueva_cl_etxt);
        PatioVenta p = new PatioVenta();
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = p.buscarCita("cliente",cliente.getCedula(),cliente.getCedula());
        textohoras.setText(cita.getHora());
        textoplaca.setText(cita.getVehiculo().getPlaca());
        String fecha =  Patioventainterfaz.getFechaMod(cita.getFechaCita());
        String dia = fecha.split("-")[2];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[0];
        textoanio.setText(anio);
        textomes.setText(mes);
        textodia.setText(dia);
    }*/

    public void cargar()  {
        listaview= mainView.findViewById(R.id.listacitas_Rv);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview=new Adaptador_Lista_Cliente_Cita( patio.buscarCitas(cliente_actual),this);
        listaview.setAdapter(adptadorlistaview);
    }

    /*public void buscarPorFecha() throws Exception {
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
        }


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
    */

    public void visualizarCita() {
        editarCita.setVisibility(View.GONE);
        verCitaLista.setVisibility(View.GONE);
        verCita.setVisibility(View.VISIBLE);

        ImageView imagen_ed = mainView.findViewById(R.id.ver_cita_im_cli_img);
        TextView fecha = mainView.findViewById(R.id.ver_fecha_ci_cli_txt);
        TextView hora = mainView.findViewById(R.id.ver_hora_ci_cli_txt);
        TextView cliente = mainView.findViewById(R.id.ver_cliente_ci_cli_txt);
        TextView contacto = mainView.findViewById(R.id.ver_contacto_ci_cli_txt);
        TextView vendedor = mainView.findViewById(R.id.ver_vendedor_ci_cli_txt);
        TextView vehiculo = mainView.findViewById(R.id.ver_vehiculo_ci_cli_txt);
        TextView precio = mainView.findViewById(R.id.ver_precioVenta_ci_cli_txt);
        TextView descripcion = mainView.findViewById(R.id.ver_descripcion_ci_cli_txt);
        TextView resolucion = mainView.findViewById(R.id.ver_resolucion_ci_cli_txt);
        StorageReference filePath = mStorageRef.child("Vehiculos/" + vMostrar.getimagen());
        try {
            final File localFile = File.createTempFile(vMostrar.getimagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Uri nuevo = Uri.parse(localFile.getAbsolutePath());
                imagen_ed.setImageURI(nuevo);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        fecha.setText(getFechaMod(cita_mostrar.getFechaCita()));
        hora.setText(String.valueOf(cita_mostrar.getHora()));
        cliente.setText(cita_mostrar.getCliente().getNombre());
        contacto.setText(cita_mostrar.getCliente().getTelefono());
        vendedor.setText(cita_mostrar.getVendedorCita().getNombre());
        vehiculo.setText(cita_mostrar.getVehiculo().getMarca() + cita_mostrar.getVehiculo().getModelo());
        String resolucion_str =cita_mostrar.getResolucion();
        descripcion.setText(cita_mostrar.getVehiculo().getDescripcion());
        if(!resolucion_str.equals("")){
            resolucion.setVisibility(View.VISIBLE);
            resolucion.setText(resolucion_str);
        }else{
            resolucion.setVisibility(View.GONE);
        }

        precio.setText(" $" + cita_mostrar.getVehiculo().getPrecioVenta());
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void itemClick(String placa, String cedula_cliente) {
        try {
            cita_mostrar = patio.buscarCita("Vehiculo",placa,cedula_cliente);
            visualizarCita();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Errores", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String b = newText;
        if(newText.length()==2){
            b+="-";
            busqueda_citas.setQuery(b,false);
        }
        if(newText.length()==5){
            b+="-";
            busqueda_citas.setQuery(b,false);
        }
        if(newText.length()>10){
            b= b.substring(0,10);
            busqueda_citas.setQuery(b,false);
        }
        adptadorlistaview.buscar(b);
        return false;
    }


}