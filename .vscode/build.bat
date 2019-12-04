@ECHO off
SETLOCAL EnableDelayedExpansion

SET tmpFile=%temp%\javaFiles.txt
SET workspaceDir=%~dp0..

SET srcDir=%workspaceDir%\src
SET binDir=%workspaceDir%\bin
SET libDir=%workspaceDir%\lib

if NOT EXIST %srcDir% (
    ECHO For some reason you are missing source files
    ECHO What else did you expect???
    GOTO :FINISH
)

IF NOT EXIST %binDir% (
    ECHO Missing 'bin' directory
    ECHO Creating new one
    MKDIR %binDir%
)

IF NOT EXIST %libDir% (
    ECHO Project requires MyLibrary.jar in 'lib' folder
    GOTO :FINISH
)

IF DEFINED jdk6_64 (
    SET javac="%jdk6_64%\bin\javac.exe"
    SET jar="%jdk6_64%\bin\jar.exe"
) ELSE IF DEFINED JDK_HOME (
    SET javac="%JDK_HOME%\bin\javac.exe"
    SET jar="%JDK_HOME%\bin\jar.exe"
) ELSE IF DEFINED JAVA_HOME (
    SET javac="%JAVA_HOME%\bin\javac.exe"
    SET jar="%JAVA_HOME%\bin\jar.exe"
) ELSE (
    ECHO Could not find java files
    GOTO :FINISH
)

IF NOT EXIST !javac! (
    ECHO Missing java compiler
    GOTO :FINISH
)

IF NOT EXIST !jar! (
    ECHO Missing jar archiver
    GOTO :FINISH
)

ECHO Locating files
DIR /s /b %srcDir%\*.java > %tmpFile%

ECHO Compiling java files
%javac% -cp %workspaceDir%\lib\MyLibrary.jar;%binDir% -d %binDir% @%tmpFile%

if ERRORLEVEL 1 (
    ECHO Some errors occured while compiling files. Exiting...
    GOTO :FINISH
)

ECHO Extracting libraries
CD %workspaceDir%\bin
%jar% -xf %workspaceDir%\lib\MyLibrary.jar sk CmdUtils32.dll CmdUtils64.dll

ECHO Creating jar file
%jar% -cfe %workspaceDir%\3DRendererDemo.jar sk.pa3kc.Program -C %workspaceDir%\bin .

GOTO :FINISH

:FINISH
    ENDLOCAL
