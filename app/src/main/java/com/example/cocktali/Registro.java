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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import Modelo.Coctelero;
import Modelo.Usuario;

public class Registro extends AppCompatActivity {


    private EditText nombreregistro;
    private EditText nickregistro;
    private EditText password;
    private EditText celular;
    private EditText descripcion;
    private CheckBox rol;
    private Button botonRegistro;
    private Boolean esCoctelero;
    private Button galeria;
    private ImageView foto;
    private static  final  int REFGALERY = 1;


    FirebaseAuth reg;
    private DatabaseReference bd;
    private StorageReference storage;
    private String correo;
    private String contraseña;
    private String numeroCelular;
    private String nombre;
    private String id;
    private String nick;
    private String descripcionCoc;
    private Uri path;
    private  String dow;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        reg = FirebaseAuth.getInstance();
        bd = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cocktali-30dad.firebaseio.com/");
        storage = FirebaseStorage.getInstance().getReference();

        esCoctelero=false;
        dow="";


        iniciarcomponentesvista();
        escucharcheckboxrol();
        escogerFoto();
        escucharbotonregistro();


    }


    /**
     * Crear los componentes de la vista
     */
    public void iniciarcomponentesvista(){

        nombreregistro = findViewById(R.id.nombreRegistro);
        nickregistro = findViewById(R.id.usuarioRegistro);
        password = findViewById(R.id.contraseñaRegistro);
        celular = findViewById(R.id.numeroRegistro);
        descripcion = findViewById(R.id.descripcionRegistro);
        rol = findViewById(R.id.checkBoxRegistro);
        botonRegistro = findViewById(R.id.registrarmeBoton);
        galeria = findViewById(R.id.cambiarfotoRegistro);
        foto = findViewById(R.id.fotoRegistro);

    }


    /**
     * Agregar el listener del boton registro
     * Hacer el registro en firebase
     * Guardar los nuevos integrantes en la DB
     * Verificar que cumple con los requisitos para el registro
     */
    public void escucharbotonregistro(){
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             obtenerdata();

             if (esCoctelero==true){
                 validarRegistroCoctelero();
             }
             else{
                 //Registrar usuario
                 validarRegistroUsuario();

             }
            }
        });
    }

    /**
     * Agregrar el listener del checkbox
     * Definir el rol de la persona
     */
    public void escucharcheckboxrol() {
        rol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked){
                    // El nuevo usuario es Coctelero
                    esCoctelero=true;
                }

                else{
                    esCoctelero=false;
                }
            }
        }
        );
    }


    /**
     * Registra una nueva persona en el firebase
     * Si el registro es exitoso, escribe el cotelero en la base de datos
     */
    public void registrarCoctelero(){
        reg.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //String idrandom = bd.child("cocteleros").push().getKey();
                    final String ram = reg.getCurrentUser().getUid();

                    final Coctelero nuevo = new Coctelero(ram,nombre,descripcionCoc,numeroCelular,correo,contraseña,"");
                    bd.child("cocteleros").child(ram).setValue(nuevo).addOnCompleteListener(new OnCompleteListener<Void>() { //Agregar el coctelero a la base de datos
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //Agregar la foto del coctelero al storage
                                final StorageReference ref = storage.child("avatars").child(ram);
                                ref.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() { //Obtener el URL de la imagen
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                dow =uri.toString(); //Guardar la URL
                                                Log.e("Enlace ",dow);
                                                bd.child("cocteleros").child(ram).child("foto").setValue(dow).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override //Agrega el link de la foto a la base de datos
                                                    public void onComplete(@NonNull Task<Void> task3) {
                                                        if(task3.isSuccessful()){

                                                            Toast.makeText(Registro.this,"Registrado correctamente",Toast.LENGTH_LONG).show();
                                                            Intent salto = new Intent(Registro.this, PerfilCoctelero.class);
                                                            salto.putExtra("idCoctelero", nuevo.getId());
                                                            startActivity(salto);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Registro.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    /**
     * Registra una nueva persona usuario en el firebase
     * Si el registro es exitoso, escribe el usuario en la base de datos
     */
    public void registrarUsuario(){
        reg.createUserWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //String idrandom = bd.child("usuarios").push().getKey();
                    final String ram = reg.getCurrentUser().getUid();

                    final Usuario nuevo = new Usuario(ram,nombre,correo,contraseña,numeroCelular,descripcionCoc,"");
                    bd.child("usuarios").child(ram).setValue(nuevo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                //Agregar la foto del usuario al storage
                                final StorageReference ref = storage.child("avatars").child(ram);
                                ref.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                dow =uri.toString();
                                                Log.e("Enlace ",dow);
                                                bd.child("usuarios").child(ram).child("foto").setValue(dow).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override //Agrega el link de la foto a la base de datos
                                                    public void onComplete(@NonNull Task<Void> task3) {
                                                        if(task3.isSuccessful()){
                                                            Toast.makeText(Registro.this,"Registrado correctamente",Toast.LENGTH_LONG).show();
                                                            Intent salto = new Intent(Registro.this, Feed.class);
                                                            salto.putExtra("idUsuario", nuevo.getId());
                                                            startActivity(salto);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Registro.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }







    /**
     * Obtiene los datos  de la vista, para registrar una persona
     *
     */
    public void obtenerdata(){
        nombre = nombreregistro.getText().toString();
        correo =  nickregistro.getText().toString();
        descripcionCoc = descripcion.getText().toString();
        numeroCelular = celular.getText().toString();
        nick = nombre;
        contraseña =  password.getText().toString();
    }


    /**
     * Valida las condiciones para registrar coctelero
     * Utiliza el metodo que escribe al coctelero en DB
     */
    public void validarRegistroCoctelero(){
        //Validacion del registro
        if(!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty() && !numeroCelular.isEmpty()){
            if(contraseña.length() > 5){
                registrarCoctelero();
            }
            else {
                Toast.makeText(Registro.this,"La contraseña minimo debe tener 6 caracteres",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(Registro.this,"Debe diligenciar todos los campos",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Valida las condiciones para registrar usuario
     * Utiliza el metodo que Registra al usuario en DB
     */
    public void validarRegistroUsuario(){
        //Validacion del registro
        if(!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty() && !numeroCelular.isEmpty()){
            if(contraseña.length() > 5){
                registrarUsuario();
            }
            else {
                Toast.makeText(Registro.this,"La contraseña minimo debe tener 6 caracteres",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(Registro.this,"Debe diligenciar todos los campos",Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Permite escoger la foto de la galeria
     */
    public void  escogerFoto(){
        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REFGALERY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REFGALERY && resultCode== RESULT_OK){
            path = data.getData();
            foto.setImageURI(path);
        }
    }



}
