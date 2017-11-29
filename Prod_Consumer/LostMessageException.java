//exception for a lost message
public class LostMessageException extends Exception	 {
	public LostMessageException(String message, int cid) {
		super(message + " for cid: " + cid + " was lost");
	}
}