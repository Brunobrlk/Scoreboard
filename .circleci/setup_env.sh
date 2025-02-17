INFO='\033[0;34m[BRLK INFO]'

echo -e "$INFO - Generating local.properties file..."
echo "$ADMOB_APP_ID" >> local.properties
echo "$AD_UNIT_ID" >> local.properties

echo -e  "$INFO - Generating google-services.json file..."
echo $GOOGLE_SERVICES > google-services.base64
base64 -d google-services.base64 > app/google-services.json

echo -e "$INFO - Generating dev-services-account.json file..."
echo $DEV_SERVICE_ACCOUNT > dev-service-account.base64
base64 -d dev-service-account.base64 > app/dev-service-account.json

echo -e "$INFO - Generating release-key.jks file..."
echo $RELEASE_KEY > release-key.base64
base64 -d release-key.base64 > app/release-key.jks
base64 -d release-key.base64 > release-key.jks