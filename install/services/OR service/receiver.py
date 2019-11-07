import json
import os
import tempfile
import logging

import sender, bugzilla_requestor, directories

class Receiver:
    def __init__(self):        
        logging.basicConfig(filename=directories.service_directory+"log.log", 
                            level=logging.INFO,
                            format='%(asctime)s %(message)s',
                            datefmt='%m/%d/%Y %I:%M:%S %p')


    def process_file(self, file):
        """
        Decodes the contents of the message, and processes them.
        """
        f = open(directories.message_directory + "".join(file))
        contents = f.read()
        json_data = json.loads(contents)

        username  = json_data['username']
        password  = json_data['password']
        api_key   = json_data['apiKey']
        operation = json_data['operation']

        logging.info("| Operation: " + operation)

        b = bugzilla_requestor.BugzillaRequestor(username, password, api_key)
        s = sender.Sender()

        # Potential operations are:
        #  numbers      - retrieve a list of bugs based on a set of numbers
        #  user         - retrieve a list of bugs for a specified user 
        #  subsystem    - retrieve a list of bugs for a specified subsystem 
        #  detail       - retrieve a set of comments and attachments for an bug 
        #  addcomment   - adds a comment to the specified bug using a POST request
        #  changestatus - changes the status of an bug with a comment
        if operation == "numbers":
            numbers = json_data['numbers']
            bugs = b.get_bugs(numbers)
            if not bugs:
                s.send_notification_message(False, "No bugs could be found for the query specified.")
            else:
                s.send_bug_response_message(bugs)

        elif operation == "user":
            user = json_data['user']
            bugs = b.get_bugs_for_user(user)

            if not bugs:
                s.send_notification_message(False, "No bugs could be found for the query specified.")
            else:
                s.send_bug_response_message(bugs)

        elif operation == "subsystem":
            subsystem = json_data['subsystem']
            bugs = b.get_bugs_for_subsystem(subsystem)
            if not bugs:
                s.send_notification_message(False, "No bugs could be found for the query specified.")
            else:
                s.send_bug_response_message(bugs)

        elif operation == "detail":
            number = json_data['number']
            comments, attachments = b.get_bug_detail(number)
            s.send_bug_detail_response_message(number, comments, attachments)

        elif operation == "addcomment":
            number  = json_data['number']
            comment = json_data['comment']
            b.add_bug_comment(number, comment)
            s.send_notification_message(True, "")

        elif operation == "changestatus":
            number  = json_data['number']
            status  = json_data['status']
            comment = json_data['comment']
            b.change_bug_status(number, status, comment)
            s.send_notification_message(True, "")

        else:
            error = "Unknown operation: " + operation
            logging.info("| " + error)
            s.send_notification_message(False, error)