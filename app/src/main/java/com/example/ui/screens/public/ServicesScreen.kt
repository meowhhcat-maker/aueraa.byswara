package com.example.ui.screens.public

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.model.PackageEntity
import com.example.data.model.PortfolioEntity
import com.example.data.model.ServiceEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ServicesScreen(
    services: List<ServiceEntity>,
    packages: List<PackageEntity>,
    portfolio: List<PortfolioEntity>,
    initialCategoryKey: String? = null,
    onEnquireForService: (serviceName: String) -> Unit,
    onSelectPackage: (packageName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryKeys = listOf("CONTENT_CREATION", "DIGITAL_MARKETING", "WEBSITE_DEV", "APP_DEV")
    var selectedCategoryKey by remember(initialCategoryKey) {
        mutableStateOf(initialCategoryKey ?: "CONTENT_CREATION")
    }

    val currentService = services.find { it.categoryKey == selectedCategoryKey }
        ?: services.firstOrNull()

    val categoryPackages = packages.filter { pkg ->
        when (selectedCategoryKey) {
            "CONTENT_CREATION" -> pkg.category.contains("Content", ignoreCase = true)
            "DIGITAL_MARKETING" -> pkg.category.contains("Marketing", ignoreCase = true)
            "WEBSITE_DEV" -> pkg.category.contains("Website", ignoreCase = true)
            "APP_DEV" -> pkg.category.contains("Application", ignoreCase = true)
            else -> true
        }
    }

    val categoryPortfolio = portfolio.filter { item ->
        when (selectedCategoryKey) {
            "CONTENT_CREATION" -> item.category.contains("Content", ignoreCase = true)
            "DIGITAL_MARKETING" -> item.category.contains("Marketing", ignoreCase = true)
            "WEBSITE_DEV" -> item.category.contains("Website", ignoreCase = true)
            "APP_DEV" -> item.category.contains("Application", ignoreCase = true)
            else -> true
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        item {
            SectionTitle(
                title = "Main Service Categories",
                subtitle = "Bespoke digital solutions with specialized engineering & creative teams",
                accentPill = "DEPARTMENTS"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Category Tab Switcher
        item {
            ScrollableTabRow(
                selectedTabIndex = categoryKeys.indexOf(selectedCategoryKey).coerceAtLeast(0),
                containerColor = AueraaSurfaceDark,
                contentColor = AueraaElectricBlue,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                categoryKeys.forEach { key ->
                    val title = when (key) {
                        "CONTENT_CREATION" -> "Content Creation"
                        "DIGITAL_MARKETING" -> "Digital Marketing"
                        "WEBSITE_DEV" -> "Website Dev"
                        else -> "App Dev"
                    }
                    val isSelected = selectedCategoryKey == key
                    Tab(
                        selected = isSelected,
                        onClick = { selectedCategoryKey = key },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AueraaNeonCyan else AueraaTextSecondary
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (currentService != null) {
            // Service Overview Hero Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.3f)))),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadge(status = currentService.badgeText)
                            CurrencyText(
                                amount = currentService.startingPriceInr,
                                frequency = "Starting Guide",
                                fontSize = 16,
                                color = AueraaNeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = currentService.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentService.fullDescription,
                            fontSize = 13.sp,
                            color = AueraaTextSecondary,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onEnquireForService(currentService.title) },
                            colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("service_enquire_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enquire for ${currentService.title}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Deliverables Checklist
            item {
                Text(
                    text = "Standard Deliverables & Capabilities",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentService.deliverablesList.split("\n").filter { it.isNotBlank() }.forEach { deliverable ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AueraaElectricBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = deliverable,
                                    fontSize = 13.sp,
                                    color = AueraaTextPrimary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Business Benefits
            item {
                Text(
                    text = "Strategic Advantages",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        currentService.benefitsList.split("\n").filter { it.isNotBlank() }.forEach { benefit ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = AueraaAccentAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = benefit,
                                    fontSize = 13.sp,
                                    color = AueraaTextSecondary
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Category Specific Packages
            if (categoryPackages.isNotEmpty()) {
                item {
                    Text(
                        text = "Related Packages",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(categoryPackages) { pkg ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = pkg.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                CurrencyText(amount = pkg.priceInr, isCustomQuote = pkg.isCustomQuoteRequired, frequency = pkg.billingFrequency, fontSize = 15)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = pkg.description, fontSize = 12.sp, color = AueraaTextSecondary)
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { onSelectPackage(pkg.name) },
                                colors = ButtonDefaults.buttonColors(containerColor = AueraaSurfaceVariant),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().height(36.dp)
                            ) {
                                Text("Select & Request Quote", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }

            // Case Studies / Portfolio
            if (categoryPortfolio.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Project Work in ${currentService.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(categoryPortfolio) { item ->
                    PortfolioItemCard(project = item)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
