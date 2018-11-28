#!/bin/bash
#
# Collection of shared functions
#

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[0;33m'
NC='\033[0m' # No Color

function approve {
    echo -e "${GREEN}$@${NC}"
}

function warn {
    echo -e "${YELLOW}$@${NC}"
}

function die {
    echo -e "${RED}$@${NC}"
    exit 1
}

function safe {
    "$@"
    local status=$?
    if [[ ${status} -ne 0 ]]; then
        die "\nBUILD FAILED\nWhen invoking \"$@\"\n" >&2
    fi
    return ${status}
}