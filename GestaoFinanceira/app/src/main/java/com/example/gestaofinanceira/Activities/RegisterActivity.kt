package com.example.gestaofinanceira.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gestaofinanceira.Models.User
import com.example.gestaofinanceira.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerEmail: EditText
    private lateinit var registerPassword: EditText
    private lateinit var registerSignOn: Button
    private lateinit var registerName: EditText
    private lateinit var registerPhone: EditText

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        database = Firebase.database

        registerEmail = findViewById(R.id.register_text_email)

        registerPassword = findViewById(R.id.register_text_password)

        registerName = findViewById(R.id.register_text_name)

        registerPhone = findViewById(R.id.register_text_phone)

        registerSignOn = findViewById(R.id.register_button_signon)
        registerSignOn.setOnClickListener{
            var formOk = true
            if(registerEmail.text.isEmpty()){
                registerEmail.error = "Este campo não pode ser vazio"
                formOk = false
            }

            if(registerPassword.text.isEmpty()){
                registerPassword.error = "Este campo não pode ser vazio"
                formOk = false
            }

            if (formOk){
                val email = registerEmail.text.toString()
                val password = registerPassword.text.toString()
                val name  = registerName.text.toString()
                val phone = registerPhone.text.toString()

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                    if(it.isSuccessful){
//                        Finances("abcdefghijklmnopqrstuvwxyz", 250.0, "Despesa", SimpleDateFormat("yyyy-MM-dd").parse("2020-06-15")!!)
                        val user = User(email, password, name, phone, ArrayList()) //

                        val userRef = database.reference.child("user")
                        userRef.child(auth.currentUser?.uid!!).setValue(user)

                        Toast.makeText(RegisterActivity@this, "Usuário cadastrado com sucesso",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(RegisterActivity@this, "Ocorreu um erro ao cadastrar",
                            Toast.LENGTH_SHORT).show()
                        Log.e("App", "Error: ${it.exception}")
                    }

                }
            }
        }
    }
}