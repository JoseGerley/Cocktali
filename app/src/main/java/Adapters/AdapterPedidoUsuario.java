package Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.cocktali.ComprarCoctel;
import com.example.cocktali.PerfilCoctelero;
import com.example.cocktali.PerfilUsuario;
import com.example.cocktali.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Modelo.Coctel;
import Modelo.Coctelero;
import Modelo.Pedido;

import static androidx.core.content.ContextCompat.startActivity;

public class AdapterPedidoUsuario extends BaseAdapter {


    private ArrayList<Pedido> pedidosUsuario;

    private PerfilUsuario conexion;
    private  Context contex;


    /**
     * Inicialzar el adapter para la vista
     */
    public AdapterPedidoUsuario(Context context) {
        contex = context;
        pedidosUsuario = new ArrayList<>();
    }

    /**
     * Limpiar los pedidos del usuario y refrescar la lista
     */
    public void limpiar(){
        pedidosUsuario = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return pedidosUsuario.size();
    }

    @Override
    public Object getItem(int position) {
        return pedidosUsuario.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.pedidos_usuario, null, false);

        TextView nombreCoctelero = row.findViewById(R.id.cocteleroPedido);
        TextView estado = row.findViewById(R.id.estadopedido);
        TextView ListaCocteles = row.findViewById(R.id.coctelespedido);
        Button celular = row.findViewById(R.id.botonllamar);
        escucharBotonLlamar(celular,pedidosUsuario.get(position).getCelularCoctelero());
        ImageView fotoCoctelero = row.findViewById(R.id.fotoCocteleroPedido);


        HashMap<String, Coctel> coctels = pedidosUsuario.get(position).getCocteles();

        String pedido ="";


        if (coctels!=null){
            for (Coctel i : coctels.values()) {
                pedido = pedido + "\n" + i.getNombre();
            }
        }

        nombreCoctelero.setText(pedidosUsuario.get(position).getNombreCoctelero());
        ListaCocteles.setText(pedido);

        if (pedidosUsuario.get(position).getEstado().equals(ComprarCoctel.ACEPTADO)) {
            estado.setTextColor(Color.parseColor("#46BD06"));
        }
        if (pedidosUsuario.get(position).getEstado().equals(ComprarCoctel.RECHAZADO)) {
            estado.setTextColor(Color.parseColor("#BD1F06"));
        }
        estado.setText(pedidosUsuario.get(position).getEstado());


        Picasso.get().load(pedidosUsuario.get(position).getFotoCoctelero()).into(fotoCoctelero);

        return row;
    }

    /**
     * Agregar un pedido a la lista de pedidos del usuario
     * @param nuevo, Pedido que se agrega a la lista
     * Se actualiza la vista
     */

    public void agregarPedidoUsuario(Pedido nuevo){
        pedidosUsuario.add(nuevo);
        notifyDataSetChanged();
    }



    /**
     * Eliminar un pedido de la lista de pedidos del usuario
     * @param nuevo, Pedido que se agrega a la lista
     * Se actualiza la vista
     */

    public void borrarPedidoUsuario(Pedido nuevo){
        pedidosUsuario.remove(nuevo);
        notifyDataSetChanged();
    }


    public Pedido buscarporId(String id) {
        Pedido retorno = null ;
        for (int i = 0; i < pedidosUsuario.size(); i++) {
            if (pedidosUsuario.get(i).getId().equals(id)) {
                retorno = pedidosUsuario.get(i);
            }
        }
        return  retorno;
    }


    /**
     * Actualiza EL ESTADO de un pedido a la lista de pedidos del usuario
     * @param nuevo, pedido que se actualiza a la lista
     * Se actualiza la vista
     */

    public void actualizarPedidoUsuario(Pedido nuevo){
        buscarporId(nuevo.getId()).setEstado(nuevo.getEstado());
        notifyDataSetChanged();
    }


    /**
     * Agregar el lister al boton de llamar de cada pedido
     * @param boton, el boton que acciona la llamada al coctelero de cada pedido
     */
    public void escucharBotonLlamar(Button boton, final String numero){
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(
                        contex, Manifest.permission.CALL_PHONE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(contex,"Debe dar permisos a la APP para llamar",Toast.LENGTH_LONG).show();
                }

                else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + numero));
                    contex.startActivity(intent);
                }


            }
        });

    }

}
