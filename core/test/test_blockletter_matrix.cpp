/**
 * test_blockletter_matrix.cpp
 * 
 * Copyright [2018] ZapID
 * 
 * Functions to test the blockletter matrix generator
 */

#include <iostream>

#include "core/blockletter.hpp"
#include "core/datatypes.hpp"

using namespace datatypes;
using namespace blockletter;

int main() {
    Chars message = CStringToChars("HI \nBO3");
    MessageMatrix correct_mmat;
    correct_mmat.ncols = 19;
    correct_mmat.nrows = 14;
    correct_mmat.matrix = {
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0,
        0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0,
        0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0,
        0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    MessageMatrix test_mmat = GetMessageMatrix(message, 20, 1);
    if (!(test_mmat.ncols == correct_mmat.ncols)) {
        std::cout << "Number of columns not matching, test failed!"
                  << std::endl;
        return 1;
    }
    if (!(test_mmat.nrows == correct_mmat.nrows)) {
        std::cout << "Number of rows not matching, test failed!"
                  << std::endl;
        return 1;
    }
    if (!(test_mmat.matrix == correct_mmat.matrix)) {
        std::cout << "MessageBlock matrix does not match, test failed!"
                  << std::endl;
        return 1;
    }
    std::cout << "All tests passed!" << std::endl;
    return 0;
}
