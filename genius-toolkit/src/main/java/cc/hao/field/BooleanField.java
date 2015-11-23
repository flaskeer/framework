package cc.hao.field;

public class BooleanField extends OMField<Boolean>{

	public BooleanField() {
		// TODO Auto-generated constructor stub
	}
	
	public BooleanField(String s){
		set(s == "Y");
	}
	
	@Override
	public String toString() {
		return value ? "Y" :"N";
	}

}
