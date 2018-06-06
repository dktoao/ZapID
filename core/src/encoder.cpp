/**
 * encoder.cpp
 * 
 * Copyright [2018] ZapID
 */

#include <array>

#include "core/encoder.hpp"
#include "core/version0.hpp"

namespace encoder {

// Setup an array of versioned encoders
std::array<BaseEncoder*, kNumEncoders> versioned_encoders = {
    new Encoder0()
};

/* -----------------------------------------------------------------------------
 * Public Functions
 * ---------------------------------------------------------------------------*/

QueryKeyOutput QueryKey(int version, Bytes code) {
    BaseEncoder* e = versioned_encoders[version];
    return e->QueryKey(code);
}

Bytes Create(int version, CreateInput input) {
    BaseEncoder* e = versioned_encoders[version];
    return e->Create(input);
}

bool Validate(int version, ValidateInput input) {
    BaseEncoder* e = versioned_encoders[version];
    return e->Validate(input);
}

}  // namespace encoder
