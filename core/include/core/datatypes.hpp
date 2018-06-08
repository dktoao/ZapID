/**
 * datatypes.h
 * 
 * Copyright [2018] ZapID
 * 
 * Various datatype definitions and utility functions
 */

#ifndef ZAPID_CORE_DATATYPES_HPP_
#define ZAPID_CORE_DATATYPES_HPP_

#include <cinttypes>
#include <string>
#include <vector>

namespace datatypes {

/* -----------------------------------------------------------------------------
 * Typedefs
 * ---------------------------------------------------------------------------*/
typedef std::vector<uint8_t> Bytes;
typedef std::vector<char> Chars;

/* -----------------------------------------------------------------------------
 * Functions
 * ---------------------------------------------------------------------------*/

/**
 * Convert a Char vector to the upper-case version of the vector and return a
 * copy.
 */
Chars UpperChars(const Chars input);

/**
 * Convert a Char vector to the upper-case version in place
 */
void UpperChars(Chars* input);

/**
 * Trim trailing whitespace characters from a Chars vector and return a copy
 */
Chars RightTrimChars(const Chars input);

/**
 * Trim trailing whitespace characters from a Chars vector in place
 */
void RightTrimChars(Chars* input);

/**
 * Convert a C style string to a Chars vector
 */
Chars CStringToChars(const char * c_string);

/**
 * Convert a C style string to a Bytes vector
 */
Bytes CStringToBytes(const char * c_string);

/**
 * Conver a Bytes object to a std::string object
 */
std::string CharsToString(Chars input);
}  // namespace datatypes

#endif  // ZAPID_CORE_DATATYPES_HPP_
