package com.mokelab.oss.licenses.parser

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun LicenseBodyScreen(
    viewModel: LicenseBodyViewModel,
    back: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LicenseBodyScreen(
        uiState = uiState,
        back = back,
        load = { viewModel.load() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseBodyScreen(
    uiState: LicenseBodyViewModel.UiState,
    back: () -> Unit,
    load: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ライセンス詳細") },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            LicenseBodyViewModel.UiState.Initial,
            LicenseBodyViewModel.UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is LicenseBodyViewModel.UiState.Error -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
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

            is LicenseBodyViewModel.UiState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = uiState.body,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            LicenseBodyViewModel.UiState.Initial -> {
                load()
            }

            LicenseBodyViewModel.UiState.Loading -> Unit
            is LicenseBodyViewModel.UiState.Success -> Unit
            is LicenseBodyViewModel.UiState.Error -> Unit
        }
    }
}