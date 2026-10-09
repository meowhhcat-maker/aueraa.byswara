package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.database.AppDatabase
import com.example.data.repository.AgencyRepository
import com.example.ui.components.FloatingWhatsAppButton
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.admin.PackageGeneratorScreen
import com.example.ui.screens.admin.SetupWizardScreen
import com.example.ui.screens.analytics.CampaignAnalyticsScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.client.ClientPortalScreen
import com.example.ui.screens.public.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AgencyViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val repository = AgencyRepository(database)
        val viewModel = AgencyViewModel(repository)

        setContent {
            AueraaDigitalTheme(darkTheme = true) {
                AueraaMainApp(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Home")
    object Services : Screen("services", "Services")
    object Combos : Screen("combos", "Combos")
    object Packages : Screen("packages", "Packages")
    object Portfolio : Screen("portfolio", "Portfolio")
    object AboutContact : Screen("about_contact", "About")
    object Enquiry : Screen("enquiry", "Get Quote")
    object Auth : Screen("auth", "Sign In")
    object ClientPortal : Screen("client_portal", "Portal")
    object AdminDashboard : Screen("admin_dashboard", "Admin HQ")
    object PackageGenerator : Screen("admin_package_generator", "Generator")
    object SetupWizard : Screen("admin_setup_wizard", "Setup")
    object CampaignAnalytics : Screen("campaign_analytics", "Analytics")
}

@Composable
fun AueraaMainApp(viewModel: AgencyViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    var selectedServiceCategory by remember { mutableStateOf<String?>(null) }
    var prefilledPackageName by remember { mutableStateOf<String?>(null) }
    var authInitialTab by remember { mutableIntStateOf(0) }

    val currentUser by viewModel.currentUser.collectAsState()
    val settings by viewModel.agencySettings.collectAsState()
    val services by viewModel.services.collectAsState()
    val packages by viewModel.activePackages.collectAsState()
    val combos by viewModel.activeCombos.collectAsState()
    val portfolio by viewModel.publicPortfolio.collectAsState()
    val contactChannels by viewModel.contactChannels.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Back button handling
    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = when (currentScreen) {
            Screen.PackageGenerator, Screen.SetupWizard, Screen.CampaignAnalytics -> {
                if (currentUser?.role == "ADMIN") Screen.AdminDashboard else Screen.Home
            }
            Screen.Enquiry -> Screen.Packages
            else -> Screen.Home
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(AueraaDarkBackground),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = AueraaSurfaceDark,
                contentColor = Color.White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .border(0.8.dp, AueraaSurfaceBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                val isHome = currentScreen == Screen.Home
                val isServices = currentScreen == Screen.Services
                val isCombos = currentScreen == Screen.Combos
                val isPackages = currentScreen == Screen.Packages
                val isPortal = currentScreen == Screen.ClientPortal || currentScreen == Screen.AdminDashboard || currentScreen == Screen.Auth

                NavigationBarItem(
                    selected = isHome,
                    onClick = { currentScreen = Screen.Home },
                    icon = { Icon(if (isHome) Icons.Filled.Home else Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AueraaNeonCyan,
                        indicatorColor = AueraaElectricBlue,
                        unselectedIconColor = AueraaTextSecondary,
                        unselectedTextColor = AueraaTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = isServices,
                    onClick = {
                        selectedServiceCategory = null
                        currentScreen = Screen.Services
                    },
                    icon = { Icon(if (isServices) Icons.Filled.Category else Icons.Outlined.Category, contentDescription = "Services") },
                    label = { Text("Services", fontSize = 10.sp, fontWeight = if (isServices) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AueraaNeonCyan,
                        indicatorColor = AueraaElectricBlue,
                        unselectedIconColor = AueraaTextSecondary,
                        unselectedTextColor = AueraaTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_services")
                )

                NavigationBarItem(
                    selected = isCombos,
                    onClick = { currentScreen = Screen.Combos },
                    icon = { Icon(if (isCombos) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome, contentDescription = "Combos") },
                    label = { Text("Combos", fontSize = 10.sp, fontWeight = if (isCombos) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AueraaNeonCyan,
                        indicatorColor = AueraaRoyalPurple,
                        unselectedIconColor = AueraaTextSecondary,
                        unselectedTextColor = AueraaTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_combos")
                )

                NavigationBarItem(
                    selected = isPackages,
                    onClick = { currentScreen = Screen.Packages },
                    icon = { Icon(if (isPackages) Icons.Filled.Inventory2 else Icons.Outlined.Inventory2, contentDescription = "Packages") },
                    label = { Text("Packages", fontSize = 10.sp, fontWeight = if (isPackages) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AueraaNeonCyan,
                        indicatorColor = AueraaElectricBlue,
                        unselectedIconColor = AueraaTextSecondary,
                        unselectedTextColor = AueraaTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_packages")
                )

                NavigationBarItem(
                    selected = isPortal,
                    onClick = {
                        currentScreen = when {
                            currentUser?.role == "ADMIN" -> Screen.AdminDashboard
                            currentUser?.role == "CLIENT" -> Screen.ClientPortal
                            else -> {
                                authInitialTab = 0
                                Screen.Auth
                            }
                        }
                    },
                    icon = { Icon(if (isPortal) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle, contentDescription = "Portal") },
                    label = {
                        Text(
                            text = when (currentUser?.role) {
                                "ADMIN" -> "Admin HQ"
                                "CLIENT" -> "My Portal"
                                else -> "Sign In"
                            },
                            fontSize = 10.sp,
                            fontWeight = if (isPortal) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = AueraaNeonCyan,
                        indicatorColor = if (currentUser?.role == "ADMIN") AueraaRoyalPurple else AueraaElectricBlue,
                        unselectedIconColor = AueraaTextSecondary,
                        unselectedTextColor = AueraaTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_portal")
                )
            }
        },
        floatingActionButton = {
            // Floating WhatsApp Button
            FloatingWhatsAppButton(
                whatsappNumber = settings?.whatsappNumber ?: "+919876543210",
                prefilledMessage = "Hi Aueraa.Digital! I'd like to consult with your digital agency team.",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Home -> {
                    HomeScreen(
                        settings = settings,
                        services = services,
                        featuredPackages = packages,
                        featuredCombos = combos,
                        featuredPortfolio = portfolio,
                        onNavigateToServices = { catKey ->
                            selectedServiceCategory = catKey
                            currentScreen = Screen.Services
                        },
                        onNavigateToPackages = { currentScreen = Screen.Packages },
                        onNavigateToCombos = { currentScreen = Screen.Combos },
                        onNavigateToPortfolio = { currentScreen = Screen.Portfolio },
                        onNavigateToEnquiry = { pkgName ->
                            prefilledPackageName = pkgName
                            currentScreen = Screen.Enquiry
                        },
                        onNavigateToSetupWizard = { currentScreen = Screen.SetupWizard },
                        onSwitchToAdmin = {
                            if (currentUser?.role == "ADMIN") {
                                currentScreen = Screen.AdminDashboard
                            } else {
                                authInitialTab = 2 // Admin HQ Gate: No unauthorized access!
                                currentScreen = Screen.Auth
                            }
                        },
                        onSwitchToClient = {
                            if (currentUser?.role == "CLIENT") {
                                currentScreen = Screen.ClientPortal
                            } else {
                                authInitialTab = 0
                                currentScreen = Screen.Auth
                            }
                        },
                        onNavigateToAnalytics = {
                            currentScreen = Screen.CampaignAnalytics
                        }
                    )
                }

                Screen.Services -> {
                    ServicesScreen(
                        services = services,
                        packages = packages,
                        portfolio = portfolio,
                        initialCategoryKey = selectedServiceCategory,
                        onEnquireForService = { serviceName ->
                            prefilledPackageName = serviceName
                            currentScreen = Screen.Enquiry
                        },
                        onSelectPackage = { pkgName ->
                            prefilledPackageName = pkgName
                            currentScreen = Screen.Enquiry
                        }
                    )
                }

                Screen.Packages -> {
                    PackagesScreen(
                        packages = packages,
                        isAdmin = currentUser?.role == "ADMIN",
                        onSavePackage = { pkg -> viewModel.savePackage(pkg) },
                        onSelectPackageForEnquiry = { pkgName ->
                            prefilledPackageName = pkgName
                            currentScreen = Screen.Enquiry
                        }
                    )
                }

                Screen.Combos -> {
                    ComboPackagesScreen(
                        combos = combos,
                        availablePackages = packages,
                        onRequestComboQuote = { comboName, _, _ ->
                            prefilledPackageName = comboName
                            currentScreen = Screen.Enquiry
                        }
                    )
                }

                Screen.Portfolio -> {
                    PortfolioScreen(
                        portfolio = portfolio,
                        onSelectProjectForInquiry = { cat ->
                            selectedServiceCategory = cat
                            currentScreen = Screen.Enquiry
                        }
                    )
                }

                Screen.AboutContact -> {
                    AboutContactScreen(
                        settings = settings,
                        contactChannels = contactChannels,
                        onOpenEnquiry = { currentScreen = Screen.Enquiry }
                    )
                }

                Screen.Enquiry -> {
                    EnquiryFormScreen(
                        prefilledPackageName = prefilledPackageName,
                        prefilledServiceCategory = selectedServiceCategory,
                        onSubmitEnquiry = { clientName, businessName, phone, whatsapp, email, cat, serv, selPkg, isCombo, budget, deadline, refs, desc, reqs, onSuccess ->
                            viewModel.submitEnquiry(
                                clientName, businessName, phone, whatsapp, email, cat, serv, selPkg, isCombo, budget, deadline, refs, desc, reqs, onSuccess
                            )
                        },
                        onBack = { currentScreen = Screen.Home }
                    )
                }

                Screen.Auth -> {
                    AuthScreen(
                        viewModel = viewModel,
                        initialTab = authInitialTab,
                        onLoginSuccess = {
                            currentScreen = if (currentUser?.role == "ADMIN") {
                                Screen.AdminDashboard
                            } else {
                                Screen.ClientPortal
                            }
                        }
                    )
                }

                Screen.ClientPortal -> {
                    val client = currentUser
                    if (client != null && client.role == "CLIENT") {
                        ClientPortalScreen(
                            viewModel = viewModel,
                            clientUser = client,
                            onLogout = {
                                viewModel.logout()
                                authInitialTab = 0
                                currentScreen = Screen.Auth
                            },
                            onNavigateToServices = { currentScreen = Screen.Services },
                            onNavigateToPackages = { currentScreen = Screen.Packages },
                            onNavigateToEnquiry = { currentScreen = Screen.Enquiry }
                        )
                    } else {
                        authInitialTab = 0
                        AuthScreen(
                            viewModel = viewModel,
                            initialTab = 0,
                            onLoginSuccess = { currentScreen = Screen.ClientPortal }
                        )
                    }
                }

                Screen.AdminDashboard -> {
                    val admin = currentUser
                    if (admin != null && admin.role == "ADMIN") {
                        AdminDashboardScreen(
                            viewModel = viewModel,
                            onNavigateToGenerator = { currentScreen = Screen.PackageGenerator },
                            onNavigateToSetupWizard = { currentScreen = Screen.SetupWizard },
                            onNavigateToAnalytics = { currentScreen = Screen.CampaignAnalytics },
                            onLogout = {
                                viewModel.logout()
                                currentScreen = Screen.Home
                            }
                        )
                    } else {
                        // Unauthorized users are strictly redirected to Admin Login/Register with Security Key!
                        authInitialTab = 2
                        currentScreen = Screen.Auth
                    }
                }

                Screen.CampaignAnalytics -> {
                    CampaignAnalyticsScreen(
                        viewModel = viewModel,
                        onBack = {
                            currentScreen = if (currentUser?.role == "ADMIN") Screen.AdminDashboard else Screen.Home
                        }
                    )
                }

                Screen.PackageGenerator -> {
                    PackageGeneratorScreen(
                        viewModel = viewModel,
                        onBack = { currentScreen = Screen.AdminDashboard }
                    )
                }

                Screen.SetupWizard -> {
                    SetupWizardScreen(
                        viewModel = viewModel,
                        onComplete = {
                            currentScreen = if (currentUser?.role == "ADMIN") Screen.AdminDashboard else Screen.Home
                        }
                    )
                }
            }
        }
    }
}
