package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Adapters.AdapterPedidoUsuario;
import Modelo.Pedido;
import Modelo.Usuario;

public class PerfilUsuario extends AppCompatActivity {

    private AdapterPedidoUsuario adapter;
    private ListView pedidos;

    private TextView nombre, correo, celular;
    private ImageView fotoUsuario;

    private DatabaseReference referencia;
    private FirebaseAuth auth;

    private Button botonCerrarSesion;

    private String idUsuario;
    private  Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        referencia = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();



        idUsuario= getIntent().getStringExtra("idUsuario");

        iniciarVista();
        obtenerUsuario();

        adapter= new AdapterPedidoUsuario(this);
        pedidos = findViewById(R.id.PedidosUsuario);
        pedidos.setAdapter(adapter);




        obtenerPedidos();

        cerrarSesion();
    }


    /**
     * Iniciar los elementos de la vista
     */

    public  void  iniciarVista(){
        nombre = findViewById(R.id.perfilNombre);
        correo = findViewById(R.id.perfilCorreo);
        celular = findViewById(R.id.perfilNumero);
        fotoUsuario = findViewById(R.id.fotodePerfil);
        botonCerrarSesion = findViewById(R.id.cerrarSesionUsuario);
    }


    /**
     * Carga la informacion de un usuario en la vista
     * Actualiza la vista con la info del usuario
     * @param: Coctelero
     */
    public void cargarInfoVista(Usuario x){
        nombre.setText(x.getNombre());
        correo.setText(x.getCorreo());
        celular.setText(x.getCelular());
        Picasso.get().load(x.getFoto()).into(fotoUsuario);
    }

    /**
     * Obtener el Usuario de la firebase
     * Cargar los datos del coctelero en la vista
     */
    public void obtenerUsuario(){

        referencia.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    usuario = dataSnapshot.getValue(Usuario.class);
                    cargarInfoVista(usuario);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /**
     * Obtener los pedidos del usuario de la base de datos
     * Cargar los pedidos en la vista
     * Se consulta en la rama de pedidos
     */

    public void obtenerPedidos(){

        referencia.child("pedidos").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                String id = nuevo.getIdUsuario();
                if (idUsuario.equals(id)){
                    adapter.agregarPedidoUsuario(nuevo);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                String id = nuevo.getIdUsuario();
                if (idUsuario.equals(id)){
                    adapter.actualizarPedidoUsuario(nuevo);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Pedido nuevo = dataSnapshot.getValue(Pedido.class);
                String id = nuevo.getIdUsuario();
                if (idUsuario.equals(id)){
                    adapter.borrarPedidoUsuario(nuevo);
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

    /**
     * Cerrar sesion del usuario
     */
    public void cerrarSesion(){
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(PerfilUsuario.this,"Sesion cerrada ", Toast.LENGTH_SHORT).show();
                Intent salto = new Intent(PerfilUsuario.this, Login.class);
                startActivity(salto);
                finish();
            }
        });

    }

}
