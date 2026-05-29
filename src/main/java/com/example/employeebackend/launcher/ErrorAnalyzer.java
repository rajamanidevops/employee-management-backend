package com.example.employeebackend.launcher;

public class ErrorAnalyzer {

    public static String analyze(String message) {
        if (message == null || message.isEmpty()) return "❌ Unknown error occurred.";

        String lower = message.toLowerCase();

        if (lower.contains("port") && lower.contains("already")) {
            return """
❌ Server Port Error

Reason:
Another app is using the same port.

Fix:
✔ Stop old application
✔ Or change server.port in application.properties
""";
        }

        if (lower.contains("failed to configure datasource")) {
            return """
❌ Database Connection Error

Reason:
DB not running or credentials are wrong.

Fix:
✔ Start your database
✔ Check URL, username, password
✔ Check DB driver dependency
""";
        }

        if (lower.contains("beancreationexception")) {
            return """
❌ Spring Bean Creation Error

Reason:
Dependency injection failed.

Fix:
✔ Check @Autowired fields
✔ Ensure @Service/@Component are present
✔ Avoid circular dependencies
""";
        }

        if (lower.contains("cannot find symbol")) {
            return """
❌ Compilation Error

Reason:
Java cannot find a class, method, or variable.

Fix:
✔ Check imports
✔ Check method/variable names
✔ Rebuild project
""";
        }

        return """
❌ Application Startup Error

Fix:
✔ Read first 'Caused by' in logs
✔ Check configuration and logs carefully
""";
    }
}