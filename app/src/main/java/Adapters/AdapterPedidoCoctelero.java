package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cocktali.AgregarCoctel;
import com.example.cocktali.ComprarCoctel;
import com.example.cocktali.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Modelo.Coctel;
import Modelo.Pedido;

public class AdapterPedidoCoctelero extends BaseAdapter {

    private DatabaseReference referencia;

    private ArrayList<Pedido> solicitudes;

    private TextView estado;


    public AdapterPedidoCoctelero() {
        solicitudes = new ArrayList<>();
        referencia = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Hacer flush de la lista y limpiar la vista
     */
    public void limpiar() {
        solicitudes = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return solicitudes.size();
    }

    @Override
    public Object getItem(int position) {
        return solicitudes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.pedidos_coctelero, null, false);

        TextView nombreCliente = row.findViewById(R.id.ClienteNombre);
         estado = row.findViewById(R.id.estadoSolicitud);
        TextView ListaCocteles = row.findViewById(R.id.listaCoctelesCliente);
        Button aceptar = row.findViewById(R.id.aceptarPedido);
        escucharBotonAceptar(aceptar, solicitudes.get(position));  //Aceptar soli
        Button rechazar = row.findViewById(R.id.DenegarPedido);
        escucharBotonRechazar(rechazar, solicitudes.get(position)); //Rechazar soli
        TextView direccion = row.findViewById(R.id.direccionCliente);
        ImageView foto = row.findViewById(R.id.fotoCliente);

        nombreCliente.setText(solicitudes.get(position).getNombreUsuario());
        direccion.setText(solicitudes.get(position).getDireccionCliente());

        HashMap<String, Coctel> coctels = solicitudes.get(position).getCocteles();

        String pedido = "";

        if (coctels != null) {
            for (Coctel i : coctels.values()) {
                pedido = pedido + "\n" + i.getNombre();
            }

            ListaCocteles.setText(pedido);
        }

        //Actualizar el estado de la solicitud visualmente
        estado.setText(solicitudes.get(position).getEstado());
        if (solicitudes.get(position).getEstado().equals(ComprarCoctel.ACEPTADO)) {
            estado.setTextColor(Color.parseColor("#46BD06"));
        }
        if (solicitudes.get(position).getEstado().equals(ComprarCoctel.RECHAZADO)) {
            estado.setTextColor(Color.parseColor("#BD1F06"));
        }

        Picasso.get().load(solicitudes.get(position).getFotoUsuario()).into(foto);

        return row;
    }


    /**
     * Agregar un pedido a la lista de solicitudes del coctelero
     *
     * @param nuevo, Solicitud que se agrega a la lista
     *               Se actualiza la vista
     */

    public void agregarSolicitudCoctelero(Pedido nuevo) {
        solicitudes.add(nuevo);
        notifyDataSetChanged();
    }

    /**
     * Elimina un pedido a la lista de solicitudes del coctelero
     *
     * @param nuevo, Solicitud que se elimina a la lista
     *               Se actualiza la vista
     */

    public void eliminarSolicitudCoctelero(Pedido nuevo) {
        solicitudes.remove(nuevo);
        notifyDataSetChanged();
    }


    public Pedido buscarporId(String id) {
        Pedido retorno = null ;
        for (int i = 0; i < solicitudes.size(); i++) {
            if (solicitudes.get(i).getId().equals(id)) {
                retorno = solicitudes.get(i);
            }
        }
        return  retorno;
    }


    /**
     * Actualiza EL ESTADO un pedido a la lista de solicitudes del coctelero
     * @param nuevo, Solicitud que se actualiza a la lista
     * Se actualiza la vista
     */

    public void actualizarSolicitudCoctelero(Pedido nuevo){
        buscarporId(nuevo.getId()).setEstado(nuevo.getEstado());
        notifyDataSetChanged();
    }




    /**
     * Agregar el listener al boton de Aceptar pedido
     * Aceptar un pedido del coctelero
     * Actualizar el estado del pedido en la base de datos
     */
    public void escucharBotonAceptar(Button boton, final Pedido nuevo){
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                referencia.child("pedidos").child(nuevo.getId()).child("estado").setValue(ComprarCoctel.ACEPTADO);
            }
        });
    }

    /**
     * Agregar el listener al boton de Rechazar pedido
     * Rechazar un pedido del coctelero
     * Actualizar el estado del pedido en la base de datos
     */
    public void escucharBotonRechazar(Button boton, final Pedido soli){
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                referencia.child("pedidos").child(soli.getId()).child("estado").setValue(ComprarCoctel.RECHAZADO);
            }
        });
    }

}
