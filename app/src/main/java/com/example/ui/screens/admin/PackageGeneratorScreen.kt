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
import com.example.data.model.PackageEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun PackageGeneratorScreen(
    viewModel: AgencyViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val generatorState by viewModel.generatorState.collectAsState()

    var category by remember { mutableStateOf("Content Creation") }
    var packageName by remember { mutableStateOf("Viral Brand Dominator") }
    var targetCustomer by remember { mutableStateOf("Luxury Cafés, Boutique Resorts & D2C Brands") }
    var clientBusinessCategory by remember { mutableStateOf("Hospitality & Lifestyle") }
    var mainGoal by remember { mutableStateOf("Dominate Instagram Reels reach & build high-ticket brand prestige") }
    var expectedBudgetInr by remember { mutableStateOf("39999") }
    var billingFrequency by remember { mutableStateOf("Monthly") }
    var includedServices by remember { mutableStateOf("4K Reel Shoots, Motion Graphics, Social Post Designs, Captions & Hashtags") }
    var deliverablesQuantities by remember { mutableStateOf("12 High-Converting Posts, 6 Cinematic Reels, 16 Stories") }
    var deliveryTimeline by remember { mutableStateOf("Monthly Retainer (Bi-Weekly Deliveries)") }
    var revisionsAndSupport by remember { mutableStateOf("3 Iteration rounds per creative, 30-Day support warranty") }
    var thirdPartyFees by remember { mutableStateOf("Direct ad spend paid to Meta/Google; shoot travel expenses separate") }
    var recommendedAddons by remember { mutableStateOf("Drone 4K aerial pack (+₹9,999), Celebrity Influencer Collab (+₹24,999)") }
    var specialConditions by remember { mutableStateOf("Client provides high-res vector logos and brand assets") }
    var packagePriceInr by remember { mutableStateOf("34999") }

    val categories = listOf("Content Creation", "Digital Marketing", "Website Development", "Application Development", "Combo Package")
    val frequencies = listOf("Monthly", "One-time", "Annual", "Custom")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                SectionTitle(
                    title = "AI Package Generator",
                    subtitle = "Guided 15-question proposal generator with strict review enforcement",
                    accentPill = "ADMIN INTELLIGENCE"
                )
            }
        }

        // Generated Draft Preview Box (if available)
        generatorState.generatedDraft?.let { draft ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaNeonCyan, AueraaElectricBlue))),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadge(status = "AI GENERATED DRAFT")
                            Text("Review Required Before Publishing", fontSize = 11.sp, color = AueraaAccentAmber)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(draft.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(draft.description, fontSize = 12.sp, color = AueraaTextSecondary)
                        Spacer(modifier = Modifier.height(10.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("• Target: ${draft.targetCustomer}", fontSize = 11.sp, color = AueraaTextPrimary)
                                Text("• Deliverables: ${draft.quantitiesSummary}", fontSize = 11.sp, color = AueraaNeonCyan)
                                Text("• Revisions & Warranty: ${draft.revisionsPolicy}", fontSize = 11.sp, color = AueraaTextSecondary)
                                Text("• Exclusions: ${draft.excludedServices}", fontSize = 11.sp, color = AueraaTextMuted)
                                Text("• Add-ons: ${draft.recommendedAddons}", fontSize = 11.sp, color = AueraaAccentAmber)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CurrencyText(amount = draft.priceInr, frequency = draft.billingFrequency, fontSize = 20, color = AueraaElectricBlue)

                            Button(
                                onClick = {
                                    viewModel.approveAndPublishGeneratedPackage(draft)
                                    onBack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = AueraaSuccessEmerald),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.testTag("approve_publish_draft_btn")
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Approve & Publish Live", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // The 15 Question Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Proposal Intelligence Questionnaire (15 Steps)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    // 1. Service Category
                    Text("1. Service Category:", fontSize = 12.sp, color = AueraaTextSecondary, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.take(3).forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat.take(12), fontSize = 11.sp) }
                            )
                        }
                    }

                    // 2. Package Name
                    OutlinedTextField(
                        value = packageName,
                        onValueChange = { packageName = it },
                        label = { Text("2. Package Name") },
                        modifier = Modifier.fillMaxWidth().testTag("generator_pkg_name"),
                        singleLine = true
                    )

                    // 3. Target Customer
                    OutlinedTextField(
                        value = targetCustomer,
                        onValueChange = { targetCustomer = it },
                        label = { Text("3. Who is this package designed for?") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 4. Business Category
                    OutlinedTextField(
                        value = clientBusinessCategory,
                        onValueChange = { clientBusinessCategory = it },
                        label = { Text("4. Client's Business Category") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 5. Main Goal
                    OutlinedTextField(
                        value = mainGoal,
                        onValueChange = { mainGoal = it },
                        label = { Text("5. Client's Main Goal / Business Objective") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 6 & 7 Budget & Billing
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = expectedBudgetInr,
                            onValueChange = { expectedBudgetInr = it },
                            label = { Text("6. Budget (INR)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = billingFrequency,
                            onValueChange = { billingFrequency = it },
                            label = { Text("7. Billing Type (Monthly/One-time)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    // 8. Included Services
                    OutlinedTextField(
                        value = includedServices,
                        onValueChange = { includedServices = it },
                        label = { Text("8. Services Included") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 9. Deliverables Quantities
                    OutlinedTextField(
                        value = deliverablesQuantities,
                        onValueChange = { deliverablesQuantities = it },
                        label = { Text("9. Exact Deliverable Quantities (posts/reels/pages)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 10. Delivery Timeline
                    OutlinedTextField(
                        value = deliveryTimeline,
                        onValueChange = { deliveryTimeline = it },
                        label = { Text("10. Delivery Timeline / Turnaround") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 11. Revisions & Support
                    OutlinedTextField(
                        value = revisionsAndSupport,
                        onValueChange = { revisionsAndSupport = it },
                        label = { Text("11. Included Revisions & Support Period") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 12. Exclusions & Third-Party Fees
                    OutlinedTextField(
                        value = thirdPartyFees,
                        onValueChange = { thirdPartyFees = it },
                        label = { Text("12. Exclusions (Ad spend, domain, hosting)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 13. Recommended Add-ons
                    OutlinedTextField(
                        value = recommendedAddons,
                        onValueChange = { recommendedAddons = it },
                        label = { Text("13. Recommended Add-ons Available") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 14. Special Conditions
                    OutlinedTextField(
                        value = specialConditions,
                        onValueChange = { specialConditions = it },
                        label = { Text("14. Special Terms / Client Dependencies") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // 15. Package Price
                    OutlinedTextField(
                        value = packagePriceInr,
                        onValueChange = { packagePriceInr = it },
                        label = { Text("15. Final Approved Base Price (INR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("generator_final_price"),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = {
                            viewModel.updateGeneratorField {
                                copy(
                                    category = category,
                                    packageName = packageName,
                                    targetCustomer = targetCustomer,
                                    clientBusinessCategory = clientBusinessCategory,
                                    mainGoal = mainGoal,
                                    expectedBudgetInr = expectedBudgetInr.toLongOrNull() ?: 30000L,
                                    billingFrequency = billingFrequency,
                                    includedServices = includedServices.split(",").map { it.trim() },
                                    deliverablesQuantities = deliverablesQuantities,
                                    deliveryTimeline = deliveryTimeline,
                                    revisionsAndSupport = revisionsAndSupport,
                                    thirdPartyFeeConditions = thirdPartyFees,
                                    recommendedAddons = recommendedAddons,
                                    specialConditions = specialConditions,
                                    finalPriceInr = packagePriceInr.toLongOrNull() ?: 29999L
                                )
                            }
                            viewModel.generatePackageDraft()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("generate_package_draft_btn")
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Synthesize Package Proposal Draft", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
