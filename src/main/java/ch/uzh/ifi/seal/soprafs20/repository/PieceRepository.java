package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PieceRepository")
public interface PieceRepository extends JpaRepository<PieceDB, Long> {
    PieceDB findByPieceId(Long pieceId);
}
