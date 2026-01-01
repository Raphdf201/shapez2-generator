package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.assembliesToggleWidth
import net.raphdf201.shapez2generator.assembliesWidth
import net.raphdf201.shapez2generator.fileBuilders.Assembly
import net.raphdf201.shapez2generator.spacing
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import shapez2_generator.composeapp.generated.resources.Res
import shapez2_generator.composeapp.generated.resources.eye_svgrepo_com
import shapez2_generator.composeapp.generated.resources.minus_svgrepo_com
import shapez2_generator.composeapp.generated.resources.plus_svgrepo_com

@Composable
fun AssembliesSection(
    assemblies: List<Assembly>,
    onAssembliesChange: (List<Assembly>) -> Unit
) {
    Column {
        Text("Assemblies")
        assemblies.forEachIndexed { i, asm ->
            AssemblyCard(
                asm
            ) { newAsm ->
                onAssembliesChange(
                    assemblies.toMutableList().apply { this[i] = newAsm }
                )
            }
            if (i < assemblies.lastIndex) {
                Spacer(Modifier.height(spacing))
            }
        }
    }
}

@Composable
fun AssemblyCard(
    assembly: Assembly,
    onAssemblyChange: (Assembly) -> Unit
) {
    OutlinedCard(Modifier.width(assembliesWidth)) {
        Row(Modifier.padding(10.dp).fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(assembly.name)

            var i by remember { mutableStateOf(
                if (assembly.included && assembly.publicized) 2
                else if (assembly.included && !assembly.publicized) 1
                else 0
            ) }
            IconToggle(
                i,
                {
                    onAssemblyChange(when (it) {
                        0 -> {
                            i = it
                            assembly.copy(included = false, publicized = false)
                        }
                        1 -> {
                            i = it
                            assembly.copy(included = true, publicized = false)
                        }
                        2 -> {
                            i = it
                            assembly.copy(included = true, publicized = true)
                        }
                        else -> assembly
                    })
                },
                listOf(Res.drawable.minus_svgrepo_com, Res.drawable.plus_svgrepo_com, Res.drawable.eye_svgrepo_com),
                Modifier.width(assembliesToggleWidth)
            )
        }
    }
}


@Composable
fun IconToggle(
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit,
    icons: List<DrawableResource>,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    unselectedColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    iconTint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(unselectedColor),
    ) {
        icons.forEachIndexed { i, icon ->
            Box(
                Modifier
                    .weight(1f)
                    .background(
                        if (selectedIndex == i) selectedColor else Color.Transparent
                    )
                    .clickable { onSelectionChanged(i) }
                    .padding(1.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Option ${i + 1}",
                    tint = if (selectedIndex == i) {
                        Color.White
                    } else {
                        iconTint.copy(alpha = 0.6f)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
