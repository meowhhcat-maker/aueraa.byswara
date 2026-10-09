package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.AgencySettings
import com.example.ui.components.AueraaBrandBanner
import com.example.ui.components.AueraaLogoMark
import com.example.ui.components.SectionTitle
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun SetupWizardScreen(
    viewModel: AgencyViewModel,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSettings by viewModel.agencySettings.collectAsState()

    var agencyName by remember(currentSettings) { mutableStateOf(currentSettings?.agencyName ?: "AUERAA.DIGITAL") }
    var tagline by remember(currentSettings) { mutableStateOf(currentSettings?.tagline ?: "Creative Content. Strategic Marketing. Powerful Digital Solutions.") }
    var introduction by remember(currentSettings) { mutableStateOf(currentSettings?.introduction ?: "Aueraa.Digital is an elite creative and technology agency transforming brands through high-impact content, hyper-targeted digital marketing, modern websites, and scalable mobile applications.") }
    var phone by remember(currentSettings) { mutableStateOf(currentSettings?.phone ?: "+91 98765 43210") }
    var whatsapp by remember(currentSettings) { mutableStateOf(currentSettings?.whatsappNumber ?: "+919876543210") }
    var email by remember(currentSettings) { mutableStateOf(currentSettings?.businessEmail ?: "contact@aueraa.digital") }
    var websiteUrl by remember(currentSettings) { mutableStateOf(currentSettings?.websiteUrl ?: "https://aueraa.digital") }
    var instagramUrl by remember(currentSettings) { mutableStateOf(currentSettings?.instagramUrl ?: "https://instagram.com/aueraa.digital") }
    var facebookUrl by remember(currentSettings) { mutableStateOf(currentSettings?.facebookUrl ?: "https://facebook.com/aueraa.digital") }
    var linkedinUrl by remember(currentSettings) { mutableStateOf(currentSettings?.linkedinUrl ?: "https://linkedin.com/company/aueraa-digital") }
    var youtubeUrl by remember(currentSettings) { mutableStateOf(currentSettings?.youtubeUrl ?: "https://youtube.com/@aueraa.digital") }
    var businessAddress by remember(currentSettings) { mutableStateOf(currentSettings?.businessAddress ?: "Level 8, Silicon Horizon Towers, Bangalore, India") }
    var gstNumber by remember(currentSettings) { mutableStateOf(currentSettings?.gstNumber ?: "29AAACA9921D1ZB") }
    var paymentDetails by remember(currentSettings) { mutableStateOf(currentSettings?.paymentDetails ?: "UPI: aueraa.digital@hdfcbank | Bank: Aueraa Digital Pvt Ltd") }
    var businessHours by remember(currentSettings) { mutableStateOf(currentSettings?.businessHours ?: "Monday – Saturday: 9:30 AM – 7:00 PM IST") }
    var terms by remember(currentSettings) { mutableStateOf(currentSettings?.termsAndConditions ?: "50% Mobilization advance required before project kickoff.") }
    var privacy by remember(currentSettings) { mutableStateOf(currentSettings?.privacyPolicy ?: "Client credentials and marketing data strictly confidential under mutual NDA.") }
    var refundPolicy by remember(currentSettings) { mutableStateOf(currentSettings?.refundPolicy ?: "Deposit refundable up to 48 hours prior to creative production kickoff.") }

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
                IconButton(onClick = onComplete) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(6.dp))
                SectionTitle(
                    title = "Initial Agency Setup Wizard",
                    subtitle = "Configure your official brand identity, payment channels & policies",
                    accentPill = "SETUP WIZARD"
                )
            }
        }

        // Brand Emblem preview
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AueraaBrandBanner(height = 44.dp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Agency Logo & Adaptive Icon Active",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AueraaNeonCyan
                    )
                    Text(
                        "Rendered with your official Aueraa.Digital origami crystal emblem.",
                        fontSize = 11.sp,
                        color = AueraaTextSecondary
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("1. Brand & Narrative", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    OutlinedTextField(
                        value = agencyName,
                        onValueChange = { agencyName = it },
                        label = { Text("Agency Name") },
                        modifier = Modifier.fillMaxWidth().testTag("wizard_agency_name"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tagline,
                        onValueChange = { tagline = it },
                        label = { Text("Tagline") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = introduction,
                        onValueChange = { introduction = it },
                        label = { Text("Agency Introduction") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Text("2. Direct Contact & Communication Channels", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = whatsapp,
                            onValueChange = { whatsapp = it },
                            label = { Text("WhatsApp (with Code)") },
                            modifier = Modifier.weight(1f).testTag("wizard_whatsapp"),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Business Email") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = websiteUrl,
                        onValueChange = { websiteUrl = it },
                        label = { Text("Official Website URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = businessAddress,
                        onValueChange = { businessAddress = it },
                        label = { Text("Business Office Address (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Text("3. Social Media & Presence URLs", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    OutlinedTextField(value = instagramUrl, onValueChange = { instagramUrl = it }, label = { Text("Instagram Profile URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = facebookUrl, onValueChange = { facebookUrl = it }, label = { Text("Facebook Page URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = linkedinUrl, onValueChange = { linkedinUrl = it }, label = { Text("LinkedIn URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = youtubeUrl, onValueChange = { youtubeUrl = it }, label = { Text("YouTube Channel URL") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                    HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Text("4. Financials & Operating Hours", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    OutlinedTextField(value = gstNumber, onValueChange = { gstNumber = it }, label = { Text("GST Registration Number (Optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = paymentDetails, onValueChange = { paymentDetails = it }, label = { Text("Payment UPI & Bank Details (Optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(value = businessHours, onValueChange = { businessHours = it }, label = { Text("Operating Hours") }, modifier = Modifier.fillMaxWidth(), singleLine = true)

                    HorizontalDivider(color = AueraaSurfaceBorder, modifier = Modifier.padding(vertical = 4.dp))
                    Text("5. Legal Policies", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                    OutlinedTextField(value = terms, onValueChange = { terms = it }, label = { Text("Terms & Conditions") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    OutlinedTextField(value = privacy, onValueChange = { privacy = it }, label = { Text("Privacy Policy") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
                    OutlinedTextField(value = refundPolicy, onValueChange = { refundPolicy = it }, label = { Text("Refund & Cancellation Policy") }, modifier = Modifier.fillMaxWidth(), minLines = 2)

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val base = currentSettings ?: AgencySettings()
                            viewModel.updateAgencySettings(
                                base.copy(
                                    agencyName = agencyName.trim(),
                                    tagline = tagline.trim(),
                                    introduction = introduction.trim(),
                                    phone = phone.trim(),
                                    whatsappNumber = whatsapp.trim(),
                                    businessEmail = email.trim(),
                                    websiteUrl = websiteUrl.trim(),
                                    instagramUrl = instagramUrl.trim(),
                                    facebookUrl = facebookUrl.trim(),
                                    linkedinUrl = linkedinUrl.trim(),
                                    youtubeUrl = youtubeUrl.trim(),
                                    businessAddress = businessAddress.trim(),
                                    gstNumber = gstNumber.trim(),
                                    paymentDetails = paymentDetails.trim(),
                                    businessHours = businessHours.trim(),
                                    termsAndConditions = terms.trim(),
                                    privacyPolicy = privacy.trim(),
                                    refundPolicy = refundPolicy.trim(),
                                    isSetupCompleted = true
                                )
                            )
                            onComplete()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("save_setup_wizard_btn")
                    ) {
                        Text("Save & Complete Agency Setup", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
