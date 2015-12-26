wget apimeme.com -o /dev/null -O- | grep option | cut -d">" -f2| cut -d"<" -f 1|while read line; do
	echo \"$line\",
done
