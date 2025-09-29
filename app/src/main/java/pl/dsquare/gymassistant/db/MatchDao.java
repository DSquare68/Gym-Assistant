package pl.dsquare.gymassistant.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import pl.dsquare.gymassistant.data.MatchRecord;

@Dao
public interface MatchDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MatchRecord> matches);

    @Query("SELECT * FROM matches")
    List<MatchRecord> getAll();

}
