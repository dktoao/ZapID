cmake_minimum_required(VERSION 3.10)

set(TEST_FILES
    test_blockletter_encoding.cpp
    test_encoders.cpp
    test_chars_utilities.cpp
)

include(CTest)

foreach(TEST_FILE ${TEST_FILES})
    get_filename_component(TARGET ${TEST_FILE} NAME_WE)
    add_executable(${TARGET} ${TEST_FILE})
    target_link_libraries(${TARGET} ${PROJECT_NAME})
    add_test(NAME ${TARGET} COMMAND ${TARGET})
endforeach(TEST_FILE)