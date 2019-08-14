@echo off

echo "==================================================================="
echo "   ___  __  ___________  ______   __   ___     __   _____   ______ "
echo "  / _ )/ / / / ___/_  / /  _/ /  / /  / _ |   / /  /  _/ | / / __/ "
echo " / _  / /_/ / (_ / / /__/ // /__/ /__/ __ |  / /___/ / | |/ / _/   "
echo "/____/\____/\___/ /___/___/____/____/_/ |_| /____/___/ |___/___/   "
echo "                                                                   "
echo "==================================================================="
echo.
echo This script needs to be run as Administrator.
pause

rem ===================================================================
rem Install Python and the required modules
rem ===================================================================
cd "%~dp0\software"
echo Installing Python, please wait...
msiexec /i python-2.7.14.amd64.msi /quiet
echo Done!

cd "certifi-2018.4.16"
C:\Python27\python.exe setup.py install

cd ..
cd "idna-2.7"
C:\Python27\python.exe setup.py install

cd ..
cd "urllib3-1.23"
C:\Python27\python.exe setup.py install

cd ..
cd "warnings-plugin-0.0.2"
C:\Python27\python.exe setup.py install

cd ..
cd "chardet-3.0.4"
C:\Python27\python.exe setup.py install

cd ..
cd "requests-2.19.1"
C:\Python27\python.exe setup.py install


rem ===================================================================
rem Install Firefox - silently (/s)
rem ===================================================================
cd ..
echo.
echo Installing Firefox...
"Firefox Setup 44.0.exe" /s


rem ===================================================================
rem Register nssm - copy the source files for later use
rem ===================================================================
xcopy "%~dp0\services\nssm-2.24" "C:\BugzillaLive\nssm" /y /s /i



rem ===================================================================
rem Install Services using their individual .bat files
rem ===================================================================
cd "%~dp0\services"
call "config service/remove service.bat"
call "config service/install service.bat"
call "list manager service/remove service.bat"
call "list manager service/install service.bat"
call "OR service/remove service.bat"
call "OR service/install service.bat"
call "document service/remove service.bat"
call "document service/install service.bat"



rem ===================================================================
rem Copy source files to C:\BugzillaLive
rem ===================================================================
echo.
echo Copying Source Files...
xcopy "%~dp0\config" "C:\BugzillaLive\config" /y /s /i
xcopy "%~dp0\services\config service" "C:\BugzillaLive\services\config service" /y /s /i
xcopy "%~dp0\services\list manager service" "C:\BugzillaLive\services\list manager service" /y /s /i
xcopy "%~dp0\services\OR service" "C:\BugzillaLive\services\OR service" /y /s /i
xcopy "%~dp0\services\gui service" "C:\BugzillaLive\services\gui service" /y /s /i
xcopy "%~dp0\services\login service" "C:\BugzillaLive\services\login service" /y /s /i
xcopy "%~dp0\services\document service" "C:\BugzillaLive\services\document service" /y /s /i
mkdir "C:\BugzillaLive\messages"
mkdir "C:\BugzillaLive\config\lists"
mkdir "C:\BugzillaLive\config\attachments"
mkdir "C:\BugzillaLive\config\documents"



rem ===================================================================
rem Create desktop shortcut 
rem ===================================================================
xcopy "%~dp0\shortcut\Bugzilla Live.lnk" "C:\Users\Public\Desktop" /y /s /i
xcopy "%~dp0\shortcut\StartBugzillaLive.cmd" "C:\BugzillaLive" /y /s /i


echo.
echo Successfully installed Bugzilla Live!
pause

start "" "C:\BugzillaLIVE\services\login service\loginservice.jar"