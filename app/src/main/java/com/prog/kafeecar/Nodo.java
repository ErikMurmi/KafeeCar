package com.prog.kafeecar;

public class Nodo {
    
    private Nodo anterior;
    private Nodo siguiente;
    
    private Object dato;

    public Nodo (Nodo anterior,Nodo siguiente,Object dato){
        this.anterior = anterior;
        this.siguiente = siguiente;
        this.dato = dato;
    }
    public Nodo getAnterior() {
        return anterior;
    }

    public void setAnterior(Nodo anterior) {
        this.anterior = anterior;
    }
    
    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }

    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

}
