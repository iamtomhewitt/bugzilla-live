# A list of directories to be used by other python modules
message_directory = "C:\\BugzillaLive\\messages\\"
service_directory = "C:\\BugzillaLive\\services\\OR service\\"

test_json = """
{
    "faults": [],
    "bugs": [
        {
            "assigned_to_detail": {
                "id": 2,
                "real_name": "Test User",
                "name": "first.last@cgi.com",
                "email": "first.last@cgi.com"
            },
            "id": 12345,
            "status": "Investigation",
            "creator": "first.last@cgi.com",
            "cf_genfrom": "Testing",
            "summary": "Test OR",
            "last_change_time": "2014-09-23T19:12:17Z",
            "cf_intext": "Internal",
            "cf_segment_release": "1.2",
            "cf_system": [
                "System"
            ],
            "assigned_to": "first.last@cgi.com",
            "component": "Component",
            "severity": "Critical",
            "product": "Product"
        }
    ]
}"""

test_comment_json = """
{
    "bugs": {
        "12345": {
            "comments": [
                {
                    "time": "2000-07-25T13:50:04Z",
                    "text": "This is a test comment for offline use.",
                    "bug_id": 12345,
                    "count": 0,
                    "attachment_id": null,
                    "is_private": false,
                    "is_markdown" : true,
                    "tags": [],
                    "creator": "first.last@cgi.com",
                    "creation_time": "2000-07-25T13:50:04Z",
                    "id": 75
                },
                {
                    "time": "2000-07-25T13:50:04Z",
                    "text": "This is another.",
                    "bug_id": 12345,
                    "count": 0,
                    "attachment_id": null,
                    "is_private": false,
                    "is_markdown" : true,
                    "tags": [],
                    "creator": "test.user@cgi.com",
                    "creation_time": "2000-07-25T13:50:04Z",
                    "id": 76
                }
            ]
        }
    },
    "comments": {}
}"""

test_attachment_json = """
{
    "attachments": {},
    "bugs": {
        "12345": [
            {
                "data": "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAHCAIAAABLMMCEAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAArSURBVBhXY/z//z8DBmCCUIyMjBAGBEBF0QBCFFk5VBRiOlwCr20ogIEBAPjFCQ01zdaUAAAAAElFTkSuQmCC",
                "size": 1234,
                "content_type": "image/png",
                "summary": "This is a file attachment",
                "file_name": "This is a filename.png",
                "id": 123,
                "creation_time": "2000-07-25T13:50:04Z",
                "is_patch": 0,
                "is_obselete": 0
            }
        ]
    }
}"""