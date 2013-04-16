Meta-Mash
=========

About
-----
This is a utility to convert audio files of multiple formats and qualities to mp3 files, all of the same quality. 
Ogg is partially supported as an output format as well.

In addition, YAML files can be used to specify metadata, which will be added to the output files as ID3 tags.
Cover art can also be added. Existing ID3 tags, that are not overridden by YAML data, should pass through untouched.

Libav is used as a backend for all the conversions.

All the work on this project so far was done in one weekend at [ub.hacking](http://2013.ubhacking.com/), 
so it's still very much a work in progess.

Ideas and Todos
---------------
- detect and skip conversion of files already in the output folder
- support more ID3 tags
- have one YAML file apply to many audio files, and define the precedence of tags
- create a visualizer for these YAML hierarchies

Requirements
-----------
- Java 1.6 or later
- Libav (and libmp3lame for mp3 output, libvorbis for ogg)

