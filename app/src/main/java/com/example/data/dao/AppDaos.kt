package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AgencySettings
import com.example.data.model.AuditLogEntity
import com.example.data.model.ClientProjectEntity
import com.example.data.model.ClientUserEntity
import com.example.data.model.ComboPackageEntity
import com.example.data.model.ContactChannelEntity
import com.example.data.model.EnquiryEntity
import com.example.data.model.InvoiceEntity
import com.example.data.model.MarketingCampaignEntity
import com.example.data.model.PackageEntity
import com.example.data.model.PortfolioEntity
import com.example.data.model.ProjectMilestoneEntity
import com.example.data.model.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgencyDao {
    @Query("SELECT * FROM agency_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<AgencySettings?>

    @Query("SELECT * FROM agency_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): AgencySettings?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: AgencySettings)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY startingPriceInr ASC")
    fun getAllServices(): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE categoryKey = :categoryKey")
    fun getServicesByCategory(categoryKey: String): Flow<List<ServiceEntity>>

    @Query("SELECT * FROM services WHERE id = :id LIMIT 1")
    suspend fun getServiceById(id: String): ServiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceEntity)
}

@Dao
interface PackageDao {
    @Query("SELECT * FROM packages WHERE status = 'ACTIVE' ORDER BY priceInr ASC")
    fun getActivePackages(): Flow<List<PackageEntity>>

    @Query("SELECT * FROM packages ORDER BY updatedTimestamp DESC")
    fun getAllPackages(): Flow<List<PackageEntity>>

    @Query("SELECT * FROM packages WHERE category = :category AND status = 'ACTIVE'")
    fun getPackagesByCategory(category: String): Flow<List<PackageEntity>>

    @Query("SELECT * FROM packages WHERE id = :id LIMIT 1")
    suspend fun getPackageById(id: String): PackageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(pkg: PackageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackages(pkgs: List<PackageEntity>)

    @Update
    suspend fun updatePackage(pkg: PackageEntity)

    @Query("DELETE FROM packages WHERE id = :id")
    suspend fun deletePackageById(id: String)
}

@Dao
interface ComboPackageDao {
    @Query("SELECT * FROM combo_packages WHERE isActive = 1 ORDER BY finalPriceInr ASC")
    fun getActiveCombos(): Flow<List<ComboPackageEntity>>

    @Query("SELECT * FROM combo_packages ORDER BY createdTimestamp DESC")
    fun getAllCombos(): Flow<List<ComboPackageEntity>>

    @Query("SELECT * FROM combo_packages WHERE id = :id LIMIT 1")
    suspend fun getComboById(id: String): ComboPackageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCombo(combo: ComboPackageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCombos(combos: List<ComboPackageEntity>)

    @Update
    suspend fun updateCombo(combo: ComboPackageEntity)

    @Delete
    suspend fun deleteCombo(combo: ComboPackageEntity)
}

@Dao
interface PortfolioDao {
    @Query("SELECT * FROM portfolio_projects WHERE isPublic = 1 ORDER BY isFeatured DESC, createdTimestamp DESC")
    fun getPublicProjects(): Flow<List<PortfolioEntity>>

    @Query("SELECT * FROM portfolio_projects ORDER BY createdTimestamp DESC")
    fun getAllProjects(): Flow<List<PortfolioEntity>>

    @Query("SELECT * FROM portfolio_projects WHERE category = :category AND isPublic = 1")
    fun getProjectsByCategory(category: String): Flow<List<PortfolioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: PortfolioEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<PortfolioEntity>)

    @Update
    suspend fun updateProject(project: PortfolioEntity)

    @Query("DELETE FROM portfolio_projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)
}

@Dao
interface EnquiryDao {
    @Query("SELECT * FROM enquiries ORDER BY createdTimestamp DESC")
    fun getAllEnquiries(): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE status = :status ORDER BY createdTimestamp DESC")
    fun getEnquiriesByStatus(status: String): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE email = :clientEmail ORDER BY createdTimestamp DESC")
    fun getEnquiriesByClientEmail(clientEmail: String): Flow<List<EnquiryEntity>>

