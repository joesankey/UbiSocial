/**
 * 
 */
package fourSquare;

import clientConnector.ClientConnector;
import contextLoader.Context;
import contextManager.ContextManager;
import data.live.TagManager;

/**
 * @author Joe Sankey
 *
 */
public class App {

	public static void main(String args [])
	{
		TagManager tagManager = new TagManager();
		
		final ClientConnector clientConnector = new ClientConnector(tagManager);
		
		Context context = new Context();
		
		tagManager.addTag("020000007106");
		
		
		
		context.loadBuilding(1);
		
		context.print();
		
		ContextManager contextManager = new ContextManager(context);
		
		tagManager.addUbiEventListener(contextManager);
		
		clientConnector.connect("192.168.33.39", 12000);
	}
	
	
	
}
