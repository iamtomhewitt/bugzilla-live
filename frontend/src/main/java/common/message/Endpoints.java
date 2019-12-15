package common.message;

public class Endpoints 
{
	public static final String GITHUB_RELEASES = "http://api.github.com/repos/iamtomhewitt/bugzilla-live/releases";
	
	
	public static final String LIST_CONTENTS(String listName) {
		return String.format("/list/%s/contents", listName);
	}

	public static final String LIST_MODIFY(String filename, String add, String remove) {
		return String.format("/list/modify?name=%s&add=%s&remove=%s", filename, add, remove);
	}
	
	public static final String LIST_ADD(String filename, String contents) {
		return String.format("/list/add?name=%s&contents=%s", filename, contents);
	}
	
	public static final String LIST_DELETE(String filename) {
		return String.format("/list/delete?name=%s", filename);
	}
	
	public static final String LISTS = "/list/lists";
	
	
	public static final String BUGS_NUMBERS(String numbers) {
		return String.format("/bugs/numbers?numbers=%s", numbers);
	}
	
	public static final String BUGS_EMAIL(String email) {
		return String.format("/bugs/email?email=%s", email);
	}
	
	public static final String BUGS_ATTACHMENTS(String number) {
		return String.format("/bugs/%s/attachments", number);
	}

	public static final String BUGS_COMMENTS(String number) {
		return String.format("/bugs/%s/comments", number);
	}

	
	public static final String CONFIG_SAVE(String key, String value)
	{
		return String.format("/config/save?key=%s&value=%s", key, value);
	}
	
	public static final String CONFIG_GET = "/config/get";
}
