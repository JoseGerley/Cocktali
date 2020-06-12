package Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.cocktali.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Modelo.Coctel;

public class AdapterComprarCocteles extends BaseAdapter {


    private ArrayList<Coctel> coctelesdisponibles;
    private HashMap<String,Coctel> coctelespedido;


    public HashMap<String, Coctel> getCoctelespedido() {
        return coctelespedido;
    }

    public void setCoctelespedido(HashMap<String, Coctel> coctelespedido) {
        this.coctelespedido = coctelespedido;
    }

    public AdapterComprarCocteles() {
        coctelesdisponibles = new ArrayList<>();
        coctelespedido = new HashMap<>();

    }


    /**
     * Permite limpiar los cocteles y actualizar la vista
     * Flush del array
     */
    public void limpiarCocteles() {
        coctelesdisponibles = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return coctelesdisponibles.size();
    }

    @Override
    public Object getItem(int position) {
        return coctelesdisponibles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.comprarcoctel_items, null, false);

        TextView nombreCoctel = row.findViewById(R.id.nombreCoctelComprar);
        TextView descripcionCoctel = row.findViewById(R.id.descripcionCoctelComprar);
        TextView precioCoctel = row.findViewById(R.id.precioCoctelComprar);
        ImageView fotoCoctel = row.findViewById(R.id.imagenCoctelComprar);
        Switch comprarboton= row.findViewById(R.id.switchComprar);
        escucharSwitchCoctel(comprarboton,coctelesdisponibles.get(position));


        nombreCoctel.setText(coctelesdisponibles.get(position).getNombre());
        descripcionCoctel.setText(coctelesdisponibles.get(position).getDescripcion());
        precioCoctel.setText("$"+coctelesdisponibles.get(position).getPrecio());
        Picasso.get().load(coctelesdisponibles.get(position).getFoto()).into(fotoCoctel);

        return row;
    }


    /**
     * Agrega el listener al Switch del coctel, de manera que permita agregar el coctel a la canasta de compra
     */

    public void escucharSwitchCoctel(Switch bot, final Coctel agregado){
        bot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    coctelespedido.put(agregado.getId(),agregado);
                }

                else if (!isChecked) {
                    coctelespedido.remove(agregado.getId());
                }
            }
        });
    }


    /**
     * Agregar un nuevo coctel al menu disponible del coctelero
     * @param nuevo, Cotel que se agrega al menu
     * Se actualiza la vista
     */

    public void agregarCoctelMenuComprar(Coctel nuevo){
        coctelesdisponibles.add(nuevo);
        notifyDataSetChanged();
    }

    public ArrayList<Coctel> getCoctelesdisponibles() {
        return coctelesdisponibles;
    }

    /**
     * Permite asignar el arreglo de cocteles(menu) de un coctelero
     * Permite agregar los coteles del coctelero, al menu de la vista
     * @param coctelesdisponibles, menu del coctelero que vende
     */
    public void setCoctelesdisponibles(ArrayList<Coctel> coctelesdisponibles) {
        this.coctelesdisponibles = coctelesdisponibles;
    }
}
