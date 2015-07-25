/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;

/**
 * @author park
 *
 */
public interface ITopicMapMicroformat extends ICredentialsMicroformat {
	//Verbs
	public static final String
		GET_TOPIC		= "GetTopic",
		PUT_TOPIC		= "PutTopic";
	
	public static final String
		TOPIC_LOCATOR 	= "Locator";
}
