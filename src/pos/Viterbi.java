package pos;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Viterbi {

	private Object[] startStates; //parts of speech,
	private Object[] endStates; //parts of speech,
	private ContextModel transition_model; //this is our contextModel (transitions)
	private DocumentDictionary  emission_model; //this is our documentDictionary for the contextModel

	public Viterbi(File f, boolean twoWords){
		if(twoWords)
			transition_model = new TwoWordContextModel(f);
		else
			transition_model = new OneWordContextModel(f);
		emission_model = new DocumentDictionary(f);
		startStates = transition_model.getStartStates();
		endStates = transition_model.getStartStates();
	}
	public Viterbi(ContextModel owcm, DocumentDictionary dd){
		transition_model = owcm;
		emission_model = dd;
		startStates = owcm.getStartStates();
		endStates = owcm.getStartStates();
		
	}
		
	public void printData(Object[] obs){
		StringBuilder sb = new StringBuilder();
		sb.append(transition_model.toString() + "\n");
		sb.append(emission_model.toString()+"\n");
		
		sb.append("DATA\n");
		for(Object ob : obs)
			sb.append(ob.toString() + "\t");
		
		System.out.println(sb.toString());
	}
	public ArrayList<Object> run( Object[] obs){
		ArrayList<HashMap<Object, Double>> stateProbs = new ArrayList<>();//[{}];
		HashMap<Object, ArrayList<Object>> path = new HashMap<>();
		
		for(int t = 0; t< obs.length; t++){
			stateProbs.add(new HashMap<Object, Double>());
		}
	 
	    // Initialize base cases (t == 0)
	    //for y in states:
		for(int y =0; y<startStates.length; y++ ){
			
			Object startState = startStates[y];
			double value = .00001;
			if(startState instanceof TwoWordKey && !((TwoWordKey) startState).w1.equals("")){}
			else{
				value = transition_model.getStateProbability(startState) * 
						emission_model.getProbability(startState, obs[0]);
			}
			stateProbs.get(0).put(startState, value);
			ArrayList<Object> temp =  new ArrayList<>();
			temp.add(startState);
			path.put(startState, temp);
			
	    }
	 
	    // Run Viterbi for t > 0
		for(int t = 1; t< obs.length; t++ ){
			HashMap<Object, ArrayList<Object>> newpath = new HashMap<>();

			//for y in states:
			for(int y =0; y<endStates.length; y++ ){
				Object endState = endStates[y];
				Object bestState= "";
				double bestProb = Double.NEGATIVE_INFINITY;

				//this loop handles the max
				for(int y0 =0; y0<startStates.length; y0++ ){
					Object startState = startStates[y0];
				
					double prob = stateProbs.get(t-1).get(startState) * transition_model.getTransProbability(startState, endState) * emission_model.getProbability(endState, obs[t]);	
					
					if(bestProb < prob){
						bestProb = prob;
						bestState = startState;
					}
				}
				
				stateProbs.get(t).put(endState, bestProb);
				ArrayList<Object> tempArrayList = listAdd(path.get(bestState), endState);
				newpath.put(endState, tempArrayList);
			}
	            			 
	        //Don't need to remember the old paths
	        path = newpath;
		}
		
	    int n = 0;//           # if only one element is observed max is sought in the initialization values
	    
	    if(obs.length !=1){
	        n = obs.length -1;
	    }

	    double bestProb = Double.NEGATIVE_INFINITY;
	    Object bestState = "";
	    for(int y =0; y< startStates.length; y++){
	    	double curProb = stateProbs.get(n).get(startStates[y]);
	    	if(bestProb < curProb){
	    		bestProb = curProb;
	    		bestState =  startStates[y];
	    	}
	    }
	    return path.get(bestState);
	}

	private ArrayList<Object> listAdd(ArrayList<Object> arrayList, Object key) {
		ArrayList<Object> list = new ArrayList<Object>();
		for(Object cur: arrayList){
			list.add(cur);
		}
		list.add(key);
		return list;
	}
	
}
