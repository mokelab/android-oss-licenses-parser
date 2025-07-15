package com.mokelab.oss.licenses.parser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LicenseListScreen(
    viewModel: LicenseListViewModel,
    back: () -> Unit,
    showLicense: (Library) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LicenseListScreen(
        uiState = uiState,
        back = back,
        load = { viewModel.load() },
        showLicense = showLicense,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseListScreen(
    uiState: LicenseListViewModel.UiState,
    back: () -> Unit,
    load: () -> Unit,
    showLicense: (Library) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ライセンス一覧") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            LicenseListViewModel.UiState.Initial,
            LicenseListViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is LicenseListViewModel.UiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                ) {
                    items(uiState.libraries) { library ->
                        ListItem(
                            headlineContent = { Text(library.name) },
                            modifier = Modifier.clickable {
                                showLicense(library)
                            }
                        )
                        HorizontalDivider()
                    }
                }
            }

            is LicenseListViewModel.UiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "エラー: ${uiState.throwable.localizedMessage ?: "不明"}",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = load) {
                        Text("再読み込み")
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            LicenseListViewModel.UiState.Initial -> {
                load()
            }

            LicenseListViewModel.UiState.Loading -> Unit
            is LicenseListViewModel.UiState.Success -> Unit
            is LicenseListViewModel.UiState.Error -> Unit
        }
    }
}