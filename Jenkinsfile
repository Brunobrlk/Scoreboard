pipeline {
    agent any
    
    environment {
        RELEASE_KEY = credentials('RELEASE_KEY')
        LOCAL_PROPERTIES = credentials('LOCAL_PROPERTIES')
        GRADLE_PROPERTIES = credentials('GRADLE_PROPERTIES')
        GOOGLE_SERVICES = credentials('GOOGLE_SERVICES')
        DEV_SERVICE_ACCOUNT = credentials('DEV_SERVICE_ACCOUNT')
        ANDROID_HOME = '/var/jenkins_home/Sdk'
    }
    
    stages {
        stage("Setup Environment"){
            steps {
                sh './setup_environment'
            }
        }

        stage("Lint"){
            steps {
                sh './gradlew lintRelease'
            }
        }

        stage("Unit Tests"){
            steps {
                sh './gradlew testReleaseUnitTest'
            }
        }

        /*
        Out of scope. Maybe setup dedicated containers for emulators?
        stage("Integration Tests"){
            steps {
                sh './gradlew basicDevicesGroupAndroidTest'
            }
        }
        */
        
        stage("Build APK"){
            steps {
                sh './gradlew clean'
                sh './gradlew assembleRelease'
            }
        }

        stage("Build AAB"){
            steps {
                sh './gradlew bundleRelease'
            }
        }

        stage("Play Store Deploy"){
            steps {
                sh './gradlew publishReleaseBundle'
            }
        }

        /*
        stage("Amazon App Store"){
            steps {}
        }
        stage("Huawei App Gallery"){
            steps {}
        }
        */
    }
}

