/**
 * zapidcore_node.cpp
 * 
 * Copyright [2018] ZapID
 * 
 * NodeJS wrappers for various ZapID core native libraries
 */

#include <node.h>
#include <v8.h>

#include <cstring>

using v8::FunctionCallbackInfo;
using v8::Isolate;
using v8::Local;
using v8::Object;
using v8::String;
using v8::ArrayBuffer;
using v8::Integer;
using v8::Uint8Array;

#include "core/blockletter.hpp"

/**
 * NodeJS binding to the blockletter::GetMessageMatrix function
 */
void GetMessageMatrix(const FunctionCallbackInfo<v8::Value>& args) {
    Isolate* isolate = args.GetIsolate();
    // Get the arguments that the function was called with
    String::Utf8Value arg0(args[0]);
    std::string message_str(*arg0);
    int64_t max_width = args[1]->IntegerValue();
    int64_t outer_padding = args[2]->IntegerValue();
    // Convert and pass to the appropriate blockletter function
    Chars message(message_str.begin(), message_str.end());
    blockletter::MessageMatrix mat = blockletter::GetMessageMatrix(
        message, max_width, outer_padding);
    // Create a javascript object and pass back
    Local<Integer> nrows = Integer::New(isolate, mat.nrows);
    Local<Integer> ncols = Integer::New(isolate, mat.ncols);
    Local<ArrayBuffer> matrix_buffer =
        ArrayBuffer::New(isolate, mat.matrix.size());
    Local<Uint8Array> matrix =
        Uint8Array::New(matrix_buffer, 0, mat.matrix.size());
    for (unsigned int ii=0; ii < mat.matrix.size(); ii++) {
        matrix->Set(ii, Integer::New(isolate, mat.matrix[ii]));
    }
    Local<Object> return_obj = Object::New(isolate);
    return_obj->Set(String::NewFromUtf8(isolate, "nrows"), nrows);
    return_obj->Set(String::NewFromUtf8(isolate, "ncols"), ncols);
    return_obj->Set(String::NewFromUtf8(isolate, "matrix"), matrix);
    args.GetReturnValue().Set(return_obj);
}

// Functions required to export as a nodejs module
void Init(Local<Object> exports) {
    NODE_SET_METHOD(exports, "get_message_matrix", GetMessageMatrix);
}
NODE_MODULE(NODE_GYP_MODULE_NAME, Init)
