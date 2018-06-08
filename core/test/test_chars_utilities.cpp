/**
 * test_chars_utilities.cpp
 * 
 * Copyright [2018] ZapID
 * 
 * Test the Chars utility function from core/datatypes
 */

#include <iostream>
#include <string>

#include "core/datatypes.hpp"

using namespace datatypes;

int main() {
    // Test the to UpperChars functions
    Chars test_message = CStringToChars("Make Me uppercase !?!$");
    Chars correct_message = CStringToChars("MAKE ME UPPERCASE !?!$");
    Chars upper_message = UpperChars(test_message);
    if (!(upper_message == correct_message)) {
        std::cout << "Upper-case conversion failed" << std::endl;
        return 1;
    }
    if (test_message == correct_message) {
        std::cout << "Input should not have changed" << std::endl;
        return 1;
    }
    UpperChars(&test_message);
    if (!(test_message == correct_message)) {
        std::cout << "Input should have changed" << std::endl;
        return 1;
    }
    // Test the RightTrimChars function
    Chars test_message2 = CStringToChars("Hello \n    \n  ");
    Chars correct_message2 = CStringToChars("Hello");
    Chars trim_message = RightTrimChars(test_message2);
    if (!(trim_message == correct_message2)) {
        std::cout << "Right trim failed" << std::endl;
        return 1;
    }
    if (test_message2 == correct_message2) {
        std::cout << "Input should not have changed" << std::endl;
        return 1;
    }
    RightTrimChars(&test_message2);
    if (!(test_message2 == correct_message2)) {
        std::cout << "Input should have changed" << std::endl;
        return 1;
    }
    std::cout << "All tests passed!" << std::endl;
    return 0;
}