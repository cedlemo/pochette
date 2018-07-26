package fr.pochette.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private List<Integer> errorCodes;
	
	public BusinessException(){
		this.errorCodes = new ArrayList<Integer>();
	}
	
	public void addError(int code) {
		this.errorCodes.add(code);
	}

	public List<Integer> getErrorCodes(){
		return this.errorCodes;
	}
	
	public boolean hasError() {
		return this.errorCodes.size() > 0;
	}
}
