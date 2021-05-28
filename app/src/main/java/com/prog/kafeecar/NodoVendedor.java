package com.prog.kafeecar;

public class NodoVendedor {
    private NodoVendedor anterior;
    private NodoVendedor siguiente;

    private Vendedor dato;

    public NodoVendedor (NodoVendedor anterior,NodoVendedor siguiente,Vendedor dato){
        this.anterior = anterior;
        this.siguiente = siguiente;
        this.dato = dato;
    }
    public NodoVendedor getAnterior() {
        return anterior;
    }

    public void setAnterior(NodoVendedor anterior) {
        this.anterior = anterior;
    }

    public NodoVendedor getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoVendedor siguiente) {
        this.siguiente = siguiente;
    }

    public Vendedor getDato() {
        return dato;
    }

    public void setDato(Vendedor dato) {
        this.dato = dato;
    }
}
