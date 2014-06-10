package pos;

public class TwoWordKey{
	String w1;
	String w2;
	
	public TwoWordKey(String _w1, String _w2){
		this.w1 = _w1;
		this.w2 = _w2;
	}
	public String toString(){
		return "\""+w1+" & "+w2+"\"";
	}
	@Override
	public int hashCode(){
		return 31*w1.hashCode() + w2.hashCode();
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof TwoWordKey )){
			return false;
		}
		TwoWordKey twk = (TwoWordKey) other;
		if(w1.equals(twk.w1) && w2.equals(twk.w2)){
			return true;
		}
		return false;
	}

}