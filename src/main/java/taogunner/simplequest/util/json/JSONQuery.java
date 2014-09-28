package taogunner.simplequest.util.json;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JSONQuery
{
	public static String JSONReadFile(int quest_id)
	{
		String filePath = "./config/SimpleQuest/quest_" + quest_id + ".json";
		StringBuilder jsonfull = new StringBuilder();
		try
		{
			BufferedReader bufReader = null;
			bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));
			jsonfull = new StringBuilder();
			String tempstring;
			while ((tempstring = bufReader.readLine()) != null) { jsonfull.append(tempstring + "\n"); }
			bufReader.close();
		}
		catch (FileNotFoundException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); } 
		return jsonfull.toString();
	}
}
