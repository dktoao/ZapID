""" passgen.py
Class containing function to setup and generate pass elements
"""

#TODO: Fix pass formatting a little

# Library Imports
import hashlib
import qrcode
import numpy
import collections

from PIL import Image
from typing import List

# Package Imports
import blockletter


class PassGenerator:    
    
    def __init__(self, fields: List[str], version: int=0):
        """PassGen class constructor"""
        
        self.fields = fields
        self.version = version
    
    def make_display_str(self, values: List[str]):
        """Make the hash string that is used to identify the pass"""
        
        assert(len(values) == len(self.fields))
        disp_str = ""
        for (fld, val) in zip(self.fields, values):
            if fld[0] == '_':
                if fld[1] == '_':
                    pass
                else:
                    disp_str += '{}\n'.format(val.strip())
            else:
                disp_str += "{}: {}\n".format(fld.strip(), val.strip())
                
        return disp_str
    
    def generate_hash(self, values: List[str], keyword: str=''):
        """Generate a sha256 hash of the display string, with an optional
        keyword for security"""
        disp_str = self.make_display_str(values)
        disp_str += keyword
        encoded_str = blockletter.encode(disp_str)
        h = hashlib.sha256()
        h.update(encoded_str)
        return h.digest()
        
    def validate_hash(self, values: List[str], check_hash: bytes, \
            keyword: str=""):
        """Validate a hash hex digest against the given data"""
        
        gen_hash = self.generate_hash(values, keyword)
        return (gen_hash == check_hash)
        
    def generate_qr(self, values: List[str], keyword: str='', \
            block_size: int=10, save_path: str=''):
        """Generate a QR code representing the hashed info of the pass and 
        protocol version"""
        
        gen_hash = self.generate_hash(values, keyword)
        qr_data = bytes(numpy.uint32([self.version])) + gen_hash
        qr_img = qrcode.make(qr_data, box_size=block_size)
        if save_path:
            qr_img.save(save_path)
        else:
            return qr_img

    def generate_pass(self, values: List[str], keyword: str='', \
            qr_size: int=10, txt_size: int=10, txt_width: int=10, \
            save_path: str=''):
        """Generate a pass image that can be validated with the associated
        phone app"""
        
        qr_img = self.generate_qr(values, keyword, qr_size)
        txt_img = Image.fromarray(blockletter.convert_phrase(
            self.make_display_str(values), txt_width, txt_size), 'P')
        height = max((qr_img.size[1], txt_img.size[1] + 4*qr_size)) + qr_size*4
        width = qr_img.size[0] + txt_img.size[0] + qr_size*4
        background = Image.new('1', (width, height), color=0x000000)
        pass_img = Image.new('1', (width-2*qr_size, height-2*qr_size), color=0xFFFFFF)
        pass_img.paste(qr_img, (qr_size, qr_size))
        pass_img.paste(txt_img, (qr_size + qr_img.size[0], qr_size + 4*qr_size))
        background.paste(pass_img, (qr_size, qr_size))
        if save_path:
            background.save(save_path)
        else:
            return background
                