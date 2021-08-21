# Capstone-Kodo-Backend

## Setup

### Database

Kodo uses the following databases:
- Cloud SQL for relational data management
- Cloud Storage for file storage

For MySQL usage, all developers should utilise a local database for personal testing prior to deployment. Cloud SQL should only be used for dev and prod environment.

#### Setup

1. Set environment variables on Intellij
    - Create three seperate configurations on Intellij, each configuration will contain necessary credentials to access the associated database based on the environment.
    - Repeat the following steps for each configuration: Edit configuration > Add environment variables > Apply changes.
    
2. Test MySQL database connection
    - For Cloud SQL usage, add private IP address to Cloud SQL Account Authorised network prior to testing.
    - Open database tab > Add MySQL database > Input credentials > Test connection. Repeat this step for each database.
