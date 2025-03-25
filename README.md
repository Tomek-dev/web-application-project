# Software Project Manager

## Overview
Advanced task management system with flexible workplace organization, project tracking, and role-based access control.

## Key Features
- Multi-workplace user management
- Granular role assignment per workplace
- Sprint-based task management
- Flexible task tracking
- Comprehensive access control

## Technology Stack
- Backend: Java Spring Boot
- ORM: Hibernate
- Database: MySQL
- Architecture: REST API
- Security: JWT Authentication

## System Architecture

### Workplace Management
- Users can have different roles in different workplaces
- Roles dynamically assignable
- Projects linked to specific workplaces

### Role Types
1. Administrator
   - Full system control
   - Manage users and workplaces
   - Create/delete projects

2. Project Manager
   - Create and assign tasks
   - Monitor project progress
   - Generate reports

3. Developer
   - Update task status
   - Add comments
   - Track assigned tasks

4. Tester
   - Review completed tasks
   - Report bugs
   - Validate task completion

5. Client
   - View project progress
   - Provide feedback
   - Limited read-only access

6. Viewer
   - Read-only access
   - Monitor project status

## Project Setup

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Running the Application
```bash
mvn clean install
mvn spring-boot:run
```

## Security
- JWT-based authentication
- Role-based access control
- Secure password storage

## Testing
- Unit tests for services
- Integration tests for controllers
- Mockito for mocking dependencies

## Future Roadmap
- Advanced reporting
- Real-time notifications
- Third-party integrations
- Enhanced analytics
