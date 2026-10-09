package com.example.ui.screens.public

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComboPackageEntity
import com.example.data.model.PackageEntity
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ComboPackagesScreen(
    combos: List<ComboPackageEntity>,
    availablePackages: List<PackageEntity>,
    onRequestComboQuote: (comboName: String, budgetInr: Long, description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableIntStateOf(0) } // 0 = Turnkey Bundles, 1 = Custom Combo Builder

    // Custom combo builder interactive state
    val selectedPackageIds = remember { mutableStateListOf<String>() }
    // Initialize with 2 packages for immediate demonstration if available
    LaunchedEffect(availablePackages) {
        if (selectedPackageIds.isEmpty() && availablePackages.size >= 2) {
            selectedPackageIds.add(availablePackages[0].id)
            selectedPackageIds.add(availablePackages[1].id)
        }
    }

    // Admin configured combo discount for custom builder (e.g. 15% if 2 or more packages)
    val customDiscountPercent = if (selectedPackageIds.size >= 2) 15.0 else 0.0

    val selectedPackages = availablePackages.filter { it.id in selectedPackageIds }
    val originalTotal = selectedPackages.sumOf { it.priceInr }
    val discountAmount = (originalTotal * (customDiscountPercent / 100.0)).toLong()
    val finalTotal = originalTotal - discountAmount

    val hasMonthly = selectedPackages.any { it.billingFrequency == "Monthly" }
    val hasOneTime = selectedPackages.any { it.billingFrequency == "One-time" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        item {
            SectionTitle(
                title = "Combo Packages",
                subtitle = "Multidisciplinary synergies combining Content, Ads, Web & Mobile",
                accentPill = "SAVE UP TO 22%"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Mode Switcher Tab
        item {
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = AueraaSurfaceDark,
                contentColor = AueraaElectricBlue,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = {
                        Text(
                            "Turnkey Bundles",
                            fontSize = 13.sp,
                            fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (activeTab == 0) AueraaNeonCyan else AueraaTextSecondary
                        )
                    }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(14.dp), tint = if (activeTab == 1) AueraaElectricBlue else AueraaTextSecondary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Custom Combo Builder",
                                fontSize = 13.sp,
                                fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (activeTab == 1) AueraaNeonCyan else AueraaTextSecondary
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (activeTab == 0) {
            // Turnkey Bundles List
            items(combos) { combo ->
                ComboCard(
                    combo = combo,
                    onSelect = {
                        onRequestComboQuote(
                            combo.name,
                            combo.finalPriceInr,
                            "Requesting quote for ${combo.name} (${combo.subtitle})"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        } else {
            // Interactive Custom Combo Builder
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaRoyalPurple.copy(alpha = 0.5f), AueraaElectricBlue.copy(alpha = 0.3f)))),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Select Services to Bundle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Combine 2 or more standalone packages to unlock an automatic 15% agency combo bundle discount.",
                            fontSize = 12.sp,
                            color = AueraaTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Selection Checkboxes
                        availablePackages.forEach { pkg ->
                            val isChecked = pkg.id in selectedPackageIds
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChecked) AueraaSurfaceVariant else Color.Transparent)
                                    .clickable {
                                        if (isChecked) selectedPackageIds.remove(pkg.id)
                                        else selectedPackageIds.add(pkg.id)
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { check ->
                                        if (check) selectedPackageIds.add(pkg.id)
                                        else selectedPackageIds.remove(pkg.id)
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = AueraaElectricBlue,
                                        uncheckedColor = AueraaTextMuted
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = pkg.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isChecked) Color.White else AueraaTextPrimary
                                    )
                                    Text(
                                        text = "${pkg.category} • ${pkg.billingFrequency}",
                                        fontSize = 11.sp,
                                        color = AueraaTextMuted
                                    )
                                }
                                CurrencyText(
                                    amount = pkg.priceInr,
                                    isCustomQuote = pkg.isCustomQuoteRequired,
                                    fontSize = 14,
                                    color = if (isChecked) AueraaNeonCyan else AueraaTextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = AueraaSurfaceBorder)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Price Computation Box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Selected Packages:", fontSize = 12.sp, color = AueraaTextSecondary)
                                    Text("${selectedPackages.size} packages", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Standalone Total:", fontSize = 12.sp, color = AueraaTextSecondary)
                                    Text("₹$originalTotal", fontSize = 13.sp, color = Color.White)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Admin Combo Discount (${customDiscountPercent.toInt()}%):", fontSize = 12.sp, color = AueraaSuccessEmerald)
                                    Text("- ₹$discountAmount", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AueraaSuccessEmerald)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                HorizontalDivider(color = AueraaSurfaceBorder)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Combined Net Total:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        if (hasMonthly && hasOneTime) {
                                            Text("(Includes Monthly Retainer + One-time)", fontSize = 10.sp, color = AueraaTextMuted)
                                        }
                                    }
                                    Text(
                                        "₹$finalTotal",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AueraaNeonCyan
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val bundleTitle = "Custom Combo (${selectedPackages.map { it.name }.joinToString(" + ")})"
                                onRequestComboQuote(
                                    bundleTitle,
                                    finalTotal,
                                    "Custom bundle composed of:\n" + selectedPackages.joinToString("\n") { "• ${it.name} (₹${it.priceInr})" }
                                )
                            },
                            enabled = selectedPackages.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("custom_combo_submit_btn")
                        ) {
                            Text("Request Custom Combo Proposal", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComboCard(
    combo: ComboPackageEntity,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("combo_card_${combo.id}"),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaRoyalPurple.copy(alpha = 0.5f), AueraaElectricBlue.copy(alpha = 0.3f)))),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AueraaRoyalPurple.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "${combo.discountPercentage.toInt()}% COMBO DISCOUNT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AueraaNeonCyan,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = combo.billingFrequency,
                    fontSize = 11.sp,
                    color = AueraaTextMuted
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = combo.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = combo.subtitle,
                fontSize = 12.sp,
                color = AueraaNeonCyan,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = combo.description,
                fontSize = 12.sp,
                color = AueraaTextSecondary,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Included deliverables box
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceVariant),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    combo.includedDeliverables.split("\n").filter { it.isNotBlank() }.forEach { deliv ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("✓ ", color = AueraaElectricBlue, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(text = deliv, fontSize = 11.sp, color = AueraaTextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = AueraaSurfaceBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Pricing comparison row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${combo.originalTotalInr}",
                            fontSize = 12.sp,
                            color = AueraaTextMuted,
                            textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Save ₹${combo.originalTotalInr - combo.finalPriceInr}",
                            fontSize = 11.sp,
                            color = AueraaSuccessEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    CurrencyText(
                        amount = combo.finalPriceInr,
                        frequency = if (combo.billingFrequency.contains("Monthly")) "mo" else null,
                        fontSize = 20,
                        color = AueraaElectricBlue
                    )
                }
                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Select Combo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
