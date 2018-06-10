// test_native_matrix.js
const zapid = require('../../core/bind/nodejs/build/Release/zapidcore');
require('console');

var test_message = 'HI, TEST ME!';

var return_obj = zapid.get_message_matrix(test_message, 20, 1);

console.log('nrows: %d', return_obj.nrows);
console.log('ncols: %d', return_obj.ncols);
console.log('matrix[20]: %d', return_obj.matrix[20]);