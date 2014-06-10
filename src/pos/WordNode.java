package pos;

public class WordNode{
	Object word;
	int count;
	WordNode(Object s, int i){
		word = s;
		count = i;
	}
	
	public String toString(){
		return word + ": "+ count;
	}
}