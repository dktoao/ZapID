cmake_minimum_required(VERSION 3.10)
set(SOURCE_FILES
    baseencoder.cpp
    blockletter.cpp
    encoder.cpp
    version0.cpp
)

add_library(${PROJECT_NAME} STATIC ${SOURCE_FILES})
target_include_directories(${PROJECT_NAME} PUBLIC 
    ${OPENSSL_INCLUDE_DIR}
    ${PROJECT_SOURCE_DIR}/include
    )
target_link_libraries(${PROJECT_NAME} OpenSSL::Crypto)