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

#include "core/datatypes.hpp"
using datatypes::Bytes;
using datatypes::Chars;
using datatypes::CStringToChars;
using datatypes::CStringToBytes;
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
    const char* private_key;
    const char* public_key;
} KeyInfo;
const int num_test_keys = 2;
KeyInfo test_keys[num_test_keys] = {
    {encoder::SHA256_PASSWORD, "TestPwd1", "TestPwd1"},
    {encoder::SHA256_PASSWORD, "LetMeIn!", "LetMeIn!"}
};
const int num_bad_keys = 2;
KeyInfo bad_keys[num_bad_keys] = {
    {encoder::SHA256_PASSWORD, "TestPwd1", "PwdOfTest2"},
    {encoder::SHA256_PASSWORD, "ImATeapot", "ImAKettle"}
};

// Dummy code so that QueryKey can be called without an actual code.
Bytes dummy_code(100, 0x00);
encoder::QueryKeyOutput qko;
encoder::CreateInput create_input;
encoder::ValidateInput validate_input;
Bytes verification_code;

int main() {
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
                create_input.message = CStringToChars(test_strings[si]);
                create_input.private_key =
                    CStringToBytes(test_keys[ki].private_key);
                verification_code = encoder::Create(ei, create_input);
                // Print the verification code
                for (int bi=0; bi < verification_code.size(); bi++) {
                    std::cout << std::hex <<
                    static_cast<int>(verification_code[bi]) << " ";
                }
                std::cout << std::endl;
                // Test the Validate function with the code created with Create
                validate_input.code = verification_code;
                validate_input.public_key =
                    CStringToBytes(test_keys[ki].public_key);
                if (!encoder::Validate(ei, validate_input)) {
                    std::cout << "Verification Failed!" << std::endl;
                    return 1;
                }
            }
            // Loop through each bad test key
            for (int ki=0; ki < num_test_keys; ki++) {
                // Print out test info
                std::cout << "With bad key pair " << ki << std::endl;
                // Call QueryKeyOutput with the dummy code to get the key type
                // required by the encoder
                qko = encoder::QueryKey(ei, dummy_code);
                if (qko.type != bad_keys[ki].key_type) {
                    std::cout << "Key type does not match, skipping test"
                        << std::endl;
                    continue;
                }
                // Create a code using Create
                create_input.message =
                    CStringToChars(test_strings[si]);
                create_input.private_key =
                    CStringToBytes(bad_keys[ki].private_key);
                verification_code = encoder::Create(ei, create_input);
                // Print the verification code
                for (int bi=0; bi < verification_code.size(); bi++) {
                    std::cout << std::hex <<
                    static_cast<int>(verification_code[bi]) << " ";
                }
                std::cout << std::endl;
                // Test the Validate function with the code created with Create
                validate_input.code = verification_code;
                validate_input.public_key =
                    CStringToBytes(bad_keys[ki].public_key);
                if (encoder::Validate(ei, validate_input)) {
                    std::cout << "Wrong key passed verification!" << std::endl;
                    return 1;
                }
            }
        }
    }
    std::cout << "All tests passed!" << std::endl;
    return 0;
}
