def testSuite = "DITSuite.xml"

def checkoutFolder  = "/tmp/workspace/$env.JOB_NAME/"

// env.MAILING_LIST_WHEN_SUCCESS contains another environment variable name pointing to an actual mailing list
// so, we need to retrieve its value
env.MAILING_LIST_WHEN_SUCCESS = env["$env.MAILING_LIST_WHEN_SUCCESS"]
env.MAILING_LIST_WHEN_FAILURE = env["$env.MAILING_LIST_WHEN_FAILURE"]

/********************************************/
/* Pipeline definition                      */
/********************************************/
pipeline {
    agent {
        node {
            label 'maven'
        }
    }
    options {
        timeout(time: 20, unit: 'MINUTES')
    }
    stages {
        stage('Build and Test') {
            steps {
                script {
                    openshift.withCluster() {
                        openshift.withProject(env.NAMESPACE) {
                            dir(checkoutFolder + '/' + env.GIT_SOURCE_SUBDIR) {
                                stage('Execute Test Suite') {
                                    sh "mvn test -B -DsuiteXMLFile=${testSuite}"
                                }
                            } // dir
                        } // openShift.withProject
                    } // openShift.withCluster
                } // script
            } // steps
        } // stage - Build and Test
    } // Stages
// Stages
    post {
        success {
            emailext(
                    attachmentsPattern: '**/report.html',
                    to: "$env.MAILING_LIST_WHEN_SUCCESS",
                    subject: "$JOB_NAME - Build # $BUILD_NUMBER - $currentBuild.result!",
                    body: """
$JOB_NAME - Build # $BUILD_NUMBER - $currentBuild.result
""")
        }
        failure {
            emailext(attachLog: true,
                    compressLog: true,
                    attachmentsPattern: '**/report.html',
                    to: "$env.MAILING_LIST_WHEN_FAILURE",
                    subject: "$JOB_NAME - Build # $BUILD_NUMBER - $currentBuild.result!",
                    body: """
$JOB_NAME - Build # $BUILD_NUMBER - $currentBuild.result

Check console output at $BUILD_URL to view the results.
""")
        }
    }



} // Pipeline

