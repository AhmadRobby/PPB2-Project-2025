package com.example.project

import androidx.credentials.CredentialManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.project.databinding.ActivityMainBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // 1. Binding di Main Activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var credentialManager: CredentialManager
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        credentialManager = CredentialManager.create(this)
        auth = Firebase.auth
        registerEvents()
    }

    fun registerEvents() {
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val request = prepareRequest()
                loginByGoogle(request)
            }
        }
    }

    fun prepareRequest(): GetCredentialRequest {
        val serverClientId =
            "848457682929-1pisj8aqfsutspn4h0m8gb2k5lejeden.apps.googleusercontent.com"

        val googleIdOptions = GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .build()

        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        return request

    }

    fun firebaseLoginCallback(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "login berhasil", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "login gagal", Toast.LENGTH_LONG).show()
                }
            }


    }


suspend fun loginByGoogle(request: GetCredentialRequest) {
    try {
        val result = credentialManager.getCredential(
            context = this,
            request = request
        )

        val credential = result.credential
        val idToken = GoogleIdTokenCredential.createFrom(credential.data)

        firebaseLoginCallback(idToken.idToken)

    } catch (exc: NoCredentialException) {
        Toast.makeText(this, "login gagal" + exc.message, Toast.LENGTH_LONG).show()
    } catch (exc: Exception) {
        Toast.makeText(this, "login gagal" + exc.message, Toast.LENGTH_LONG).show()
    }
}

}


