package GoogleTalkInterface;

import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 * 
 */

/**
 * @author Joe Sankey
 *
 */
public class GoogleTalkInterface {

	static XMPPConnection connection = new XMPPConnection("gmail.com");
	
	static String username;
	static String password;
	
	public GoogleTalkInterface(String username, String password)
	{
		GoogleTalkInterface.username = username;
		GoogleTalkInterface.password = password;
		
		try {
			connection.connect();
			connection.login(username, password, "Talk.v80");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void setStatus(String status){
		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		
		//Login with Username and password

		
		// set presence status info
		Presence presence = new Presence(Presence.Type.available);
		
		presence.setStatus(status);
		
		//Highest priority sets it to the status text
		presence.setPriority(24);
		
		connection.sendPacket(presence);
	}
}
