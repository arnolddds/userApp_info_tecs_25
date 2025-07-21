package com.sobolev.userapp_infotecs_25

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sobolev.userapp_infotecs_25.ui.theme.UserApp_infoTecs_25Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserApp_infoTecs_25Theme {

            }
        }
    }
}
