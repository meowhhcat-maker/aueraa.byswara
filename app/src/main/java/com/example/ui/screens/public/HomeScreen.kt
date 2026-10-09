package com.example.ui.screens.public

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgencySettings
import com.example.data.model.ComboPackageEntity
import com.example.data.model.PackageEntity
import com.example.data.model.PortfolioEntity
import com.example.data.model.ServiceEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    settings: AgencySettings?,
    services: List<ServiceEntity>,
    featuredPackages: List<PackageEntity>,
    featuredCombos: List<ComboPackageEntity>,
    featuredPortfolio: List<PortfolioEntity>,
    onNavigateToServices: (String?) -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToCombos: () -> Unit,
    onNavigateToPortfolio: () -> Unit,
    onNavigateToEnquiry: (packageName: String?) -> Unit,
    onNavigateToSetupWizard: () -> Unit,
    onSwitchToAdmin: () -> Unit,
    onSwitchToClient: () -> Unit,
    onNavigateToAnalytics: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top Brand Header & Quick Role Switches
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AueraaBrandBanner(height = 42.dp)

                    // Quick Switch Pill
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilledTonalButton(
                            onClick = onSwitchToClient,
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = AueraaSurfaceVariant,
                                contentColor = AueraaNeonCyan
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("quick_client_portal_btn")
                        ) {
                            Text("Client", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onSwitchToAdmin,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AueraaRoyalPurple,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("quick_admin_portal_btn")
                        ) {
                            Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Hero Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                AueraaSurfaceDark,
                                Color(0xFF0A1020),
                                AueraaDarkBackground
                            )
                        )
                    )
                    .border(1.dp, AueraaElectricBlue.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AueraaElectricBlue.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PREMIUM DIGITAL AGENCY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AueraaNeonCyan,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Creative Content.\nStrategic Marketing.\nPowerful Digital Solutions.",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = settings?.introduction
                            ?: "We build scalable digital empires for ambitious brands through content, ads, websites, and mobile applications.",
                        fontSize = 13.sp,
                        color = AueraaTextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onNavigateToEnquiry(null) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AueraaElectricBlue,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("hero_get_quote_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Get Proposal", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        OutlinedButton(
                            onClick = onNavigateToPackages,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AueraaTextPrimary),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue))),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f).testTag("hero_view_packages_btn")
                        ) {
                            Text("View Packages", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Four Core Service Pillars
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                SectionTitle(
                    title = "Core Capabilities",
                    subtitle = "Four specialized departments driving end-to-end growth",
                    accentPill = "SPECIALIZATIONS"
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                services.forEach { service ->
                    ServicePillarCard(
                        service = service,
                        onClick = { onNavigateToServices(service.categoryKey) }
                    )
                }
            }
        }

        // Featured Turnkey Combos Banner
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                AueraaRoyalPurple.copy(alpha = 0.35f),
                                AueraaSurfaceDark,
                                AueraaElectricBlue.copy(alpha = 0.25f)
                            )
                        )
                    )
                    .border(1.dp, AueraaRoyalPurple.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable { onNavigateToCombos() }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = AueraaNeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "COMBO PACKAGES BUILDER",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AueraaNeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Combine Services & Save up to 22%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Bundled solutions for Startups, Restaurants, Resorts & D2C",
                            fontSize = 12.sp,
                            color = AueraaTextSecondary
                        )
                    }
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Open Combos",
                        tint = AueraaNeonCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Live Recharts Campaign Performance Analytics Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AueraaSurfaceDark)
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(AueraaElectricBlue, AueraaNeonCyan)),
                        RoundedCornerShape(16.dp)
                    )
                    .clickable { onNavigateToAnalytics() }
                    .padding(16.dp)
                    .testTag("home_campaign_analytics_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Analytics,
                                contentDescription = null,
                                tint = AueraaNeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "LIVE RECHARTS ANALYTICS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AueraaNeonCyan
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Marketing Campaign Performance & ROAS",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Conversion rate curves, ad spend breakdown & multi-channel ROI",
                            fontSize = 12.sp,
                            color = AueraaTextSecondary
                        )
                    }
                    IconButton(
                        onClick = onNavigateToAnalytics,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AueraaElectricBlue)
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "View Recharts Analytics",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Popular Packages
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(
                        title = "Featured Packages",
                        subtitle = "Transparent pricing with defined deliverables",
                        accentPill = "TRANSPARENT PRICING"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(featuredPackages.take(5)) { pkg ->
                    PackageCardCompact(
                        pkg = pkg,
                        onSelect = { onNavigateToEnquiry(pkg.name) }
                    )
                }
            }
        }

        // Portfolio Showcase
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionTitle(
                        title = "Recent Client Results",
                        subtitle = "Real business transformations and measurable impact",
                        accentPill = "CASE STUDIES"
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                featuredPortfolio.take(3).forEach { project ->
                    PortfolioItemCard(project = project)
                }
            }
        }

        // Bottom CTA & Contact info
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(AueraaSurfaceDark)
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AueraaLogoMark(size = 44.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Ready to Scale Your Digital Presence?",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Direct consultation with our creative & technical directors.",
                        fontSize = 12.sp,
                        color = AueraaTextSecondary,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { onNavigateToEnquiry(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Your Project Now", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Phone: ${settings?.phone ?: "+91 98765 43210"}  •  Email: ${settings?.businessEmail ?: "contact@aueraa.digital"}",
                        fontSize = 11.sp,
                        color = AueraaTextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun ServicePillarCard(
    service: ServiceEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("service_pillar_${service.categoryKey.lowercase()}"),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (service.iconKey) {
                "content" -> Icons.Default.VideoCameraBack
                "marketing" -> Icons.Default.TrendingUp
                "website" -> Icons.Default.Language
                else -> Icons.Default.Smartphone
            }
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(AueraaSurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AueraaElectricBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = service.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(status = service.badgeText)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = service.shortDescription,
                    fontSize = 12.sp,
                    color = AueraaTextSecondary,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Starts from ",
                        fontSize = 11.sp,
                        color = AueraaTextMuted
                    )
                    CurrencyText(amount = service.startingPriceInr, fontSize = 13, color = AueraaNeonCyan)
                }
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = AueraaTextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PackageCardCompact(
    pkg: PackageEntity,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .testTag("package_card_${pkg.id}"),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.4f)))),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = pkg.category)
                Text(
                    text = pkg.packageType,
                    fontSize = 11.sp,
                    color = AueraaTextMuted
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pkg.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pkg.quantitiesSummary,
                fontSize = 11.sp,
                color = AueraaNeonCyan,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(10.dp))
            CurrencyText(
                amount = pkg.priceInr,
                isCustomQuote = pkg.isCustomQuoteRequired,
                frequency = pkg.billingFrequency,
                fontSize = 17,
                color = AueraaElectricBlue
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(containerColor = AueraaSurfaceVariant, contentColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(36.dp)
            ) {
                Text("Select Package", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PortfolioItemCard(
    project: PortfolioEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = project.category)
                Text(
                    text = project.clientName,
                    fontSize = 11.sp,
                    color = AueraaTextMuted
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = project.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.description,
                fontSize = 12.sp,
                color = AueraaTextSecondary,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(AueraaSuccessEmerald.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = AueraaSuccessEmerald,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = project.metricsResult,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AueraaSuccessEmerald
                    )
                }
            }
            if (!project.beforeAfterSummary.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = project.beforeAfterSummary,
                    fontSize = 11.sp,
                    color = AueraaNeonCyan,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
