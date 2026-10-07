package pe.edu.upc.saludya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.iam.domain.model.VerifiedIdentity
import pe.edu.upc.saludya.iam.presentation.identity.IdentityVerificationRoute
import pe.edu.upc.saludya.iam.presentation.registration.RegistrationRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SaludYaTheme(dynamicColor = false) {
                var verified by remember { mutableStateOf<VerifiedIdentity?>(null) }
                val identity = verified
                Surface(color = MaterialTheme.colorScheme.background) {
                    if (identity == null) {
                        IdentityVerificationRoute(onContinueRegistration = { verified = it })
                    } else {
                        BackHandler { verified = null }
                        RegistrationRoute(identity, onBack = { verified = null })
                    }
                }
            }
        }
    }
}
