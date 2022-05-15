plugins {
    kotlin("multiplatform") version "1.6.20"
}

group = "org.kanovo"
version = "1.0-SNAPSHOT"
val mingwHeaders = "D:\\msys2\\mingw64\\include"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
        compilations["main"].cinterops {
            val glew by creating {
                includeDirs {
                    allHeaders(mingwHeaders,"library/include")
                }
            }
            val glfw by creating {
                includeDirs {
                    allHeaders(mingwHeaders,"library/include")
                }
            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
tasks {
    register("copyDllDebug", Copy::class){
        from("library/bin"){
            include("glfw3.dll","glew32.dll")
        }
        into("build/bin/native/debugExecutable")
    }
    register("copyDllRelease", Copy::class){
        from("library/bin"){
            include("glfw3.dll","glew32.dll")
        }
        into("build/bin/native/debugExecutable")
        into("build/bin/native/releaseExecutable")
    }
}