package com.android.vmapp.points

import androidx.annotation.Keep
import androidx.room.*

@Keep
@Entity(tableName = "goods")
data class GoodsItemModel(
    @PrimaryKey val id: String,
    val enabled: Boolean = true,
    val type: String = "",
    val icon: String = "",
    val name: String = "",
    val desc: String = "",
    val annotations: String = "",
    val coins: Int = 0,
    val extra: String = ""
)

@Dao
interface GoodsItemModelDao {
    @Query("SELECT * FROM goods WHERE enabled = 1")
    fun getAllGoods(): List<GoodsItemModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(goods: List<GoodsItemModel>)
}

@Keep
@Entity(tableName = "missions")
data class MissionModel(
    @PrimaryKey val id: String,
    val title: String = "",
    val rewardCoins: Int = 0,
    val isCompleted: Boolean = false,
    val extra: String = ""
)

@Dao
interface MissionModelDao {
    @Query("SELECT * FROM missions")
    fun getAllMissions(): List<MissionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(missions: List<MissionModel>)
}

@Keep
@Entity(tableName = "coins")
data class MyCoins(
    @PrimaryKey val id: Int = 1,
    val totalCoins: Int = 0
)

@Dao
interface MyCoinsDao {
    @Query("SELECT * FROM coins WHERE id = 1")
    fun getMyCoins(): MyCoins?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setMyCoins(coins: MyCoins)
}

class PointsException(message: String) : Exception(message)

@Database(entities = [GoodsItemModel::class, MissionModel::class, MyCoins::class], version = 1, exportSchema = false)
abstract class PointsDatabase : RoomDatabase() {
    abstract fun goodsDao(): GoodsItemModelDao
    abstract fun missionDao(): MissionModelDao
    abstract fun coinsDao(): MyCoinsDao

    companion object {
        @Volatile
        private var instance: PointsDatabase? = null

        fun getInstance(context: android.content.Context): PointsDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context.applicationContext, PointsDatabase::class.java, "points.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}
