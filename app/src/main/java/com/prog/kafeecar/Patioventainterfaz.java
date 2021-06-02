package com.prog.kafeecar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import static java.lang.String.*;

public class Patioventainterfaz extends AppCompatActivity {

    public static PatioVenta patioventa = new PatioVenta();
    Usuario usuarioActual = null;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final int REQUEST_PERMISSION_CODE = 100;
    private static final int REQUEST_IMAGE_GALERY = 101;
    public static Catalogo_Admin_Fragment adminView = new Catalogo_Admin_Fragment();

    ImageButton imagenPerfilVendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //setTheme(R.style.Theme_AppCompat);
        //setContentView(R.layout.login);
        try {
            cargarDatos();

        } catch (Exception e) {
            Toast.makeText(Patioventainterfaz.this, "Datos no quemados", Toast.LENGTH_SHORT).show();
        }

        //Mensajes de informacion emergentes
        Toast.makeText(Patioventainterfaz.this, "Datos quemados", Toast.LENGTH_SHORT).show();
        /*try {
            //setContentView(R.layout.nuevacita);
            //setContentView(R.layout.registrar_cita);

            //visualizarCita();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        BottomNavigationView navBar = findViewById(R.id.barra_nav);
        navBar.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, new Catalogo_Admin_Fragment()).commit();

    }

    public static void cargarDatos() throws Exception {
        System.out.println("\t 2. Lista de vehiculos \n");
        patioventa.aniadirVehiculo(new Vehiculo("PSD-1234", "Y3553", "Mercedes", "GLB SUV", "Gris", "Tiene 5 años de uso", 70000, 95000, 80000, true, 2016, "Y3553.jpg"));
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
        patioventa.aniadirUsuario((new Vendedor(8, 17, 13, patioventa, "Juan Jácome", "1721053207", "1721053207", "juanj@gmail.com", "clave", sdf.parse("2006-06-05"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor(8, 17, 13, patioventa, "Elizabeth Perez", "1732221032", "1721053207", "eli.perez@gmail.com", "Spe123", sdf.parse("2000-05-09"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor(8, 17, 13, patioventa, "David Montalvo", "1721835213", "1721053207", "david_m@gmail.com", "DM12pc", sdf.parse("2001-02-19"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor(8, 17, 13, patioventa, "Luiz Velasquez", "1928364726", "1721053207", "luisvelasquesz@outlook.es", "super1015", sdf.parse("1990-01-12"))), "Vendedor");
        patioventa.aniadirUsuario((new Vendedor(8, 17, 13, patioventa, "Jessica Alvarez", "0923837273", "1721053207", "jessyesperanza@gmail.com", "0912jessy", sdf.parse("2001-4-08"))), "Vendedor");
        patioventa.aniadirUsuario(new Cliente("Daniel", "175014048", "0999548928", "example", "1207", sdf.parse("2001-4-08")), "Cliente");
        patioventa.aniadirUsuario(new Cliente("Erik", "1750115623", "0999548928", "example", "1207", sdf.parse("2001-4-08")), "Cliente");
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
                            selectedFragement = Patioventainterfaz.getAdminView();
                            break;
                        case R.id.nav_ventas:
                            selectedFragement = new Ventas_Fragment();
                            break;
                        case R.id.nav_estadisticas:
                            selectedFragement = new Estadisticas_Fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frag_contenedor, selectedFragement).commit();
                    return true;
                }
            };

    public static Catalogo_Admin_Fragment getAdminView(){
        return adminView;
    }

    public void logIn(View v) throws Exception {
        //Toast.makeText(this, "Esjecuto el metodo",Toast.LENGTH_SHORT).show();
        EditText correo = findViewById(R.id.email_etxt);
        EditText clave = findViewById(R.id.clave_etxt);
        String correo_str = correo.getText().toString();
        String clave_str = clave.getText().toString();
        Vendedor ven = patioventa.buscarVendedores("Correo",correo_str);

        if(v!=null){
            if(ven.getClave().compareTo(clave_str) == 0){
                usuarioActual = ven;
                Toast.makeText(Patioventainterfaz.this, "Se inicio sesion",Toast.LENGTH_SHORT).show();
            }
        }

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
        precio.setText(new String(precio.getText().toString() + " $"+citaPrueba.getVehiculo().getPrecioVenta()));

    }

    public void visualizarVehiculo(View v) throws Exception {

        TextView titulo = findViewById(R.id.auto_titulo_txt);
        TextView placa = findViewById(R.id.placa_txt);
        TextView matricula =findViewById(R.id.matricula_txt);
        TextView anio = findViewById(R.id.vehiculo_anio_txt);
        TextView marca = findViewById(R.id.vehiculo_marca_txt);
        TextView modelo = findViewById(R.id.vehiculo_modelo_txt);
        TextView color = findViewById(R.id.vehiculo_color_txt);
        TextView descripcion = findViewById(R.id.vehiculo_descripcion_txt);
        TextView precioInicial = findViewById(R.id.vehiculo_pinicial_txt);
        TextView preciVenta = findViewById(R.id.vehiculo_pventa_txt);
        TextView promocion = findViewById(R.id.vehiculo_promocion_txt);
        TextView matriculado = findViewById(R.id.vehiculo_matriculado_txt);

        Vehiculo vMostrar  = (Vehiculo) Patioventainterfaz.patioventa.getVehiculos().getPos(1);
        String titulo_str = vMostrar.getMarca()+" "+vMostrar.getModelo();
        titulo.setText(titulo_str);
        placa.setText(format("Placa: %s", vMostrar.getPlaca()));
        matricula.setText(format(getString(R.string.matricula_frmt), vMostrar.getMatricula()));
        anio.setText(format("Año :%s",vMostrar.getAnio()));
        marca.setText(format("Marca :%s",vMostrar.getMarca()));
        modelo.setText(format("Modelo :%s",vMostrar.getModelo()));
        descripcion.setText(format("Descripción :%s",vMostrar.getDescripcion()));
        color.setText(format("Color :%s",vMostrar.getColor()));
        precioInicial.setText(format("Precio inicial :%.2f",vMostrar.getPrecioInicial()));
        preciVenta.setText(format("Precio venta :%.2f",vMostrar.getPrecioVenta()));
        promocion.setText(format("Precio promoción:%.2f",vMostrar.getPromocion()));
        if(vMostrar.isMatriculado()){
            matriculado.setText("Matriculado: Si");
        }else{
            matriculado.setText("Matriculado: No");
        }


    }



    public void registrarVendedor(View v) throws ParseException {

        EditText nombreVendedor = findViewById(R.id.nombreVendedor_etxt);
        EditText apellidoVendedor = findViewById(R.id.apellidoVendedor_etxt);
        EditText cedulaVendedor = findViewById(R.id.cedulaVendedor_etxt);
        EditText diaNacimientoVendedor = findViewById(R.id.diaNacimientoVendedor_etxt);
        EditText mesNacimientoVendedor = findViewById(R.id.mesNacimientoVendedor_etxt);
        EditText anioNacimientoVendedor = findViewById(R.id.anioNacimientoVendedor_etxt);
        EditText telefonoVendedor = findViewById(R.id.telefonoVendedor_etxt);
        EditText correoVendedor = findViewById(R.id.correoVendedor_etxt);
        EditText contraseniaVendedor = findViewById(R.id.contraseniaVendedor_etxt);
        EditText confirmarContraseniaVendedor = findViewById(R.id.confirmarContraseniaVendedor_etxt);
        EditText horaEntradaVendedor = findViewById(R.id.horaEntradaVendedor_etxt);
        EditText horaSalidaVendedor = findViewById(R.id.horaSalidaVendedor_etxt);
        EditText horaAlmuerzoVendedor = findViewById(R.id.horaAlmuerzoVendedor_etxt);

        String nombreVendedor_str = nombreVendedor.getText().toString() + "" + apellidoVendedor.getText().toString();
        String cedulaVendedor_str = cedulaVendedor.getText().toString();
        String fechaNacimientoVendedor_date = diaNacimientoVendedor.getText().toString()
                + "-" + mesNacimientoVendedor.getText().toString()
                + "-" + anioNacimientoVendedor.getText().toString();
        String telefonoVendedor_str = telefonoVendedor.getText().toString();
        String correoVendedor_str = correoVendedor.getText().toString();
        String contraseniaVendedor_str = contraseniaVendedor.getText().toString();
        String confirmarContraseniaVendedor_str = confirmarContraseniaVendedor.getText().toString();
        int horaEntradaVendedor_int = Integer.parseInt(horaEntradaVendedor.getText().toString());
        int horaAlmuerzoVendedor_int = Integer.parseInt(horaAlmuerzoVendedor.getText().toString());
        int horaSalidaVendedor_int = Integer.parseInt(horaSalidaVendedor.getText().toString());

        if(contraseniaVendedor_str.compareTo(confirmarContraseniaVendedor_str)==0){
            String contraseniaVerificada = contraseniaVendedor_str;
            patioventa.aniadirUsuario(new Vendedor(horaEntradaVendedor_int,horaSalidaVendedor_int,horaAlmuerzoVendedor_int, patioventa,
                    nombreVendedor_str, cedulaVendedor_str, telefonoVendedor_str, correoVendedor_str, contraseniaVerificada,
                    sdf.parse(fechaNacimientoVendedor_date)),"Vendedor");
        }else{
            //Toast.makeText(Patioventainterfaz.this, "Las contraseÃ±as no coinciden. Ingrese Nuevamente.",Toast.LENGTH_SHORT).show();
            contraseniaVendedor.setText("");
            confirmarContraseniaVendedor.setText("");
        }
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
        Date fecha = sdf.parse(anio_str+"-"+mes_str+"-"+dia_str);
        int entero = Integer.parseInt(horas_str);
        Vendedor vendedor = patioventa.buscarVendedores("Cedula","1800370809");
        Vehiculo vehiculo = patioventa.buscarVehiculos("Matricula","A001");
        Cliente cliente = patioventa.buscarClientes("Cedula","1752866291");
        Cita aux = new Cita(fecha,entero,"",cliente,vendedor,vehiculo);
        patioventa.aniadirCita(aux);
        if(patioventa.getCitas().contiene(aux)){
            Toast.makeText(Patioventainterfaz.this, "Se agrego correctamente.",Toast.LENGTH_SHORT).show();
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
        String fechacita_str =  fechacitaanio.getText().toString()+"-"+fechacitames.getText().toString()+"-"+fechacitadia.getText().toString();
        //String cliente_str = cliente.getText().toString();
        //String vendedor_str = vendedor.getText().toString();
        String resolucion_str = resolucion.getText().toString();
        //String vehiculo_str = auto.getText().toString();
        Vendedor vendedor = patioventa.buscarVendedores("Cedula","1800370809");
        Vehiculo vehiculo = patioventa.buscarVehiculos("Matricula","A001");
        Cliente cliente = patioventa.buscarClientes("Cedula","1752866291");
        int hora = Integer.parseInt(fechacitahora.getText().toString());

        Cita nueva = new Cita( sdf.parse(fechacita_str),hora,resolucion_str,cliente,vendedor,vehiculo);
        patioventa.aniadirCita(nueva);
        if(patioventa.getCitas().contiene(nueva)){
            Toast.makeText(Patioventainterfaz.this, "Se agrego correctamente.",Toast.LENGTH_SHORT).show();
        }
    }

    public String getFechaMod(Date fechaMod){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(fechaMod);
    }

    //Seleccion de imagen en la galeria

    private void openGalery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GALERY);
    }
    public void botonImagenPerfilVendedor(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(Patioventainterfaz.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                openGalery();
            }else{
                ActivityCompat.requestPermissions(Patioventainterfaz.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);
            }
        }else{
            openGalery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_GALERY){
            if(resultCode == Activity.RESULT_OK && data != null){
                Uri foto = data.getData();
                imagenPerfilVendedor = findViewById(R.id.imagenPerfilVendedor_ibtn);
                imagenPerfilVendedor.setImageURI(foto);
            }else{
                Toast.makeText(Patioventainterfaz.this, "No se ha insertado la imagen", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE){
            if(permissions.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGalery();
            }else{
                Toast.makeText(Patioventainterfaz.this,"Necesita habilitar los permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void irRegistarse(View view) {
        setContentView(R.layout.home);
    }
}