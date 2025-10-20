package com.example.project

import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project.adapter.TodoAdapter
import com.example.project.databinding.ActivityTodoBinding
import com.example.project.databinding.TestingBinding
import com.example.project.entity.Todo
import com.example.project.usecases.TodoUseCase
import kotlinx.coroutines.launch


class TodoActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityTodoBinding
    private lateinit var TodoAdapter: TodoAdapter
    private lateinit var todoUseCase: TodoUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupRecyclerView()
        initializeData()
        registerEvents()
    }
    fun registerEvents(){
        activityBinding.tombolTambah.setOnClickListener{
            toCreateTodoPage()
        }
    }
    private fun setupRecyclerView() {
        TodoAdapter= TodoAdapter(mutableListOf())
        activityBinding.container.adapter = TodoAdapter
        activityBinding.container.layoutManager= LinearLayoutManager(this)
    }

    fun initializeData() {
        activityBinding.container.visibility = View.GONE
        activityBinding.uiLoading.visibility = View.VISIBLE

        lifecycleScope.launch {
            val data = todoUseCase.getTodo()
            activityBinding.container.visibility= View.VISIBLE
            activityBinding.uiLoading.visibility= View.GONE
            TodoAdapter.updateDataSet(data)
        }
    }

    fun displayMessege(message: String){
        Toast.makeText(this@TodoActivity, message, Toast.LENGTH_SHORT).show()
    }
    fun toCreateTodoPage(){
        val intent =intent(this, CreateTodoActivity::class.java)
        startActivity(intent)
        finish()
    }
}