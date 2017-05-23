package cn.edu.buaa.sei.lmf;

public class LMFResourceException extends Exception {
	
	private static final long serialVersionUID = 8459522460948875212L;

	public LMFResourceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public LMFResourceException(String message) {
		super(message);
	}
	
	public LMFResourceException(Throwable cause) {
		super(cause);
	}

}
