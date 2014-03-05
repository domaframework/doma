#!/usr/bin/env python

from livereload import Server, shell

server = Server()
server.watch('sources/**.rst', shell('make html'))
server.serve()
