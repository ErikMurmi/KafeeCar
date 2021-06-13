package com.prog.kafeecar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Perfil_cliente_fragment extends Fragment{
    /*private View mainview;
    private View login;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Button cerrarsesion;
    private Button editar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainview = inflater.inflate(R.layout.perfil_cliente, container, false);
        cerrarsesion.setOnClickListener(v ->{
            try{
                Patioventainterfaz usuarioactual = null;
                mainview.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        editar.setOnClickListener(v ->{
            try{
                editarperfil_cliente();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        return mainview;
    }

    public void editarperfil_cliente() throws ParseException {
        EditText textonombre;
        EditText textotelefono;
        EditText textocedula;
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textocorreo;
        EditText textocontrasenia;

        int c =0;

        cerrarsesion = mainview.findViewById(R.id.cerrar_sesion_usuario_btn);
        textonombre = mainview.findViewById(R.id.nombre_usuario_etxt);
        textotelefono = mainview.findViewById(R.id.telefono_usuario_etxt);
        textocedula = mainview.findViewById(R.id.cedula_usuario_etxt);
        textodia = mainview.findViewById(R.id.dia_usuario_etxt);
        textomes = mainview.findViewById(R.id.mes_usuario_etxt);
        textoanio = mainview.findViewById(R.id.anio_usuario_etxt);
        textocorreo = mainview.findViewById(R.id.correo_usuario_etxt);
        textocontrasenia = mainview.findViewById(R.id.contrasenia_usuario_etxt);




        String nombre = textonombre.getText().toString();
        String telefono_str = textotelefono.getText().toString();
        String cedula_str = textocedula.getText().toString();
        String dia_str = textodia.getText().toString();
        String mes_str = textomes.getText().toString();
        String anio_str = textoanio.getText().toString();
        String correo = textocorreo.getText().toString();
        String contrasenia = textocontrasenia.getText().toString();

        int dia = Integer.parseInt(dia_str);
        int mes = Integer.parseInt(mes_str);
        int anio = Integer.parseInt(anio_str);

        if(telefono_str.length()!=10)
        {
            Toast.makeText(mainview.getContext(), "Telefono invalido", Toast.LENGTH_SHORT).show();
            textotelefono.setText("");
            c++;
        }

        if(cedula_str.length()!=10)
        {
            Toast.makeText(mainview.getContext(), "Cedula invalida", Toast.LENGTH_SHORT).show();
            textocedula.setText("");
            c++;
        }

        if (dia < 1 || dia > 30) {
            Toast.makeText(mainview.getContext(), "Dia invalido", Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        if (mes < 1 || mes > 12) {
            Toast.makeText(mainview.getContext(), "mes invalido", Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        if (anio < 1900 || anio > 2003) {
            Toast.makeText(mainview.getContext(), "año invalido", Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        if(c!=0)
        {
            Date fechadate = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            String fecha = Patioventainterfaz.getFechaMod(fechadate);
            Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;
            cliente.cambiarDatos(nombre,cedula_str,telefono_str,correo,contrasenia,fecha);
        }
    }

    public void visualizarperfileditable_Cliente()
    {
        EditText textonombre;
        EditText textotelefono;
        EditText textocedula;
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textocorreo;
        EditText textocontrasenia;
        Cliente cliente = (Cliente) Patioventainterfaz.usuarioActual;

        textonombre = mainview.findViewById(R.id.nombre_usuario_etxt);
        textotelefono = mainview.findViewById(R.id.telefono_usuario_etxt);
        textocedula = mainview.findViewById(R.id.cedula_usuario_etxt);
        textodia = mainview.findViewById(R.id.dia_usuario_etxt);
        textomes = mainview.findViewById(R.id.mes_usuario_etxt);
        textoanio = mainview.findViewById(R.id.anio_usuario_etxt);
        textocorreo = mainview.findViewById(R.id.correo_usuario_etxt);
        textocontrasenia = mainview.findViewById(R.id.contrasenia_usuario_etxt);

        textonombre.setText(cliente.getNombre());
        textotelefono.setText(cliente.getTelefono());
        textocedula.setText(cliente.getCedula());
        String fecha = Patioventainterfaz.getFechaMod(cliente.getFechaNacimiento());
        String dia = fecha.split("-")[2];
        String mes = fecha.split("-")[1];
        String anio = fecha.split("-")[0];
        textodia.setText(dia);
        textomes.setText(mes);
        textoanio.setText(anio);
        textocorreo.setText(cliente.getCorreo());
        textocontrasenia.setText(cliente.getClave());
    }*/


}
