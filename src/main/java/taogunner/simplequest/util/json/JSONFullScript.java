package taogunner.simplequest.util.json;

public class JSONFullScript
{
	public JFSNPC npc;
	JFSDialog[] dialogs;
	
	public class JFSNPC
	{
		public int quest_id = 0;
		public int texture_id = 0;
		public int item_id = 0;
		public String name = "Simple Quest NPC";
		public String description = "Simple Quest NPC Description";
	}
	
	class JFSDialog
	{
		String text = "EMPTY DIALOG";
		JFSAnswer[] answers;
		String[] actions;
		
		class JFSAnswer
		{
			String text = "EMPTY ANSWER";
			int jump = -1;
			String[] conditions;
		}
	}
}
