package com.example.recyclerviewapi

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerviewapi.adapter.RandomUserAdapter
import com.example.recyclerviewapi.repository.RandomUserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val randomUserAdapter = RandomUserAdapter(null)
        val viewLayout = LinearLayoutManager(this)

        userList = findViewById<RecyclerView>(R.id.recycler_user_list).apply {
            adapter = randomUserAdapter
            layoutManager = viewLayout
        }

    }

    override fun onStart(){
        super.onStart()
        GlobalScope.launch {
            val randomUser = RandomUserRepository.getRandomUsers(10)

            val adapter = userList.adapter as RandomUserAdapter
            if (adapter.randomUser == null){
                adapter.randomUser = randomUser
            }else{
                adapter?.randomUser?.results?.addAll(randomUser?.results?.toMutableList()!!)
            }

            userList.addOnScrollListener(scrollListener(adapter))
            updateRecycleView(adapter)
        }
    }

    private fun scrollListener(adapter: RandomUserAdapter?): RecyclerView.OnScrollListener {
        return object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                GlobalScope.launch {
                    if (!recyclerView.canScrollVertically(1)) {
                        if (adapter != null){
                            val newLoad = RandomUserRepository.getRandomUsers(10)
                            adapter.randomUser?.results?.addAll(newLoad?.results?.toMutableList()!!)
                            updateRecycleView(adapter)
                        }
                    }
                }
            }
        }
    }

    private fun updateRecycleView(adapter: RandomUserAdapter){
        val handler = Handler(Looper.getMainLooper())
        handler.post{
            adapter.notifyDataSetChanged()
        }
    }
}