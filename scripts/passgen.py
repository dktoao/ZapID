""" passgen.py
Class containing function to setup and generate pass elements
"""

# Library Imports
import qrcode

from PIL import Image

# Package Imports
import blockletter


class PassGenerator:
    """Class that stores the layout variables for pass generation"""

    def __init__(self, width: int=200, height: int=100, qr_block: int=2,
                 txt_block: int=2, logo_block: int=1, border_block: int=1,
                 qr_pad: int=4):

        # Input parameters
        self.width = width
        self.height = height
        self.qr_block = qr_block
        self.txt_block = txt_block
        self.logo_block = logo_block
        self.border_block = border_block
        self.qr_pad = qr_pad

        # Calculated parameters
        width_pad = 6*self.border_block
        self.inner_width = self.width - width_pad
        height_pad = 6*self.border_block
        self.inner_height = self.height - height_pad

    def generate(self, message: str, code: str):

        # Generate the logo for the top of the QR code
        logo_img = Image.fromarray(
            blockletter.convert_phrase('ZAPID.TECH', 10, self.logo_block), 'P'
        )
        logo_width, logo_height = logo_img.size

        # Generate the QR Code and make sure that it fits in the inner box
        qr_img = qrcode.make(code, box_size=self.qr_block, border=self.qr_pad)
        qr_width, qr_height = qr_img.size
        if (logo_height + qr_height) > self.inner_height:
            raise Exception('QR Code height does not fit within pass '
                            'boundaries')
        code_logo_width = max(logo_width, qr_width)
        if (code_logo_width + self.txt_block*6) > self.inner_width:
            raise Exception('QR Code width does not fit within pass '
                            'boundaries')

        # Calculate the number of allowable text characters
        txt_width = self.inner_width - code_logo_width - self.txt_block*6
        txt_char_width = txt_width // self.txt_block // 6
        txt_img = Image.fromarray(
            blockletter.convert_phrase(
                message, txt_char_width, self.txt_block
            ), 'P'
        )
        message_width, message_height = txt_img.size
        if self.inner_height < message_height:
            raise Exception('Block letters height does not fit within pass '
                            'boundaries')
        if txt_width < message_width:
            raise Exception('Block letters width does not fit within pass '
                            'boundaries')
        inner_img = Image.new(
            '1', (self.inner_width, self.inner_height), color=0xFFFFFF
        )
        inner_img.paste(logo_img, (0, 0))
        inner_img.paste(qr_img, (0, logo_height))
        inner_img.paste(txt_img, (code_logo_width+self.txt_block*6, 0))

        # Put together the whole pass image
        pass_img = Image.new(
            '1', (self.width, self.height), color=0xFFFFFF
        )
        border_img = Image.new(
            '1', (self.width - 2*self.border_block,
                  self.height - 2*self.border_block),
            color=0x000000
        )
        border_inner_img = Image.new(
            '1', (self.width - 4*self.border_block,
                  self.height - 4*self.border_block),
            color=0xFFFFFF
        )
        pass_img.paste(border_img, (self.border_block, self.border_block))
        pass_img.paste(
            border_inner_img, (2*self.border_block, 2*self.border_block)
        )
        pass_img.paste(inner_img, (3*self.border_block, 3*self.border_block))

        return pass_img
