package com.prog.kafeecar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Ventas_admin_Fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private View mainView;
    private SearchView busqueda_ventas;
    private PatioVenta patio;

    private Button administrar_venta_generales_btn;
    private Button administrar_venta_aniadirventa_btn;
    private LinearLayout ventas_admin_generales_lyt;
    private LinearLayout add_vt_ad_lyt;
    private LinearLayout ventas_admin_lyt;

    private int posicion_dia=-1;
    private int posicion_mes=-1;
    private int posicion_anio=-1;

    private String fecha_nueva_cita;

    private boolean mes_mostrados = false;
    private boolean anios_mostradas = false;
    public boolean dias_mostrados = false;

    private LinearLayout verEstadisticasGenerales;
    private Adaptador_Lista_Ventas adptadorlistaview;
    private Venta venta_mostrar;
    private String [] meses=new String[]{"ENERO","FEBRERO","MAYO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
    private int []sales ;
    private Button vt_admin_estadisticas_btn;

    private Vendedor usuario_admin = (Vendedor) Patioventainterfaz.usuarioActual;

    private int[] colors =new int[]{Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51)};
    @Nullable
    LineChart lineChart;
    BarChart barChart;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ventas_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        sales=Patioventainterfaz.contadores();


        //Botones
        //Pag principal
        administrar_venta_generales_btn = mainView.findViewById(R.id.administrar_venta_generales_btn);
        administrar_venta_aniadirventa_btn = mainView.findViewById(R.id.administrar_venta_aniadirventa_btn);
        vt_admin_estadisticas_btn = mainView.findViewById(R.id.vt_admin_estadisticas_btn);
        //ADD
        Button descartar_vt_ad_btn = mainView.findViewById(R.id.descartar_vt_ad_btn);
        ImageButton add_cliente_vt_ad_btn = mainView.findViewById(R.id.add_cliente_vt_ad_btn);
        Button guardar_vt_ad_btn = mainView.findViewById(R.id.guardar_vt_ad_btn);
        //LYT
        ventas_admin_generales_lyt = mainView.findViewById(R.id.ventas_admin_generales_lyt);
        add_vt_ad_lyt= mainView.findViewById(R.id.add_vt_ad_lyt);
        ventas_admin_lyt=mainView.findViewById(R.id.ventas_admin_lyt);

        administrar_venta_generales_btn.setOnClickListener(view -> irListaGenerales());

        administrar_venta_aniadirventa_btn.setOnClickListener(view -> {
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            add_vt_ad_lyt.setVisibility(View.VISIBLE);
            adaptadorAniadir();
        });

        vt_admin_estadisticas_btn.setOnClickListener(view -> {
            vt_admin_estadisticas_btn.setVisibility(View.GONE);
            ventas_admin_generales_lyt.setVisibility(View.GONE);
            ventas_admin_lyt.setVisibility(View.GONE);
            verEstadisticasGenerales.setVisibility(View.VISIBLE);
        });


        TextInputLayout anio_lyt = mainView.findViewById(R.id.anio_vt_ad_til);
        TextInputLayout mes_lyt = mainView.findViewById(R.id.mes_vt_ad_til);
        TextInputLayout dias_lyt = mainView.findViewById(R.id.dia_vt_ad_til);
        AutoCompleteTextView anio = mainView.findViewById(R.id.anio_vt_ad_acv);
        AutoCompleteTextView mes = mainView.findViewById(R.id.mes_vt_ad_acv);
        AutoCompleteTextView dias = mainView.findViewById(R.id.dia_vt_ad_acv);

        anio_lyt.setEndIconOnClickListener(v -> anio.performClick());
        anio.setOnClickListener(v -> {
            if(anios_mostradas){
                anio.dismissDropDown();
                anios_mostradas =false;
            }else{
                ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.anios);
                anio.setAdapter(adapt);
                anio.showDropDown();
                anios_mostradas = true;
            }
        });

        anio.setOnItemClickListener((parent, view, position, id) -> setPosicion_anio(position));

        mes_lyt.setEndIconOnClickListener(v -> mes.performClick());
        mes.setOnClickListener(v -> {
            if(mes_mostrados){
                mes.dismissDropDown();
                mes_mostrados =false;
            }else{
                ArrayAdapter<String> adapt_mes = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,Patioventainterfaz.meses);
                mes.setAdapter(adapt_mes);
                mes.showDropDown();
                mes_mostrados = true;
            }
        });

        mes.setOnItemClickListener((parent, view, position, id) -> setPosicion_mes(position));

        dias_lyt.setEndIconOnClickListener(v -> dias.performClick());

        dias.setOnClickListener(v -> {
            if(posicion_mes!=-1 && posicion_anio!=-1){
                if(dias_mostrados){
                    dias.dismissDropDown();
                    dias_mostrados =false;
                }else{
                    ArrayAdapter<String> adapt = new ArrayAdapter<>(mainView.getContext(), R.layout.dropdown_menu_items,diaListaDesplegable());
                    dias.setAdapter(adapt);
                    dias.showDropDown();
                    dias_mostrados = true;
                }
            }else{
                Toast.makeText(mainView.getContext(), "Campos de fecha vacios", Toast.LENGTH_SHORT).show();
            }
        });

        dias.setOnItemClickListener((parent, view, position, id) -> setPosicion_dia(position));

        guardar_vt_ad_btn.setOnClickListener(view -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Registrar Venta");
            msg.setMessage("¿Estás seguro de registrar esta nueva venta?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> {

                if(registarVenta()){
                    irListaGenerales();
                    Toast.makeText(mainView.getContext(),"Se registro la venta",Toast.LENGTH_SHORT).show();
                }
            });
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        descartar_vt_ad_btn.setOnClickListener(v -> {
            AlertDialog.Builder msg = new AlertDialog.Builder(mainView.getContext());
            msg.setTitle("Descartar Venta");
            msg.setMessage("¿Estás seguro de descartar la nueva venta?");
            msg.setPositiveButton("Aceptar", (dialog, which) -> irPaginaPrincipal());
            msg.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
            msg.show();
        });

        //Add
        /*



        // layouts



        verEstadisticasGenerales=mainView.findViewById(R.id.vt_estadisticasgenerales_admin_lyt);

        //0nClick




        barChart= (BarChart)mainView.findViewById(R.id.gaficaVentasGenerales);
        createcharts();
        ventas = patio.getVentasGenerales();*/
        return mainView;
    }

    public void irListaGenerales(){
        administrar_venta_generales_btn.setVisibility(View.GONE);
        add_vt_ad_lyt.setVisibility(View.GONE);
        ventas_admin_generales_lyt.setVisibility(View.VISIBLE);
        cargar();
    }

    public void irPaginaPrincipal(){
        ventas_admin_generales_lyt.setVisibility(View.GONE);
        add_vt_ad_lyt.setVisibility(View.GONE);
        ventas_admin_lyt.setVisibility(View.VISIBLE);
    }
    public void adaptadorAniadir(){
        AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getCedulasClientes());
        cliente.setAdapter(adapter);

        TextView cedula_vendedor_ci_vn_txt = mainView.findViewById(R.id.cedula_vendedor_vt_ad_txt);
        cedula_vendedor_ci_vn_txt.setText(usuario_admin.getCedula());

        AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_ad_actv);
        ArrayAdapter<String> adapterPla = new ArrayAdapter<>(mainView.getContext(), android.R.layout.simple_list_item_1, patio.getPlacasVehiculo());
        auto.setAdapter(adapterPla);
    }

    public boolean registarVenta(){
        Cliente cliente_c = null;
        Vehiculo vehiculo = null;
        int c = 0;
        try {
            AutoCompleteTextView cliente = mainView.findViewById(R.id.cedula_cliente_vt_ad_actv);
            AutoCompleteTextView auto = mainView.findViewById(R.id.placa_vt_ad_actv);
            try {
                if (!isEmpty(cliente)) {
                    String cliente_str = cliente.getText().toString();
                    if (cliente_str.length() != 10) {
                        Toast.makeText(mainView.getContext(), "Número de cédula inválido", Toast.LENGTH_SHORT).show();
                        cliente.setText("");
                        c++;
                    }

                        cliente_c = patio.buscarClientes("Cedula", cliente_str);

                } else {
                    Toast.makeText(mainView.getContext(), "Campo vacío: *Cédula Cliente*", Toast.LENGTH_SHORT).show();
                    c++;
                }
            } catch (Exception e) {
                Toast.makeText(mainView.getContext(), "cliente error", Toast.LENGTH_SHORT).show();
            }
            try {
                if (!isEmpty(auto)) {
                    String vehiculo_str = auto.getText().toString();

                        vehiculo = patio.buscarVehiculos("Placa", vehiculo_str);

                    if (vehiculo == null) {
                        Toast.makeText(mainView.getContext(), "No existe el vehículo", Toast.LENGTH_SHORT).show();
                        auto.setText("");
                        c++;
                    }
                } else {
                    Toast.makeText(mainView.getContext(), "Campo vacío: *Placa Vehiculo*", Toast.LENGTH_SHORT).show();
                    c++;
                }
        } catch (Exception e) {
            Toast.makeText(mainView.getContext(), "vehiculo error", Toast.LENGTH_SHORT).show();
        }

        EditText precio = mainView.findViewById(R.id.precio_vt_ad_etxt);
        float precio_flt = Float.parseFloat(precio.getText().toString());

            if (c == 0) {
                fecha_nueva_cita = (posicion_dia+1)+"-"+(posicion_mes+1)+"-"+Patioventainterfaz.anios[posicion_anio];
                Date fecha = null;
                fecha = Patioventainterfaz.sdf.parse(fecha_nueva_cita);
                Venta nueva = new Venta(fecha,cliente_c,usuario_admin,vehiculo,precio_flt);
                if (patio.getVentasGenerales().contiene(nueva)) {
                    Toast.makeText(mainView.getContext(), "Se agrego correctamente la venta", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        } catch (ParseException e) {
            Toast.makeText(mainView.getContext(), "error fecha", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void cargar(){
        RecyclerView listaview = mainView.findViewById(R.id.lista_ventas_admin_rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(mainView.getContext());
        listaview.setLayoutManager(manager);
        listaview.setItemAnimator(new DefaultItemAnimator());
        adptadorlistaview = new Adaptador_Lista_Ventas(patio.getVentasGenerales(), this);
        listaview.setAdapter(adptadorlistaview);
        //listaview.addItemDecoration(new DividerItemDecoration(listaview.getContext(), DividerItemDecoration.VERTICAL));
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String b = newText;
        if(newText.length()==2){
            b+="-";
            busqueda_ventas.setQuery(b,false);
        }
        if(newText.length()==5){
            b+="-";
            busqueda_ventas.setQuery(b,false);
        }
        if(newText.length()>10){
            b= b.substring(0,10);
            busqueda_ventas.setQuery(b,false);
        }
        adptadorlistaview.filtro(b);
        return false;
    }

    private Chart getSameChart(Chart chart,String leyenda,int colores,int fondo, int yxes){
        chart.getDescription().setText(leyenda);
        chart.getDescription().setTextSize(15);
        chart.setBackgroundColor(fondo);
        chart.animateY(yxes);
        legend(chart);

        return chart;
    }
    private void legend(Chart chart){
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        ArrayList<LegendEntry> entries=new ArrayList<>();
        for (int i =0; i<meses.length;i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor=colors[i];
            entry.label=meses[i];
            entries.add(entry);
        }
        legend.setCustom(entries);
    }
    private ArrayList<BarEntry> getBarEntries(){
        ArrayList<BarEntry> entries=new ArrayList<>();
        for (int i =0; i<sales.length;i++){
            entries.add(new BarEntry(i,sales[i]));
        }

        return entries;
    }
    private void axisX(XAxis axis){

        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));

    }
    private void axisXleft(YAxis axis){
        axis.setSpaceTop(30);
        axis.setAxisMinimum(0);
    }
    private void axisXRight(YAxis axis){
        axis.setEnabled(false);

    }
    public void createcharts(){
        barChart=(BarChart) getSameChart(barChart,"",Color.RED,Color.rgb(253,254,254),30000);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(true);
        barChart.setData(getbardata());
        barChart.invalidate();
        axisX(barChart.getXAxis());
        axisXleft(barChart.getAxisLeft());
        axisXRight(barChart.getAxisRight());
    }

    public ArrayList<String> diaListaDesplegable(){
        ArrayList<String> dias = new ArrayList<>();
        int anioa = Integer.parseInt(Patioventainterfaz.anios[posicion_anio]);
        int i;
        for (i = 1; i<=Patioventainterfaz.diasLista[posicion_mes];i++){
            dias.add(String.valueOf(i));
        }
        if(Patioventainterfaz.esBisiesto(anioa) && posicion_mes==1){
            dias.add(String.valueOf(i+1));
        }
        return dias;
    }

    private DataSet getData(DataSet dataSet){
        dataSet.setColors(colors);
        dataSet.setValueTextSize(Color.WHITE);
        dataSet.setValueTextSize(10);
        return dataSet;
    }
    private BarData getbardata(){
        BarDataSet barDataSet=(BarDataSet) getData(new BarDataSet(getBarEntries(),""));
        barDataSet.setBarShadowColor(Color.GRAY);
        BarData barData =new BarData(barDataSet);
        barData.setBarWidth(0.45f);
        return barData;
    }

    @Override
    public void itemClick(String placa, String cliente) {
        Toast.makeText(mainView.getContext(), "Se registro la venta.", Toast.LENGTH_SHORT).show();
    }

    public final void setPosicion_mes(int pos){
        posicion_mes= pos;
    }
    public final void setPosicion_anio(int pos){
        posicion_anio= pos;
    }
    private void setPosicion_dia(int pos) {posicion_dia = pos; }
}
