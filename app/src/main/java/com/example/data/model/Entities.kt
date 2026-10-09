package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agency_settings")
data class AgencySettings(
    @PrimaryKey val id: Int = 1,
    val agencyName: String = "AUERAA.DIGITAL",
    val tagline: String = "Creative Content. Strategic Marketing. Powerful Digital Solutions.",
    val introduction: String = "Aueraa.Digital is an elite creative and technology agency transforming brands through high-impact content, hyper-targeted digital marketing, modern websites, and scalable mobile applications.",
    val phone: String = "+91 98765 43210",
    val whatsappNumber: String = "+919876543210",
    val businessEmail: String = "contact@aueraa.digital",
    val instagramUrl: String = "https://instagram.com/aueraa.digital",
    val facebookUrl: String = "https://facebook.com/aueraa.digital",
    val linkedinUrl: String = "https://linkedin.com/company/aueraa-digital",
    val youtubeUrl: String = "https://youtube.com/@aueraa.digital",
    val websiteUrl: String = "https://aueraa.digital",
    val businessAddress: String = "Tech Hub Tower, Silicon Lane, Bangalore & Cyber City, Hyderabad, India",
    val currency: String = "INR (₹)",
    val gstNumber: String = "36AAACA1234F1Z5",
    val paymentDetails: String = "UPI: pay@aueraa | Bank: AUERAA DIGITAL PVT LTD, HDFC Bank, A/C: 50200088991122, IFSC: HDFC0001234",
    val businessHours: String = "Monday – Saturday: 9:00 AM – 7:30 PM IST",
    val termsAndConditions: String = "1. Advance payment of 50% is required before project commencement.\n2. Revisions are restricted to the approved scope.\n3. Content and assets must be provided in high resolution.",
    val privacyPolicy: String = "AUERAA.DIGITAL strictly safeguards all client assets, confidential business strategies, and marketing analytics.",
    val refundPolicy: String = "Milestone-based refund policy: Cancellations prior to discovery allow 80% refund; once production commences, work delivered is non-refundable.",
    val primaryColorHex: String = "#0080FF",
    val secondaryColorHex: String = "#7C3AED",
    val isSetupCompleted: Boolean = true
)

@Entity(tableName = "services")
data class ServiceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val categoryKey: String, // CONTENT_CREATION, DIGITAL_MARKETING, WEBSITE_DEV, APP_DEV
    val shortDescription: String,
    val fullDescription: String,
    val deliverablesList: String, // newline-separated
    val benefitsList: String, // newline-separated
    val startingPriceInr: Long,
    val iconKey: String,
    val badgeText: String,
    val isPopular: Boolean = false
)

@Entity(tableName = "packages")
data class PackageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "Content Creation", "Digital Marketing", "Website Development", "Application Development", "Combo Package"
    val targetCustomer: String,
    val clientBusinessCategory: String,
    val mainGoal: String,
    val packageType: String, // "Monthly", "One-time", "Annual", "Custom"
    val description: String,
    val deliverables: String, // newline-separated or formatted
    val quantitiesSummary: String, // e.g. "15 Posts, 8 Reels, 4 Stories"
    val priceInr: Long,
    val isCustomQuoteRequired: Boolean = false,
    val currency: String = "INR",
    val billingFrequency: String = "Monthly", // "Monthly", "One-time", "Quarterly", "Annual"
    val taxPercentage: Double = 18.0,
    val deliveryDuration: String = "15-20 Days",
    val paymentSchedule: String = "50% Advance, 50% on Final Delivery",
    val revisionsPolicy: String = "3 rounds of structured revisions included",
    val supportPeriod: String = "30 Days post-launch warranty & support",
    val includedServices: String, // newline-separated
    val excludedServices: String, // e.g. "Paid advertising spend, custom 3D shoot location fee"
    val recommendedAddons: String, // e.g. "Influencer Collab Add-on, Drone shoot"
    val status: String = "ACTIVE", // "ACTIVE", "DRAFT", "ARCHIVED"
    val isAiGeneratedDraft: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "combo_packages")
