package com.example.gestaofinanceira.Activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gestaofinanceira.Models.Finances
import com.example.gestaofinanceira.Models.User
import com.example.gestaofinanceira.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class ChartActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var user: User? = null

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        chart = findViewById(R.id.chart)

        getUserAndBuildChart()
//
    }

    private fun getUserAndBuildChart(){
        auth = Firebase.auth
        database = Firebase.database

        val userRef = database.reference.child("user")
        Log.i("App", "UID: ${1}")
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
                    buildChart(user!!)
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun buildChart(user: User){

        var entriesIncome: MutableList<Entry> = emptyList<Entry>().toMutableList()
        var entriesExpenses: MutableList<Entry> = emptyList<Entry>().toMutableList()
        var entriesBalance: MutableList<Entry> = emptyList<Entry>().toMutableList()

        val firstDate = TimeUnit.MILLISECONDS.toDays(user.financeActivity.first().date.time).toFloat()

        var type: String
        var days: Float
        var index: Int
        var accumValue: Float = 0F
        var balanceValue = 0F
        for(activity in user.financeActivity){
            index = user.financeActivity.indexOf(activity)
            type = activity.type
            days = TimeUnit.MILLISECONDS.toDays(activity.date.time) - firstDate

            if((index + 1) <= user.financeActivity.size - 1) {
                if (activity.date.equals(user.financeActivity[index + 1].date) && activity.type.equals(
                        user.financeActivity[index + 1].type)) {
                    accumValue += activity.value.toFloat()
                }else{
                    if(accumValue != 0F){
                        accumValue += activity.value.toFloat()
                        if(type.equals("DESPESA")){
                            balanceValue -= accumValue
                            entriesExpenses.add(Entry(days, accumValue))
                        }else{
                            balanceValue += accumValue
                            entriesIncome.add(Entry(days, accumValue))
                        }
                        accumValue = 0F
                    }else{
                        if(type.equals("DESPESA")){
                            balanceValue -= activity.value.toFloat()
                            entriesExpenses.add(Entry(days, activity.value.toFloat()))
                        }else{
                            balanceValue += activity.value.toFloat()
                            entriesIncome.add(Entry(days, activity.value.toFloat()))
                        }
                    }
                    if(!activity.date.equals(user.financeActivity[index + 1].date)){
                        entriesBalance.add(Entry(days, balanceValue))
                    }
                }
            }else{
                if(accumValue != 0F){
                    accumValue += activity.value.toFloat()
                    if(type.equals("DESPESA")){
                        balanceValue -= accumValue
                        entriesExpenses.add(Entry(days, accumValue))
                    }else{
                        balanceValue += accumValue
                        entriesIncome.add(Entry(days, accumValue))
                    }
                    accumValue = 0F
                }else{
                    if(type.equals("DESPESA")){
                        balanceValue -= activity.value.toFloat()
                        entriesExpenses.add(Entry(days, activity.value.toFloat()))
                    }else{
                        balanceValue += activity.value.toFloat()
                        entriesIncome.add(Entry(days, activity.value.toFloat()))
                    }
                }
                entriesBalance.add(Entry(days, balanceValue))
            }
        }

        val datasetIncome = LineDataSet(entriesIncome, "Receita")
        datasetIncome.color = Color.GREEN

        val datasetExpenses = LineDataSet(entriesExpenses, "Despesa")
        datasetExpenses.color = Color.RED

        val datasetBalance = LineDataSet(entriesBalance, "Saldo")
        datasetBalance.color = Color.rgb(255, 165, 0)

        val listDataSet = listOf(datasetIncome, datasetExpenses, datasetBalance)
        val lineData = LineData(listDataSet)
        chart.apply {
            data = lineData
            axisRight.isEnabled = false
            description.isEnabled = false
        }

        chart.invalidate()
    }
}