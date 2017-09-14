#!/bin/bash
#
# Collection of shared functions
#

function die {
    echo -e "$@"
    exit 1
}

function safe {
    "$@"
    local status=$?
    if [ $status -ne 0 ]; then
        die "\nBUILD FAILED\nWhen invoking \"$@\"\n" >&2
    fi
    return $status
}

function version_gt() {
    test "$(printf '%s\n' "$@" | sort -V | head -n 1)" != "$1";
}

function get_version_name {
    echo $(grep -e "versionName '" $1 | cut -d"'" -f2)
}

function get_version_code {
    echo $(grep -e "versionCode " $1 | cut -d" " -f10 ) # VERY FLAKY!
}

function fail_build {
    die "\nBUILD FAILED\n"
}
