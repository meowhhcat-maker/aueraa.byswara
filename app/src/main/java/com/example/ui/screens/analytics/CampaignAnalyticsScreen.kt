package com.example.ui.screens.analytics

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MarketingCampaignEntity
import com.example.data.model.MetricDataPoint
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun CampaignAnalyticsScreen(
    viewModel: AgencyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val campaigns by viewModel.campaigns.collectAsState()
    val context = LocalContext.current

    var selectedTimeframe by remember { mutableIntStateOf(0) } // 0=7D, 1=30D, 2=90D, 3=YTD
    var selectedPlatformFilter by remember { mutableStateOf("ALL") }

    // Dynamic Timeframe Data points for interactive Recharts
    val timeSeriesData = remember(selectedTimeframe) {
        when (selectedTimeframe) {
            0 -> listOf(
                MetricDataPoint("Mon", "06 Oct", 3.82, 14500, 68150, 4.7, 42000, 1850, 71),
                MetricDataPoint("Tue", "07 Oct", 4.10, 16200, 79380, 4.9, 48000, 2100, 86),
                MetricDataPoint("Wed", "08 Oct", 3.95, 15000, 69000, 4.6, 45000, 1920, 76),
                MetricDataPoint("Thu", "09 Oct", 4.65, 17800, 89000, 5.0, 52000, 2350, 109),
                MetricDataPoint("Fri", "10 Oct", 5.20, 21000, 115500, 5.5, 62000, 2800, 146),
                MetricDataPoint("Sat", "11 Oct", 5.65, 24500, 142100, 5.8, 74000, 3350, 189),
                MetricDataPoint("Sun", "12 Oct", 4.88, 19500, 101400, 5.2, 58000, 2550, 124)
            )
            1 -> listOf(
                MetricDataPoint("W1", "Week 1", 3.65, 84000, 369600, 4.4, 240000, 10500, 383),
                MetricDataPoint("W2", "Week 2", 4.12, 98000, 460600, 4.7, 290000, 12800, 527),
                MetricDataPoint("W3", "Week 3", 4.85, 115000, 586500, 5.1, 340000, 15400, 747),
                MetricDataPoint("W4", "Week 4", 5.34, 128000, 704000, 5.5, 390000, 17900, 956)
            )
            2 -> listOf(
                MetricDataPoint("M1", "Aug", 3.40, 280000, 1176000, 4.2, 850000, 38000, 1292),
                MetricDataPoint("M2", "Sep", 4.25, 340000, 1632000, 4.8, 1050000, 47000, 1998),
                MetricDataPoint("M3", "Oct", 4.98, 410000, 2173000, 5.3, 1280000, 58000, 2888)
            )
            else -> listOf(
                MetricDataPoint("Q1", "Q1", 3.10, 620000, 2480000, 4.0, 1900000, 84000, 2604),
                MetricDataPoint("Q2", "Q2", 3.85, 780000, 3510000, 4.5, 2400000, 110000, 4235),
                MetricDataPoint("Q3", "Q3", 4.60, 950000, 4655000, 4.9, 3100000, 142000, 6532),
                MetricDataPoint("Q4", "Q4 TD", 5.15, 520000, 2808000, 5.4, 1680000, 78000, 4017)
            )
        }
    }

    val filteredCampaigns = remember(campaigns, selectedPlatformFilter) {
        if (selectedPlatformFilter == "ALL") campaigns
        else campaigns.filter { it.platform == selectedPlatformFilter }
    }

    val totalSpend = remember(filteredCampaigns) { filteredCampaigns.sumOf { it.adSpendInr } }
    val totalRevenue = remember(filteredCampaigns) { filteredCampaigns.sumOf { it.revenueGeneratedInr } }
    val avgRoas = remember(totalSpend, totalRevenue) {
        if (totalSpend > 0) String.format(java.util.Locale.US, "%.2fx", totalRevenue.toDouble() / totalSpend)
        else "4.64x"
    }
    val avgConversionRate = remember(filteredCampaigns) {
        if (filteredCampaigns.isNotEmpty()) {
            String.format(java.util.Locale.US, "%.2f%%", filteredCampaigns.map { it.conversionRate }.average())
        } else "4.82%"
    }
    val totalConversions = remember(filteredCampaigns) { filteredCampaigns.sumOf { it.totalConversions } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground)
            .testTag("campaign_analytics_screen"),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AueraaSurfaceDark)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Marketing Performance",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(AueraaNeonCyan.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("RECHARTS LIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)
                            }
                        }
                        Text(
                            text = "Conversion rate & ad spend intelligence dashboard",
                            fontSize = 11.sp,
                            color = AueraaTextSecondary
                        )
                    }
                }

                FilledTonalButton(
                    onClick = {
                        Toast.makeText(
                            context,
                            "Performance Report exported as PDF & CSV! Sent to client & admin emails.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = AueraaSurfaceVariant),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export", fontSize = 11.sp)
                }
            }
        }

        // Timeframe Selector Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(AueraaSurfaceDark)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val timeframes = listOf("7 Days", "30 Days", "90 Days", "Year to Date")
                timeframes.forEachIndexed { idx, label ->
                    val isSel = selectedTimeframe == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) AueraaElectricBlue else Color.Transparent)
                            .padding(vertical = 8.dp)
                            .testTag("timeframe_chip_$idx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSel) Color.White else AueraaTextSecondary
                        )
                    }
                }
            }
        }

        // Top 4 High-Impact KPI Metric Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricHighlightCard(
                        title = "TOTAL AD SPEND",
                        value = "₹${totalSpend / 1000}k",
                        subtitle = "Across ${filteredCampaigns.size} campaigns",
                        badge = "+14.2% vs last",
                        badgePositive = true,
                        icon = Icons.Default.MonetizationOn,
                        tint = AueraaElectricBlue,
                        modifier = Modifier.weight(1f)
                    )
                    MetricHighlightCard(
                        title = "TOTAL REVENUE",
                        value = "₹${totalRevenue / 1000}k",
                        subtitle = "Verified client orders",
                        badge = "+28.6% Growth",
                        badgePositive = true,
                        icon = Icons.Default.TrendingUp,
                        tint = AueraaSuccessEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricHighlightCard(
                        title = "CONVERSION RATE",
                        value = avgConversionRate,
                        subtitle = "$totalConversions Total Leads",
                        badge = "+18.4% CVR lift",
                        badgePositive = true,
                        icon = Icons.Default.AutoGraph,
                        tint = AueraaNeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MetricHighlightCard(
                        title = "AVERAGE ROAS",
                        value = avgRoas,
                        subtitle = "Target: 4.0x Return",
                        badge = "High Profitability",
                        badgePositive = true,
                        icon = Icons.Default.WorkspacePremium,
                        tint = AueraaRoyalPurple,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section 1: Conversion Rate Trend (Interactive Area Chart)
        item {
            SectionTitle(
                title = "Conversion Rate Analysis",
                subtitle = "Daily percentage of ad clicks that convert into paid client bookings",
                accentPill = "CONVERSION FUNNEL"
            )
            Spacer(modifier = Modifier.height(4.dp))
            RechartsConversionAreaChart(data = timeSeriesData)
        }

        // Section 2: Ad Spend vs Revenue (Grouped Bar Chart)
        item {
            SectionTitle(
                title = "Campaign Spend vs Return",
                subtitle = "Direct comparison of budget deployed against sales revenue delivered",
                accentPill = "FINANCIAL PERFORMANCE"
            )
            Spacer(modifier = Modifier.height(4.dp))
            RechartsAdSpendRevenueBarChart(data = timeSeriesData)
        }

        // Section 3: Channel Spend Breakdown (Donut Radial)
        item {
            RechartsChannelDonutChart()
        }

        // Section 4: Platform Filter & Campaign Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(
                    title = "Active Marketing Campaigns",
                    subtitle = "Real-time tracking of Meta, Google, LinkedIn & YouTube funnels",
                    accentPill = "CAMPAIGN AUDIT"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Platform Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val filters = listOf("ALL", "META_ADS", "GOOGLE_ADS", "LINKEDIN_ADS", "YOUTUBE")
                filters.forEach { filter ->
                    FilterChip(
                        selected = selectedPlatformFilter == filter,
                        onClick = { selectedPlatformFilter = filter },
                        label = {
                            Text(
                                text = when (filter) {
                                    "ALL" -> "All Channels"
                                    "META_ADS" -> "Meta"
                                    "GOOGLE_ADS" -> "Google"
                                    "LINKEDIN_ADS" -> "LinkedIn"
                                    else -> "YouTube"
                                },
                                fontSize = 11.sp
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(filteredCampaigns) { campaign ->
            CampaignPerformanceCard(campaign = campaign)
        }
    }
}

@Composable
fun MetricHighlightCard(
    title: String,
    value: String,
    subtitle: String,
    badge: String,
    badgePositive: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        modifier = modifier.border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AueraaTextSecondary
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(tint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (badgePositive) AueraaSuccessEmerald.copy(alpha = 0.18f) else AueraaErrorRose.copy(alpha = 0.18f))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (badgePositive) AueraaSuccessEmerald else AueraaErrorRose
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = AueraaTextMuted
            )
        }
    }
}

@Composable
fun CampaignPerformanceCard(campaign: MarketingCampaignEntity) {
    val platformColor = when (campaign.platform) {
        "META_ADS" -> Color(0xFF1877F2)
        "GOOGLE_ADS" -> Color(0xFFEA4335)
        "LINKEDIN_ADS" -> Color(0xFF0A66C2)
        else -> Color(0xFFFF0000)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, platformColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .testTag("campaign_card_${campaign.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Platform & Objective row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(platformColor.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = campaign.platform.replace("_", " "),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = platformColor
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(AueraaSurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = campaign.targetObjective,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AueraaNeonCyan
                        )
                    }
                }

                StatusBadge(status = campaign.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = campaign.campaignName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Client: ${campaign.clientName}  •  ${campaign.startDate} - ${campaign.endDate}",
                fontSize = 11.sp,
                color = AueraaTextSecondary
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics Grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AueraaSurfaceVariant.copy(alpha = 0.6f))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Ad Spend", fontSize = 10.sp, color = AueraaTextMuted)
                    Text("₹${campaign.adSpendInr}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AueraaElectricBlue)
                }
                Column {
                    Text("Revenue", fontSize = 10.sp, color = AueraaTextMuted)
                    Text("₹${campaign.revenueGeneratedInr}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AueraaSuccessEmerald)
                }
                Column {
                    Text("ROAS", fontSize = 10.sp, color = AueraaTextMuted)
                    Text("${campaign.roas}x", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = AueraaNeonCyan)
                }
                Column {
                    Text("Conversion Rate", fontSize = 10.sp, color = AueraaTextMuted)
                    Text("${campaign.conversionRate}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Delivery Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Impressions: ${campaign.totalImpressions / 1000}k  |  Clicks: ${campaign.totalClicks}",
                    fontSize = 11.sp,
                    color = AueraaTextMuted
                )
                Text(
                    text = "CPA: ₹${campaign.cpaInr}  |  CTR: ${campaign.ctr}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AueraaTextSecondary
                )
            }
        }
    }
}
