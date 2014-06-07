package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
/**
 * This is a class to map parts of speech to their followers. 
 * Right now it only works with 1-word pairings, but it should be able to do more than that.
 * @author Brooke
 *
 */
public class ContextModel{

	HashMap<String, ArrayList<WordNode>> model;
	
	private class WordNode{
		String word;
		int count;
		WordNode(String s, int i){
			word = s;
			count = i;
		}
	}
	
	public ContextModel(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			System.out.println("Program will exit");
			e.printStackTrace();
			System.exit(0);
		}
		
		model = new HashMap<String, ArrayList<WordNode>>();
		
		LinkedList<String> context = new LinkedList<String>();
		context.add("");
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			//split[0] would be the word itself.
			//split[1] is the key.
			
			String word = split[1];
			String key = context.remove();
			context.add(word);
			
			if("".equals(word)){
				continue;
			}
			if(model.containsKey(key)){
				ArrayList<WordNode> wnList = model.get(key);
				boolean found = false;
				for(WordNode wn: wnList){
					if(wn.word.equals(word)){
						wn.count++;
						model.put(key, wnList);
						found = true;
						break;
					}
				}
				if(!found)
					wnList.add(new WordNode(word, 1));
				
			}
			else{
				ArrayList<WordNode> newList = new ArrayList<WordNode>();
				newList.add(new WordNode(word, 1));
				model.put(key, newList);
			}
			
		}
		
	}	
	
	public String getNextKey(String seedWord){
		Random rand = new Random();
		ArrayList<WordNode> words = null;
		if(seedWord == null){
			String key = "";
			words = model.get(key);
		}else{
			words = model.get(seedWord);
		}
		/*get a word probabilitstically*/
		
		
		int randomIndex = rand.nextInt((words.size()));
	    	
		return words.get(randomIndex).word;
	}
	
	public LinkedList<String> getSeedWords(){
		LinkedList<String> context = new LinkedList<String>();

		context.add("");
		return context;
	}
	
	/**This method does two things:
	 * 1)make the key lower case to avoid "RanDomWOrds" not being the same as "rANDOMWord"
	   2) trims off non-letter characters at the begining and ending of words (so that "healing." is the same as "healing")
	 * @param key
	 * @return
	 */
	public static String trim(String key) {
		String temp = key;
		
		//makes the key lower case
		key = key.toLowerCase();
		
		//trim off the none letter characters
		while(!key.equals("") && !Character.isLetter(key.charAt(0))){
			key = key.substring(1);
		}
		while(!key.equals("") && !Character.isLetter(key.charAt(key.length()-1))){
			key = key.substring(0, key.length()-1);
		}
		
		return key;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ContextModel:");
		for(String key: model.keySet()){
			sb.append("\n\t"+ key);
			List<WordNode> wnList = model.get(key);
			for(WordNode wn : wnList){
				sb.append("\n\t\t"+ wn.word +"  "+ wn.count);
			}
			
		}
		return sb.toString();
	}




	public boolean contains(String word) {
		return false;
		//return wordCounts.containsKey(word);
	}
}
