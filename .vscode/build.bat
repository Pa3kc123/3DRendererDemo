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
    GOTO :EOF
)

IF NOT EXIST %binDir% (
    ECHO Missing 'bin' directory
    ECHO Creating new one
    MKDIR %binDir%
)

IF NOT EXIST %libDir% (
    ECHO Project requires MyLibrary.jar in 'lib' folder
    GOTO :EOF
)

SET javac="%jdk6_64%\bin\javac.exe"
IF NOT EXIST !javac! (
    SET javac="%JDK_HOME%\bin\javac.exe"

    if NOT EXIST !javac! (
        ECHO Missing java compiler
        GOTO :EOF
    )
)

SET jar="%jdk6_64%\bin\jar.exe"
IF NOT EXIST !jar! (
    SET jar="%JDK_HOME%\bin\jar.exe"

    IF NOT EXIST !jar! (
        ECHO Missing jar archiver
        GOTO :EOF
    )
)

ECHO Locating files
DIR /s /b %srcDir%\*.java > %tmpFile%

ECHO Compiling java files
%javac% -cp %workspaceDir%\lib\MyLibrary.jar;%binDir% -d %binDir% @%tmpFile%

ECHO Extracting libraries
CD %workspaceDir%\bin
%jar% -xf %workspaceDir%\lib\MyLibrary.jar sk CmdUtils32.dll CmdUtils64.dll

ECHO Creating jar file
%jar% -cfe %workspaceDir%\3DRendererDemo.jar sk.pa3kc.Program -C %workspaceDir%\bin .

ENDLOCAL
