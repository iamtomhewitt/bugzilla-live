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

        if username is None:
            s.send_notification_message(False, "Could not get ORs as username is empty.")
            return
        if password is None:
            s.send_notification_message(False, "Could not get ORs as password is empty.")
            return
        if api_key is None:
            s.send_notification_message(False, "Could not get ORs as API key is empty.")
            return

        # Potential operations are:
        #  numbers      - retrieve a list of ORs based on a set of numbers
        #  user         - retrieve a list of ORs for a specified user 
        #  subsystem    - retrieve a list of ORs for a specified subsystem 
        #  detail       - retrieve a set of comments and attachments for an OR 
        #  addcomment   - adds a comment to the specified OR using a POST request
        #  changestatus - changes the status of an OR with a comment
        if operation == "numbers":
            numbers = json_data['numbers']
            ORs, error = b.get_ORs(numbers)
            if error:
                s.send_notification_message(False, error)
            else:
                s.send_OR_response_message(ORs)

        elif operation == "user":
            user = json_data['user']
            ORs, error = b.get_ORs_for_user(user)
            if error:
                s.send_notification_message(False, error)
            else:
                s.send_OR_response_message(ORs)

        elif operation == "subsystem":
            subsystem = json_data['subsystem']
            ORs, error = b.get_ORs_for_subsystem(subsystem)
            if error:
                s.send_notification_message(False, error)
            else:
                s.send_OR_response_message(ORs)

        elif operation == "detail":
            number = json_data['number']
            comments, attachments = b.get_OR_detail(number)
            s.send_OR_detail_response_message(number, comments, attachments)

        elif operation == "addcomment":
            number  = json_data['number']
            comment = json_data['comment']
            b.add_OR_comment(number, comment)
            s.send_notification_message(True, "")

        elif operation == "changestatus":
            number  = json_data['number']
            status  = json_data['status']
            comment = json_data['comment']
            b.change_OR_status(number, status, comment)
            s.send_notification_message(True, "")

        else:
            error = "Unknown operation: " + operation
            logging.info("| " + error)
            s.send_notification_message(False, error)