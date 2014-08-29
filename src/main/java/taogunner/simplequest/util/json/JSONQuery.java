package taogunner.simplequest.util.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JSONQuery
{
	private StringBuilder jsonfull = new StringBuilder();
	
	public String JSONReadFile(int quest_id) throws IOException
	{
		String filePath = "./config/SimpleQuest/quest_" + quest_id + ".json";
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));
		String tempstring;
		while ((tempstring = bufReader.readLine()) != null) { jsonfull.append(tempstring + "\n"); }
		bufReader.close();
		return jsonfull.toString();
	}
}
