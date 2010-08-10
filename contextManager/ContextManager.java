/**
 * 
 */
package contextManager;

import java.math.BigInteger;

import ubiEvents.UbiEventListener;
import contextLoader.Context;
import data.live.Position;
import data.live.Tag;
import fourSquare.FourSquareService;

/**
 * @author Joe Sankey
 *
 */
public class ContextManager implements UbiEventListener{
	
	Context context;
	FourSquareService fourSquare;
	
	
	public ContextManager(Context context)
	{
		this.context = context;
		//fourSquare = new FourSquareService();
	}

	/* (non-Javadoc)
	 * @see ubiEvents.UbiEventListener#tagAdded(data.live.Tag)
	 */
	@Override
	public void tagAdded(Tag tag) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ubiEvents.UbiEventListener#tagStream(java.math.BigInteger, data.live.Position)
	 */
	@Override
	public void tagStream(BigInteger tagId, Position newPosition) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see ubiEvents.UbiEventListener#tagUpdated(data.live.Tag)
	 */
	@Override
	public void tagUpdated(Tag tag) {
		String vid;
		System.out.println("Checking!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	
		if((vid = context.containsTag(tag)) != null)
		{
			System.out.println("TAG IN ZONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		}
		//	fourSquare.checkin(vid);
	}

}
