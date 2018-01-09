#!/bin/bash
datetime=$(date +'%Y %m %d %H:%M:%S')

java -Djava.ext.dirs=lib -cp . com.keunsy.oneutil.OneObjectUtil
echo "$datetime stop" >> restart.log
