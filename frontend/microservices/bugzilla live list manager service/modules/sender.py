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
        Writes the successful bool and a reason into a .listresponse file.
        """
        logging.info("| Sending notification message")

        # Construct the JSON response
        json_data = {}
        json_data['message'] = 'listresponse'
        json_data['operation'] = 'notification'
        json_data['successful'] = 'yes' if successful is True else 'no'
        json_data['failurereason'] = reason
        json_data = json.dumps(json_data)

        # Random filename as we could overwrite a message that is already there
        chars = string.ascii_letters + string.digits
        name = ''.join(random.choice(chars) for i in range(8))
        random_filename = directories.message_directory + name + ".listresponse"

        final = json.dumps(json.loads(json_data), indent=4, sort_keys=True)
        
        response = open(random_filename, 'w')
        response.write(final)