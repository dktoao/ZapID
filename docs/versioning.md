# Versioning Specifications for IDs

## Basic Info
* **Every** version format must start with 4 byte string with a valid ASCII hexidecimal
number which represents the version.
* blockletter_cipher is defined by BlockLetter.java 'encode' and 'decode' fucntions
* base64_encode is the base defined by the java.util.Base64 encoder
* 'message' is a ASCII encoded string that should uniquely identify the asset.

## Version 0:
Most basic version with minimal security, does not require keywords or keycodes.

### Format
hex version number

base64_encode(message)

delimeter: @

base64_encode(md5(message))

### Notes 
* Considered very minimal security because effectively the only security
measures are obscurity (encoding and use of md5 which is itself insecure).
  * Anyone with access to the spec could make working valid passes.
* Allows use of only QR codes which simplifies the app. Can later add
text verification once it is working in the app.
* format is very inefficient due to the base64_encode and really should 
only be used for testing or demonstration.