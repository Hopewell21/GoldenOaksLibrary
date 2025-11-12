# CI/CD Setup Guide

This guide explains how to configure the GitHub Actions workflows for automated testing and deployment.

## Required GitHub Secrets

To enable full CI/CD functionality, you need to configure the following secrets in your GitHub repository:

### Settings → Secrets and variables → Actions

#### For Release Builds (Optional)
1. **KEYSTORE_BASE64**: Base64-encoded keystore file
   ```bash
   base64 -i your-keystore.jks | pbcopy  # macOS
   base64 -i your-keystore.jks | xclip   # Linux
   ```
2. **KEYSTORE_PASSWORD**: Password for the keystore
3. **KEY_ALIAS**: Alias of the signing key
4. **KEY_PASSWORD**: Password for the signing key

#### For Firebase App Distribution (Optional)
1. **FIREBASE_APP_ID**: Your Firebase app ID
2. **FIREBASE_SERVICE_ACCOUNT**: JSON content of Firebase service account

#### For Google Play Deployment (Optional)
1. **GOOGLE_PLAY_SERVICE_ACCOUNT**: JSON content of Google Play service account

## Workflow Overview

### Main CI/CD Pipeline (`android-ci-cd.yml`)

1. **Lint Check**: Validates code quality
2. **Unit Tests**: Runs all unit tests
3. **Instrumented Tests**: Runs UI tests on Android emulators (API 29, 33)
4. **Build Debug APK**: Creates debug build
5. **Build Release APK**: Creates signed release build (main branch only)
6. **Deploy to GitHub Releases**: Automatically creates releases
7. **Deploy to Firebase**: Distributes to testers (develop branch)
8. **Deploy to Google Play**: Publishes to internal testing track
9. **Security Scan**: Scans for vulnerabilities
10. **Dependency Check**: Checks for outdated/vulnerable dependencies

### Pull Request Checks (`pr-checks.yml`)

- Validates Gradle configuration
- Checks code formatting
- Runs unit tests
- Builds debug APK
- Comments on PR with status

## Workflow Triggers

- **Push to main/develop**: Runs full CI/CD pipeline
- **Pull Request**: Runs validation checks
- **Release created**: Deploys to GitHub Releases

## Manual Workflow Dispatch

You can manually trigger workflows from the Actions tab:
1. Go to Actions → Select workflow
2. Click "Run workflow"
3. Select branch and click "Run workflow"

## Local Testing

Before pushing, test locally:

```bash
# Run lint
./gradlew lint

# Run unit tests
./gradlew test

# Run instrumented tests (requires emulator)
./gradlew connectedDebugAndroidTest

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

## Troubleshooting

### Build Failures
- Check Gradle version compatibility
- Verify all dependencies are available
- Review build logs in Actions tab

### Test Failures
- Ensure tests pass locally
- Check emulator configuration for instrumented tests
- Review test output in artifacts

### Deployment Issues
- Verify secrets are correctly configured
- Check service account permissions
- Review deployment logs

## Customization

### Modify Test Matrix
Edit `instrumented-tests` job in `android-ci-cd.yml`:
```yaml
strategy:
  matrix:
    api-level: [29, 33]  # Add/remove API levels
```

### Change Deployment Tracks
Edit `deploy-play-internal` job:
```yaml
track: internal  # Change to: alpha, beta, production
```

### Adjust Retention Days
Modify `retention-days` in artifact upload steps:
```yaml
retention-days: 30  # Change as needed
```

## Best Practices

1. **Always test locally** before pushing
2. **Review PR checks** before merging
3. **Monitor security scans** regularly
4. **Keep dependencies updated**
5. **Use feature branches** for development
6. **Tag releases** for version tracking

## Support

For issues with CI/CD:
1. Check workflow logs in Actions tab
2. Review GitHub Actions documentation
3. Open an issue in the repository

