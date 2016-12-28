#$ -j y
STR=$(echo $1 | sed -e 's/\r//g')
java XMLOperations "$STR"
