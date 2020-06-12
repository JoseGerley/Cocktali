package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cocktali.Fragments.FragmentCocteles;
import com.example.cocktali.Fragments.FragmentPedidos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Modelo.Coctelero;

public class PerfilCoctelero extends AppCompatActivity {


    FragmentCocteles fragmentcocteles;
    FragmentPedidos fragmentpedidos;

    private Button botonPedidos;
    private Button botonCocteles;
    private Button cerrarSesion;

    private DatabaseReference referencia;
    private FirebaseAuth auth;

    private  String id;
    private  Coctelero coctelero;

    private TextView nombreCoctelero, correoCoctelero, descripcionCoctelero;
    private ImageView fotoCoctelero;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_coctelero);


        fragmentcocteles =  new FragmentCocteles();
        fragmentpedidos = new FragmentPedidos();

        inicializarVista();

        botonCocteles = findViewById(R.id.BotCocteleroCoctel);
        botonPedidos = findViewById(R.id.BotCocteleroPedido);
        cerrarSesion = findViewById(R.id.cerrarSesjonCoctelero);

        referencia = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        id= getIntent().getStringExtra("idCoctelero");

        obtenerCoctelero(); //Obtiene el coctelero de la DB y actualiza

        //Agregar el fragment por defecto cuando abra la actividad

        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragments,fragmentcocteles).commit();


        escucharBotonCocteles();
        escucharBotonPedidos();

        cerrarSesion();
    }


    /**
     * Iniciar los componentes de la vista
     */
    public void inicializarVista(){
        nombreCoctelero = findViewById(R.id.perfilCocteleroNombre);
        correoCoctelero = findViewById(R.id.cocteleroCorreo);
        descripcionCoctelero = findViewById(R.id.descripcionPerfilCoctelero);
        fotoCoctelero = findViewById(R.id.fotoPerfilCoctlero);
    }

    /**
     * Crea el fragment coctel y le indica el ID del coctelero
     */
    public  void  crearFragmentCoctel(){
        fragmentcocteles =  new FragmentCocteles();
        Bundle args = new Bundle();
        args.putString("idCoctelero", coctelero.getId());
        fragmentcocteles.setArguments(args);
    }


    /**
     *  Agregar el listener al boton de mostrar los Cocteles
     */
    public void escucharBotonCocteles(){
        botonCocteles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction cambio =  getSupportFragmentManager().beginTransaction();
                cambio.replace(R.id.contenedorFragments,fragmentcocteles);
                cambio.commit();

            }
        });
    }

    /**
     * Agregar el listener al boton de mostrar los Pedidos
     */

    public void escucharBotonPedidos(){
        botonPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction cambio =  getSupportFragmentManager().beginTransaction();
                cambio.replace(R.id.contenedorFragments,fragmentpedidos);
                cambio.commit();
            }
        });
    }


    /**
     * Obtener el coctelero de la firebase
     * Cargar los datos del coctelero en la vista
     */
    public void obtenerCoctelero(){
        referencia.child("cocteleros").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    coctelero = dataSnapshot.getValue(Coctelero.class);
                    cargarInfoVista(coctelero);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Carga la informacion de un coctelero en la vista
     * Muestra la informacion del coctelero en la vista
     * @param: Coctelero
     */
    public void cargarInfoVista(Coctelero x){
        nombreCoctelero.setText(x.getNombre());
        correoCoctelero.setText(x.getCorreo());
        descripcionCoctelero.setText(x.getDescription());
        Picasso.get().load(x.getFoto()).into(fotoCoctelero);
    }


    /**
     * Id del coctelero actual
     * @return
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    /**
     * Cerrar la sesion del usuario
     *
     */
    public void cerrarSesion(){
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(PerfilCoctelero.this,"Sesion cerrada ", Toast.LENGTH_SHORT).show();
                Intent salto = new Intent(PerfilCoctelero.this, Login.class);
                startActivity(salto);
                finish();
            }
        });

    }
}
