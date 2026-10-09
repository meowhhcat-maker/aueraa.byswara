package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AgencyRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PackageGeneratorState(
    val step: Int = 1,
    val category: String = "Content Creation",
    val packageName: String = "",
    val targetCustomer: String = "",
    val clientBusinessCategory: String = "",
    val mainGoal: String = "",
    val expectedBudgetInr: Long = 30000,
    val billingFrequency: String = "Monthly",
    val includedServices: List<String> = listOf("Post Designs", "Reels Video Editing", "Captions & Hashtags"),
    val deliverablesQuantities: String = "15 Posts, 6 Reels, 15 Stories",
    val deliveryTimeline: String = "Monthly Retainer",
    val revisionsAndSupport: String = "3 Revisions per asset, 30-Day support",
    val thirdPartyFeeConditions: String = "Ad spend paid directly by client to platforms",
    val recommendedAddons: String = "On-site 4K video shoot day, Influencer outreach",
    val specialConditions: String = "Client provides brand logo and raw high-res images",
    val finalPriceInr: Long = 29999,
    val generatedDraft: PackageEntity? = null
)

class AgencyViewModel(private val repository: AgencyRepository) : ViewModel() {

    // Current User Session
    val currentUser: StateFlow<ClientUserEntity?> = repository.currentUser

    // Agency Settings
    val agencySettings: StateFlow<AgencySettings?> = repository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Services
    val services: StateFlow<List<ServiceEntity>> = repository.allServices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Packages
    val activePackages: StateFlow<List<PackageEntity>> = repository.activePackages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPackages: StateFlow<List<PackageEntity>> = repository.allPackages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combo Packages
    val activeCombos: StateFlow<List<ComboPackageEntity>> = repository.activeCombos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allCombos: StateFlow<List<ComboPackageEntity>> = repository.allCombos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Portfolio
    val publicPortfolio: StateFlow<List<PortfolioEntity>> = repository.publicPortfolio
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPortfolio: StateFlow<List<PortfolioEntity>> = repository.allPortfolio
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Enquiries / CRM
    val allEnquiries: StateFlow<List<EnquiryEntity>> = repository.allEnquiries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Projects & Milestones
    val allProjects: StateFlow<List<ClientProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allInvoices: StateFlow<List<InvoiceEntity>> = repository.allInvoices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLogEntity>> = repository.recentAuditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contactChannels: StateFlow<List<ContactChannelEntity>> = repository.allContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val campaigns: StateFlow<List<MarketingCampaignEntity>> = repository.allCampaigns
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI & Active Form States
    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage = _uiMessage.asSharedFlow()

    // Package Generator Active Form State
    private val _generatorState = MutableStateFlow(PackageGeneratorState())
    val generatorState = _generatorState.asStateFlow()

    // 1. Settings / Setup Wizard
    fun updateAgencySettings(settings: AgencySettings) {
        viewModelScope.launch {
            repository.updateSettings(settings)
            _uiMessage.emit("Agency settings saved successfully!")
        }
    }

    // 2. Package Generator (15-Question Engine)
    fun updateGeneratorField(update: PackageGeneratorState.() -> PackageGeneratorState) {
        _generatorState.value = _generatorState.value.update()
    }

    fun generatePackageDraft() {
        val s = _generatorState.value
        val uniqueId = "PKG-${s.category.take(3).uppercase()}-${System.currentTimeMillis() % 10000}"

        val draft = PackageEntity(
            id = uniqueId,
            name = s.packageName.ifBlank { "${s.category} Professional Plan" },
            category = s.category,
            targetCustomer = s.targetCustomer.ifBlank { "Modern growing businesses & brands" },
            clientBusinessCategory = s.clientBusinessCategory.ifBlank { "Multi-Industry" },
            mainGoal = s.mainGoal.ifBlank { "Customer acquisition and authority building" },
            packageType = s.billingFrequency,
            description = "AI-assisted custom package proposal engineered specifically for ${s.clientBusinessCategory.ifBlank { "your business" }} to achieve ${s.mainGoal.ifBlank { "exponential digital growth" }}.",
            deliverables = s.includedServices.joinToString("\n") + "\nQuantities: " + s.deliverablesQuantities,
            quantitiesSummary = s.deliverablesQuantities,
            priceInr = s.finalPriceInr,
            isCustomQuoteRequired = s.finalPriceInr <= 0,
            currency = "INR",
            billingFrequency = s.billingFrequency,
            taxPercentage = 18.0,
            deliveryDuration = s.deliveryTimeline,
            revisionsPolicy = s.revisionsAndSupport,
            supportPeriod = "30 Days post-delivery support",
            includedServices = s.includedServices.joinToString("\n"),
            excludedServices = s.thirdPartyFeeConditions,
            recommendedAddons = s.recommendedAddons,
            status = "DRAFT", // AI generated is always Draft until reviewed & approved
            isAiGeneratedDraft = true,
            createdTimestamp = System.currentTimeMillis(),
            updatedTimestamp = System.currentTimeMillis()
        )
        _generatorState.value = _generatorState.value.copy(generatedDraft = draft)
    }

