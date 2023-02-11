package Model;


public class Cola {
    ListaEnlazada lista;
    public Cola(){
        lista=new ListaEnlazada();
    }
    
    public boolean esVacia(){
        return lista.esVacia();
    }
    
    public void encolar(int e,int s,int pr){
        lista.insertaNodos(e, s, pr);
    }
    
    public NodoLista desencolar(){
        return lista.deletePrimerNodo();
    }
}
