package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cocktali.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Modelo.Coctelero;
import Modelo.Pedido;

public class AdapterCoctelero  extends BaseAdapter {

    private ArrayList<Coctelero> cocteleros;


    /**
     * Limpia la lista de cocteleros a mostrar, equivale a actualizar
     */
    public void limpiar(){
        cocteleros = new ArrayList<>();
    }


    /**
     * Inicializar el adapter para el feed
     */
    public AdapterCoctelero(){ cocteleros = new ArrayList<>();}


    @Override
    public int getCount() {
        return cocteleros.size();
    }

    @Override
    public Object getItem(int position) {
        return cocteleros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.elementosfeed, null, false);

        TextView nombre = row.findViewById(R.id.feedCoctelero);
        TextView descripcion = row.findViewById(R.id.feedDescripcionCoctelero);
        TextView celular = row.findViewById(R.id.feedNumeroCoctelero);
        ImageView foto = row.findViewById(R.id.feedImagenCoctelero);


        nombre.setText(cocteleros.get(position).getNombre());
        descripcion.setText(cocteleros.get(position).getDescription());
        celular.setText(cocteleros.get(position).getNumero());

        if (cocteleros.get(position).getFoto() != null && !(cocteleros.get(position).getFoto().equals("")) ) { //Verificar si la foto ya se ha cargado
            Picasso.get().load(cocteleros.get(position).getFoto()).into(foto);
        }

        return row;
    }


    /**
     * Agregar un nuevo coctelero al feed del usuario
     * @param nuevo, Cotelero que se agrega al feed
     * Se actualiza la vista
     */

    public void agregarCoctelero(Coctelero nuevo){
        cocteleros.add(nuevo);
        notifyDataSetChanged();
    }


    public Coctelero buscarporId(String id) {
        Coctelero retorno = null ;
        for (int i = 0; i < cocteleros.size(); i++) {
            if (cocteleros.get(i).getId().equals(id)) {
                retorno = cocteleros.get(i);
            }
        }
        return  retorno;
    }

    /** Proceso ASINCRONO, de recuperar la foto de la base de datos
     * Actualizar la foto del coctelero, al feed del usuario
     * @param nuevo, Coctelero que tiene foto que se actualiza
     * Se actualiza la vista
     */

    public void actualizarFoto(Coctelero nuevo){
        buscarporId(nuevo.getId()).setFoto(nuevo.getFoto());
        notifyDataSetChanged();
    }

}
