"""batchread.py
Function and class for representing batch data
"""

from enum import Enum
from collections import OrderedDict


class _Section(Enum):
    GLOBAL = 0
    HEADER = 1
    INDIVIDUAL = 2
    END = 3


class BatchReader:

    def __init__(self, filename):

        section = _Section.START
        file = open(filename, 'r', encoding='utf8')
        self.global_info = OrderedDict()
        self.local_values = []
        self.length = 0
        for line in file:
            split_line = line.split('\t')
            # section switch
            if split_line[0] == 'GLOBAL':
                section = _Section.GLOBAL
                continue
            elif split_line[0] == 'INDIVIDUAL':
                section = _Section.HEADER
                continue
            elif split_line == '':
                section = _Section.END
                continue
            # fill variables
            if section == _Section.GLOBAL:
                self.global_info[split_line[0]] = split_line[1]
            elif section == _Section.HEADER:
                self.local_fields = split_line
                if not (split_line[-1] == '__ZapID'):
                    raise Exception('Final INDIVIDUAL field must == "__ZapID"')
                self.local_fields = split_line
                section = _Section.INDIVIDUAL
            elif section == _Section.INDIVIDUAL:
                self.local_values.append(split_line)
                self.length += 1

    def message_at(self, index):
        message = []
        for field, value in self.global_info.items():
            if field[0] == '_':
                if field[1] == '_':
                    continue
                else:
                    message.append(value)
            else:
                message.append('{}: {}'.format(field, value))

        for idx, val in enumerate(self.local_fields[index]):
            field = self.local_fields[idx]
            if field[0] == '_':
                if field[1] == '_':
                    continue
                else:
                    message.append(val)
            else:
                message.append('{}: {}'.format(field, val))
        return '\n'.join(message)

    def code_at(self, index):
        return self.local_values[index][-1]

    def __iter__(self):
        for ii in range(self.length):
            yield (self.message_at(ii), self.code_at(ii))
