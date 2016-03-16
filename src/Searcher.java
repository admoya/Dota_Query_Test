import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Searcher {

	String key;
	ArrayList<Integer> Radiant, Dire;
	ArrayList<JSONObject> matches;
	public Searcher(String key, ArrayList<Integer> Radiant, ArrayList<Integer> Dire){
		this.key = key;
		this.Radiant = Radiant;
		this.Dire = Dire;
	}
	
	public ArrayList<JSONObject> getMatches() throws JSONException, MalformedURLException, IOException{
		boolean keepSearching = true;
		long startingId = 0;
		ArrayList<JSONObject> matches = new ArrayList<JSONObject>();
		String URL = "https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=CBCB7ACCBD8383C77FD301512881A366";
		URL += "&game_mode=1";
		
		while (keepSearching){
			String tmpURL = URL;
			if (startingId != 0){
				tmpURL += "&start_at_match_id=" + startingId;
			}
		ArrayList<JSONObject> tmpMatches = new ArrayList<JSONObject>();
		if (!Radiant.isEmpty() || !Dire.isEmpty()){
			for(int hero : Radiant){
				tmpURL +="&hero_id=" + Integer.toString(hero);
				System.out.println(tmpURL);
				JSONObject response = new JSONObject(IOUtils.toString(new URL(tmpURL), Charset.forName("UTF-8")));
				JSONArray tmpMatchArray = response.getJSONObject("result").getJSONArray("matches");
				for (int i = 0; i < tmpMatchArray.length(); i++){
					if(!matchAlreadyInArray(tmpMatches, tmpMatchArray.getJSONObject(i)))
						tmpMatches.add(tmpMatchArray.getJSONObject(i));
				}
			}
			for(int hero : Dire){
				tmpURL += "&hero_id=" + Integer.toString(hero);
				System.out.println(tmpURL);
				JSONObject response = new JSONObject(IOUtils.toString(new URL(tmpURL), Charset.forName("UTF-8")));
				JSONArray tmpMatchArray = response.getJSONObject("result").getJSONArray("matches");
				for (int i = 0; i < tmpMatchArray.length(); i++){
					if(!matchAlreadyInArray(tmpMatches, tmpMatchArray.getJSONObject(i)))
						tmpMatches.add(tmpMatchArray.getJSONObject(i));
				}
			}
		}	
		System.out.println(tmpMatches.size());
		
		for(int i = 0; i < tmpMatches.size(); i++){
			boolean addMatch = true;
			JSONObject match = tmpMatches.get(i);
			JSONArray players = match.getJSONArray("players");
			if (players.length() != 10)
			{
				addMatch = false;
			}
			else if (!Radiant.isEmpty() || !Dire.isEmpty()){
				for (int r : Radiant){
					boolean rFound = false;
					for (int j = 0; j < 5; j++){
						JSONObject player = players.getJSONObject(j);
						if (player.getInt("hero_id") == r){
							rFound = true;
						}
					}
					if (!rFound){
						addMatch = false;
					}
				}
				for (int d : Dire){
					boolean dFound = false;
					for (int j = 5; j < 9; j++){
						JSONObject player = players.getJSONObject(j);
						if (player.getInt("hero_id") == d){
							dFound = true;
						}
					}
					if (!dFound){
						addMatch = false;
					}
				}
			}
			if (addMatch)
				matches.add(match);
		}
			if (tmpMatches.size() > 1){
				startingId = getUnsignedInt(tmpMatches.get(tmpMatches.size()-1).getInt("match_id"));
				//System.out.println(tmpMatches.get(tmpMatches.size()-1).getInt("start_time"));
			}
			else
				keepSearching = false;
		}
		
		return matches;
	}
	
	private boolean matchAlreadyInArray(ArrayList<JSONObject> array, JSONObject match){
		boolean inList = false;
		int inputId = match.getInt("match_id");
		for (int i = 0; i < array.size(); i++){
			if (inputId == array.get(i).getInt("match_id"))
				inList = true;
		}
		return inList;
	}
	
	public static long getUnsignedInt(int x) {
	    return x & (-1L >>> 32);
	}
}
