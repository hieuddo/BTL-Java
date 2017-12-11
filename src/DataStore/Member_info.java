package DataStore;

public class Member_info {
	private String code;
	private String name;
	private String accessLevel;
	private String returnType;
	private boolean memberType;
	
	public Member_info() {
		name = new String();
		accessLevel = new String("~");
	}
	public Member_info(String code) {
		this();
		this.code = code;
		parsing();
	}
	private void parsing() {
		setAccessLevel();
		deleteKeyWord();
		if ( code.indexOf("=") > -1 ) 
			code = code.substring(0, code.indexOf("="));
		if ( code.indexOf("throws") > -1 )
			code = code.substring(0, code.indexOf("throws"));
		setMemberType();
		this.name = code.trim();
	}
	
	private void deleteKeyWord() {
		code = code.replace("final", "");
		code = code.replace("public", "");
		code = code.replace("private", "");
		code = code.replace("protected", "");
		code = code.replace("static", "");
	}
	private void setAccessLevel() {
		if ( code.indexOf("public") > -1 )
			this.accessLevel = "+";
		if ( code.indexOf("private") > -1 )
			this.accessLevel = "-";
		if ( code.indexOf("protected") > -1 )
			this.accessLevel = "#";
	}
	private void setMemberType() {
		if ( code.indexOf("(") >= 0 ) {
			this.memberType = false;
			return;
		}
		this.memberType = true;
	}
	public String getName() {
		return this.accessLevel+this.name;
	}
	public String getAccessLevel() {
		return this.accessLevel;
	}
	public boolean getMemberType() {
		return memberType;
	}
	public String getInfo() {
		String s = new String();
		s += this.accessLevel + " " + this.name;
		return s;
	}
}