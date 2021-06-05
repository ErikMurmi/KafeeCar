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

    private LinearLayout irRegistrarVendedor;
    private LinearLayout irVisualizarVendedor;
    private LinearLayout irAdministrarVendedor;
    private LinearLayout irEditarVendedor;

    private FloatingActionButton aniadirVendedor_btn;
    private Button deshabilitar_btn;
    private Button editar_btn;
    private Button listo_btn;
    private Button editarlisto_btn;
    private Button editarDeshacer_btn;
    private Button verVendedor_btn;

    private ImageButton imagenPerfilVendedor_btn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.vendedor_admin,container,false);
        patio = Patioventainterfaz.patioventa;

        //botones
        aniadirVendedor_btn = mainView.findViewById(R.id.boton_mas_admin_btn);
        deshabilitar_btn = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        editar_btn = mainView.findViewById(R.id.editar_vendedor_btn);
        listo_btn = mainView.findViewById(R.id.botonListo_btn);
        editarDeshacer_btn = mainView.findViewById(R.id.botonEditDeshacerVendedor_btn);
        editarlisto_btn = mainView.findViewById(R.id.botonEditListo_btn);
        verVendedor_btn = mainView.findViewById(R.id.boton_ver_vendedor_btn);

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

        verVendedor_btn.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.VISIBLE);
        });

        editar_btn.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.VISIBLE);
            EditText cedulaEdit = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            verVendedorEditable(cedulaEdit.getText().toString());
        });

        /*listo_btn.setOnClickListener(v -> {
            try{
                registrarVendedor();
            }catch (Exception e){
                Toast.makeText(mainView.getContext(), "No se pudo añadir el vendedor", Toast.LENGTH_SHORT).show();
            }
        });

        editarlisto_btn.setOnClickListener(v -> {

        });

        imagenPerfilVendedor_btn.setOnClickListener(v -> {
            openGalery();
        });
        */


        return mainView;
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

    public void visualizarVendedor() throws Exception {

        TextView nombre = mainView.findViewById(R.id.nombre_vendedor_txt);
        TextView fechaNacimiento = mainView.findViewById(R.id.fecha_nacimiento_vendedor_txt);
        TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
        TextView telefono = mainView.findViewById(R.id.telefono_vendedor_txt);
        TextView correo = mainView.findViewById(R.id.correo_vendedor_txt);
        TextView entrada = mainView.findViewById(R.id.entrada_vendedor_txt);
        TextView almuerzo = mainView.findViewById(R.id.almuerzo_vendedor_txt);
        TextView salida = mainView.findViewById(R.id.salida_vendedor_txt);

        Vendedor venMostrar = (Vendedor) Patioventainterfaz.patioventa.getVendedores().getPos(1);
        nombre.setText(venMostrar.getNombre());
        fechaNacimiento.setText(Patioventainterfaz.getFechaMod(venMostrar.getFechaNacimiento()));
        cedula.setText(venMostrar.getCedula());
        telefono.setText(venMostrar.getTelefono());
        correo.setText(venMostrar.getCorreo());
        entrada.setText(venMostrar.getHoraEntrada());
        almuerzo.setText(venMostrar.getHoraComida());
        salida.setText(venMostrar.getHoraSalida());
    }

    public void verVendedorEditable(String cedula){
        try{
            Vendedor cedulaVen = patio.buscarVendedores("Cedula", cedula);

        }catch (Exception e){
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
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
