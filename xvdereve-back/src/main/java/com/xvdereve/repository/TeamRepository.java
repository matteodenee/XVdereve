package com.xvdereve.repository;

import com.xvdereve.model.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("""
        SELECT DISTINCT t
        FROM Team t
        LEFT JOIN FETCH t.players
    """)
    List<Team> findAllWithPlayers();
}