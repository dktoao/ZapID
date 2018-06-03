/**
 * test_encoder0.cpp
 * 
 * Copyright [2018] ZapID
 * 
 * Test each version of the encoders various functions: QueryKey, Create and 
 * Validate. Make sure that Create and Validate are compatible with one another.
 */

#include <string>
#include <iostream>

#include "core/baseencoder.hpp"
#include "core/encoder.hpp"

// Messages to encode and decode
const int num_test_strings = 3;
const char* test_strings[num_test_strings] = {
    "TEST NO. 1",
    "THIS IS\nTEST # 2!",
    "DO ME?!?, I'M TEST 3! :)"
};

// Public and private key database
typedef struct {
    encoder::KeyType key_type;
    char* private_key;
    char* public_key;
} KeyInfo;
const int num_test_keys = 2;
KeyInfo test_keys[num_test_keys] = {
    {encoder::SHA256_PASSWORD, "TestPwd1", "TestPwd1"},
    {encoder::SHA256_PASSWORD, "LetMeIn!", "LetMeIn!"}
};

// Dummy code so that QueryKey can be called without an actual code.
encoder::Bytes dummy_code(100, 0x00);
encoder::QueryKeyOutput qko;
encoder::CreateInput create_input;
encoder::ValidateInput verify_input;
encoder::Bytes verification_code;

void main() {
    // Loop through each encoder
    for (int ei=0; ei < encoder::kNumEncoders; ei++) {
        // Loop through each test string
        std::cout << "Testing Encoder " << ei << std::endl;
        for (int si=0; si < num_test_strings; si++) {
            std::cout << "With test string \"" << test_strings[si] << "\""
                << std::endl;
            // Loop through each test key
            for (int ki=0; ki < num_test_keys; ki++) {
                // Print out test info
                std::cout << "With key pair " << ki << std::endl;
                // Call QueryKeyOutput with the dummy code to get the key type
                // required by the encoder
                qko = encoder::QueryKey(ei, dummy_code);
                if (qko.type != test_keys[ki].key_type) {
                    std::cout << "Key type does not match, skipping test"
                        << std::endl;
                    continue;
                }
                // Create a code using Create
                create_input.message =
                    encoder::CStringToChars(test_strings[si]);
                create_input.private_key =
                    encoder::CStringToBytes(test_keys[ki].private_key);
                verification_code = encoder::Create(ei, create_input);
                // Print the verification code
                for (int bi=0; bi < verification_code.size(); bi++) {
                    std::cout << verification_code[bi] << " ";
                }
                std::cout << std::endl;
                //  TODO: Test other functions: Validate
            }
        }
    }
}