import json
import random
import string
import logging

import directories

class Sender():
    def __init__(self):        
        logging.basicConfig(filename=directories.service_directory+"log.log", 
                            level=logging.INFO,
                            format='%(asctime)s %(message)s',
                            datefmt='%m/%d/%Y %I:%M:%S %p')


    def send_notification_message(self, successful, reason):
        """
        Writes the successful bool and a reason into a .configresponse file.
        """
        logging.info("Sending notification message")

        json_data = {}
        json_data['message'] = 'configresponse'
        json_data['operation'] = 'notification'
        json_data['successful'] = 'yes' if successful else 'no'
        json_data['failurereason'] = reason
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".configresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)


    def send_properties_message(self, properties, successful, reason):
        """
        Writes the retrieved properties, successful bool and a reason into a .configresponse file.
        """
        logging.info("Sending properties message")

        json_data = {}
        json_data['message'] = 'configresponse'
        json_data['operation'] = 'getconfig'
        json_data['successful'] = 'yes' if successful else 'no'
        json_data['failurereason'] = reason
        
        json_data['properties'] = [properties]
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".configresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)