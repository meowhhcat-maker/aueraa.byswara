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
import com.example.ui.components.*
import com.example.ui.screens.admin.PackageEditorDialog
import com.example.ui.theme.*

@Composable
fun PackagesScreen(
    packages: List<PackageEntity>,
    isAdmin: Boolean = false,
    onSavePackage: ((PackageEntity) -> Unit)? = null,
    onSelectPackageForEnquiry: (packageName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Content Creation", "Digital Marketing", "Website Development", "Application Development")
    var selectedCategory by remember { mutableStateOf("All") }
    var viewingPackageDetails by remember { mutableStateOf<PackageEntity?>(null) }
    var editingPackage by remember { mutableStateOf<PackageEntity?>(null) }

    val filteredPackages = packages.filter {
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
                title = "Service Packages",
                subtitle = "Fixed-scope deliverables with transparent pricing & guaranteed milestones",
                accentPill = "TRANSPARENT PRICING"
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Category Filter Chips
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
            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredPackages.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Inventory2, contentDescription = null, tint = AueraaTextMuted, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("No packages available in this category yet.", color = AueraaTextSecondary, fontSize = 14.sp)
                    }
                }
            }
        } else {
            items(filteredPackages) { pkg ->
                PackageDetailCard(
                    pkg = pkg,
                    onViewDetails = { viewingPackageDetails = pkg },
                    onSelect = { onSelectPackageForEnquiry(pkg.name) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }

    // Modal Sheet / Dialog for Full Package Specifications
    viewingPackageDetails?.let { pkg ->
        PackageSpecsDialog(
            pkg = pkg,
            isAdmin = isAdmin,
            onEdit = {
                viewingPackageDetails = null
                editingPackage = pkg
            },
            onDismiss = { viewingPackageDetails = null },
            onSelect = {
                viewingPackageDetails = null
                onSelectPackageForEnquiry(pkg.name)
            }
        )
    }

    editingPackage?.let { pkgToEdit ->
        PackageEditorDialog(
            packageToEdit = pkgToEdit,
            isNewPackage = false,
            onDismiss = { editingPackage = null },
            onSave = { updated ->
                onSavePackage?.invoke(updated)
                editingPackage = null
            }
        )
    }
}

@Composable
fun PackageDetailCard(
    pkg: PackageEntity,
    onViewDetails: () -> Unit,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("package_item_${pkg.id}"),
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.35f)))),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = pkg.category)
                Text(
                    text = "ID: ${pkg.id}",
                    fontSize = 11.sp,
                    color = AueraaTextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pkg.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pkg.description,
                fontSize = 13.sp,
                color = AueraaTextSecondary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Quantities & Timeline Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AueraaSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "📦 ${pkg.quantitiesSummary}",
                        fontSize = 11.sp,
                        color = AueraaNeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AueraaSurfaceVariant)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "⏱ ${pkg.deliveryDuration}",
                        fontSize = 11.sp,
                        color = AueraaTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = AueraaSurfaceBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Pricing & Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (pkg.billingFrequency == "Monthly") "Monthly Retainer" else "One-Time Investment",
                        fontSize = 11.sp,
                        color = AueraaTextMuted
                    )
                    CurrencyText(
                        amount = pkg.priceInr,
                        isCustomQuote = pkg.isCustomQuoteRequired,
                        frequency = if (pkg.billingFrequency == "Monthly") "mo" else null,
                        fontSize = 20,
                        color = AueraaElectricBlue
                    )
                    if (pkg.priceInr > 0) {
                        Text(
                            text = "+ 18% GST (₹${(pkg.priceInr * 0.18).toLong()})",
                            fontSize = 10.sp,
                            color = AueraaTextMuted
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onViewDetails,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AueraaTextPrimary),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Specs", fontSize = 12.sp)
                    }
                    Button(
                        onClick = onSelect,
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Enquire", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PackageSpecsDialog(
    pkg: PackageEntity,
    isAdmin: Boolean = false,
    onEdit: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    onSelect: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AueraaSurfaceDark,
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(status = pkg.category)
                    Text("ID: ${pkg.id}", fontSize = 11.sp, color = AueraaTextMuted)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = pkg.name,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(text = pkg.description, fontSize = 13.sp, color = AueraaTextSecondary)
                }
                item {
                    SpecsSectionTitle("Deliverables Included")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            pkg.deliverables.split("\n").filter { it.isNotBlank() }.forEach {
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text("✓ ", color = AueraaElectricBlue, fontWeight = FontWeight.Bold)
                                    Text(it, fontSize = 12.sp, color = AueraaTextPrimary)
                                }
                            }
                        }
                    }
                }
                item {
                    SpecsSectionTitle("Target Audience & Goal")
                    Text("Target: ${pkg.targetCustomer}", fontSize = 12.sp, color = AueraaTextSecondary)
                    Text("Goal: ${pkg.mainGoal}", fontSize = 12.sp, color = AueraaNeonCyan)
                }
                item {
                    SpecsSectionTitle("Revisions & Support")
                    Text("Revisions: ${pkg.revisionsPolicy}", fontSize = 12.sp, color = AueraaTextSecondary)
                    Text("Support: ${pkg.supportPeriod}", fontSize = 12.sp, color = AueraaTextSecondary)
                }
                item {
                    SpecsSectionTitle("Terms & Exclusions")
                    Text("Payment Schedule: ${pkg.paymentSchedule}", fontSize = 12.sp, color = AueraaTextSecondary)
                    Text("Exclusions: ${pkg.excludedServices}", fontSize = 12.sp, color = AueraaErrorRose.copy(alpha = 0.8f))
                }
                if (pkg.recommendedAddons.isNotBlank()) {
                    item {
                        SpecsSectionTitle("Recommended Add-ons")
                        Text(pkg.recommendedAddons, fontSize = 12.sp, color = AueraaAccentAmber)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Base Price:", fontSize = 12.sp, color = AueraaTextSecondary)
                                Text("₹${pkg.priceInr}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("GST (18%):", fontSize = 12.sp, color = AueraaTextSecondary)
                                Text("₹${(pkg.priceInr * 0.18).toLong()}", fontSize = 12.sp, color = AueraaTextSecondary)
                            }
                            HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("₹${(pkg.priceInr * 1.18).toLong()}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isAdmin && onEdit != null) {
                    FilledTonalButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = AueraaElectricBlue.copy(alpha = 0.2f), contentColor = AueraaNeonCyan)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit Details", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue)
                ) {
                    Text("Enquire for this Package", fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = AueraaTextSecondary)
            }
        }
    )
}

@Composable
private fun SpecsSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = AueraaNeonCyan,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}
