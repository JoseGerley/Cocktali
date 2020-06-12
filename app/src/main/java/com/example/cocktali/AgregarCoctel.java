package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import Modelo.Coctel;

public class AgregarCoctel extends AppCompatActivity {


    private String idCoctelero;
    private EditText nombre, precio, descipcion;
    private ImageView foto;
    private Button agregar;
    private Uri path;
    private DatabaseReference referencia;
    private StorageReference storage;
    private static  final  int REFGALERY = 10;

    private String nombreCoctel;
    private String precioCoctel;
    private String fotoCoctel;
    private String descripcionCotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_coctel);
        idCoctelero= getIntent().getStringExtra("idCoctelero");

        referencia = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();

        iniciarVista();
        esucucharFotoCoctel();
        esucharBotonAgregar();
    }

    /**
     * Iniciar los componentes de la vista
     */
    public  void iniciarVista(){
        nombre=findViewById(R.id.nombreNuevoCoctel);
        precio=findViewById(R.id.precioNuevoCoctel);
        descipcion = findViewById(R.id.descripcionNuevoCoctel);
        foto = findViewById(R.id.fotoNuevoCoctel);
        agregar = findViewById(R.id.botonAñadirCoctel);
    }

    /**
     * Agregar un nuevo coctel, al inventario del coctelero seleccionado
     * Actualizar el arreglo coctel en la base de datos
     */
    public  void  nuevoCoctel(){
       final String ram = referencia.child("cocteleros").child(idCoctelero).child("coteles").push().getKey();
        Coctel nuevo = new Coctel(ram,nombreCoctel,descripcionCotel,precioCoctel,"");
        referencia.child("cocteleros").child(idCoctelero).child("cocteles").child(ram).setValue(nuevo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                final StorageReference ref = storage.child("cocteles").child(ram);
                ref.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                               final String dow =uri.toString(); //Guardar la URL
                                referencia.child("cocteleros").child(idCoctelero).child("cocteles").child(ram).child("foto").setValue(dow).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> tarea) {
                                        if (tarea.isSuccessful()){
                                            Toast.makeText(AgregarCoctel.this,"Registrado correctamente",Toast.LENGTH_LONG).show();
                                            Intent salto = new Intent(AgregarCoctel.this, PerfilCoctelero.class);
                                            salto.putExtra("idCoctelero", idCoctelero);
                                            startActivity(salto);
                                        }

                                    }
                                });

                            }
                        });
                    }
                });

            }
        });
    }

    /**
     * Agregar el listener para cambiar la la foto del coctel
     */
    public void  esucucharFotoCoctel(){
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REFGALERY);
            }
        });

    }

            /**
             * Agregar el listener al boton que permite agregar un coctel
             */
            public void esucharBotonAgregar() {

            agregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obtenerDataCoctel();
                    nuevoCoctel();
                }
            });

            }


            @Override
            protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                if (requestCode == REFGALERY && resultCode == RESULT_OK) {
                    path = data.getData();
                    foto.setImageURI(path);
                }
            }


    /**
     * Obtener la data de la vista para el cotel
     */
    public  void  obtenerDataCoctel(){
        nombreCoctel = nombre.getText().toString();
        precioCoctel = precio.getText().toString();
        descripcionCotel = descipcion.getText().toString();

    }

}
