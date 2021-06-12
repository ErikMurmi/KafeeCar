package com.prog.kafeecar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.GregorianCalendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.String.*;

public class Patioventainterfaz extends AppCompatActivity {

    public static PatioVenta patioventa = new PatioVenta();
    public static Usuario usuarioActual = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    private final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private Uri foto;

    private ImageButton reg_img;

    ImageButton imagenPerfilVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        try {
            cargarDatos();

        } catch (Exception e) {
            Toast.makeText(Patioventainterfaz.this, "Datos no quemados", Toast.LENGTH_SHORT).show();
        }

        //Mensajes de informacion emergentes
        Toast.makeText(Patioventainterfaz.this, "Datos quemados", Toast.LENGTH_SHORT).show();

        if (patioventa.getAdministrador() == null) {
            setContentView(R.layout.registrar_admin_lyt);
            reg_img = findViewById(R.id.reg_imagen_admin_btn);
            Button reg_list = findViewById(R.id.reg_list_btn);
            reg_list.setOnClickListener(v -> {
                try {
                    registrarAdministrador();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public static void cargarDatos() throws Exception {
        System.out.println("\t 2. Lista de vehiculos \n");
        patioventa.aniadirVehiculo(new Vehiculo("PSD-1234", "Y3553", "Mercedes", "GLB SUV", "Gris", "Tiene 5 años de uso", 70000, 95000, 80000, true, 2016, "PSD-1234.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("GHC-2434", "I3748", "Chevrolet", "Cruze", "Blanco", "Muestra un choque en la parte lateral", 17000, 25000, 18500, true, 2014, "GHC-2434.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IMH-2233", "O8394", "Hyundai", "Elantra", "Rojo", "Automático", 14000, 15000, 0, false, 2013, "O8394.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("PHG-1225", "382I83", "Ford", "EcoSport", "Negro", "Exelente estado documentación y matrícula al día", 14000, 15800, 0, true, 2014, "382I83.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("PCR-1832", "38J382", "Chevrolet", "Optra", "Negro", "EL dueño lo vende por viaje", 5000, 6000, 0, true, 2004, "38J382.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("GUN-1764", "283I32", "Chevrolet", "Sail", "Negro", "Cahsis de sedán y mecaanismo manual ", 13000, 16000, 0, true, 2019, "283I32.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IPO-1963", "23JW22", "Jeep", "Compass Sport", "Gris", "Autómático poco uso", 70000, 95000, 80000, true, 2017, "23JW22.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("IOS-1275", "M382N3", "Hyundai", "Tucson", "Gris", "Vidrios electricos radio de pantalla", 17000, 20300, 0, true, 2015, "M382N3.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("HPO-2517", "JD8382", "Chevrolet", "DMAX Optima", "Gris", "Camioneta una cavina", 14500, 16500, 16000, true, 2013, "JD8382.jpg"));
        patioventa.aniadirVehiculo(new Vehiculo("SGD-0916", "D3828E", "Hyundai", "HD270", "Blanca", "Volqueta para trabajo", 40000, 42000, 41500, true, 2011, "D3828E.jpg"));
        System.out.println("Se añadieron los 10 vehículos ");
        System.out.println("\t 2. Lista de Vendedores \n");
        Vendedor admin = new Vendedor("1721053207.jpg",8, 17, 13, patioventa, "Juan Jácome", "1721053207", "1721053207", "juanj@gmail.com", "clave", sdf.parse("2006-06-05"));
        //patioventa.aniadirUsuario(new Vendedor("1721053207.jpg",8, 17, 13, patioventa, "Juan Jácome", "1721053207", "1721053207", "juanj@gmail.com", "clave", sdf.parse("05-06-2006")), "Vendedor");
        patioventa.aniadirUsuario(admin,"Administrador");
        patioventa.aniadirUsuario((new Vendedor("1732221032.jpg",8, 17, 13, patioventa, "Elizabeth Perez", "1732221032", "1721053207", "eli.perez@gmail.com", "Spe123", sdf.parse("09-05-2000"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("1721835213.jpg",8, 17, 13, patioventa, "David Montalvo", "1721835213", "1721053207", "david_m@gmail.com", "DM12pc", sdf.parse("19-02-2001"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("1928364726.jpg",8, 17, 13, patioventa, "Luiz Velasquez", "1928364726", "1721053207", "luisvelasquesz@outlook.es", "super1015", sdf.parse("12-01-1990"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor("0923837273.jpg",8, 17, 13, patioventa, "Jessica Alvarez", "1721053207", "0923837273", "jessyesperanza@gmail.com", "0912jessy", sdf.parse("08-4-2001"))), "Vendedor");
        patioventa.aniadirUsuario(new Cliente("Daniel", "175014048", "0999548928", "daniel@gmail.com", "1207", sdf.parse("08-4-2001")), "Cliente");
        patioventa.aniadirUsuario(new Cliente("Erik", "1750115623", "0999548928", "erik@gmail.com", "1207", sdf.parse("08-4-2001")), "Cliente");
        patioventa.aniadirUsuario(new Cliente("Diana", "1750115233", "0995648998", "diana@gmail.com", "1207", sdf.parse("08-4-2001")), "Cliente");
        System.out.println("Se añadieron 5 vendedores ");
        System.out.println("*********************************");
        System.out.println("\t 2. Lista de citas \n");
        Date fechaCita = new Date(2021, 05, 20);
        Date fechaCita1 = new Date(2021, 05, 20);
        Date fechaCita2 = new Date(2021, 05, 20);
        Date fechaCita3 = new Date(2021, 05, 20);
        Date fechaCita4 = new Date(2021, 05, 20);
        Date fechaCita5 = new Date(2021, 05, 20);
        Cita c1 = new Cita(fechaCita, 10, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Cita c2 = new Cita(fechaCita1, 14, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(2));
        Cita c3 = new Cita(fechaCita2, 16, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(3));
        Cita c4 = new Cita(fechaCita3, 9, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Cita c5 = new Cita(fechaCita4, 12, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(2));
        Cita c6 = new Cita(fechaCita5, 8, " ", (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(3));
        patioventa.aniadirCita(c1);
        patioventa.aniadirCita(c2);
        patioventa.aniadirCita(c3);
        patioventa.aniadirCita(c4);
        patioventa.aniadirCita(c5);
        patioventa.aniadirCita(c6);
        System.out.println("Se añadieron 6 citas");
        System.out.println("*********************************");
        Venta ven1 = new Venta(fechaCita, 24012.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Venta ven2 = new Venta(fechaCita1, 486145.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(2));
        Venta ven3 = new Venta(fechaCita2, 89695.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Venta ven4 = new Venta(fechaCita3, 79832.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(1));
        Venta ven5 = new Venta(fechaCita4, 102365.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(2));
        Venta ven6 = new Venta(fechaCita5, 798450.8f, (Cliente) patioventa.getClientes().getPos(1), (Vendedor) patioventa.getVendedores().getPos(1), (Vehiculo) patioventa.getVehiculos().getPos(3));
        patioventa.aniadirVenta(ven1);
        patioventa.aniadirVenta(ven2);
        patioventa.aniadirVenta(ven3);
        patioventa.aniadirVenta(ven4);
        patioventa.aniadirVenta(ven5);
        patioventa.aniadirVenta(ven6);

        System.out.println("Se añadieron 6 ventas");
    }

    private void irAplicacion(String tipo) {
        if (tipo.compareTo("ADMIN") == 0) {
            setContentView(R.layout.home_admin);
            BottomNavigationView navBar = findViewById(R.id.barra_nav);
            navBar.setOnNavigationItemSelectedListener(navListener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Admin_Fragment()).commit();
        } else if (tipo.compareTo("VENDEDOR") == 0) {
            setContentView(R.layout.home_vendedor);
            BottomNavigationView navBar = findViewById(R.id.barra_nav_vendedor);
            navBar.setOnNavigationItemSelectedListener(nav_vendedor_Listener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Admin_Fragment()).commit();
        } else if (tipo.compareTo("CLIENTE") == 0) {
            setContentView(R.layout.home_cliente);
            BottomNavigationView navBar = findViewById(R.id.barra_nav_cliente);
            navBar.setOnNavigationItemSelectedListener(nav_cliente_Listener);
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Admin_Fragment()).commit();
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragement = null;
                    switch (item.getItemId()) {
                        case R.id.nav_citas:
                            selectedFragement = new Citas_Fragment();
                            break;
                        case R.id.nav_cat:
                            selectedFragement = new Catalogo_Admin_Fragment();
                            break;
                        case R.id.nav_vendedores:
                            selectedFragement = new Vendedores_Admin_Fragment();
                            break;
                        case R.id.nav_admin_perfil:
                            selectedFragement = new Perfil_admin_Fragment();
                            break;
                        case R.id.nav_ventas:
                            selectedFragement = new Ventas_admin_Fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                    return true;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener nav_cliente_Listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragement = null;
                    switch (item.getItemId()) {
                        case R.id.nav_cat_cl:
                            selectedFragement = new Catalogo_Admin_Fragment();
                            break;
                        case R.id.nav_citas_cl:
                            selectedFragement = new Citas_cliente_fragment();
                            break;
                        case R.id.nav_fav_cl:
                            selectedFragement = new Favoritos_cliente_fragment();
                            break;
                        case R.id.nav_perfil_cl:
                            selectedFragement = new Perfil_cliente_fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                    return true;
                }
            };
    private BottomNavigationView.OnNavigationItemSelectedListener nav_vendedor_Listener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragement = null;
                    switch (item.getItemId()) {
                        case R.id.nav_cat_ven:
                            selectedFragement = new Catalogo_Admin_Fragment();
                            break;
                        case R.id.nav_citas_ven:
                            selectedFragement = new Citas_vendedor_fragment();
                            break;
                        case R.id.nav_ventas_ven:
                            selectedFragement = new Ventas_vendedor_fragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                    return true;
                }
            };


    public void logIn(View v) throws Exception {

        String msg = "";
        String tipo = "";
        EditText correo = findViewById(R.id.email_etxt);
        EditText clave = findViewById(R.id.clave_etxt);
        String correo_str = correo.getText().toString();
        String clave_str = clave.getText().toString();
        Usuario usuario;
        if (patioventa.getAdministrador() != null && correo_str.compareTo(patioventa.getAdministrador().getCorreo()) == 0) {
            usuario = patioventa.getAdministrador();
            tipo = "ADMIN";
        } else {
            usuario = patioventa.buscarVendedores("Correo", correo_str);
            if (usuario == null) {
                usuario = patioventa.buscarClientes("Correo", correo_str);
                tipo = "CLIENTE";
            } else {
                tipo = "VENDEDOR";
            }
        }

        if (usuario != null) {
            if (usuario.getClave().compareTo(clave_str) == 0) {
                msg = "Se ha iniciado secion correctamente";
                usuarioActual = usuario;
                irAplicacion(tipo);
            } else {
                msg = "Contraseña incorrecta";
            }

        } else {
            msg = "Correo no registrado";
        }
        Toast.makeText(Patioventainterfaz.this, msg, Toast.LENGTH_SHORT).show();
    }


    public void visualizarCita(View v) throws Exception {
        //setTheme(R.style.Theme_KafeeCar_Diseno);
        //setContentView(R.layout.cita);
        TextView fecha = findViewById(R.id.fechaCita_txt);
        TextView hora = findViewById(R.id.horaCita_txt);
        TextView cliente = findViewById(R.id.clienteCita_txt);
        TextView contacto = findViewById(R.id.contactoCita_txt);
        TextView vendedor = findViewById(R.id.vendedorCita_txt);
        TextView vehiculo = findViewById(R.id.vehiculoCita_txt);
        TextView descripcion = findViewById(R.id.descripcionCita_txt);
        TextView resolucion = findViewById(R.id.resolucionCita_txt);
        TextView precio = findViewById(R.id.precioVentaCita_txt);
        Cita citaPrueba = (Cita) patioventa.getCitas().getPos(0);
        fecha.setText(new String(format(fecha.getText().toString() + getFechaMod(citaPrueba.getFechaCita()))));
        hora.setText(new String(hora.getText().toString() + citaPrueba.getHora()));
        cliente.setText(new String(cliente.getText().toString() + citaPrueba.getVisitante().getNombre()));
        contacto.setText(new String(contacto.getText().toString() + citaPrueba.getVisitante().getTelefono()));
        vendedor.setText(new String(vendedor.getText().toString() + citaPrueba.getVendedorCita().getNombre()));
        vehiculo.setText(new String(vehiculo.getText().toString() + citaPrueba.getVehiculo().getModelo()));
        descripcion.setText(new String(descripcion.getText().toString() + citaPrueba.getVehiculo().getDescripcion()));
        resolucion.setText(new String(resolucion.getText().toString() + citaPrueba.getResolucion()));
        precio.setText(new String(precio.getText().toString() + " $" + citaPrueba.getVehiculo().getPrecioVenta()));

    }


    public void citaNueva(View v) throws Exception {
        EditText textodia = findViewById(R.id.dia_etxt);
        EditText textomes = findViewById(R.id.mes_etxt);
        EditText textoanio = findViewById(R.id.anio_etxt);
        EditText textohoras = findViewById(R.id.hora_etxt);
        String dia_str = textodia.getText().toString();
        String mes_str = textomes.getText().toString();
        String anio_str = textoanio.getText().toString();
        String horas_str = textohoras.getText().toString();
        Date fecha = sdf.parse(anio_str + "-" + mes_str + "-" + dia_str);
        int entero = Integer.parseInt(horas_str);
        Vendedor vendedor = patioventa.buscarVendedores("Cedula", "1800370809");
        Vehiculo vehiculo = patioventa.buscarVehiculos("Matricula", "A001");
        Cliente cliente = patioventa.buscarClientes("Cedula", "1752866291");
        Cita aux = new Cita(fecha, entero, "", cliente, vendedor, vehiculo);
        patioventa.aniadirCita(aux);
        if (patioventa.getCitas().contiene(aux)) {
            Toast.makeText(Patioventainterfaz.this, "Se agrego correctamente.", Toast.LENGTH_SHORT).show();
        }

    }


    public void registarCita(View v) throws Exception {
        //EditText cliente = findViewById(R.id.cliente_txt);
        //EditText vendedor = findViewById(R.id.vendedor_txt);
        EditText resolucion = findViewById(R.id.resolucion_txt);
        EditText fechacitadia = findViewById(R.id.fechacitadia_txt);
        EditText fechacitames = findViewById(R.id.fechacitames_txt);
        EditText fechacitaanio = findViewById(R.id.fechacitaanio_txt);
        EditText fechacitahora = findViewById(R.id.fechacitahoracita_txt);
        // EditText auto=findViewById(R.id.vehiculo_txt);
        String fechacita_str = fechacitaanio.getText().toString() + "-" + fechacitames.getText().toString() + "-" + fechacitadia.getText().toString();
        //String cliente_str = cliente.getText().toString();
        //String vendedor_str = vendedor.getText().toString();
        String resolucion_str = resolucion.getText().toString();
        //String vehiculo_str = auto.getText().toString();
        Vendedor vendedor = patioventa.buscarVendedores("Cedula", "1800370809");
        Vehiculo vehiculo = patioventa.buscarVehiculos("Matricula", "A001");
        Cliente cliente = patioventa.buscarClientes("Cedula", "1752866291");
        int hora = Integer.parseInt(fechacitahora.getText().toString());

        Cita nueva = new Cita(sdf.parse(fechacita_str), hora, resolucion_str, cliente, vendedor, vehiculo);
        patioventa.aniadirCita(nueva);
        if (patioventa.getCitas().contiene(nueva)) {
            Toast.makeText(Patioventainterfaz.this, "Se agrego correctamente.", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFechaMod(Date fechaMod) {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        return sf.format(fechaMod);
    }

    //Seleccion de imagen en la galeria

    public void openGalery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }

    public void botonImagenPerfilVendedor(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(Patioventainterfaz.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGalery();
            } else {
                ActivityCompat.requestPermissions(Patioventainterfaz.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        } else {
            openGalery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_GALERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                foto = data.getData();
                reg_img.setImageURI(foto);
            } else {
                Toast.makeText(Patioventainterfaz.this, "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGalery();
            } else {
                Toast.makeText(Patioventainterfaz.this, "Necesita habilitar los permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void irRegistarse(View view) {
        setContentView(R.layout.registrar_usuario);
    }


    public void aniadirCliente(View v) {
        EditText textonombre;
        EditText textocedula;
        EditText textodia;
        EditText textomes;
        EditText textoanio;
        EditText textotelefono;
        EditText textocorreo;
        EditText textoclave;
        EditText textorepetirclave;
        int c = 0;

        textonombre = findViewById(R.id.nombre_etxt);
        String nombre_str = textonombre.getText().toString();

        textocedula = findViewById(R.id.cedula_etxt);
        String cedula_str = textocedula.getText().toString();
        if (cedula_str.length() != 10) {
            Toast.makeText(Patioventainterfaz.this, "Numero de cedula invalido", Toast.LENGTH_SHORT).show();
            textocedula.setText("");
            c++;
        }

        textotelefono = findViewById(R.id.telefono_etxt);
        String telefono_str = textotelefono.getText().toString();
        if (telefono_str.length() != 10) {
            Toast.makeText(Patioventainterfaz.this, "Numero de telefono invalido", Toast.LENGTH_SHORT).show();
            textotelefono.setText("");
            c++;
        }

        textocorreo = findViewById(R.id.correo_etxt);
        String correo_str = textocorreo.getText().toString();
        if (!validarMail(correo_str)) {
            Toast.makeText(Patioventainterfaz.this, "Correo no valido", Toast.LENGTH_SHORT).show();
            textocorreo.setText("");
            c++;
        }

        textoclave = findViewById(R.id.claveuser_etxt);
        String clave_str = textoclave.getText().toString();
        textorepetirclave = findViewById(R.id.repetirclaveuser_etxt);
        String repetirclave_str = textorepetirclave.getText().toString();
        if (clave_str.compareTo(repetirclave_str) != 0) {
            Toast.makeText(Patioventainterfaz.this, "Las claves no coinciden", Toast.LENGTH_SHORT).show();
            textoclave.setText("");
            textorepetirclave.setText("");
            c++;
        }


        textomes = findViewById(R.id.mes_etxt);
        String mes_str = textomes.getText().toString();
        int mes = Integer.parseInt(mes_str);
        if (mes < 1 || mes > 12) {
            Toast.makeText(Patioventainterfaz.this, "mes invalido", Toast.LENGTH_SHORT).show();
            textomes.setText("");
            c++;
        }

        textoanio = findViewById(R.id.anio_etxt);
        String anio_str = textoanio.getText().toString();
        int anio = Integer.parseInt(anio_str);
        if (anio < 1900 || anio > 2003) {
            Toast.makeText(Patioventainterfaz.this, "año invalido", Toast.LENGTH_SHORT).show();
            textoanio.setText("");
            c++;
        }

        textodia = findViewById(R.id.dia_etxt);
        String dia_str = textodia.getText().toString();
        int dia = Integer.parseInt(dia_str);
        if (!validarDia(anio, mes, dia)) {
            Toast.makeText(Patioventainterfaz.this, "Dia invalido", Toast.LENGTH_SHORT).show();
            textodia.setText("");
            c++;
        }

        if (c == 0) {
            Date fecha = null;
            try {
                fecha = sdf.parse(dia_str + "-" + mes_str + "-" + anio_str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Cliente cliente = new Cliente(nombre_str, cedula_str, telefono_str, correo_str, clave_str, fecha);
            patioventa.aniadirUsuario(cliente, "Cliente");
            try {
                if (patioventa.buscarClientes("Cedula", cliente.getCedula()) != null) {
                    Toast.makeText(Patioventainterfaz.this, "Se aniadio el cliente correctamente", Toast.LENGTH_SHORT).show();
                    irAplicacion("ADMIN");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void aniadirVenta(View v) throws Exception {
        EditText precio = findViewById(R.id.precio_venta_txt);
        EditText clientes = findViewById(R.id.cliente_venta_txt);
        EditText vendedor = findViewById(R.id.vendedor_venta_txt);
        EditText auto = findViewById(R.id.vehiculo_venta_txt);
        EditText fechaventadia = findViewById(R.id.fecha_venta_dia_etxt);
        EditText fechaventames = findViewById(R.id.fecha_venta_mes_etxt);
        EditText fechaventaanio = findViewById(R.id.fecha_venta_anio_etxt);
        EditText fechaventahora = findViewById(R.id.fecha_venta_hora_etxt);
        String fechaventa_str = fechaventaanio.getText().toString() + "-" + fechaventames.getText().toString() + "-" + fechaventadia.getText().toString();
        String clientes_str = clientes.getText().toString();
        String vendedores_str = vendedor.getText().toString();
        String autos_str = auto.getText().toString();
        int hora = Integer.parseInt(fechaventahora.getText().toString());
        float precioventa = Float.parseFloat(precio.getText().toString());
        Cliente clienteventa = patioventa.buscarClientes("Nombre", clientes_str);
        Vendedor vendedorventa = patioventa.buscarVendedores("Nombre", vendedores_str);
        Vehiculo autoventa = patioventa.buscarVehiculos("Matricula", autos_str);


        Venta nueva = new Venta(sdf.parse(fechaventa_str), precioventa, clienteventa, vendedorventa, autoventa);
        patioventa.aniadirVenta(nueva);

        if (patioventa.getVentasGenerales().contiene(nueva)) {
            Toast.makeText(Patioventainterfaz.this, "Se registro la venta.", Toast.LENGTH_SHORT).show();
        }
    }


    public void registrarAdministrador() throws ParseException {
        EditText nombreAdmin = findViewById(R.id.reg_nombre_admin_etxt);
        EditText apellidoAdmin = findViewById(R.id.reg_apellido_admin_etxt);
        EditText cedulaAdmin = findViewById(R.id.reg_cedula_admin_etxt);
        EditText diaNacimientoAdmin = findViewById(R.id.reg_dia_admin_etxt);
        EditText mesNacimientoAdmin = findViewById(R.id.reg_mes_admin_etxt);
        EditText anioNacimientoAdmin = findViewById(R.id.reg_anio_admin_etxt);
        EditText telefonoAdmin = findViewById(R.id.reg_telefono_admin_etxt);
        EditText correoAdmin = findViewById(R.id.reg_correo_admin_etxt);
        EditText contraseniaAdmin = findViewById(R.id.reg_clave_admin_etxt);
        EditText confirmarContraseniaAdmin = findViewById(R.id.reg_claveconfim_admin_etxt);
        EditText horaEntradaAdmin = findViewById(R.id.reg_entrada_admin_etxt);
        EditText horaSalidaAdmin = findViewById(R.id.reg_salida_admin_etxt);
        EditText horaAlmuerzoAdmin = findViewById(R.id.reg_almuerzo_admin_etxt);

        String nombreAdmin_str = nombreAdmin.getText().toString() + "" + apellidoAdmin.getText().toString();
        String cedulaAdmin_str = cedulaAdmin.getText().toString();
        String fechaNacimientoAdmin_date = diaNacimientoAdmin.getText().toString()
                + "-" + mesNacimientoAdmin.getText().toString()
                + "-" + anioNacimientoAdmin.getText().toString();
        String telefonoAdmin_str = telefonoAdmin.getText().toString();
        String correoAdmin_str = correoAdmin.getText().toString();
        String contraseniaAdmin_str = contraseniaAdmin.getText().toString();
        String confirmarContraseniaAdmin_str = confirmarContraseniaAdmin.getText().toString();
        int horaEntradaAdmin_int = Integer.parseInt(horaEntradaAdmin.getText().toString());
        int horaAlmuerzoAdmin_int = Integer.parseInt(horaAlmuerzoAdmin.getText().toString());
        int horaSalidaAdmin_int = Integer.parseInt(horaSalidaAdmin.getText().toString());


        if (contraseniaAdmin_str.compareTo(confirmarContraseniaAdmin_str) == 0) {
            String contraseniaVerificada = contraseniaAdmin_str;
            patioventa.aniadirUsuario( new Vendedor(
                    String.format("%s.jpg",cedulaAdmin_str),
                    horaEntradaAdmin_int,
                    horaSalidaAdmin_int,
                    horaAlmuerzoAdmin_int,
                    patioventa,
                    nombreAdmin_str,
                    cedulaAdmin_str,
                    telefonoAdmin_str,
                    correoAdmin_str,
                    contraseniaVerificada,
                    sdf.parse(fechaNacimientoAdmin_date)), "Vendedor");
            irAplicacion("ADMIN");
            Toast.makeText(Patioventainterfaz.this, "Administrador registrado correctamente",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(Patioventainterfaz.this, "No se agrego el administrador",Toast.LENGTH_SHORT).show();
            contraseniaAdmin.setText("");
            confirmarContraseniaAdmin.setText("");
        }

        StorageReference filePath = mStorageRef.child("Vendedores").child(cedulaAdmin_str+".jpg");
        filePath.putFile(foto).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(Patioventainterfaz.this, "Imagen subida satisfactoriamente",Toast.LENGTH_SHORT).show());
    }



    public static boolean validarMail(String email) {//Valida un mail con un formato, es estático para poder usado en cualquier contexto
        // Patron para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");//El patron que debe seguir el usaurio al ingreasar un mail

        Matcher mather = pattern.matcher(email);//Comprueba si el String ingresado tiene el formato antes mencionado. Si lo cumple devuelve un boleano "true" y si no cumple devuelve "false"
        return mather.find();//Devuelve el boleano
    }


    public boolean validarDia(int anio, int mes, int dia) {
        boolean valido = true;
        int numeroDias = -1;
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numeroDias = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numeroDias = 30;
                break;
            case 2:
                numeroDias = 28;
                if (esBisiesto(anio))
                    numeroDias = 29;
                break;
        }

        if (dia > numeroDias || dia < 1) {
            valido = false;
        }
        return valido;
    }

    public static boolean esBisiesto(int anio) {

        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;
    }

    public void salir(View v){
        usuarioActual =  null;
        setContentView(R.layout.login);
    }


}
