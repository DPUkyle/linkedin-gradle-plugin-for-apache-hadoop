package com.linkedin.gradle

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class HelloWorld extends Specification {

    @Rule
    TemporaryFolder tmp = new TemporaryFolder()
    def buildDotGradle
    def settingsDotGradle
    def gradleDotProperties

    def setup() {
        //buildDotGradle = tmp.newFile('build.gradle')
        settingsDotGradle = tmp.newFile('settings.gradle')
    }

    def 'say hi'() {
        given:
        GradleRunner runner = GradleRunner.create()
                .withProjectDir(tmp.root)
                .withPluginClasspath()
                .withArguments('buildAzkabanFlows')

        when:
        BuildResult result = runner.build()

        then:
        Assert.fail('wut')
    }
}
