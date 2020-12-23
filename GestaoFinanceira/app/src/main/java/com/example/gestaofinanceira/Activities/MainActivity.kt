package com.example.gestaofinanceira.Activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestaofinanceira.Adapter.FinancesAdapter
import com.example.gestaofinanceira.Models.Finances
import com.example.gestaofinanceira.Models.User
import com.example.gestaofinanceira.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var user: User? = null

    private lateinit var activitiesList: RecyclerView
    private lateinit var addFinanceButton: Button
    private lateinit var chartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewLayout = LinearLayoutManager(this)
        val financesAdapter = FinancesAdapter(null)

        addFinanceButton = findViewById(R.id.main_button_addactivity)
        addFinanceButton.setOnClickListener(this)

        chartButton = findViewById(R.id.main_button_chart)
        chartButton.setOnClickListener(this)

        activitiesList = findViewById<RecyclerView>(R.id.recycler_finances_list).apply {
            adapter = financesAdapter
            layoutManager = viewLayout
        }

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
                    updateRecycleView()
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.key == auth.currentUser?.uid!!) {
                    user = snapshot.getValue(User::class.java)
                    Log.i("App", "User: ${user}")
                    updateRecycleView()
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_button_addactivity -> {
                val it = Intent(this, FinanceActivity::class.java)
//                it.putExtra("finances", user?.financeActivity)
                startActivity(it)
            }

            R.id.main_button_chart -> {
                val it = Intent(this, ChartActivity::class.java)
                startActivity(it)
            }
        }
    }

    private fun updateRecycleView(){
        GlobalScope.launch {
            val adapter = activitiesList.adapter as FinancesAdapter
            val userWithLast5Activities = user!!.copy()
            userWithLast5Activities.financeActivity = emptyArray<Finances>().toMutableList()

            var countDespesa = 0
            var countLucro = 0
            for (activity in user!!.financeActivity.reversed()){
                if (activity.type == "DESPESA"){
                    if(countDespesa != 5){
                        countDespesa += 1
                        userWithLast5Activities.financeActivity.add(activity)
                    }
                }else{
                    if(countLucro != 5){
                        countLucro += 1
                        userWithLast5Activities.financeActivity.add(activity)
                    }
                }
            }

            adapter.user = userWithLast5Activities

            val handler = Handler(Looper.getMainLooper())
            handler.post {
                adapter.notifyDataSetChanged()
            }
        }
    }
}

