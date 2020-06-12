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

import Modelo.Coctel;

public class AdapterCocteles extends BaseAdapter {


    private ArrayList<Coctel> menuCocteles;

    /**
     * Limpiar la lista de los cocteles del Coctelero
     */
    public void limpiar(){ menuCocteles = new ArrayList<>();}


    /**
     * Constuir el adapter
     */
    public AdapterCocteles(){menuCocteles = new ArrayList<>();}




    @Override
    public int getCount() {
        return menuCocteles.size();
    }

    @Override
    public Object getItem(int position) {
        return menuCocteles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.cocteles_item, null, false);

        TextView nombreCoctel = row.findViewById(R.id.nombre);
        TextView descripcionCoctel = row.findViewById(R.id.descripcion);
        TextView precioCoctel = row.findViewById(R.id.precio);
        ImageView fotoCoctel = row.findViewById(R.id.foto);

        nombreCoctel.setText(menuCocteles.get(position).getNombre());
        descripcionCoctel.setText(menuCocteles.get(position).getDescripcion());
        precioCoctel.setText("$"+menuCocteles.get(position).getPrecio());
        Picasso.get().load(menuCocteles.get(position).getFoto()).into(fotoCoctel);

        return row;
    }


    /**
     * Agregar un nuevo item Coctel a la lista de cocteles del Coctelero
     * @param nuevo, coctel que se agrega
     * se actualiza la vista
     */
    public void agregarCoctelalMenu(Coctel nuevo){
        menuCocteles.add(nuevo);
        notifyDataSetChanged();
    }


}
