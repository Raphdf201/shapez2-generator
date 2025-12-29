package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.fileBuilders.Assembly

@Composable
fun AssembliesSection(
    assemblies: List<Assembly>,
    onAssembliesChange: (List<Assembly>) -> Unit
) {
    Column {
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
    OutlinedCard(Modifier.width(550.dp)) {
        Column(Modifier.padding(10.dp)) {
            Text(assembly.name)

            CheckBox(
                checked = assembly.included,
                onCheckedChange = { onAssemblyChange(Assembly(assembly.name, it, assembly.publicized)) }
            ) {
                Text("Enable")
            }

            CheckBox(
                checked = assembly.publicized,
                onCheckedChange = { onAssemblyChange(Assembly(assembly.name, assembly.included, it)) },
                enabled = assembly.included
            ) {
                Text("Publicized")
            }
        }
    }
}
