package com.duelivotni.carrie.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class ServiceException
        extends RuntimeException {

    private String[] messages;
    private HttpStatus status;

}
