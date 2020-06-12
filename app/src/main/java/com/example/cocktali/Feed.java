package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Adapters.AdapterCoctelero;
import Modelo.Coctelero;
import Modelo.Usuario;

public class Feed extends AppCompatActivity {

    private AdapterCoctelero adapter;
    private Usuario usuario;
    private ListView feed;
    private Button botonperfil;
    private String idUsuario;
    private DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        referencia = FirebaseDatabase.getInstance().getReference();
        feed = findViewById(R.id.feedElementos);
        botonperfil = findViewById(R.id.botonperfil);

        adapter = new AdapterCoctelero();
        feed.setAdapter(adapter);
        idUsuario= getIntent().getStringExtra("idUsuario");
        obtenerUsuario();

        rellenarFeed();
        escucharbotonperfil();

    }


    /**
     * Rellenar el feed con los cocteleros registrados en la app
     * Actualiza la lista en la vista feed, con los cocteleros registrados
     * Conectarse con la rama cocteleros
     */
    public  void rellenarFeed(){

        //Leer la lista de los cocteleros en la base de datos
        referencia.child("cocteleros").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
              Coctelero coctelero = dataSnapshot.getValue(Coctelero.class);
              adapter.agregarCoctelero(coctelero);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Coctelero coctelero = dataSnapshot.getValue(Coctelero.class);
                adapter.actualizarFoto(coctelero);

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
     * Agregar el listener al boton perfil y escuchar la accion
     */
    public void escucharbotonperfil(){
        botonperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent salto = new Intent(Feed.this, PerfilUsuario.class);
                salto.putExtra("idUsuario",idUsuario);
                startActivity(salto);
            }
        });

    }


    /**
     * Agrega el listener a cada cotelero que hace parte de la lista feed
     * Permite abrir la actividad de Comprar Coctel segun el coctelero de preferencia
     */
    public void escucharitemsfeed(){
        feed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Coctelero seleccion = (Coctelero) adapter.getItem(position);
                String idCoctelero = seleccion.getId();
                Intent salto = new Intent(Feed.this,ComprarCoctel.class);
                salto.putExtra("idCoctelero",idCoctelero);
                salto.putExtra("fotoUsuario",usuario.getFoto());
                salto.putExtra("idUsuario",idUsuario);
                salto.putExtra("nombreUsuario",usuario.getNombre());
                startActivity(salto);
            }
        });
    }

    /**
     * Obtener el usuario
     */
    public void obtenerUsuario(){

        referencia.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    usuario = dataSnapshot.getValue(Usuario.class);
                    escucharitemsfeed();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
