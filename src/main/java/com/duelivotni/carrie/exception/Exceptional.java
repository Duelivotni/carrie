package com.duelivotni.carrie.exception;

import org.springframework.http.HttpStatus;

public interface Exceptional {

    default ServiceException exception(HttpStatus status, String... messages) {
        return new ServiceException(messages, status);
    }

    default ServiceException bad(String... messages) {
        return this.exception(HttpStatus.BAD_REQUEST, messages);
    }

    default  ServiceException unauthorized(String... messages) {
        return this.exception(HttpStatus.UNAUTHORIZED, messages);
    }

    default  ServiceException forbidden(String... messages) {
        return this.exception(HttpStatus.UNAUTHORIZED, messages);
    }

    default  ServiceException notFound(String... messages) {
        return this.exception(HttpStatus.NOT_FOUND, messages);
    }

}
