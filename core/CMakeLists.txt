cmake_minimum_required(VERSION 3.10)
project(zapidcore)
# Version number
set(VERSION_MAJOR 0)
set(VERSION_MINOR 1)

# Add a configuration header file
configure_file (
    include/core/config.h.in
    include/core/config.h
)

# First, add external libs (openssl crypto)
set(OPENSSL_USE_STATIC_LIBS TRUE)
set(OPENSSL_ROOT_DIR /usr/local/ssl)
find_package(OpenSSL REQUIRED)

# Configure cmake output locations
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/lib)

# Enable Testing
enable_testing()

# Sub directories
add_subdirectory(src)
add_subdirectory(test)
