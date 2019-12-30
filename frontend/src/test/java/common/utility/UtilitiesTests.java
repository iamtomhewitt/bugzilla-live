package common.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import common.bug.Bug;
import common.bug.BugAttachment;
import common.bug.BugComment;
import common.error.JsonTransformationException;
import common.utility.JacksonAdapter;

public class UtilitiesTests
{
	@SuppressWarnings("unchecked")
	@Test
	public void jacksonCanTurnBugsJsonIntoListOfBugType() throws JSONException, JsonTransformationException
	{
		String json = testBugJson;
		JSONObject jsonObject = new JSONObject(json);
		
		List<Bug> bugs = JacksonAdapter.fromJson(jsonObject.get("bugs").toString(), Bug.class);

		assertNotNull(bugs);
		assertEquals(2, bugs.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void jacksonCanTurnBugCommentsJsonIntoListOfBugType() throws JSONException, JsonTransformationException
	{
		String json = testBugCommentsJson;
		JSONObject jsonObject = new JSONObject(json);
		String bug = jsonObject.getJSONObject("bugs").getJSONObject("22345").getJSONArray("comments").toString();
		List<BugComment> comments = JacksonAdapter.fromJson(bug, BugComment.class);

		assertNotNull(comments);
		assertEquals(7, comments.size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void jacksonCanTurnBugAttachmentsJsonIntoListOfBugType() throws JSONException, JsonTransformationException
	{
		String json = testBugAttachmentJson;
		JSONObject jsonObject = new JSONObject(json);
		String bug = jsonObject.getJSONObject("bugs").getJSONArray("12345").toString();
		List<BugAttachment> attachments = JacksonAdapter.fromJson(bug, BugAttachment.class);

		assertNotNull(attachments);
		assertEquals(1, attachments.size());
	}
	
	private String testBugJson = "{\r\n" + 
			"    \"type\": \"bugResponse\",\r\n" + 
			"    \"operation\": \"bugs\",\r\n" + 
			"    \"status\": 200,\r\n" + 
			"    \"bugs\": [\r\n" + 
			"        {\r\n" + 
			"            \"blocks\": [\r\n" + 
			"                11091,\r\n" + 
			"                17976\r\n" + 
			"            ],\r\n" + 
			"            \"is_cc_accessible\": true,\r\n" + 
			"            \"cf_tracking_seamonkey257esr\": \"---\",\r\n" + 
			"            \"keywords\": [],\r\n" + 
			"            \"cf_qa_whiteboard\": \"\",\r\n" + 
			"            \"url\": \"\",\r\n" + 
			"            \"qa_contact\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"            \"cf_tracking_thunderbird_72\": \"---\",\r\n" + 
			"            \"regressions\": [],\r\n" + 
			"            \"cf_status_thunderbird_71\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_72\": \"---\",\r\n" + 
			"            \"cf_fx_iteration\": \"---\",\r\n" + 
			"            \"cf_tracking_firefox_relnote\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_68\": \"---\",\r\n" + 
			"            \"cf_last_resolved\": \"1999-11-04T19:50:21Z\",\r\n" + 
			"            \"cf_fx_points\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_69\": \"---\",\r\n" + 
			"            \"cf_status_seamonkey257esr\": \"---\",\r\n" + 
			"            \"cc_detail\": [\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"esther@formerly-netscape.com.tld\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"esther@formerly-netscape.com.tld\",\r\n" + 
			"                    \"nick\": \"esther\",\r\n" + 
			"                    \"id\": 4114\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"jagadeeshmce@gmail.com\",\r\n" + 
			"                    \"real_name\": \"jagadeesh\",\r\n" + 
			"                    \"name\": \"jagadeeshmce@gmail.com\",\r\n" + 
			"                    \"nick\": \"jagadeeshmce\",\r\n" + 
			"                    \"id\": 287841\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"kesarirajesh@yahoo.com\",\r\n" + 
			"                    \"real_name\": \"rajesh kesari\",\r\n" + 
			"                    \"name\": \"kesarirajesh@yahoo.com\",\r\n" + 
			"                    \"nick\": \"kesarirajesh\",\r\n" + 
			"                    \"id\": 193982\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"nick\": \"lchiang\",\r\n" + 
			"                    \"id\": 4080\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"momoi@alumni.iu.edu\",\r\n" + 
			"                    \"real_name\": \"Katsuhiko  Momoi\",\r\n" + 
			"                    \"name\": \"momoi@alumni.iu.edu\",\r\n" + 
			"                    \"nick\": \"momoi\",\r\n" + 
			"                    \"id\": 1308\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"msanz@formerly-netscape.com.tld\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"msanz@formerly-netscape.com.tld\",\r\n" + 
			"                    \"nick\": \"msanz\",\r\n" + 
			"                    \"id\": 4338\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"nhottanscp@yahoo.co.jp\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"nhottanscp@yahoo.co.jp\",\r\n" + 
			"                    \"nick\": \"nhottanscp\",\r\n" + 
			"                    \"id\": 4467\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"ruelscreenprint@outlook.ph\",\r\n" + 
			"                    \"real_name\": \"Ruel Balabis\",\r\n" + 
			"                    \"name\": \"ruelscreenprint@outlook.ph\",\r\n" + 
			"                    \"nick\": \"ruelscreenprint\",\r\n" + 
			"                    \"id\": 541943\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"tymerkaev@gmail.com\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"tymerkaev@gmail.com\",\r\n" + 
			"                    \"nick\": \"tymerkaev\",\r\n" + 
			"                    \"id\": 356256\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"vnakumari@gmail.com\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"vnakumari@gmail.com\",\r\n" + 
			"                    \"nick\": \"vnakumari\",\r\n" + 
			"                    \"id\": 527136\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"vseerror@lehigh.edu\",\r\n" + 
			"                    \"real_name\": \"Wayne Mery (:wsmwk)\",\r\n" + 
			"                    \"name\": \"vseerror@lehigh.edu\",\r\n" + 
			"                    \"nick\": \"wsmwk\",\r\n" + 
			"                    \"id\": 29811\r\n" + 
			"                }\r\n" + 
			"            ],\r\n" + 
			"            \"summary\": \"[DOGFOOD] Unable to Forward a message received as an Inline page or an attachment\",\r\n" + 
			"            \"cf_user_story\": \"\",\r\n" + 
			"            \"platform\": \"x86\",\r\n" + 
			"            \"votes\": 0,\r\n" + 
			"            \"version\": \"Trunk\",\r\n" + 
			"            \"mentors_detail\": [],\r\n" + 
			"            \"cf_tracking_thunderbird_70\": \"---\",\r\n" + 
			"            \"is_creator_accessible\": true,\r\n" + 
			"            \"is_confirmed\": true,\r\n" + 
			"            \"priority\": \"P1\",\r\n" + 
			"            \"cf_status_thunderbird_esr60\": \"---\",\r\n" + 
			"            \"assigned_to_detail\": {\r\n" + 
			"                \"email\": \"jefft@formerly-netscape.com.tld\",\r\n" + 
			"                \"real_name\": \"\",\r\n" + 
			"                \"name\": \"jefft@formerly-netscape.com.tld\",\r\n" + 
			"                \"nick\": \"jefft\",\r\n" + 
			"                \"id\": 4478\r\n" + 
			"            },\r\n" + 
			"            \"creator\": \"marina@formerly-netscape.com.tld\",\r\n" + 
			"            \"last_change_time\": \"2015-09-23T18:36:19Z\",\r\n" + 
			"            \"comment_count\": 19,\r\n" + 
			"            \"creator_detail\": {\r\n" + 
			"                \"email\": \"marina@formerly-netscape.com.tld\",\r\n" + 
			"                \"real_name\": \"\",\r\n" + 
			"                \"name\": \"marina@formerly-netscape.com.tld\",\r\n" + 
			"                \"nick\": \"marina\",\r\n" + 
			"                \"id\": 4648\r\n" + 
			"            },\r\n" + 
			"            \"cc\": [\r\n" + 
			"                \"esther@formerly-netscape.com.tld\",\r\n" + 
			"                \"jagadeeshmce@gmail.com\",\r\n" + 
			"                \"kesarirajesh@yahoo.com\",\r\n" + 
			"                \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"momoi@alumni.iu.edu\",\r\n" + 
			"                \"msanz@formerly-netscape.com.tld\",\r\n" + 
			"                \"nhottanscp@yahoo.co.jp\",\r\n" + 
			"                \"ruelscreenprint@outlook.ph\",\r\n" + 
			"                \"tymerkaev@gmail.com\",\r\n" + 
			"                \"vnakumari@gmail.com\",\r\n" + 
			"                \"vseerror@lehigh.edu\"\r\n" + 
			"            ],\r\n" + 
			"            \"duplicates\": [],\r\n" + 
			"            \"cf_tracking_thunderbird_esr60\": \"---\",\r\n" + 
			"            \"assigned_to\": \"jefft@formerly-netscape.com.tld\",\r\n" + 
			"            \"groups\": [],\r\n" + 
			"            \"see_also\": [],\r\n" + 
			"            \"id\": 12345,\r\n" + 
			"            \"whiteboard\": \"[PDT+][PR1]\",\r\n" + 
			"            \"creation_time\": \"1999-08-23T23:34:25Z\",\r\n" + 
			"            \"depends_on\": [\r\n" + 
			"                15069\r\n" + 
			"            ],\r\n" + 
			"            \"mentors\": [],\r\n" + 
			"            \"dupe_of\": null,\r\n" + 
			"            \"regressed_by\": [],\r\n" + 
			"            \"cf_status_thunderbird_70\": \"---\",\r\n" + 
			"            \"qa_contact_detail\": {\r\n" + 
			"                \"email\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"real_name\": \"\",\r\n" + 
			"                \"name\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"nick\": \"lchiang\",\r\n" + 
			"                \"id\": 4080\r\n" + 
			"            },\r\n" + 
			"            \"resolution\": \"FIXED\",\r\n" + 
			"            \"cf_tracking_thunderbird_73\": \"---\",\r\n" + 
			"            \"type\": \"defect\",\r\n" + 
			"            \"classification\": \"Components\",\r\n" + 
			"            \"alias\": null,\r\n" + 
			"            \"op_sys\": \"Windows NT\",\r\n" + 
			"            \"status\": \"VERIFIED\",\r\n" + 
			"            \"cf_tracking_thunderbird_esr68\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_69\": \"---\",\r\n" + 
			"            \"cf_crash_signature\": \"\",\r\n" + 
			"            \"is_open\": false,\r\n" + 
			"            \"severity\": \"normal\",\r\n" + 
			"            \"cf_tracking_seamonkey253\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_71\": \"---\",\r\n" + 
			"            \"flags\": [],\r\n" + 
			"            \"cf_status_seamonkey253\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_73\": \"---\",\r\n" + 
			"            \"component\": \"Backend\",\r\n" + 
			"            \"cf_tracking_seamonkey249\": \"---\",\r\n" + 
			"            \"cf_status_seamonkey249\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_68\": \"---\",\r\n" + 
			"            \"product\": \"MailNews Core\",\r\n" + 
			"            \"target_milestone\": \"M11\",\r\n" + 
			"            \"cf_status_thunderbird_esr68\": \"---\"\r\n" + 
			"        },\r\n" + 
			"        {\r\n" + 
			"            \"blocks\": [],\r\n" + 
			"            \"is_cc_accessible\": true,\r\n" + 
			"            \"cf_tracking_seamonkey257esr\": \"---\",\r\n" + 
			"            \"keywords\": [],\r\n" + 
			"            \"cf_qa_whiteboard\": \"\",\r\n" + 
			"            \"url\": \"\",\r\n" + 
			"            \"qa_contact\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"            \"cf_tracking_thunderbird_72\": \"---\",\r\n" + 
			"            \"regressions\": [],\r\n" + 
			"            \"cf_status_thunderbird_71\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_72\": \"---\",\r\n" + 
			"            \"cf_fx_iteration\": \"---\",\r\n" + 
			"            \"cf_tracking_firefox_relnote\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_68\": \"---\",\r\n" + 
			"            \"cf_last_resolved\": \"2000-02-12T04:36:03Z\",\r\n" + 
			"            \"cf_fx_points\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_69\": \"---\",\r\n" + 
			"            \"cf_status_seamonkey257esr\": \"---\",\r\n" + 
			"            \"cc_detail\": [\r\n" + 
			"                {\r\n" + 
			"                    \"email\": \"chrisn@statecollege.com\",\r\n" + 
			"                    \"real_name\": \"\",\r\n" + 
			"                    \"name\": \"chrisn@statecollege.com\",\r\n" + 
			"                    \"nick\": \"chrisn\",\r\n" + 
			"                    \"id\": 3835\r\n" + 
			"                }\r\n" + 
			"            ],\r\n" + 
			"            \"summary\": \"Get error \\\"no such article\\\" on a particular news message\",\r\n" + 
			"            \"cf_user_story\": \"\",\r\n" + 
			"            \"platform\": \"x86\",\r\n" + 
			"            \"votes\": 0,\r\n" + 
			"            \"version\": \"Trunk\",\r\n" + 
			"            \"mentors_detail\": [],\r\n" + 
			"            \"cf_tracking_thunderbird_70\": \"---\",\r\n" + 
			"            \"is_creator_accessible\": true,\r\n" + 
			"            \"is_confirmed\": true,\r\n" + 
			"            \"priority\": \"P3\",\r\n" + 
			"            \"cf_status_thunderbird_esr60\": \"---\",\r\n" + 
			"            \"assigned_to_detail\": {\r\n" + 
			"                \"email\": \"sspitzer@mozilla.org\",\r\n" + 
			"                \"real_name\": \"(not reading, please use seth@sspitzer.org instead)\",\r\n" + 
			"                \"name\": \"sspitzer@mozilla.org\",\r\n" + 
			"                \"nick\": \"sspitzer\",\r\n" + 
			"                \"id\": 1193\r\n" + 
			"            },\r\n" + 
			"            \"creator\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"            \"last_change_time\": \"2011-08-06T05:44:59Z\",\r\n" + 
			"            \"comment_count\": 7,\r\n" + 
			"            \"creator_detail\": {\r\n" + 
			"                \"email\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"real_name\": \"\",\r\n" + 
			"                \"name\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"nick\": \"lchiang\",\r\n" + 
			"                \"id\": 4080\r\n" + 
			"            },\r\n" + 
			"            \"cc\": [\r\n" + 
			"                \"chrisn@statecollege.com\"\r\n" + 
			"            ],\r\n" + 
			"            \"duplicates\": [],\r\n" + 
			"            \"cf_tracking_thunderbird_esr60\": \"---\",\r\n" + 
			"            \"assigned_to\": \"sspitzer@mozilla.org\",\r\n" + 
			"            \"groups\": [],\r\n" + 
			"            \"see_also\": [],\r\n" + 
			"            \"id\": 22345,\r\n" + 
			"            \"whiteboard\": \"\",\r\n" + 
			"            \"creation_time\": \"1999-12-22T01:35:05Z\",\r\n" + 
			"            \"depends_on\": [],\r\n" + 
			"            \"mentors\": [],\r\n" + 
			"            \"dupe_of\": 26944,\r\n" + 
			"            \"regressed_by\": [],\r\n" + 
			"            \"cf_status_thunderbird_70\": \"---\",\r\n" + 
			"            \"qa_contact_detail\": {\r\n" + 
			"                \"email\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"real_name\": \"\",\r\n" + 
			"                \"name\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                \"nick\": \"lchiang\",\r\n" + 
			"                \"id\": 4080\r\n" + 
			"            },\r\n" + 
			"            \"resolution\": \"DUPLICATE\",\r\n" + 
			"            \"cf_tracking_thunderbird_73\": \"---\",\r\n" + 
			"            \"type\": \"defect\",\r\n" + 
			"            \"classification\": \"Components\",\r\n" + 
			"            \"alias\": null,\r\n" + 
			"            \"op_sys\": \"Windows NT\",\r\n" + 
			"            \"status\": \"VERIFIED\",\r\n" + 
			"            \"cf_tracking_thunderbird_esr68\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_69\": \"---\",\r\n" + 
			"            \"cf_crash_signature\": \"\",\r\n" + 
			"            \"is_open\": false,\r\n" + 
			"            \"severity\": \"normal\",\r\n" + 
			"            \"cf_tracking_seamonkey253\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_71\": \"---\",\r\n" + 
			"            \"flags\": [],\r\n" + 
			"            \"cf_status_seamonkey253\": \"---\",\r\n" + 
			"            \"cf_status_thunderbird_73\": \"---\",\r\n" + 
			"            \"component\": \"Networking\",\r\n" + 
			"            \"cf_tracking_seamonkey249\": \"---\",\r\n" + 
			"            \"cf_status_seamonkey249\": \"---\",\r\n" + 
			"            \"cf_tracking_thunderbird_68\": \"---\",\r\n" + 
			"            \"product\": \"MailNews Core\",\r\n" + 
			"            \"target_milestone\": \"M13\",\r\n" + 
			"            \"cf_status_thunderbird_esr68\": \"---\"\r\n" + 
			"        }\r\n" + 
			"    ]\r\n" + 
			"}";
	private String testBugCommentsJson = "{\r\n" + 
			"    \"type\": \"bugResponse\",\r\n" + 
			"    \"operation\": \"bugs\",\r\n" + 
			"    \"status\": 200,\r\n" + 
			"    \"bugs\": {\r\n" + 
			"        \"22345\": {\r\n" + 
			"            \"comments\": [\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 0,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"time\": \"1999-12-22T01:35:05Z\",\r\n" + 
			"                    \"author\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"[BUGDAY]  Get error \\\"no such article\\\" on a particular news message\\n\\nSeen on M12 Win32 build 12/21/1999\\n\\n1)  Subscribe to n.p.m.layout\\n2)  Select the message in the thread pane by Todd Fahrner dated 12/21/1999\\n3:24pm titled \\\"RE: xp logical resolution\\\"\\n3)  Get error message:  A News NNTP error occurred:  No such article\\n\\nThis news posting displays fine in 4.x so the posting wasn't cancelled.\",\r\n" + 
			"                    \"id\": 158239,\r\n" + 
			"                    \"creation_time\": \"1999-12-22T01:35:05Z\",\r\n" + 
			"                    \"raw_text\": \"[BUGDAY]  Get error \\\"no such article\\\" on a particular news message\\n\\nSeen on M12 Win32 build 12/21/1999\\n\\n1)  Subscribe to n.p.m.layout\\n2)  Select the message in the thread pane by Todd Fahrner dated 12/21/1999\\n3:24pm titled \\\"RE: xp logical resolution\\\"\\n3)  Get error message:  A News NNTP error occurred:  No such article\\n\\nThis news posting displays fine in 4.x so the posting wasn't cancelled.\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 1,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"time\": \"1999-12-22T03:41:59Z\",\r\n" + 
			"                    \"author\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"accepting, marking m13.\",\r\n" + 
			"                    \"id\": 158240,\r\n" + 
			"                    \"creation_time\": \"1999-12-22T03:41:59Z\",\r\n" + 
			"                    \"raw_text\": \"accepting, marking m13.\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 2,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"time\": \"1999-12-22T05:43:59Z\",\r\n" + 
			"                    \"author\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"fixed.\\n\\nthe problem was we were unescaping the message id, when we shouldn't.\\n\\nthe message id for this post was\\nnews://news.mozilla.org/B4854C33.53FD%25fahrner@pobox.com\\n\\nwhen we unescaped %25, and tried to as the server for it, it barfed.\",\r\n" + 
			"                    \"id\": 158241,\r\n" + 
			"                    \"creation_time\": \"1999-12-22T05:43:59Z\",\r\n" + 
			"                    \"raw_text\": \"fixed.\\n\\nthe problem was we were unescaping the message id, when we shouldn't.\\n\\nthe message id for this post was\\nnews://news.mozilla.org/B4854C33.53FD%25fahrner@pobox.com\\n\\nwhen we unescaped %25, and tried to as the server for it, it barfed.\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 3,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"asa@mozilla.org\",\r\n" + 
			"                    \"time\": \"2000-01-23T15:02:05Z\",\r\n" + 
			"                    \"author\": \"asa@mozilla.org\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"I think we can clear this one out.  Verified Fixed.\",\r\n" + 
			"                    \"id\": 170814,\r\n" + 
			"                    \"creation_time\": \"2000-01-23T15:02:05Z\",\r\n" + 
			"                    \"raw_text\": \"I think we can clear this one out.  Verified Fixed.\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 4,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"asa@mozilla.org\",\r\n" + 
			"                    \"time\": \"2000-02-12T04:28:24Z\",\r\n" + 
			"                    \"author\": \"asa@mozilla.org\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"I think that this is back.  I'm getting the error on quite a few of the news \\nmessages in n.p.m.ui  I verified (in 4.x) that the news message does indeed \\nexist.  An example (one of many) is Jonas Sicking Re: changing windows font post \\nfrom 2/11/00 at 2:20 PM in netscape.public.mozilla.ui  If this is something \\ndifferent that it was last time I'll gladly slpit out a new bug but I don't know \\nhow to determine this.\",\r\n" + 
			"                    \"id\": 193767,\r\n" + 
			"                    \"creation_time\": \"2000-02-12T04:28:24Z\",\r\n" + 
			"                    \"raw_text\": \"I think that this is back.  I'm getting the error on quite a few of the news \\nmessages in n.p.m.ui  I verified (in 4.x) that the news message does indeed \\nexist.  An example (one of many) is Jonas Sicking Re: changing windows font post \\nfrom 2/11/00 at 2:20 PM in netscape.public.mozilla.ui  If this is something \\ndifferent that it was last time I'll gladly slpit out a new bug but I don't know \\nhow to determine this.\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 5,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"time\": \"2000-02-12T04:36:03Z\",\r\n" + 
			"                    \"author\": \"sspitzer@mozilla.org\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"it is back\\n\\nI have the fix in my tree.\\n\\nthis is now bug #26944\\n\\n*** This bug has been marked as a duplicate of 26944 ***\",\r\n" + 
			"                    \"id\": 193772,\r\n" + 
			"                    \"creation_time\": \"2000-02-12T04:36:03Z\",\r\n" + 
			"                    \"raw_text\": \"it is back\\n\\nI have the fix in my tree.\\n\\nthis is now bug #26944\\n\\n*** This bug has been marked as a duplicate of 26944 ***\"\r\n" + 
			"                },\r\n" + 
			"                {\r\n" + 
			"                    \"is_private\": false,\r\n" + 
			"                    \"count\": 6,\r\n" + 
			"                    \"attachment_id\": null,\r\n" + 
			"                    \"creator\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"time\": \"2000-02-12T07:38:31Z\",\r\n" + 
			"                    \"author\": \"lchiang@formerly-netscape.com.tld\",\r\n" + 
			"                    \"bug_id\": 22345,\r\n" + 
			"                    \"tags\": [],\r\n" + 
			"                    \"text\": \"Ok, I'll mark verified.\",\r\n" + 
			"                    \"id\": 193822,\r\n" + 
			"                    \"creation_time\": \"2000-02-12T07:38:31Z\",\r\n" + 
			"                    \"raw_text\": \"Ok, I'll mark verified.\"\r\n" + 
			"                }\r\n" + 
			"            ]\r\n" + 
			"        }\r\n" + 
			"    }\r\n" + 
			"}";
	private String testBugAttachmentJson = "{\r\n" + 
			"    \"type\": \"bugResponse\",\r\n" + 
			"    \"operation\": \"bugs\",\r\n" + 
			"    \"status\": 200,\r\n" + 
			"    \"bugs\": {\r\n" + 
			"        \"12345\": [\r\n" + 
			"            {\r\n" + 
			"                \"is_private\": 0,\r\n" + 
			"                \"creator\": \"continuation@gmail.com\",\r\n" + 
			"                \"bug_id\": 12345,\r\n" + 
			"                \"last_change_time\": \"2015-09-23T18:36:19Z\",\r\n" + 
			"                \"size\": 982,\r\n" + 
			"                \"creator_detail\": {\r\n" + 
			"                    \"email\": \"continuation@gmail.com\",\r\n" + 
			"                    \"real_name\": \"Andrew McCreight [:mccr8]\",\r\n" + 
			"                    \"name\": \"continuation@gmail.com\",\r\n" + 
			"                    \"nick\": \"mccr8\",\r\n" + 
			"                    \"id\": 406194\r\n" + 
			"                },\r\n" + 
			"                \"file_name\": \"Bug-12345---Some-patch.patch\",\r\n" + 
			"                \"summary\": \"Some patch.\",\r\n" + 
			"                \"creation_time\": \"2015-09-23T18:35:48Z\",\r\n" + 
			"                \"id\": 8665038,\r\n" + 
			"                \"is_obsolete\": 1,\r\n" + 
			"                \"flags\": [],\r\n" + 
			"                \"data\": \"IyBIRyBjaGFuZ2VzZXQgcGF0Y2gKIyBVc2VyIEFuZHJldyBNY0NyZWlnaHQgPGNvbnRpbnVhdGlvbkBnbWFpbC5jb20+CgpCdWcgMTIzNDUgLSBTb21lIHBhdGNoLgoKZGlmZiAtLWdpdCBhL3hwY29tL2Jhc2UvbnNDeWNsZUNvbGxlY3Rvci5jcHAgYi94cGNvbS9iYXNlL25zQ3ljbGVDb2xsZWN0b3IuY3BwCmluZGV4IDc3OTgxMmYuLjA1ZWNlNWYgMTAwNjQ0Ci0tLSBhL3hwY29tL2Jhc2UvbnNDeWNsZUNvbGxlY3Rvci5jcHAKKysrIGIveHBjb20vYmFzZS9uc0N5Y2xlQ29sbGVjdG9yLmNwcApAQCAtMSwxNCArMSwxNiBAQAogLyogLSotIE1vZGU6IEMrKzsgdGFiLXdpZHRoOiA4OyBpbmRlbnQtdGFicy1tb2RlOiBuaWw7IGMtYmFzaWMtb2Zmc2V0OiAyIC0qLSAqLwogLyogdmltOiBzZXQgdHM9OCBzdHM9MiBldCBzdz0yIHR3PTgwOiAqLwogLyogVGhpcyBTb3VyY2UgQ29kZSBGb3JtIGlzIHN1YmplY3QgdG8gdGhlIHRlcm1zIG9mIHRoZSBNb3ppbGxhIFB1YmxpYwogICogTGljZW5zZSwgdi4gMi4wLiBJZiBhIGNvcHkgb2YgdGhlIE1QTCB3YXMgbm90IGRpc3RyaWJ1dGVkIHdpdGggdGhpcwogICogZmlsZSwgWW91IGNhbiBvYnRhaW4gb25lIGF0IGh0dHA6Ly9tb3ppbGxhLm9yZy9NUEwvMi4wLy4gKi8KIAorLy8gU29tZSBwYXRjaC4KKwogLy8KIC8vIFRoaXMgZmlsZSBpbXBsZW1lbnRzIGEgZ2FyYmFnZS1jeWNsZSBjb2xsZWN0b3IgYmFzZWQgb24gdGhlIHBhcGVyCiAvLwogLy8gICBDb25jdXJyZW50IEN5Y2xlIENvbGxlY3Rpb24gaW4gUmVmZXJlbmNlIENvdW50ZWQgU3lzdGVtcwogLy8gICBCYWNvbiAmIFJhamFuICgyMDAxKSwgRUNPT1AgMjAwMSAvIFNwcmluZ2VyIExOQ1Mgdm9sIDIwNzIKIC8vCiAvLyBXZSBhcmUgbm90IHVzaW5nIHRoZSBjb25jdXJyZW50IG9yIGFjeWNsaWMgY2FzZXMgb2YgdGhhdCBwYXBlcjsgc28KIC8vIHRoZSBncmVlbiwgcmVkIGFuZCBvcmFuZ2UgY29sb3JzIGFyZSBub3QgdXNlZC4KCg==\",\r\n" + 
			"                \"description\": \"Some patch.\",\r\n" + 
			"                \"content_type\": \"text/plain\",\r\n" + 
			"                \"attacher\": \"continuation@gmail.com\",\r\n" + 
			"                \"is_patch\": 1\r\n" + 
			"            }\r\n" + 
			"        ]\r\n" + 
			"    }\r\n" + 
			"}";
}
