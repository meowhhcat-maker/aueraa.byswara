package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.PackageEntity
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageEditorDialog(
    packageToEdit: PackageEntity?,
    isNewPackage: Boolean = false,
    onDismiss: () -> Unit,
    onSave: (PackageEntity) -> Unit
) {
    val initial = packageToEdit ?: PackageEntity(
        id = "PKG-${System.currentTimeMillis() % 100000}",
        name = "New Custom Package",
        category = "Content Creation",
        targetCustomer = "Growing Brands & Businesses",
        clientBusinessCategory = "All Industries",
        mainGoal = "Brand Visibility & Conversions",
        packageType = "Monthly",
        description = "Comprehensive custom package deliverables tailored for maximum business impact.",
        deliverables = "High-Converting Creative Designs\nReels Video Editing\nCaptions & Strategic Hashtags\nMonthly Performance Review",
        quantitiesSummary = "12 Posts, 4 Reels, 8 Stories",
        priceInr = 29999,
        isCustomQuoteRequired = false,
        currency = "INR",
        billingFrequency = "Monthly",
        taxPercentage = 18.0,
        deliveryDuration = "Monthly Retainer",
        paymentSchedule = "50% Advance, 50% on Delivery",
        revisionsPolicy = "3 rounds of structured revisions",
        supportPeriod = "30 Days post-delivery support",
        includedServices = "Strategy\nCreative Production\nRevisions",
        excludedServices = "Paid ad spend billed to client card",
        recommendedAddons = "On-site 4K Shoot Day: ₹14,999",
        status = "ACTIVE"
    )

    var name by remember(packageToEdit) { mutableStateOf(initial.name) }
    var category by remember(packageToEdit) { mutableStateOf(initial.category) }
    var priceText by remember(packageToEdit) { mutableStateOf(initial.priceInr.toString()) }
    var isCustomQuote by remember(packageToEdit) { mutableStateOf(initial.isCustomQuoteRequired) }
    var billingFrequency by remember(packageToEdit) { mutableStateOf(initial.billingFrequency) }
    var deliveryDuration by remember(packageToEdit) { mutableStateOf(initial.deliveryDuration) }
    var quantitiesSummary by remember(packageToEdit) { mutableStateOf(initial.quantitiesSummary) }
    var description by remember(packageToEdit) { mutableStateOf(initial.description) }
    var deliverables by remember(packageToEdit) { mutableStateOf(initial.deliverables) }
    var includedServices by remember(packageToEdit) { mutableStateOf(initial.includedServices) }
    var excludedServices by remember(packageToEdit) { mutableStateOf(initial.excludedServices) }
    var recommendedAddons by remember(packageToEdit) { mutableStateOf(initial.recommendedAddons) }
    var targetCustomer by remember(packageToEdit) { mutableStateOf(initial.targetCustomer) }
    var clientBusinessCategory by remember(packageToEdit) { mutableStateOf(initial.clientBusinessCategory) }
    var mainGoal by remember(packageToEdit) { mutableStateOf(initial.mainGoal) }
    var revisionsPolicy by remember(packageToEdit) { mutableStateOf(initial.revisionsPolicy) }
    var supportPeriod by remember(packageToEdit) { mutableStateOf(initial.supportPeriod) }
    var paymentSchedule by remember(packageToEdit) { mutableStateOf(initial.paymentSchedule) }
    var status by remember(packageToEdit) { mutableStateOf(initial.status) }

    val categories = listOf("Content Creation", "Digital Marketing", "Website Development", "Application Development", "Combo Package")
    val frequencies = listOf("Monthly", "One-time", "Quarterly", "Annual", "Custom")
    val statuses = listOf("ACTIVE", "DRAFT", "ARCHIVED")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, AueraaElectricBlue.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            color = AueraaSurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isNewPackage) Icons.Default.AddBox else Icons.Default.Edit,
                                contentDescription = null,
                                tint = AueraaElectricBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isNewPackage) "Create New Package" else "Edit Package Details",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Package ID: ${initial.id}",
                            fontSize = 11.sp,
                            color = AueraaTextMuted
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = AueraaTextSecondary)
                    }
                }

                HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Form
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Selection
                    item {
                        Text("Category", fontSize = 12.sp, color = AueraaNeonCyan, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.take(3).forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat.take(13), fontSize = 11.sp) }
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.drop(3).forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    // Package Name
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Package Name") },
                            modifier = Modifier.fillMaxWidth().testTag("edit_pkg_name"),
                            singleLine = true
                        )
                    }

                    // Pricing & Custom Quote
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                label = { Text("Price (INR ₹)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                enabled = !isCustomQuote,
                                modifier = Modifier.weight(1f).testTag("edit_pkg_price"),
                                singleLine = true
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Custom Quote", fontSize = 10.sp, color = AueraaTextSecondary)
                                Switch(
                                    checked = isCustomQuote,
                                    onCheckedChange = { isCustomQuote = it }
                                )
                            }
                        }
                    }

                    // Billing Frequency
                    item {
                        Text("Billing Frequency", fontSize = 12.sp, color = AueraaNeonCyan, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            frequencies.forEach { freq ->
                                FilterChip(
                                    selected = billingFrequency == freq,
                                    onClick = { billingFrequency = freq },
                                    label = { Text(freq, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    // Deliverables Quantities Summary & Delivery Duration
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = quantitiesSummary,
                                onValueChange = { quantitiesSummary = it },
                                label = { Text("Quantities Summary") },
                                placeholder = { Text("e.g. 15 Posts, 6 Reels") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = deliveryDuration,
                                onValueChange = { deliveryDuration = it },
                                label = { Text("Delivery Duration") },
                                placeholder = { Text("e.g. 14 Days / Monthly") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    // Short Description
                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Package Overview Description") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }

                    // Detailed Deliverables (Line-by-line)
                    item {
                        OutlinedTextField(
                            value = deliverables,
                            onValueChange = { deliverables = it },
                            label = { Text("Included Deliverables (One per line)") },
                            modifier = Modifier.fillMaxWidth().testTag("edit_pkg_deliverables"),
                            minLines = 4
                        )
                    }

                    // Included & Excluded Services
                    item {
                        OutlinedTextField(
                            value = includedServices,
                            onValueChange = { includedServices = it },
                            label = { Text("Included Services") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = excludedServices,
                            onValueChange = { excludedServices = it },
                            label = { Text("Excluded Services / Special Terms") },
                            placeholder = { Text("e.g. Ad spend billed directly to client") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }

                    // Recommended Addons
                    item {
                        OutlinedTextField(
                            value = recommendedAddons,
                            onValueChange = { recommendedAddons = it },
                            label = { Text("Recommended Add-ons & Extras") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )
                    }

                    // Target Audience & Goals
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = targetCustomer,
                                onValueChange = { targetCustomer = it },
                                label = { Text("Target Client") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = clientBusinessCategory,
                                onValueChange = { clientBusinessCategory = it },
                                label = { Text("Industry Category") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = mainGoal,
                            onValueChange = { mainGoal = it },
                            label = { Text("Client Business Objective") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // Revisions, Support & Payment Schedule
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = revisionsPolicy,
                                onValueChange = { revisionsPolicy = it },
                                label = { Text("Revisions Policy") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = supportPeriod,
                                onValueChange = { supportPeriod = it },
                                label = { Text("Support Warranty") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = paymentSchedule,
                            onValueChange = { paymentSchedule = it },
                            label = { Text("Payment Schedule") },
                            placeholder = { Text("e.g. 50% Advance, 50% on Delivery") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // Status
                    item {
                        Text("Package Status", fontSize = 12.sp, color = AueraaNeonCyan, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            statuses.forEach { st ->
                                FilterChip(
                                    selected = status == st,
                                    onClick = { status = st },
                                    label = { Text(st, fontSize = 11.sp) }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = AueraaTextSecondary)
                    }

                    Button(
                        onClick = {
                            val parsedPrice = if (isCustomQuote) 0L else (priceText.toLongOrNull() ?: 0L)
                            val updated = initial.copy(
                                name = name.trim(),
                                category = category,
                                priceInr = parsedPrice,
                                isCustomQuoteRequired = isCustomQuote || parsedPrice <= 0,
                                billingFrequency = billingFrequency,
                                deliveryDuration = deliveryDuration.trim(),
                                quantitiesSummary = quantitiesSummary.trim(),
                                description = description.trim(),
                                deliverables = deliverables.trim(),
                                includedServices = includedServices.trim(),
                                excludedServices = excludedServices.trim(),
                                recommendedAddons = recommendedAddons.trim(),
                                targetCustomer = targetCustomer.trim(),
                                clientBusinessCategory = clientBusinessCategory.trim(),
                                mainGoal = mainGoal.trim(),
                                revisionsPolicy = revisionsPolicy.trim(),
                                supportPeriod = supportPeriod.trim(),
                                paymentSchedule = paymentSchedule.trim(),
                                status = status,
                                updatedTimestamp = System.currentTimeMillis()
                            )
                            onSave(updated)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        modifier = Modifier.weight(1f).testTag("save_package_details_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Package", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
