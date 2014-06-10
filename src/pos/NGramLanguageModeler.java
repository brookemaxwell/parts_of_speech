package pos;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class uses a contextModel and DocumentDictionary to create a paragraph.
 * main.java runs it with a paragraph length of 20.
 * @author Brooke
 *
 */
public class NGramLanguageModeler {
	

	DocumentDictionary dd;
	Queue<String> context;
	
	NGramLanguageModeler(DocumentDictionary dd){
		this.dd = dd;
		context = new LinkedList<String>();
	}
	
	public void runOneWordContextModel(	OneWordContextModel cm, int numOfWords){
		//the dictionary is our model.
		context = cm.getSeedWords();
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < numOfWords; i++){
			String nextKey = cm.getMostProbableNextKey(context.remove());
			s.append(dd.getNextWord(nextKey));
			s.append(" ");
			context.add(nextKey);
		}
		
		System.out.println(s.toString());
	}

	public void runTwoWordContextModel(TwoWordContextModel tcm, int numOfWords) {
		//the dictionary is our model.
		context = tcm.getSeedWords();
		
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < numOfWords; i++){
			String w1 = context.remove();
			String w2 = context.peek();
			TwoWordKey nextKey = tcm.getMostProbableNextKey(new TwoWordKey(w1,w2));
			s.append(dd.getNextWord(nextKey));
			s.append(" ");
			context.add(nextKey.w2);
		}
		
		System.out.println(s.toString());
	}

}
