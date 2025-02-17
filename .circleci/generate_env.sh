base64 -w 0 release-key.jks > .circleci/release-key.base64
base64 -w 0 app/google-services.json > .circleci/google-services.base64
base64 -w 0 app/dev-service-account.json > .circleci/dev-service-account.base64