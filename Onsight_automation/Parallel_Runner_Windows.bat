@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

:: Validate argument
IF "%~1"=="" (
    echo Please provide an environment: DEV, PROD, STAGE, or UAT
    exit /b 1
)

:: Convert to UPPERCASE
set "ENV=%~1"

for /f %%i in ('powershell -command "'%ENV%'.ToUpper()"') do set "ENV=%%i"

:: Check valid environments
IF /I "!ENV!"=="DEV" GOTO RUN_TESTS
IF /I "!ENV!"=="PROD" GOTO RUN_TESTS
IF /I "!ENV!"=="STAGE" GOTO RUN_TESTS
IF /I "!ENV!"=="UAT" GOTO RUN_TESTS

echo Invalid environment: %ENV%
echo Valid options: DEV, PROD, STAGE, UAT
exit /b 1

:RUN_TESTS
echo Running tests for environment: !ENV!

:: Start Android tests
start "!ENV! Android Tests" cmd /c mvn clean test -P!ENV!_android>>!ENV!_Android.txt

:: Start iOS tests
start "!ENV! iOS Tests" cmd /c mvn clean test -P!ENV!_ios -Dparallel=true>>!ENV!_iOS.txt

ENDLOCAL