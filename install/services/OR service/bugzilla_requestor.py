import requests
from requests.auth import HTTPBasicAuth
import json
import logging

import directories

class BugzillaRequestor:
    """
    Makes GET, POST, and PUT requests to Bugzilla using the 'requests' module. A JSON response is returned and sent back
    to the module using the BugzillaRequestor
    """

    global url 
    url = "https://skynet-scm.skynet.groupinfra.com/bugzilla/rest"

    def __init__(self, username, password, api_key):        
        logging.basicConfig(filename=directories.service_directory+"log.log", 
                            level=logging.INFO,
                            format='%(asctime)s %(message)s',
                            datefmt='%m/%d/%Y %I:%M:%S %p')

        self.username = username
        self.password = password
        self.api_key  = api_key

        logging.info("| Username: %s, Password: %s, API Key: %s" % (self.username, "*****", self.api_key))


    def get_bugs(self, list_of_numbers):
        """
        Makes a request to Bugzilla for the set of a bug numbers specified. Bugzilla returns a JSON response, which we parse into a list, which is
        returned.
        """

        # Remove the last comma so we dont have an empty 'ids=' in our query
        list_of_numbers = list_of_numbers.rstrip(',')

        bugs = ""
        for number in list_of_numbers.split(","):
            bugs += "&ids=" + number

        api_url = url + "/bug/?api_key=" + self.api_key + bugs + "&include_fields=id,status,product,component,summary,assigned_to,severity,cf_genfrom,cf_intext,cf_system,cf_contitem,cf_segment_release,last_change_time"
        bugs = self.make_bug_request(api_url)

        return bugs


    def get_bugs_for_user(self, user):
        """
        Makes a request to Bugzilla for a specified user. Bugzilla returns a JSON response, which we parse into a list, which is
        returned.
        """
        api_url = url + "/bug?assigned_to=" + user + "&bug_status=Investigation&bug_status=Diagnosed&bug_status=Diagnosed&bug_status=Coded&bug_status=Addressed&bug_status=Released&bug_status=Fixed&bug_status=No Fault&bug_status=Built" + "&api_key=" + self.api_key
        bugs = self.make_bug_request(api_url)
        return bugs


    def get_bugs_for_subsystem(self, subsystem):
        """
        Makes a request to Bugzilla for a specified subsystem. Bugzilla returns a JSON response, which we parse into a list, which is
        returned.
        """
        api_url = url + "/bug?product=" + subsystem + "&bug_status=Investigation&bug_status=Diagnosed&bug_status=Diagnosed&bug_status=Coded&bug_status=Addressed&bug_status=Released&bug_status=Fixed&bug_status=No Fault&bug_status=Built" + "&api_key=" + self.api_key
        bugs = self.make_bug_request(api_url)
        return bugs


    def get_bug_detail(self, number):
        """
        Gets the comments and attachments for an a bug. Bugzilla returns a JSON response, which we parse into two separate lists, which are
        returned.
        """
        online = False
        logging.info("| Online: " + str(online))

        # Make a request for comments
        comments = []

        if online:
            api_url = url + "/bug/" + number + "/comment?api_key=" + self.api_key
            response_json = self.make_get_request(api_url)
        else:
            response_json = json.loads(directories.test_comment_json)

        for comment in response_json['bugs'][number]['comments']:
            a_comment = {}
            a_comment['commenter']   = comment['creator']
            a_comment['time']        = comment['time']
            a_comment['comment']     = comment['text']
            
            comments.append(a_comment)
    
        # Now make a request for attachments
        attachments = []

        if online:
            api_url = url + "/bug/" + number + "/attachment?api_key=" + self.api_key
            response_json = self.make_get_request(api_url)
        else:
            response_json = json.loads(directories.test_attachment_json)

        for attachment in response_json['bugs'][number]:
            an_attachment = {}
            an_attachment['id']             = attachment['id']           
            an_attachment['description']    = attachment['summary']
            an_attachment['filename']       = attachment['file_name']
            an_attachment['data']           = attachment['data']

            attachments.append(an_attachment)

        return comments, attachments


    def add_bug_comment(self, number, comment):
        """
        Makes a request to add a comment to an a bug.
        """
        api_url = url + "/bug/" + number + "/comment?api_key=" + self.api_key
        data = {
            "comment" : comment
        }
        response = self.make_post_request(api_url, data)
        print("Response: "+str(response))
        return True;
             
        
    def change_bug_status(self, number, status, comment_json):
        """
        Changes the status of an a bug with a comment included.
        """
        status = {
            "status" : status,
            "comment" : comment_json
        }

        api_url = url + "/bug/" + number + "?api_key=" + self.api_key
        self.make_put_request(api_url, status)


    def make_post_request(self, api_url, data):
        """
        Makes a POST request to bugzilla, and returns the response as JSON.
        """
        logging.info("| Making a POST request with: " + api_url)
        r = requests.post(api_url, data=data, auth=HTTPBasicAuth(self.username, self.password), verify=False)
        logging.info("| Response: " + r.text)
        return json.loads(r.text)


    def make_get_request(self, api_url):
        """
        Makes a GET request to bugzilla, and returns the response as JSON.
        """
        logging.info("| Making a GET request with: " + api_url)
        r = requests.get(api_url, auth=HTTPBasicAuth(self.username, self.password), verify=False)
        logging.info("| Response: " + r.text)
        return json.loads(r.text)


    def make_put_request(self, api_url, data):
        """
        Makes a PUT request to bugzilla, and returns a JSON response.
        """

        logging.info("| Making a PUT request with: " + api_url)
        r = requests.put(api_url, json=data, auth=HTTPBasicAuth(self.username, self.password), verify=False)
        logging.info("| Response: " + r.text)
        return json.loads(r.text)

    
    def make_bug_request(self, api_url):
        """
        A shared method for making get requests to retrieve bugs. The api_url is what specifies what kind of a bug retrieval this is.
        It could either be a a set of numbers, or a url for a specific user or subsystem.
        """
        online = False
        logging.info("| Online: " + str(online))

        if online:
            response_json = self.make_get_request(api_url)        
        else:
            response_json = json.loads(directories.test_json)

        bugs = []

        # Now convert the r.text into a custom JSON string that the GUI service can interpret.
        # 'bugs' are also known as bugs, but cannot use the word 'a bug' as this is a Python type word.
        for bug in response_json['bugs']:
            an_bug = {}
            an_bug['assignedTo']          = bug['assigned_to']
            an_bug['component']           = bug['component']
            an_bug['generatedFrom']       = bug['cf_genfrom']
            an_bug['internalExternal']    = bug['cf_intext']
            an_bug['lastUpdated']         = bug['last_change_time']
            an_bug['number']              = bug['id']
            an_bug['product']             = bug['product']
            an_bug['segmentRelease']      = bug['cf_segment_release']
            an_bug['severity']            = bug['severity']
            an_bug['status']              = bug['status']
            an_bug['summary']             = bug['summary']
            an_bug['system']              = 'Unknown' if not bug['cf_system'] else bug['cf_system'][0]

            bugs.append(an_bug)

        return bugs   