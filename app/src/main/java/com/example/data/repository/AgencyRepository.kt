package com.example.data.repository

import com.example.data.dao.*
import com.example.data.database.AppDatabase
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgencyRepository(private val database: AppDatabase) {

    private val agencyDao: AgencyDao = database.agencyDao()
    private val serviceDao: ServiceDao = database.serviceDao()
    private val packageDao: PackageDao = database.packageDao()
    private val comboDao: ComboPackageDao = database.comboPackageDao()
    private val portfolioDao: PortfolioDao = database.portfolioDao()
    private val enquiryDao: EnquiryDao = database.enquiryDao()
    private val userDao: ClientUserDao = database.clientUserDao()
    private val projectDao: ClientProjectDao = database.clientProjectDao()
    private val milestoneDao: MilestoneDao = database.milestoneDao()
    private val invoiceDao: InvoiceDao = database.invoiceDao()
    private val auditDao: AuditLogDao = database.auditLogDao()
    private val contactDao: ContactChannelDao = database.contactChannelDao()
    private val campaignDao: CampaignDao = database.campaignDao()

    // Current Session State
    private val _currentUser = MutableStateFlow<ClientUserEntity?>(null)
    val currentUser = _currentUser.asStateFlow()

    // 1. Settings
    val settingsFlow: Flow<AgencySettings?> = agencyDao.getSettingsFlow()
    suspend fun getSettings(): AgencySettings? = agencyDao.getSettings()
    suspend fun updateSettings(settings: AgencySettings) {
        agencyDao.insertOrUpdate(settings)
        auditDao.insertLog(
            AuditLogEntity(
                action = "UPDATE_SETTINGS",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Agency profile & contact details updated"
            )
        )
    }

    // 2. Services
    val allServices: Flow<List<ServiceEntity>> = serviceDao.getAllServices()
    fun getServicesByCategory(categoryKey: String): Flow<List<ServiceEntity>> =
        serviceDao.getServicesByCategory(categoryKey)
    suspend fun getServiceById(id: String): ServiceEntity? = serviceDao.getServiceById(id)

    // 3. Packages
    val activePackages: Flow<List<PackageEntity>> = packageDao.getActivePackages()
    val allPackages: Flow<List<PackageEntity>> = packageDao.getAllPackages()
    fun getPackagesByCategory(category: String): Flow<List<PackageEntity>> =
        packageDao.getPackagesByCategory(category)
    suspend fun getPackageById(id: String): PackageEntity? = packageDao.getPackageById(id)

    suspend fun savePackage(pkg: PackageEntity) {
        packageDao.insertPackage(pkg)
        auditDao.insertLog(
            AuditLogEntity(
                action = "SAVE_PACKAGE",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Package '${pkg.name}' (ID: ${pkg.id}) saved with price ₹${pkg.priceInr}"
            )
        )
    }

    suspend fun updatePackage(pkg: PackageEntity) {
        packageDao.updatePackage(pkg)
        auditDao.insertLog(
            AuditLogEntity(
                action = "UPDATE_PACKAGE",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Package '${pkg.name}' updated"
            )
        )
    }

    suspend fun deletePackage(id: String) {
        packageDao.deletePackageById(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "DELETE_PACKAGE",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Package ID $id deleted"
            )
        )
    }

    suspend fun duplicatePackage(sourceId: String) {
        val original = packageDao.getPackageById(sourceId) ?: return
        val newId = "PKG-${System.currentTimeMillis() % 100000}"
        val copy = original.copy(
            id = newId,
            name = "${original.name} (Copy)",
            status = "DRAFT",
            createdTimestamp = System.currentTimeMillis(),
            updatedTimestamp = System.currentTimeMillis()
        )
        savePackage(copy)
    }

    // 4. Combo Packages
    val activeCombos: Flow<List<ComboPackageEntity>> = comboDao.getActiveCombos()
    val allCombos: Flow<List<ComboPackageEntity>> = comboDao.getAllCombos()
    suspend fun saveCombo(combo: ComboPackageEntity) = comboDao.insertCombo(combo)
    suspend fun updateCombo(combo: ComboPackageEntity) = comboDao.updateCombo(combo)
    suspend fun deleteCombo(combo: ComboPackageEntity) = comboDao.deleteCombo(combo)

    // 5. Portfolio
    val publicPortfolio: Flow<List<PortfolioEntity>> = portfolioDao.getPublicProjects()
    val allPortfolio: Flow<List<PortfolioEntity>> = portfolioDao.getAllProjects()
    suspend fun savePortfolioProject(item: PortfolioEntity) = portfolioDao.insertProject(item)
    suspend fun updatePortfolioProject(item: PortfolioEntity) = portfolioDao.updateProject(item)
    suspend fun deletePortfolioProject(id: String) = portfolioDao.deleteProjectById(id)

    // 6. Enquiries & CRM
    val allEnquiries: Flow<List<EnquiryEntity>> = enquiryDao.getAllEnquiries()
    fun getEnquiriesForClient(clientEmail: String): Flow<List<EnquiryEntity>> =
        enquiryDao.getEnquiriesByClientEmail(clientEmail)

    suspend fun submitEnquiry(
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
        additionalRequirements: String
    ): String {
        val datePrefix = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
        val randomSuffix = (100..999).random()
        val enquiryId = "AUERAA-ENQ-$datePrefix-$randomSuffix"

        val entity = EnquiryEntity(
            id = enquiryId,
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
            additionalRequirements = additionalRequirements,
            status = "NEW"
        )
        enquiryDao.insertEnquiry(entity)

        auditDao.insertLog(
            AuditLogEntity(
                action = "NEW_ENQUIRY",
                performedBy = email,
                details = "Enquiry $enquiryId received from $clientName ($businessName)"
            )
        )
        return enquiryId
    }

    suspend fun updateEnquiryStatus(
        enquiryId: String,
        status: String,
        followUpDate: String? = null,
        internalNotes: String? = null,
        lostReason: String? = null
    ) {
        val existing = enquiryDao.getEnquiryById(enquiryId) ?: return
        val updated = existing.copy(
            status = status,
            followUpDate = followUpDate ?: existing.followUpDate,
            internalNotes = internalNotes ?: existing.internalNotes,
            lostReason = lostReason ?: existing.lostReason
        )
        enquiryDao.updateEnquiry(updated)
    }

    suspend fun convertEnquiryToProject(enquiryId: String): String? {
        val enquiry = enquiryDao.getEnquiryById(enquiryId) ?: return null
        val projectId = "PRJ-${SimpleDateFormat("yyMM", Locale.US).format(Date())}-${(100..999).random()}"
        val project = ClientProjectEntity(
            id = projectId,
            clientId = enquiry.email,
            clientName = enquiry.businessName.ifBlank { enquiry.clientName },
            projectName = "${enquiry.selectedPackageName} for ${enquiry.businessName}",
            serviceCategory = enquiry.serviceCategory,
            status = "PLANNING",
            startDate = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date()),
            targetCompletionDate = enquiry.deadline,
            progressPercent = 10,
            deliverablesDeliveredCount = 0,
            deliverablesTotalCount = 8
        )
        projectDao.insertProject(project)

        // Seed initial milestones
        val ms1 = ProjectMilestoneEntity(
            id = "MS-${projectId}-1",
            projectId = projectId,
            title = "Discovery & Strategy Kickoff",
            description = "Requirements signoff and visual moodboard",
            dueDate = "Kickoff + 3 Days",
            isCompleted = false,
            orderIndex = 1
        )
        val ms2 = ProjectMilestoneEntity(
            id = "MS-${projectId}-2",
            projectId = projectId,
            title = "Initial Deliverables & Review",
            description = "First batch of deliverables staged for client review",
            dueDate = "Kickoff + 10 Days",
            isCompleted = false,
            orderIndex = 2
        )
        milestoneDao.insertMilestones(listOf(ms1, ms2))

        updateEnquiryStatus(enquiryId, "CONVERTED", internalNotes = "Converted to Project $projectId")
        return projectId
    }

    // 7. Client & Project Management
    val allProjects: Flow<List<ClientProjectEntity>> = projectDao.getAllProjects()
    fun getProjectsForClient(clientId: String): Flow<List<ClientProjectEntity>> =
        projectDao.getProjectsByClientId(clientId)
    fun getMilestonesForProject(projectId: String): Flow<List<ProjectMilestoneEntity>> =
        milestoneDao.getMilestonesForProject(projectId)

    suspend fun updateProjectProgress(
        projectId: String,
        progressPercent: Int,
        status: String,
        deliveredCount: Int,
        totalCount: Int
    ) {
        val project = projectDao.getProjectById(projectId) ?: return
        projectDao.updateProject(
            project.copy(
                progressPercent = progressPercent,
                status = status,
                deliverablesDeliveredCount = deliveredCount,
                deliverablesTotalCount = totalCount
            )
        )
    }

    suspend fun toggleMilestone(milestone: ProjectMilestoneEntity) {
        milestoneDao.updateMilestone(milestone.copy(isCompleted = !milestone.isCompleted))
    }

    suspend fun submitRevisionRequest(projectId: String, notes: String) {
        val project = projectDao.getProjectById(projectId) ?: return
        projectDao.updateProject(
            project.copy(
                status = "REVISIONS",
                revisionRequestNotes = notes
            )
        )
        auditDao.insertLog(
            AuditLogEntity(
                action = "REVISION_REQUEST",
                performedBy = _currentUser.value?.email ?: "Client",
                details = "Revision requested for $projectId: $notes"
            )
        )
    }

    suspend fun approveProject(projectId: String) {
        val project = projectDao.getProjectById(projectId) ?: return
        projectDao.updateProject(
            project.copy(
                status = "COMPLETED",
                isApprovedByClient = true,
                progressPercent = 100
            )
        )
    }

    // 8. Invoicing & Billing
    val allInvoices: Flow<List<InvoiceEntity>> = invoiceDao.getAllInvoices()
    fun getInvoicesForClient(clientId: String): Flow<List<InvoiceEntity>> =
        invoiceDao.getInvoicesForClient(clientId)

    suspend fun createInvoice(
        clientId: String,
        clientName: String,
        projectName: String,
        itemsDescription: String,
        subtotalInr: Long,
        advancePaidInr: Long = 0,
        dueDate: String = "In 7 Days"
    ): String {
        val invId = "INV-AUERAA-${SimpleDateFormat("yyMM", Locale.US).format(Date())}-${(100..999).random()}"
        val taxAmount = (subtotalInr * 0.18).toLong()
        val total = subtotalInr + taxAmount
        val balance = total - advancePaidInr
        val status = when {
            balance <= 0 -> "PAID"
            advancePaidInr > 0 -> "PARTIALLY_PAID"
            else -> "PENDING"
        }
        val invoice = InvoiceEntity(
            id = invId,
            clientId = clientId,
            clientName = clientName,
            projectName = projectName,
            itemsDescription = itemsDescription,
            subtotalInr = subtotalInr,
            gstPercent = 18.0,
            taxAmountInr = taxAmount,
            totalAmountInr = total,
            advancePaidInr = advancePaidInr,
            balanceDueInr = balance,
            status = status,
            dueDate = dueDate
        )
        invoiceDao.insertInvoice(invoice)
        return invId
    }

    suspend fun recordPayment(invoiceId: String, amountPaid: Long, paymentMethod: String) {
        val invoice = invoiceDao.getInvoiceById(invoiceId) ?: return
        val newAdvance = invoice.advancePaidInr + amountPaid
        val newBalance = (invoice.totalAmountInr - newAdvance).coerceAtLeast(0)
        val newStatus = if (newBalance == 0L) "PAID" else "PARTIALLY_PAID"
        invoiceDao.updateInvoice(
            invoice.copy(
                advancePaidInr = newAdvance,
                balanceDueInr = newBalance,
                status = newStatus,
                paymentDate = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date()),
                paymentMethod = paymentMethod
            )
        )
    }

    // 9. Users & Authentication
    val allUsers: Flow<List<ClientUserEntity>> = userDao.getAllUsers()

    suspend fun login(email: String, password: String):Result<ClientUserEntity> {
        val user = userDao.getUserByEmail(email.trim())
        return if (user != null && user.passwordHash == password.trim()) {
            _currentUser.value = user
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    suspend fun register(
        email: String,
        password: String,
        fullName: String,
        companyName: String,
        phone: String,
        whatsapp: String,
        businessCategory: String
    ): Result<ClientUserEntity> {
        val existing = userDao.getUserByEmail(email.trim())
        if (existing != null) {
            return Result.failure(Exception("Account already exists with this email"))
        }
        val newUser = ClientUserEntity(
            id = email.trim(),
            email = email.trim(),
            passwordHash = password.trim(),
            fullName = fullName.trim(),
            companyName = companyName.trim(),
            phone = phone.trim(),
            whatsapp = whatsapp.trim().ifBlank { phone.trim() },
            businessCategory = businessCategory.trim(),
            role = "CLIENT"
        )
        userDao.insertUser(newUser)
        _currentUser.value = newUser
        return Result.success(newUser)
    }

    suspend fun registerAdmin(
        email: String,
        password: String,
        fullName: String,
        department: String,
        phone: String,
        adminSecretKey: String
    ): Result<ClientUserEntity> {
        val masterKey = "AUERAA-ADMIN-2026"
        if (adminSecretKey.trim() != masterKey) {
            auditDao.insertLog(
                AuditLogEntity(
                    action = "UNAUTHORIZED_ADMIN_REGISTRATION_ATTEMPT",
                    performedBy = email.trim(),
                    details = "Failed admin registration attempt with invalid passcode: ${adminSecretKey.take(4)}***"
                )
            )
            return Result.failure(Exception("Unauthorized: Invalid Agency Admin Security Passcode. Contact AUERAA.DIGITAL leadership."))
        }

        val existing = userDao.getUserByEmail(email.trim())
        if (existing != null) {
            return Result.failure(Exception("Admin account already registered with email: ${email.trim()}"))
        }

        val newAdmin = ClientUserEntity(
            id = email.trim(),
            email = email.trim(),
            passwordHash = password.trim(),
            fullName = fullName.trim(),
            companyName = "AUERAA.DIGITAL ($department)",
            phone = phone.trim(),
            whatsapp = phone.trim(),
            businessCategory = "Digital Agency - $department",
            role = "ADMIN"
        )
        userDao.insertUser(newAdmin)
        _currentUser.value = newAdmin
        auditDao.insertLog(
            AuditLogEntity(
                action = "ADMIN_REGISTERED",
                performedBy = email.trim(),
                details = "New Administrator registered: $fullName ($department)"
            )
        )
        return Result.success(newAdmin)
    }

    suspend fun loginWithSocial(
        provider: String, // "GOOGLE", "LINKEDIN", "SOCIAL"
        email: String,
        fullName: String,
        targetRole: String = "CLIENT",
        adminPasscode: String? = null
    ): Result<ClientUserEntity> {
        val cleanEmail = email.trim().lowercase()
        val existing = userDao.getUserByEmail(cleanEmail)

        if (targetRole == "ADMIN") {
            if (existing != null && existing.role == "ADMIN") {
                _currentUser.value = existing
                auditDao.insertLog(
                    AuditLogEntity(
                        action = "SOCIAL_LOGIN_ADMIN",
                        performedBy = cleanEmail,
                        details = "Admin logged in via $provider"
                    )
                )
                return Result.success(existing)
            } else {
                // Registering/Accessing Admin with Social requires security passcode
                val masterKey = "AUERAA-ADMIN-2026"
                if (adminPasscode?.trim() != masterKey) {
                    return Result.failure(Exception("Admin verification required: Enter valid Agency Security Passcode to authorize $provider account as Admin."))
                }
                val adminUser = ClientUserEntity(
                    id = cleanEmail,
                    email = cleanEmail,
                    passwordHash = "social_auth_$provider",
                    fullName = fullName.ifBlank { "Executive Administrator" },
                    companyName = "AUERAA.DIGITAL Management",
                    phone = "+91 98765 43210",
                    whatsapp = "+919876543210",
                    businessCategory = "Agency Executive ($provider)",
                    role = "ADMIN"
                )
                userDao.insertUser(adminUser)
                _currentUser.value = adminUser
                auditDao.insertLog(
                    AuditLogEntity(
                        action = "SOCIAL_ADMIN_REGISTERED",
                        performedBy = cleanEmail,
                        details = "New Administrator verified via $provider"
                    )
                )
                return Result.success(adminUser)
            }
        } else {
            // Client Social Login
            val clientUser = existing ?: ClientUserEntity(
                id = cleanEmail,
                email = cleanEmail,
                passwordHash = "social_auth_$provider",
                fullName = fullName.ifBlank { "Client Partner" },
                companyName = "Client Organization",
                phone = "+91 98000 00000",
                whatsapp = "+91 98000 00000",
                businessCategory = "Digital Growth Client",
                role = "CLIENT"
            ).also { userDao.insertUser(it) }

            _currentUser.value = clientUser
            auditDao.insertLog(
                AuditLogEntity(
                    action = "SOCIAL_LOGIN_CLIENT",
                    performedBy = cleanEmail,
                    details = "Client logged in via $provider"
                )
            )
            return Result.success(clientUser)
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun setQuickUser(user: ClientUserEntity) {
        _currentUser.value = user
    }

    // 10. Audit Logs
    val recentAuditLogs: Flow<List<AuditLogEntity>> = auditDao.getRecentLogs()

    // 11. Contact Channels Directory (Add, Edit, Delete)
    val allContacts: Flow<List<ContactChannelEntity>> = contactDao.getAllContacts()

    suspend fun saveContact(contact: ContactChannelEntity) {
        contactDao.insertContact(contact)
        auditDao.insertLog(
            AuditLogEntity(
                action = "SAVE_CONTACT",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Contact '${contact.title}' (${contact.channelType}: ${contact.value}) saved"
            )
        )
    }

    suspend fun deleteContact(id: String) {
        contactDao.deleteContactById(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "DELETE_CONTACT",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Contact ID $id deleted"
            )
        )
    }

    // 12. Marketing Campaigns & Analytics
    val allCampaigns: Flow<List<MarketingCampaignEntity>> = campaignDao.getAllCampaigns()

    suspend fun saveCampaign(campaign: MarketingCampaignEntity) {
        campaignDao.insertCampaign(campaign)
        auditDao.insertLog(
            AuditLogEntity(
                action = "SAVE_CAMPAIGN",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Campaign '${campaign.campaignName}' (${campaign.platform}) saved. Spend: ₹${campaign.adSpendInr}"
            )
        )
    }

    suspend fun deleteCampaign(id: String) {
        campaignDao.deleteCampaignById(id)
        auditDao.insertLog(
            AuditLogEntity(
                action = "DELETE_CAMPAIGN",
                performedBy = _currentUser.value?.email ?: "Admin",
                details = "Campaign ID $id removed"
            )
        )
    }
}
