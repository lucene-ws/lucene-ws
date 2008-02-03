package net.lucenews3.atom;

/**
 * A Text construct contains human-readable text, usually in small quantities.
 * The content of Text constructs is Language-Sensitive.
 * 
 * @see http://tools.ietf.org/html/rfc4287#section-3.1
 * 
 */
public interface Text {

	public String getType();
	
	public void setType(String type);
	
}
