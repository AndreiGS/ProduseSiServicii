package com.findthebusiness.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {

    @ExceptionHandler(value = UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> exception (UserAlreadyRegisteredException exception) {
        return new ResponseEntity<>("User already registered", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserNotRegisteredException.class)
    public ResponseEntity<Object> exception (UserNotRegisteredException exception) {
        return new ResponseEntity<>("User not registered", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = WrongCredentialsForRequestException.class)
    public ResponseEntity<Object> exception (WrongCredentialsForRequestException exception) {
        return new ResponseEntity<>("Wrong credentials", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = CannotResponseToRequestException.class)
    public ResponseEntity<Object> exception (CannotResponseToRequestException exception) {
        return new ResponseEntity<>("Cannot response", HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(value = BannedAccountException.class)
    public ResponseEntity<Object> exception (BannedAccountException exception) {
        return new ResponseEntity<>("This account has been banned", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotActiveAccountException.class)
    public ResponseEntity<Object> exception (NotActiveAccountException exception) {
        return new ResponseEntity<>("This account is not active", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = HasNotAcceptedTermsException.class)
    public ResponseEntity<Object> exception (HasNotAcceptedTermsException exception) {
        return new ResponseEntity<>("User has not accepted the terms", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = PasswordsDoNotMatchException.class)
    public ResponseEntity<Object> exception (PasswordsDoNotMatchException exception) {
        return new ResponseEntity<>("Passwords do not match", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = ChangeInfoTokenExpiredException.class)
    public ResponseEntity<Object> exception (ChangeInfoTokenExpiredException exception) {
        return new ResponseEntity<>("Change information token has expired", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ChangePasswordTokenExpiredException.class)
    public ResponseEntity<Object> exception (ChangePasswordTokenExpiredException exception) {
        return new ResponseEntity<>("Change password token has expired", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotEnoughCreditException.class)
    public ResponseEntity<Object> exception (NotEnoughCreditException exception) {
        return new ResponseEntity<>("User has not enough money", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotTheOwnerException.class)
    public ResponseEntity<Object> exception (NotTheOwnerException exception) {
        return new ResponseEntity<>("User is trying to modify someone else's shop", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = TabAlreadyExistsException.class)
    public ResponseEntity<Object> exception (TabAlreadyExistsException exception) {
        return new ResponseEntity<>("Tab already exists", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(value = CannotChangeSizeException.class)
    public ResponseEntity<Object> exception (CannotChangeSizeException exception) {
        return new ResponseEntity<>("Cannot change size of shop", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CannotAddItemException.class)
    public ResponseEntity<Object> exception (CannotAddItemException exception) {
        return new ResponseEntity<>("Cannot change size of shop", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ShopsLimitExceeded.class)
    public ResponseEntity<Object> exception (ShopsLimitExceeded exception) {
        return new ResponseEntity<>("Cannot add new shop", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = CannotChangePublishException.class)
    public ResponseEntity<Object> exception (CannotChangePublishException exception) {
        return new ResponseEntity<>("Cannot change publish state of shop", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = NotEnoughTokensException.class)
    public ResponseEntity<Object> exception (NotEnoughTokensException exception) {
        return new ResponseEntity<>("User has not enough tokens", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = PhotoRequiredException.class)
    public ResponseEntity<Object> exception (PhotoRequiredException exception) {
        return new ResponseEntity<>("Photo is required", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = NameTooLongException.class)
    public ResponseEntity<Object> exception (NameTooLongException exception) {
        return new ResponseEntity<>("Name too long", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DescriptionTooLongException.class)
    public ResponseEntity<Object> exception (DescriptionTooLongException exception) {
        return new ResponseEntity<>("Description too long", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CannotSaveTabException.class)
    public ResponseEntity<Object> exception (CannotSaveTabException exception) {
        return new ResponseEntity<>("Cannot save tab", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = PasswordTooShortException.class)
    public ResponseEntity<Object> exception (PasswordTooShortException exception) {
        return new ResponseEntity<>("Password is too short", HttpStatus.LENGTH_REQUIRED);
    }

    @ExceptionHandler(value = BreachedPasswordException.class)
    public ResponseEntity<Object> exception (BreachedPasswordException exception) {
        return new ResponseEntity<>("Password has been breached in the past", HttpStatus.LOCKED);
    }

    @ExceptionHandler(value = PasswordTooWeakException.class)
    public ResponseEntity<Object> exception (PasswordTooWeakException exception) {
        return new ResponseEntity<>("Password is too weak", HttpStatus.UPGRADE_REQUIRED);
    }
}
