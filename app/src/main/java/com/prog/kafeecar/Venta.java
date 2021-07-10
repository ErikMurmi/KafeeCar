/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.kafeecar;

import java.util.Date;

/**
 *
 * @author Erik
 */
public class Venta {
    
    private Date fecha;
    private Cliente comprador;
    private Vendedor vendedor;
    private Vehiculo vehiculo;
    private float precio;

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Venta(Date fecha, Cliente comprador, Vendedor vendedor, Vehiculo v, float precio) {
        this.fecha = fecha;
        this.comprador = comprador;
        this.vendedor = vendedor;
        vehiculo = v;
        this.precio = precio;
    }
    
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public float getPrecio() {
        return precio;
    }

    public Cliente getComprador() {
        return comprador;
    }

    public void setComprador(Cliente comprador) {
        this.comprador = comprador;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculos) {
        this.vehiculo = vehiculos;
    }
    
    @Override
    public String toString() {
        return "Venta" + 
                "\nFecha: " + fecha +
                "\nComprador: " + comprador +
                "\nVendedor: " + vendedor + 
                "\nVehiculo=" + vehiculo;
    }
    
    /**
     * Método que ayuda al vendedor o administrador a actualizar la información de una venta
     * @param fechaventa Parametro de venta para establecer como nuevo dato 
     * @param precioFinal Parametro de venta para establecer como nuevo dato 
     * @param vehiculos Parametro de venta para establecer como nuevo dato 
     * @param cliente Parametro de venta para establecer como nuevo dato 
     * @param vendedor Parametro de venta para establecer como nuevo dato 
     * @return Retorna los datos de la venta del vehiculo 
     */
    public Vehiculo actualizar(Date fechaventa, Float precioFinal, Vehiculo vehiculos, Cliente cliente, Vendedor vendedor){
        return null;
     
    }
    
    
    
    
}
