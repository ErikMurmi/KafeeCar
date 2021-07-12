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
    private Cliente cliente;
    private Vendedor vendedor;
    private Vehiculo vehiculo;
    private float precio;

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Venta(Date fecha, Cliente cliente, Vendedor vendedor, Vehiculo v, float precio) {
        this.fecha = fecha;
        this.cliente = cliente;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
                "\nComprador: " + cliente +
                "\nVendedor: " + vendedor + 
                "\nVehiculo=" + vehiculo;
    }
    
    /**
     * Método que ayuda al vendedor o administrador a actualizar la información de una venta
     * @param fecha Parametro de venta para establecer como nuevo dato
     * @param precio Parametro de venta para establecer como nuevo dato
     * @param vehiculo Parametro de venta para establecer como nuevo dato
     * @param cliente Parametro de venta para establecer como nuevo dato
     * @param vendedor Parametro de venta para establecer como nuevo dato
     */
    public void actualizar(Date fecha, Float precio, Vehiculo vehiculo, Cliente cliente, Vendedor vendedor){
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.vehiculo = vehiculo;
        this.precio = precio;
    }
}
