package com.example.gestaofinanceira.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.gestaofinanceira.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginRegister: TextView
    private lateinit var loginSignIn: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        loginEmail = findViewById(R.id.login_text_email)

        loginPassword = findViewById(R.id.login_text_senha)

        loginRegister = findViewById(R.id.login_textView_register)
        loginRegister.setOnClickListener(this)

        loginSignIn = findViewById(R.id.login_button_signin)
        loginSignIn.setOnClickListener(this)
    }

    override fun onClick(v: View?){
        when(v?.id) {
            R.id.login_textView_register -> {
                val it = Intent(this, RegisterActivity::class.java)
                startActivity(it)
            }

            R.id.login_button_signin -> {
                var formOk = true
                if(loginEmail.text.isEmpty()){
                    loginEmail.error = "Este campo não pode ser vazio"
                    formOk = false
                }

                if(loginPassword.text.isEmpty()){
                    loginPassword.error = "Este campo não pode ser vazio"
                    formOk = false
                }

                if (formOk){
                    val email = loginEmail.text.toString()
                    val password = loginPassword.text.toString()

                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            val it = Intent(this, MainActivity::class.java)
                            startActivity(it)
                            finish()
                        }else{
                            Toast.makeText(LoginActivity@this, "Ocorreu um erro ao logar",
                                Toast.LENGTH_SHORT).show()
                            Log.e("App", "Error: ${it.exception}")
                        }
                    }
                }
            }
        }
    }
}