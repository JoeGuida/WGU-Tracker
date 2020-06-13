package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessment assessment);

    @Query("DELETE FROM assessments WHERE id = :assessmentId")
    void deleteAssessmentById(int assessmentId);

    @Query("SELECT * FROM assessments WHERE id = :assessmentId")
    Assessment getAssessmentById(int assessmentId);

    @Query("SELECT * FROM assessments WHERE courseId = :courseId")
    List<Assessment> selectAllWithCourseId(int courseId);

    @Query("SELECT * FROM assessments")
    List<Assessment> selectAll();

    @Query("UPDATE assessments SET name = :name, type = :type, status = :status, goalDate = :goalDate WHERE id = :assessmentId")
    void updateAssessmentById(int assessmentId, String name, String type, String status, long goalDate);

}
