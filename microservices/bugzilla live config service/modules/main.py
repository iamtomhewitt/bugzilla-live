import os
import time
import logging

import receiver
import directories

before = dict ([(f, None) for f in os.listdir (directories.message_directory)])

logging.basicConfig(filename=directories.service_directory+"log.log", 
                    level=logging.INFO,
                    format='%(asctime)s %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')

while 1:
  after = dict ([(f, None) for f in os.listdir (directories.message_directory)])

  added = [f for f in after if not f in before]
  removed = [f for f in before if not f in after]

  for f in added: 
    filename = "".join(f)
    if filename.endswith(".configrequest"):
      logging.info("Message received: " + "".join(f))
      r = receiver.Receiver()
      r.process_file(f)
      os.remove(directories.message_directory + filename)
    
  before = after

  time.sleep (1)
