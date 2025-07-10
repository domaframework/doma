#!/usr/bin/env python3
"""
Convert RST files to Markdown format for Sphinx with MyST parser support.
"""

import os
import re
import glob
from pathlib import Path


def convert_rst_to_md(rst_content):
    """Convert RST content to Markdown format."""
    
    # Convert titles and headers
    lines = rst_content.split('\n')
    converted_lines = []
    i = 0
    
    while i < len(lines):
        line = lines[i]
        
        # Check if next line is a title underline
        if i + 1 < len(lines):
            next_line = lines[i + 1]
            
            # Title with = underline
            if re.match(r'^=+$', next_line) and len(next_line) >= len(line.strip()):
                converted_lines.append(f'# {line.strip()}')
                i += 2  # Skip the underline
                continue
            
            # Section with - underline  
            elif re.match(r'^-+$', next_line) and len(next_line) >= len(line.strip()):
                converted_lines.append(f'## {line.strip()}')
                i += 2  # Skip the underline
                continue
            
            # Subsection with ~ underline
            elif re.match(r'^~+$', next_line) and len(next_line) >= len(line.strip()):
                converted_lines.append(f'### {line.strip()}')
                i += 2  # Skip the underline
                continue
            
            # Subsubsection with ^ underline
            elif re.match(r'^\^+$', next_line) and len(next_line) >= len(line.strip()):
                converted_lines.append(f'#### {line.strip()}')
                i += 2  # Skip the underline
                continue
        
        # Convert directives
        line = convert_directives(line)
        
        # Convert inline markup
        line = convert_inline_markup(line)
        
        converted_lines.append(line)
        i += 1
    
    return '\n'.join(converted_lines)


def convert_directives(line):
    """Convert RST directives to Markdown/MyST equivalents."""
    
    # Image directive
    if line.strip().startswith('.. image::'):
        image_path = line.strip().replace('.. image::', '').strip()
        return f'![Image]({image_path})'
    
    # Code block directive
    if line.strip().startswith('.. code-block::'):
        lang = line.strip().replace('.. code-block::', '').strip()
        if lang:
            return f'```{lang}'
        else:
            return '```'
    
    # Note directive
    if line.strip().startswith('.. note::'):
        return '```{note}'
    
    # Warning directive
    if line.strip().startswith('.. warning::'):
        return '```{warning}'
    
    # Important directive
    if line.strip().startswith('.. important::'):
        return '```{important}'
    
    # Admonition directive
    admonition_match = re.match(r'^.. admonition::\s*(.*)$', line.strip())
    if admonition_match:
        title = admonition_match.group(1)
        return f'```{{admonition}} {title}'
    
    # Toctree directive
    if line.strip().startswith('.. toctree::'):
        return '```{toctree}'
    
    # End of directive block
    if line.strip() == '' and len(line) > 0:
        return line
    
    return line


def convert_inline_markup(line):
    """Convert RST inline markup to Markdown."""
    
    # Bold text
    line = re.sub(r'\*\*(.*?)\*\*', r'**\1**', line)
    
    # Italic text
    line = re.sub(r'\*(.*?)\*', r'*\1*', line)
    
    # Inline code
    line = re.sub(r'``(.*?)``', r'`\1`', line)
    
    # External links
    line = re.sub(r'`([^<]+) <([^>]+)>`_', r'[\1](\2)', line)
    
    # Internal references
    line = re.sub(r':ref:`([^`]+)`', r'[\\1](\\1)', line)
    line = re.sub(r':doc:`([^`]+)`', r'[\\1](\\1)', line)
    
    return line


def convert_file(rst_file_path):
    """Convert a single RST file to Markdown."""
    
    with open(rst_file_path, 'r', encoding='utf-8') as f:
        rst_content = f.read()
    
    # Convert content
    md_content = convert_rst_to_md(rst_content)
    
    # Create MD file path
    md_file_path = rst_file_path.replace('.rst', '.md')
    
    # Write converted content
    with open(md_file_path, 'w', encoding='utf-8') as f:
        f.write(md_content)
    
    print(f'Converted: {rst_file_path} -> {md_file_path}')


def main():
    """Convert all RST files in the docs directory."""
    
    # Get all RST files
    rst_files = glob.glob('**/*.rst', recursive=True)
    
    print(f'Found {len(rst_files)} RST files to convert')
    
    for rst_file in rst_files:
        convert_file(rst_file)
    
    print('Conversion completed!')


if __name__ == '__main__':
    main()