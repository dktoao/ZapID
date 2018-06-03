/* ----------------------------------------------------------------------------
 * baseencoder.hpp
 * Copyright [2018] ZapID
 * 
 * This defines the abstract base class that future versions of each encoder
 * must inherit from.
 * --------------------------------------------------------------------------*/

#ifndef ZAPID_CORE_BASEENCODER_HPP_
#define ZAPID_CORE_BASEENCODER_HPP_

#include <vector>
#include <cinttypes>

namespace encoder {

// Typedefs for various types
typedef std::vector<uint8_t> Bytes;
typedef std::vector<char> Chars;

typedef enum KeyType {
    SHA256_PASSWORD,
    ASYMMETRIC,
} KeyType;

typedef struct {
    Bytes code;
    Bytes public_key;
} ValidateInput;

typedef struct {
    Chars message;
    Bytes private_key;
} CreateInput;

typedef struct {
    KeyType type;
    Bytes lookup_hash;
} QueryKeyOutput;

// Helper functions for converting various types to each other

/**
 * Convert a C style string to a Chars (std::vector<char>) object
 */
Chars CStringToChars(const char * c_string);

/**
 * Convert a C style string to a Bytes (std::vector<uint8_t>) object
 */
Bytes CStringToBytes(const char * c_string);

/**
 * BaseEncoder class that all versions of the encoder must inherit from
 */
class BaseEncoder {
 public:
    /**
     * This function is to be overriden to supply the key lookup system
     * with a hash of thee private key used to created the code.
     */
    virtual QueryKeyOutput QueryKey(Bytes code);

    /**
     * Create the data that will go in the QR code based on the given
     * inputs
     * 
     * @return Bytes 
     */
    virtual Bytes Create(CreateInput);

    /**
     * Validate the QR code based on the public key provided
     */
    virtual bool Validate(ValidateInput);
};
}  // namespace encoder

# endif  // ZAPID_CORE_BASEENCODER_HPP_
