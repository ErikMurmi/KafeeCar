package com.prog.kafeecar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;

public class Ventas_admin_Fragment extends Fragment implements Adaptador_Lista_Ventas.RecyclerItemClick, SearchView.OnQueryTextListener {
    private View mainView;
    private SearchView busqueda_ventas;
    private PatioVenta patio;

    private Button irVentasGenerales;
    private Button irRegistrarNuevaVenta;
    private Button realizarVenta;
    private LinearLayout verVentasGenerales;
    private LinearLayout verRegistroNuevaVenta;
    private LinearLayout verVentasAdmin;
    private LinearLayout verEstadisticasGenerales;
    private Adaptador_Lista_Ventas adptadorlistaview;
    private TextView contar;
    private TextView contador;
    private String [] meses=new String[]{"ENERO","FEBRERO","MAYO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"};
   private int []sales ;
   private Button irEstadisticasGenerales;


    private Lista ventas= new Lista();
    private int[] colors =new int[]{Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51),Color.rgb(255,87,51)};
    @Nullable
    LineChart lineChart;
    BarChart barChart;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.ventas_admin, container, false);
        patio = Patioventainterfaz.patioventa;
        //contador=mainView.findViewById(R.id.contador);
       sales=Patioventainterfaz.contadores();


        //Botones
        irVentasGenerales=mainView.findViewById(R.id.administrar_venta_generales_btn);
        irRegistrarNuevaVenta=mainView.findViewById(R.id.administrar_venta_aniadirventa_btn);
        realizarVenta=mainView.findViewById(R.id.vt_admin_realizarventa_btn);
        irEstadisticasGenerales=mainView.findViewById(R.id.vt_admin_estadisticas_btn);
        // layouts
        verVentasGenerales=mainView.findViewById(R.id.ventas_admin_generales_lyt);
        verRegistroNuevaVenta= mainView.findViewById(R.id.adminregistro_lyt);
        verVentasAdmin=mainView.findViewById(R.id.ventas_admin_lyt);
        verEstadisticasGenerales=mainView.findViewById(R.id.vt_estadisticasgenerales_admin_lyt);

        irVentasGenerales.setOnClickListener(view -> {
            irVentasGenerales.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.VISIBLE);

        });

        irRegistrarNuevaVenta.setOnClickListener(view -> {
            irRegistrarNuevaVenta.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.GONE);
            verVentasAdmin.setVisibility(View.GONE);
            verRegistroNuevaVenta.setVisibility(View.VISIBLE);

        });
        irEstadisticasGenerales.setOnClickListener(view -> {

            irEstadisticasGenerales.setVisibility(View.GONE);
            verVentasGenerales.setVisibility(View.GONE);
            verVentasAdmin.setVisibility(View.GONE);
            verEstadisticasGenerales.setVisibility(View.VISIBLE);

        });
        realizarVenta.setOnClickListener(view -> {

            try {
                aniadirVenta();
                Toast.makeText(mainView.getContext(),"Se realizo la venta",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        contar= mainView.findViewById(R.id.contar_txt);
        contar.setText(String.valueOf(patio.getVehiculos().contar()));

        cargar();
        barChart= (BarChart)mainView.findViewById(R.id.gaficaVentasGenerales);
        createcharts();
        ventas = patio.getVentasGenerales();
        return mainView;
    }

    public void aniadirVenta() throws Exception {
        Venta nueva=null;
        Cliente clienteventa=null;
        Vendedor vendedorventa=null;
        Vehiculo autoventa =null;


        EditText fechaventadia = mainView.findViewById(R.id.fecha_venta_dia_etxt);
        EditText fechaventames = mainView.findViewById(R.id.fecha_venta_mes_etxt);
        EditText fechaventaanio = mainView.findViewById(R.id.fecha_venta_anio_etxt);

        String fechaventa_str ="";


        int d=0;
        int c = 0;

        if((!isEmpty(fechaventadia) && !isEmpty(fechaventames)) && (!isEmpty(fechaventaanio))){
            fechaventa_str = fechaventaanio.getText().toString() + "-" + fechaventames.getText().toString() + "-" + fechaventadia.getText().toString();
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campos de fecha vacios",Toast.LENGTH_SHORT).show();

        }
        EditText clientes = mainView.findViewById(R.id.cliente_venta_txt);
        EditText vendedor = mainView.findViewById(R.id.vendedor_venta_txt);
        EditText auto = mainView.findViewById(R.id.vehiculo_venta_txt);
        if(!isEmpty(clientes)){
            String clientes_str = clientes.getText().toString();
            clienteventa = patio.buscarClientes("Nombre", clientes_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de cliente vacio",Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty(vendedor)){
            String vendedores_str = vendedor.getText().toString();
            vendedorventa = patio.buscarVendedores("Nombre", vendedores_str);
        }else{
            c++;
            Toast.makeText(mainView.getContext(),"Campo de Vendedor vacio",Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty(auto)){
            String autos_str = auto.getText().toString();
            autoventa = patio.buscarVehiculos("Placa", autos_str);
            d=1;
        }else{

            c++;
            Toast.makeText(mainView.getContext(),"Campo de Vehiculo vacio",Toast.LENGTH_SHORT).show();
        }



       if(c==0) {
           patio.aniadirVenta(nueva);

           if (patio.getVentasGenerales().contiene(nueva)) {
               Toast.makeText(mainView.getContext(), "Se registro la venta.", Toast.LENGTH_SHORT).show();
           } else {
               Toast.makeText(mainView.getContext(), "La venta no see registro, esto pudo pasar por que hay campos vacios o por que los datos ingresados no existen en el sistema por favor verifique los datos.", Toast.LENGTH_SHORT).show();
           }
       }


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
}
