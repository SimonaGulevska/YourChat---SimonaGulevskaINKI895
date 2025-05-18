package com.example.yourchat.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DraftDao {
    @Query("SELECT * FROM draft")
    List<Draft> getAll();

    @Query("SELECT * FROM draft WHERE user_id = :userId AND other_user_id = :otherUserId")
    List<Draft> loadAllByUserId(String userId, String otherUserId);

    @Insert
    void insertAll(Draft... drafts);

    @Delete
    void delete(Draft drafts);

}
