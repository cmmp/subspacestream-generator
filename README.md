This is a Java implementation of a subspace stream generator.

It generates 2-d geometric objects embedded in higher dimensional data streams. No overlapping among clusters is guaranteed.

Noise IS added to the stream in the form of irrelevant dimensions for different clusters. Noise is modeled as coming from a Uniform distribution. Noise IS NOT mixed directly with clusters, a boundary is guaranteed.

To run the program you will need [Maven](http://maven.apache.org/) installed.

You will also need [myutils-java](https://github.com/cmmp/javautils) locally installed. Clone and install it before using this project. The other dependencies will be fetched automatically by Maven.

At the project directory run:

`mvn install`

A sample script `run.sh` indicates how to generate a stream.

