#!/bin/bash

M2="C:/Users/Cássio/.m2/repository"   #/home/cassio/.m2/repository
#CP=target/classes;$M2/org/pentaho/pentaho-commons/pentaho-package-manager/1.0.8/pentaho-package-manager-1.0.8.jar;$M2/commons-cli/commons-cli/1.2/commons-cli-1.2.jar;$M2/nz/ac/waikato/cms/weka/weka-dev/3.7.11/weka-dev-3.7.11.jar;$M2/org/apache/commons/commons-math3/3.2/commons-math3-3.2.jar;../myutils/target/classes
CP="target/classes;$M2/org/pentaho/pentaho-commons/pentaho-package-manager/1.0.8/pentaho-package-manager-1.0.8.jar;$M2/commons-cli/commons-cli/1.2/commons-cli-1.2.jar;$M2/nz/ac/waikato/cms/weka/weka-dev/3.7.11/weka-dev-3.7.11.jar;$M2/org/apache/commons/commons-math3/3.2/commons-math3-3.2.jar;../myutils/target/classes"

# lambda = 0.0002 -> at w = 5000 prob of model 1 is 0.5
# 2^(-0.0002 * 5000) = 0.5

NDIM=20
java -Xmx2G -cp $CP br.fapesp.subspacestream.SubspaceStreamGenerator -a 4 -d $NDIM -v -N 100000 -l 0.0001
java -cp $CP  weka.core.converters.CSVSaver -i subspace_stream.arff -o subspace_stream.csv
matlab  -noFigureWindows -automation -wait -nosplash -nojvm -nodesktop -r "run('C:/Users/Cássio/Dropbox/workspace/subspacestream/convert.m'); exit;"

