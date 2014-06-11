package pos;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
/**
 * This is a class to map parts of speech to their followers. 
 *
 */
public class TwoWordContextModel extends ContextModel{
	
	HashSet<String> wordTypes;
	
	public TwoWordContextModel(File file) {
		super(file);
		wordTypes = new HashSet<String>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Error occured with: "+ file.getAbsolutePath());
			System.out.println("Program will exit");
			e.printStackTrace();
			System.exit(0);
		}
		
		LinkedList<String> context = new LinkedList<String>();
		context.add("");
		context.add("");	
		
		while(scanner.hasNext()){
			
			String[] split = scanner.next().split("_");
			//split[0] would be the word itself.
			//split[1] is the key.
			
			String word = split[1];
			String key1 = context.remove();
			String key2 = context.getFirst();
			context.add(word);
			
			if("".equals(word)){
				continue;
			}
			totalKeyOccurrences++;
			TwoWordKey twKey = new TwoWordKey(key1, key2);
			wordTypes.add(key1);
			wordTypes.add(key2);
			if(model.containsKey(twKey)){
				ArrayList<WordNode> wnList = model.get(twKey);
				keyOccurrences.put(twKey,  keyOccurrences.get(twKey) + 1); //increment the number of occurrences of the key
				boolean found = false;
				for(WordNode wn: wnList){
					if(wn.word.equals(word)){
						wn.count++;
						model.put(twKey, wnList);
						found = true;
						break;
					}
				}
				if(!found)
					wnList.add(new WordNode(new TwoWordKey(twKey.w2, word), 1));
				
			}
			else{
				ArrayList<WordNode> newList = new ArrayList<WordNode>();
				newList.add(new WordNode(new TwoWordKey(twKey.w2, word), 1));
				model.put(twKey, newList);
				keyOccurrences.put(twKey, 1); //increment the number of occurrences of the key
			}
		}
	}	
	
	@Override
	public TwoWordKey getMostProbableNextKey(Object object){
		rand = new Random();
		ArrayList<WordNode> words = null;
		
		TwoWordKey twKey = (TwoWordKey) object;

		if(object == null){
			twKey = new TwoWordKey("","");
			words = model.get(twKey);
		}else{
			words = model.get(twKey);
		}
		
		//get a count of the total number of words associated with that part of speech
		int total =0;
		for(WordNode curNode: words){
			total+= curNode.count;
		}
		
		//generate a random index from 0 to the total number of words
		int randomIndex = rand.nextInt((total));

		//get the word at that index
		TwoWordKey randomlyPickedWord =new TwoWordKey("","");	
		for(WordNode curNode: words){
			randomIndex-= curNode.count;
			if(randomIndex <0){
				randomlyPickedWord = (TwoWordKey)curNode.word;
				break;
			}
		}
		System.out.println(twKey + " then " + randomlyPickedWord);
		return randomlyPickedWord;
	}
	
	public LinkedList<String> getSeedWords(){
		LinkedList<String> context = new LinkedList<String>();
		context.add("");
		context.add("");
		return context;
	}

	@Override
	public HashSet<String> getWordTypes() {
		return wordTypes;
	}

}

