/**
 * encoder.hpp
 * 
 * Copyright [2018] ZapID
 * 
 * Utilities for accessing different versions of each ZapID encoder classes 
 * using a version number rather than explicitly declaring the class and using
 * it's member functions
 */

#ifndef ZAPID_CORE_ENCODER_HPP_
#define ZAPID_CORE_ENCODER_HPP_

#include "baseencoder.hpp"

namespace encoder {

/**
 * Constant that sets up the available number of encoders. This must be updated
 * everytime a new version of the encoding scheme is created and added to
 * versioned_encoders in encoder.cpp
 */
const int kNumEncoders = 1;

/**
 * Function to access the Key query functionality of every encoder based on the 
 * version of the desired encoder.
 */
QueryKeyOutput QueryKey(int version, Bytes code);

/**
 * Function to access the ZapID code creation routines based on the version of
 * encoder. 
 */
Bytes Create(int version, CreateInput input);

/**
 * Funtion to access the ZapID code verification routines based on the version 
 * of the encoder. 
 */
bool Validate(int version, ValidateInput);

}  // namespace encoder

#endif  // ZAPID_CORE_ENCODER_HPP_
