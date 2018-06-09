/* ----------------------------------------------------------------------------
 * blockletter.cpp
 * Copyright [2018] ZapID
 * 
 * This class defines scheme used to encode and generate "block letters" from
 * a 5x5 grid of pixels.
 * --------------------------------------------------------------------------*/

/* ----------------------------------------------------------------------------
 * Includes
 * --------------------------------------------------------------------------*/
#include <cmath>
#include <exception>

#include "core/blockletter.hpp"

/* ----------------------------------------------------------------------------
 * Variables
 * --------------------------------------------------------------------------*/
typedef struct {
    char symbol;
    uint32_t bit_grid;
} BlockInfo;

const int kLettermapLength = 57;

const BlockInfo letter_map[kLettermapLength] = {
    {'\0', 0x00001000},  // 0  -> 0x00 -> 000000
    {'0',  0x00e9d72e},  // 1  -> 0x01 -> 000001
    {'1',  0x01f210c4},  // 2  -> 0x02 -> 000010
    {'2',  0x01f1322e},  // 3  -> 0x03 -> 000011
    {'3',  0x00e8b22e},  // 4  -> 0x04 -> 000100
    {'4',  0x01087e31},  // 5  -> 0x05 -> 000101
    {'5',  0x00f83c3f},  // 6  -> 0x06 -> 000110
    {'6',  0x00e8bc3e},  // 7  -> 0x07 -> 000111
    {'7',  0x0011111f},  // 8  -> 0x08 -> 001000
    {'8',  0x00e8ba2e},  // 9  -> 0x09 -> 001001
    {'9',  0x01087a3e},  // 10 -> 0x0A -> 001010
    {'A',  0x0118fe2e},  // 11 -> 0x0B -> 001011
    {'B',  0x00f8be2f},  // 12 -> 0x0C -> 001100
    {'C',  0x00e8862e},  // 13 -> 0x0D -> 001101
    {'D',  0x00f8c62f},  // 14 -> 0x0E -> 001110
    {'E',  0x01f09c3f},  // 15 -> 0x0F -> 001111
    {'F',  0x00109c3f},  // 16 -> 0xA0 -> 010000
    {'G',  0x00e8e43e},  // 17 -> 0xA1 -> 010001
    {'H',  0x0118fe31},  // 18 -> 0xA2 -> 010010
    {'I',  0x01f2109f},  // 19 -> 0xA3 -> 010011
    {'J',  0x0032109f},  // 20 -> 0xA4 -> 010100
    {'K',  0x01149d31},  // 21 -> 0xA5 -> 010101
    {'L',  0x01f08421},  // 22 -> 0xA6 -> 010110
    {'M',  0x0118d771},  // 23 -> 0xA7 -> 010111
    {'N',  0x011cd671},  // 24 -> 0xA8 -> 011000
    {'O',  0x00e8c62e},  // 25 -> 0xA9 -> 011001
    {'P',  0x0010be2f},  // 26 -> 0xAA -> 011010
    {'Q',  0x0164c62e},  // 27 -> 0xAB -> 011011
    {'R',  0x0114be2f},  // 28 -> 0xAC -> 011100
    {'S',  0x00f8383e},  // 29 -> 0xAD -> 011101
    {'T',  0x0042109f},  // 30 -> 0xAE -> 011110
    {'U',  0x00e8c631},  // 31 -> 0xAF -> 011111
    {'V',  0x00454631},  // 32 -> 0xB0 -> 100000
    {'W',  0x00aac631},  // 33 -> 0xB1 -> 100001
    {'X',  0x01151151},  // 34 -> 0xB2 -> 100010
    {'Y',  0x00421151},  // 35 -> 0xB3 -> 100011
    {'Z',  0x01f1111f},  // 36 -> 0xB4 -> 100100
    {',',  0x00420000},  // 37 -> 0xB5 -> 100101
    {'.',  0x00400000},  // 38 -> 0xB6 -> 100110
    {'!',  0x00401082},  // 39 -> 0xB7 -> 100111
    {'#',  0x00afabea},  // 40 -> 0xB8 -> 101000
    {'$',  0x00fa38be},  // 41 -> 0xB9 -> 101001
    {'(',  0x01042110},  // 42 -> 0xBA -> 101010
    {')',  0x00110841},  // 43 -> 0xBB -> 101011
    {'+',  0x00023880},  // 44 -> 0xBC -> 101100
    {'-',  0x00003800},  // 45 -> 0xBD -> 101101
    {'=',  0x000701c0},  // 46 -> 0xBE -> 101110
    {'?',  0x0040322e},  // 47 -> 0xBF -> 101111
    {':',  0x00020080},  // 48 -> 0xC0 -> 110000
    {';',  0x00108020},  // 49 -> 0xC1 -> 110001
    {'/',  0x00111110},  // 50 -> 0xC2 -> 110010
    {'\\', 0x01041041},  // 51 -> 0xC3 -> 110011
    {'&',  0x0164C8A2},  // 52 -> 0xC4 -> 110100
    {'\"', 0x0000014A},  // 53 -> 0xC5 -> 110101
    {'\'', 0x00000084},  // 54 -> 0xC6 -> 110110
    {' ',  0x00000000},  // 55 -> 0xC7 -> 110111
    {'\n', 0x01ffffff},  // 56 -> 0xC8 -> 111000
};