    @Query("SELECT * FROM enquiries WHERE id = :id LIMIT 1")
    suspend fun getEnquiryById(id: String): EnquiryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnquiry(enquiry: EnquiryEntity)

    @Update
    suspend fun updateEnquiry(enquiry: EnquiryEntity)

    @Query("DELETE FROM enquiries WHERE id = :id")
    suspend fun deleteEnquiryById(id: String)
}

@Dao
interface ClientUserDao {
    @Query("SELECT * FROM client_users ORDER BY createdTimestamp DESC")
    fun getAllUsers(): Flow<List<ClientUserEntity>>

    @Query("SELECT * FROM client_users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): ClientUserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: ClientUserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<ClientUserEntity>)

    @Update
    suspend fun updateUser(user: ClientUserEntity)
}

@Dao
interface ClientProjectDao {
    @Query("SELECT * FROM client_projects ORDER BY createdTimestamp DESC")
    fun getAllProjects(): Flow<List<ClientProjectEntity>>

    @Query("SELECT * FROM client_projects WHERE clientId = :clientId ORDER BY createdTimestamp DESC")
    fun getProjectsByClientId(clientId: String): Flow<List<ClientProjectEntity>>

    @Query("SELECT * FROM client_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ClientProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ClientProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ClientProjectEntity>)

    @Update
    suspend fun updateProject(project: ClientProjectEntity)
}

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM project_milestones WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getMilestonesForProject(projectId: String): Flow<List<ProjectMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestones(milestones: List<ProjectMilestoneEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilestone(milestone: ProjectMilestoneEntity)

    @Update
    suspend fun updateMilestone(milestone: ProjectMilestoneEntity)
}

@Dao
interface InvoiceDao {
    @Query("SELECT * FROM invoices ORDER BY createdTimestamp DESC")
    fun getAllInvoices(): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE clientId = :clientId ORDER BY createdTimestamp DESC")
    fun getInvoicesForClient(clientId: String): Flow<List<InvoiceEntity>>

    @Query("SELECT * FROM invoices WHERE id = :id LIMIT 1")
    suspend fun getInvoiceById(id: String): InvoiceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoice(invoice: InvoiceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvoices(invoices: List<InvoiceEntity>)

    @Update
    suspend fun updateInvoice(invoice: InvoiceEntity)
}

@Dao
interface AuditLogDao {
    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<AuditLogEntity>>

    @Insert
    suspend fun insertLog(log: AuditLogEntity)
}

@Dao
interface ContactChannelDao {
    @Query("SELECT * FROM contact_channels ORDER BY isPrimary DESC, orderIndex ASC")
    fun getAllContacts(): Flow<List<ContactChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactChannelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactChannelEntity>)

    @Update
    suspend fun updateContact(contact: ContactChannelEntity)

    @Query("DELETE FROM contact_channels WHERE id = :id")
    suspend fun deleteContactById(id: String)
}

@Dao
interface CampaignDao {
    @Query("SELECT * FROM marketing_campaigns ORDER BY createdTimestamp DESC")
    fun getAllCampaigns(): Flow<List<MarketingCampaignEntity>>

    @Query("SELECT * FROM marketing_campaigns WHERE status = :status ORDER BY createdTimestamp DESC")
    fun getCampaignsByStatus(status: String): Flow<List<MarketingCampaignEntity>>

    @Query("SELECT * FROM marketing_campaigns WHERE id = :id LIMIT 1")
    suspend fun getCampaignById(id: String): MarketingCampaignEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: MarketingCampaignEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaigns(campaigns: List<MarketingCampaignEntity>)

    @Update
    suspend fun updateCampaign(campaign: MarketingCampaignEntity)

    @Query("DELETE FROM marketing_campaigns WHERE id = :id")
    suspend fun deleteCampaignById(id: String)
}

