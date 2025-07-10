#!/usr/bin/env python3
"""
RST to Markdown/MyST converter for Doma documentation
Converts RST files to Markdown format while preserving Sphinx directives
"""

import os
import re
import sys
from pathlib import Path
from typing import Dict, List, Tuple, Optional


class RSTToMarkdownConverter:
    def __init__(self):
        self.title_markers = {
            '=': {'level': 1, 'markdown': '#'},
            '-': {'level': 2, 'markdown': '##'},
            '~': {'level': 3, 'markdown': '###'},
            '^': {'level': 4, 'markdown': '####'},
            '"': {'level': 5, 'markdown': '#####'},
            "'": {'level': 6, 'markdown': '######'},
        }
        
    def convert_title(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST title to Markdown format"""
        if index >= len(lines) - 1:
            return lines[index], index
            
        current_line = lines[index].strip()
        next_line = lines[index + 1].strip() if index + 1 < len(lines) else ""
        
        # Check for overline-underline title format
        if (index > 0 and 
            lines[index - 1].strip() and 
            len(set(lines[index - 1].strip())) == 1 and
            lines[index - 1].strip()[0] in self.title_markers and
            next_line and 
            len(set(next_line)) == 1 and
            next_line[0] in self.title_markers):
            
            marker = next_line[0]
            if marker in self.title_markers:
                level = self.title_markers[marker]['markdown']
                return f"{level} {current_line}", index + 1
        
        # Check for underline-only title format
        if (next_line and 
            len(set(next_line)) == 1 and 
            next_line[0] in self.title_markers and
            len(next_line) >= len(current_line)):
            
            marker = next_line[0]
            if marker in self.title_markers:
                level = self.title_markers[marker]['markdown']
                return f"{level} {current_line}", index + 1
        
        # Check if current line is a title marker line (should be skipped)
        if (current_line and 
            len(set(current_line)) == 1 and 
            current_line[0] in self.title_markers and
            index > 0 and 
            lines[index - 1].strip()):
            # This is an underline, skip it
            return "", index
                
        return lines[index], index
        
    def convert_inline_markup(self, text: str) -> str:
        """Convert RST inline markup to Markdown"""
        # Convert strong emphasis **text** (keep as-is)
        # Convert emphasis *text* (keep as-is)
        # Convert inline code ``text`` to `text`
        text = re.sub(r'``([^`]+)``', r'`\1`', text)
        
        # Convert interpreted text `text` to `text`
        text = re.sub(r'(?<!`)`([^`]+)`(?!`)', r'`\1`', text)
        
        # Convert references :ref:`label` to [label](label)
        text = re.sub(r':ref:`([^`]+)`', r'[\1](\1)', text)
        
        # Convert doc references :doc:`path` to [path](path)
        text = re.sub(r':doc:`([^`]+)`', r'[\1](\1)', text)
        
        # Convert external links `text <url>`_ to [text](url)
        text = re.sub(r'`([^<]+)<([^>]+)>`_', r'[\1](\2)', text)
        
        return text
        
    def convert_code_block(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST code-block directive to Markdown"""
        if not lines[index].strip().startswith('.. code-block::'):
            return lines[index], index
            
        # Extract language from code-block directive
        match = re.match(r'\.\.\ code-block::\ *(\w+)?', lines[index].strip())
        language = match.group(1) if match and match.group(1) else ""
        
        result = [f"```{language}"]
        i = index + 1
        
        # Skip empty lines and options
        while i < len(lines) and (not lines[i].strip() or lines[i].startswith('   :')):
            i += 1
            
        # Collect code content
        while i < len(lines) and (lines[i].startswith('  ') or not lines[i].strip()):
            if lines[i].strip():
                # Remove the common indentation (usually 2 spaces)
                code_line = lines[i][2:] if lines[i].startswith('  ') else lines[i]
                result.append(code_line.rstrip())
            else:
                result.append('')
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_toctree(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST toctree to MyST toctree"""
        if not lines[index].strip().startswith('.. toctree::'):
            return lines[index], index
            
        result = ["```{toctree}"]
        i = index + 1
        
        # Process toctree options
        while i < len(lines) and lines[i].startswith('   :'):
            option = lines[i].strip()[1:]  # Remove leading ':'
            result.append(f":{option}")
            i += 1
            
        # Skip empty line after options
        if i < len(lines) and not lines[i].strip():
            i += 1
            
        # Process toctree entries
        while i < len(lines) and (lines[i].startswith('   ') or not lines[i].strip()):
            if lines[i].strip():
                entry = lines[i].strip()
                result.append(entry)
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_image(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST image directive to MyST"""
        if not lines[index].strip().startswith('.. image::'):
            return lines[index], index
            
        # Extract image path
        match = re.match(r'\.\.\ image::\ *(.+)', lines[index].strip())
        image_path = match.group(1) if match else ""
        
        result = [f"```{{image}} {image_path}"]
        i = index + 1
        
        # Process image options
        while i < len(lines) and lines[i].startswith('   :'):
            option = lines[i].strip()[1:]  # Remove leading ':'
            result.append(f":{option}")
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_note(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST note directive to MyST"""
        if not lines[index].strip().startswith('.. note::'):
            return lines[index], index
            
        result = ["```{note}"]
        i = index + 1
        
        # Skip empty line after directive
        if i < len(lines) and not lines[i].strip():
            i += 1
            
        # Process note content
        while i < len(lines) and (lines[i].startswith('  ') or not lines[i].strip()):
            if lines[i].strip():
                content = lines[i][2:] if lines[i].startswith('  ') else lines[i]
                result.append(content.rstrip())
            else:
                result.append('')
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_admonition(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST admonition directive to MyST"""
        match = re.match(r'\.\.\ admonition::\ *(.+)', lines[index].strip())
        if not match:
            return lines[index], index
            
        title = match.group(1)
        result = [f"```{{admonition}} {title}"]
        i = index + 1
        
        # Process admonition options (like :class:)
        while i < len(lines) and lines[i].startswith('  :'):
            option = lines[i].strip()[1:]  # Remove leading ':'
            result.append(f":{option}")
            i += 1
            
        # Skip empty line after options
        if i < len(lines) and not lines[i].strip():
            i += 1
            
        # Process admonition content
        while i < len(lines) and (lines[i].startswith('  ') or not lines[i].strip()):
            if lines[i].strip():
                content = lines[i][2:] if lines[i].startswith('  ') else lines[i]
                result.append(content.rstrip())
            else:
                result.append('')
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_tabs(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST tabs directive to MyST"""
        if not lines[index].strip().startswith('.. tabs::'):
            return lines[index], index
            
        result = ["````{tabs}"]
        i = index + 1
        
        # Skip empty line after tabs directive
        if i < len(lines) and not lines[i].strip():
            i += 1
            
        # Process tab items
        while i < len(lines):
            line = lines[i]
            
            # Check for end of tabs block - when we encounter non-indented content
            if not line.startswith('    ') and line.strip():
                # Unless it's a tab directive at the same level
                if not line.strip().startswith('.. tab::'):
                    break
                    
            if line.strip().startswith('.. tab::'):
                # Extract tab title
                tab_match = re.match(r'\.\.\ tab::\ *(.+)', line.strip())
                tab_title = tab_match.group(1) if tab_match else ""
                result.append(f"```{{tab}} {tab_title}")
                i += 1
                
                # Skip empty line after tab directive
                if i < len(lines) and not lines[i].strip():
                    i += 1
                    
                # Process tab content
                while i < len(lines):
                    current_line = lines[i]
                    
                    # Check if we've reached the next tab
                    if current_line.strip().startswith('.. tab::'):
                        break
                    
                    # Check if we've reached the end of tabs block
                    if not current_line.startswith('    ') and current_line.strip():
                        # Check if this line is not part of the tabs structure
                        if not current_line.strip().startswith('.. tab::'):
                            # This signals the end of the tabs block
                            break
                        
                    if current_line.strip():
                        # Handle nested directives within tabs
                        if current_line.strip().startswith('.. code-block::'):
                            # Create a temporary list for processing the code block
                            temp_lines = []
                            temp_i = i
                            while temp_i < len(lines) and (lines[temp_i].startswith('    ') or not lines[temp_i].strip()):
                                # Remove the tab indentation (4 spaces) to process the code block
                                if lines[temp_i].startswith('    '):
                                    temp_lines.append(lines[temp_i][4:])
                                else:
                                    temp_lines.append(lines[temp_i])
                                temp_i += 1
                            
                            # Convert the code block
                            code_result, _ = self.convert_code_block(temp_lines, 0)
                            result.append(code_result)
                            i = temp_i
                            continue
                        else:
                            content = current_line[4:] if current_line.startswith('    ') else current_line
                            result.append(content.rstrip())
                    else:
                        result.append('')
                    i += 1
                    
                result.append("```")
                # Continue to check for next tab
                
            elif line.startswith('    ') or not line.strip():
                # Skip indented content that's not part of a tab
                i += 1
            else:
                # End of tabs block
                break
                
        result.append("````")
        return '\n'.join(result), i - 1
        
    def convert_contents(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST contents directive to MyST"""
        if not lines[index].strip().startswith('.. contents::'):
            return lines[index], index
            
        result = ["```{contents}"]
        i = index + 1
        
        # Process contents options
        while i < len(lines) and lines[i].startswith('   :'):
            option = lines[i].strip()[1:]  # Remove leading ':'
            result.append(f":{option}")
            i += 1
            
        result.append("```")
        return '\n'.join(result), i - 1
        
    def convert_field_list(self, lines: List[str], index: int) -> Tuple[str, int]:
        """Convert RST field list to Markdown"""
        if not lines[index].strip().startswith(':'):
            return lines[index], index
            
        result = []
        i = index
        
        while i < len(lines) and lines[i].strip().startswith(':'):
            match = re.match(r':([^:]+):\s*(.*)', lines[i].strip())
            if match:
                field_name = match.group(1)
                field_value = match.group(2)
                result.append(f"**{field_name}**: {field_value}")
            i += 1
            
        return '\n'.join(result), i - 1
        
    def convert_file(self, input_path: str, output_path: str) -> None:
        """Convert a single RST file to Markdown"""
        with open(input_path, 'r', encoding='utf-8') as f:
            content = f.read()
            
        lines = content.splitlines()
        result = []
        i = 0
        
        while i < len(lines):
            line = lines[i]
            
            # Skip comments
            if line.strip().startswith('..') and not any(directive in line for directive in 
                ['.. code-block::', '.. image::', '.. note::', '.. toctree::', 
                 '.. tabs::', '.. tab::', '.. admonition::', '.. contents::']):
                i += 1
                continue
                
            # Convert directives
            if line.strip().startswith('.. code-block::'):
                converted, i = self.convert_code_block(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. toctree::'):
                converted, i = self.convert_toctree(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. image::'):
                converted, i = self.convert_image(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. note::'):
                converted, i = self.convert_note(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. admonition::'):
                converted, i = self.convert_admonition(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. tabs::'):
                converted, i = self.convert_tabs(lines, i)
                result.append(converted)
            elif line.strip().startswith('.. contents::'):
                converted, i = self.convert_contents(lines, i)
                result.append(converted)
            elif line.strip().startswith(':') and ':' in line.strip()[1:]:
                converted, i = self.convert_field_list(lines, i)
                result.append(converted)
            else:
                # Convert titles
                converted, i = self.convert_title(lines, i)
                # Convert inline markup
                converted = self.convert_inline_markup(converted)
                result.append(converted)
                
            i += 1
            
        # Write output
        os.makedirs(os.path.dirname(output_path), exist_ok=True)
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(result))
            
    def convert_directory(self, docs_dir: str) -> None:
        """Convert all RST files in a directory"""
        docs_path = Path(docs_dir)
        
        # Find all RST files
        rst_files = list(docs_path.rglob('*.rst'))
        
        print(f"Found {len(rst_files)} RST files to convert")
        
        for rst_file in rst_files:
            # Calculate output path
            relative_path = rst_file.relative_to(docs_path)
            output_path = docs_path / relative_path.with_suffix('.md')
            
            print(f"Converting {rst_file} -> {output_path}")
            
            try:
                self.convert_file(str(rst_file), str(output_path))
                print(f"✓ Successfully converted {rst_file.name}")
            except Exception as e:
                print(f"✗ Error converting {rst_file.name}: {e}")
                
        print(f"\nConversion complete! Converted {len(rst_files)} files.")


def main():
    if len(sys.argv) < 2:
        print("Usage: python rst_to_markdown_converter.py <docs_directory>")
        sys.exit(1)
        
    docs_dir = sys.argv[1]
    
    if not os.path.isdir(docs_dir):
        print(f"Error: Directory '{docs_dir}' does not exist")
        sys.exit(1)
        
    converter = RSTToMarkdownConverter()
    converter.convert_directory(docs_dir)


if __name__ == "__main__":
    main()