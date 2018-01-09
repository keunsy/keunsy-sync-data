#!/bin/bash
datetime=$(date +'%Y %m %d %H:%M:%S')

java -Djava.ext.dirs=lib -cp . com.OneObjectUtil
echo "$datetime stop" >> restart.log
