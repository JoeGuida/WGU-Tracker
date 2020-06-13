package database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class Term {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "title")
    String title;

    @ColumnInfo(name = "startDate")
    long startDate;

    @ColumnInfo(name = "endDate")
    long endDate;

    public Term(String title, long startDate, long endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Ignore
    public int getId() {
        return id;
    }

    @Ignore
    public String getTitle() {
        return title;
    }

    @Ignore
    public long getStartDate() {
        return startDate;
    }

    @Ignore
    public long getEndDate() {
        return endDate;
    }

    @Ignore
    public void setId(int id) {
        this.id = id;
    }

    @Ignore
    public void setTitle(String title) {
        this.title = title;
    }

    @Ignore
    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    @Ignore
    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

}
