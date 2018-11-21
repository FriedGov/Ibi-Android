node('master') {
    stage('Checkout') {
        echo 'Getting source code...'
        sh "cd $WORKSPACE/ibi-android"
        try {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'GitHub_wesselperik', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD']]) {
            sh("git config credential.username ${env.GIT_USERNAME}")
            sh("git config credential.helper '!f() { echo password=\$GIT_PASSWORD; }; f'")
            sh("git pull origin master")
        }
        } finally {
            sh("git config --unset credential.username")
            sh("git config --unset credential.helper")
        }
    }

    stage('Executing Findbugs') {
        sh "cd $WORKSPACE/ibi-android"
        sh './gradlew findbugs'
    }
    stage('Executing Checkstyle') {
            sh "cd $WORKSPACE/ibi-android"
            sh './gradlew checkstyle'
        }
        stage('Executing Tests') {
                    sh "cd $WORKSPACE/ibi-android"
                    sh './gradlew testDebugUnitTest'
                }
    
    stage('Archiving artifacts') {
        echo 'Archiving artifacts...'
        archiveArtifacts artifacts: 'app/build/outputs/apk/debug/*.apk, app/build/reports/findbugs/findbugs.html, app/build/reports/checkstyle/checkstyle-report.xml, app/build/reports/checkstyle/checkstyle.html'
    }
}