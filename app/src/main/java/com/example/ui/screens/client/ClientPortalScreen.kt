package com.example.ui.screens.client

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun ClientPortalScreen(
    viewModel: AgencyViewModel,
    clientUser: ClientUserEntity,
    onLogout: () -> Unit,
    onNavigateToServices: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToEnquiry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.agencySettings.collectAsState()
    val allProjects by viewModel.allProjects.collectAsState()
    val allInvoices by viewModel.allInvoices.collectAsState()
    val allEnquiries by viewModel.allEnquiries.collectAsState()

    // Isolated Client Data: Only projects, invoices, and enquiries belonging to this client!
    val clientProjects = allProjects.filter { it.clientId == clientUser.email || it.clientId == clientUser.id }
    val clientInvoices = allInvoices.filter { it.clientId == clientUser.email || it.clientId == clientUser.id }
    val clientEnquiries = allEnquiries.filter { it.email == clientUser.email }

    var selectedTab by remember { mutableIntStateOf(0) } // 0=Dashboard/Projects, 1=Invoices, 2=My Enquiries, 3=Support
    var revisionProject by remember { mutableStateOf<ClientProjectEntity?>(null) }
    var revisionNotesText by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        // Client Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaElectricBlue.copy(alpha = 0.5f), Color.Transparent))),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome, ${clientUser.fullName}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "${clientUser.companyName} • ${clientUser.businessCategory}",
                                fontSize = 12.sp,
                                color = AueraaNeonCyan
                            )
                        }
                        IconButton(onClick = onLogout) {
                            Icon(Icons.Default.Logout, contentDescription = "Sign Out", tint = AueraaTextSecondary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Client Navigation Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AueraaSurfaceDark,
                contentColor = AueraaElectricBlue,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                val tabs = listOf("Projects", "Invoices", "Enquiries", "Support")
                tabs.forEachIndexed { idx, label ->
                    val isSel = selectedTab == idx
                    Tab(
                        selected = isSel,
                        onClick = { selectedTab = idx },
                        text = {
                            Text(
                                label,
                                fontSize = 12.sp,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) AueraaNeonCyan else AueraaTextSecondary
                            )
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        when (selectedTab) {
            0 -> {
                // Active Projects & Deliverables
                item {
                    SectionTitle(
                        title = "Your Active Projects & Deliverables",
                        subtitle = "Track real-time milestones, review staged assets & request revisions",
                        accentPill = "PROJECT HUB"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (clientProjects.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.WorkOutline, contentDescription = null, tint = AueraaTextMuted, modifier = Modifier.size(36.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No active projects found for this account.", color = AueraaTextSecondary, fontSize = 13.sp)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = onNavigateToEnquiry,
                                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue)
                                ) {
                                    Text("Start a New Project")
                                }
                            }
                        }
                    }
                } else {
                    items(clientProjects) { project ->
                        val milestones by viewModel.getMilestonesForProject(project.id).collectAsState(initial = emptyList())

                        Card(
                            modifier = Modifier.fillMaxWidth().testTag("client_project_${project.id}"),
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(AueraaSurfaceBorder, AueraaElectricBlue.copy(alpha = 0.35f)))),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusBadge(status = project.status)
                                    Text("Target: ${project.targetCompletionDate}", fontSize = 11.sp, color = AueraaTextMuted)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(project.projectName, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Service: ${project.serviceCategory}", fontSize = 12.sp, color = AueraaNeonCyan)

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Overall Progress: ${project.progressPercent}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Deliverables: ${project.deliverablesDeliveredCount} / ${project.deliverablesTotalCount}", fontSize = 12.sp, color = AueraaTextSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { project.progressPercent / 100f },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                    color = AueraaElectricBlue,
                                    trackColor = AueraaSurfaceVariant
                                )

                                // Milestones Checklist
                                if (milestones.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Text("Project Milestones:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    milestones.forEach { ms ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = if (ms.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                                contentDescription = null,
                                                tint = if (ms.isCompleted) AueraaSuccessEmerald else AueraaTextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(ms.title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = if (ms.isCompleted) Color.White else AueraaTextSecondary)
                                                Text("Due: ${ms.dueDate}", fontSize = 10.sp, color = AueraaTextMuted)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                HorizontalDivider(color = AueraaSurfaceBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                // Deliverable Download & Actions
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (!project.deliverableDownloadUrl.isNullOrBlank()) {
                                        OutlinedButton(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(project.deliverableDownloadUrl))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Assets", fontSize = 11.sp)
                                        }
                                    }
                                    OutlinedButton(
                                        onClick = { revisionProject = project },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Request Revision", fontSize = 11.sp)
                                    }
                                    if (!project.isApprovedByClient && project.progressPercent >= 50) {
                                        Button(
                                            onClick = { viewModel.approveProject(project.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = AueraaSuccessEmerald),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Approve Work", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            1 -> {
                // Invoices & Billing
                item {
                    SectionTitle(
                        title = "Your Invoices & Recorded Payments",
                        subtitle = "Official tax invoices, advance payments & outstanding balances",
                        accentPill = "BILLING"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (clientInvoices.isEmpty()) {
                    item {
                        Text("No invoices generated yet for this account.", color = AueraaTextMuted, fontSize = 13.sp)
                    }
                } else {
                    items(clientInvoices) { inv ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth().testTag("client_invoice_${inv.id}")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    StatusBadge(status = inv.status)
                                    Text(inv.id, fontSize = 11.sp, color = AueraaTextMuted)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(inv.projectName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(inv.itemsDescription, fontSize = 12.sp, color = AueraaTextSecondary)

                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text("Total (incl. 18% GST):", fontSize = 11.sp, color = AueraaTextMuted)
                                        Text("₹${inv.totalAmountInr}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Column {
                                        Text("Paid:", fontSize = 11.sp, color = AueraaTextMuted)
                                        Text("₹${inv.advancePaidInr}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AueraaSuccessEmerald)
                                    }
                                    Column {
                                        Text("Balance Due:", fontSize = 11.sp, color = AueraaTextMuted)
                                        Text("₹${inv.balanceDueInr}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (inv.balanceDueInr > 0) AueraaErrorRose else AueraaTextMuted)
                                    }
                                }

                                if (inv.balanceDueInr > 0) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            openWhatsAppChat(
                                                context,
                                                settings?.whatsappNumber ?: "+919876543210",
                                                "Hi Aueraa Finance! I want to settle the balance for Invoice ${inv.id} (₹${inv.balanceDueInr}). Please share UPI QR code."
                                            )
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Pay Balance via UPI / Bank Transfer", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            2 -> {
                // My Enquiries
                item {
                    SectionTitle(
                        title = "Your Service Enquiries",
                        subtitle = "Proposals requested and current review stages",
                        accentPill = "INQUIRIES"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (clientEnquiries.isEmpty()) {
                    item {
                        Text("No inquiries submitted yet.", color = AueraaTextMuted, fontSize = 13.sp)
                    }
                } else {
                    items(clientEnquiries) { enq ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    StatusBadge(status = enq.status)
                                    Text(enq.id, fontSize = 11.sp, color = AueraaTextMuted)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(enq.selectedPackageName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Service: ${enquiryCategoryDisplay(enq)}", fontSize = 12.sp, color = AueraaNeonCyan)
                                Text("Budget: ₹${enq.expectedBudgetInr}  |  Timeline: ${enq.deadline}", fontSize = 11.sp, color = AueraaTextSecondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            3 -> {
                // Support & Direct Contact
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
                            Text("Dedicated Agency Support", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Your dedicated account director is available for strategy calls, revisions, and sprint reviews.", fontSize = 12.sp, color = AueraaTextSecondary)

                            Button(
                                onClick = {
                                    openWhatsAppChat(
                                        context,
                                        settings?.whatsappNumber ?: "+919876543210",
                                        "Hi Aueraa.Digital support, this is ${clientUser.fullName} from ${clientUser.companyName} regarding our ongoing project."
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Message Agency on WhatsApp", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:${settings?.businessEmail ?: "contact@aueraa.digital"}")
                                        putExtra(Intent.EXTRA_SUBJECT, "Project Inquiry / Support - ${clientUser.companyName}")
                                    }
                                    context.startActivity(emailIntent)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Email Account Manager", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }

    // Revision Request Dialog
    revisionProject?.let { proj ->
        AlertDialog(
            onDismissRequest = { revisionProject = null },
            containerColor = AueraaSurfaceDark,
            title = { Text("Request Revisions for ${proj.projectName}", color = Color.White, fontSize = 16.sp) },
            text = {
                Column {
                    Text("Describe the modifications, timestamp adjustments, or creative changes requested:", fontSize = 12.sp, color = AueraaTextSecondary)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = revisionNotesText,
                        onValueChange = { revisionNotesText = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        label = { Text("Revision Notes") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (revisionNotesText.isNotBlank()) {
                            viewModel.submitRevision(proj.id, revisionNotesText.trim())
                            revisionProject = null
                            revisionNotesText = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue)
                ) {
                    Text("Submit Revision Request", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { revisionProject = null }) {
                    Text("Cancel", color = AueraaTextSecondary)
                }
            }
        )
    }
}

private fun enquiryCategoryDisplay(enquiry: EnquiryEntity): String {
    return if (enquiry.isComboRequest) "${enquiry.serviceCategory} (Combo)" else enquiry.serviceCategory
}
