package com.example.eventmanagement.ui.auth

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.activity.viewModels;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.lifecycleScope;
import com.example.eventmanagement.R;
import com.example.eventmanagement.ui.dashboard.DashboardActivity;
import com.example.eventmanagement.utils.UiState;
import com.google.firebase.auth.FirebaseAuth;
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels();
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java)); finish(); return
        }; setContentView(R.layout.activity_login);
        val e = findViewById<EditText>(R.id.email);
        val p = findViewById<EditText>(R.id.password);
        val btn =
            findViewById<Button>(R.id.login); findViewById<Button>(R.id.signup).setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java)
            )
        }; findViewById<TextView>(R.id.reset).setOnClickListener { vm.reset(e.text.toString()) }; btn.setOnClickListener {
            vm.login(
                e.text.toString().trim(),
                p.text.toString()
            )
        }; lifecycleScope.launch {
            vm.state.collect {
                when (it) {
                    UiState.Loading -> btn.isEnabled = false; is UiState.Success -> {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            DashboardActivity::class.java
                        )
                    ); finish()
                }; is UiState.Error -> {
                    btn.isEnabled = true; Toast.makeText(
                        this@LoginActivity,
                        it.message,
                        Toast.LENGTH_LONG
                    ).show()
                }; else -> Unit
                }
            }
        }
    }
}
