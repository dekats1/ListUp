package com.example.myapplication.ui_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.MyRed
import com.example.myapplication.utils.DrawerEvents

// Иконки категорий — добавьте нужные под ваши пункты меню
private val categoryIcons = listOf(
    "🖥️", "💻", "📱", "🖥", "⌨️", "🖱️", "🎧", "📺"
)

@Composable
fun DrawerMenu(onEvent: (DrawerEvents) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backimage),
            contentDescription = "bg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize()) {
            Header()
            Body { event -> onEvent(event) }
        }
    }
}

@Composable
fun Header() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, MyRed)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Image(
                painter = painterResource(id = R.drawable.header),
                contentDescription = "Header",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = "-Техника-",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MyRed)
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun Body(onEvent: (DrawerEvents) -> Unit) {
    val list = stringArrayResource(id = R.array.drawer_list)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(list) { index, title ->
            DrawerMenuItem(
                title = title,
                icon = categoryIcons.getOrElse(index) { "📦" },
                index = index,
                onClick = { onEvent(DrawerEvents.OnItemClick(title, index)) }
            )
        }
        // Пустой элемент внизу чтобы последний пункт не прилипал к краю
        item { Spacer(modifier = Modifier.height(12.dp)) }
    }
}

@Composable
fun DrawerMenuItem(
    title: String,
    icon: String,
    index: Int,
    onClick: () -> Unit
) {
    // Чередующийся цвет фона для наглядности
    val bgColor = if (index % 2 == 0)
        Color(0xFF1E1E2E)
    else
        Color(0xFF252535)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MyRed.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Иконка категории в круглом фоне
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(MyRed.copy(alpha = 0.3f), Color.Transparent)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = icon, fontSize = 20.sp)
                }

                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Text(
                        text = "Перейти к категории",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MyRed,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}