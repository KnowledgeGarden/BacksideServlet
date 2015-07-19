/**
 * 
 */
package org.topicquests.backside.servlet;

import java.util.Map;

import org.nex.config.ConfigPullParser;
import org.topicquests.backside.servlet.apps.CredentialCache;
import org.topicquests.backside.servlet.apps.usr.UserModel;
import org.topicquests.backside.servlet.apps.usr.api.IUserModel;
import org.topicquests.topicmap.json.model.JSONTopicmapEnvironment;
import org.topicquests.topicmap.json.model.StatisticsUtility;
import org.topicquests.util.LoggingPlatform;

/**
 * @author park
 *
 */
public class ServletEnvironment {
	private LoggingPlatform log = LoggingPlatform.getInstance("logger.properties");
	private Map<String,Object>properties;
	private StatisticsUtility stats;
	private JSONTopicmapEnvironment tmEnvironment=null;
	private CredentialCache cache;
	private IUserModel userModel;
	private boolean isShutDown = false;
	
	
	/**
	 * @param isConsole <code>true</code> means boot JSONTopicMap console
	 */
	public ServletEnvironment(boolean isConsole) throws Exception {
		System.out.println("ServletEnvironment-");
		ConfigPullParser p = new ConfigPullParser("config-props.xml");
		properties = p.getProperties();
		System.out.println("PROPS "+properties);
		cache = new CredentialCache(this);
		//TODO build topicmap
		stats = new StatisticsUtility();
		userModel = new UserModel(this);
	//	tmEnvironment = new JSONTopicmapEnvironment(stats);
		isShutDown = false;
		System.out.println("ServletEnvironment+");
	}

	public IUserModel getUserModel() {
		return userModel;
	}
	public CredentialCache getCredentialCache() {
		return cache;
	}
	
	public JSONTopicmapEnvironment getTopicMapEnvironment() {
		return tmEnvironment;
	}
	
	public void shutDown() {
		//This might be called by several servlets
		if (!isShutDown) {
			log.shutDown();
			if (tmEnvironment != null)
				tmEnvironment.shutDown();
			isShutDown = true;
		}
	}
	////////////////////////
	// Utilities
	////////////////////////
	
	public String getStringProperty(String key) {
		return (String)properties.get(key);
	}
	
	public Object getProperty(String key) {
		return properties.get(key);
	}
	
	public void logDebug(String msg) {
		log.logDebug(msg);

	}

	public void logError(String msg, Exception e) {
		log.logError(msg, e);
	}

}
