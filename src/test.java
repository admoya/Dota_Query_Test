import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class test {

	public static void main(String[] args) throws JSONException, MalformedURLException, IOException {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject(IOUtils.toString(new URL("https://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/V001/?key=CBCB7ACCBD8383C77FD301512881A366"), Charset.forName("UTF-8")));
		JSONArray array = json.getJSONObject("result").getJSONArray("matches");
		for (int i = 0; i < array.length(); i++){
			System.out.println(getUnsignedInt(array.getJSONObject(i).getInt("match_id")));
		}
		JSONArray players = array.getJSONObject(0).getJSONArray("players");
		System.out.println(players.getJSONObject(0));
	}
	public static long getUnsignedInt(int x) {
	    return x & (-1L >>> 32);
	}
}


