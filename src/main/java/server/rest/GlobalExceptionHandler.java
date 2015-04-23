package server.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import server.business.BusinessException;
import server.business.ErrorHolder;

/**
 * This exception handler applies automatically to all routers.  
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value={BusinessException.class})
	@ResponseBody
	public String handleBusinessExceptions(BusinessException e, HttpServletResponse response) throws IOException{
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ErrorHolder(e).toJSON();
	}	
	
	@ExceptionHandler
	@ResponseBody
	public String handleExceptions(Exception e, HttpServletResponse response) throws IOException{		
		e.printStackTrace();
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ErrorHolder(e).toJSON();		
	}
	
}
