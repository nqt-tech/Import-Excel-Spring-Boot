package net.sharecs.city.sharecsservice.core.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.sharecs.city.sharecsservice.business.z_common.enumeration.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/*
 * @Author: Nguyen Tuan - Sharecs.net 
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class InfoException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final int errorCode;

    private final String errorMessage;
    
    private final LocalDateTime timestamp = LocalDateTime.now();
    
    public InfoException(ErrorCode errorCode, String errorMessage) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode.value();
        this.errorMessage = errorMessage;
    }

    public InfoException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = ErrorCode.UNKNOWN.value();
        this.errorMessage = errorMessage;
    }


}
