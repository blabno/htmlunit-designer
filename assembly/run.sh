#!/bin/bash
CWD=`pwd`
cd `dirname $0`/lib
LIBS=`ls *.jar -1`
CLASSPATH="."
for LIB in $LIBS ; do
	CLASSPATH=$CLASSPATH:lib/$LIB
done
cd $CWD
java -cp $CLASSPATH pl.com.it_crowd.htmlunit_designer.MainFrame