    fun approveAndPublishGeneratedPackage(draft: PackageEntity) {
        viewModelScope.launch {
            val approved = draft.copy(
                status = "ACTIVE",
                isAiGeneratedDraft = false,
                updatedTimestamp = System.currentTimeMillis()
            )
            repository.savePackage(approved)
            _generatorState.value = PackageGeneratorState() // reset
            _uiMessage.emit("Package '${approved.name}' approved and published live!")
        }
    }

    fun savePackage(pkg: PackageEntity) {
        viewModelScope.launch {
            repository.savePackage(pkg)
            _uiMessage.emit("Package saved successfully.")
        }
    }

    fun deletePackage(id: String) {
        viewModelScope.launch {
            repository.deletePackage(id)
            _uiMessage.emit("Package removed.")
        }
    }

    fun duplicatePackage(id: String) {
        viewModelScope.launch {
            repository.duplicatePackage(id)
            _uiMessage.emit("Package duplicated as draft.")
        }
    }

    // 3. Combos
    fun saveCombo(combo: ComboPackageEntity) {
        viewModelScope.launch {
            repository.saveCombo(combo)
            _uiMessage.emit("Combo package saved!")
        }
    }

    fun deleteCombo(combo: ComboPackageEntity) {
        viewModelScope.launch {
            repository.deleteCombo(combo)
            _uiMessage.emit("Combo package deleted.")
        }
    }

    // 4. Portfolio
    fun savePortfolio(item: PortfolioEntity) {
        viewModelScope.launch {
            repository.savePortfolioProject(item)
            _uiMessage.emit("Portfolio project saved!")
        }
    }

    fun deletePortfolio(id: String) {
        viewModelScope.launch {
            repository.deletePortfolioProject(id)
            _uiMessage.emit("Portfolio project removed.")
        }
    }

