package com.prog.kafeecar;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Citas_cliente_fragment extends Fragment {

    private View mainview;
    private Button guardar;
    private Button actualizar;
    private Button descartar;
    private Button regresar;
    private ScrollView vercita;
    private LinearLayout editarcita;
    private PatioVenta patio;
    private Patioventainterfaz PatioIterfaz = new Patioventainterfaz();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.listacitas_cliente, container, false);
        patio = Patioventainterfaz.patioventa;

        //Botones

        guardar = mainview.findViewById(R.id.guardar_clita_nueva_btn);
        actualizar = mainview.findViewById(R.id.actualizar_btn);
        descartar = mainview.findViewById(R.id.descartar_btn);

        //layouts
        vercita = mainview.findViewById(R.id.ver_cita_Scroll);
        editarcita = mainview.findViewById(R.id.editar_cita_linear);

        guardar.setOnClickListener(v ->{
            try {
                modificarCita();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        actualizar.setOnClickListener(v -> {
            try {
                visualizarCitacliente();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        descartar.setOnClickListener(v -> {
            mainview.setVisibility(View.VISIBLE);
            vercita.setVisibility(View.GONE);
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


        textodia = mainview.findViewById(R.id.fecha_txt3);
        textomes = mainview.findViewById(R.id.fecha_txt2);
        textoanio = mainview.findViewById(R.id.fecha_txt);
        textohoras = mainview.findViewById(R.id.hora_txt);
        textovendedor = mainview.findViewById(R.id.vendedor_txt);
        textovehiculo = mainview.findViewById(R.id.vehiculo_txt);
        textodescripcion = mainview.findViewById(R.id.descripcion_txt);
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = patio.buscarCitas("cliente", cliente.getCedula());
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

    public void citaNueva() throws Exception {
        int c = 0;
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textohoras;

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

        textohoras = mainview.findViewById(R.id.hora_txt);
        String horas_str = textohoras.getText().toString();
        int horas = Integer.parseInt(horas_str);
        if (horas < 0 || horas > 24) {
            Toast.makeText(mainview.getContext(), "horas invalidas", Toast.LENGTH_SHORT).show();
            textohoras.setText("");
            c++;
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
                Cita ac = patio.buscarCitas("correo","Hola@gmail.com");
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
        textohoras = mainview.findViewById(R.id.hora_clita_nueva_etxt);
        PatioVenta p = new PatioVenta();
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
        Cita cita = p.buscarCitas("cliente",cliente.getCedula());
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

    public void verlistacitas() throws Exception
    {
        TextView horalista;
        TextView horalista2;
        TextView Clientelista;
        TextView Clientelista2;
        TextView Telefonolista;
        TextView Telefonolista2;
        TextView Matriculalista;
        TextView Matriculalista2;

/*
        horalista = mainview.findViewById(R.id.hora_lista_t);
        horalista2 = mainview.findViewById(R.id.hora_lista2_txt);
        Clientelista = mainview.findViewById(R.id.cliente_listacita_txt);
        Clientelista2 = mainview.findViewById(R.id.clente_listacita2_txt);
        Telefonolista = mainview.findViewById(R.id.telefono_listacita_txt);
        Telefonolista2 = mainview.findViewById(R.id.telefono_listacita2_txt);
        Matriculalista =mainview.findViewById(R.id.matricula_listacita_txt);
        Matriculalista2 = mainview.findViewById(R.id.matricula_licencia2_txt);


        Cita cita = patio.buscarCitas("Cliente", "1750115623");
        horalista.setText(cita.getHora());
        Clientelista.setText(cita.getVisitante().getNombre());
        Telefonolista.setText(cita.getVisitante().getTelefono());
        Matriculalista.setText(cita.getVehiculo().getMatricula());

        Cita cita2 = patio.buscarCitas("Cliente", "175014048");
        horalista2.setText(cita2.getHora());
        Clientelista2.setText(cita2.getVisitante().getNombre());
        Telefonolista2.setText(cita2.getVisitante().getTelefono());
        Matriculalista2.setText(cita2.getVehiculo().getMatricula());

 */
    }
}