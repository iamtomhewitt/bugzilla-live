@echo off
set serviceName="Bugzilla Live Document Service"
set nssm="C:\BugzillaLive\nssm\win64\nssm.exe"
%nssm% stop %serviceName%
%nssm% remove %serviceName%