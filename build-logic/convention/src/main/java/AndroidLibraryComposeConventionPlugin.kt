import com.android.build.api.dsl.LibraryExtension
import com.andyc.convention.ExtensionType
import com.andyc.convention.configureAndroidCompose
import com.andyc.convention.configureBuildTypes
import com.andyc.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.getByType

/**
 * Custom Gradle plugin to configure Android library module that uses Compose
 */
class AndroidLibraryComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("runique.android.library")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(extension)
        }
    }
}