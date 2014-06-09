package pos;

import java.util.ArrayList;
import java.util.HashMap;

public class Viterbi {

	private String[] states = {"Healthy", "Fever"};
			 
	private String[] observations = {"normal", "cold", "fever"};
			 
	private HashMap<String, Double> start_probability;
	//= {{"Healthy", 0.6}, {"Fever", 0.4}};
			 
	private HashMap<String, HashMap<String, Double>> transition_probability;
	/*= {
	{"Healthy", {"Healthy", 0.7, "Fever", 0.3},
	  "Fever", {"Healthy", 0.4, "Fever", 0.6}}};
	 */
	
		private HashMap<String, HashMap<String, Double>>  emission_probability;
		/*= {
			   'Healthy' : {'normal': 0.5, 'cold': 0.4, 'dizzy': 0.1},
			   'Fever' : {'normal': 0.1, 'cold': 0.3, 'dizzy': 0.6}
			}*/
	
	public Viterbi(){
		start_probability = new HashMap<String, Double>();
		//{"Healthy", 0.6}, {"Fever", 0.4}
		start_probability.put("Healthy", .6);
		start_probability.put("Fever", .4);
		
		transition_probability =  new HashMap<String, HashMap<String, Double>>();
		/*"Healthy", {"Healthy", 0.7, "Fever", 0.3},
	      "Fever", {"Healthy", 0.4, "Fever", 0.6};*/
		HashMap<String, Double> transProbHealth =  new HashMap<>();
		transProbHealth.put("Healthy", .7);
		transProbHealth.put("Fever", .3);
		transition_probability.put("Healthy", transProbHealth);
		HashMap<String, Double> transProbFever =  new HashMap<>();
		transProbFever.put("Healthy", .4);
		transProbFever.put("Fever", .6);
		transition_probability.put("Fever", transProbFever);
		
		emission_probability=  new HashMap<String, HashMap<String, Double>>();
		/*= 'Healthy' : {'normal': 0.5, 'cold': 0.4, 'dizzy': 0.1},
			   'Fever' : {'normal': 0.1, 'cold': 0.3, 'dizzy': 0.6}*/
		HashMap<String, Double> emisProbHealth =  new HashMap<>();
		emisProbHealth.put("normal", .5);
		emisProbHealth.put("cold", .4);
		emisProbHealth.put("dizzy", .1);
		emission_probability.put("Healthy", transProbHealth);
		HashMap<String, Double> emisProbFever =  new HashMap<>();
		emisProbFever.put("normal", .1);
		emisProbFever.put("cold", .3);
		emisProbFever.put("dizzy", .6);
		emission_probability.put("Fever", emisProbFever);
		
	}
		
		
	public void viterbi( String[] obs){
		ArrayList<HashMap<String, Double>> V = new ArrayList<>();//[{}];
		HashMap<String, ArrayList<String>> path = new HashMap<>();
	 
	    // Initialize base cases (t == 0)
	    //for y in states:
		for(int y =0; y<states.length; y++ ){
			// V[0][y] = start_p[y] * emit_p[y][obs[0]]
			String key = states[y];
			double value = start_probability.get(key) * emission_probability.get(key).get(obs[0]);
			V.get(0).put(key, value);
			//path[y] = [y]
			ArrayList<String> temp =  new ArrayList<>();
			temp.add(key);
			path.put(key, temp);
			
	    }
	 
	    // Run Viterbi for t > 0
	    //for t in range(1, len(obs)):
		for(int t = 1; t< obs.length; t++ ){
	        //V.append({})
			HashMap<String, Double> tempV = new HashMap<String, Double>();
			HashMap<String, ArrayList<String>> newpath = new HashMap<>();

			//for y in states:
			for(int y =0; y<states.length; y++ ){
	            String key = states[y];
				String bestState= "";
				double bestProb = Double.NEGATIVE_INFINITY;
				//(prob, state) = max((V[t-1][y0] * trans_p[y0][y] * emit_p[y][obs[t]], y0) for y0 in states)
				//this loop handles the max
				for(int y0 =0; y0<states.length; y0++ ){
					String primeKey = states[y0];
					double prob = V.get(t-1).get(primeKey) * transition_probability.get(primeKey).get(key) * emission_probability.get(key).get(obs[t]);		
					if(bestProb < prob){
						bestProb = prob;
						bestState = primeKey;
					}
				}
				// V[t][y] = prob
				V.get(t).put(key, bestProb);
			    //newpath[y] = path[state] + [y]		//I am guessing on what this is supposed to be doing
				ArrayList<String> tempArrayList = listAdd(path.get(bestState), key);
				newpath.put(key, tempArrayList);
			}
	            			 
	        //Don't need to remember the old paths
	        path = newpath;
		}
		
	    int n = 0;//           # if only one element is observed max is sought in the initialization values
	    //if len(obs)!=1:
	    if(obs.length !=1){
	    	// n = t
	        n = obs.length;
	    }
	    //print_dptable(V)
	    //(prob, state) = max((V[n][y], y) for y in states)
	    double bestProb = Double.NEGATIVE_INFINITY;
	    String bestState = "";
	    for(int y =0; y< states.length; y++){
	    	double curProb = V.get(n).get(states[y]);
	    	if(bestProb < curProb){
	    		bestProb = curProb;
	    		bestState =  states[y];
	    	}
	    	
	    }
	    //return (prob, path[state])
	 /*
	# Don't study this, it just prints a table of the steps.
	def print_dptable(V):
	    s = "    " + " ".join(("%7d" % i) for i in range(len(V))) + "\n"
	    for y in V[0]:
	        s += "%.5s: " % y
	        s += " ".join("%.7s" % ("%f" % v[y]) for v in V)
	        s += "\n"
	    print(s)
	*/
	}


	private ArrayList<String> listAdd(ArrayList<String> arrayList, String key) {
		ArrayList<String> list = new ArrayList<String>();
		for(String cur: arrayList){
			list.add(cur);
		}
		list.add(key);
		return list;
	}
	
}