    // 5. Enquiries
    fun submitEnquiry(
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
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val refId = repository.submitEnquiry(
                clientName = clientName,
                businessName = businessName,
                contactNumber = contactNumber,
                whatsappNumber = whatsappNumber,
                email = email,
                businessCategory = businessCategory,
                serviceCategory = serviceCategory,
                selectedPackageName = selectedPackageName,
                isComboRequest = isComboRequest,
                expectedBudgetInr = expectedBudgetInr,
                deadline = deadline,
                referenceLinks = referenceLinks,
                projectDescription = projectDescription,
                additionalRequirements = additionalRequirements
            )
            onSuccess(refId)
            _uiMessage.emit("Enquiry submitted! Ref: $refId")
        }
    }

    fun updateEnquiryStatus(
        enquiryId: String,
        status: String,
        followUpDate: String? = null,
        internalNotes: String? = null,
        lostReason: String? = null
    ) {
        viewModelScope.launch {
            repository.updateEnquiryStatus(enquiryId, status, followUpDate, internalNotes, lostReason)
            _uiMessage.emit("Enquiry status updated to $status")
        }
    }

    fun convertEnquiryToProject(enquiryId: String) {
        viewModelScope.launch {
            val projectId = repository.convertEnquiryToProject(enquiryId)
            if (projectId != null) {
                _uiMessage.emit("Converted to Project $projectId successfully!")
            }
        }
    }

    // 6. Project & Milestones
    fun updateProjectProgress(
        projectId: String,
        progressPercent: Int,
        status: String,
        deliveredCount: Int,
        totalCount: Int
    ) {
        viewModelScope.launch {
            repository.updateProjectProgress(projectId, progressPercent, status, deliveredCount, totalCount)
            _uiMessage.emit("Project updated successfully.")
        }
    }

    fun toggleMilestone(milestone: ProjectMilestoneEntity) {
        viewModelScope.launch {
            repository.toggleMilestone(milestone)
        }
    }

    fun submitRevision(projectId: String, notes: String) {
        viewModelScope.launch {
            repository.submitRevisionRequest(projectId, notes)
            _uiMessage.emit("Revision request submitted to agency!")
        }
    }

    fun approveProject(projectId: String) {
        viewModelScope.launch {
            repository.approveProject(projectId)
            _uiMessage.emit("Project approved! Deliverables finalized.")
        }
    }

    // 7. Invoices
    fun createInvoice(
        clientId: String,
        clientName: String,
        projectName: String,
        itemsDescription: String,
        subtotalInr: Long,
        advancePaidInr: Long = 0,
        dueDate: String = "In 7 Days"
    ) {
        viewModelScope.launch {
            val invId = repository.createInvoice(
                clientId, clientName, projectName, itemsDescription, subtotalInr, advancePaidInr, dueDate
            )
            _uiMessage.emit("Invoice $invId generated.")
        }
    }

    fun recordPayment(invoiceId: String, amount: Long, method: String) {
        viewModelScope.launch {
            repository.recordPayment(invoiceId, amount, method)
            _uiMessage.emit("Payment of ₹$amount recorded for $invoiceId")
        }
    }

    // 8. Auth
    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val res = repository.login(email, pass)
            if (res.isSuccess) {
                onSuccess()
                _uiMessage.emit("Welcome, ${res.getOrNull()?.fullName}!")
            } else {
                onError(res.exceptionOrNull()?.message ?: "Login failed")
            }
        }
    }

    fun register(
        email: String,
        pass: String,
        fullName: String,
        companyName: String,
        phone: String,
        whatsapp: String,
        category: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val res = repository.register(email, pass, fullName, companyName, phone, whatsapp, category)
            if (res.isSuccess) {
                onSuccess()
                _uiMessage.emit("Account created! Welcome, $fullName")
            } else {
                onError(res.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    fun switchToAdmin() {
        viewModelScope.launch {
            val adminUser = repository.login("admin@aueraa.digital", "admin123").getOrNull()
            if (adminUser != null) {
                repository.setQuickUser(adminUser)
                _uiMessage.emit("Switched to Admin Portal (Aueraa Agency Director)")
            }
        }
    }

    fun switchToClient() {
        viewModelScope.launch {
            val clientUser = repository.login("client@startup.in", "client123").getOrNull()
            if (clientUser != null) {
                repository.setQuickUser(clientUser)
                _uiMessage.emit("Switched to Client Portal (Verma Artisan Roasters)")
            }
        }
    }

    fun getMilestonesForProject(projectId: String): Flow<List<ProjectMilestoneEntity>> =
        repository.getMilestonesForProject(projectId)

    // Contact Details Operations (Add, Edit, Delete)
    fun saveContact(contact: ContactChannelEntity) {
        viewModelScope.launch {
            repository.saveContact(contact)
            _uiMessage.emit("Contact channel '${contact.title}' saved!")
        }
    }

    fun deleteContact(id: String) {
        viewModelScope.launch {
            repository.deleteContact(id)
            _uiMessage.emit("Contact channel removed.")
        }
    }

    // Campaign Performance & Analytics Operations
    fun saveCampaign(campaign: MarketingCampaignEntity) {
        viewModelScope.launch {
            repository.saveCampaign(campaign)
            _uiMessage.emit("Campaign '${campaign.campaignName}' updated!")
        }
    }

    fun deleteCampaign(id: String) {
        viewModelScope.launch {
            repository.deleteCampaign(id)
            _uiMessage.emit("Campaign removed.")
        }
    }

    // Admin Secure Registration Feature
    fun registerAdmin(
        email: String,
        password: String,
        fullName: String,
        department: String,
        phone: String,
        adminSecretKey: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.registerAdmin(email, password, fullName, department, phone, adminSecretKey)
            result.fold(
                onSuccess = { admin ->
                    _uiMessage.emit("Admin access granted: Welcome ${admin.fullName}!")
                    onSuccess()
                },
                onFailure = { err ->
                    onError(err.message ?: "Admin registration failed.")
                }
            )
        }
    }

    // Social Media Login Options (Google, LinkedIn, Social)
    fun loginWithSocial(
        provider: String,
        email: String,
        fullName: String,
        targetRole: String = "CLIENT",
        adminPasscode: String? = null,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = repository.loginWithSocial(provider, email, fullName, targetRole, adminPasscode)
            result.fold(
                onSuccess = { user ->
                    _uiMessage.emit("Signed in successfully via $provider as ${user.fullName}!")
                    onSuccess()
                },
                onFailure = { err ->
                    onError(err.message ?: "$provider login failed.")
                }
            )
        }
    }
}

class AgencyViewModelFactory(private val repository: AgencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AgencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AgencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
