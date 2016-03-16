import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) throws JSONException, MalformedURLException, IOException {
		// TODO Auto-generated method stub
		String key = "CBCB7ACCBD8383C77FD301512881A366";
		ArrayList<Integer> Radiant = new ArrayList<Integer>();
		ArrayList<Integer> Dire = new ArrayList<Integer>();
		Radiant.add(1);
		Radiant.add(63);
		Radiant.add(59);
//		Radiant.add(37);
//		Radiant.add(68);
		Searcher test1 = new Searcher(key, Radiant, Dire);
		
		ArrayList<JSONObject> matches = test1.getMatches();
		for (JSONObject match : matches){
			System.out.println(getUnsignedInt(match.getInt("match_id")));
			System.out.println(match.getJSONArray("players"));
		}
	}
	public static long getUnsignedInt(int x) {
	    return x & (-1L >>> 32);
	}
}
