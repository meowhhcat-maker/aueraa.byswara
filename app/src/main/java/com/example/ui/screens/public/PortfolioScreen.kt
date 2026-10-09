package com.example.ui.screens.public

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PortfolioEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun PortfolioScreen(
    portfolio: List<PortfolioEntity>,
    onSelectProjectForInquiry: (category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Content Creation", "Digital Marketing", "Website Development", "Application Development")
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredList = portfolio.filter {
        if (selectedCategory == "All") true
        else it.category.equals(selectedCategory, ignoreCase = true)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        item {
            SectionTitle(
                title = "Client Success Portfolio",
                subtitle = "Verifiable performance results & brand transformations across 4 departments",
                accentPill = "CASE STUDIES"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Category Filter
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AueraaElectricBlue,
                            selectedLabelColor = Color.White,
                            containerColor = AueraaSurfaceDark,
                            labelColor = AueraaTextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) AueraaElectricBlue else AueraaSurfaceBorder
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(filteredList) { item ->
            FullPortfolioCard(
                project = item,
                onInquireSimilar = { onSelectProjectForInquiry(item.category) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
fun FullPortfolioCard(
    project: PortfolioEntity,
    onInquireSimilar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("portfolio_card_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.3f)))),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = project.category)
                if (project.clientPrivacyConsent) {
                    Text(
                        text = "Client: ${project.clientName}",
                        fontSize = 11.sp,
                        color = AueraaTextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = project.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = project.description,
                fontSize = 13.sp,
                color = AueraaTextSecondary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Verified Metrics Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AueraaSuccessEmerald.copy(alpha = 0.12f))
                    .border(1.dp, AueraaSuccessEmerald.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Verified,
                        contentDescription = null,
                        tint = AueraaSuccessEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "VERIFIABLE RESULT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AueraaSuccessEmerald,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = project.metricsResult,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Before / After Impact
            if (!project.beforeAfterSummary.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceVariant),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "BEFORE & AFTER COMPARISON",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = AueraaNeonCyan
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = project.beforeAfterSummary,
                            fontSize = 12.sp,
                            color = AueraaTextPrimary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // Deliverables & Tech stack
            Text(
                text = "Deliverables: ${project.deliverablesText}",
                fontSize = 12.sp,
                color = AueraaTextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Tech Stack / Tools: ${project.techStack}",
                fontSize = 11.sp,
                color = AueraaTextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AueraaSurfaceBorder)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onInquireSimilar,
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Build Something Similar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
