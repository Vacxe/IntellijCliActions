package io.github.vacxe.cliactions.ui

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LazyScrollable(content: LazyListScope.() -> Unit) {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(10.dp)
        ) {
            val state = rememberLazyListState()

            LazyColumn(Modifier.fillMaxSize().padding(end = 12.dp), state,
                content = content)
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }
    }
}

@Composable
fun TextBox(text: String = "Item") {
    Surface(
        color = Color(135, 135, 135, 40),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.height(32.dp)
                .fillMaxWidth()
                .padding(start = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = text)
        }
    }
}