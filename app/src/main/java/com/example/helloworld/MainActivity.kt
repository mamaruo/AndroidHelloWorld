package com.example.helloworld

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloworld.ui.theme.HelloWorldTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            HelloWorld()
                            LanguageChangeButton()
                        }
                    }
                }
            }
        }
    }
}


/* 顶部栏 包含应用概述和作者信息 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.topbar),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "2023110224 Mamaruo",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    )
}

@Composable
fun HelloWorld() {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary
        ),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.hw),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageChangeButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val activity = context as? Activity

    val options = remember {
        listOf(
            Triple("简体中文", "cn", Locale.SIMPLIFIED_CHINESE),
            Triple("English", "gb", Locale.ENGLISH),
            Triple("한국어", "kr", Locale.KOREAN)
        )
    }

    val currentLocale = context.resources.configuration.locales.get(0)
    val initialIndex = remember {
        options.indexOfFirst { it.third.language == currentLocale.language }.coerceAtLeast(0)
    }
    var selectedIndex by remember { mutableIntStateOf(initialIndex) }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, (label, country, locale) ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    if (selectedIndex != index) {
                        selectedIndex = index
                        activity?.let { act ->
                            val configuration = act.resources.configuration
                            configuration.setLocale(locale)
                            @Suppress("DEPRECATION")
                            act.resources.updateConfiguration(configuration, act.resources.displayMetrics)
                            act.recreate()
                        }
                    }
                },
                selected = index == selectedIndex,
                icon = { Flag(country = country, modifier = Modifier.size(SegmentedButtonDefaults.IconSize)) },
                label = { Text(text = label) }
            )
        }
    }
}

@Composable
fun Flag(country: String, modifier: Modifier = Modifier) {
    Image(
        painterResource(
            when (country) {
                "kr" -> R.drawable.kr
                "gb" -> R.drawable.gb
                "cn" -> R.drawable.cn
                else -> error("不支持的国家: $country")
            }
        ), null, modifier
    )
}

@Preview
@Composable
fun TopBarPreview() {
    TopBar()
}

@Preview
@Composable
fun FlagPreview() {
    Column {
        Flag("cn", Modifier.size(100.dp))
        Flag("gb", Modifier.size(100.dp))
        Flag("kr", Modifier.size(100.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HelloWorldPreview() {
    HelloWorld()
}