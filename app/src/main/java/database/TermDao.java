package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms")
    List<Term> selectAll();

    @Query("SELECT * FROM terms WHERE id = :id")
    Term getTermById(int id);

    @Query("DELETE FROM terms WHERE id = :id")
    void deleteTermById(int id);

    @Query("UPDATE terms SET title = :title, startDate = :startDate, endDate = :endDate WHERE id = :id")
    void updateTermById(int id, String title, long startDate, long endDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(Term term);

}
