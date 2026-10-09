package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.*
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        AgencySettings::class,
        ServiceEntity::class,
        PackageEntity::class,
        ComboPackageEntity::class,
        PortfolioEntity::class,
        EnquiryEntity::class,
        ClientUserEntity::class,
        ClientProjectEntity::class,
        ProjectMilestoneEntity::class,
        InvoiceEntity::class,
        AuditLogEntity::class,
        ContactChannelEntity::class,
        MarketingCampaignEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun agencyDao(): AgencyDao
    abstract fun serviceDao(): ServiceDao
    abstract fun packageDao(): PackageDao
    abstract fun comboPackageDao(): ComboPackageDao
    abstract fun portfolioDao(): PortfolioDao
    abstract fun enquiryDao(): EnquiryDao
    abstract fun clientUserDao(): ClientUserDao
    abstract fun clientProjectDao(): ClientProjectDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun invoiceDao(): InvoiceDao
    abstract fun auditLogDao(): AuditLogDao
    abstract fun contactChannelDao(): ContactChannelDao
    abstract fun campaignDao(): CampaignDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aueraa_digital_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        SeedDataLoader.populateDatabase(database)
                    }
                }
            }
        }
    }
}
