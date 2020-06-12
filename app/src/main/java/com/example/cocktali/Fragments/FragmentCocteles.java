package com.example.cocktali.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cocktali.AgregarCoctel;
import com.example.cocktali.Feed;
import com.example.cocktali.Login;
import com.example.cocktali.PerfilCoctelero;
import com.example.cocktali.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Adapters.AdapterCocteles;
import Modelo.Coctel;


public class FragmentCocteles extends Fragment  {

    private  ListView listaCocteles;
    private AdapterCocteles adapter;
    private FloatingActionButton addcoctel;
    private String id;
    private PerfilCoctelero conexion;
    private DatabaseReference referencia;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_cocteles, container, false);

         conexion = (PerfilCoctelero) getActivity();
         id = conexion.getId();

        adapter =  new AdapterCocteles();
        referencia = FirebaseDatabase.getInstance().getReference();


        listaCocteles = view.findViewById(R.id.listaFragmentCocteles);
        listaCocteles.setAdapter(adapter);
        addcoctel= view.findViewById(R.id.floatcoctel);

     agregarCoctel();
     escucharbotonflotante();

      return view;
    }


    /**
     * Rellenar la lista de los cocteles disponibles
     * Consultar el hashmap de Cocteles del Coctelero en la base de datos
     * Actualizar la vista, con los items del coctel
     */
    public void agregarCoctel(){

        referencia.child("cocteleros").child(id).child("cocteles").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Coctel nuevo = dataSnapshot.getValue(Coctel.class);
                adapter.agregarCoctelalMenu(nuevo);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    /**
     * Permite escuchar el boton flotante para agregar un nuevo octel al menu disponible
     */
    public void escucharbotonflotante(){
        addcoctel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent salto = new Intent(getActivity(), AgregarCoctel.class);
                salto.putExtra("idCoctelero",id);
                startActivity(salto);
            }
        });
    }

}
