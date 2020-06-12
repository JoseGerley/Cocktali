package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Adapters.AdapterComprarCocteles;
import Modelo.Coctel;
import Modelo.Coctelero;
import Modelo.Pedido;

public class ComprarCoctel extends AppCompatActivity {


    private AdapterComprarCocteles adapter;
    private String idCoctelero;
    private String idUsuario;
    private String fotoUsuario;
    private String nombreUsuario;
    private ListView menuCoctelero;
    private DatabaseReference referencia;
    private Coctelero coctelero;

    private Button comprar;

    private TextView nombre, celular, direccion, descripcion;
    private ImageView foto;


   public static final String ACEPTADO = "aceptado";
   public static final String RECHAZADO = "rechazado";
   public static final String ESPERA = "espera";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_coctel);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        iniciarVista();
        referencia = FirebaseDatabase.getInstance().getReference();
        idCoctelero = getIntent().getStringExtra("idCoctelero");
        fotoUsuario = getIntent().getStringExtra("fotoUsuario");
        idUsuario = getIntent().getStringExtra("idUsuario");
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        actualizarCoctelero();

        adapter = new AdapterComprarCocteles();
        menuCoctelero = findViewById(R.id.listaCoctelesComprar);
        menuCoctelero.setAdapter(adapter);

        escucharComprar();
        rellenarMenu();
    }



    public void iniciarVista(){
        nombre = findViewById(R.id.nombreVendedor);
        celular = findViewById(R.id.numeroVendedor);

        descripcion=findViewById(R.id.descripcionVendedor);
        direccion = findViewById(R.id.comentarioPedido);

        foto = findViewById(R.id.imagenVendedor);
        comprar = findViewById(R.id.ComprarCoctel);
    }


    /**
     * Agregar cocteles a el menu del coctelero en cuestion
     * Consulta el hashmap de cocteles en la DB, del coctelero seleccionado
     * Permite agregar un nuevo coctel al menu, desde el adapter
     */
    public  void  rellenarMenu(){

        referencia.child("cocteleros").child(idCoctelero).child("cocteles").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Coctel nuevo = dataSnapshot.getValue(Coctel.class);
                adapter.agregarCoctelMenuComprar(nuevo);
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
     * Obtener el coctelero seleccionado
     * Buscar el coctelero en la base de datos
     */
    public void actualizarCoctelero(){
            referencia.child("cocteleros").child(idCoctelero).addListenerForSingleValueEvent(new ValueEventListener() {
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
        nombre.setText(x.getNombre());
        descripcion.setText(x.getDescription());
        celular.setText(x.getNumero());
        Picasso.get().load(x.getFoto()).into(foto);
    }

    /**
     * Agregar el listener para hacer un nuevo pedido al coctelero
     * Actualiza la base de datos en la rama, pedidos
     */
    public  void escucharComprar(){

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerCompra();
            }
        });

    }

    /**
     * Hacer efectiva la compra y agregarla a la base de datos
     */
    public void hacerCompra(){
       final String id =referencia.child("pedidos").push().getKey();
        String dir =  direccion.getText().toString();
        Pedido nuevo = new Pedido(id,idCoctelero,idUsuario,coctelero.getNumero(),dir,ESPERA,coctelero.getFoto(),fotoUsuario,coctelero.getNombre(),nombreUsuario);
        referencia.child("pedidos").child(id).setValue(nuevo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if(task.isComplete()){
                Log.e("registro" , "registro exitoso");

                referencia.child("pedidos").child(id).child("cocteles").setValue(adapter.getCoctelespedido());
                Toast.makeText(ComprarCoctel.this,"Su pedido se realizo correctamente",Toast.LENGTH_LONG).show();
               // Intent salto = new Intent(ComprarCoctel.this,ComprarCoctel.class);
                //salto.putExtra("idUsuario",idUsuario);
                //startActivity(salto);
                //finish();
            }

            }
        });

    }


}
