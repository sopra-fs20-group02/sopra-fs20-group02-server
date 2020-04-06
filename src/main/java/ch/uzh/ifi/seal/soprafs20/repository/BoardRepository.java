package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("boardRepository")
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByBoardId(Long boardId);
}