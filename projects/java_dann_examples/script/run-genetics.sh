#!/bin/sh
java -Xmx128m -classpath ../build/jar/dann_examples.jar:../../java_dann/build/jar/dann.jar:../lib/freehep-java3d-2.0.jar:../lib/j3dcore.jar:../lib/vecmath.jar:../lib/j3dutils.jar com.syncleus.core.dann.examples.test.Test3d
