@ECHO OFF
SETLOCAL

SET workspaceDir=%~dp0..

SET binDir=%workspaceDir%\bin
SET tmpFile=%temp%\javaFiles.txt
SET outputJar=%workspaceDir%\3DRendererDemo.jar

IF EXIST %binDir%\sk (
    ECHO Clearing bin directory
    RD %binDir%\sk /s /q
    DEL %binDir%\* /f /q
)

IF EXIST %tmpFile% (
    ECHO Removing temporary file
    DEL %tmpFile% /f /q
)

IF EXIST %outputJar% (
    ECHO Removing output jar
    DEL %outputJar% /f /q
)

ENDLOCAL
