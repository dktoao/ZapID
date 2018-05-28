/**
 * blockletter
 * 
 * Implementation of the BlockLetter class that is used for encoding and
 * decoding various characters into their respective sequential and 
 * 5x5 grid representations
 * 
 * Copyright [2018] ZapID
 */
#ifndef ZAPID_CORE_BLOCKLETTER_HPP_
#define ZAPID_CORE_BLOCKLETTER_HPP_

/* ----------------------------------------------------------------------------
 * Includes
 * --------------------------------------------------------------------------*/
#include <cinttypes>
#include <vector>

namespace blockletter {
/* ----------------------------------------------------------------------------
 * Functions
 * --------------------------------------------------------------------------*/

/**
 * Encode an input string into a packed uint8 vector with 6 bits per letter
 * based on the order in the letter_map array. Inverse function of Decode6.
 */ 
std::vector<uint8_t> Encode6(const std::vector<char> input);

/**
 * Decode a 6 bit encoded uint8 array into the string that it represents.
 * Inverse of Encode6
 */
std::vector<char> Decode6(const std::vector<uint8_t> input);

};  // namespace blockletter

#endif  // ZAPID_CORE_BLOCKLETTER_HPP_
