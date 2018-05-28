cmake_minimum_required(VERSION 3.10)

set(SOURCE_FILES
    blockletter.cpp
)

add_library(${PROJECT_NAME} STATIC ${SOURCE_FILES})