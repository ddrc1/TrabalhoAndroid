package com.example.gestaofinanceira.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestaofinanceira.Models.Finances
import com.example.gestaofinanceira.Models.User
import com.example.gestaofinanceira.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class FinanceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var user: User? = null

    private lateinit var financeValue: EditText
    private lateinit var financeDate: EditText
    private lateinit var financeDescription: EditText
    private lateinit var financeExpense: RadioButton
    private lateinit var financeIncome: RadioButton
    private lateinit var financeAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finance)

        financeValue = findViewById(R.id.finance_text_valor)

        financeDate = findViewById(R.id.finance_text_date)

        financeDescription = findViewById(R.id.finance_text_descricao)

        financeExpense = findViewById(R.id.finance_radioButton_expense)
        financeIncome = findViewById(R.id.finance_radioButton_income)

        financeAdd = findViewById(R.id.finance_button_add)
        financeAdd.setOnClickListener(this)

    }

    private fun getAndUpdateUser(finance: Finances){
        auth = Firebase.auth
        database = Firebase.database

        Log.i("App", "UID: ${auth.currentUser?.uid}")

        val userRef = database.reference.child("user")
        userRef.orderByChild("name").addChildEventListener(object : ChildEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key == auth.currentUser?.uid!!) {
                    user = snapshot.getValue(User::class.java)
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key == auth.currentUser?.uid!!) {
                    user = snapshot.getValue(User::class.java)
                    Log.i("App", "User: ${user}")
                    user!!.financeActivity.add(finance)
                    user!!.financeActivity.sort()
                    updateUser(userRef, user!!)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }



    private fun updateUser(userRef: DatabaseReference, user: User) {
        auth.updateCurrentUser(auth.currentUser!!).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                userRef.child(auth.currentUser?.uid!!).setValue(user)
                Toast.makeText(RegisterActivity@ this, "Atividade registrada com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(RegisterActivity@ this, "Ocorreu um erro ao registrar a atividade", Toast.LENGTH_SHORT).show()
                Log.e("App", "Error: ${it.exception}")
            }

        }
    }

    override fun onClick(v: View?) {
        var formOk = true

        var value: Double? = null
        if(financeValue.text.isEmpty()){
            financeValue.error = "Este campo não pode ser vazio"
            formOk = false
        }else{
            try {
                value = financeValue.text.toString().toDouble()
                if (value <= 0){
                    formOk = false
                }
            }catch (e: Exception){
                financeValue.error =  "O valor inválido"
                formOk = false
            }
        }

        var date: Date? = null
        if(financeDate.text.isEmpty()){
            financeDate.error = "Este campo não pode ser vazio"
            formOk = false
        }else{
            try {
                date = SimpleDateFormat("dd/MM/yyyy").parse(financeDate.text.toString())!!
            }catch (e: Exception){
                financeDate.error =  "O valor inválido"
                formOk = false
            }
        }
        var financeType: String
        if(financeExpense.isChecked){
            financeType = "DESPESA"
        }else{
            financeType = "RECEITA"
        }

        if (formOk) {
            val type = financeType
            val description = financeDescription.text.toString()
            val finance = Finances(description, value!!, type, date!!)
            getAndUpdateUser(finance)
        }

    }

}