package com.example.cocktali.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cocktali.PerfilCoctelero;
import com.example.cocktali.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Adapters.AdapterCocteles;
import Adapters.AdapterPedidoCoctelero;
import Modelo.Pedido;


public class FragmentPedidos extends Fragment {

    private ListView listaSolicitudes;
    private AdapterPedidoCoctelero adapter;
    private DatabaseReference reference;
    private PerfilCoctelero conexion;

    private String id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_pedidos, container, false);

        adapter =  new AdapterPedidoCoctelero();
        reference = FirebaseDatabase.getInstance().getReference();
        conexion = (PerfilCoctelero) getActivity();
        id = conexion.getId();
        listaSolicitudes = view.findViewById(R.id.listaFragmentsPedidos);
        listaSolicitudes.setAdapter(adapter);

        consultarSolicitudes();

        return view;
    }






    /**
     * Consulta las solicitudes que tenga un coctelero
     * Busca en la rama pedidos y verifica los id correspondiente
     * Llenar el listview con las solicitudes encontradas, usando el adapter
     */
    public void consultarSolicitudes(){

        reference.child("pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                if (nuevo.getIdCoctelero().equals(id)){
                    adapter.agregarSolicitudCoctelero(nuevo);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                if (nuevo.getIdCoctelero().equals(id)){
                    adapter.actualizarSolicitudCoctelero(nuevo);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                if (nuevo.getIdCoctelero().equals(id)){
                    adapter.eliminarSolicitudCoctelero(nuevo);
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
