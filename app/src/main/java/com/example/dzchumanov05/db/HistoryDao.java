package com.example.dzchumanov05.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {
    // Добавляем запись в историю
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(History history);

    // Удаляем запись из истории
    @Delete
    void delete(History history);

    // Обновляем запись в истории
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(History history);

    // Получаем все записи истории
    @Query("SELECT * FROM history")
    List<History> getFullHistory();

    // Получаем запись из истории по id
    @Query("SELECT * FROM history WHERE id = :id")
    History getHistoryById(long id);

    //Получаем количество записей в истории
    @Query("SELECT COUNT() FROM history ")
    long getHistoryCount();
}
