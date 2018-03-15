package com.zyj

import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.task('testPlugin') << {
            println 'hello, world!'
        }
    }
}
