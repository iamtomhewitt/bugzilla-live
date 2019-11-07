import os
import time
import logging

import receiver, directories

before = dict ([(None, None)])

logging.basicConfig(filename=directories.service_directory+"log.log", 
                    level=logging.INFO,
                    format='%(asctime)s %(message)s',
                    datefmt='%m/%d/%Y %I:%M:%S %p')

logging.info("| Bug Service has started")
while 1:
  after = dict ([(f, None) for f in os.listdir (directories.message_directory)])
  added = [f for f in after if not f in before]

  for f in added: 
    filename = "".join(f)
    if filename.endswith(".orrequest"):
      logging.info("| Message received: " + "".join(f))
      r = receiver.Receiver()
      r.process_file(f)
      os.remove(directories.message_directory + filename)
    
  before = after

  time.sleep (1)
