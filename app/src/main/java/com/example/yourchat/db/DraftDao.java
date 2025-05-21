package com.example.yourchat.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
// DAO (Data Access Object) interface for Draft table
@Dao
public interface DraftDao {
    // Method for returning all records (drafts) from the table
    @Query("SELECT * FROM draft")
    List<Draft> getAll();
    // Method for returning drafts by userID and otherUserID
    @Query("SELECT * FROM draft WHERE user_id = :userId AND other_user_id = :otherUserId")
    List<Draft> loadAllByUserId(String userId, String otherUserId);
    // Method for inserting multiple drafts at once
    @Insert
    void insertAll(Draft... drafts);
    // Method for deleting one draft
    @Delete
    void delete(Draft drafts);

}
