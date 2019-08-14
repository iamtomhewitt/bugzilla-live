@echo off
set jarLocation="C:\BugzillaLive\services\document service\documentservice.jar"
set jarLocation=%jarLocation:"=\"%
set serviceName="Bugzilla Live Document Service"
set nssm="C:\BugzillaLive\nssm\win64\nssm.exe"

%nssm% install %serviceName% "C:\Program Files\Zulu\zulu-8\bin\javaw.exe" "-jar %jarLocation%"

%nssm% set %serviceName% AppDirectory C:\Program Files\Zulu\zulu-8\bin 
%nssm% set %serviceName% Description %serviceName%

%nssm% start %serviceName%