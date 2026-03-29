package ru.yandex.practicum.filmorate.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    @Query("SELECT l.filmId FROM Like l WHERE l.userId = :userId")
    List<Long> findFilmIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.filmId = :filmId")
    long countLikesByFilmId(@Param("filmId") Long filmId);

    boolean existsByUserIdAndFilmId(Long userId, Long filmId);
}
