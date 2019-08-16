@ECHO off
SETLOCAL

SET tempFile=%temp%\javaFiles.txt
SET workspaceDir=%~dp0..

SET srcDir=%workspaceDir%\src
SET binDir=%workspaceDir%\bin
SET javac="%jdk6_64%\bin\javac.exe"
SET jar="%jdk6_64%\bin\jar.exe"

ECHO Clearing old classes
RD %binDir%\sk /s /q

ECHO Locating files
DIR /s /b %srcDir%\*.java > %tempFile%

ECHO Compiling java files
%javac% -cp %workspaceDir%\lib\MyLibrary.jar;%binDir% -d %binDir% @%tempFile%

ECHO Extracting libraries
CD %workspaceDir%\bin
%jar% -xf %workspaceDir%\lib\MyLibrary.jar sk CmdUtils32.dll CmdUtils64.dll

ECHO Creating jar file
%jar% -cfe %workspaceDir%\3DRendererDemo.jar sk.pa3kc.Program -C %workspaceDir%\bin .

ENDLOCAL
