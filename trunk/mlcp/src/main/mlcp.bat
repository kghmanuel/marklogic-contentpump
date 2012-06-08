@ECHO OFF
REM set arg=%~1
REM echo %arg%
set argss=%*
echo %argss%


set cmdpath=%~dp0
echo Command Path:  %cmdpath%
set cmdpath=%cmdpath:~0,-1%
for %%d in (%cmdpath%) do set cmdppath=%%~dpd
REM echo %cmdppath%
set LIB_HOME=%cmdppath%lib
REM echo LIB_HOME: %LIB_HOME%

SET "VMARGS=-DCONTENTPUMP_HOME=%LIB_HOME% -DCONTENTPUMP_VERSION=1.0"

SetLocal EnableDelayedExpansion

echo "***
set classpath=%LIB_HOME%conf

for %%X in (%LIB_HOME%\*) do (
  echo %%X
  set tmp=%%X
  set classpath=!classpath!;!tmp!
)
java -cp %classpath% %VMARGS% com.marklogic.contentpump.ContentPump %*
