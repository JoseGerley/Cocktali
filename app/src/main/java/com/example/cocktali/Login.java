package com.example.cocktali;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Modelo.Coctelero;
import Modelo.Usuario;

public class Login extends AppCompatActivity {



    private EditText username;
    private EditText password;
    private Button iniciarsesion;
    private TextView registrarse;
    private FirebaseAuth registrador;
    private DatabaseReference datos;

    private String correo;
    private String contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        crearelementos();
        escuchariniciarsesion();
        escucharregistrarse();

        registrador = FirebaseAuth.getInstance();
        datos = FirebaseDatabase.getInstance().getReference();
    }


    /**
     * Iniciar los elementos de la vista grafica
     */
    public void crearelementos(){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        iniciarsesion = findViewById(R.id.iniciarsesion);
        registrarse = findViewById(R.id.registrarse);
    }


    /**
     * Escuchar el boton iniciar sesion: Verificar las credenciales y dirigirse al perfil
     */
    public void escuchariniciarsesion(){
        iniciarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarlasesion();


            }
        });
    }


    /**
     * Escuchar la accion de registrarse: Dirigirse a la actividad de registrar
     */

    public void escucharregistrarse(){

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent salto = new Intent(Login.this, Registro.class);
                startActivity(salto);

            }
        });
    }


    /**
     * Valida los campos del usuario
     * Busca en la base de datos si el usuario ya existe
     * Ingresa al perfil de dicho usuario o coctelero
     */
    public void iniciarlasesion(){

        correo = username.getText().toString();
        contraseña = password.getText().toString();

        if(!correo.isEmpty() && !contraseña.isEmpty()){

        registrador.signInWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    consultarFirebaseRamas();
                }
                else{
                    Toast.makeText(Login.this,"No se pudo iniciar sesion, revise los campos",Toast.LENGTH_LONG).show();
                }
            }
        });


        }

        else{
            Toast.makeText(Login.this,"Complete los campos",Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Si el usuario ya inicio sesion le muestre la actividad del perfil
     * Mantener siempre la sesion iniciada, a menos que la persona lo cancele
     * Consultar el UID de la instancia registro
     */
    public void onStart(){
      super.onStart();
        if(registrador.getCurrentUser() !=null){

            datos.child("usuarios").child(registrador.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){ // Si la persona es USUARIO

                        Intent salto = new Intent(Login.this, Feed.class);
                        salto.putExtra("idUsuario", registrador.getCurrentUser().getUid());
                        startActivity(salto);
                        finish();
                    }
                    else if(!dataSnapshot.exists()){ //Si la persona es COCTELERO
                        Intent salto = new Intent(Login.this, PerfilCoctelero.class);
                        salto.putExtra("idCoctelero", registrador.getCurrentUser().getUid());
                        startActivity(salto);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(Login.this,"Error de ingreso",Toast.LENGTH_SHORT).show();
                }
            });

          //startActivity(new Intent());
        }
    }


    /**
     * Consultar el usuario en la base de datos
     * Consulta una persona que tenga el rol de usuario, si no existe busca en la rama cocteleros
     * Si existe guarda el objeto para abrir el perfil
     * Si no existe informa al usuario
     */
    public void consultarFirebaseRamas(){
        final String id= registrador.getCurrentUser().getUid();
        datos.child("usuarios").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Usuario nuevo = dataSnapshot.getValue(Usuario.class);
                    Log.e("Usuario: ",nuevo.getNombre());
                    Intent salto = new Intent(Login.this, Feed.class);
                    salto.putExtra("idUsuario", nuevo.getId());
                    startActivity(salto);
                    finish();
                }

                else {
                    datos.child("cocteleros").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                            if(dataSnapshot2.exists()){
                                Coctelero nuevo = dataSnapshot2.getValue(Coctelero.class);
                                Intent salto = new Intent(Login.this, PerfilCoctelero.class);
                                salto.putExtra("idCoctelero", nuevo.getId());
                                startActivity(salto);
                                finish();
                            }

                            else{

                                Toast.makeText(Login.this,"El usuario no esta registrado",Toast.LENGTH_LONG).show();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





}
