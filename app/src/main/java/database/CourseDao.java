package database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Course course);

    @Query("DELETE FROM courses WHERE id = :courseId")
    void deleteCourseById(int courseId);

    @Query("SELECT * FROM courses WHERE id = :id")
    Course getCourseById(int id);

    @Query("SELECT * FROM courses WHERE termId = :termId")
    List<Course> selectAllWithTermId(int termId);

    @Query("SELECT * FROM courses")
    List<Course> selectAll();

    @Query("UPDATE courses SET name = :name, startDate = :startDate, endDate = :endDate, status = :status, notes = :notes, mentorName = :mentorName, mentorPhone = :mentorPhone, mentorEmail = :mentorEmail WHERE id = :courseId")
    void updateCourseById(int courseId, String name, long startDate, long endDate, String status, String notes, String mentorName, String mentorPhone, String mentorEmail);
}
