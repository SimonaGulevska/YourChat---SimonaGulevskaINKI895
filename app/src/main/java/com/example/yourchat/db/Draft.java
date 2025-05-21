package com.example.yourchat.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Draft {
    // attribute (primary key) auto increment
    @PrimaryKey(autoGenerate = true)
    public int id;
    // column which stores user id
    @ColumnInfo(name = "user_id")
    public String userId;
    // column which stores other user id
    @ColumnInfo(name = "other_user_id")
    public String otherUserId;
    // column which stores the content of the message
    @ColumnInfo(name = "message")
    public String message;
}
