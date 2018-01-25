# Versioning Specifications for IDs

## Basic Info
* **Every** version format must start with a 32bit version number encoded as a
little-endian uint32.
* blockletter_cipher is defined by blockletter.py 'encode' and 'decode' fucntions
* base64_encode is the base defined by the java.util.Base64 encoder
* 'message' is a utf-8 encoded string that should uniquely identify the asset.

## Version 0:
Most basic version with minimal security, does not require keywords or keycodes.

### Format
32bit version number

base64_encode(blockletter_cipher(message))

delimeter: @

base64_encode(md5(message))

### Notes 
* Considered very minimal security because effectively the only security
measure are obscurity, a few made up ciphers, encoding and use of sha256.
  * Anyone with access to the spec could make working valid passes.
* Allows use of only QR codes which simplifies the app. Can later add
text verification once it is working in the app.
* format is very inefficient due to the base64_encode and really should 
only be used for testing or demonstration.