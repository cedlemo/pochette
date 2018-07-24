package fr.pochette.exception;

import java.util.ResourceBundle;

public class ErrorMessageReader {
	private static ResourceBundle rb;
	
	static {
		try {
			rb = ResourceBundle.getBundle("fr.pochette.exception.error_messages");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String getMessage(int code)
	{
		String message="";
		try
		{
			if(rb!=null)
			{
				message=rb.getString(String.valueOf(code));
			}
			else
			{
				message="Something went wrong with the file that contains the error messages";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			message="Unknown error";
		}
		
		return message;
	}
}
