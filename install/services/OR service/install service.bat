@echo off
set serviceName="Bugzilla Live OR Service"
set nssm="C:\BugzillaLive\nssm\win64\nssm.exe"
set python_script="C:\BugzillaLive\services\OR service\main.py"
set python_script=%python_script:"=\"%

%nssm% install %serviceName% "C:\Python27\python.exe " "%python_script%"

%nssm% set %serviceName% AppDirectory C:\Python27
%nssm% set %serviceName% Description %serviceName%

%nssm% start %serviceName%