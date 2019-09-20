package com.linkedin.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Unroll

class HadoopDslTestCase extends Specification {
    @Rule
    TemporaryFolder tmp = new TemporaryFolder()
    def buildDotGradle
    def settingsDotGradle
//    def gradleDotProperties

    def setup() {
        tmp.create()
        buildDotGradle = tmp.newFile('build.gradle') //<< this.class.classLoader.getResource('buildZips.gradle').text
        settingsDotGradle = tmp.newFile('settings.gradle')
//        gradleDotProperties = tmp.newFile('gradle.properties')
    }

    @Unroll
    def "Hadoop DSL test case for the file #filename"() {
        given:
        def resourcePath = "gradle/${shouldPass ? 'positive' : 'negative'}/${filename}.gradle"
        buildDotGradle << this.class.classLoader.getResource(resourcePath).text
        def projectName = 'hadoop-plugin-test'
//        def version = '1.0.0'
        settingsDotGradle << """rootProject.name='${projectName}'"""
//        gradleDotProperties << """version=${version}"""

        when:
        GradleRunner runner = GradleRunner.create()
                .withProjectDir(tmp.root)
                .withPluginClasspath()
                .withArguments('buildAzkabanFlows', '-is')
//                .forwardOutput()

        BuildResult result = shouldPass ? runner.build() : runner.buildAndFail()

        then:
//        def expectedOutcome = shouldPass ? TaskOutcome.SUCCESS : TaskOutcome.FAILED
//        result.task(':buildAzkabanFlows').outcome == expectedOutcome
        def expectedOutputLines = this.class.classLoader.getResource("expectedOutput/${shouldPass ? 'positive' : 'negative'}/${filename}.out").readLines()
        expectedOutputLines.each { line ->
            assert result.output.contains(line)
        }
        if (shouldPass) {
            result.task(':buildAzkabanFlows').outcome ==  TaskOutcome.SUCCESS
            def jobsFolder = new File(tmp.root, "jobs/${filename}")
            jobsFolder.exists()
            def expectedJobsPath = "expectedJobs/${filename}"
            jobsFolder.eachFile { actualFile ->
                def expectedFile = this.class.classLoader.getResource("${expectedJobsPath}/${actualFile.name}")
                // operations inside closure require explicit assert statements
                assert expectedFile != null
                assert actualFile.text == expectedFile.text
            }
        }

        where:
        filename                     | shouldPass
        'basicFlow'                  | true
        'cycles1'                    | false
        'cycles2'                    | false
        'invalidFields'              | false
        'invalidNames'               | false
        'missingFields'              | false
        'missingRequiredParameters'  | false
        'propertySetChecks'          | false
        'propertySetCycles'          | false
        'scope1'                     | false
        'scope2'                     | false
        'scope3'                     | false
        'scope4'                     | false
        'scope5'                     | false
        'scope6'                     | false
        'subflowChecks1'             | false
        'subflowCycles1'             | false
        'triggerCheck'               | false
        'workflowChecks'             | false
    }

}
