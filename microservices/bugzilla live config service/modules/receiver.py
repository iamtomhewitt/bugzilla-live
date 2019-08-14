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
        f = open(directories.message_directory + "".join(file))
        contents = f.read()
        json_string = json.loads(contents)

        s = sender.Sender()

        # Split out the individual parts of the JSON message
        operation           = json_string['operation']
        properties          = json_string['properties']
        property_type       = json_string['propertyType']

        if operation == "save":
            if property_type == "user":
                logging.info("Saving user properties to " + directories.usr_properties_file)
                success, error = self.save_properties(directories.usr_properties_file, properties)
                s.send_notification_message(success, error)

            elif property_type == "application":
                logging.info("Saving application properties to " + directories.app_properties_file)
                success, error = self.save_properties(directories.app_properties_file, properties)
                s.send_notification_message(success, error)

            else:
                message = "Unrecognised property type: " + property_type
                logging.info(message)
                s.send_notification_message(False, message)

        elif operation == "get":
            if property_type == "user":
                logging.info("Getting user properties from " + directories.usr_properties_file)
                props, error = self.load_properties(directories.usr_properties_file, properties)
                s.send_properties_message(props, True, True, "")

            elif property_type == "application":
                logging.info("Getting application properties from " + directories.app_properties_file)
                props, error = self.load_properties(directories.app_properties_file, properties)
                s.send_properties_message(props, False, True, "")

            else:
                message = "Unrecognised property type: " + property_type
                logging.info(message)
                s.send_notification_message(False, message)

        else:
            message = "Unknown operation: " + operation
            logging.info(message)
            s.send_notification_message(False, message)
            
    
    def save_properties(self, properties_file, properties_to_save):
        """
        Saves the properties_to_save to a properties file based on the property_type.
        Returns a success boolean and an error message if there was one.
        """
        # Get each individual property to save that was in the json message
        for p in properties_to_save:
            for key, value in p.iteritems():
                # Now replace that property in the properties file
                #logging.info("Key '%s', Value '%s'" % (key, value))
                success, error = self.save_property(properties_file, key, value)
                if success == False:
                    logging.info ("Could not save properties: %s" % error)
                    return False, error
        
        return True, ""
        
    
    def load_properties(self, filepath, properties_to_get, sep='=', comment_char='#'):
        """ 
        Read the file passed as parameter as a properties file.         
        Returns:
            A dictionary and an error if there was one.
        """
        try:
            loaded_props = {}
            props_to_return = {}

            # First, load all the properties from the file into a dictionary
            with open(filepath, "rt") as f:
                for line in f:
                    l = line.strip()
                    if l and not l.startswith(comment_char):
                        key_value = l.split(sep)
                        key = key_value[0].strip()
                        value = sep.join(key_value[1:]).strip().strip('"') 
                        loaded_props[key] = value 

                # Now that we have loaded all the properties, only return the ones 
                # that were present in the request message
                for p in properties_to_get:
                    for key, value in p.iteritems():
                        if key in loaded_props:
                            props_to_return[key] = loaded_props[key]

            return props_to_return, ""
        
        except IOError as e:
            error = "IOError: %s" % e
            return None, error
            
            
    def save_property(self, filename, key, value):
        """
        Saves a given key and value to the filename. If a key exists already, it is updated, otherwise the key is added.
        Returns a success boolean and an error message if there was one.
        """
        found = False

        try:
            with open(filename, 'rU') as f_in, tempfile.NamedTemporaryFile('w', dir=os.path.dirname(filename), delete=False) as f_out:
                for line in f_in.readlines():
                    if line.startswith(key):
                        logging.info("Found key '%s', replacing with '%s'" % (key, value))
                        found = True
                        line = '='.join((line.split('=')[0], '{}'.format(value)))+"\n"

                    f_out.write(line)
                
                if not found:
                    logging.info("Writing '%s' with value '%s'" % (key, value))
                    f_out.write("\n"+key+"="+value)

            # remove old version
            os.unlink(filename)
            # rename new version
            os.rename(f_out.name, filename)

            return True, ""

        except IOError as e:
            error = "IOError: %s" % e
            return False, error