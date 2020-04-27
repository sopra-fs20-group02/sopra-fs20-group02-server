package ch.uzh.ifi.seal.soprafs20.repository;

import ch.uzh.ifi.seal.soprafs20.constant.Color;
import ch.uzh.ifi.seal.soprafs20.constant.PieceType;
import ch.uzh.ifi.seal.soprafs20.entity.PieceDB;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PieceRepositoryIntegrationTest {

    @Autowired
    private PieceRepository pieceRepository;

    @Test
    public void findByPieceId_success() {
        // given
        PieceDB piece = new PieceDB();
        piece.setPieceId(1L);
        piece.setPieceType(PieceType.ROOK);
        piece.setXCord(1);
        piece.setYCord(1);
        piece.setColor(Color.WHITE);
        piece.setHasMoved(false);
        piece.setCaptured(false);

        // save piece in database
        pieceRepository.save(piece);
        pieceRepository.flush();

        // when
        PieceDB found = pieceRepository.findByPieceId(piece.getPieceId());

        // then
        assertNotNull(found.getPieceId());
        assertEquals(piece.getPieceType(), PieceType.ROOK);
        assertEquals(piece.getXCord(), piece.getXCord());
        assertEquals(piece.getYCord(), piece.getYCord());
        assertEquals(piece.getColor(), Color.WHITE);
        assertFalse(piece.isHasMoved());
        assertFalse(piece.isCaptured());
    }
}
