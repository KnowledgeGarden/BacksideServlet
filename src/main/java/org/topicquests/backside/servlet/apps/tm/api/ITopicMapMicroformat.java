/**
 * 
 */
package org.topicquests.backside.servlet.apps.tm.api;

import org.topicquests.backside.servlet.api.ICredentialsMicroformat;
import org.topicquests.common.api.ITopicQuestsOntology;

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
		NEW_SUBCLASS_TOPIC		= "NewSub",
		//allows to add key/value pairs and special items
		ADD_FEATURES_TO_TOPIC	= "AddFeatures",
		LIST_INSTANCE_TOPICS	= "ListInstances";
	
	//attributes
	public static final String
		TOPIC_LOCATOR 		= ITopicQuestsOntology.LOCATOR_PROPERTY,
		SUPERTYPE_LOCATOR	= ITopicQuestsOntology.SUBCLASS_OF_PROPERTY_TYPE,
		PARENT_LOCATOR		= ITopicQuestsOntology.INSTANCE_OF_PROPERTY_TYPE,
		TOPIC_LABEL			= ITopicQuestsOntology.LABEL_PROPERTY,
		TOPIC_DETAILS		= ITopicQuestsOntology.DETAILS_PROPERTY,
		//2-character code, e.g. "en"
		LANGUAGE			= "Lang",
		//nodes get images
		LARGE_IMAGE_PATH	= ITopicQuestsOntology.LARGE_IMAGE_PATH,
		SMALL_IMAGE_PATh	= ITopicQuestsOntology.SMALL_IMAGE_PATH,
		// "t" or "f" case insensitive
		IS_PRIVATE			= ITopicQuestsOntology.IS_PRIVATE_PROPERTY,
		TAG_NAMES			= "TagNames";
}
