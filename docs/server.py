#!/usr/bin/env python

from livereload import Server, shell

server = Server()
server.watch('sources/*.rst', shell('make dirhtml'))
server.watch('sources/**/*.rst', shell('make dirhtml'))
server.serve()
