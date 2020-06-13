package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments")
public class Assessment {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "type")
    String type;

    @ColumnInfo(name = "status")
    String status;

    @ColumnInfo(name = "goalDate")
    long goalDate;

    @ColumnInfo(name = "courseId")
    @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "courseId")
    int courseId;

    public Assessment(String name, String type, String status, long goalDate, int courseId) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.goalDate = goalDate;
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public long getGoalDate() { return goalDate; }

    public int getCourseId() {
        return courseId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setGoalDate(long goalDate) { this.goalDate = goalDate; }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }


}
