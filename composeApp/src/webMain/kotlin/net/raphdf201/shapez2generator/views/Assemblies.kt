package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.fileBuilders.Assembly
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
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun AssemblyCard(
    assembly: Assembly,
    onAssemblyChange: (Assembly) -> Unit
) {
    OutlinedCard(Modifier.width(610.dp)) {
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
                Modifier.width(80.dp)
            )
        }
    }
}
