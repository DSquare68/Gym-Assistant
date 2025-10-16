package pl.dsquare.home.android.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    @Query("SELECT * FROM MATCHES WHERE DATE_OF_MATCH BETWEEN :friday AND :monday order by DATE_OF_MATCH and code_of_data = :code")
    List<MatchRecord> getQueue(String friday, String monday, String code);
}
