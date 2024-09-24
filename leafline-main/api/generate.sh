#!/usr/bin/env bash
# Generate a Java client from the server OpenAPI specification.
# =============================================================

OPENAPI_GENERATOR_CLI='npx --package @openapitools/openapi-generator-cli openapi-generator-cli'

# Clean previously generated code
rm -rf e2etests/src/main
rm -rf e2etests/docs

# Configure Java generation
export JAVA_OPTS='-DapiTests=false -DmodelTests=false'

# Generate Java client code
$OPENAPI_GENERATOR_CLI generate -p hideGenerationTimestamp=true -i serverapi.yaml -g java -o ./e2etests
