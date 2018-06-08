/**
 * blockletter.hpp
 * 
 * Copyright [2018] ZapID
 * 
 * Implementation of the BlockLetter class that is used for encoding and
 * decoding various characters into their respective sequential and 
 * 5x5 grid representations
 */
#ifndef ZAPID_CORE_BLOCKLETTER_HPP_
#define ZAPID_CORE_BLOCKLETTER_HPP_

/* -----------------------------------------------------------------------------
 * Includes
 * ---------------------------------------------------------------------------*/
#include <cinttypes>
#include <string>
#include <vector>

#include "core/datatypes.hpp"
using datatypes::Bytes;
using datatypes::Chars;
using datatypes::RightTrimChars;
using datatypes::UpperChars;

namespace blockletter {
/* -----------------------------------------------------------------------------
 * Typedefs and Globals
 * ---------------------------------------------------------------------------*/
const int kSideLength = 5;
const int kLetterPadding = 1;
const int kLinePadding = 1;

typedef struct {
    std::vector<bool> matrix;
    int nrows;
    int ncols;
} MessageMatrix;

/* -----------------------------------------------------------------------------
 * Functions
 * ---------------------------------------------------------------------------*/

/**
 * Encode an input string into a packed uint8 vector with 6 bits per letter
 * based on the order in the letter_map array. Inverse function of Decode6.
 */ 
Bytes Encode6(const Chars input);

/**
 * Decode a 6 bit encoded uint8 array into the string that it represents.
 * Inverse of Encode6
 */
Chars Decode6(const Bytes input);

/** Get the 2d matrix of pixels that represents the message input.
 * 
 * @param message Message to convert to a block matrix
 * @param max_width Maximum width of the messsage in pixels
 * @param outer_padding Number of blank padding pixels which will be placed
 *   around the message.
 * @return MessageMatrix MessageMatrix object representing the image of the
 *   message. 
 */
MessageMatrix GetMessageMatrix(const Chars message, int max_width,
    int outer_padding);

};  // namespace blockletter

#endif  // ZAPID_CORE_BLOCKLETTER_HPP_
