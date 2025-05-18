package com.example.yourchat.db;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Draft {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public String userId;
    @ColumnInfo(name = "other_user_id")
    public String otherUserId;

    @ColumnInfo(name = "message")
    public String message;
}
