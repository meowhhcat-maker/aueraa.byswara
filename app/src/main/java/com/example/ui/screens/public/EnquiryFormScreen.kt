package com.example.ui.screens.public

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnquiryFormScreen(
    prefilledPackageName: String? = null,
    prefilledServiceCategory: String? = null,
    onSubmitEnquiry: (
        clientName: String,
        businessName: String,
        contactNumber: String,
        whatsappNumber: String,
        email: String,
        businessCategory: String,
        serviceCategory: String,
        selectedPackageName: String,
        isComboRequest: Boolean,
        expectedBudgetInr: Long,
        deadline: String,
        referenceLinks: String,
        projectDescription: String,
        additionalRequirements: String,
        onSuccess: (refId: String) -> Unit
    ) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var clientName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var businessCategory by remember { mutableStateOf("Food & Beverage / Restaurant") }
    var serviceCategory by remember(prefilledServiceCategory) {
        mutableStateOf(prefilledServiceCategory ?: "Content Creation")
    }
    var selectedPackageName by remember(prefilledPackageName) {
        mutableStateOf(prefilledPackageName ?: "Social Content Starter")
    }
    var isComboRequest by remember { mutableStateOf(prefilledPackageName?.contains("Combo", ignoreCase = true) == true) }
    var expectedBudgetInr by remember { mutableStateOf("35000") }
    var deadline by remember { mutableStateOf("Within 2 Weeks") }
    var referenceLinks by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var additionalRequirements by remember { mutableStateOf("") }

    var submittedRefId by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val serviceCategories = listOf("Content Creation", "Digital Marketing", "Website Development", "Application Development", "Combo Package")
    val businessCategories = listOf(
        "Small & Medium Business (SME)",
        "Startup / Tech Founder",
        "Restaurant, Café & Food Brand",
        "Resort, Villa & Hospitality",
        "E-Commerce & D2C Brand",
        "Educational Institution",
        "Corporate / Enterprise",
        "Personal Brand & Creator"
    )

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
                    title = "Request a Proposal",
                    subtitle = "Detailed project intake for accurate pricing & deliverables",
                    accentPill = "INQUIRY INTAKE"
                )
            }
        }

        if (submittedRefId != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                    border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.verticalGradient(listOf(AueraaSuccessEmerald, AueraaElectricBlue))),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = AueraaSuccessEmerald,
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Enquiry Submitted Successfully!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Reference ID: $submittedRefId",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AueraaNeonCyan
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Our senior agency team is reviewing your requirements. You can track this in your Client Portal or connect directly on WhatsApp with your reference number.",
                            fontSize = 13.sp,
                            color = AueraaTextSecondary,
                            lineHeight = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = {
                                openWhatsAppChat(
                                    context,
                                    "+919876543210",
                                    "Hi Aueraa.Digital! I just submitted an enquiry for $businessName (Ref: $submittedRefId). Please review the proposal details."
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Fast-Track on WhatsApp", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
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
                        Text("1. Client & Business Details", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("Your Name / Representative") },
                            modifier = Modifier.fillMaxWidth().testTag("enquiry_client_name"),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = businessName,
                            onValueChange = { businessName = it },
                            label = { Text("Business / Brand Name") },
                            modifier = Modifier.fillMaxWidth().testTag("enquiry_business_name"),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = contactNumber,
                                onValueChange = { contactNumber = it },
                                label = { Text("Phone Number") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f).testTag("enquiry_phone"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = whatsappNumber,
                                onValueChange = { whatsappNumber = it },
                                label = { Text("WhatsApp (with Code)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f).testTag("enquiry_whatsapp"),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Business Email Address") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth().testTag("enquiry_email"),
                            singleLine = true
                        )

                        Text("Business Industry / Category:", fontSize = 13.sp, color = AueraaTextSecondary, fontWeight = FontWeight.Medium)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            businessCategories.take(4).forEach { cat ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (businessCategory == cat) AueraaSurfaceVariant else Color.Transparent)
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = businessCategory == cat,
                                        onClick = { businessCategory = cat },
                                        colors = RadioButtonDefaults.colors(selectedColor = AueraaElectricBlue)
                                    )
                                    Text(cat, fontSize = 12.sp, color = if (businessCategory == cat) Color.White else AueraaTextSecondary)
                                }
                            }
                        }

                        HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                        Text("2. Required Services & Scope", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                        Text("Primary Service Category:", fontSize = 13.sp, color = AueraaTextSecondary)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            serviceCategories.take(3).forEach { cat ->
                                val isSel = serviceCategory == cat
                                FilterChip(
                                    selected = isSel,
                                    onClick = { serviceCategory = cat },
                                    label = { Text(cat.take(12), fontSize = 11.sp) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = selectedPackageName,
                            onValueChange = { selectedPackageName = it },
                            label = { Text("Selected Package or Custom Service") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isComboRequest,
                                onCheckedChange = { isComboRequest = it },
                                colors = CheckboxDefaults.colors(checkedColor = AueraaElectricBlue)
                            )
                            Text("This is a Combo Package inquiry (Multiple Services)", fontSize = 12.sp, color = AueraaTextPrimary)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = expectedBudgetInr,
                                onValueChange = { expectedBudgetInr = it },
                                label = { Text("Expected Budget (₹ INR)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("enquiry_budget"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = deadline,
                                onValueChange = { deadline = it },
                                label = { Text("Target Deadline") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )
                        }

                        OutlinedTextField(
                            value = referenceLinks,
                            onValueChange = { referenceLinks = it },
                            label = { Text("Reference Websites, Social Links or Competitors") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = projectDescription,
                            onValueChange = { projectDescription = it },
                            label = { Text("Project Goals & Overview") },
                            modifier = Modifier.fillMaxWidth().testTag("enquiry_description"),
                            minLines = 3
                        )

                        OutlinedTextField(
                            value = additionalRequirements,
                            onValueChange = { additionalRequirements = it },
                            label = { Text("Special Requirements or Technical Notes (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 2
                        )

                        if (!errorMessage.isNullOrBlank()) {
                            Text(errorMessage!!, color = AueraaErrorRose, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                if (clientName.isBlank() || contactNumber.isBlank() || email.isBlank()) {
                                    errorMessage = "Please enter your name, phone number, and email."
                                } else {
                                    errorMessage = null
                                    onSubmitEnquiry(
                                        clientName.trim(),
                                        businessName.trim().ifBlank { clientName.trim() },
                                        contactNumber.trim(),
                                        whatsappNumber.trim().ifBlank { contactNumber.trim() },
                                        email.trim(),
                                        businessCategory,
                                        serviceCategory,
                                        selectedPackageName.trim(),
                                        isComboRequest,
                                        expectedBudgetInr.toLongOrNull() ?: 0L,
                                        deadline.trim(),
                                        referenceLinks.trim(),
                                        projectDescription.trim().ifBlank { "Client requested consultation for $selectedPackageName" },
                                        additionalRequirements.trim()
                                    ) { generatedId ->
                                        submittedRefId = generatedId
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("submit_enquiry_confirm_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Submit Project Enquiry & Generate Proposal", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
