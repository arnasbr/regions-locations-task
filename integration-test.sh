#!/bin/bash

# Execute sbt command
sbt "run match --locations Input/test1/locations.json --regions Input/test1/regions.json --output Output/results.json"

# Compare the output to the expected output
diff src/main/resources/output/results.json src/main/resources/expectedResults.json

# Check the result of diff (0 means no differences, 1 means differences)
if [ $? -eq 0 ]
then
    echo "The test passed successfully, the output matches the expected results."
else
    echo "The test failed, the output does not match the expected results."
fi
