package net.raphdf201.shapez2generator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun App() {
    var dark by remember { mutableStateOf(false) }
    val textColor: Color = if (dark) Color.White else Color.Black
    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = if (dark) grey else Color.White) {
            MultiChoiceSegmentedButtonRow {
                SegmentedButton(false, {}, RectangleShape) {
                    Text("buton", Modifier, textColor)
                }
            }
        }
    }
}
