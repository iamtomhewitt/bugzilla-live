import json
import os
import tempfile
import logging

import sender
import directories

class Receiver():
    def __init__(self):
        logging.basicConfig(filename=directories.service_directory+"log.log",
                            level=logging.INFO,
                            format='%(asctime)s %(message)s',
                            datefmt='%m/%d/%Y %I:%M:%S %p')

    def process_file(self, file):
        """
        Decodes the contents of the message, and processes them.
        """
        # Read the contents for the file as a JSON string
        f = open(directories.message_directory + "".join(file))
        contents = f.read()
        json_string = json.loads(contents)

        s = sender.Sender()

        # Split out the individual parts of the JSON message
        message = json_string['message']
        operation = json_string['operation']
        filename = json_string['filename']

        logging.info("| Operation: " + operation)

        if operation == "create":
            file_contents = json_string['filecontents']
            success, error = self.create_list(filename, file_contents)
            s.send_notification_message(success, error)

        elif operation == "modify":
            append = json_string['append']
            remove = json_string['remove']
            success, error = self.modify_list(filename, append, remove)
            s.send_notification_message(success, error)

        elif operation == "delete":
            success, error = self.delete_list(filename)
            s.send_notification_message(success, error)

        else:
            error = "Unknown operation: " + operation
            logging.info("| " + error)
            s.send_notification_message(False, error)


    def create_list(self, filename, file_contents):
        """
        Creates a list with the specified file contents.
        """
        try:
            file = open(filename, 'w')
            file.write(file_contents)
            file.close()
            logging.info("| List created: " + filename)
            return True, ""

        except IOError as e:
            error = "IOError: %s" % e
            logging.error("| " + error)
            return False, error


    def modify_list(self, filename, append, remove):
        """
        Modifies a list, and replaces the contents with what needs to be removed and what
        needs to be added.
        """
        file = open(filename, 'r')
        contents = file.read()
        file.close()
        
        contents = contents + "," + append
        contents = contents.replace(remove, "")
        contents = contents.replace(",,", ",")
        if contents.startswith(","):
            contents = contents[1:len(contents)]

        try:
            file = open(filename, 'w')
            file.write(contents)
            file.close()
            logging.info("| List modified: " + filename)
            logging.info("| Added: " + append)
            logging.info("| Removed: " + remove)
            return True, ""

        except IOError as e:
            error = "IOError: %s" % e
            logging.error("| " + error)
            return False, error


    def delete_list(self, filename):
        """
        Deletes the specified list.
        """
        try:
            os.remove(filename)
            logging.info("| List deleted: " + filename)
            return True, ""

        except IOError as e:
            error = "IOError: %s" % e
            logging.error(error)
            return False, error
