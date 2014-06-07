package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This is a class to sort words into parts of speech with their occurrences. It maps a part of speech to words.
 * @author Brooke
 *
 */
public class DocumentDictionary{

	HashMap<String, ArrayList<WordNode>> partsOfSpeech;
	
	private class WordNode{
		String word;
		int count;
		WordNode(String s, int i){
			word = s;
			count = i;
		}
	}
	
	public DocumentDictionary(File file) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			e.printStackTrace();
		}
		
		partsOfSpeech = new HashMap<String, ArrayList<WordNode>>();
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			String key = split[1];
			String word = split[0];
			
			if("".equals(key)){
				continue;
			}
			if(partsOfSpeech.containsKey(key)){
				ArrayList<WordNode> count = partsOfSpeech.get(key);
				boolean found = false;
				for(WordNode wn: count){
					if(wn.word.equals(word)){
						wn.count++;
						partsOfSpeech.put(key, count);
						found = true;
						break;
					}
				}
				if(!found)
					count.add(new WordNode(word, 1));
				
			}
			else{
				ArrayList<WordNode> newList = new ArrayList<WordNode>();
				newList.add(new WordNode(word, 1));
				partsOfSpeech.put(key, newList);
			}
		}
		
	}	
	
	public String getRandomWord(String seedWord){
		return "randomword";
	}
	public String getNextWord(String seedWord){
		Random rand = new Random();
		ArrayList<WordNode> words = null;
		if(seedWord == null){
			return "";
		}else{
			words = partsOfSpeech.get(seedWord);
		}
		/*get a word probabilitstically*/
		
		
		int randomIndex = rand.nextInt((words.size()));
	    	
		return words.get(randomIndex).word;
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
		sb.append("DocumentDictionary:");
		for(String key: partsOfSpeech.keySet()){
			sb.append("\n\t"+ key);
			List<WordNode> wnList = partsOfSpeech.get(key);
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
