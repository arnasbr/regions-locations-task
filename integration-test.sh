#!/bin/bash

# Execute sbt command
sbt "run match --locations src/main/resources/input/locations.json
--regions src/main/resources/input/regions.json --output src/main/resources/output/results.json"

# Compare the output to the expected output
diff src/main/resources/output/results.json src/main/resources/expectedResults.json

# Check the result of diff (0 means no differences, 1 means differences)
if [ $? -eq 0 ]
then
    echo "The test passed successfully, the output matches the expected results."
    exit 0
else
    echo "The test failed, the output does not match the expected results."
    exit 1
fi
