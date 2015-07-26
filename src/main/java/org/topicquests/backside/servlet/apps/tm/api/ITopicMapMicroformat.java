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
		//GET
		GET_TOPIC				= "GetTopic",
		PUT_TOPIC				= "PutTopic",
		//POST
		REMOVE_TOPIC			= "RemTopic",
		//followed by cargo withspecs for the topic,
		//e.g. Locator = some value or not present, meaning needs locator
		// SuperType or ParentType, lavel, description, language, IsPrivate
		NEW_INSTANCE_TOPIC		= "NewInstance",
		NEW_SUBCLASS_TOPIC		= "NewSub";
	//attributes
	public static final String
		TOPIC_LOCATOR 		= "Locator",
		SUPERTYPE_LOCATOR	= "SType",
		PARENT_LOCATOR		= "PType",
		TOPIC_LABEL			= "Label",
		TOPIC_DETAILS		= "Details",
		//2-character code, e.g. "en"
		LANGUAGE			= "Lang",
		//nodes get images
		LARGE_IMAGE_PATH	= "LiP",
		SMALL_IMAGE_PATh	= "SiP",
		// "t" or "f" case insensitive
		IS_PRIVATE			= "IsPvt";
}
