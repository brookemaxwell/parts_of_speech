package pos;

public class WordNode{
	String word;
	int count;
	WordNode(String s, int i){
		word = s;
		count = i;
	}
	
	public String toString(){
		return word + ": "+ count;
	}
}