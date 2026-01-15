📦 Secure Toolkit — Installation & Usage

Add Dependency

To include Secure Toolkit in your project, add this to your pom.xml:

<dependency>
  <groupId>io.github.mariohg470</groupId>
  <artifactId>secure-toolkit</artifactId>
  <version>1.0.0</version>
</dependency>

Or if you’re using Gradle:

implementation 'io.github.mariohg470:secure-toolkit:1.0.0'

Features

🔐 Secure endpoints with TLS support
⚡ Flexible client for HTTP/JSON communication
📊 Built-in logging and monitoring
🧪 Tested with JUnit + Mockito

Quick Example:

import io.github.mariohg470.toolkit.SecureClient;

public class Demo {
    public static void main(String[] args) {
        SecureClient client = new SecureClient("https://api.example.com");
        String response = client.get("/status");
        System.out.println("Server says: " + response);
    }
}

Documentation

See the GitHub repo (github.com in Bing) for full examples, configuration options, and advanced usage.

📜 Release Notes — Version 1.0.0

🎉 Highlights

First official release of Secure Toolkit to Maven Central.
Provides a production‑ready foundation for secure client/server communication in Java.

🔐 Security

TLS handshake support with keystore/truststore integration.
Configurable secure endpoints for client/server workflows.

⚡ Core Features

Flexible HTTP/JSON client with built‑in error handling.
Logging powered by SLF4J + Logback for clear diagnostics.
Build metadata embedded (Git commit, branch, build time).

🧪 Testing

Unit tests with JUnit 5.
Mocking support via Mockito.
Integration test profile included.

📦 Packaging

Standard JAR plus shaded “fat JAR” with manifest entries.
Profiles for server and client runnable JARs.
Signed artifacts, sources, and javadocs published to Maven Central.

🛣️ Roadmap

🔜 Planned for 1.1.0

Multi‑client TLS chat system with username registration and command handling.
Enhanced monitoring endpoints with build info and server status.
Improved usability with clearer error messages and logging formats.

🚀 Future Goals

Non‑blocking NIO support for scalable server architecture.
UDP chat demo with file logging and secure extensions.
CI/CD pipeline integration for automated builds and releases.
Expanded documentation with runnable demos and step‑by‑step guides.

🌟 Long‑Term Visión

Publish a professional‑grade utility library that developers can easily integrate into their projects.
Continue evolving Secure Toolkit into a trusted foundation for secure, maintainable Java applications.

🤝 Contributing

We welcome contributions to Secure Toolkit! Whether it’s fixing a bug, improving documentation, or adding new features, your help makes this project stronger.

How to Contribute:

Fork the repository on GitHub.
Create a new branch for your changes:

git checkout -b feature/my-new-feature

Make your changes and commit them with clear messages.
Push your branch to your forked repo.
Open a Pull Request describing your changes.

Guidelines

Follow Semantic Versioning when proposing changes.
Ensure all code is formatted and passes tests (mvn clean verify).
Add or update unit tests for new features.
Keep documentation (README, Javadocs) up to date with your changes.

Communication

Use GitHub Issues to report bugs or request features.
Pull Requests are reviewed before merging — please be patient and responsive to feedback.

📄 License

Secure Toolkit is licensed under the Apache License, Version 2.0.
You may use, copy, modify, and distribute this software in both commercial and non‑commercial projects, provided that you comply with the terms of the license.

Copyright 2026 Mario

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
