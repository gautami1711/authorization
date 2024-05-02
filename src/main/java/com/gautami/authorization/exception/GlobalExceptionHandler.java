package com.gautami.authorization.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler({InvalidRequest.class, NotFoundException.class, AlreadyExists.class, Forbidden.class,})
        public void handleException(HttpServletResponse response, RuntimeException ex) throws IOException {
            response.setStatus(getHttpStatus(ex));
            response.setContentType("application/json");
            String errorMessage = "{ \"error\": \"" + ex.getMessage() + "\" }";
            response.getWriter().write(errorMessage);
        }

        private int getHttpStatus(RuntimeException ex) {
            if (ex instanceof InvalidRequest) {
                return HttpServletResponse.SC_BAD_REQUEST;
            }  else if (ex instanceof NotFoundException) {
                return HttpServletResponse.SC_NOT_FOUND;
            }else if(ex instanceof Forbidden){
                return HttpServletResponse.SC_FORBIDDEN;
            }
            else if(ex instanceof AlreadyExists){
                return HttpServletResponse.SC_CONFLICT;
            }

            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }



