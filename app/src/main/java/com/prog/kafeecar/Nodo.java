/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prog.kafeecar;

/**
 *
 * @author Erik
 */
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
