/**
 * datatypes.cpp
 * 
 * Copyright [2018] ZapID
 */

#include <cctype>

#include "core/datatypes.hpp"

namespace datatypes {

Chars UpperChars(const Chars input) {
    int n = input.size();
    Chars output(n);
    for (int ii=0; ii < n; ii++) {
        output[ii] = std::toupper(input[ii]);
    }
    return output;
}

void UpperChars(Chars* input) {
    int n = (*input).size();
    for (int ii=0; ii < n; ii++) {
        (*input)[ii] = std::toupper((*input)[ii]);
    }
}

Chars RightTrimChars(const Chars input) {
    int last_idx;
    for (last_idx = input.size()-1; last_idx > 1; last_idx--) {
        if (!(input[last_idx] == ' ' || input[last_idx] == '\n')) {
            break;
        }
    }
    Chars output(&input[0], &input[last_idx+1]);
    return output;
}

void RightTrimChars(Chars* input) {
    for (int last_idx = (*input).size()-1; last_idx > 1; last_idx--) {
        if (((*input)[last_idx] == ' ' || (*input)[last_idx] == '\n')) {
            (*input).pop_back();
        } else {
            break;
        }
    }
}

Chars CStringToChars(const char* c_string) {
    std::string cpp_string(c_string);
    Chars char_vector(cpp_string.begin(), cpp_string.end());
    return char_vector;
}

Bytes CStringToBytes(const char* c_string) {
    std::string cpp_string(c_string);
    Bytes byte_vector(cpp_string.begin(), cpp_string.end());
    return byte_vector;
}

std::string CharsToString(Chars input) {
    std::string output(input.begin(), input.end());
    return output;
}

}  // namespace datatypes
