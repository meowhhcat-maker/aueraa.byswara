package com.example.ui.screens.admin

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun AdminDashboardScreen(
    viewModel: AgencyViewModel,
    onNavigateToGenerator: () -> Unit,
    onNavigateToSetupWizard: () -> Unit,
    onNavigateToAnalytics: () -> Unit = {},
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.agencySettings.collectAsState()
    val enquiries by viewModel.allEnquiries.collectAsState()
    val packages by viewModel.allPackages.collectAsState()
    val combos by viewModel.allCombos.collectAsState()
    val projects by viewModel.allProjects.collectAsState()
    val invoices by viewModel.allInvoices.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()
    val contactChannels by viewModel.contactChannels.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0=Overview, 1=Leads/CRM, 2=Packages, 3=Projects, 4=Finance, 5=Contacts, 6=Settings

    // State for creating a new invoice dialog
    var showCreateInvoiceDialog by remember { mutableStateOf(false) }

    // State for editing or creating packages
    var editingPackage by remember { mutableStateOf<PackageEntity?>(null) }
    var isCreatingNewPackage by remember { mutableStateOf(false) }

    // State for editing or creating contacts
    var editingContact by remember { mutableStateOf<ContactChannelEntity?>(null) }
    var isCreatingNewContact by remember { mutableStateOf(false) }
    var contactToDelete by remember { mutableStateOf<ContactChannelEntity?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        // Top Admin Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AueraaLogoMark(size = 32.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "AUERAA ADMIN HQ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = "Executive Operations & CRM Portal",
                            fontSize = 11.sp,
                            color = AueraaNeonCyan
                        )
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(
                        onClick = onNavigateToAnalytics,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = AueraaNeonCyan.copy(alpha = 0.18f),
                            contentColor = AueraaNeonCyan
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp).testTag("admin_analytics_btn")
                    ) {
                        Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Recharts", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    FilledTonalButton(
                        onClick = onNavigateToSetupWizard,
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = AueraaSurfaceVariant),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp).testTag("admin_setup_wizard_btn")
                    ) {
                        Icon(Icons.Default.SettingsSuggest, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Wizard", fontSize = 11.sp)
                    }
                    IconButton(onClick = onLogout, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = AueraaErrorRose)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Navigation Tabs for Admin
        item {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = AueraaSurfaceDark,
                contentColor = AueraaElectricBlue,
                edgePadding = 0.dp,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                val tabs = listOf("Overview", "Leads CRM", "Packages", "Projects", "Finance", "Contacts", "Settings")
                tabs.forEachIndexed { index, title ->
                    val isSel = selectedTab == index
                    Tab(
                        selected = isSel,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
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
                // Overview Tab
                item {
                    val totalRevenue = invoices.sumOf { it.totalAmountInr }
                    val collectedRevenue = invoices.sumOf { it.advancePaidInr }
                    val pendingRevenue = invoices.sumOf { it.balanceDueInr }
                    val newLeadsCount = enquiries.count { it.status == "NEW" }
                    val activeProjectsCount = projects.count { it.status == "IN_PROGRESS" || it.status == "REVISIONS" }

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricCard(
                                title = "New Leads",
                                value = newLeadsCount.toString(),
                                subtitle = "${enquiries.size} Total Pipeline",
                                icon = Icons.Default.ContactMail,
                                iconTint = AueraaElectricBlue,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Active Projects",
                                value = activeProjectsCount.toString(),
                                subtitle = "${projects.size} Total Accounts",
                                icon = Icons.Default.Work,
                                iconTint = AueraaRoyalPurple,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            MetricCard(
                                title = "Collected Revenue",
                                value = "₹$collectedRevenue",
                                subtitle = "₹$pendingRevenue Due",
                                icon = Icons.Default.AccountBalanceWallet,
                                iconTint = AueraaSuccessEmerald,
                                modifier = Modifier.weight(1f)
                            )
                            MetricCard(
                                title = "Live Packages",
                                value = packages.count { it.status == "ACTIVE" }.toString(),
                                subtitle = "${combos.size} Combo Bundles",
                                icon = Icons.Default.Inventory,
                                iconTint = AueraaAccentAmber,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // AI Generator Quick Action Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToGenerator() }
                            .testTag("admin_open_generator_card"),
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaRoyalPurple, AueraaElectricBlue))),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("AI DYNAMIC PACKAGE GENERATOR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Synthesize Custom Client Proposals", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("15-Step dynamic proposal engine with budget & scope validation", fontSize = 11.sp, color = AueraaTextSecondary)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Marketing Performance Recharts Analytics Quick Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToAnalytics() }
                            .testTag("admin_open_analytics_card"),
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(AueraaElectricBlue, AueraaNeonCyan))
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Analytics, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("RECHARTS CAMPAIGN ANALYTICS", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Conversion Rate & Ad Spend Intelligence", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("Interactive charts for multi-channel ROAS, conversion curves, and ad expenditure.", fontSize = 11.sp, color = AueraaTextSecondary)
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = AueraaElectricBlue, modifier = Modifier.size(24.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Recent Audit Trail
                item {
                    SectionTitle(
                        title = "Recent System Activities",
                        subtitle = "Real-time tamper-evident operations log",
                        accentPill = "SECURITY AUDIT"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(auditLogs.take(5)) { log ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                    Text(log.action, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(log.performedBy, fontSize = 10.sp, color = AueraaTextMuted)
                                }
                                Text(log.details, fontSize = 11.sp, color = AueraaTextSecondary)
                            }
                        }
                    }
                }
            }

            1 -> {
                // Leads & CRM Management
                item {
                    SectionTitle(
                        title = "Enquiry Pipeline & CRM",
                        subtitle = "Track incoming leads, record follow-up calls, and convert to projects",
                        accentPill = "LEAD INTAKE"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (enquiries.isEmpty()) {
                    item {
                        Text("No enquiries received yet.", color = AueraaTextMuted, fontSize = 13.sp)
                    }
                } else {
                    items(enquiries) { enq ->
                        AdminEnquiryCard(
                            enquiry = enq,
                            onUpdateStatus = { newStatus ->
                                viewModel.updateEnquiryStatus(enq.id, newStatus)
                            },
                            onConvertToProject = {
                                viewModel.convertEnquiryToProject(enq.id)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            2 -> {
                // Package Manager
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle(
                            title = "Package Inventory",
                            subtitle = "Modify prices, deliverables, terms & custom offerings",
                            accentPill = "CATALOG & PRICING"
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isCreatingNewPackage = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.1f).testTag("admin_add_custom_package_btn")
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("New Package", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onNavigateToGenerator,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AueraaNeonCyan),
                            border = ButtonDefaults.outlinedButtonBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaRoyalPurple, AueraaElectricBlue))),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.1f).testTag("admin_ai_generator_btn")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = AueraaNeonCyan)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("AI Generator", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                items(packages) { pkg ->
                    AdminPackageManagementCard(
                        pkg = pkg,
                        onEdit = { editingPackage = pkg },
                        onDuplicate = { viewModel.duplicatePackage(pkg.id) },
                        onDelete = { viewModel.deletePackage(pkg.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            3 -> {
                // Project & Milestones Tracker
                item {
                    SectionTitle(
                        title = "Client Projects & Deliverables",
                        subtitle = "Live milestone delivery, revision tracking & client approval management",
                        accentPill = "DELIVERY MANAGEMENT"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                items(projects) { project ->
                    AdminProjectCard(
                        project = project,
                        onUpdateProgress = { progress, status, deliv, total ->
                            viewModel.updateProjectProgress(project.id, progress, status, deliv, total)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            4 -> {
                // Finance & Invoicing
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle(
                            title = "Invoicing & Financial Ledger",
                            subtitle = "Issue quotations, record milestone payments & audit balances",
                            accentPill = "FINANCE"
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showCreateInvoiceDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaSuccessEmerald),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("admin_create_invoice_btn")
                    ) {
                        Icon(Icons.Default.AddCard, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Issue New Quotation / Tax Invoice", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                items(invoices) { inv ->
                    AdminInvoiceCard(
                        invoice = inv,
                        onRecordPayment = { amount ->
                            viewModel.recordPayment(inv.id, amount, "UPI / NEFT Recorded by Admin")
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            5 -> {
                // Official Agency Contact Channels Directory
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle(
                            title = "Agency Contact Directory",
                            subtitle = "Add, edit, test or delete customer-facing communication channels",
                            accentPill = "COMMUNICATIONS"
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { isCreatingNewContact = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_add_contact_btn")
                    ) {
                        Icon(Icons.Default.AddIcCall, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add New Contact Channel", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                if (contactChannels.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.ContactPhone,
                                    contentDescription = null,
                                    tint = AueraaElectricBlue,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "No Contact Channels Configured",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "Click '+ Add New Contact Channel' above to publish client communication endpoints.",
                                    fontSize = 12.sp,
                                    color = AueraaTextSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(contactChannels) { contact ->
                        AdminContactCard(
                            contact = contact,
                            onEdit = { editingContact = contact },
                            onDelete = { contactToDelete = contact },
                            onLaunch = { launchContactAction(context, contact) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            6 -> {
                // Agency Settings Panel
                item {
                    SectionTitle(
                        title = "Agency Configuration",
                        subtitle = "Brand credentials, legal policies & integration endpoints",
                        accentPill = "SYSTEM SETTINGS"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    AdminSettingsCard(
                        settings = settings,
                        onSaveSettings = { updated ->
                            viewModel.updateAgencySettings(updated)
                        },
                        onOpenWizard = onNavigateToSetupWizard
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Agency Contact Channels", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("${contactChannels.size} active channels configured", fontSize = 12.sp, color = AueraaNeonCyan)
                                }
                                Button(
                                    onClick = { selectedTab = 5 },
                                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text("Manage Channels", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            contactChannels.take(3).forEach { ch ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(AueraaSurfaceVariant.copy(alpha = 0.5f))
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(ch.title, fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Medium)
                                    Text(ch.value, fontSize = 11.sp, color = AueraaTextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateInvoiceDialog) {
        CreateInvoiceDialog(
            onDismiss = { showCreateInvoiceDialog = false },
            onCreate = { clientId, clientName, projectName, desc, subtotal, advance ->
                viewModel.createInvoice(clientId, clientName, projectName, desc, subtotal, advance)
                showCreateInvoiceDialog = false
            }
        )
    }

    if (editingPackage != null || isCreatingNewPackage) {
        PackageEditorDialog(
            packageToEdit = editingPackage,
            isNewPackage = isCreatingNewPackage,
            onDismiss = {
                editingPackage = null
                isCreatingNewPackage = false
            },
            onSave = { updatedPackage ->
                viewModel.savePackage(updatedPackage)
                editingPackage = null
                isCreatingNewPackage = false
            }
        )
    }

    if (editingContact != null || isCreatingNewContact) {
        ContactEditorDialog(
            contactToEdit = editingContact,
            isNewContact = isCreatingNewContact,
            onDismiss = {
                editingContact = null
                isCreatingNewContact = false
            },
            onSave = { updatedContact ->
                viewModel.saveContact(updatedContact)
                editingContact = null
                isCreatingNewContact = false
            }
        )
    }

    if (contactToDelete != null) {
        AlertDialog(
            onDismissRequest = { contactToDelete = null },
            containerColor = AueraaSurfaceDark,
            title = {
                Text(
                    text = "Delete Contact Channel?",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete '${contactToDelete?.title}' (${contactToDelete?.value})? It will be immediately removed from the customer directory.",
                    color = AueraaTextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        contactToDelete?.let { viewModel.deleteContact(it.id) }
                        contactToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaErrorRose)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { contactToDelete = null }) {
                    Text("Cancel", color = AueraaTextSecondary)
                }
            }
        )
    }
}

@Composable
fun AdminEnquiryCard(
    enquiry: EnquiryEntity,
    onUpdateStatus: (String) -> Unit,
    onConvertToProject: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("crm_lead_${enquiry.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = enquiry.status)
                Text(enquiry.id, fontSize = 11.sp, color = AueraaTextMuted)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${enquiry.clientName} • ${enquiry.businessName}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Phone: ${enquiry.contactNumber}  |  Email: ${enquiry.email}",
                fontSize = 11.sp,
                color = AueraaTextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Service: ${enquiry.serviceCategory} (${enquiry.selectedPackageName})",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = AueraaNeonCyan
            )
            Text(
                text = "Budget: ₹${enquiry.expectedBudgetInr}  |  Timeline: ${enquiry.deadline}",
                fontSize = 12.sp,
                color = AueraaTextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Notes / Needs: ${enquiry.projectDescription}",
                fontSize = 12.sp,
                color = AueraaTextPrimary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = AueraaSurfaceBorder)
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onUpdateStatus("CONTACTED") },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Contacted", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = { onUpdateStatus("PROPOSAL_SENT") },
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Proposal", fontSize = 11.sp)
                }
                Button(
                    onClick = onConvertToProject,
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.weight(1.2f).testTag("convert_lead_to_project_btn")
                ) {
                    Text("Convert to Project", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminPackageManagementCard(
    pkg: PackageEntity,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag("admin_pkg_${pkg.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = pkg.status)
                Text(pkg.id, fontSize = 11.sp, color = AueraaTextMuted)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(pkg.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("${pkg.category} • ${pkg.billingFrequency}", fontSize = 12.sp, color = AueraaNeonCyan)
            Spacer(modifier = Modifier.height(4.dp))
            Text(pkg.quantitiesSummary, fontSize = 11.sp, color = AueraaTextSecondary)
            Spacer(modifier = Modifier.height(6.dp))
            CurrencyText(amount = pkg.priceInr, isCustomQuote = pkg.isCustomQuoteRequired, frequency = pkg.billingFrequency, fontSize = 16, color = AueraaElectricBlue)
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp).testTag("edit_pkg_${pkg.id}_btn")
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(6.dp))
                FilledTonalButton(
                    onClick = onDuplicate,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Text("Duplicate", fontSize = 11.sp)
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(30.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AueraaErrorRose)
                }
            }
        }
    }
}

@Composable
fun AdminProjectCard(
    project: ClientProjectEntity,
    onUpdateProgress: (progress: Int, status: String, delivered: Int, total: Int) -> Unit
) {
    var progressSlider by remember(project.progressPercent) { mutableFloatStateOf(project.progressPercent.toFloat()) }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AueraaSurfaceBorder, Color.Transparent))),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = project.status)
                Text(project.id, fontSize = 11.sp, color = AueraaTextMuted)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(project.projectName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Client: ${project.clientName} (${project.clientId})", fontSize = 12.sp, color = AueraaTextSecondary)

            if (!project.revisionRequestNotes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Card(colors = CardDefaults.cardColors(containerColor = AueraaErrorRose.copy(alpha = 0.15f))) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Text("⚠ Client Revision Request: ", fontWeight = FontWeight.Bold, color = AueraaErrorRose, fontSize = 11.sp)
                        Text(project.revisionRequestNotes, color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progress: ${progressSlider.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AueraaNeonCyan)
                Text("Deliverables: ${project.deliverablesDeliveredCount} / ${project.deliverablesTotalCount}", fontSize = 12.sp, color = AueraaTextSecondary)
            }
            Slider(
                value = progressSlider,
                onValueChange = { progressSlider = it },
                onValueChangeFinished = {
                    onUpdateProgress(
                        progressSlider.toInt(),
                        if (progressSlider >= 100f) "COMPLETED" else "IN_PROGRESS",
                        project.deliverablesDeliveredCount,
                        project.deliverablesTotalCount
                    )
                },
                valueRange = 0f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = AueraaElectricBlue,
                    activeTrackColor = AueraaElectricBlue
                )
            )
        }
    }
}

@Composable
fun AdminInvoiceCard(
    invoice: InvoiceEntity,
    onRecordPayment: (Long) -> Unit
) {
    var payAmountText by remember { mutableStateOf(invoice.balanceDueInr.toString()) }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(status = invoice.status)
                Text(invoice.id, fontSize = 11.sp, color = AueraaTextMuted)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(invoice.clientName, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(invoice.itemsDescription, fontSize = 12.sp, color = AueraaTextSecondary)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total: ₹${invoice.totalAmountInr}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text("Paid: ₹${invoice.advancePaidInr}", fontSize = 13.sp, color = AueraaSuccessEmerald)
                Text("Balance Due: ₹${invoice.balanceDueInr}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (invoice.balanceDueInr > 0) AueraaErrorRose else AueraaTextMuted)
            }

            if (invoice.balanceDueInr > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = payAmountText,
                        onValueChange = { payAmountText = it },
                        label = { Text("Amount (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f).height(50.dp),
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            val amt = payAmountText.toLongOrNull() ?: 0L
                            if (amt > 0) onRecordPayment(amt)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaSuccessEmerald),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Record Payment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSettingsCard(
    settings: AgencySettings?,
    onSaveSettings: (AgencySettings) -> Unit,
    onOpenWizard: () -> Unit
) {
    var agencyName by remember(settings) { mutableStateOf(settings?.agencyName ?: "AUERAA.DIGITAL") }
    var phone by remember(settings) { mutableStateOf(settings?.phone ?: "+91 98765 43210") }
    var whatsapp by remember(settings) { mutableStateOf(settings?.whatsappNumber ?: "+919876543210") }
    var email by remember(settings) { mutableStateOf(settings?.businessEmail ?: "contact@aueraa.digital") }
    var website by remember(settings) { mutableStateOf(settings?.websiteUrl ?: "https://aueraa.digital") }
    var gst by remember(settings) { mutableStateOf(settings?.gstNumber ?: "29AAACA9921D1ZB") }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Agency Profile & Endpoints", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                FilledTonalButton(onClick = onOpenWizard) {
                    Text("Full Setup Wizard", fontSize = 11.sp)
                }
            }

            OutlinedTextField(
                value = agencyName,
                onValueChange = { agencyName = it },
                label = { Text("Agency Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp") },
                    modifier = Modifier.weight(1f),
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
                value = gst,
                onValueChange = { gst = it },
                label = { Text("GST Registration Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val current = settings ?: AgencySettings()
                    onSaveSettings(
                        current.copy(
                            agencyName = agencyName.trim(),
                            phone = phone.trim(),
                            whatsappNumber = whatsapp.trim(),
                            businessEmail = email.trim(),
                            websiteUrl = website.trim(),
                            gstNumber = gst.trim()
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Agency Settings", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CreateInvoiceDialog(
    onDismiss: () -> Unit,
    onCreate: (clientId: String, clientName: String, projectName: String, desc: String, subtotal: Long, advance: Long) -> Unit
) {
    var clientName by remember { mutableStateOf("Verma Artisan Roasters") }
    var clientId by remember { mutableStateOf("client@startup.in") }
    var projectName by remember { mutableStateOf("Brand Retainer - Month 2") }
    var desc by remember { mutableStateOf("Content & Digital Marketing Retainer") }
    var subtotalText by remember { mutableStateOf("45000") }
    var advanceText by remember { mutableStateOf("22500") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AueraaSurfaceDark,
        title = { Text("Issue New Quotation / Invoice", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = clientName, onValueChange = { clientName = it }, label = { Text("Client Name") }, singleLine = true)
                OutlinedTextField(value = clientId, onValueChange = { clientId = it }, label = { Text("Client Email") }, singleLine = true)
                OutlinedTextField(value = projectName, onValueChange = { projectName = it }, label = { Text("Project / Service") }, singleLine = true)
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Deliverables Summary") }, singleLine = true)
                OutlinedTextField(value = subtotalText, onValueChange = { subtotalText = it }, label = { Text("Subtotal (INR)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
                OutlinedTextField(value = advanceText, onValueChange = { advanceText = it }, label = { Text("Advance Paid (INR)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sub = subtotalText.toLongOrNull() ?: 0L
                    val adv = advanceText.toLongOrNull() ?: 0L
                    onCreate(clientId, clientName, projectName, desc, sub, adv)
                },
                colors = ButtonDefaults.buttonColors(containerColor = AueraaSuccessEmerald)
            ) {
                Text("Generate Invoice", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = AueraaTextSecondary) }
        }
    )
}

@Composable
fun AdminContactCard(
    contact: ContactChannelEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onLaunch: () -> Unit
) {
    val (icon, iconTint) = when (contact.channelType.uppercase()) {
        "WHATSAPP" -> Icons.Default.Chat to Color(0xFF25D366)
        "PHONE" -> Icons.Default.Phone to AueraaElectricBlue
        "EMAIL" -> Icons.Default.Email to AueraaRoyalPurple
        "ADDRESS" -> Icons.Default.LocationOn to AueraaAccentAmber
        "SOCIAL" -> Icons.Default.Public to AueraaNeonCyan
        else -> Icons.Default.ContactSupport to AueraaElectricBlue
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("admin_contact_${contact.id}")
            .border(
                1.dp,
                if (contact.isPrimary) AueraaNeonCyan.copy(alpha = 0.5f) else AueraaSurfaceBorder,
                RoundedCornerShape(12.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(iconTint.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = contact.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            if (contact.isPrimary) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(AueraaNeonCyan.copy(alpha = 0.18f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "★ PRIMARY",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AueraaNeonCyan
                                    )
                                }
                            }
                        }
                        Text(
                            text = contact.channelType,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = iconTint
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("admin_edit_contact_${contact.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Contact",
                            tint = AueraaElectricBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("admin_delete_contact_${contact.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Contact",
                            tint = AueraaErrorRose,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Value & Subtitle
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AueraaSurfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column {
                    Text(
                        text = contact.value,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AueraaTextPrimary
                    )
                    if (contact.subtitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = contact.subtitle,
                            fontSize = 11.sp,
                            color = AueraaTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onLaunch,
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        imageVector = when (contact.channelType.uppercase()) {
                            "PHONE" -> Icons.Default.Call
                            "WHATSAPP" -> Icons.Default.Send
                            "EMAIL" -> Icons.Default.MailOutline
                            else -> Icons.Default.OpenInNew
                        },
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = AueraaNeonCyan
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Test Launch", fontSize = 11.sp, color = AueraaNeonCyan)
                }

                Text(
                    text = "ID: ${contact.id}",
                    fontSize = 10.sp,
                    color = AueraaTextMuted
                )
            }
        }
    }
}

fun launchContactAction(context: android.content.Context, contact: ContactChannelEntity) {
    try {
        when (contact.channelType.uppercase()) {
            "WHATSAPP" -> {
                val clean = contact.value.replace("+", "").replace(" ", "").replace("-", "")
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=$clean&text=${Uri.encode("Hello Aueraa.Digital!")}")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }
            "PHONE" -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.value.replace(" ", "")}"))
                context.startActivity(intent)
            }
            "EMAIL" -> {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${contact.value.trim()}"))
                context.startActivity(intent)
            }
            "ADDRESS" -> {
                val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(contact.value)}")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                context.startActivity(mapIntent)
            }
            else -> {
                if (contact.value.startsWith("http://") || contact.value.startsWith("https://")) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(contact.value))
                    context.startActivity(intent)
                }
            }
        }
    } catch (_: Exception) {}
}
