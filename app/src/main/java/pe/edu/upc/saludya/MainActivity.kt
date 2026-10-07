package pe.edu.upc.saludya

import android.os.Bundle
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import pe.edu.upc.saludya.iam.presentation.access.RecoveryLinkEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.upc.saludya.core.desingsystem.theme.SaludYaTheme
import pe.edu.upc.saludya.iam.presentation.access.IamRoot

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var recoveryLink by mutableStateOf<RecoveryLinkEvent?>(null)
    private var linkSequence = 0L
    private fun readRecoveryIntent(incoming: Intent) {
        if (incoming.action == Intent.ACTION_VIEW) incoming.dataString?.let {
            recoveryLink = RecoveryLinkEvent(++linkSequence, it)
            setIntent(Intent(incoming).apply { data = null })
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        readRecoveryIntent(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readRecoveryIntent(intent)
        enableEdgeToEdge()
        setContent {
            SaludYaTheme(dynamicColor = false) {
                Surface(color = MaterialTheme.colorScheme.background) { IamRoot(recoveryLink = recoveryLink) }
            }
        }
    }
}
