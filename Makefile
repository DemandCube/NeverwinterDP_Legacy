# make help
# @ surpresses the output of the line
help:
	@make -qp | awk -F':' '/^[a-zA-Z0-9][^$$#\/\t=]*:([^=]|$$)/ {split($$1,A,/ /);for(i in A)print A[i]}'

# Original is in github-flow

# raw to commandline execute
# make -qp | awk -F':' '/^[a-zA-Z0-9][^$#\/\t=]*:([^=]|$)/ {split($1,A,/ /);for(i in A)print A[i]}' 


# Will run gradle clean on every project part of neverwinterdp
clean:
	./neverwinterdp.sh gradle clean

# Will try to build every component for neverwinterdp
compile:
	./neverwinterdp.sh gradle clean build install

# Will git clone all the dependent projects
checkout-dependencies:
	./neverwinterdp.sh checkout

# Generate the javadoc in
#       $build/docs/javadoc/
docs:
        ./gradlew javadoc
                
gradle-help:
        ./gradlew -q tasks

