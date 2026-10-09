package com.example.ui.screens.public

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AgencySettings
import com.example.data.model.ContactChannelEntity
import com.example.ui.components.AueraaBrandBanner
import com.example.ui.components.AueraaLogoMark
import com.example.ui.components.SectionTitle
import com.example.ui.components.openWhatsAppChat
import com.example.ui.theme.*

@Composable
fun AboutContactScreen(
    settings: AgencySettings?,
    contactChannels: List<ContactChannelEntity> = emptyList(),
    onOpenEnquiry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val faqs = listOf(
        Pair("How does pricing work for ad campaigns?", "We charge a transparent monthly agency management retainer. Your advertising media spend is billed directly by Meta (Facebook/Instagram) or Google to your own payment card for 100% financial transparency."),
        Pair("How many revisions are included?", "Each standard package includes up to 3 structured rounds of feedback. We share staging links before final asset delivery or domain deployment."),
        Pair("What is the typical turnaround time?", "Social media content packs and campaigns are delivered within 7 to 10 days of kickoff. Websites take 2 to 3 weeks, while native mobile applications typically require 4 to 6 weeks."),
        Pair("Do we own the intellectual property and code?", "Yes! Upon receipt of final payment, you retain 100% full commercial copyright to all created graphics, edited video exports, custom website codebase, and mobile application source code.")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            SectionTitle(
                title = "About & Global Contact",
                subtitle = "AUERAA.DIGITAL — Creative Content. Strategic Marketing. Powerful Digital Solutions.",
                accentPill = "AGENCY CREDENTIALS"
            )
        }

        // Brand Banner Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.3f)))),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    AueraaBrandBanner(height = 44.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = settings?.introduction
                            ?: "We are a full-spectrum digital agency empowering ambitious businesses, startups, creators, and hospitality ventures through world-class creative production and cutting-edge software development.",
                        fontSize = 13.sp,
                        color = AueraaTextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // Contact Channels Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Official Agency Contact Channels", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    if (contactChannels.isNotEmpty()) {
                        contactChannels.forEach { channel ->
                            val (icon, iconTint, clickAction) = when (channel.channelType.uppercase()) {
                                "WHATSAPP" -> Triple(
                                    Icons.Default.Chat,
                                    Color(0xFF25D366),
                                    {
                                        openWhatsAppChat(
                                            context,
                                            channel.value,
                                            "Hi Aueraa.Digital! I'm reaching out through ${channel.title}."
                                        )
                                    }
                                )
                                "PHONE" -> Triple(
                                    Icons.Default.Phone,
                                    AueraaElectricBlue,
                                    {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${channel.value.replace(" ", "")}"))
                                        context.startActivity(intent)
                                    }
                                )
                                "EMAIL" -> Triple(
                                    Icons.Default.Email,
                                    AueraaRoyalPurple,
                                    {
                                        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${channel.value.trim()}"))
                                        context.startActivity(intent)
                                    }
                                )
                                "ADDRESS" -> Triple(
                                    Icons.Default.LocationOn,
                                    AueraaAccentAmber,
                                    null
                                )
                                else -> Triple(
                                    Icons.Default.Public,
                                    AueraaNeonCyan,
                                    null
                                )
                            }

                            ContactRow(
                                icon = icon,
                                iconTint = iconTint,
                                title = if (channel.isPrimary) "${channel.title} ★ PRIMARY" else channel.title,
                                value = channel.value,
                                onClick = clickAction
                            )
                        }
                    } else {
                        ContactRow(
                            icon = Icons.Default.Chat,
                            iconTint = Color(0xFF25D366),
                            title = "WhatsApp Concierge",
                            value = settings?.whatsappNumber ?: "+91 98765 43210",
                            onClick = {
                                openWhatsAppChat(
                                    context,
                                    settings?.whatsappNumber ?: "+919876543210",
                                    "Hi Aueraa.Digital! I'd like to consult with your digital agency team."
                                )
                            }
                        )

                        ContactRow(
                            icon = Icons.Default.Phone,
                            iconTint = AueraaElectricBlue,
                            title = "Direct Calling",
                            value = settings?.phone ?: "+91 98765 43210",
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${settings?.phone ?: "+919876543210"}"))
                                context.startActivity(intent)
                            }
                        )

                        ContactRow(
                            icon = Icons.Default.Email,
                            iconTint = AueraaRoyalPurple,
                            title = "Business Inquiries",
                            value = settings?.businessEmail ?: "contact@aueraa.digital",
                            onClick = {
                                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${settings?.businessEmail ?: "contact@aueraa.digital"}"))
                                context.startActivity(intent)
                            }
                        )

                        ContactRow(
                            icon = Icons.Default.LocationOn,
                            iconTint = AueraaAccentAmber,
                            title = "Corporate Office",
                            value = settings?.businessAddress ?: "Silicon Horizon Towers, Bangalore, India",
                            onClick = null
                        )

                        ContactRow(
                            icon = Icons.Default.Schedule,
                            iconTint = AueraaNeonCyan,
                            title = "Business Hours",
                            value = settings?.businessHours ?: "Monday – Saturday: 9:30 AM – 7:00 PM IST",
                            onClick = null
                        )
                    }
                }
            }
        }

        // Social Links Row
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Digital Presence & Social Profiles", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SocialChip("Instagram", settings?.instagramUrl ?: "https://instagram.com/aueraa.digital", context, Modifier.weight(1f))
                        SocialChip("LinkedIn", settings?.linkedinUrl ?: "https://linkedin.com", context, Modifier.weight(1f))
                        SocialChip("YouTube", settings?.youtubeUrl ?: "https://youtube.com", context, Modifier.weight(1f))
                    }
                }
            }
        }

        // FAQs
        item {
            Text("Frequently Asked Questions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(faqs) { faq ->
            val q = faq.first
            val a = faq.second
            var expanded by remember { mutableStateOf(false) }
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(q, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White, modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = AueraaNeonCyan
                        )
                    }
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(a, fontSize = 12.sp, color = AueraaTextSecondary, lineHeight = 16.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Legal & Policies
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Legal & Policies", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Terms: ${settings?.termsAndConditions ?: "50% Advance required for kickoff."}", fontSize = 11.sp, color = AueraaTextSecondary)
                    Text("Privacy: ${settings?.privacyPolicy ?: "Strict client confidentiality guaranteed."}", fontSize = 11.sp, color = AueraaTextSecondary)
                    Text("GSTIN: ${settings?.gstNumber ?: "29AAACA9921D1ZB"}", fontSize = 11.sp, color = AueraaNeonCyan)
                }
            }
        }
    }
}

@Composable
private fun ContactRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    value: String,
    onClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 11.sp, color = AueraaTextMuted)
            Text(value, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Medium)
        }
        if (onClick != null) {
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = AueraaTextMuted, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun SocialChip(
    name: String,
    url: String,
    context: Context,
    modifier: Modifier = Modifier
) {
    FilledTonalButton(
        onClick = {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            } catch (_: Exception) {}
        },
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
        modifier = modifier
    ) {
        Text(name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}
