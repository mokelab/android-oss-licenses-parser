package com.mokelab.oss.licenses.parser

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable

@Composable
fun MainApp() {
    val backStack = rememberNavBackStack(Top)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Top> {
                TopScreen(
                    showLicenses = {
                        backStack.add(LicenseList)
                    }
                )
            }
            entry<LicenseList> {
                val viewModel: LicenseListViewModel = hiltViewModel()
                LicenseListScreen(
                    viewModel = viewModel,
                    back = { backStack.removeAt(backStack.lastIndex) },
                    showLicense = { library ->
                        backStack.add(
                            LicenseBodyRoute(
                                offset = library.offset,
                                length = library.length,
                                name = library.name
                            )
                        )
                    }
                )
            }
            entry<LicenseBodyRoute> { route ->
                val viewModel: LicenseBodyViewModel =
                    hiltViewModel { factory: LicenseBodyViewModel.Factory ->
                        factory.create(
                            offset = route.offset,
                            length = route.length,
                            name = route.name
                        )
                    }
                LicenseBodyScreen(
                    viewModel = viewModel,
                    back = { backStack.removeAt(backStack.lastIndex) },
                )
            }
        }
    )
}

@Serializable
data object Top : NavKey

@Serializable
data object LicenseList : NavKey

@Serializable
data class LicenseBodyRoute(
    val offset: Int,
    val length: Int,
    val name: String,
) : NavKey
