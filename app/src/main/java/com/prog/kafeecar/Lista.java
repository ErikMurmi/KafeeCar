package com.prog.kafeecar;

public class Lista {
    private Nodo inicio;
    private Nodo fin;
    
    public Lista(){
        inicio=fin=null;
    }

    public Nodo getInicio() {
        return inicio;
    }

    public void setInicio(Nodo inicio) {
        this.inicio = inicio;
    }

    public Nodo getFin() {
        return fin;
    }

    public void setFin(Nodo fin) {
        this.fin = fin;
    }

    public boolean esVacia(){
        return inicio == null && fin == null;
    }
    
    
    public int contar(){
        if(esVacia())
            return 0;
        Nodo aux = inicio;
        int contador = 0;
        while(aux != null){
            contador++;
            aux = aux.getSiguiente();
        } 
        return contador;   
    }
    
    
    /**
     * Elimina todos los objetos de la lista
     */
    public void vaciar(){
        inicio=fin=null;
    }
    
    
    /**
     * Aniade un elemento al final de la lista
     * @param dato 
     */
    public void add(Object dato){
        Nodo nuevo = new Nodo(null,null,dato);
        if(esVacia()){
            inicio = fin = nuevo;
        }else{
            fin.setSiguiente(nuevo);
            nuevo.setAnterior(fin);
            fin = nuevo;
        }   
    }
    
    
    /**
     * Aniade un elemento al final de la lista
     * @param dato 
     */
    public void addInicio(Object dato){
        Nodo nuevo = new Nodo(null,null,dato);
        if(esVacia()){
            inicio = fin = nuevo;
        }else{
            inicio.setAnterior(nuevo);
            nuevo.setSiguiente(inicio);
            inicio = nuevo;
        }   
    }


    /**
     * Aniade un elemento a cierta posicion
     * @param pos
     * @param dato
     * @throws Exception
     */
    public void addPos(int pos, Object dato) throws Exception{
        if(esVacia()){
            throw new Exception("Esta vacia");
        }else if(pos>contar()){
            throw new Exception("El indice supera el tamanio de la lista");
        }
        int cont=0;
        Nodo aux = inicio;
        while(cont < pos){
            cont++;
            aux = aux.getSiguiente();
        }
        Nodo nuevo = new Nodo(aux.getAnterior(),aux,dato);
        aux.getAnterior().setSiguiente(nuevo);
        aux.setAnterior(nuevo);
    }

    /**
     * Obtiene el objeto de una posicion indicada
     * @param pos
     * @return
     * @throws Exception
     */
    public Object getPos(int pos){
        if(esVacia()){
            System.out.println("Lista vacia");
            return null;
        }else if(pos>contar()){
            System.out.println("Indice supera el tamanio de la lista");
            return null;
        }
        int cont=0;
        Nodo aux = inicio;
        while(cont < pos){
            cont++;
            aux = aux.getSiguiente();
        }
        return aux.getDato();
    }
    
    /**
     * Comprueba si la lista contiene un objeto
     * @param buscado
     * @return 
     */
    public boolean contiene(Object buscado){
        Nodo aux = inicio;
        boolean encontrado = false;
        if(esVacia()){
            return false;
        }
        while((aux != null && aux.getDato()!=null ) || !encontrado){

            if(buscado instanceof String ){
                String text=(String) buscado;
                String text2=(String)aux.getDato();
                if(text.compareTo(text2)==0){
                    encontrado=true;
                    break;
                }
            }
            else if(aux.getDato().equals(buscado)){
                encontrado = true;
            }
            aux = aux.getSiguiente();
        } 
        
        return encontrado;
    }
    
    
    /**
     * Cambia el dato de una posicion determinada
     * @param indice
     * @param dato
     * @throws Exception 
     */
    public void cambiarDatoIndice(int indice,Object dato) throws Exception{
        if(esVacia()){
            throw new Exception("No hay elementos");
        }else if(indice>contar()){
            throw new Exception("El indice supera el tamanio de la lista");
        }
        int cont=0;
        Nodo aux = inicio;
        while(cont < indice){
            cont++;
            aux = aux.getSiguiente();
        }
        aux.setDato(dato);
    }
    

    private Object eliminarNodo(Nodo nodoeliminar){
        if(esVacia()){
            return null;
        }
        if(nodoeliminar==inicio && nodoeliminar==fin){
            inicio=fin=null;
        }else if(nodoeliminar==inicio){
            inicio=nodoeliminar.getSiguiente();
            nodoeliminar.setSiguiente(null);
        }else if(nodoeliminar==fin){
            fin=nodoeliminar.getAnterior();
            nodoeliminar.setAnterior(null);
        }else {
            nodoeliminar.getAnterior().setSiguiente(nodoeliminar.getSiguiente());
            nodoeliminar.getSiguiente().setAnterior(nodoeliminar.getAnterior());
            nodoeliminar.setAnterior(null);
            nodoeliminar.setSiguiente(null);
        }
        return nodoeliminar.getDato();
    }
    
    /**
     * Elimina un objeto
     * @param buscado objeto a eliminar
     * @return objeto eliminado
     */

    public Object eliminar(Object buscado){
        Nodo aux = inicio;
        while(aux != null && aux.getDato()!=null){

            if(buscado instanceof String ) {
                String text = (String) buscado;
                String text2 = (String) aux.getDato();
                if (text.compareTo(text2) == 0) {
                    return eliminarNodo(aux);
                }
            }
            else if(aux.getSiguiente().getDato().equals(buscado)){
                return eliminarNodo(aux);
            }
            aux = aux.getSiguiente();
        } 
        
        return aux.getDato();
    }


    public void eliminarPos(int pos) throws Exception{
        if(esVacia()){
            throw new Exception("Esta vacia");
        }else if(pos>contar()){
            throw new Exception("El indice supera el tamanio de la lista");
        }
        int cont=0;
        Nodo aux = inicio;
        while(cont < pos){
            cont++;
            aux = aux.getSiguiente();
        }
        if (inicio==fin && cont==0){
            inicio=fin=null;
        }
        else if (cont==0){
            aux= inicio;
            inicio=inicio.getSiguiente();
            aux.setSiguiente(null);
            inicio.setAnterior(null);
        }
        else if(cont == contar()-1){
            aux=fin;
            fin=fin.getAnterior();
            aux.setAnterior(null);
            fin.setSiguiente(null);
        }else{
            aux.getAnterior().setSiguiente(aux.getSiguiente());
            aux.getSiguiente().setAnterior(aux.getAnterior());
            aux.setAnterior(null);
            aux.setSiguiente(null);
        }
    }

    public void copiar(Lista c){
        for(int i=0; i<c.contar();i++){
            try {
                add(c.getPos(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Lista copiar(){
        Lista copia = new Lista();
        for(int i=0; i<contar();i++){
            try {
                copia.add(getPos(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return copia;
    }
    
}