/* ----------------------------------------------------------------------------
 * Private Functions
 * --------------------------------------------------------------------------*/
uint8_t FindSymbolIndex(const char symbol) {
    int ii = 0;
    int found_index = -1;
    while (ii < kLettermapLength) {
        if (letter_map[ii].symbol == symbol) {
            found_index = ii;
            break;
        }
        ii++;
    }
    if (found_index == -1) {
        throw "Invalid blockletter character";
    }
    return static_cast<uint8_t>(found_index);
}

char GetCharAtIndex(const uint8_t index) {
    return letter_map[index].symbol;
}

uint32_t GetBitGrid(const char symbol) {
    uint8_t index = FindSymbolIndex(symbol);
    return letter_map[index].bit_grid;
}

namespace blockletter {
/* ----------------------------------------------------------------------------
 * Public Functions
 * --------------------------------------------------------------------------*/
Bytes Encode6(const Chars input) {
    // setup vectors
    float fractional_bytes = static_cast<float>(input.size()) * (6.0 / 8.0);
    int num_bytes = static_cast<int>(std::ceil(fractional_bytes));
    Chars uinput = UpperChars(input);
    Bytes output(num_bytes);

    // loop through each triple of bytes and pack them with the chars
    uint8_t pack1;
    uint8_t pack2;
    int idx_input = 0;
    for (int idx_triple=0; idx_triple < num_bytes; idx_triple+=3) {
        // Pack the first of the triple
        // 0011 1111
        pack1 = (FindSymbolIndex(uinput[idx_input]) & 0x3F);
        if (idx_input+1 < input.size()) {
            // 0011 0000
            pack2 = (FindSymbolIndex(uinput[idx_input+1]) & 0x30);
            output[idx_triple] = ((pack1 << 2) | (pack2 >> 4));
        } else {
            pack2 = 0x00;
            output[idx_triple] = ((pack1 << 2) | (pack2 >> 4));
            break;
        }
        // Pack the second of the triple
        // 0000 1111
        pack1 = (FindSymbolIndex(uinput[idx_input+1]) & 0x0F);
        if (idx_input+2 < input.size()) {
            // 0011 1100
            pack2 = (FindSymbolIndex(uinput[idx_input+2]) & 0x3C);
            output[idx_triple+1] = ((pack1 << 4) | (pack2 >> 2));
        } else {
            pack2 = 0x00;
            output[idx_triple+1] = ((pack1 << 4) | (pack2 >> 2));
            break;
        }
        // Pack the last triple
        // 0000 0011
        pack1 = (FindSymbolIndex(uinput[idx_input+2]) & 0x03);
        if (idx_input+3 < input.size()) {
            // 0011 1111
            pack2 = (FindSymbolIndex(uinput[idx_input+3]) & 0x3F);
            output[idx_triple+2] = ((pack1 << 6) | pack2);
        } else {
            pack2 = 0x00;
            output[idx_triple+2] = ((pack1 << 6) | pack2);
            break;
        }
        // Update input index
        idx_input += 4;
    }
    return output;
}

Chars Decode6(const Bytes input) {
    // Calculate the size of the message
    float fractional_chars = input.size() * (8.0 / 6.0);
    int num_chars = static_cast<int>(std::floor(fractional_chars));
    Chars output(num_chars);

    // loop through every 4 letters in the input and get their values
    uint8_t pack1;
    uint8_t pack2;
    int idx_input = 0;
    for (int idx_quad=0; idx_quad < num_chars; idx_quad+=4) {
        // Retrieve the first character
        pack1 = ((input[idx_input] & 0xFC) >> 2);
        output[idx_quad] = GetCharAtIndex(pack1);
        // Retrieve the second character
        if (idx_quad+1 < num_chars) {
            pack1 = ((input[idx_input] & 0x03) << 4);
            pack2 = ((input[idx_input+1] & 0xF0) >> 4);
            output[idx_quad+1] = GetCharAtIndex(pack1 | pack2);
        } else {
            break;
        }
        // Retrieve third character
        if (idx_quad+2 < num_chars) {
            pack1 = ((input[idx_input+1] & 0x0F) << 2);
            pack2 = ((input[idx_input+2] & 0xC0) >> 6);
            output[idx_quad+2] = GetCharAtIndex(pack1 | pack2);
        } else {
            break;
        }
        // Retrieve the fourth character
        if (idx_quad+3 < num_chars) {
            pack2 = ((input[idx_input+2] & 0x3F));
            output[idx_quad+3] = GetCharAtIndex(pack2);
        }
        // Update in input index
        idx_input += 3;
    }
    // If the last character is the null character, remove it. This happens
    // if the length of the array % 4 = 3
    if (output[output.size()-1] == '\0') {
        output.pop_back();
    }
    return output;
}

MessageMatrix GetMessageMatrix(const Chars message, int max_width,
        int outer_padding) {
    // Make the message uppercase
    Chars umessage = UpperChars(message);
    // Calculate the character width of the message based on the max_width and
    // padding inputs
    int max_padded_width = max_width - 2*outer_padding;
    int max_char_width = (max_padded_width + kLetterPadding)
                         / (kSideLength + kLetterPadding);
    // Break the message up based on the max width, space breaks and newline
    // characters
    std::vector<Chars> linebreak_message;
    int lines = 0;
    int col = 0;
    int last_break = 0;
    int next_break = 0;
    for (int mi=0; mi < umessage.size(); mi++) {
        // Update the next place to break if there is a space or newline
        if (umessage[mi] == ' ' || umessage[mi] == '\n') {
            next_break = mi + 1;
        }
        // Break the line on these conditions
        if (col >= max_char_width || umessage[mi] == '\n') {
            // Check for condition where there is no oportuntity for a break
            // we have to break in the middle of a word.
            if (next_break == last_break) {
                next_break = last_break + max_char_width;
            }
            Chars sub_chars(&umessage[last_break], &umessage[next_break+1]);
            RightTrimChars(&sub_chars);
            linebreak_message.push_back(sub_chars);
            last_break = next_break;
            col = mi - last_break + 1;
            lines++;
        }
        col++;
    }
    if (last_break < umessage.size()) {
        Chars sub_chars(&umessage[last_break], &umessage[umessage.size()]);
        RightTrimChars(&sub_chars);
        linebreak_message.push_back(sub_chars);
        lines++;
    }
    // Put together the output MessageMatrix output
    MessageMatrix output;
    output.ncols =
        max_char_width * kSideLength
        + (max_char_width-1) * kLetterPadding
        + 2 * outer_padding;
    output.nrows =
        lines * kSideLength
        + (lines-1) * kLinePadding
        + 2 * outer_padding;
    output.matrix = std::vector<uint8_t>(output.ncols * output.nrows);
    // Loop through each element and return it
    int block_pixel_idx;
    int block_pixel_col;
    int block_pixel_row;
    int letter_col;
    int letter_row;
    int letter_pixel_idx;
    int letter_pixel_col;
    int letter_pixel_row;
    for (block_pixel_idx=0; block_pixel_idx < (output.ncols * output.nrows);
            block_pixel_idx++) {
        // First calculate the letter index and the index of the pixel in the
        // letter
        block_pixel_row = block_pixel_idx / output.ncols;
        block_pixel_col = block_pixel_idx - (block_pixel_row * output.ncols);

        // Figure out the letter column and letter pixel column
        letter_col = block_pixel_col - outer_padding;
        if (letter_col < 0) {
            // We are off in the left margin
            continue;
        }
        letter_pixel_col = letter_col % (kSideLength + kLetterPadding);
        if (letter_pixel_col >= kSideLength) {
            // We are in the space between letters
            continue;
        }
        letter_col = letter_col / (kSideLength + kLetterPadding);
        if (letter_col >= max_char_width) {
            // We are off in the right margin
            continue;
        }

        // Figure out the letter row and letter pixel row
        letter_row = block_pixel_row - outer_padding;
        if (letter_row < 0) {
            // We are off in the top margin
            continue;
        }
        letter_pixel_row = letter_row % (kSideLength + kLinePadding);
        if (letter_pixel_row >= kSideLength) {
            // We are in the space between lines
            continue;
        }
        letter_row = block_pixel_row / (kSideLength + kLinePadding);
        if (letter_row >= lines) {
            // We are off in the bottom margin
            continue;
        }

        // Calculate the letter pixel index
        letter_pixel_idx = (letter_pixel_row * kSideLength) + letter_pixel_col;

        // Get the letter we are inserting and then see if we need to fill in
        // the pixel
        if (letter_col >= linebreak_message[letter_row].size()) {
            // We are past the last letter on the line
            continue;
        }
        char insert_letter = linebreak_message[letter_row][letter_col];
        uint32_t bit_grid = GetBitGrid(insert_letter);
        uint32_t pixel_mask = (1 << letter_pixel_idx);
        if (bit_grid & pixel_mask) {
            output.matrix[block_pixel_idx] = 1;
        }
    }
    return output;
}
};  // namespace blockletter
