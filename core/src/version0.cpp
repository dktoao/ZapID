/* ----------------------------------------------------------------------------
 * version0.cpp
 * Copyright [2018] ZapID
 * 
 * Version 0 of the ZapID encoder system. This very simple version takes only
 * A message, password and a SHA-256 hash of that message and password to 
 * determine validity.
 * --------------------------------------------------------------------------*/

#include <openssl/sha.h>

#include "core/version0.hpp"

namespace encoder {

QueryKeyOutput Encoder0::QueryKey(Bytes code) {
    QueryKeyOutput qko;
    qko.type = SHA256_PASSWORD;
    Bytes lookup_hash(&code[0], &code[kPasswordDigestLength]);
    qko.lookup_hash = lookup_hash;
    return qko;
}

Bytes Encoder0::Create(CreateInput input) {
    // Get the hash of the concatenated message and private_key (password)
    Bytes concat_input(input.message.begin(), input.message.end());
    concat_input.insert(
        concat_input.end(),
        input.private_key.begin(),
        input.private_key.end());
    // Hash both message+password and the password
    unsigned char password_digest[kPasswordDigestLength];
    unsigned char message_digest[kPasswordDigestLength];
    Bytes encoded_message;
    SHA256(&input.private_key[0], input.private_key.size(), password_digest);
    SHA256(&concat_input[0], concat_input.size(), message_digest);
    encoded_message = blockletter::Encode6(input.message);
    // Put all the components back together in the outputed code
    Bytes output;
    output.reserve(
        kPasswordDigestLength +
        SHA256_DIGEST_LENGTH +
        encoded_message.size());
    output.insert(
        output.begin(),
        &password_digest[0],
        &password_digest[kPasswordDigestLength]);
    output.insert(
        output.end(),
        &message_digest[0],
        &message_digest[SHA256_DIGEST_LENGTH]);
    output.insert(
        output.end(),
        encoded_message.begin(),
        encoded_message.end());
    return output;
}

bool Encoder0::Validate(ValidateInput input) {
    // First extract the message from the code
    int offset_message = kPasswordDigestLength + SHA256_DIGEST_LENGTH;
    Bytes coded_message(
        &input.code[offset_message], &input.code[input.code.size()]);
    Chars message = blockletter::Decode6(coded_message);
    CreateInput ci;
    ci.message = message;
    ci.private_key = input.public_key;
    Bytes check_code = Create(ci);
    bool return_val = false;
    if (check_code == input.code) {
        return_val = true;
    }
    return return_val;
}

}  // namespace encoder
