@echo off
setlocal

set tempFile=%temp%\javaFiles.txt
set workspaceDir=%~dp0..

set srcDir=%workspaceDir%\src
set binDir=%workspaceDir%\bin
set javac="%jdk6_64%\bin\javac.exe"
set jar="%jdk6_64%\bin\jar.exe"

echo Clearing old classes
rd %binDir%\sk /s /q

echo Locating files
dir /s /b %srcDir%\*.java > %tempFile%

echo Compiling java files
%javac% -cp %workspaceDir%\lib\MyLibrary.jar;%binDir% -d %binDir% @%tempFile%

echo Extracting libraries
cd %workspaceDir%\bin
%jar% -xf %workspaceDir%\lib\MyLibrary.jar sk CmdUtils32.dll CmdUtils64.dll

echo Creating jar file
%jar% -cfe %workspaceDir%\3DRendererDemo.jar sk.pa3kc.Program -C %workspaceDir%\bin .

endlocal
