package database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Term.class, Course.class, Assessment.class}, version = 8, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "wgutracker.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    // Data Access Objects
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    public static AppDatabase getInstance(Context context) {
       if(instance == null) {
           synchronized (LOCK) {
               if(instance == null) {
                   instance = Room.databaseBuilder(context.getApplicationContext(),
                           AppDatabase.class, DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
               }
           }
       }
       return instance;
    }
}
