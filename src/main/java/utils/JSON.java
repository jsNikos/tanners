package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
	public static String stringify(Object object){
		String result = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			result = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public static <T> T parse(String content, Class<T> type){
		ObjectMapper objectMapper = new ObjectMapper();
		T result = null;
		try {
			result = objectMapper.readValue(content, type);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}
}
