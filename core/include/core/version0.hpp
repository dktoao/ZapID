/* ----------------------------------------------------------------------------
 * version0.hpp
 * Copyright [2018] ZapID
 * 
 * Version 0 of the ZapID encoder system. This very simple version takes only
 * A message, password and a SHA-256 hash of that message and password to 
 * determine validity.
 * --------------------------------------------------------------------------*/

#ifndef ZAPID_CORE_VERSION0_HPP_
#define ZAPID_CORE_VERSION0_HPP_

#include "baseencoder.hpp"
#include "blockletter.hpp"

namespace encoder {

// Setting for how big the password digest is
// WARNING: modifying this value will break backwards compatibility
const int kPasswordDigestLength = 6;

class Encoder0 : public BaseEncoder {
 private:
    const int kPasswordDigestLength = 6;
 public:
    QueryKeyOutput QueryKey(Bytes code);
    Bytes Create(CreateInput);
    bool Validate(ValidateInput);
};
}  // namespace encoder

#endif  // ZAPID_CORE_VERSION0_HPP_
