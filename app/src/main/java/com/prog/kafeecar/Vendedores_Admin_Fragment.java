package com.prog.kafeecar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Vendedores_Admin_Fragment extends Fragment {
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private PatioVenta patio;

    private View mainView;

    private EditText cedulaVendedorE;

    private LinearLayout irRegistrarVendedor;
    private LinearLayout irVisualizarVendedor;
    private LinearLayout irAdministrarVendedor;
    private LinearLayout irEditarVendedor;

    //lyts de los vendedores en la lista
    private LinearLayout verVendedor_lyt;
    private LinearLayout verVendedor1_lyt;


    private FloatingActionButton aniadirVendedor_btn;
    private Button deshabilitar_btn;
    private Button editar_btn;
    private Button listo_btn;
    private Button editarlisto_btn;
    private Button editarDeshacer_btn;

    private ImageButton imagenPerfilVendedor_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.vendedor_admin,container,false);
        patio = Patioventainterfaz.patioventa;
        verListaVendedores("1732221032", "1721835213");

        cedulaVendedorE = mainView.findViewById(R.id.cedulaEditVendedor_etxt);

        //botones
        aniadirVendedor_btn = mainView.findViewById(R.id.boton_mas_admin_btn);
        deshabilitar_btn = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        editar_btn = mainView.findViewById(R.id.editar_vendedor_btn);
        listo_btn = mainView.findViewById(R.id.botonListo_btn);
        editarDeshacer_btn = mainView.findViewById(R.id.botonEditDeshacerVendedor_btn);
        editarlisto_btn = mainView.findViewById(R.id.botonEditListo_btn);

        imagenPerfilVendedor_btn = mainView.findViewById(R.id.imagenPerfilVendedor_ibtn);

        //declaracion de los lyts de los vendedores en la lista
        verVendedor_lyt = mainView.findViewById(R.id.AVvendedor_lyt);
        verVendedor1_lyt = mainView.findViewById(R.id.AVvendedor2_lyt);

        //layouts

        irRegistrarVendedor = mainView.findViewById(R.id.registrar_vendedor_lyt);
        irVisualizarVendedor = mainView.findViewById(R.id.visualizar_vendedor_lyt);
        irAdministrarVendedor = mainView.findViewById(R.id.administrar_vendedor_lyt);
        irEditarVendedor = mainView.findViewById(R.id.editar_vendedor_lyt);

        aniadirVendedor_btn.setOnClickListener(v -> {
            //Desactivar otros diseños
            irVisualizarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            //Activar el diseño deseado
            irRegistrarVendedor.setVisibility(View.VISIBLE);

        });

        verVendedor_lyt.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.VISIBLE);
            try {
                visualizarVendedor("1732221032");
            }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se pudo realizar la peticion deseada", Toast.LENGTH_SHORT).show();
        }
        });

        editar_btn.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.VISIBLE);
            verVendedorEditable(cedulaVendedorE.getText().toString());
        });

        deshabilitar_btn.setOnClickListener(v -> {
            try {
                TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
                Vendedor vendedor = patio.buscarVendedores("Cedula", cedula.getText().toString());
                if (deshabilitar_btn.getText().toString().compareToIgnoreCase("Deshabilitar") == 0) {
                    Toast.makeText(mainView.getContext(), "¡ADVETENCIA: ESTE USUARIO SE DESHABILITARÁ DEL SISTEMA!", Toast.LENGTH_SHORT).show();
                    vendedor.setActivo(false);
                    deshabilitar_btn.setText("Habilitar");
                } else {
                    deshabilitar_btn.setText("Deshabilitar");
                    Toast.makeText(mainView.getContext(), "¡ADVETENCIA: ESTE USUARIO SE HABILITARÁ EN EL SISTEMA!", Toast.LENGTH_SHORT).show();
                    vendedor.setActivo(true);
                }
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo ejecutar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        listo_btn.setOnClickListener(v -> {
            try{
                registrarVendedor();
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo añadir el vendedor", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });

        editarlisto_btn.setOnClickListener(v -> {
            try{
                editarVendedor(cedulaVendedorE.getText().toString());
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });

        imagenPerfilVendedor_btn.setOnClickListener(v -> {
            openGalery();
        });

        return mainView;
    }

    public void verListaVendedores(String cedula, String cedula1){
        try{
            String am = "am";

            Vendedor v_Mostrar = patio.buscarVendedores("Cedula",cedula);
            Vendedor v_Mostrar1 = patio.buscarVendedores("Cedula",cedula1);

            TextView nombreV = mainView.findViewById(R.id.AVnombre_txt);
            TextView entradaV = mainView.findViewById(R.id.AVhoraEntrada_txt);
            TextView almuerzoV = mainView.findViewById(R.id.AVhoraAlmuerzo_txt);
            TextView salidaV = mainView.findViewById(R.id.AVhoraSalida_txt);

            TextView nombreV1 = mainView.findViewById(R.id.AVnombre1_txt);
            TextView entradaV1 = mainView.findViewById(R.id.AVhoraEntrada1_txt);
            TextView almuerzoV1 = mainView.findViewById(R.id.AVhoraAlmuerzo1_txt);
            TextView salidaV1 = mainView.findViewById(R.id.AVhoraSalida1_txt);

            nombreV.setText(v_Mostrar.getNombre());
            entradaV.setText(String.format("%d:00 %s",v_Mostrar.getHoraEntrada(),formatoHora(v_Mostrar.getHoraEntrada())));
            almuerzoV.setText(String.format("%d:00 %s",v_Mostrar.getHoraComida(),formatoHora(v_Mostrar.getHoraComida())));
            salidaV.setText(String.format("%d:00 %s",v_Mostrar.getHoraSalida(),formatoHora(v_Mostrar.getHoraSalida())));

            nombreV1.setText(v_Mostrar1.getNombre());
            entradaV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraEntrada(),formatoHora(v_Mostrar1.getHoraEntrada())));
            almuerzoV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraComida(),formatoHora(v_Mostrar1.getHoraComida())));
            salidaV1.setText(String.format("%d:00 %s",v_Mostrar1.getHoraSalida(),formatoHora(v_Mostrar1.getHoraSalida())));

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public String formatoHora(int hora){
        if(hora>12){
            return "pm";
        }
        return "am";
    }

    public void regresarPantallaPrncipal(){
        verListaVendedores("1732221032", "1721835213");
        irRegistrarVendedor.setVisibility(View.GONE);
        irVisualizarVendedor.setVisibility(View.GONE);
        irEditarVendedor.setVisibility(View.GONE);
        //boton y pantalla visible
        aniadirVendedor_btn.setVisibility(View.VISIBLE);
        irAdministrarVendedor.setVisibility(View.VISIBLE);
    }

    public void registrarVendedor() throws ParseException {

        EditText nombreVendedor = mainView.findViewById(R.id.nombreVendedor_etxt);
        EditText apellidoVendedor = mainView.findViewById(R.id.apellidoVendedor_etxt);
        EditText cedulaVendedor = mainView.findViewById(R.id.cedulaVendedor_etxt);
        EditText diaNacimientoVendedor = mainView.findViewById(R.id.diaNacimientoVendedor_etxt);
        EditText mesNacimientoVendedor = mainView.findViewById(R.id.mesNacimientoVendedor_etxt);
        EditText anioNacimientoVendedor = mainView.findViewById(R.id.anioNacimientoVendedor_etxt);
        EditText telefonoVendedor = mainView.findViewById(R.id.telefonoVendedor_etxt);
        EditText correoVendedor = mainView.findViewById(R.id.correoVendedor_etxt);
        EditText contraseniaVendedor = mainView.findViewById(R.id.contraseniaVendedor_etxt);
        EditText confirmarContraseniaVendedor = mainView.findViewById(R.id.confirmarContraseniaVendedor_etxt);
        EditText horaEntradaVendedor = mainView.findViewById(R.id.horaEntradaVendedor_etxt);
        EditText horaSalidaVendedor = mainView.findViewById(R.id.horaSalidaVendedor_etxt);
        EditText horaAlmuerzoVendedor = mainView.findViewById(R.id.horaAlmuerzoVendedor_etxt);

        String nombreVendedor_str = nombreVendedor.getText().toString() + "" + apellidoVendedor.getText().toString();
        String cedulaVendedor_str = cedulaVendedor.getText().toString();
        String fechaNacimientoVendedor_date = diaNacimientoVendedor.getText().toString()
                + "/" + mesNacimientoVendedor.getText().toString()
                + "/" + anioNacimientoVendedor.getText().toString();
        String telefonoVendedor_str = telefonoVendedor.getText().toString();
        String correoVendedor_str = correoVendedor.getText().toString();
        String contraseniaVendedor_str = contraseniaVendedor.getText().toString();
        String confirmarContraseniaVendedor_str = confirmarContraseniaVendedor.getText().toString();
        int horaEntradaVendedor_int = Integer.parseInt(horaEntradaVendedor.getText().toString());
        int horaAlmuerzoVendedor_int = Integer.parseInt(horaAlmuerzoVendedor.getText().toString());
        int horaSalidaVendedor_int = Integer.parseInt(horaSalidaVendedor.getText().toString());

        if(contraseniaVendedor_str.compareTo(confirmarContraseniaVendedor_str)==0){
            String contraseniaVerificada = contraseniaVendedor_str;
            patio.aniadirUsuario(new Vendedor(horaEntradaVendedor_int,horaSalidaVendedor_int,horaAlmuerzoVendedor_int, patio,
                    nombreVendedor_str, cedulaVendedor_str, telefonoVendedor_str, correoVendedor_str, contraseniaVerificada,
                    sdf.parse(fechaNacimientoVendedor_date)),"Vendedor");
        }else{
            //Toast.makeText(Patioventainterfaz.this, "Las contraseÃ±as no coinciden. Ingrese Nuevamente.",Toast.LENGTH_SHORT).show();
            contraseniaVendedor.setText("");
            confirmarContraseniaVendedor.setText("");
        }
    }

    public void visualizarVendedor(String ced) throws Exception {

        TextView nombre = mainView.findViewById(R.id.nombre_vendedor_txt);
        TextView fechaNacimiento = mainView.findViewById(R.id.fecha_nacimiento_vendedor_txt);
        TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
        TextView telefono = mainView.findViewById(R.id.telefono_vendedor_txt);
        TextView correo = mainView.findViewById(R.id.correo_vendedor_txt);
        TextView entrada = mainView.findViewById(R.id.entrada_vendedor_txt);
        TextView almuerzo = mainView.findViewById(R.id.almuerzo_vendedor_txt);
        TextView salida = mainView.findViewById(R.id.salida_vendedor_txt);

        Vendedor venMostrar = patio.buscarVendedores("Cedula",ced);
        nombre.setText(venMostrar.getNombre());
        fechaNacimiento.setText(Patioventainterfaz.getFechaMod(venMostrar.getFechaNacimiento()));
        cedula.setText(venMostrar.getCedula());
        telefono.setText(venMostrar.getTelefono());
        correo.setText(venMostrar.getCorreo());
        entrada.setText(String.valueOf(venMostrar.getHoraEntrada()));
        almuerzo.setText(String.valueOf(venMostrar.getHoraComida()));
        salida.setText(String.valueOf(venMostrar.getHoraSalida()));

        Button habilitar = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        if (venMostrar.getActivo()) {
            habilitar.setText("Deshabilitar");
        } else {
            habilitar.setText("Habilitar");
        }
    }

    public void verVendedorEditable(String cedula){
        try{
            Vendedor cedulaVen = patio.buscarVendedores("Cedula", cedula);
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);
            EditText contrasenia_ed = mainView.findViewById(R.id.contraseniaEditVendedor_etxt);
            EditText confirmarContrasenia_ed = mainView.findViewById(R.id.confirmarContraseniaEditVendedor_etxt);

            String fechaNacimiento = Patioventainterfaz.getFechaMod(cedulaVen.getFechaNacimiento());
            String dia = fechaNacimiento.split("-")[2];
            String mes = fechaNacimiento.split("-")[1];
            String anio = fechaNacimiento.split("-")[0];

            nombre_ed.setText(cedulaVen.getNombre());
            dia_ed.setText(dia);
            mes_ed.setText(mes);
            anio_ed.setText(anio);
            cedula_ed.setText(cedulaVen.getCedula());
            telefono_ed.setText(cedulaVen.getTelefono());
            correo_ed.setText(cedulaVen.getCorreo());

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarVendedor(String cedula){
        try {
            Vendedor cedulaVen = patio.buscarVendedores("Cedula", cedula);
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);

            String fechaNacimientoVendedor= dia_ed.getText().toString()
                    + "/" + mes_ed.getText().toString()
                    + "/" + anio_ed.getText().toString();
            cedulaVen.cambiarDatosSinClave(
                    nombre_ed.getText().toString(),
                    cedula_ed.getText().toString(),
                    telefono_ed.getText().toString(),
                    correo_ed.getText().toString(),
                    fechaNacimientoVendedor);

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGalery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_GALERY){
            if(resultCode == Activity.RESULT_OK && data != null){
                Uri foto = data.getData();
                imagenPerfilVendedor_btn.setImageURI(foto);
            }else{
                Toast.makeText(mainView.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
