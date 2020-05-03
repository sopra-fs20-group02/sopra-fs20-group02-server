package ch.uzh.ifi.seal.soprafs20.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
public class GlobalExceptionAdvice extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "This should be application specific";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    // Exception is raised if the username or name is already taken
    @ExceptionHandler(SopraServiceException.class)
    public ResponseEntity handleBadRequestException(SopraServiceException ex) {
        log.error(String.format("SopraServiceException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleTransactionSystemException(Exception ex, HttpServletRequest request) {
        log.error(String.format("Request: %s raised %s", request.getRequestURL(), ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Keep this one disable for all testing purposes -> it shows more detail with this one disabled
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity handleException(Exception ex) {
        log.error(String.format("Exception raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Exception is raised during login if the credentials are incorrect
    @ExceptionHandler(LoginException.class)
    public ResponseEntity handelLoginException (Exception ex) {
        log.error(String.format("LoginException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // Exception is raised during login if the user is already logged in
    @ExceptionHandler(AlreadyLoggedInException.class)
    public ResponseEntity handleAlreadyLoggedInException (Exception ex) {
        log.error(String.format("AlreadyLoggedInException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NO_CONTENT);
    }

    // Exception is raised if a user with a specific id does not exist
    @ExceptionHandler(UserException.class)
    public ResponseEntity handelUserException (Exception ex) {
        log.error(String.format("UserException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // TODO: needs to be tested
    @ExceptionHandler(PieceNotInGameException.class)
    public ResponseEntity handlePieceNotInGameException (Exception ex) {
        log.error(String.format("PieceNotInGameException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // TODO: needs to be tested
    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity handleInvalidMoveException (Exception ex) {
        log.error(String.format("InvalidMoveException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // TODO: needs to be tested
    @ExceptionHandler(JoinGameException.class)
    public ResponseEntity handelJoingGameException (Exception ex) {
        log.error(String.format("InvalidMoveException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(LeaveGameException.class)
    public ResponseEntity handelLeaveGameException (Exception ex) {
        log.error(String.format("LeaveGameException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OthersTurnException.class)
    public ResponseEntity handelOthersTurnException (Exception ex) {
        log.error(String.format("OthersTurnException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MakeMoveException.class)
    public ResponseEntity handelMakeMoveException (Exception ex) {
        log.error(String.format("MakeMoveException raised:%s", ex));
        return new ResponseEntity(ex.getMessage(), HttpStatus.CONFLICT);
    }
}