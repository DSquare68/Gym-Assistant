package pl.dsquare.home.android.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.Collection;
import java.util.List;

import pl.dsquare.home.android.data.MatchRecord;

@Dao
public interface MatchDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MatchRecord> matches);

    @Query("SELECT * FROM matches")
    List<MatchRecord> getAll();

    @Query("DELETE FROM matches")
    void deleteAll();

    @Query("SELECT * FROM MATCHES WHERE QUEUE = :queue and mode_of_data = :code order by DATE_OF_MATCH ")
    List<MatchRecord> getQueue(int queue, String code);

    @Query("SELECT QUEUE FROM MATCHES WHERE DATE_OF_MATCH > :day and mode_of_data = :code order by DATE_OF_MATCH LIMIT 1")
    int getQueueByDate(String day, String code);
}
