/* ----------------------------------------------------------------------------
 * baseencoder.cpp
 * Copyright [2018] ZapID
 * --------------------------------------------------------------------------*/

#include <string>

#include "core/baseencoder.hpp"

namespace encoder {

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

}  // namespace encoder
