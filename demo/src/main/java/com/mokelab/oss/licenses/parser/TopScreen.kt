package com.mokelab.oss.licenses.parser

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopScreen(
    showLicenses: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Button(onClick = showLicenses) {
                Text("Show Licenses")
            }
        }
    }
}