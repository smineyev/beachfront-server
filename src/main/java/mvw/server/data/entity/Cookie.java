package mvw.server.data.entity;

public class Cookie {
	private String key;
	private String val;
	
	public Cookie(String key, String val) {
		super();
		this.key = key;
		this.val = val;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
	
}
