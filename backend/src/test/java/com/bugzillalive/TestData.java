package com.bugzillalive;

public class TestData {

	public final static String BUG_COMMENT = "{\n" +
		"\t\"bugs\": {\n" +
		"\t\t\"12345\": {\n" +
		"\t\t\t\"comments\": [\n" +
		"\t\t\t\t{\n" +
		"\t\t\t\t\t\"tags\": [],\n" +
		"\t\t\t\t\t\"attachment_id\": null,\n" +
		"\t\t\t\t\t\"author\": \"someone@gmail.com\",\n" +
		"\t\t\t\t\t\"bug_id\": 12345,\n" +
		"\t\t\t\t\t\"creation_time\": \"2020-09-27T20:44:00Z\",\n" +
		"\t\t\t\t\t\"is_private\": false,\n" +
		"\t\t\t\t\t\"text\": \"text\",\n" +
		"\t\t\t\t\t\"raw_text\": \"text\",\n" +
		"\t\t\t\t\t\"time\": \"2020-09-27T20:44:00Z\",\n" +
		"\t\t\t\t\t\"creator\": \"someone@gmail.com\",\n" +
		"\t\t\t\t\t\"id\": 92463,\n" +
		"\t\t\t\t\t\"count\": 0\n" +
		"\t\t\t\t}\n" +
		"\t\t\t]\n" +
		"\t\t}\n" +
		"\t},\n" +
		"\t\"comments\": {}\n" +
		"}";

	public final static String BUG_ATTACHMENT = "{\n" +
		"    \"attachments\": {},\n" +
		"    \"bugs\": {\n" +
		"        \"12345\": [\n" +
		"            {\n" +
		"                \"creator_detail\": {\n" +
		"                    \"nick\": \"someone\",\n" +
		"                    \"id\": 406194,\n" +
		"                    \"email\": \"someone@gmail.com\",\n" +
		"                    \"real_name\": \"Someone\",\n" +
		"                    \"name\": \"someone@gmail.com\"\n" +
		"                },\n" +
		"                \"attacher\": \"someone@gmail.com\",\n" +
		"                \"size\": 982,\n" +
		"                \"summary\": \"Some patch.\",\n" +
		"                \"last_change_time\": \"2015-09-23T18:36:19Z\",\n" +
		"                \"id\": 8665038,\n" +
		"                \"bug_id\": 12345,\n" +
		"                \"content_type\": \"text/plain\",\n" +
		"                \"creation_time\": \"2015-09-23T18:35:48Z\",\n" +
		"                \"is_obsolete\": 1,\n" +
		"                \"flags\": [],\n" +
		"                \"file_name\": \"Bug-12345---Some-patch.patch\",\n" +
		"                \"is_private\": 0,\n" +
		"                \"description\": \"Some patch.\",\n" +
		"                \"creator\": \"someone@gmail.com\",\n" +
		"                \"is_patch\": 1,\n" +
		"                \"data\": \"IyBIRyBjaGF\"\n" +
		"            }\n" +
		"        ]\n" +
		"    }\n" +
		"}";
}
