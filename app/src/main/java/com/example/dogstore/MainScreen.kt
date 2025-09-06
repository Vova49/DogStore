package com.example.dogstore

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dogstore.data.DogInfo
import com.example.dogstore.data.DogRepository

@Composable
fun MainScreen() {
    val dogRepository = remember { DogRepository() }
    var dogs by remember { mutableStateOf<List<DogInfo>>(emptyList()) }
    
    LaunchedEffect(Unit) {
        dogs = dogRepository.getDogsList()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Заголовок вверху по центру
            Text(
                text = "List Of Dogs",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                color = Color.Black
            )
            
            // Прокручиваемый список собак
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(dogs) { dog ->
                    DogItem(dog, dogRepository)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DogItem(dog: DogInfo, dogRepository: DogRepository) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть - изображение
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                val imageModel = remember { mutableStateOf<Any?>(null) }
                
                LaunchedEffect(dog.id) {
                    imageModel.value = dogRepository.getDogImage(dog.id)
                }
                
                AsyncImage(
                    model = imageModel.value,
                    contentDescription = "Картинка",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Надпись "Картинка" удалена
            }
            
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Правая часть - информация
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Id: ${dog.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "CreatedAt: ${dog.createdAt}",
                    fontSize = 14.sp
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Tags: ${dog.tags.joinToString(", ")}",
                    fontSize = 14.sp
                )
            }
        }
    }
}

