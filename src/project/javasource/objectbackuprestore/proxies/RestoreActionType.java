// This file was generated by Mendix Business Modeler.
//
// WARNING: Code you write here will be lost the next time you deploy the project.

package objectbackuprestore.proxies;

public enum RestoreActionType
{
	ReadSummary(new String[][] { new String[] { "en_US", "Read summary" }, new String[] { "nl_NL", "Read summary" } }),
	ReadDetails(new String[][] { new String[] { "en_US", "Read details" }, new String[] { "nl_NL", "Read details" } }),
	UpdateDatabase(new String[][] { new String[] { "en_US", "Update database" }, new String[] { "nl_NL", "Update database" } });

	private java.util.Map<String,String> captions;

	private RestoreActionType(String[][] captionStrings)
	{
		this.captions = new java.util.HashMap<String,String>();
		for (String[] captionString : captionStrings)
			captions.put(captionString[0], captionString[1]);
	}

	public String getCaption(String languageCode)
	{
		if (captions.containsKey(languageCode))
			return captions.get(languageCode);
		return captions.get("en_US");
	}

	public String getCaption()
	{
		return captions.get("en_US");
	}
}
