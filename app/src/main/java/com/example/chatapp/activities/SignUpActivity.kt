package com.example.chatapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnSignUp: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://chatapp-139b8-default-rtdb.europe-west1.firebasedatabase.app/")

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPassword = findViewById(R.id.editPass)
        btnSignUp = findViewById(R.id.btnSignUp)

        btnSignUp.setOnClickListener {
            //Obtener datos
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val pass = editPassword.text.toString()

            signUp(name, email, pass)
        }

    }

    private fun signUp(name: String, email: String, pass: String) {
        //lógica para registrar usuario
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if(it.isSuccessful){
                    //dirigirse a la pagina principal
                    addUser(name, email, auth.currentUser!!.uid)
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Some error ocurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUser(name: String, email: String, uid: String) {
        database.getReference("users").child(uid).setValue(User(name, email, uid))
    }
}