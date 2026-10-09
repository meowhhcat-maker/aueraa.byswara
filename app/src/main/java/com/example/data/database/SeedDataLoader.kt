package com.example.data.database

import com.example.data.model.*

object SeedDataLoader {
    suspend fun populateDatabase(database: AppDatabase) {
        val agencyDao = database.agencyDao()
        val serviceDao = database.serviceDao()
        val packageDao = database.packageDao()
        val comboDao = database.comboPackageDao()
        val portfolioDao = database.portfolioDao()
        val enquiryDao = database.enquiryDao()
        val userDao = database.clientUserDao()
        val projectDao = database.clientProjectDao()
        val milestoneDao = database.milestoneDao()
        val invoiceDao = database.invoiceDao()
        val auditDao = database.auditLogDao()
        val contactDao = database.contactChannelDao()
        val campaignDao = database.campaignDao()

        // 1. Agency Settings
        agencyDao.insertOrUpdate(
            AgencySettings(
                id = 1,
                agencyName = "AUERAA.DIGITAL",
                tagline = "Creative Content. Strategic Marketing. Powerful Digital Solutions.",
                introduction = "Aueraa.Digital is a full-spectrum creative and technology agency. We design scroll-stopping content, deploy high-ROI advertising campaigns, craft responsive modern websites, and architect mission-critical mobile and cloud applications.",
                phone = "+91 98765 43210",
                whatsappNumber = "+919876543210",
                businessEmail = "contact@aueraa.digital",
                instagramUrl = "https://instagram.com/aueraa.digital",
                facebookUrl = "https://facebook.com/aueraa.digital",
                linkedinUrl = "https://linkedin.com/company/aueraa-digital",
                youtubeUrl = "https://youtube.com/@aueraa.digital",
                websiteUrl = "https://aueraa.digital",
                businessAddress = "Level 8, Silicon Horizon Towers, Outer Ring Road, Bangalore - 560103, India",
                currency = "INR (₹)",
                gstNumber = "29AAACA9921D1ZB",
                paymentDetails = "UPI: aueraa.digital@hdfcbank | NEFT: Aueraa Digital Pvt Ltd, HDFC Bank, A/C: 50200088991122, IFSC: HDFC0001234",
                businessHours = "Monday to Saturday: 9:30 AM – 7:00 PM IST",
                termsAndConditions = "1. Quotation validity is 15 business days.\n2. 50% mobilization advance required for project kickoff.\n3. Advertising spend is paid directly to Meta/Google ad networks.\n4. Deliverables include specified revisions; scope additions are billed separately.",
                privacyPolicy = "AUERAA.DIGITAL respects enterprise privacy. All client media, source code, data, and credentials remain 100% confidential under signed mutual NDAs.",
                refundPolicy = "Deposit is refundable up to 48 hours prior to creative production kickoff. Pro-rated milestone disbursements apply for ongoing retainers.",
                primaryColorHex = "#0080FF",
                secondaryColorHex = "#7C3AED",
                isSetupCompleted = true
            )
        )

        // 2. Services
        val services = listOf(
            ServiceEntity(
                id = "content_creation",
                title = "Content Creation",
                categoryKey = "CONTENT_CREATION",
                shortDescription = "High-impact visual storytelling: Reels, 4K video editing, commercial photography & carousel campaigns.",
                fullDescription = "We create high-converting content designed to capture attention and build authority. From cinematic Instagram Reels and YouTube Shorts to studio-grade product shoots, resort visuals, and ad creatives.",
                deliverablesList = "Social Media Post Designs\nInstagram Reels & 4K Video Editing\nCommercial Product Photography\nRestaurant & Food Video Shoots\nResort & Hospitality Virtual Tours\nHigh-CTR Carousel Graphics & Stories\nTargeted Ad Creatives & Motion Graphics\nCaptions, Viral Scripts & Hashtag Intelligence\nMonthly Structured Content Calendars\nUGC-Style Creator Videos",
                benefitsList = "3x higher engagement than generic stock posts\nConsistent visual identity across all platforms\nFull commercial copyright ownership\nAlgorithm-optimized 9:16 and 1:1 formats\nFast turnaround with dedicated content directors",
                startingPriceInr = 24999,
                iconKey = "content",
                badgeText = "CREATIVE ESSENTIAL",
                isPopular = true
            ),
            ServiceEntity(
                id = "digital_marketing",
                title = "Digital Marketing",
                categoryKey = "DIGITAL_MARKETING",
                shortDescription = "ROI-driven performance marketing: Meta & Google Ads, SEO, Local Maps & Lead Funnels.",
                fullDescription = "Turn attention into paying customers. We build data-backed customer acquisition systems combining precision Meta Ads, Google Search & PMax campaigns, technical SEO, and conversion rate optimization.",
                deliverablesList = "End-to-End Social Media Management (IG & FB)\nMeta Ads (Facebook & Instagram Ads Management)\nGoogle Search, Display & Performance Max Ads\nHigh-Conversion Lead Generation Funnels\nTechnical SEO & High-Intent Keyword Ranking\nLocal SEO & Google Business Profile 5-Star Domination\nInfluencer Marketing Coordination\nPerformance Analytics Dashboards & Weekly Reports\nLifecycle Email Marketing & WhatsApp Automation\nContinuous Creative A/B Testing & Bid Optimization",
                benefitsList = "Ad spend paid directly to platforms—transparent management fees\nProven ROAS (Return On Ad Spend) track record\nReal-time lead alerts directly to your phone/CRM\nZero vanity metrics—focus on qualified revenue pipeline\nDedicated media buyer and copy strategist",
                startingPriceInr = 29999,
                iconKey = "marketing",
                badgeText = "HIGH ROI",
                isPopular = true
            ),
            ServiceEntity(
                id = "website_development",
                title = "Website Development",
                categoryKey = "WEBSITE_DEV",
                shortDescription = "Ultra-fast modern websites, high-converting landing pages, e-commerce stores & portals.",
                fullDescription = "Your digital storefront must look extraordinary and convert relentlessly. We build lightweight, ultra-responsive web applications and websites engineered for speed, SEO, and seamless user experiences.",
                deliverablesList = "Custom Responsive Corporate & Business Websites\nHigh-Converting Landing Pages for Ad Campaigns\nInteractive Portfolio & Agency Showcase Sites\nRestaurant, Café & Hospitality Menus with Online Booking\nResort, Villa & Hotel Booking Enquiries & Tours\nFull E-Commerce Web Stores (Shopify / Next.js / WooCommerce)\nCustom-Coded Web Apps (React, Next.js, Node.js, Tailwind)\nSEO-Friendly Architecture & Core Web Vitals < 1s\nDomain Setup, Cloudflare CDN & Secure Cloud Hosting\nContact Forms, WhatsApp Click-to-Chat & Payment Gateway Integration\nQuarterly Maintenance, Security Patches & Backups",
                benefitsList = "Lightning-fast sub-second loading speeds\n100% Mobile & Tablet responsive perfection\nZero vendor lock-in with clean documented code\nBuilt-in SEO structure for organic Google search rank\nEasy content management for your internal team",
                startingPriceInr = 34999,
                iconKey = "website",
                badgeText = "ENTERPRISE GRADE",
                isPopular = true
            ),
            ServiceEntity(
                id = "application_development",
                title = "Application Development",
                categoryKey = "APP_DEV",
                shortDescription = "Native Android & iOS apps, custom client portals, booking systems & internal CRMs.",
                fullDescription = "Empower your business operations and customer retention with bespoke software. We engineer scalable Android and iOS mobile applications, PWAs, customer portals, and internal workflow management dashboards.",
                deliverablesList = "Native Android Applications (Kotlin & Jetpack Compose)\nCross-Platform iOS & Android Mobile Apps\nSecure Customer Portals & User Dashboard Systems\nService Booking & Appointment Scheduling Engines\nBespoke E-Commerce & Catalog Ordering Apps\nInternal Inventory & Warehouse Tracking Systems\nCustom CRM & Lead Pipeline Dashboards\nEducational Institution & Student Portals\nREST & GraphQL API Architecture & Cloud Databases\nRole-Based Auth (Google Sign-In, OTP, Email)\nContinuous App Store / Play Store Release Support",
                benefitsList = "Smooth 60/120 FPS native performance\nOffline-first architecture with automatic sync\nEnterprise data privacy and military-grade encryption\nScalable infrastructure ready for millions of requests\nComplete source code ownership & deployment handover",
                startingPriceInr = 59999,
                iconKey = "app",
                badgeText = "CUSTOM TECH",
                isPopular = false
            )
        )
        serviceDao.insertServices(services)

        // 3. Packages
        val packages = listOf(
            PackageEntity(
                id = "PKG-CC-STARTER",
                name = "Social Content Starter",
                category = "Content Creation",
                targetCustomer = "Local Businesses, Cafés, Startups & Emerging Brands",
                clientBusinessCategory = "F&B, Retail, Professional Services",
                mainGoal = "Build steady aesthetic social media presence and engagement",
                packageType = "Monthly",
                description = "Consistent monthly content pack with professionally designed static posts, story designs, and curated reels to keep your profile active and attractive.",
                deliverables = "12 Custom Feed Graphics (Posts & Carousels)\n4 High-Definition Instagram Reels (up to 30s)\n12 Branded Story Creatives with Interactive Stickers\nCurated Captions with High-Rank Hashtag Clusters\nMonthly Editorial Content Calendar",
                quantitiesSummary = "12 Posts, 4 Reels, 12 Stories",
                priceInr = 24999,
                isCustomQuoteRequired = false,
                currency = "INR",
                billingFrequency = "Monthly",
                taxPercentage = 18.0,
                deliveryDuration = "Monthly Retainer",
                paymentSchedule = "50% Advance at month start, 50% at mid-month delivery",
                revisionsPolicy = "2 rounds of revisions per asset",
                supportPeriod = "Ongoing monthly support",
                includedServices = "Graphic Design\nVideo Editing\nContent Strategy\nCaption Writing",
                excludedServices = "On-site physical camera crew shoot fees\nPaid influencer collaboration fees",
                recommendedAddons = "On-location 1-Day Video Shoot: ₹14,999\nExtra Reel Pack (4 Reels): ₹8,999",
                status = "ACTIVE",
                isAiGeneratedDraft = false
            ),
            PackageEntity(
                id = "PKG-CC-CINEMATIC",
                name = "Cinematic Brand Creator Pro",
                category = "Content Creation",
                targetCustomer = "Luxury Resorts, Villas, Fine Dining & Lifestyle Brands",
                clientBusinessCategory = "Hospitality, Real Estate, Fashion & Luxury",
                mainGoal = "Showcase prestige, boost bookings, and achieve viral organic reach",
                packageType = "Monthly",
                description = "High-end content production featuring on-site 4K video capture, drone cinematography, professional color grading, and UGC creator integrations.",
                deliverables = "1 Dedicated On-Site Production Shoot Day / Month\n8 Cinematic 4K Instagram Reels & YouTube Shorts\n16 Curated High-End Feed Posts & Carousels\n20 Interactive Story Creatives\nProfessional Color Grading & Sound Design\nCustom Motion Graphics & Logo Animations\nFull Raw 4K B-Roll Asset Archive for Brand Use",
                quantitiesSummary = "1 Shoot Day, 8 Reels, 16 Posts, 20 Stories",
                priceInr = 54999,
                isCustomQuoteRequired = false,
                currency = "INR",
                billingFrequency = "Monthly",
                taxPercentage = 18.0,
                deliveryDuration = "Monthly Retainer (Delivered Bi-Weekly)",
                paymentSchedule = "60% Mobilization Advance, 40% on Delivery",
                revisionsPolicy = "3 rounds of revisions per video edit",
                supportPeriod = "Priority WhatsApp Creative Director channel",
                includedServices = "Camera Crew & 4K Gear\nDrone Videography (where permitted)\nPost-Production & Editing\nContent Calendar & Distribution Plan",
                excludedServices = "Outstation travel & lodging expenses (if outside city)",
                recommendedAddons = "Celebrity Influencer Visit Coordination: ₹25,000\nFull Brand Documentary Film (2-3 min): ₹35,000",
                status = "ACTIVE",
                isAiGeneratedDraft = false
            ),
            PackageEntity(
                id = "PKG-DM-LEADGEN",
                name = "Meta & Google Performance Engine",
                category = "Digital Marketing",
                targetCustomer = "B2B, Healthcare, Real Estate, Education & Services",
                clientBusinessCategory = "Service Businesses, Education, Clinics & Real Estate",
                mainGoal = "Generate high-intent qualified leads and customer appointments",
                packageType = "Monthly",
                description = "Full-funnel digital marketing engine setting up Meta & Google ad campaigns, landing page lead tracking, conversion optimization, and CRM routing.",
                deliverables = "Meta Ads Campaign Setup & Management (FB & IG)\nGoogle Search & High-Intent Keyword Campaign\nLead Magnet Strategy & WhatsApp API Integration\nWeekly Ad Creative Refresh (8 New Ad Graphics + 4 Video Ads)\nRetargeting Funnels for Web Visitors\nAutomated Lead Notification System to Phone/Email\nWeekly Performance Reporting & ROAS Optimization",
                quantitiesSummary = "Up to 5 Active Campaigns, 12 Ad Creatives / mo",
                priceInr = 34999,
                isCustomQuoteRequired = false,
                currency = "INR",
                billingFrequency = "Monthly",
                taxPercentage = 18.0,
                deliveryDuration = "Continuous Monthly Retainer",
                paymentSchedule = "100% Management Retainer advance each 30-day cycle",
                revisionsPolicy = "Unlimited ad creative iterations within budget",
                supportPeriod = "Dedicated Senior Media Buyer on Slack/WhatsApp",
                includedServices = "Ad Strategy & Account Setup\nAd Creative Production\nAudience Research & A/B Testing\nBi-weekly Strategy Review Call",
                excludedServices = "Direct Ad Spend paid directly to Meta & Google (billed to client's card)",
                recommendedAddons = "Landing Page Custom Build: ₹19,999\nAutomated CRM Lead Routing Setup: ₹9,999",
                status = "ACTIVE",
                isAiGeneratedDraft = false
            ),
            PackageEntity(
                id = "PKG-WEB-GROWTH",
                name = "Modern Business Website & Booking Suite",
                category = "Website Development",
                targetCustomer = "SMEs, Boutique Hotels, Clinics, Consultancies & Modern Brands",
                clientBusinessCategory = "Corporate, Hospitality, Medical, Consultancies",
                mainGoal = "Establish credibility, capture inquiries, and rank on local Google search",
                packageType = "One-time",
                description = "Ultra-fast, fully responsive 6-8 page custom website engineered with modern styling, lightning-fast loading, WhatsApp integration, and SEO fundamentals.",
                deliverables = "6 to 8 Bespoke Custom Responsive Pages\nInteractive Booking / Inquiry Form with Instant WhatsApp Sync\nMobile-First Performance (< 1s Load Speed)\nOn-Page SEO Optimization & Google Analytics 4 Setup\nSSL Certificate, Cloudflare CDN & Domain DNS Configuration\nCMS Integration for Easy Blog / Team / Service Updates\nInteractive Google Maps & Contact Direct Dialing",
                quantitiesSummary = "Up to 8 Pages, Full Mobile + Tablet + Desktop Responsive",
                priceInr = 44999,
                isCustomQuoteRequired = false,
                currency = "INR",
                billingFrequency = "One-time",
                taxPercentage = 18.0,
                deliveryDuration = "14 - 18 Business Days",
                paymentSchedule = "50% Advance, 30% Design Approval, 20% on Domain Launch",
                revisionsPolicy = "3 comprehensive revision cycles during staging",
                supportPeriod = "60 Days post-launch bug warranty & training",
                includedServices = "UI/UX Figma Mockup\nFrontend & Backend Code\nSpeed Optimization\nCMS Training Session",
                excludedServices = "Third-party annual domain registration & cloud hosting fees (~₹3,000/yr)",
                recommendedAddons = "E-Commerce Gateway & Cart Expansion: ₹18,000\nMulti-Language Localization: ₹10,000",
                status = "ACTIVE",
                isAiGeneratedDraft = false
            ),
            PackageEntity(
                id = "PKG-APP-CUSTOM",
                name = "Full-Stack Mobile App & Management Portal",
                category = "Application Development",
                targetCustomer = "Startups, E-commerce, Booking Platforms, Internal Field Ops",
                clientBusinessCategory = "Tech, Logistics, Health, On-Demand Services",
                mainGoal = "Scale customer retention and automate internal operations",
                packageType = "One-time",
                description = "Production-grade native Android & cross-platform app paired with a secure cloud admin dashboard, push notifications, user authentication, and database.",
                deliverables = "Native Android Application (Kotlin & Jetpack Compose)\nWeb Admin Dashboard for Orders / Users / Inventory\nCloud Database & Secure Authentication Engine\nPush Notification Integration (FCM)\nPayment Gateway Checkout (Razorpay / Stripe)\nUser Profile, History & Booking Management\nPlay Store Release Preparation & Submission Handover",
                quantitiesSummary = "12-16 App Screens + Web Admin Dashboard",
                priceInr = 89999,
                isCustomQuoteRequired = false,
                currency = "INR",
                billingFrequency = "One-time",
                taxPercentage = 18.0,
                deliveryDuration = "4 - 6 Weeks",
                paymentSchedule = "40% Kickoff, 30% Beta Milestone, 30% Launch Handover",
                revisionsPolicy = "Structured milestone sign-offs + 2 sprint polish cycles",
                supportPeriod = "90 Days post-launch technical warranty",
                includedServices = "System Architecture\nUI/UX Design\nApp Engineering\nCloud API Development\nStore Deployment",
                excludedServices = "Google Play / Apple Developer Account fees ($25 / $99 per year)",
                recommendedAddons = "iOS Native Build Expansion: ₹39,999\nAutomated AI WhatsApp Assistant Bot: ₹24,999",
                status = "ACTIVE",
                isAiGeneratedDraft = false
            )
        )
        packageDao.insertPackages(packages)

        // 4. Combo Packages (Requested in Section 6)
        val comboPackages = listOf(
            ComboPackageEntity(
                id = "COMBO-GROWTH-01",
                name = "Social Media Growth Combo",
                subtitle = "Content Creation + Social Management + Monthly Reporting",
                description = "Our most popular package for busy founders: we produce all your reels and graphics, handle the daily posting and community engagement, and deliver detailed monthly analytics.",
                targetAudience = "Cafés, Fashion Labels, Startups, Creators & Local Businesses",
                includedServiceCategories = "Content Creation\nDigital Marketing\nAnalytics & Reporting",
                includedDeliverables = "16 High-Design Feed Posts\n6 Cinematic Instagram Reels\nDaily Story Management\nEngagement & Comment Monitoring\nMonthly ROI & Reach Performance Report",
                originalTotalInr = 49998,
                discountPercentage = 16.0,
                finalPriceInr = 41999,
                billingFrequency = "Monthly",
                turnaroundTime = "Continuous Monthly Retainer",
                isTemplate = true,
                isActive = true
            ),
            ComboPackageEntity(
                id = "COMBO-LAUNCH-02",
                name = "Business Launch Combo",
                subtitle = "Branding + Modern Website + Social Content + Ad Setup",
                description = "Everything needed to launch your new venture with maximum authority: brand identity, lightning-fast website, launch social content, and initial lead generation ad campaigns.",
                targetAudience = "New Startups, Emerging Companies & Rebranding Businesses",
                includedServiceCategories = "Website Development\nContent Creation\nDigital Marketing\nBrand Identity",
                includedDeliverables = "Complete Logo & Brand Identity Style Guide\nCustom 6-Page High-Speed Responsive Website\nLaunch Pack of 15 Social Posts & 4 Video Reels\nMeta & Google Ads Campaign Setup for Instant Leads\nGoogle Business Profile Verification & Local SEO",
                originalTotalInr = 94998,
                discountPercentage = 21.0,
                finalPriceInr = 74999,
                billingFrequency = "One-time + 1st Month Support",
                turnaroundTime = "3 - 4 Weeks",
                isTemplate = true,
                isActive = true
            ),
            ComboPackageEntity(
                id = "COMBO-REST-03",
                name = "Restaurant & Hospitality Growth Combo",
                subtitle = "Food Photography + Menu Website + Local SEO + Meta Ads",
                description = "Drive packed tables and online orders. Includes professional food videography, digital mobile-menu website, Google Maps 5-star optimization, and hyper-local Instagram ad campaigns.",
                targetAudience = "Restaurants, Cafés, Cloud Kitchens, Lounges & Bars",
                includedServiceCategories = "Content Creation\nWebsite Development\nDigital Marketing\nLocal SEO",
                includedDeliverables = "1 Dedicated Food & Ambience Shoot / Month\n8 Mouthwatering Instagram Reels\nMobile QR Digital Menu & Table Booking Website\nLocal Google Maps Top-3 Ranking Strategy\nHyper-Local Meta Ads targeting diners within 10km radius",
                originalTotalInr = 69998,
                discountPercentage = 18.0,
                finalPriceInr = 56999,
                billingFrequency = "Monthly Retainer",
                turnaroundTime = "Ongoing Retainer",
                isTemplate = true,
                isActive = true
            ),
            ComboPackageEntity(
                id = "COMBO-RESORT-04",
                name = "Resort & Villa Marketing Combo",
                subtitle = "Drone & 4K Shoot + Booking Portal + Influencer Ads",
                description = "Transform your luxury resort or private villa into an Instagram sensation with breathtaking drone footage, high-converting booking enquiry pages, and national weekend getaway campaigns.",
                targetAudience = "Resorts, Private Luxury Villas, Boutique Hotels & Homestays",
                includedServiceCategories = "Content Creation\nWebsite Development\nDigital Marketing",
                includedDeliverables = "4K Cinematic Drone & Interior Architectural Shoot\n10 Viral Reels showcasing experience & amenities\nDirect Booking & Inquiry Engine Website\nMeta Ad Campaigns targeting high-income weekend travelers\nDirect WhatsApp Concierge Integration",
                originalTotalInr = 99999,
                discountPercentage = 15.0,
                finalPriceInr = 84999,
                billingFrequency = "Monthly Retainer",
                turnaroundTime = "Ongoing Retainer",
                isTemplate = true,
                isActive = true
            ),
            ComboPackageEntity(
                id = "COMBO-ECOM-05",
                name = "E-Commerce Growth Accelerator Combo",
                subtitle = "Online Store + Product Content + ROAS Performance Ads",
                description = "Complete turnkey e-commerce acceleration: fast-loading online store, studio-grade product showcase assets, high-converting Meta & Google Shopping ads, and abandoned cart recovery.",
                targetAudience = "Direct-to-Consumer (D2C) Brands, Retailers, Apparel & Beauty",
                includedServiceCategories = "Website Development\nContent Creation\nDigital Marketing\nConversion Optimization",
                includedDeliverables = "Modern E-Commerce Store with Payment & Courier Sync\nStudio Product Photos & Video Ad Creatives\nMeta Dynamic Product Ads & Google PMax Setup\nAbandoned Cart WhatsApp / Email Recovery Sequence\nWeekly ROAS Dashboard & Scaling Strategy",
                originalTotalInr = 119999,
                discountPercentage = 20.0,
                finalPriceInr = 95999,
                billingFrequency = "One-time Build + Monthly Ad Mgmt",
                turnaroundTime = "3 - 4 Weeks Launch",
                isTemplate = true,
                isActive = true
            ),
            ComboPackageEntity(
                id = "COMBO-COMPLETE-06",
                name = "Complete Digital Business Ecosystem",
                subtitle = "Custom Mobile App + Web Portal + Full Marketing & Content",
                description = "The ultimate digital dominance partnership: bespoke Android application, cloud dashboard, custom website, continuous high-volume content, and omnichannel marketing execution.",
                targetAudience = "Funded Startups, Established Enterprises & Fast-Scaling Brands",
                includedServiceCategories = "Application Development\nWebsite Development\nContent Creation\nDigital Marketing",
                includedDeliverables = "Bespoke Android App & Cloud Infrastructure\nExecutive Web Portal & Customer Management CRM\nFull Monthly Content Production (20 Posts + 10 Reels)\nOmnichannel Performance Marketing (Meta + Google + SEO)\nDedicated Technical Project Manager & Creative Director",
                originalTotalInr = 189999,
                discountPercentage = 22.0,
                finalPriceInr = 147999,
                billingFrequency = "Monthly Partner Retainer",
                turnaroundTime = "6 Weeks Mobilization + Ongoing",
                isTemplate = true,
                isActive = true
            )
        )
        comboDao.insertCombos(comboPackages)

        // 5. Portfolio Projects (Section 7)
        val portfolioItems = listOf(
            PortfolioEntity(
                id = "PORT-01",
                title = "Solstice Heritage Luxury Villas",
                category = "Content Creation",
                clientName = "Solstice Hospitality",
                clientPrivacyConsent = true,
                description = "Produced a 4K cinematic video series and drone visual campaign capturing sunrise over private pool estates, resulting in full weekend bookings for 4 consecutive months.",
                deliverablesText = "12 Viral Reels, 4K Drone Film, Architectural Photography",
                metricsResult = "+420% Direct Booking Inquiries, 1.8M Organic Views",
                techStack = "Sony FX3, DJI Mavic 3 Cine, DaVinci Resolve",
                liveUrl = "https://instagram.com/aueraa.digital",
                relatedPackageId = "PKG-CC-CINEMATIC",
                isFeatured = true,
                isPublic = true,
                beforeAfterSummary = "Before: 2 bookings/mo via OTAs. After: 18+ direct inquiries/week without paying OTA commissions.",
                heroTag = "HOSPITALITY VIRALITY"
            ),
            PortfolioEntity(
                id = "PORT-02",
                title = "Nectar Kitchens & Roastery",
                category = "Digital Marketing",
                clientName = "Nectar Gourmet Group",
                clientPrivacyConsent = true,
                description = "Engineered hyper-local Meta & Google Ads targeting brunch lovers within 7km, combined with Google Business Profile local SEO optimization.",
                deliverablesText = "Lead Funnels, Meta Ads, Local Map Pack Domination",
                metricsResult = "4.8x ROAS, 850+ Table Reservations in 60 Days",
                techStack = "Meta Ads Manager, Google PMax, WhatsApp Cloud API",
                liveUrl = "https://aueraa.digital",
                relatedPackageId = "PKG-DM-LEADGEN",
                isFeatured = true,
                isPublic = true,
                beforeAfterSummary = "Before: High cost per lead of ₹140. After: Qualified reservation inquiry at ₹24.",
                heroTag = "4.8X AD ROAS"
            ),
            PortfolioEntity(
                id = "PORT-03",
                title = "Vanguard Wealth Management",
                category = "Website Development",
                clientName = "Vanguard Advisory Partners",
                clientPrivacyConsent = true,
                description = "Architected a luxury, high-speed corporate advisory platform with financial calculators, secure customer appointment booking, and sub-second loading.",
                deliverablesText = "Custom Next.js Web App, Interactive Portfolio, CMS",
                metricsResult = "99/100 Google PageSpeed Score, 0.6s Initial Load",
                techStack = "Next.js, TypeScript, Tailwind CSS, Cloudflare CDN",
                liveUrl = "https://aueraa.digital",
                relatedPackageId = "PKG-WEB-GROWTH",
                isFeatured = true,
                isPublic = true,
                beforeAfterSummary = "Before: Clunky WordPress taking 5.2s to load. After: Instant 0.6s edge-rendered load.",
                heroTag = "SUB-SECOND SPEED"
            ),
            PortfolioEntity(
                id = "PORT-04",
                title = "SwiftFleet Logistics Dispatch App",
                category = "Application Development",
                clientName = "SwiftFleet Enterprises",
                clientPrivacyConsent = true,
                description = "Developed an offline-capable native Android application for 200+ delivery captains with real-time GPS telemetry, proof-of-delivery photo capture, and dispatch sync.",
                deliverablesText = "Native Android App, Dispatch API, Driver Dashboard",
                metricsResult = "99.98% Uptime, 35% Faster Order Dispatch Cycles",
                techStack = "Kotlin, Jetpack Compose, Room, Coroutines, Retrofit",
                liveUrl = "https://aueraa.digital",
                relatedPackageId = "PKG-APP-CUSTOM",
                isFeatured = true,
                isPublic = true,
                beforeAfterSummary = "Before: Manual WhatsApp coordination and paper manifests. After: 1-tap automated route confirmation.",
                heroTag = "NATIVE KOTLIN APP"
            )
        )
        portfolioDao.insertProjects(portfolioItems)

        // 6. Seed Users (Admin & Client)
        val users = listOf(
            ClientUserEntity(
                id = "admin@aueraa.digital",
                email = "admin@aueraa.digital",
                passwordHash = "admin123",
                fullName = "Aueraa Agency Director",
                companyName = "AUERAA.DIGITAL Management",
                phone = "+91 98765 43210",
                whatsapp = "+919876543210",
                businessCategory = "Digital Agency",
                role = "ADMIN",
                status = "ACTIVE"
            ),
            ClientUserEntity(
                id = "client@startup.in",
                email = "client@startup.in",
                passwordHash = "client123",
                fullName = "Rohan Verma",
                companyName = "Verma Artisan Roasters",
                phone = "+91 98111 22334",
                whatsapp = "+919811122334",
                businessCategory = "Food & Beverage",
                role = "CLIENT",
                status = "ACTIVE"
            )
        )
        userDao.insertUsers(users)

        // 7. Seed Client Project & Milestones
        val sampleProject = ClientProjectEntity(
            id = "PRJ-2026-101",
            clientId = "client@startup.in",
            clientName = "Verma Artisan Roasters",
            projectName = "Artisan Roasters Social & Ads Launch",
            serviceCategory = "Combo Package",
            packageId = "COMBO-GROWTH-01",
            status = "IN_PROGRESS",
            startDate = "01 Oct 2026",
            targetCompletionDate = "31 Oct 2026",
            progressPercent = 65,
            deliverablesDeliveredCount = 10,
            deliverablesTotalCount = 16,
            deliverableDownloadUrl = "https://drive.google.com/aueraa-assets/roasters-drive",
            revisionRequestNotes = null,
            isApprovedByClient = false
        )
        projectDao.insertProject(sampleProject)

        val milestones = listOf(
            ProjectMilestoneEntity(
                id = "MS-101",
                projectId = "PRJ-2026-101",
                title = "Moodboard & Content Strategy Approval",
                description = "Visual guidelines, brand voice, and shoot concept approved",
                dueDate = "05 Oct 2026",
                isCompleted = true,
                orderIndex = 1
            ),
            ProjectMilestoneEntity(
                id = "MS-102",
                projectId = "PRJ-2026-101",
                title = "Batch 1 Creative Assets (8 Posts + 3 Reels)",
                description = "First two weeks of promotional assets delivered for staging review",
                dueDate = "12 Oct 2026",
                isCompleted = true,
                orderIndex = 2
            ),
            ProjectMilestoneEntity(
                id = "MS-103",
                projectId = "PRJ-2026-101",
                title = "Meta Ad Campaign Activation",
                description = "Lead & reach ad funnels launched on Meta Ads Manager",
                dueDate = "18 Oct 2026",
                isCompleted = false,
                orderIndex = 3
            ),
            ProjectMilestoneEntity(
                id = "MS-104",
                projectId = "PRJ-2026-101",
                title = "Final Review & End-of-Month Analytics",
                description = "Monthly performance audit and next month strategy handover",
                dueDate = "31 Oct 2026",
                isCompleted = false,
                orderIndex = 4
            )
        )
        milestoneDao.insertMilestones(milestones)

        // 8. Seed Sample Invoices
        val sampleInvoice = InvoiceEntity(
            id = "INV-AUERAA-2026-089",
            clientId = "client@startup.in",
            clientName = "Verma Artisan Roasters",
            projectName = "Artisan Roasters Social & Ads Launch",
            itemsDescription = "Social Media Growth Combo - Month 1 Retainer (16 Posts, 6 Reels, Ads Setup)",
            subtotalInr = 41999,
            gstPercent = 18.0,
            taxAmountInr = 7560,
            totalAmountInr = 49559,
            advancePaidInr = 25000,
            balanceDueInr = 24559,
            status = "PARTIALLY_PAID",
            dueDate = "20 Oct 2026",
            paymentDate = "02 Oct 2026",
            paymentMethod = "UPI (Ref: 4291882910)"
        )
        invoiceDao.insertInvoice(sampleInvoice)

        // 9. Seed Sample Enquiries
        val sampleEnquiry = EnquiryEntity(
            id = "AUERAA-ENQ-2026-001",
            clientName = "Dr. Ananya Sen",
            businessName = "Aura Dental & Esthetics",
            contactNumber = "+91 99223 34455",
            whatsappNumber = "+919922334455",
            email = "contact@auradental.com",
            businessCategory = "Healthcare & Clinic",
            serviceCategory = "Digital Marketing",
            selectedPackageName = "Meta & Google Performance Engine",
            isComboRequest = false,
            expectedBudgetInr = 35000,
            deadline = "Within 2 Weeks",
            referenceLinks = "https://instagram.com/competitor_clinic",
            projectDescription = "Need patient leads for cosmetic dentistry and smile makeovers in South Bangalore. Existing clinic website needs WhatsApp lead generation.",
            additionalRequirements = "Focus on weekday appointment bookings.",
            status = "NEW",
            followUpDate = "Tomorrow 11:00 AM",
            internalNotes = "High priority lead. Client wants to target HNI neighborhoods.",
            lostReason = null
        )
        enquiryDao.insertEnquiry(sampleEnquiry)

        // 10. Audit Log
        auditDao.insertLog(
            AuditLogEntity(
                action = "SYSTEM_INITIALIZATION",
                performedBy = "System",
                details = "AUERAA.DIGITAL database schema initialized with production packages and services."
            )
        )

        // 11. Initial Contact Channels Directory
        val defaultContacts = listOf(
            ContactChannelEntity(
                id = "CONT-WA-PRIMARY",
                title = "Primary WhatsApp Concierge",
                channelType = "WHATSAPP",
                value = "+919876543210",
                subtitle = "Instant proposals & chat with Creative Directors",
                isPrimary = true,
                orderIndex = 1
            ),
            ContactChannelEntity(
                id = "CONT-PHONE-OFFICE",
                title = "Direct Calling Desk",
                channelType = "PHONE",
                value = "+91 98765 43210",
                subtitle = "Mon - Sat: 9:30 AM - 7:00 PM IST",
                isPrimary = true,
                orderIndex = 2
            ),
            ContactChannelEntity(
                id = "CONT-EMAIL-MAIN",
                title = "Business Inquiries & RFPs",
                channelType = "EMAIL",
                value = "contact@aueraa.digital",
                subtitle = "Guaranteed response within 4 hours",
                isPrimary = true,
                orderIndex = 3
            ),
            ContactChannelEntity(
                id = "CONT-EMAIL-SUPPORT",
                title = "Active Client Project Support",
                channelType = "EMAIL",
                value = "support@aueraa.digital",
                subtitle = "Sprint updates & file deliveries",
                isPrimary = false,
                orderIndex = 4
            ),
            ContactChannelEntity(
                id = "CONT-HQ-ADDRESS",
                title = "Corporate Headquarters",
                channelType = "ADDRESS",
                value = "Level 8, Silicon Horizon Towers, Outer Ring Road, Bangalore - 560103, India",
                subtitle = "Visits by prior executive appointment",
                isPrimary = false,
                orderIndex = 5
            )
        )
        contactDao.insertContacts(defaultContacts)

        // 12. Initial Marketing Campaigns Performance Data
        val sampleCampaigns = listOf(
            MarketingCampaignEntity(
                id = "CMP-2026-001",
                campaignName = "Meta Luxury Hospitality & Villa Retargeting",
                clientName = "Verma Artisan Roasters & Eco-Villas",
                platform = "META_ADS",
                status = "ACTIVE",
                targetObjective = "CONVERSIONS",
                startDate = "01 Oct 2026",
                endDate = "31 Oct 2026",
                totalBudgetInr = 150000,
                adSpendInr = 112000,
                revenueGeneratedInr = 582400,
                roas = 5.2,
                conversionRate = 4.85,
                totalImpressions = 342000,
                totalClicks = 14200,
                totalConversions = 688,
                ctr = 4.15,
                cpaInr = 162,
                cpcInr = 7.88
            ),
            MarketingCampaignEntity(
                id = "CMP-2026-002",
                campaignName = "Google Ads Performance Max E-Commerce Scale",
                clientName = "Artisan Roasters D2C Coffee Beans",
                platform = "GOOGLE_ADS",
                status = "ACTIVE",
                targetObjective = "SALES",
                startDate = "05 Oct 2026",
                endDate = "31 Oct 2026",
                totalBudgetInr = 200000,
                adSpendInr = 168000,
                revenueGeneratedInr = 789600,
                roas = 4.7,
                conversionRate = 5.42,
                totalImpressions = 480000,
                totalClicks = 21500,
                totalConversions = 1165,
                ctr = 4.48,
                cpaInr = 144,
                cpcInr = 7.81
            ),
            MarketingCampaignEntity(
                id = "CMP-2026-003",
                campaignName = "Instagram Reels Viral Cafe Footfall Launch",
                clientName = "Verma Artisan Roasters Flagship",
                platform = "META_ADS",
                status = "ACTIVE",
                targetObjective = "LEAD_GENERATION",
                startDate = "01 Oct 2026",
                endDate = "25 Oct 2026",
                totalBudgetInr = 75000,
                adSpendInr = 64500,
                revenueGeneratedInr = 245100,
                roas = 3.8,
                conversionRate = 3.92,
                totalImpressions = 680000,
                totalClicks = 28400,
                totalConversions = 1113,
                ctr = 4.18,
                cpaInr = 58,
                cpcInr = 2.27
            ),
            MarketingCampaignEntity(
                id = "CMP-2026-004",
                campaignName = "LinkedIn B2B Executive LeadGen Funnel",
                clientName = "AUERAA Agency Growth",
                platform = "LINKEDIN_ADS",
                status = "ACTIVE",
                targetObjective = "LEAD_GENERATION",
                startDate = "10 Oct 2026",
                endDate = "31 Oct 2026",
                totalBudgetInr = 120000,
                adSpendInr = 95000,
                revenueGeneratedInr = 494000,
                roas = 5.2,
                conversionRate = 6.20,
                totalImpressions = 115000,
                totalClicks = 4900,
                totalConversions = 304,
                ctr = 4.26,
                cpaInr = 312,
                cpcInr = 19.38
            ),
            MarketingCampaignEntity(
                id = "CMP-2026-005",
                campaignName = "YouTube TrueView Video Brand Storytelling",
                clientName = "Artisan Roasters Origin Series",
                platform = "YOUTUBE",
                status = "ACTIVE",
                targetObjective = "BRAND_AWARENESS",
                startDate = "03 Oct 2026",
                endDate = "28 Oct 2026",
                totalBudgetInr = 90000,
                adSpendInr = 78000,
                revenueGeneratedInr = 288600,
                roas = 3.7,
                conversionRate = 3.45,
                totalImpressions = 840000,
                totalClicks = 19800,
                totalConversions = 683,
                ctr = 2.36,
                cpaInr = 114,
                cpcInr = 3.94
            )
        )
        campaignDao.insertCampaigns(sampleCampaigns)
    }
}
