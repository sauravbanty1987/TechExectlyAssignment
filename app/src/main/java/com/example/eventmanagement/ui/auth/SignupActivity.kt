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
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private val vm: AuthViewModel by viewModels();
    override fun onCreate(b: Bundle?) {
        super.onCreate(b); setContentView(R.layout.activity_signup);
        val e = findViewById<EditText>(R.id.email);
        val p = findViewById<EditText>(R.id.password);
        val c = findViewById<EditText>(R.id.confirm);
        val btn = findViewById<Button>(R.id.signup); btn.setOnClickListener {
            if (p.text.toString() != c.text.toString()) Toast.makeText(
                this,
                "Passwords do not match",
                Toast.LENGTH_SHORT
            ).show() else vm.signup(e.text.toString().trim(), p.text.toString())
        }; lifecycleScope.launch {
            vm.state.collect {
                when (it) {
                    is UiState.Success -> {
                        startActivity(
                            Intent(
                                this@SignupActivity,
                                DashboardActivity::class.java
                            )
                        ); finish()
                    }; is UiState.Error -> Toast.makeText(
                    this@SignupActivity,
                    it.message,
                    Toast.LENGTH_LONG
                ).show(); else -> Unit
                }
            }
        }
    }
}
