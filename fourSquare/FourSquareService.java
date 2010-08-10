/**
 * 
 */
package fourSquare;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author Joe Sankey
 *
 */
public class FourSquareService {

	org.apache.http.client.HttpClient httpClient;
	OAuthConsumer consumer;
	private static final String PROXY_HOST = "proxy.dcu.ie";
	private static final int PROXY_PORT = 3128;
	

	public FourSquareService()
	{
		try{
		httpClient = new DefaultHttpClient();
		consumer = new CommonsHttpOAuthConsumer(
				"2BIQFO0NUYTZJGOR12EVBMYYVT2XDMHNTPWZLMHRXYFTY10P", 
		"SEDZTVVM43IKEYX2U0FAKQYG1UWTKUPFLOBKMME4IREL5QRF"); 		
		HttpGet reqLogin = new HttpGet("http://api.foursquare.com/v1/authexchange?fs_username=ubisense@notesnapper.com&fs_password=four8ide");
		consumer.sign(reqLogin);
		HttpResponse resLogin = httpClient.execute(reqLogin);
		if (resLogin.getEntity() == null) {
			System.out.println("FAILED TO CONNECT.");
			return;
		}
		Document document = 
			DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(resLogin.getEntity().getContent());
		Element eOAuthToken = 
			(Element) document.getElementsByTagName("oauth_token").item(0);
		String sOAuthToken =  eOAuthToken.getTextContent();
		System.out.println("oauth_token:"+sOAuthToken);
		Element eOAuthTokenSecret = 
			(Element) document.getElementsByTagName("oauth_token_secret").item(0);
		String sOAuthTokenSecret =  eOAuthTokenSecret.getTextContent();
		System.out.println("oauth_token_secret:"+sOAuthTokenSecret);
		consumer.setTokenWithSecret(sOAuthToken, sOAuthTokenSecret);
		}catch(ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (OAuthMessageSignerException e) {
			e.printStackTrace();
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		}
	}

	public void checkin(String id){
		try{
		HttpPost testPost = new HttpPost("http://api.foursquare.com/v1/checkin.json?vid=" + id);
           
        consumer.sign(testPost);
     
        HttpResponse testCheckin = httpClient.execute(testPost);
        
		if (testCheckin.getEntity() == null) {
			System.out.println("Failed to checkin.");
		}
		InputStream is1 = testCheckin.getEntity().getContent();
		for (int i = is1.read(); i > -1; i = is1.read()) {
			System.out.write(i);
		}
		
		}catch(OAuthMessageSignerException e)
		{
			
		} catch (OAuthExpectationFailedException e) {
			e.printStackTrace();
		} catch (OAuthCommunicationException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args)
	{
		FourSquareService four = new FourSquareService();
		
		four.checkin("7001240");
	}


}
