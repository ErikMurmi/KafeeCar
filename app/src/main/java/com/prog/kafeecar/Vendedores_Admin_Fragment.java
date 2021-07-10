package com.prog.kafeecar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class Vendedores_Admin_Fragment extends Fragment implements Adaptador_Lista_Vendedores.RecyclerItemClick, SearchView.OnQueryTextListener {
    private static final int REQUEST_IMAGE_GALERY = 101;

    private PatioVenta patio;
    Vendedor venMostrar;

    private View mainView;

    private ImageView imagenPerfil_img;

    private Adaptador_Lista_Vendedores adptadorlistaview;

    private LinearLayout irRegistrarVendedor;
    private LinearLayout irVisualizarVendedor;
    private LinearLayout irAdministrarVendedor;
    private LinearLayout irEditarVendedor;

    private FloatingActionButton aniadirVendedor_btn;
    private Button deshabilitar_btn;
    private ImageButton imagenPerfilVendedor_btn;
    private ImageButton imagenPerfilVendedorEdit_btn;

    private Uri foto;

    private boolean editarImagen = false;

    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.vendedor_admin, container, false);
        patio = Patioventainterfaz.patioventa;

        //botones
        aniadirVendedor_btn = mainView.findViewById(R.id.boton_mas_admin_btn);
        deshabilitar_btn = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        Button editar_btn = mainView.findViewById(R.id.editar_vendedor_btn);
        Button listo_btn = mainView.findViewById(R.id.botonListo_btn);
        Button editarDeshacer_btn = mainView.findViewById(R.id.botonEditDeshacerVendedor_btn);
        Button editarlisto_btn = mainView.findViewById(R.id.botonEditListo_btn);
        Button cancelar_btn = mainView.findViewById(R.id.botonCancelarVendedores_btn);

        SearchView campoBusqueda = mainView.findViewById(R.id.busqueda_vn_ad_srv);

        imagenPerfilVendedor_btn = mainView.findViewById(R.id.imagenPerfilVendedor_ibtn);
        imagenPerfilVendedorEdit_btn = mainView.findViewById(R.id.imagenPerfilEditVendedor_ibtn);

        imagenPerfil_img = mainView.findViewById(R.id.imagen_perfil_vendedor_img);

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

        editar_btn.setOnClickListener(v -> {
            irRegistrarVendedor.setVisibility(View.GONE);
            irAdministrarVendedor.setVisibility(View.GONE);
            irVisualizarVendedor.setVisibility(View.GONE);
            aniadirVendedor_btn.setVisibility(View.GONE);
            irEditarVendedor.setVisibility(View.VISIBLE);
            verVendedorEditable();
        });

        deshabilitar_btn.setOnClickListener(v -> {
            try {
                TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
                Vendedor vendedor = patio.buscarVendedores("Cedula", cedula.getText().toString());
                if (deshabilitar_btn.getText().toString().compareToIgnoreCase("Deshabilitar") == 0) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setMessage("Este vendedor se DESHABILITARÁ. ¿Desea continuar?");
                    msg.setTitle("ADVERTENCIA");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        vendedor.setActivo(false);
                        deshabilitar_btn.setText("Habilitar");
                        regresarPantallaPrncipal();
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                } else {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setMessage("Este vendedor se HABILITARÁ. ¿Desea continuar?");
                    msg.setTitle("ADVERTENCIA");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        vendedor.setActivo(true);
                        deshabilitar_btn.setText("Deshabilitar");
                        regresarPantallaPrncipal();
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "No se pudo ejecutar la petición", Toast.LENGTH_SHORT).show();
            }
        });

        listo_btn.setOnClickListener(v -> {
            try {
                if(registrarVendedor()){
                    Toast.makeText(mainView.getContext(), "No se pudo añadir el vendedor", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "No se pudo añadir el vendedor", Toast.LENGTH_SHORT).show();
                regresarPantallaPrncipal();
            }
        });

        cancelar_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Cancelar");
            msg.setMessage("¿Está seguro de salir sin guardar?");
            msg.setPositiveButton("Si", (dialog, which) -> regresarPantallaPrncipal());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        editarlisto_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Editar Cambios");
            msg.setMessage("¿Está seguro de actualizar los datos del vendedor " + venMostrar.getNombre() + "?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    if(editarVendedor()){
                        regresarPantallaPrncipal();
                    }
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "No se pudo actualizar la información", Toast.LENGTH_SHORT).show();
                    regresarPantallaPrncipal();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        editarDeshacer_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Deshacer Cambios");
            msg.setMessage("¿Está seguro de no guardar los cambios?");
            msg.setPositiveButton("Si", (dialog, which) -> {
                try {
                    irRegistrarVendedor.setVisibility(View.GONE);
                    irAdministrarVendedor.setVisibility(View.GONE);
                    aniadirVendedor_btn.setVisibility(View.GONE);
                    irEditarVendedor.setVisibility(View.GONE);
                    visualizarVendedor();
                    irVisualizarVendedor.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Toast.makeText(mainView.getContext(), "No se pudo realizar la peticion deseada", Toast.LENGTH_SHORT).show();
                    regresarPantallaPrncipal();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        imagenPerfilVendedor_btn.setOnClickListener(v -> openGalery());

        imagenPerfilVendedorEdit_btn.setOnClickListener(v -> {
            editarImagen = true;
            openGalery();
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (irEditarVendedor.getVisibility() == View.VISIBLE) {
                    AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
                    msg.setTitle("NO GUARDAR");
                    msg.setMessage("¿Estás seguro de salir sin guardar los cambios?");
                    msg.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            visualizarVendedor();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
                    msg.show();
                }
                if (irVisualizarVendedor.getVisibility() == View.VISIBLE) {
                    regresarPantallaPrncipal();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        campoBusqueda.setOnQueryTextListener(this);
        cargar();

        return mainView;
    }

    public void cargar(){
        RecyclerView listaview = mainView.findViewById(R.id.lista_vendedores_admin);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Vendedores(patio.getVendedores(), this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }

    public String formatoHora(int hora) {
        if (hora > 12) {
            return "pm";
        }
        return "am";
    }

    public void regresarPantallaPrncipal() {
        try {
            cargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        irRegistrarVendedor.setVisibility(View.GONE);
        irVisualizarVendedor.setVisibility(View.GONE);
        irEditarVendedor.setVisibility(View.GONE);
        //boton y pantalla visible
        aniadirVendedor_btn.setVisibility(View.VISIBLE);
        irAdministrarVendedor.setVisibility(View.VISIBLE);
    }

    public boolean registrarVendedor() {
        EditText nombreVendedor;
        EditText apellidoVendedor;
        EditText cedulaVendedor;
        EditText diaNacimientoVendedor;
        EditText mesNacimientoVendedor;
        EditText anioNacimientoVendedor;
        EditText telefonoVendedor;
        EditText correoVendedor;
        EditText contraseniaVendedor;
        EditText confirmarContraseniaVendedor;
        EditText horaEntradaVendedor;
        EditText horaSalidaVendedor;
        EditText horaAlmuerzoVendedor;
        int c = 0;
        //todo
        //insertar el color rojo para campos vacios y mensajes emergentes para lo mismo
        if(foto == null){
            Toast.makeText(mainView.getContext(), "Campo vacío: *Imagen*", Toast.LENGTH_SHORT).show();
            c++;
        }

        nombreVendedor = mainView.findViewById(R.id.nombreVendedor_etxt);
        apellidoVendedor = mainView.findViewById(R.id.apellidoVendedor_etxt);
        String nombreCampoVendedor_str = nombreVendedor.getText().toString();
        String apellidoCampoVendedor_str = apellidoVendedor.getText().toString();
        String nombreCompletoVendedor_str = "";
        if (nombreCampoVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Nombre*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (apellidoCampoVendedor_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Apellido*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                nombreCompletoVendedor_str = nombreVendedor.getText().toString() + "" + apellidoVendedor.getText().toString();
            }
        }

        cedulaVendedor = mainView.findViewById(R.id.cedulaVendedor_etxt);
        String cedulaVendedor_str = cedulaVendedor.getText().toString();
        if (cedulaVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Cédula*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (cedulaVendedor_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                cedulaVendedor.setText("");
                c++;
            }
        }

        anioNacimientoVendedor = mainView.findViewById(R.id.anioNacimientoVendedor_etxt);
        String anio_str = anioNacimientoVendedor.getText().toString();
        int anio = -1;
        if (anio_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            anio = Integer.parseInt(anio_str);
            if (anio < 1900 || anio > 2003) {
                Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                anioNacimientoVendedor.setText("");
                c++;
            }
        }

        mesNacimientoVendedor = mainView.findViewById(R.id.mesNacimientoVendedor_etxt);
        String mes_str = mesNacimientoVendedor.getText().toString();
        int mes = -1;
        if (mes_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Mes*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            mes = Integer.parseInt(mes_str);
            if (mes < 1 || mes > 12) {
                Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                mesNacimientoVendedor.setText("");
                c++;
            }
        }

        diaNacimientoVendedor = mainView.findViewById(R.id.diaNacimientoVendedor_etxt);
        String dia_str = diaNacimientoVendedor.getText().toString();
        int dia;
        if (dia_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            dia = Integer.parseInt(dia_str);
            if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                diaNacimientoVendedor.setText("");
                c++;
            }
        }

        telefonoVendedor = mainView.findViewById(R.id.telefonoVendedor_etxt);
        String telefonoVendedor_str = telefonoVendedor.getText().toString();
        if (telefonoVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Teléfono*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (telefonoVendedor_str.length() != 10) {
                Toast.makeText(mainView.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                telefonoVendedor.setText("");
                c++;
            }
        }

        correoVendedor = mainView.findViewById(R.id.correoVendedor_etxt);
        String correoVendedor_str = correoVendedor.getText().toString();
        if (correoVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Correo*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (!Patioventainterfaz.validarMail(correoVendedor_str)) {
                Toast.makeText(mainView.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
                correoVendedor.setText("");
                c++;
            }
        }

        contraseniaVendedor = mainView.findViewById(R.id.contraseniaVendedor_etxt);
        confirmarContraseniaVendedor = mainView.findViewById(R.id.confirmarContraseniaVendedor_etxt);
        String contraseniaVendedor_str = contraseniaVendedor.getText().toString();
        String confirmarContraseniaVendedor_str = confirmarContraseniaVendedor.getText().toString();
        if (contraseniaVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Contraseña*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            if (confirmarContraseniaVendedor_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Confirmar Contraseña*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                if (contraseniaVendedor_str.compareTo(confirmarContraseniaVendedor_str) != 0) {
                    Toast.makeText(mainView.getContext(), "Las claves no coinciden", Toast.LENGTH_SHORT).show();
                    contraseniaVendedor.setText("");
                    confirmarContraseniaVendedor.setText("");
                    c++;
                }
            }
        }

        horaEntradaVendedor = mainView.findViewById(R.id.horaEntradaVendedor_etxt);
        String horaEntradaVendedor_str = horaEntradaVendedor.getText().toString();
        int horaEntradaVendedor_int = -1;
        if (horaEntradaVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Entrada*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            horaEntradaVendedor_int = Integer.parseInt(horaEntradaVendedor.getText().toString());
            if (horaEntradaVendedor_int < 0 || horaEntradaVendedor_int > 24) {
                Toast.makeText(mainView.getContext(), "Hora de entrada inválida", Toast.LENGTH_SHORT).show();
                horaEntradaVendedor.setText("");
                c++;
            }
        }

        horaAlmuerzoVendedor = mainView.findViewById(R.id.horaAlmuerzoVendedor_etxt);
        String horaAlmuerzoVendedor_str = horaAlmuerzoVendedor.getText().toString();
        int horaAlmuerzoVendedor_int = -1;
        if (horaAlmuerzoVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Almuerzo*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            horaAlmuerzoVendedor_int = Integer.parseInt(horaAlmuerzoVendedor.getText().toString());
            if (horaAlmuerzoVendedor_int < 0 || horaAlmuerzoVendedor_int > 24) {
                Toast.makeText(mainView.getContext(), "Hora de almuerzo inválida", Toast.LENGTH_SHORT).show();
                horaAlmuerzoVendedor.setText("");
                c++;
            }
        }

        horaSalidaVendedor = mainView.findViewById(R.id.horaSalidaVendedor_etxt);
        String horaSalidaVendedor_str = horaSalidaVendedor.getText().toString();
        int horaSalidaVendedor_int = -1;
        if (horaSalidaVendedor_str.isEmpty()) {
            Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Salida*", Toast.LENGTH_SHORT).show();
            c++;
        } else {
            horaSalidaVendedor_int = Integer.parseInt(horaSalidaVendedor.getText().toString());
            if (horaSalidaVendedor_int < 0 || horaSalidaVendedor_int > 24) {
                Toast.makeText(mainView.getContext(), "Hora de salida inválida", Toast.LENGTH_SHORT).show();
                horaSalidaVendedor.setText("");
                c++;
            }
        }

        if (c == 0) {
            Date fecha = null;
            try {
                fecha = Patioventainterfaz.sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            StorageReference filePath = mStorageRef.child("Vendedores").child(cedulaVendedor_str+".jpg");
            filePath.putFile(foto);

            Vendedor vendedor = new Vendedor(
                    cedulaVendedor_str + ".jpg",
                    horaEntradaVendedor_int,
                    horaSalidaVendedor_int,
                    horaAlmuerzoVendedor_int,
                    patio,
                    nombreCompletoVendedor_str,
                    cedulaVendedor_str,
                    telefonoVendedor_str,
                    correoVendedor_str,
                    contraseniaVendedor_str,
                    fecha);
            patio.aniadirUsuario(vendedor, "Vendedor");

            try {
                if (patio.buscarVendedores("Cedula", vendedor.getCedula()) != null) {
                    Toast.makeText(mainView.getContext(), "Se añadió el vendedor correctamente", Toast.LENGTH_SHORT).show();
                    regresarPantallaPrncipal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void visualizarVendedor() {
        irRegistrarVendedor.setVisibility(View.GONE);
        irAdministrarVendedor.setVisibility(View.GONE);
        irEditarVendedor.setVisibility(View.GONE);
        aniadirVendedor_btn.setVisibility(View.GONE);
        irVisualizarVendedor.setVisibility(View.VISIBLE);

        TextView nombre = mainView.findViewById(R.id.nombre_vendedor_txt);
        TextView fechaNacimiento = mainView.findViewById(R.id.fecha_nacimiento_vendedor_txt);
        TextView cedula = mainView.findViewById(R.id.cedula_vendedor_txt);
        TextView telefono = mainView.findViewById(R.id.telefono_vendedor_txt);
        TextView correo = mainView.findViewById(R.id.correo_vendedor_txt);
        TextView entrada = mainView.findViewById(R.id.entrada_vendedor_txt);
        TextView almuerzo = mainView.findViewById(R.id.almuerzo_vendedor_txt);
        TextView salida = mainView.findViewById(R.id.salida_vendedor_txt);

        nombre.setText(venMostrar.getNombre());
        fechaNacimiento.setText(Patioventainterfaz.getFechaMod(venMostrar.getFechaNacimiento()));
        cedula.setText(venMostrar.getCedula());
        telefono.setText(venMostrar.getTelefono());
        correo.setText(venMostrar.getCorreo());
        entrada.setText(String.format("%d:00 %s", venMostrar.getHoraEntrada(), formatoHora(venMostrar.getHoraEntrada())));
        almuerzo.setText(String.format("%d:00 %s", venMostrar.getHoraComida(), formatoHora(venMostrar.getHoraComida())));
        salida.setText(String.format("%d:00 %s", venMostrar.getHoraSalida(), formatoHora(venMostrar.getHoraSalida())));

        Button habilitar = mainView.findViewById(R.id.deshabilitar_vendedor_btn);
        if (venMostrar.getActivo()) {
            habilitar.setText("Deshabilitar");
        } else {
            habilitar.setText("Habilitar");
        }
        StorageReference filePath = mStorageRef.child("Vendedores/" + venMostrar.getImagen());

        try {
            final File localFile = File.createTempFile(venMostrar.getImagen(), "jpg");
            filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                imagenPerfil_img.setImageBitmap(bitmap);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verVendedorEditable() {
        try {
            Vendedor cedulaVen = venMostrar;
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);
            EditText entrada_ed = mainView.findViewById(R.id.horaEntradaEditVendedor_etxt);
            EditText almuerzo_ed = mainView.findViewById(R.id.horaAlmuerzoEditVendedor_etxt);
            EditText salida_ed = mainView.findViewById(R.id.horaSalidaEditVendedor_etxt);

            String fechaNacimiento = Patioventainterfaz.getFechaMod(cedulaVen.getFechaNacimiento());
            String dia = fechaNacimiento.split("-")[0];
            String mes = fechaNacimiento.split("-")[1];
            String anio = fechaNacimiento.split("-")[2];
            nombre_ed.setText(cedulaVen.getNombre());
            dia_ed.setText(dia);
            mes_ed.setText(mes);
            anio_ed.setText(anio);
            cedula_ed.setText(cedulaVen.getCedula());
            telefono_ed.setText(cedulaVen.getTelefono());
            correo_ed.setText(cedulaVen.getCorreo());
            entrada_ed.setText(String.valueOf(cedulaVen.getHoraEntrada()));
            almuerzo_ed.setText(String.valueOf(cedulaVen.getHoraComida()));
            salida_ed.setText(String.valueOf(cedulaVen.getHoraSalida()));

            StorageReference filePath = mStorageRef.child("Vendedores/" + cedulaVen.getImagen());

            try {
                final File localFile = File.createTempFile(cedulaVen.getImagen(), "jpg");
                filePath.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imagenPerfilVendedorEdit_btn.setImageBitmap(bitmap);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "No se puede mostrar la información", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean editarVendedor() {
        try {
            int c = 0;
            Vendedor cedulaVen = venMostrar;
            EditText nombre_ed = mainView.findViewById(R.id.nombreEditVendedor_etxt);
            String nombre_str = nombre_ed.getText().toString();
            if (nombre_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Nombre*", Toast.LENGTH_SHORT).show();
                c++;
            }

            EditText anio_ed = mainView.findViewById(R.id.anioNacimientoEditVendedor_etxt);
            String anio_str = anio_ed.getText().toString();
            int anio = -1;
            if (anio_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Año*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                anio = Integer.parseInt(anio_ed.getText().toString());
                if (anio < 1900 || anio > 2003) {
                    Toast.makeText(mainView.getContext(), "Año inválido", Toast.LENGTH_SHORT).show();
                    anio_ed.setText("");
                    c++;
                }
            }
            EditText mes_ed = mainView.findViewById(R.id.mesNacimientoEditVendedor_etxt);
            String mes_str = mes_ed.getText().toString();
            int mes = -1;
            if (mes_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Mes*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                mes = Integer.parseInt(mes_str);
                if (mes < 1 || mes > 12) {
                    Toast.makeText(mainView.getContext(), "Mes inválido", Toast.LENGTH_SHORT).show();
                    mes_ed.setText("");
                    c++;
                }
            }

            EditText dia_ed = mainView.findViewById(R.id.diaNacimientoEditVendedor_etxt);
            String dia_str = dia_ed.getText().toString();
            int dia;
            if (dia_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Día*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                dia = Integer.parseInt(dia_str);
                if (!Patioventainterfaz.validarDia(anio, mes, dia)) {
                    Toast.makeText(mainView.getContext(), "Día inválido", Toast.LENGTH_SHORT).show();
                    dia_ed.setText("");
                    c++;
                }
            }

            EditText cedula_ed = mainView.findViewById(R.id.cedulaEditVendedor_etxt);
            String cedula_str = cedula_ed.getText().toString();
            if (cedula_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Cédula*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                if (cedula_str.length() != 10) {
                    Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                    cedula_ed.setText("");
                }
            }

            EditText telefono_ed = mainView.findViewById(R.id.telefonoEditVendedor_etxt);
            String telefono_str = telefono_ed.getText().toString();
            if (telefono_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Teléfono*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                if (telefono_str.length() != 10) {
                    Toast.makeText(mainView.getContext(), "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
                    telefono_ed.setText("");
                    c++;
                }
            }

            EditText correo_ed = mainView.findViewById(R.id.correoEditVendedor_etxt);
            String correo_str = correo_ed.getText().toString();
            if (correo_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Correo*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                if (!Patioventainterfaz.validarMail(correo_str)) {
                    Toast.makeText(mainView.getContext(), "Correo no valido", Toast.LENGTH_SHORT).show();
                    correo_ed.setText("");
                    c++;
                }
            }

            EditText horaEntrada_ed = mainView.findViewById(R.id.horaEntradaEditVendedor_etxt);
            String horaEntradaVendedor_str = horaEntrada_ed.getText().toString();
            int horaEntradaVendedor_int = -1;
            if (horaEntradaVendedor_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Entrada*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                horaEntradaVendedor_int = Integer.parseInt(horaEntrada_ed.getText().toString());
                if (horaEntradaVendedor_int < 0 || horaEntradaVendedor_int > 24) {
                    Toast.makeText(mainView.getContext(), "Hora de entrada inválida", Toast.LENGTH_SHORT).show();
                    horaEntrada_ed.setText("");
                    c++;
                }
            }

            EditText horaAlmuerzo_ed = mainView.findViewById(R.id.horaAlmuerzoEditVendedor_etxt);
            String horaAlmuerzoVendedor_str = horaAlmuerzo_ed.getText().toString();
            int horaAlmuerzoVendedor_int = -1;
            if (horaAlmuerzoVendedor_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Almuerzo*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                horaAlmuerzoVendedor_int = Integer.parseInt(horaAlmuerzo_ed.getText().toString());
                if (horaAlmuerzoVendedor_int < 0 || horaAlmuerzoVendedor_int > 24) {
                    Toast.makeText(mainView.getContext(), "Hora de almuerzo inválida", Toast.LENGTH_SHORT).show();
                    horaAlmuerzo_ed.setText("");
                    c++;
                }
            }

            EditText horaSalida_ed = mainView.findViewById(R.id.horaSalidaEditVendedor_etxt);
            String horaSalidaVendedor_str = horaSalida_ed.getText().toString();
            int horaSalidaVendedor_int = -1;
            if (horaSalidaVendedor_str.isEmpty()) {
                Toast.makeText(mainView.getContext(), "Campo vacío: *Hora de Salida*", Toast.LENGTH_SHORT).show();
                c++;
            } else {
                horaSalidaVendedor_int = Integer.parseInt(horaSalida_ed.getText().toString());
                if (horaSalidaVendedor_int < 0 || horaSalidaVendedor_int > 24) {
                    Toast.makeText(mainView.getContext(), "Hora de salida inválida", Toast.LENGTH_SHORT).show();
                    horaSalida_ed.setText("");
                    c++;
                }
            }

            if (c == 0) {
                String fecha = dia_str + "-" + mes_str + "-" + anio_str;
                if(foto!=null){
                    StorageReference filePath = mStorageRef.child("Vendedores").child(cedula_ed.getText().toString() + ".jpg");
                    filePath.putFile(foto);
                }
                cedulaVen.cambiarDatosSinClaveVendedor(
                        horaEntradaVendedor_int,
                        horaAlmuerzoVendedor_int,
                        horaSalidaVendedor_int,
                        nombre_ed.getText().toString(),
                        cedula_ed.getText().toString(),
                        telefono_ed.getText().toString(),
                        correo_ed.getText().toString(),
                        fecha);
                cedulaVen.setImagen(String.format("%s.jpg", cedula_ed.getText().toString()));
                try {
                    if (patio.buscarVendedores("Cedula", cedulaVen.getCedula()) != null) {
                        Toast.makeText(mainView.getContext(), "Se actualizaron los datos correctamente", Toast.LENGTH_SHORT).show();
                        irVisualizarVendedor.setVisibility(View.GONE);
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "No se pudo actualizar la información del vendedor", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_GALERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                foto = data.getData();
                if (editarImagen) {
                    imagenPerfilVendedorEdit_btn.setImageURI(foto);
                    editarImagen = false;
                } else {
                    imagenPerfilVendedor_btn.setImageURI(foto);
                }
            } else {
                Toast.makeText(mainView.getContext(), "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void itemClick(String cedula) {
        try {
            venMostrar = patio.buscarVendedores("Cedula", cedula);
            visualizarVendedor();
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "No se puede mostrar la lista", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {return false; }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toUpperCase();
        try{
            adptadorlistaview.busqueda(newText);
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
