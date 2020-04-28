package ch.uzh.ifi.seal.soprafs20.rest.mapper;

import ch.uzh.ifi.seal.soprafs20.entity.Game;
import ch.uzh.ifi.seal.soprafs20.entity.Move;
import ch.uzh.ifi.seal.soprafs20.entity.User;
import ch.uzh.ifi.seal.soprafs20.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "token", target = "token")
    //@Mapping(source = "gameHistory", target = "gameHistory")
    @Mapping(source = "userStats", target = "userStats")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "userId", target = "userId")
    //@Mapping(source = "pieceId", target = "pieceId")
    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    Move convertMovePostDTOtoEntity(MovePostDTO movePostDTO);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "token", target = "token")
    //@Mapping(source = "gameHistory", target = "gameHistory")
    @Mapping(source = "userStats", target = "userStats")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "token", target = "token")
    UserLoginDTO convertEntityToUserLoginDTO(User user);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "creationDate", target = "creationDate")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "token", target = "token")
    UserProfileDTO convertEntityToUserProfileDTO(User user);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "playerWhite", target = "playerWhite")
    @Mapping(source = "playerBlack", target = "playerBlack")
    @Mapping(source = "pieces", target = "pieces")
    @Mapping(source = "startTime", target = "startTime")
    @Mapping(source = "endTime", target = "endTime")
    @Mapping(source = "whiteDuration", target = "whiteDuration")
    @Mapping(source = "blackDuration", target = "blackDuration")
    @Mapping(source = "gameStatus", target = "gameStatus")
    @Mapping(source = "isWhiteTurn", target = "isWhiteTurn")
    @Mapping(source = "winner", target = "winner")
    GameGetDTO convertEntityToGameGetDTO(Game game);

    /*@Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "userId", target = "userId")
    JoinPutDTO convertJoinPutDTOToEntity(JoinPutDTO joinPutDTO);*/
}
