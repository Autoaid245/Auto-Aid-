import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.project.auto_aid.ui.theme.BackgroundLight
import com.project.auto_aid.ui.theme.BluePrimary
import com.project.auto_aid.ui.theme.BlueSecondary
import com.project.auto_aid.ui.theme.TextDark

@Composable
fun AutoAidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val lightColors = lightColorScheme(
        primary = BluePrimary,
        onPrimary = Color.White,

        secondary = BlueSecondary,
        onSecondary = Color.White,

        background = BackgroundLight,
        onBackground = TextDark,

        surface = BackgroundLight,
        onSurface = TextDark,

        surfaceVariant = Color(0xFFE0ECFF),
        onSurfaceVariant = TextDark,

        outline = Color(0xFF7DAFFF)
    )

    MaterialTheme(
        colorScheme = lightColors,
        content = content
    )
}
