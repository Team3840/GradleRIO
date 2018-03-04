package jaci.openrio.gradle.ide

import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.nativeplatform.NativeExecutableBinarySpec
import org.gradle.nativeplatform.NativeExecutableSpec
import org.gradle.platform.base.BinaryContainer
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.BinaryTasks
import org.gradle.platform.base.ComponentSpecContainer

@CompileStatic
class IDEPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def ecfgTask = project.tasks.create('editorConfig', EditorConfigurationTask) { EditorConfigurationTask task ->
            task.group = 'GradleRIO'
            task.description = 'Generate Editor Configuration for Build and Debugging'
        }
        project.extensions.create('editorConfiguration', EditorConfigurationExtension)
    }

    static class IDERules extends RuleSource {
        @Mutate
        void createEditorConfigTasks(ModelMap<Task> tasks, BinaryContainer bins, ExtensionContainer extCont) {
            def ext = extCont.getByType(EditorConfigurationExtension)
            bins.withType(NativeExecutableBinarySpec).each { NativeExecutableBinarySpec bin ->
                if (bin.targetPlatform.name.equals('roborio')) {
                    ext._binaries << bin
                }
            }
        }
    }

}
