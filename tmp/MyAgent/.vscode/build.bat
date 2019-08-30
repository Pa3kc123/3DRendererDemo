@ECHO OFF
SETLOCAL

SET tmpFile=%temp%\javaFiles.txt
SET workspaceDir=%~dp0..

SET srcDir=%workspaceDir%\src
SET binDir=%workspaceDir%\bin
SET javac="%jdk8_64%\bin\javac.exe"
SET jar="%jdk8_64%\bin\jar.exe"

ECHO Locating java java files
DIR /s /b %srcDir%\*.java > %tmpFile%

ECHO Compiling java files
%javac% -cp %binDir% -d %binDir% @%tmpFile%

ECHO Creating jar file
%jar% -cfm %workspaceDir%\MyAgent.jar %workspaceDir%\MANIFEST.MF -C %workspaceDir%\bin .

ENDLOCAL
