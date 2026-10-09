package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel

@Composable
fun AuthScreen(
    viewModel: AgencyViewModel,
    initialTab: Int = 0, // 0 = Client Login, 1 = Register Client, 2 = Admin HQ
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    // Admin sub-mode: 0 = Admin Login, 1 = Admin Register
    var adminSubMode by remember { mutableIntStateOf(0) }

    // Credentials
    var email by remember { mutableStateOf("client@startup.in") }
    var password by remember { mutableStateOf("client123") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Client Register fields
    var fullName by remember { mutableStateOf("") }
    var companyName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var businessCategory by remember { mutableStateOf("E-Commerce Brand") }

    // Admin Register fields
    var adminFullName by remember { mutableStateOf("") }
    var adminEmail by remember { mutableStateOf("lead.growth@aueraa.digital") }
    var adminDepartment by remember { mutableStateOf("Head of Performance Marketing") }
    var adminPhone by remember { mutableStateOf("+91 98765 43210") }
    var adminPassword by remember { mutableStateOf("adminPass2026") }
    var adminSecurityKey by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Social Modal State
    var socialModalProvider by remember { mutableStateOf<String?>(null) }

    val adminDepartments = listOf(
        "Head of Performance Marketing",
        "Agency Director / Executive",
        "Senior Media Buyer",
        "Creative & Content Director",
        "Full-Stack Tech Lead"
    )

    LaunchedEffect(selectedTab, adminSubMode) {
        errorMessage = null
        if (selectedTab == 2) {
            if (adminSubMode == 0) {
                email = "admin@aueraa.digital"
                password = "admin123"
            }
        } else if (selectedTab == 0) {
            email = "client@startup.in"
            password = "client123"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(AueraaDarkBackground)
            .testTag("auth_screen"),
        contentPadding = PaddingValues(20.dp, 24.dp, 20.dp, 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Branding Banner
        item {
            AueraaLogoMark(size = 52.dp)
            Spacer(modifier = Modifier.height(10.dp))
            AueraaBrandBanner(height = 40.dp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Secure Portal Authentication",
                fontSize = 13.sp,
                color = AueraaTextSecondary
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Top Authentication Tabs
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = AueraaSurfaceDark,
                contentColor = AueraaElectricBlue,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, AueraaSurfaceBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Client Login",
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Register",
                            fontSize = 12.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = if (selectedTab == 2) AueraaRoyalPurple else AueraaTextSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Admin HQ",
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 2) AueraaRoyalPurple else AueraaTextSecondary
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Admin Security Badge / Sub-toggle
        if (selectedTab == 2) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AueraaRoyalPurple.copy(alpha = 0.12f)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, AueraaRoyalPurple.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = AueraaRoyalPurple, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Restricted Admin Security Gate", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Unverified public access is blocked. Authorized team members must log in or register with the Agency Security Key.",
                            fontSize = 11.sp,
                            color = AueraaTextSecondary,
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Sub-Mode toggle between Login and Admin Registration
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(AueraaSurfaceDark)
                                .padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (adminSubMode == 0) AueraaRoyalPurple else Color.Transparent)
                                    .clickable { adminSubMode = 0 }
                                    .padding(vertical = 6.dp)
                                    .testTag("admin_submode_login"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Admin Sign In",
                                    fontSize = 11.sp,
                                    fontWeight = if (adminSubMode == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (adminSubMode == 0) Color.White else AueraaTextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1.2f)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (adminSubMode == 1) AueraaRoyalPurple else Color.Transparent)
                                    .clickable { adminSubMode = 1 }
                                    .padding(vertical = 6.dp)
                                    .testTag("admin_submode_register"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        "Register Staff Admin",
                                        fontSize = 11.sp,
                                        fontWeight = if (adminSubMode == 1) FontWeight.Bold else FontWeight.Normal,
                                        color = if (adminSubMode == 1) Color.White else AueraaTextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Form Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = AueraaSurfaceDark),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(AueraaSurfaceBorder, Color.Transparent)
                    )
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    when {
                        // CLIENT REGISTRATION
                        selectedTab == 1 -> {
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                label = { Text("Full Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = companyName,
                                onValueChange = { companyName = it },
                                label = { Text("Company / Brand Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Phone") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = whatsapp,
                                    onValueChange = { whatsapp = it },
                                    label = { Text("WhatsApp") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                            }
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Work Email") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().testTag("auth_email_field"),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_password_field"),
                                singleLine = true
                            )
                        }

                        // ADMIN REGISTRATION (with Master Security Passcode verification)
                        selectedTab == 2 && adminSubMode == 1 -> {
                            Text("Official Staff Administrator Registration", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AueraaNeonCyan)

                            OutlinedTextField(
                                value = adminFullName,
                                onValueChange = { adminFullName = it },
                                label = { Text("Admin Legal Name") },
                                placeholder = { Text("e.g. Priya Sharma") },
                                modifier = Modifier.fillMaxWidth().testTag("admin_reg_name"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = adminEmail,
                                onValueChange = { adminEmail = it },
                                label = { Text("Official Agency Email") },
                                placeholder = { Text("e.g. name@aueraa.digital") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().testTag("admin_reg_email"),
                                singleLine = true
                            )

                            // Department Selection
                            Text("Department / Executive Role", fontSize = 11.sp, color = AueraaTextSecondary)
                            adminDepartments.take(3).forEach { dept ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { adminDepartment = dept }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = adminDepartment == dept, onClick = { adminDepartment = dept })
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(dept, fontSize = 12.sp, color = if (adminDepartment == dept) Color.White else AueraaTextMuted)
                                }
                            }

                            OutlinedTextField(
                                value = adminPhone,
                                onValueChange = { adminPhone = it },
                                label = { Text("Direct Phone / WhatsApp") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = adminPassword,
                                onValueChange = { adminPassword = it },
                                label = { Text("Admin Access Password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )

                            // Agency Admin Master Security Key
                            Column {
                                OutlinedTextField(
                                    value = adminSecurityKey,
                                    onValueChange = { adminSecurityKey = it },
                                    label = { Text("Agency Master Security Passcode") },
                                    placeholder = { Text("Enter AUERAA-ADMIN-2026") },
                                    leadingIcon = { Icon(Icons.Default.VpnKey, contentDescription = null, tint = AueraaNeonCyan) },
                                    modifier = Modifier.fillMaxWidth().testTag("admin_reg_security_key"),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Protected: Requires agency key to register",
                                        fontSize = 10.sp,
                                        color = AueraaTextMuted
                                    )
                                    TextButton(
                                        onClick = { adminSecurityKey = "AUERAA-ADMIN-2026" },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("Auto-fill Key (Demo)", fontSize = 10.sp, color = AueraaNeonCyan)
                                    }
                                }
                            }
                        }

                        // STANDARD LOGIN (Client or Admin)
                        else -> {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text(if (selectedTab == 2) "Admin Agency Email" else "Client Email") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier.fillMaxWidth().testTag("auth_email_field"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_password_field"),
                                singleLine = true
                            )
                        }
                    }

                    if (!errorMessage.isNullOrBlank()) {
                        Text(errorMessage!!, color = AueraaErrorRose, fontSize = 12.sp, lineHeight = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Primary Submit Button
                    Button(
                        onClick = {
                            isLoading = true
                            errorMessage = null
                            when {
                                selectedTab == 2 && adminSubMode == 1 -> {
                                    // Admin Registration
                                    viewModel.registerAdmin(
                                        email = adminEmail,
                                        password = adminPassword,
                                        fullName = adminFullName.ifBlank { "Agency Administrator" },
                                        department = adminDepartment,
                                        phone = adminPhone,
                                        adminSecretKey = adminSecurityKey,
                                        onSuccess = {
                                            isLoading = false
                                            onLoginSuccess()
                                        },
                                        onError = { err ->
                                            isLoading = false
                                            errorMessage = err
                                        }
                                    )
                                }
                                selectedTab == 1 -> {
                                    // Client Registration
                                    viewModel.register(
                                        email, password, fullName, companyName, phone, whatsapp, businessCategory,
                                        onSuccess = {
                                            isLoading = false
                                            onLoginSuccess()
                                        },
                                        onError = { err ->
                                            isLoading = false
                                            errorMessage = err
                                        }
                                    )
                                }
                                else -> {
                                    // Login
                                    viewModel.login(
                                        email, password,
                                        onSuccess = {
                                            isLoading = false
                                            onLoginSuccess()
                                        },
                                        onError = { err ->
                                            isLoading = false
                                            errorMessage = err
                                        }
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == 2) AueraaRoyalPurple else AueraaElectricBlue
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("auth_submit_btn")
                    ) {
                        Text(
                            text = when {
                                selectedTab == 2 && adminSubMode == 1 -> "Authorize & Register Admin"
                                selectedTab == 2 -> "Sign In to Admin HQ"
                                selectedTab == 1 -> "Create Client Account"
                                else -> "Sign In to Client Portal"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Divider with "OR SOCIAL SIGN IN"
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = AueraaSurfaceBorder)
                        Text(
                            text = "  OR SIGN IN WITH  ",
                            fontSize = 10.sp,
                            color = AueraaTextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = AueraaSurfaceBorder)
                    }

                    // Social Login Options (Google, LinkedIn, Social Media)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // 1. Google Account Option
                        OutlinedButton(
                            onClick = { socialModalProvider = "GOOGLE" },
                            modifier = Modifier.fillMaxWidth().testTag("social_login_google"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            GoogleBrandIcon(modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (selectedTab == 2) "Continue with Google Workspace" else "Continue with Google",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // 2. LinkedIn Account Option
                        Button(
                            onClick = { socialModalProvider = "LINKEDIN" },
                            modifier = Modifier.fillMaxWidth().testTag("social_login_linkedin"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A66C2))
                        ) {
                            LinkedInBrandIcon(modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (selectedTab == 2) "Executive Sign In with LinkedIn" else "Continue with LinkedIn",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }

                        // 3. Social Media (Instagram / Meta)
                        OutlinedButton(
                            onClick = { socialModalProvider = "SOCIAL" },
                            modifier = Modifier.fillMaxWidth().testTag("social_login_meta"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AueraaNeonCyan)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = AueraaNeonCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Continue with Meta / Social Media",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }

    // Social Authentication Selection Dialog
    socialModalProvider?.let { provider ->
        val providerTitle = when (provider) {
            "GOOGLE" -> "Google Account"
            "LINKEDIN" -> "LinkedIn Profile"
            else -> "Social Media Profile"
        }

        var customEmail by remember { mutableStateOf("") }
        var socialAdminPasscode by remember { mutableStateOf("") }
        val isTargetAdmin = selectedTab == 2

        Dialog(onDismissRequest = { socialModalProvider = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.94f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, AueraaElectricBlue.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
                color = AueraaSurfaceDark
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (provider == "GOOGLE") GoogleBrandIcon(modifier = Modifier.size(20.dp))
                            else if (provider == "LINKEDIN") LinkedInBrandIcon(modifier = Modifier.size(20.dp))
                            else Icon(Icons.Default.Share, contentDescription = null, tint = AueraaNeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sign In with $providerTitle",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        IconButton(onClick = { socialModalProvider = null }, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = AueraaTextSecondary)
                        }
                    }

                    Text(
                        text = if (isTargetAdmin)
                            "Authenticate authorized agency administrator account using $providerTitle."
                        else "Choose an account to instantly access your client dashboard.",
                        fontSize = 12.sp,
                        color = AueraaTextSecondary
                    )

                    HorizontalDivider(color = AueraaSurfaceBorder)

                    // Quick Account Options
                    Text("Select Account", fontSize = 11.sp, color = AueraaNeonCyan, fontWeight = FontWeight.Bold)

                    val quickAccounts = if (isTargetAdmin) {
                        listOf(
                            Triple("Aueraa Agency Director", "director.aueraa@gmail.com", "Executive Director"),
                            Triple("Aueraa Growth Executive", "admin.growth@aueraa.digital", "Performance Lead")
                        )
                    } else {
                        listOf(
                            Triple("Rohan Verma", "rohan.verma@startup.in", "Verma Artisan Roasters"),
                            Triple("Client Partner", "meowhhcat@gmail.com", "Enterprise Client")
                        )
                    }

                    quickAccounts.forEach { (name, accEmail, subtitle) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(AueraaSurfaceVariant)
                                .clickable {
                                    viewModel.loginWithSocial(
                                        provider = provider,
                                        email = accEmail,
                                        fullName = name,
                                        targetRole = if (isTargetAdmin) "ADMIN" else "CLIENT",
                                        adminPasscode = if (isTargetAdmin) "AUERAA-ADMIN-2026" else null,
                                        onSuccess = {
                                            socialModalProvider = null
                                            onLoginSuccess()
                                        },
                                        onError = { err -> errorMessage = err }
                                    )
                                }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isTargetAdmin) AueraaRoyalPurple else AueraaElectricBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(name.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text(accEmail, fontSize = 11.sp, color = AueraaTextSecondary)
                                }
                            }
                            Text(subtitle, fontSize = 10.sp, color = AueraaNeonCyan)
                        }
                    }

                    if (isTargetAdmin) {
                        // Admin security key check for social
                        OutlinedTextField(
                            value = socialAdminPasscode,
                            onValueChange = { socialAdminPasscode = it },
                            label = { Text("Agency Master Passcode (for new Admin)") },
                            placeholder = { Text("AUERAA-ADMIN-2026") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // Custom Email option
                    OutlinedTextField(
                        value = customEmail,
                        onValueChange = { customEmail = it },
                        label = { Text("Or connect another $providerTitle email") },
                        placeholder = { Text("user@company.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            val emailToUse = customEmail.ifBlank {
                                if (isTargetAdmin) "admin.social@aueraa.digital" else "client.social@company.com"
                            }
                            viewModel.loginWithSocial(
                                provider = provider,
                                email = emailToUse,
                                fullName = if (isTargetAdmin) "Verified Admin" else "Verified Client",
                                targetRole = if (isTargetAdmin) "ADMIN" else "CLIENT",
                                adminPasscode = if (isTargetAdmin) socialAdminPasscode.ifBlank { "AUERAA-ADMIN-2026" } else null,
                                onSuccess = {
                                    socialModalProvider = null
                                    onLoginSuccess()
                                },
                                onError = { err -> errorMessage = err }
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AueraaElectricBlue),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Connect Account & Continue", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
