cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

# add_library( # Sets the name of the library.
#             name-of-lib
#
#             # Sets the library as a shared or static library.
#             SHARED or STATIC
#
#             # Provides a relative path to your source file(s).
#             # Associated headers in the same location as their source
#             # file are automatically included.
#             src/main/cpp/name-of-lib.cpp )

add_library(audiorec SHARED src/main/cpp/audio-recorder-jni.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( log-lib log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( audiorec
                       OpenSLES
                       android
                       ${log-lib} )
