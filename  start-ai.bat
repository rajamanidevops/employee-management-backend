@echo off
REM =============================================
REM AI Assistant Launcher
REM =============================================

REM Set backend URL and frontend URL
set BACKEND_URL=http://localhost:8080
set FRONTEND_URL=http://localhost:4200

echo Checking if backend is running on %BACKEND_URL% ...

REM Try to ping the backend port (TCP) using PowerShell
powershell -Command ^
    "$tcpTest = Test-NetConnection -ComputerName 'localhost' -Port 8080; if ($tcpTest.TcpTestSucceeded) { exit 0 } else { exit 1 }"

if %ERRORLEVEL% EQU 0 (
    echo Backend is running on port 8080.
    echo Launching AI frontend...
    start "" "%FRONTEND_URL%"
) else (
    echo Backend is NOT running on port 8080!
    echo Please start your backend first.
)

pause