package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "courses")
public class Course {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "name")
    String name;

    @ColumnInfo(name = "startDate")
    long startDate;

    @ColumnInfo(name = "endDate")
    long endDate;

    @ColumnInfo(name = "status")
    String status;

    @ColumnInfo(name = "notes")
    String notes;

    @ColumnInfo(name = "mentorName")
    String mentorName;

    @ColumnInfo(name = "mentorPhone")
    String mentorPhone;

    @ColumnInfo(name = "mentorEmail")
    String mentorEmail;

    @ColumnInfo(name = "termId")
    @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "termId", onDelete = ForeignKey.CASCADE)
    int termId;

    public Course(String name, long startDate, long endDate,
                  String status, String notes, String mentorName,
                  String mentorPhone, String mentorEmail, int termId) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.notes = notes;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.termId = termId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public String getStatus() { return status; }

    public String getNotes() {
        return notes;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public int getTermId() {
        return termId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) { this.status = status; }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }


}
