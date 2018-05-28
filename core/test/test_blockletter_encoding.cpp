/**
 * @brief Test suite for ZapID Core
 * 
 * @file test.cpp
 * 
 * Copyright [2018] ZapID
 */

#include <iostream>
#include <string>

#include "blockletter.hpp"

int main() {
    // Announce test
    std::cout << "Testing BlockLetterEncoding" << std::endl;
    // Setup
    std::string test_message_1 = "TEST MESSAGE 1!";
    std::vector<char> test_vector_1(test_message_1.begin(),
                                    test_message_1.end());
    std::vector<uint8_t> correct_result_vector_1 = {
        0x78, 0xF7, 0x5E, 0xDD, 0x73, 0xDD,
        0x74, 0xB4, 0x4F, 0xDC, 0x29, 0xC0,
    };
    std::vector<uint8_t> result_vector_1 = blockletter::Encode6(test_vector_1);
    // Test 1: make sure the size matches
    if (result_vector_1.size() == correct_result_vector_1.size()) {
        std::cout << "Test 1 Passed" << std::endl;
    } else {
        std::cout << "Test 1 Failed" << std::endl;
        return 1;
    }
    // Test 2: make sure that all the elements are the same
    bool result_match = true;
    for (int ii=0; ii < result_vector_1.size(); ii++) {
        if (result_vector_1[ii] != correct_result_vector_1[ii]) {
            result_match = false;
            break;
        }
    }
    if (result_match) {
        std::cout << "Test 2 Passed" << std::endl;
    } else {
        std::cout << "Test 2 Failed" << std::endl;
        return 1;
    }
    // Test 3 Convert back and make sure that the results match
    std::vector<char> result_message_1 = blockletter::Decode6(result_vector_1);
    if (test_vector_1.size() == result_message_1.size()) {
        std::cout << "Test 3 Passed" << std::endl;
    } else {
        std::cout << "Test 3 Failed" << std::endl;
        return 1;
    }
    // Test 4 make sure all the elements match
    result_match = true;
    for (int ii=0; ii < result_message_1.size(); ii++) {
        if (result_message_1[ii] != test_vector_1[ii]) {
            result_match = 0;
            break;
        }
    }
    if (result_match) {
        std::cout << "Test 4 Passed" << std::endl;
    } else {
        std::cout << "Test 4 Failed" << std::endl;
        return 1;
    }
    // If you get here then all tests have passed
    std::cout << "All tests have passed!" << std::endl;
    return 0;
}
