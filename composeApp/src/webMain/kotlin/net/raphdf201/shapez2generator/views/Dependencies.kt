package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.raphdf201.shapez2generator.fileBuilders.ManifestDependency

@Composable
fun DependenciesSection(
    dependencies: List<ManifestDependency>,
    onDependenciesChange: (List<ManifestDependency>) -> Unit
) {
    Column {
        Text("Dependencies")

        dependencies.forEachIndexed { i, dep ->
            DependencyCard(
                dependency = dep,
                onDependencyChange = { newDep ->
                    onDependenciesChange(
                        dependencies.toMutableList().apply { this[i] = newDep }
                    )
                }
            )
            Spacer(Modifier.height(10.dp))
        }

        DependencyButtons(
            onAdd = { onDependenciesChange(dependencies + ManifestDependency("", "", "")) },
            onRemove = {
                if (dependencies.isNotEmpty()) {
                    onDependenciesChange(dependencies.dropLast(1))
                }
            }
        )
    }
}

@Composable
fun DependencyCard(
    dependency: ManifestDependency,
    onDependencyChange: (ManifestDependency) -> Unit
) {
    OutlinedCard {
        TextField(
            value = dependency.ModId,
            onValueChange = { onDependencyChange(ManifestDependency(it, dependency.ModTitle, dependency.Version)) },
            label = { Text("Mod Id") },
            singleLine = true
        )

        TextField(
            value = dependency.ModTitle,
            onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, it, dependency.Version)) },
            label = { Text("Mod Title") },
            singleLine = true
        )

        TextField(
            value = dependency.Version,
            onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, dependency.ModTitle, it)) },
            label = { Text("Version") },
            singleLine = true
        )
    }
}

@Composable
fun DependencyButtons(
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(Modifier, Arrangement.SpaceEvenly) {
        Button(onClick = onAdd) {
            Text("+")
        }
        Spacer(Modifier.width(10.dp))
        Button(onClick = onRemove) {
            Text("-")
        }
    }
}
