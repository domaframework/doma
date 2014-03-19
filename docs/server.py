#!/usr/bin/env python

from livereload import Server, shell

server = Server()
server.watch('sources/*', shell('make dirhtml'))
server.watch('sources/**/*', shell('make dirhtml'))
server.serve()
