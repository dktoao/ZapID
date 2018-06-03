cmake_minimum_required(VERSION 3.10)
project(zapidcorelib)
# Version number
set(VERSION_MAJOR 0)
set(VERSION_MINOR 1)

# Add a configuration header file
configure_file (
    src/coreconfig.h.in
    src/coreconfig.h
)

# Configure cmake output locations
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY bin)

# Include directories
include_directories(${PROJECT_SOURCE_DIR}/include)

# Enable Testing
enable_testing()

# Sub directories
add_subdirectory(src)
add_subdirectory(test)
