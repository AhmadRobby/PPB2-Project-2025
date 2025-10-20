package com.example.project

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project.databinding.ActivityCreateTodoBinding
import com.example.project.databinding.ActivityEditTodoBinding

class EditTodoActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityEditTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityBinding = ActivityEditTodoBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}