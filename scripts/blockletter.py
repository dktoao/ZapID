""" blockletter.py
Class for creating and getting the 5x5 block representation of letter, numbers 
and special characters
"""

from numpy import zeros, uint32, identity, reshape, tile, transpose, dot


char_map = {
    "0": 0x00E9D72E,
    "1": 0x01F210C4,
    "2": 0x01F1322E,
    "3": 0x00E8B22E,
    "4": 0x01087E31,
    "5": 0x00F83C3F,
    "6": 0x00E8BC3E,
    "7": 0x0011111F,
    "8": 0x00E8BA2E,
    "9": 0x01087A3E,
    "A": 0x0118FE2E,
    "B": 0x00F8BE2F,
    "C": 0x00E8862E,
    "D": 0x00F8C62F,
    "E": 0x01F09C3F,
    "F": 0x00109C3F,
    "G": 0x00E8E43E,
    "H": 0x0118FE31,
    "I": 0x01F2109F,
    "J": 0x0032109F,
    "K": 0x01149D31,
    "L": 0x01F08421,
    "M": 0x0118D771,
    "N": 0x011CD671,
    "O": 0x00E8C62E,
    "P": 0x0010BE2F,
    "Q": 0x0164C62E,
    "R": 0x0114BE2F,
    "S": 0x00F8383E,
    "T": 0x0042109F,
    "U": 0x00E8C631,
    "V": 0x00454631,
    "W": 0x00AAC631,
    "X": 0x01151151,
    "Y": 0x00421151,
    "Z": 0x01F1111F,
    ",": 0x00420000,
    ".": 0x00400000,
    "!": 0x00401082,
    "#": 0x00AFABEA,
    "$": 0x00FA38BE,
    "(": 0x01042110,
    ")": 0x00110841,
    "+": 0x00023880,
    "-": 0x00003800,
    "=": 0x000701C0,
    "?": 0x0040322E,
    ":": 0x00020080,
    ";": 0x00108020,
    "/": 0x00111110,
    "\\": 0x01041041,
    " ": 0x00000000,
    "\n": 0x01ffffff,
    }
    
    
def get_char_number(char):
    
    try:
        number = uint32(char_map[char])
    except KeyError:
        raise KeyError(
                "Invalid character to convert to array: {}".format(char))
    return number


def encode(input_str):
    
    input_str = input_str.upper().replace('\n', '').replace(' ', '')
    output = zeros(len(input_str), dtype='uint32')
    for (idx, char) in enumerate(input_str):
        output[idx] = get_char_number(char)
    return output.data.tobytes()


def convert_to_array(char):
    
    number = get_char_number(char)
    block = zeros(25, dtype="uint8")
    for ii in range(0, 25):
        bitmask = uint32(1 << ii)
        block[ii] = (number & bitmask) >> ii
        
    return reshape(block, (5, 5))


def break_phrase(phrase, char_width):
    
    line = 0
    row = 0
    last_break = 0
    break_index = 0
    broken_phrase = []
    for (index, char) in enumerate(phrase):
        if char == " " or char == "\n":
            break_index = index + 1
        if row > (char_width - 1) or char == '\n':
            if break_index == last_break:
                break_index = last_break + char_width
            broken_phrase.append(phrase[last_break:break_index].strip())
            last_break = break_index
            line += 1
            row = index - last_break + 1
            continue
        row += 1
    if last_break < len(phrase):
        broken_phrase.append(phrase[last_break::].strip())
    return broken_phrase


def convert_phrase(phrase, char_width, block_size=10):
    
    broken_phrase = break_phrase(phrase, char_width)
    for ii, line in enumerate(broken_phrase):
        broken_phrase[ii] = line.upper()
    max_col = max(map(len, broken_phrase))
    max_row = len(broken_phrase)
    
    # Put together the array
    word_array = zeros((6*max_row-1, 6*max_col-1), dtype="uint8")
    for r in range(0, max_row):
        for c in range(0, len(broken_phrase[r])):
            word_array[(6*r):(6*(r+1)-1), (6*c):(6*(c+1)-1)] = \
                convert_to_array(broken_phrase[r][c])
                
    # Resize the array
    right = identity(word_array.shape[1], dtype='uint8')
    right = tile(reshape(right, (-1, 1)), (1, block_size))
    right = reshape(right, (word_array.shape[1], word_array.shape[1]*block_size))
    left = identity(word_array.shape[0], dtype='uint8')
    left = tile(reshape(left, (-1, 1)), (1, block_size))
    left = reshape(left, (word_array.shape[0], word_array.shape[0]*block_size))
    left = transpose(left)
    word_array = dot(left, dot(word_array, right)) - 1
                
    return word_array
