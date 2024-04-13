# Local Development Setup

## Configuration Profiles
This application uses Spring profiles to facilitate environment-specific configurations. The `local` profile is intended for development use.

## Sensitive Data Management
Sensitive configuration details (e.g., API keys, database credentials) should be stored in `application-local-secrets.yml`. This file is not tracked by Git as specified in our `.gitignore`:

##Secrets files
application-local-secrets.yml

## Setting Up Local Secrets
Each developer needs to create their own `application-local-secrets.yml` file following the structure provided in `application-local-secrets.yml.example`. Do not add actual secrets to the example file as it is tracked by Git.

## Switching Profiles
To switch the active profile, update the `spring.profiles.active` property in `application.yml` to include `local-secrets` alongside any other desired profiles:

```yaml
spring:
  profiles:
    active: local,local-secrets
