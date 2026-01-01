package net.raphdf201.shapez2generator.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import net.raphdf201.shapez2generator.ManifestDependency
import net.raphdf201.shapez2generator.SharedWorkshopItem
import net.raphdf201.shapez2generator.SimpleWorkshopItem
import net.raphdf201.shapez2generator.dependenciesWidth
import net.raphdf201.shapez2generator.spacing
import org.jetbrains.compose.resources.painterResource
import shapez2_generator.composeapp.generated.resources.Res
import shapez2_generator.composeapp.generated.resources.minus_svgrepo_com
import shapez2_generator.composeapp.generated.resources.plus_svgrepo_com
import kotlin.coroutines.coroutineContext

@Composable
fun DependenciesSection(
    dependencies: List<ManifestDependency>,
    onDependenciesChange: (List<ManifestDependency>) -> Unit,
    steamDependencies: List<SimpleWorkshopItem>,
    onSteamDependencySelect: (Int) -> Unit
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
                },
                removeDep = {
                    if (dependencies.isNotEmpty()) {
                        onDependenciesChange(dependencies.filterIndexed { idx, _ -> idx != i })
                    }
                }
            )
            Spacer(Modifier.height(spacing))
        }

        Row(Modifier.width(dependenciesWidth), Arrangement.SpaceEvenly) {
            Button({ onDependenciesChange(dependencies + ManifestDependency("local:", "", "")) }) {
                Row {
                    Icon(painterResource(Res.drawable.plus_svgrepo_com), "add new dependency", Modifier.size(15.dp))
                    Text("local")
                }
            }
            Box {
                var expanded by remember { mutableStateOf(false) }
                Button({
                    expanded = !expanded
                }) {
                    Row {
                        Icon(painterResource(Res.drawable.plus_svgrepo_com), "add new dependency", Modifier.size(15.dp))
                        Text("steam")
                    }
                }
                DropdownMenu(expanded, { expanded = false }) {
                    steamDependencies.forEachIndexed { i, dep ->
                        DropdownMenuItem({
                            Text(dep.steamName)
                        }, {
                            expanded = false
                            onSteamDependencySelect(i)
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun DependencyCard(
    dependency: ManifestDependency,
    onDependencyChange: (ManifestDependency) -> Unit,
    removeDep: () -> Unit
) {
    OutlinedCard(Modifier.width(dependenciesWidth)) {
        TextField(
            value = dependency.ModId,
            onValueChange = { onDependencyChange(ManifestDependency(it, dependency.ModTitle, dependency.Version)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mod Id") },
            singleLine = true
        )

        TextField(
            value = dependency.ModTitle,
            onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, it, dependency.Version)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mod Title") },
            singleLine = true
        )

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = dependency.Version,
                onValueChange = { onDependencyChange(ManifestDependency(dependency.ModId, dependency.ModTitle, it)) },
                label = { Text("Version") },
                singleLine = true
            )
            Button(
                onClick = removeDep,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(painterResource(Res.drawable.minus_svgrepo_com), "remove dependency", Modifier.size(15.dp))
            }
        }
    }
}