data class ComboPackageEntity(
    @PrimaryKey val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val targetAudience: String,
    val includedServiceCategories: String, // newline-separated
    val includedDeliverables: String, // newline-separated
    val originalTotalInr: Long,
    val discountPercentage: Double = 15.0,
    val finalPriceInr: Long,
    val billingFrequency: String = "Monthly",
    val turnaroundTime: String = "2-3 Weeks",
    val isTemplate: Boolean = true,
    val isActive: Boolean = true,
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "portfolio_projects")
data class PortfolioEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val clientName: String,
    val clientPrivacyConsent: Boolean = true,
    val description: String,
    val deliverablesText: String,
    val metricsResult: String,
    val techStack: String,
    val liveUrl: String = "",
    val relatedPackageId: String? = null,
    val isFeatured: Boolean = false,
    val isPublic: Boolean = true,
    val beforeAfterSummary: String? = null,
    val heroTag: String = "BRAND TRANSFORMATION",
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "enquiries")
data class EnquiryEntity(
    @PrimaryKey val id: String, // e.g. "AUERAA-ENQ-101"
    val clientName: String,
    val businessName: String,
    val contactNumber: String,
    val whatsappNumber: String,
    val email: String,
    val businessCategory: String,
    val serviceCategory: String,
    val selectedPackageName: String,
    val isComboRequest: Boolean = false,
    val expectedBudgetInr: Long = 0,
    val deadline: String = "Flexible",
    val referenceLinks: String = "",
    val projectDescription: String,
    val additionalRequirements: String = "",
    val status: String = "NEW", // "NEW", "CONTACTED", "PROPOSAL_SENT", "CONVERTED", "LOST"
    val followUpDate: String? = null,
    val internalNotes: String = "",
    val lostReason: String? = null,
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "client_users")
data class ClientUserEntity(
    @PrimaryKey val id: String, // email or generated ID
    val email: String,
    val passwordHash: String = "pass123",
    val fullName: String,
    val companyName: String,
    val phone: String,
    val whatsapp: String,
    val businessCategory: String,
    val role: String = "CLIENT", // "CLIENT", "ADMIN"
    val isOwner: Boolean = false,
    val status: String = "ACTIVE",
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "client_projects")
data class ClientProjectEntity(
    @PrimaryKey val id: String, // e.g. "PRJ-2026-001"
    val clientId: String, // foreign reference to client_users.id or email
    val clientName: String,
    val projectName: String,
    val serviceCategory: String,
    val packageId: String? = null,
    val status: String = "IN_PROGRESS", // "PLANNING", "IN_PROGRESS", "REVIEW", "REVISIONS", "COMPLETED"
    val startDate: String,
    val targetCompletionDate: String,
    val progressPercent: Int = 45,
    val deliverablesDeliveredCount: Int = 3,
    val deliverablesTotalCount: Int = 8,
    val deliverableDownloadUrl: String? = "https://drive.google.com/aueraa-assets-drive",
    val revisionRequestNotes: String? = null,
    val isApprovedByClient: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "project_milestones")
data class ProjectMilestoneEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val orderIndex: Int = 1
)

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String, // e.g. "INV-AUERAA-2026-101"
    val clientId: String,
    val clientName: String,
    val projectName: String,
    val itemsDescription: String,
    val subtotalInr: Long,
    val gstPercent: Double = 18.0,
    val taxAmountInr: Long,
    val totalAmountInr: Long,
    val advancePaidInr: Long,
    val balanceDueInr: Long,
    val status: String = "PENDING", // "PENDING", "PARTIALLY_PAID", "PAID", "OVERDUE"
    val dueDate: String,
    val paymentDate: String? = null,
    val paymentMethod: String? = "UPI / NEFT",
    val createdTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "audit_logs")
data class AuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val action: String,
    val performedBy: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "contact_channels")
data class ContactChannelEntity(
    @PrimaryKey val id: String,
    val title: String, // e.g. "Primary WhatsApp Concierge", "Client Support Hotline", "Bangalore Tech Desk"
    val channelType: String, // "WHATSAPP", "PHONE", "EMAIL", "ADDRESS", "SOCIAL", "OTHER"
    val value: String, // e.g. "+91 98765 43210", "contact@aueraa.digital"
    val subtitle: String = "",
    val isPrimary: Boolean = false,
    val orderIndex: Int = 0
)

@Entity(tableName = "marketing_campaigns")
data class MarketingCampaignEntity(
    @PrimaryKey val id: String, // e.g. "CMP-2026-001"
    val campaignName: String,
    val clientName: String,
    val platform: String, // "META_ADS", "GOOGLE_ADS", "LINKEDIN_ADS", "YOUTUBE", "MULTI_CHANNEL"
    val status: String = "ACTIVE", // "ACTIVE", "PAUSED", "COMPLETED"
    val targetObjective: String = "CONVERSIONS", // "LEAD_GENERATION", "CONVERSIONS", "BRAND_AWARENESS", "SALES"
    val startDate: String,
    val endDate: String,
    val totalBudgetInr: Long,
    val adSpendInr: Long,
    val revenueGeneratedInr: Long,
    val roas: Double, // Revenue / Ad Spend e.g. 4.8
    val conversionRate: Double, // e.g. 4.85 (%)
    val totalImpressions: Long,
    val totalClicks: Long,
    val totalConversions: Long,
    val ctr: Double, // % e.g. 4.15
    val cpaInr: Long, // Cost Per Acquisition e.g. 162
    val cpcInr: Double, // Cost Per Click e.g. 7.88
    val createdTimestamp: Long = System.currentTimeMillis()
)

data class MetricDataPoint(
    val label: String, // "Mon", "Tue", ...
    val dateText: String, // "02 Oct", ...
    val conversionRate: Double, // e.g. 3.8 (%)
    val adSpendInr: Long, // e.g. 12500
    val revenueInr: Long, // e.g. 57500
    val roas: Double, // e.g. 4.6
    val impressions: Long,
    val clicks: Long,
    val conversions: Long
)

