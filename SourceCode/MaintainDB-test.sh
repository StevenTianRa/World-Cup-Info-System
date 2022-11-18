#!/bin/sh

CLASSDIR="$(pwd)"

MTEST1_DATDIR="$(pwd)/Database/ProductionDataset/"
cd $MTEST1_DATDIR
db2 -stvf connectCS348.sql
db2 -stvf droptables.sql
db2 -stvf createtables.sql
db2 -stvf populatetables.sql

### Testing script for CS348 W18 A2
cd $CLASSDIR/Solutions

## Compile source code
CLASSFILE1="MaintainDB.class"
CLASSFILE2="QueryDB.class"
if [ \( -f $CLASSFILE1 \) -a \( -f $CLASSFILE2 \) ] 
then
   rm $CLASSFILE1
   rm $CLASSFILE2
   echo "Clean class files"
fi

cd $CLASSDIR
chmod +x compile
./compile 
java MaintainDB
