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
		JFSAction actions;
		
		class JFSAnswer
		{
			String text = "EMPTY ANSWER";
			int jump = -1;
			JFSCondition conditions;
			
			class JFSCondition
			{
				JFSCItem[] item;
				JFSCParam[] param;
				JFSCQuest[] quest;
				JFSCTimestamp timestamp;
				
				class JFSCItem
				{
					int item_id = 0;
					int value = 0;
				}
				class JFSCParam
				{
					int param_id = 0;
					int value = 0;
					int logic = 0;
				}
				class JFSCQuest
				{
					int quest_id = 0;
					int value = 0;
					int logic = 0;
				}
				class JFSCTimestamp
				{
					boolean logic = true;
				}
			}
		}
		
		class JFSAction
		{
			JFSAItem[] item;
			JFSAParam[] param;
			JFSAQuest[] quest;
			JFSATeleport teleport;
			JFSATimestamp timestamp;
			String message;
			
			class JFSAItem
			{
				int item_id = 0;
				int value = 0;
			}
			class JFSAParam
			{
				int param_id = 0;
				int value = 0;
			}
			class JFSAQuest
			{
				int quest_id = 0;
				int value = 0;
			}
			class JFSATeleport
			{
				int x = 0;
				int y = 0;
				int z = 0;
			}
			class JFSATimestamp
			{
				int value = 0;
			}
		}
	}
}
