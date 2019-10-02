import json
import random
import string
import logging

import directories

class Sender:
    """
    Sends .orresponse messages to the messages folder.
    """

    def __init__(self):        
        logging.basicConfig(filename=directories.service_directory+"log.log", 
                            level=logging.INFO,
                            format='%(asctime)s %(message)s',
                            datefmt='%m/%d/%Y %I:%M:%S %p')


    def send_notification_message(self, successful, reason):
        """
        Writes the successful bool and a reason into a .orresponse file.
        """
        logging.info("| Sending notification message")

        json_data = {}
        json_data['message'] = 'orresponse'
        json_data['operation'] = 'notification'
        json_data['successful'] = 'yes' if successful else 'no'
        json_data['failurereason'] = reason
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".orresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)


    def send_bug_response_message(self, bugs_as_list):
        """
        Sends a response message containing the retrieved bugs.
        """
        logging.info("| Sending bug response message")

        json_data = {}
        json_data['message'] = 'orresponse'
        json_data['operation'] = 'orresponse'
        json_data['successful'] = 'yes'
        json_data['failurereason'] = ''
        json_data['bugs'] = bugs_as_list
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".orresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)


    def send_bug_detail_response_message(self, number, comments, attachments):
        """
        Sends a response message containing the detail (comments and attachements) of an bug.
        """
        logging.info("| Sending bug detail response message")

        json_data = {}
        json_data['message'] = 'orresponse'
        json_data['operation'] = 'ordetail'
        json_data['successful'] = 'yes'
        json_data['failurereason'] = ''
        json_data['comments'] = comments
        json_data['attachments'] = attachments
        json_data['number'] = number
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".orresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)