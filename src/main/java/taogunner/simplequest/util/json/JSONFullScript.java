package taogunner.simplequest.util.json;

import com.google.gson.annotations.Expose;

public class JSONFullScript
{
	public JFSNPC npc;
	JFSDialog[] dialogs;

	public class JFSNPC
	{
		public int quest_id;
		public int texture_id;
		public int item_id;
		public String name;
		public String description;
		
		public JFSNPC()
		{
			this.quest_id = 0;
			this.texture_id = 0;
			this.item_id = 0;
			this.name = "Simple Quest NPC";
			this.description = "Simple Quest NPC Description";
		}
	}
	
	public class JFSDialog
	{
		String text;
		JFSAnswer[] answers;
		String[] actions;
		
		public class JFSAnswer
		{
			String text;
			int jump;
			String[] conditions;
			
			public JFSAnswer()
			{
				this.jump = -1;
			}
		}
	}
}
